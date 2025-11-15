package es.pocketrainer.servicio.usuario;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import es.pocketrainer.modelo.usuario.Rol;
import es.pocketrainer.modelo.usuario.Usuario;
import es.pocketrainer.repositorio.cliente.ClienteRepositorio;
import es.pocketrainer.repositorio.entrenador.EntrenadorRepositorio;
import es.pocketrainer.repositorio.usuario.UsuarioRepositorio;
import es.pocketrainer.seguridad.UsuarioSesion;
import es.pocketrainer.servicio.notificaciones.NotificacionServicio;

@Service
public class UsuarioServicioImpl implements UsuarioServicio {

	private static Logger LOGGER = LoggerFactory.getLogger(UsuarioServicioImpl.class);

	@Resource
	private UsuarioRepositorio usuarioRepositorio;

	@Resource
	private ClienteRepositorio clienteRepositorio;

	@Resource
	private EntrenadorRepositorio entrenadorRepositorio;

	@Resource
	private PasswordEncoder encoder;
	@Autowired
	private AuthenticationManager authenticationManager;

	@Resource
	private NotificacionServicio notificacionServicio;

//	@Autowired
//	public void lazySetAuthenticationManager(AuthenticationManager authenticationManager) {
//		this.authenticationManager = authenticationManager;
//	}

	@Override
	public UsuarioSesion loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario usuario = usuarioRepositorio.buscarUsuarioPorUsuarioNombre(username);

		if (usuario != null) {
			return new UsuarioSesion(username, usuario.getPassword(), usuario.getId(), getAuthorities(usuario.getRolLista()),
					clienteRepositorio.buscarClientePorUsuarioId(usuario.getId()), entrenadorRepositorio.buscarEntrenadorActivoPorUsuarioId(usuario.getId()));
		} else {
			LOGGER.error("Error iniciando sesion: Usuario no encontrado");
			throw new UsernameNotFoundException("Usuario no encontrado");
		}
	}

	@Override
	public Usuario buscarUsuarioPorUsuarioNombre(String usuarioNombre) {
		return usuarioRepositorio.buscarUsuarioPorUsuarioNombre(usuarioNombre);
	}

	@Override
	public void crearNuevoUsuario(Usuario usuario) {
		usuario.setPassword(encoder.encode(usuario.getPassword()));
		usuarioRepositorio.save(usuario);
	}

	/**
	 * Unifica en una sola lista de authority los roles y permisos asociados a estos
	 * 
	 * @param rolLista la lista de roles
	 * @return la colecion de authority
	 */
	private Collection<? extends GrantedAuthority> getAuthorities(Collection<Rol> rolLista) {
		List<GrantedAuthority> authorities = new ArrayList<>();
		for (Rol rol : rolLista) {
			authorities.add(new SimpleGrantedAuthority(rol.getCodigo()));
			rol.getPermisoLista().stream().map(p -> new SimpleGrantedAuthority(p.getNombre())).forEach(authorities::add);
		}

		return authorities;
	}

	@Override
	public Boolean existeUsuario(Usuario usuario) {
		return usuarioRepositorio.buscarUsuarioPorUsuarioNombre(usuario.getUsuarioNombre()) != null;
	}

	@Override
	public boolean comprobarPassword(String password) {
		boolean correcto = true;

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authentication.getName(), password));
		} catch (Exception e) {
			correcto = false;
		}

		return correcto;
	}

	@Override
	public void cambiarPassword(String password) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		var usuario = buscarUsuarioPorUsuarioNombre(authentication.getName());
		usuario.setPassword(encoder.encode(password));
		usuarioRepositorio.save(usuario);
	}

	@Override
	public void recuperarPassword(String correo) {
		if (StringUtils.isNotBlank(correo)) {
			var usuario = buscarUsuarioPorUsuarioNombre(correo);
			if (usuario != null) {
				// Generar y actualizar password
				String nuevaPassword = generateCommonLangPassword();
				usuario.setPassword(encoder.encode(nuevaPassword));
				usuarioRepositorio.save(usuario);

				// Enviar
				notificacionServicio.notificarGeneracionNuevaPassword(usuario, nuevaPassword);
			}
		}
	}

	@Override
	public String generateCommonLangPassword() {

//		o prohibo aki el % o modifico el firewall, en google vienen entrados buscando el error
//		n: The request was rejected because the URL contained a potentially malicious String "%25" at 
//		
		String upperCaseLetters = RandomStringUtils.random(2, 65, 90, true, true);
		String lowerCaseLetters = RandomStringUtils.random(2, 97, 122, true, true);
		String numbers = RandomStringUtils.randomNumeric(2);
		String specialChar = RandomStringUtils.random(2, 33, 47, false, false);
		String totalChars = RandomStringUtils.randomAlphanumeric(2);
		String combinedChars = upperCaseLetters.concat(lowerCaseLetters).concat(numbers).concat(specialChar).concat(totalChars);
		List<Character> pwdChars = combinedChars.chars().mapToObj(c -> (char) c).collect(Collectors.toList());
		Collections.shuffle(pwdChars);
		String password = pwdChars.stream().collect(StringBuilder::new, StringBuilder::append, StringBuilder::append).toString();
		return password;
	}

}
