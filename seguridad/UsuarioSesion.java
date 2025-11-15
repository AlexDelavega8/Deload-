package es.pocketrainer.seguridad;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import es.pocketrainer.modelo.cliente.Cliente;
import es.pocketrainer.modelo.entrenador.Entrenador;

public class UsuarioSesion extends User {

	private static Logger LOGGER = LoggerFactory.getLogger(UsuarioSesion.class);
	private static final long serialVersionUID = 4516034154258115320L;

	private Long id;
	private String nombre;
	private String apellido1;
	private String apellido2;
	private Long entrenadorId;
	private Long clienteId;

	public UsuarioSesion(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired,
			boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
		super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
	}

	public UsuarioSesion(String username, String password, Long id, Collection<? extends GrantedAuthority> authorities, Cliente cliente,
			Entrenador entrenador) {
		super(username, password, authorities);
		this.id = id;
		if (cliente != null) {
			nombre = cliente.getNombre();
			apellido1 = cliente.getApellido1();
			apellido2 = cliente.getApellido2();
			clienteId = cliente.getId();
		} else if (entrenador != null) {
			nombre = entrenador.getNombre();
			apellido1 = entrenador.getApellido1();
			apellido2 = entrenador.getApellido2();
			entrenadorId = entrenador.getId();
		} else {
			LOGGER.error("Error iniciando sesion: El usuario no pertenece ni a un cliente ni a un entrenador");
			throw new UsernameNotFoundException("El usuario no partenece ni a un cliente ni a un entrenador");
		}
	}

	public boolean tieneRol(String codigo) {
		return getAuthorities().stream().anyMatch((grantedAuthority) -> grantedAuthority.getAuthority().equals(codigo));
	}

	public boolean tienePermiso(String codigo) {
		return getAuthorities().stream().anyMatch((grantedAuthority) -> grantedAuthority.getAuthority().equals(codigo));
	}

	public Long getId() {
		return id;
	}

	public String getNombre() {
		return nombre;
	}

	public String getApellido1() {
		return apellido1;
	}

	public String getApellido2() {
		return apellido2;
	}

	public Long getEntrenadorId() {
		return entrenadorId;
	}

	public Long getClienteId() {
		return clienteId;
	}

	@Override
	public String toString() {
		return "UsuarioSesion [" + (id != null ? "id=" + id + ", " : "") + (nombre != null ? "nombre=" + nombre + ", " : "")
				+ (apellido1 != null ? "apellido1=" + apellido1 + ", " : "") + (apellido2 != null ? "apellido2=" + apellido2 + ", " : "")
				+ (entrenadorId != null ? "entrenadorId=" + entrenadorId + ", " : "") + (clienteId != null ? "clienteId=" + clienteId : "") + "]";
	}

}
