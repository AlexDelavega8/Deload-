package es.pocketrainer.servicio.entrenador;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import es.pocketrainer.formulario.BuscarEntrenadorFormulario;
import es.pocketrainer.modelo.entrenador.Entrenador;
import es.pocketrainer.repositorio.entrenador.EntrenadorRepositorio;
import es.pocketrainer.repositorio.usuario.RolRepositorio;
import es.pocketrainer.servicio.notificaciones.NotificacionServicio;
import es.pocketrainer.servicio.usuario.UsuarioServicio;
import es.pocketrainer.servicio.valormaestro.ValorMaestroServicio;
import es.pocketrainer.util.Constantes;
import es.pocketrainer.util.Constantes.EntrenadorEstadoEnum;
import es.pocketrainer.util.Constantes.RolEnum;

@Service
public class EntrenadorServicioImpl implements EntrenadorServicio {

	@Resource
	private EntrenadorRepositorio entrenadorRepositorio;

	@Resource
	private PasswordEncoder encoder;

	@Resource
	private RolRepositorio rolRepositorio;

	@Resource
	private UsuarioServicio usuarioServicio;

	@Resource
	private NotificacionServicio notificacionServicio;

	@Resource
	private ValorMaestroServicio valorMaestroServicio;

	@Override
	public List<Entrenador> buscarEntrenadorActivoTodos() {
		return entrenadorRepositorio.findByEstado(valorMaestroServicio.buscarValorMaestroPorCodigo(EntrenadorEstadoEnum.ACTIVO.codigo()));
	}

	@Override
	public List<Entrenador> buscarEntrenadorTodos() {
		return entrenadorRepositorio.findAll();
	}

	@Override
	public Entrenador buscarEntrenadorPorId(Long entrenadorId) {
		return entrenadorRepositorio.findById(entrenadorId).get();
	}

	@Override
	public Entrenador buscarAdministrador() {
		return entrenadorRepositorio.buscarEntrenadorPorEmail(Constantes.CORREO_ADMINISTRADOR);
	}

	@Override
	public void revisarTrabajoDeEntrenador() {
		// TODO Auto-generated method stub

	}

	@Transactional
	@Override
	public void crearEntrenadorNuevo(Entrenador entrenador) {
		entrenador.setCodigo(entrenador.getNombre().concat(" ").concat(entrenador.getApellido1())
				.concat((entrenador.getApellido2() != null ? " " + entrenador.getApellido2() : "")));
		entrenador.setFechaRegistroInicial(ZonedDateTime.now());
		entrenador.setEmail(entrenador.getUsuario().getUsuarioNombre());
		var passwordNueva = usuarioServicio.generateCommonLangPassword();
		entrenador.getUsuario().setPassword(encoder.encode(passwordNueva));
		entrenador.getUsuario().setRolLista(Set.of(rolRepositorio.buscarRolPorCodigo(RolEnum.ENTRENADOR.codigo())));
		entrenador.setEstado(valorMaestroServicio.buscarValorMaestroPorCodigo(EntrenadorEstadoEnum.ACTIVO.codigo()));
		notificacionServicio.notificarEntrenadorCreado(entrenador, passwordNueva);

		entrenadorRepositorio.save(entrenador);
	}

	@Transactional
	@Override
	public void darBajaEntrenadorAccion(Long entrenadorId) {
		var entrenador = buscarEntrenadorPorId(entrenadorId);
		entrenador.setEstado(valorMaestroServicio.buscarValorMaestroPorCodigo(EntrenadorEstadoEnum.BAJA.codigo()));
	}

	@Transactional
	@Override
	public void activarEntrenadorAccion(Long entrenadorId) {
		var entrenador = buscarEntrenadorPorId(entrenadorId);
		entrenador.setEstado(valorMaestroServicio.buscarValorMaestroPorCodigo(EntrenadorEstadoEnum.ACTIVO.codigo()));

	}

	@Override
	public List<Entrenador> buscarEntrenadorPorFiltro(BuscarEntrenadorFormulario buscarEntrenadorFormulario) {
		return entrenadorRepositorio.buscarEntrenadorPorFiltro(buscarEntrenadorFormulario);
	}

}
