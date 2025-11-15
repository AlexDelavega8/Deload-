package es.pocketrainer.servicio.cliente;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.pocketrainer.formulario.BuscarClienteFormulario;
import es.pocketrainer.modelo.cliente.Cliente;
import es.pocketrainer.modelo.cliente.ClienteConfiguracion;
import es.pocketrainer.modelo.cliente.ClienteParq;
import es.pocketrainer.modelo.cliente.Suscripcion;
import es.pocketrainer.modelo.cobro.SuscripcionCobro;
import es.pocketrainer.modelo.rutina.Rutina;
import es.pocketrainer.modelo.valormaestro.ValorMaestro;
import es.pocketrainer.modelo.videoconferencia.Videoconferencia;
import es.pocketrainer.repositorio.cliente.ClienteConfiguracionRepositorio;
import es.pocketrainer.repositorio.cliente.ClienteRepositorio;
import es.pocketrainer.repositorio.servicio.ServicioRepositorio;
import es.pocketrainer.repositorio.usuario.RolRepositorio;
import es.pocketrainer.repositorio.usuario.UsuarioRepositorio;
import es.pocketrainer.servicio.cobro.CobroServicio;
import es.pocketrainer.servicio.entrenador.EntrenadorServicio;
import es.pocketrainer.servicio.notificaciones.NotificacionServicio;
import es.pocketrainer.servicio.parametrizacion.ParametroConfiguracionServicio;
import es.pocketrainer.servicio.rutina.RutinaServicio;
import es.pocketrainer.servicio.usuario.UsuarioServicio;
import es.pocketrainer.servicio.valormaestro.ValorMaestroServicio;
import es.pocketrainer.servicio.videoconferencia.VideoconferenciaServicio;
import es.pocketrainer.util.Constantes;
import es.pocketrainer.util.Constantes.ClienteEstadoEnum;
import es.pocketrainer.util.Constantes.RolEnum;
import es.pocketrainer.util.Constantes.SuscripcionEstadoEnum;
import es.pocketrainer.util.Constantes.ValorMaestroTipoEnum;
import es.pocketrainer.util.Constantes.VideoconferenciaEstadoEnum;
import es.pocketrainer.util.ConstantesGlobales;
import es.pocketrainer.util.ErrorUtil;

@Service
public class ClienteServicioImpl implements ClienteServicio {

	private static Logger LOGGER = LoggerFactory.getLogger(ClienteServicioImpl.class);

	@Value("${entorno}")
	private String entorno;

	@Resource
	private ParametroConfiguracionServicio parametroConfiguracionServicio;

	@Resource
	private ClienteConfiguracionRepositorio clienteConfiguracionRepositorio;

	@Resource
	private UsuarioServicio usuarioServicio;

	@Resource
	private CobroServicio cobroServicio;

	@Resource
	private ClienteRepositorio clienteRepositorio;

	@Resource
	private UsuarioRepositorio usuarioRepositorio;

	@Resource
	private RutinaServicio rutinaServicio;

	@Resource
	private EntrenadorServicio entrenadorServicio;

	@Resource
	private PasswordEncoder encoder;

	@Resource
	private RolRepositorio rolRepositorio;

	@Resource
	private ValorMaestroServicio valorMaestroServicio;

	@Resource
	private NotificacionServicio notificacionServicio;

	@Resource
	private VideoconferenciaServicio videoconferenciaServicio;

	@Resource
	private ServicioRepositorio servicioRepositorio;

	@Transactional
	@Override
	public List<Cliente> buscarClienteTodos() {
		return clienteRepositorio.findAll();
	}

	@Transactional
	@Override
	public List<Cliente> buscarClientePorEntrenador(Long entrenadorId) {
		return clienteRepositorio.buscarClientePorEntrenador(entrenadorId);
	}

	@Transactional
	@Override
	public Cliente buscarClientePorId(Long clienteId) {
		return clienteRepositorio.findById(clienteId).get();
	}

	@Transactional
	@Override
	public void enviarMensajeEntrenador(Long clienteId, String mensaje) {
		Cliente cliente = buscarClientePorId(clienteId);
		notificacionServicio.notificarMensajeDeCliente(cliente, mensaje);
	}

	@Transactional
	@Override
	public void crearClienteNuevo(Cliente cliente) {
		// Crear suscripcion
		var suscripcion = new Suscripcion();
		var fecharReferencia = ZonedDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
		suscripcion.setCliente(cliente);

		// De primeras se considera impagado y que con baja en dos dias. Esto cambia al
		// confirmarse pago inicial.
		suscripcion.setEstado(valorMaestroServicio.buscarValorMaestroPorCodigo(SuscripcionEstadoEnum.IMPAGADA_INICIAL.codigo()));
		suscripcion.setFechaBaja(ZonedDateTime.now().plusDays(2));

		// La fecha de primera suscripción y fecha de última activación que sean exactas
		suscripcion.setFechaPrimeraSuscripcion(ZonedDateTime.now());
		suscripcion.setFechaUltimaActivacion(ZonedDateTime.now());
		suscripcion.setMesesActiva(0);
		suscripcion.setFechaPeriodoDesde(calcularProximaSuscripcionFechaPeriodoDesde(suscripcion));
		suscripcion.setFechaPeriodoHasta(calcularProximaSuscripcionFechaPeriodoHasta(suscripcion));
		suscripcion.setMesesActiva(1);

		suscripcion.setServicio(servicioRepositorio.findById(1L).get());
		cliente.setSuscripcion(suscripcion);

		// Crear las cuatro rutinas
		cliente.addRutina(rutinaServicio.prepararRutinaNueva(cliente, fecharReferencia));
		cliente.addRutina(rutinaServicio.prepararRutinaNueva(cliente, fecharReferencia));
		cliente.addRutina(rutinaServicio.prepararRutinaNueva(cliente, fecharReferencia));
		cliente.addRutina(rutinaServicio.prepararRutinaNueva(cliente, fecharReferencia));

		// Crear una video conferencia
		cliente.addVideoconferencia(videoconferenciaServicio.prepararVideoconferenciaNueva());

		// Parq
		for (ValorMaestro parq : valorMaestroServicio.buscarValorMaestroListaPorCodigoTipo(ValorMaestroTipoEnum.PARQ)) {
			var clienteParq = new ClienteParq();
			clienteParq.setParq(parq);
			cliente.addClienteParq(clienteParq);
		}

		// Resto de campos
		cliente.setFechaRegistroInicial(ZonedDateTime.now());
		cliente.setCodigo(cliente.getNombre().concat(" ").concat(cliente.getApellido1()).concat((cliente.getApellido2() != null ? " " + cliente.getApellido2() : "")));
		cliente.setEstado(valorMaestroServicio.buscarValorMaestroPorCodigo(ClienteEstadoEnum.ACTIVO.codigo()));
		cliente.setEmail(cliente.getUsuario().getUsuarioNombre());

		// Encriptar contraseña
		cliente.getUsuario().setPassword(encoder.encode(cliente.getUsuario().getPassword()));
		cliente.getUsuario().setRolLista(Set.of(rolRepositorio.buscarRolPorCodigo(RolEnum.CLIENTE.codigo())));

		// Textos legales
		cliente.setAceptaPoliticaPrivacidad(Boolean.TRUE);
		cliente.setFechaAceptaPoliticaPrivacidad(ZonedDateTime.now());
		cliente.setAceptaTerminosCondiciones(Boolean.TRUE);
		cliente.setFechaAceptaTerminosCondiciones(ZonedDateTime.now());

		// Configuracion general sin valores
		var clienteConfiguracion = new ClienteConfiguracion();
		clienteConfiguracion.setCliente(cliente);
		cliente.setClienteConfiguracion(clienteConfiguracion);

		// Guardar
		clienteRepositorio.saveAndFlush(cliente);

		// Notificar por correo al cliente
		notificacionServicio.notificarRegistroCorrecto(cliente);

		// Notificar por correo al gestor
		notificacionServicio.notificarNuevoCliente(entrenadorServicio.buscarAdministrador(), cliente);
	}

	@Transactional
	@Override
	public void actualizarCliente(Cliente cliente) {
		clienteRepositorio.save(cliente);
	}

	@Transactional
	@Override
	public Cliente actualizarClienteDesdeMisDatosPagina(Cliente cliente) {
		Cliente clienteActual = buscarClientePorId(cliente.getId());
		clienteActual
				.setCodigo(cliente.getNombre().concat(" ").concat(cliente.getApellido1()).concat((cliente.getApellido2() != null ? " " + cliente.getApellido2() : "")));
		clienteActual.setNombre(cliente.getNombre());
		clienteActual.setApellido1(cliente.getApellido1());
		clienteActual.setApellido2(cliente.getApellido2());
		clienteActual.setNif(cliente.getNif());
		clienteActual.setSexo(cliente.getSexo() != null ? cliente.getSexo() : null);
		clienteActual.setFechaNacimiento(cliente.getFechaNacimiento());
		clienteActual.setEmail(cliente.getUsuario().getUsuarioNombre());
		clienteActual.setTelefono(cliente.getTelefono());
		clienteActual.setDisponibilidadTelefonica(cliente.getDisponibilidadTelefonica());
		clienteActual.setDomicilio(cliente.getDomicilio());
		clienteActual.setCodigoPostal(cliente.getCodigoPostal());
		clienteActual.setLocalidad(cliente.getLocalidad());
		clienteActual.setPais(cliente.getPais());
		clienteActual.setObjetivos(cliente.getObjetivos());
		clienteActual.setEntrenaActualmente(cliente.getEntrenaActualmente());
		clienteActual.setActividadFisicaActual(cliente.getActividadFisicaActual());
		clienteActual.setClavesEntrenamiento(cliente.getClavesEntrenamiento());
		clienteActual.setLesiones(cliente.getLesiones());
		clienteActual.setAltura(cliente.getAltura());
		clienteActual.setPeso(cliente.getPeso());

		actualizarSet(clienteActual.getDiaEntrenamientoLista(), cliente.getDiaEntrenamientoLista());
		actualizarSet(clienteActual.getLugarEntrenamientoLista(), cliente.getLugarEntrenamientoLista());
		actualizarSet(clienteActual.getPerfilLista(), cliente.getPerfilLista());

		if (clienteActual.getMedioConocimientoLista() == null) {
			clienteActual.setMedioConocimientoLista(cliente.getMedioConocimientoLista());
		} else {
			actualizarSet(clienteActual.getMedioConocimientoLista(), cliente.getMedioConocimientoLista());
		}

		if (clienteActual.getMaterialLista() == null) {
			clienteActual.setMaterialLista(cliente.getMaterialLista());
		} else {
			actualizarSet(clienteActual.getMaterialLista(), cliente.getMaterialLista());
		}

		for (ClienteParq clienteParqAnterior : clienteActual.getClienteParqLista()) {
			for (ClienteParq clienteParq : cliente.getClienteParqLista()) {
				if (clienteParq.getParq().getCodigo().equals(clienteParqAnterior.getParq().getCodigo())) {
					clienteParqAnterior.setRespuesta(clienteParq.getRespuesta());
					break;
				}
			}
		}

		clienteActual.getUsuario().setUsuarioNombre(cliente.getUsuario().getUsuarioNombre());

		if (clienteActual.getEntrenador() != null) {
			notificacionServicio.notificarClienteCambiaDatos(clienteActual);
		}

		return clienteRepositorio.save(clienteActual);
	}

	@Transactional
	@Override
	public void asignarEntrenador(Long clienteId, Long entrenadorId) {

		var entrenador = entrenadorServicio.buscarEntrenadorPorId(entrenadorId);
		var cliente = buscarClientePorId(clienteId);

		var entrenadorAnterior = cliente.getEntrenador();
		cliente.setEntrenador(entrenador);

		if (entrenadorAnterior == null) {
			// Notificar al cliente primer entrenador
			notificacionServicio.notificarEntrenadorAsignado(cliente);
		} else {
			// Notificar al cliente cambio
			notificacionServicio.notificarEntrenadorCambiado(cliente);
		}

		// Actualizar entrenador asociado al ultimo cobro
		cliente.getSuscripcion().getSuscripcionCobroLista().sort((it1, it2) -> it2.getFechaHoraEmision().compareTo(it1.getFechaHoraEmision()));
		cliente.getSuscripcion().getSuscripcionCobroLista().get(0).setEntrenador(entrenador);

		// Notificar al entrenador
		notificacionServicio.notificarClienteAsignado(cliente);
	}

	@Transactional
	@Override
	public void programarVideoconferencia(Long clienteId, Long videoconferenciaId, ZonedDateTime fechaHoraProgramada) {

		Videoconferencia videoconferencia = videoconferenciaServicio.buscarVideoConferenciaPorId(videoconferenciaId);
		var fechaAnterior = videoconferencia.getFechaHoraProgramada();
		videoconferencia.setFechaHoraProgramada(fechaHoraProgramada);

		if (fechaHoraProgramada != null) {
			if (videoconferencia.getEstado().getCodigo().equals(VideoconferenciaEstadoEnum.SIN_PROGRAMAR.codigo())) {
				LOGGER.info("Programando videoconferencia para las " + fechaHoraProgramada + " para el cliente " + videoconferencia.getCliente().getNombreCompleto());
				videoconferencia.setEstado(valorMaestroServicio.buscarValorMaestroPorCodigo(VideoconferenciaEstadoEnum.PROGRAMADA.codigo()));
				videoconferencia.setEntrenador(videoconferencia.getCliente().getEntrenador());
				notificacionServicio.notificarClienteVideoconferenciaProgramada(videoconferencia);
			} else {
				LOGGER.info("Reprogramando videoconferencia para las " + fechaHoraProgramada + " para el cliente " + videoconferencia.getCliente().getNombreCompleto());
				videoconferencia.setEstado(valorMaestroServicio.buscarValorMaestroPorCodigo(VideoconferenciaEstadoEnum.PROGRAMADA.codigo()));
				notificacionServicio.notificarClienteVideoconferenciaReprogramada(videoconferencia);
			}
		} else {
			videoconferencia.setEstado(valorMaestroServicio.buscarValorMaestroPorCodigo(VideoconferenciaEstadoEnum.SIN_PROGRAMAR.codigo()));
			if (fechaAnterior != null) {
				notificacionServicio.notificarClienteVideoconferenciaCancelada(videoconferencia, fechaAnterior);
			}
		}

	}

	@Transactional
	@Override
	public void cancelarSuscripcion(Long clienteId) {
		Cliente cliente = buscarClientePorId(clienteId);
		cliente.getSuscripcion().setFechaBaja(cliente.getSuscripcion().getFechaPeriodoHasta());
		cliente.getSuscripcion().setEstado(valorMaestroServicio.buscarValorMaestroPorCodigo(SuscripcionEstadoEnum.CANCELADA.codigo()));
		cliente.setEstado(valorMaestroServicio.buscarValorMaestroPorCodigo(ClienteEstadoEnum.PENDIENTE_BAJA.codigo()));
		// Notificar a cliente y entrenador
		notificacionServicio.notificarSuscripcionCancelada(cliente);
		notificacionServicio.notificarClienteCancelaSuscripcion(entrenadorServicio.buscarAdministrador(), cliente);

	}

	@Transactional
	@Override
	public void suspenderCancelacionSuscripcion(Long clienteId) {
		Cliente cliente = buscarClientePorId(clienteId);
		cliente.getSuscripcion().setFechaBaja(null);
		cliente.getSuscripcion().setEstado(valorMaestroServicio.buscarValorMaestroPorCodigo(SuscripcionEstadoEnum.ACTIVA.codigo()));
		cliente.setEstado(valorMaestroServicio.buscarValorMaestroPorCodigo(ClienteEstadoEnum.ACTIVO.codigo()));
	}

	@Transactional
	@Override
	public Cliente reactivarSuscripcion(Long clienteId) {
		var cliente = buscarClientePorId(clienteId);
		var suscripcion = cliente.getSuscripcion();

		// Solo reactivar si está caducada, para evitar problemas si consiguen llegar al
		// botón de reactivar como sea (dandole atrás al explorador, varias pestañas...)
		// tras haber pagado ya
		if (suscripcion.getEstado().getCodigo().equals(SuscripcionEstadoEnum.CADUCADA.codigo())) {
			var fechaReferencia = ZonedDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);

			// Recuperar ultimas rutinas que nunca hizo y poner fecha creacion ahora
			var rutinaLista = rutinaServicio.buscarRutinaPorClienteFechaCreacion(clienteId, suscripcion.getFechaPeriodoDesde());
			rutinaLista.forEach(it -> it.setFechaCreacion(fechaReferencia));

			// La fecha de última activación que sea exacta
			suscripcion.setFechaUltimaActivacion(ZonedDateTime.now());
			suscripcion.setMesesActiva(0);
			suscripcion.setFechaPeriodoDesde(calcularProximaSuscripcionFechaPeriodoDesde(suscripcion));
			suscripcion.setFechaPeriodoHasta(calcularProximaSuscripcionFechaPeriodoHasta(suscripcion));
			suscripcion.setMesesActiva(1);
			suscripcion.setEstado(valorMaestroServicio.buscarValorMaestroPorCodigo(SuscripcionEstadoEnum.IMPAGADA.codigo()));
			suscripcion.setFechaBaja(ZonedDateTime.now().plusDays(2));

			cliente.setEstado(valorMaestroServicio.buscarValorMaestroPorCodigo(ClienteEstadoEnum.ACTIVO.codigo()));
			notificacionServicio.notificarSuscripcionReactivada(cliente);
			notificacionServicio.notificarClienteReactivaSuscripcion(entrenadorServicio.buscarAdministrador(), cliente);
		}

		return cliente;
	}

	/**
	 * <pre>
	 * 1.Detectar renovaciones, intentar cobro:
	 * 		Cambiar estados, establecer fecha limite de baja para que este mismo proceso lo detecte otro dia y la de debaja definitiva.
	 * 		Intentar cobro:
	 * 			Cobro ok: Todos felices, se pone de nuevo la suscripcion ACTIVA y se quita fecha de baja
	 *      	Cobro ko: Por error en ese instante o por que diretamente se sabia que la tarjeta estaba caducada.
	 *      	
	 * 2.Detectar caducada por impago en renovación
	 * 		Caso concreto donde tiene pendiente pago de renovacion por que en el punto 1 no se pudo cobrar y ahora es la fecha de baja fin.
	 * 3.Detectar caducada por impago inicial
	 * 		Caso concreto donde se registro y hubo problemas de pago. Aquí hay que poner en el proceso inicial esa fecha limite de pago, es decir, la fecha de baja para que aquí
	 *      se ELIMINE todo, no se da de baja por que realmente nunca tuvo nada.
	 * 4.Detectar caducada por cancelación: Simplemente estaba cancelada y por tanto no se renueva.
	 * 
	 * 
	 * </pre>
	 */
	@Transactional
	@Override
	public void revisarSuscripciones() {
		var fechaReferencia = ZonedDateTime.now();

		for (Cliente cliente : buscarClienteTodos()) {
			try {
				LOGGER.info("Revisando suscripcion de " + cliente.getNombreCompleto());
				var suscripcion = cliente.getSuscripcion();

				// Renovacion normal: Intentar cobro
				if (esSuscripcionParaRenovar(suscripcion, fechaReferencia)) {
					LOGGER.info("Suscripcion renovada: " + cliente.getNombreCompleto());
					renovarSuscripcion(suscripcion);
				}

				// Caduca por no haber hecho pago inicial: Borrar todo
				if (esSuscripcionCaducadaPorImpagoInicial(suscripcion, fechaReferencia)) {
					LOGGER.info("Borrando cliente por no pagar la suscripción inicial. Cliente: Nombre: " + cliente.getNombreCompleto() + " | Telefono: "
							+ cliente.getTelefono() + " | Mail: " + cliente.getEmail());
					clienteRepositorio.delete(cliente);
				}

				// Caduca por no haber hecho pago en renovacion: Caducada
				if (esSuscripcionCaducadaPorImpagoRenovacion(suscripcion, fechaReferencia)) {
					LOGGER.info("Suscripcion caducada por impago en renovacion: " + cliente.getNombreCompleto());
					cliente.setEstado(valorMaestroServicio.buscarValorMaestroPorCodigo(ClienteEstadoEnum.BAJA.codigo()));
					suscripcion.setEstado(valorMaestroServicio.buscarValorMaestroPorCodigo(SuscripcionEstadoEnum.CADUCADA.codigo()));
					notificacionServicio.notificarClienteBaja(entrenadorServicio.buscarAdministrador(), cliente);
					notificacionServicio.notificarFinSuscripcion(cliente);
					cliente.setEntrenador(null);
				}

				// Caduca porque el cliente cancelo: Caducada
				if (esSuscripcionCaducadaPorCancelacion(suscripcion, fechaReferencia)) {
					LOGGER.info("Suscripcion caducada por cancelacion: " + cliente.getNombreCompleto());
					cliente.setEstado(valorMaestroServicio.buscarValorMaestroPorCodigo(ClienteEstadoEnum.BAJA.codigo()));
					suscripcion.setEstado(valorMaestroServicio.buscarValorMaestroPorCodigo(SuscripcionEstadoEnum.CADUCADA.codigo()));
					notificacionServicio.notificarClienteBaja(entrenadorServicio.buscarAdministrador(), cliente);
					notificacionServicio.notificarFinSuscripcion(cliente);
					cliente.setEntrenador(null);
				}
			} catch (Exception e) {
				var error = "Error revisando suscripcion de: " + cliente.getNombreCompleto();
				LOGGER.error(error, e);
				Map<String, Object> datosError = new HashMap<>();
				datosError.put("urlError", "revisarSuscripciones");
				datosError.put("error", error);
				datosError.put("entorno", entorno);
				datosError.put("sesion", "Sistema");
				datosError.put("errorTraza", ErrorUtil.generarExceptionStackTrace(e));
				datosError.put("fecha", ZonedDateTime.now());
				notificacionServicio.notificarError(datosError);
			}
		}
	}

	@Override
	public void realizarCobrosRenovacionesSuscripciones() {
		// Fijarse que este metodo no abre transacion
		for (SuscripcionCobro suscripcionCobro : cobroServicio.buscarSuscripcionCobroPendiente()) {
			try {
				LOGGER.info("Cobrando renovacion de suscripcion: " + suscripcionCobro.getSuscripcion().getCliente().getNombreCompleto());
				// Cada llamada abre una transaccion independiente
				cobroServicio.generarSolicitudRenovacion(suscripcionCobro.getId());
			} catch (Exception e) {
				var error = "Error cobrando renovacion de suscripcion: " + suscripcionCobro.getSuscripcion().getCliente().getNombreCompleto();
				LOGGER.error(error, e);
				Map<String, Object> datosError = new HashMap<>();
				datosError.put("urlError", "realizarCobrosRenovacionesSuscripciones");
				datosError.put("error", error);
				datosError.put("entorno", entorno);
				datosError.put("sesion", "Sistema");
				datosError.put("errorTraza", ErrorUtil.generarExceptionStackTrace(e));
				datosError.put("fecha", ZonedDateTime.now());
				notificacionServicio.notificarError(datosError);
			}

		}
	}

	/**
	 * Ver {@link ClienteServicioImpl#renovarSuscripcion} para ver la fecha que se
	 * usa como renovación, que sucederá despuùes de esta preparación de
	 * renovaciones
	 */
	@Transactional
	@Override
	public void prepararRenovaciones() {

		// Se preparan 5 días antes de la renovacion
		List<Cliente> clienteLista = buscarClientesProximosARenovar();

		for (Cliente cliente : clienteLista) {
			LOGGER.info("Cliente proximo a renovar: " + cliente.getCodigo());
			// Crear rutinas nuevas en base inicio de proxima renvoacion
			var fecharReferencia = calcularProximaSuscripcionFechaPeriodoDesde(cliente.getSuscripcion());

			// Revisar que no tenga ya preparada la renovacion
			var renovacionYaPreparada = false;
			for (Rutina rutina : cliente.getRutinaLista()) {
				if (rutina.getFechaCreacion().isEqual(fecharReferencia)) {
					LOGGER.info("La renovacion ya fue preparada anteriormente para el cliente: " + cliente.getCodigo());
					renovacionYaPreparada = true;
					break;
				}
			}

			if (!renovacionYaPreparada) {
				LOGGER.info("Preparando nuevas rutinas y videoconferencia para renovacion del cliente: " + cliente.getCodigo());
				cliente.addRutina(rutinaServicio.prepararRutinaNueva(cliente, fecharReferencia));
				cliente.addRutina(rutinaServicio.prepararRutinaNueva(cliente, fecharReferencia));
				cliente.addRutina(rutinaServicio.prepararRutinaNueva(cliente, fecharReferencia));
				cliente.addRutina(rutinaServicio.prepararRutinaNueva(cliente, fecharReferencia));

				// Crear video conferencia nueva y caducar la anterior independientemente de que
				// fuera usada
				var videoConferenciaSinUsar = cliente.getVideoConferenciaLista().stream()
						.filter(it -> !it.getEstado().getCodigo().equals(VideoconferenciaEstadoEnum.CADUCADA.codigo())).findFirst().orElse(null);
				if (videoConferenciaSinUsar != null) {
					videoConferenciaSinUsar.setEstado(valorMaestroServicio.buscarValorMaestroPorCodigo(VideoconferenciaEstadoEnum.CADUCADA.codigo()));
				}
				cliente.addVideoconferencia(videoconferenciaServicio.prepararVideoconferenciaNueva());
				notificacionServicio.notificarClienteRenovacionSuscripcionPreparada(cliente);
				// Tiempo debido ante errores enviando mails, aparentemente por ser muy rapidos
				try {
					Thread.sleep(Constantes.TIEMPO_ESPERA_RENOVACIONES);
				} catch (InterruptedException e) {
					LOGGER.error("Error grave durante sleep en proceso de renovación del cliente " + cliente.getCodigo(), e);
				}
				if (cliente.getSuscripcion().getEstado().getCodigo().equals(SuscripcionEstadoEnum.ACTIVA.codigo())
						|| cliente.getSuscripcion().getEstado().getCodigo().equals(SuscripcionEstadoEnum.ACTIVA_NUEVA.codigo())) {
					notificacionServicio.notificarClienteRenovacionProxima(cliente);
				}
			}
		}
	}

	/**
	 * <pre>
	 * Ejemplo:
	 * 	Suscripción actual: 02/03 00:00:00 a 01/04 23:59:59, tiene un mes activa y la fecha de última renovación es 02/03 17:00:00
	 *  Renovación        : 02/04 00:00:00 a 01/05 23:59:59, tiene dos meses activa y la fecha de última renovación continua igual
	 * </pre>
	 * 
	 * @param suscripcion
	 */
	private void renovarSuscripcion(Suscripcion suscripcion) {

		suscripcion.setFechaPeriodoDesde(calcularProximaSuscripcionFechaPeriodoDesde(suscripcion));
		suscripcion.setFechaPeriodoHasta(calcularProximaSuscripcionFechaPeriodoHasta(suscripcion));
		suscripcion.setMesesActiva(suscripcion.getMesesActiva() + 1);

		// Controlar suscripcion especial Antonio - cliente 2 - suscripcion 1
		// if (!suscripcion.getId().equals(Constantes.SUSCRIPCION_PRUEBA)) {

		// Solo renovar de verdad en entorno produccion
		if (entorno.equals(ConstantesGlobales.ENTORNO_PRO)) {
			suscripcion.setEstado(valorMaestroServicio.buscarValorMaestroPorCodigo(SuscripcionEstadoEnum.IMPAGADA.codigo()));
			suscripcion.setFechaBaja(suscripcion.getFechaPeriodoDesde().plusDays(2));
			cobroServicio.generarSuscripcionCobroRenovacion(suscripcion);
		} else {
			cobroServicio.generarSuscripcionCobroRenovacionYaPagada(suscripcion);
		}

	}

	/**
	 * Suscripcion para renovar:
	 * <ul>
	 * <li>Estado activa nueva o activa</li>
	 * <li>Fecha fin de periodo anterior a la fecha de referencia</li>
	 * </ul>
	 * 
	 * @param suscripcion
	 * @param fechaReferencia
	 * @return verdadero si es una suscripcion para renovar, falso en otro caso
	 */
	private boolean esSuscripcionParaRenovar(Suscripcion suscripcion, ZonedDateTime fechaReferencia) {
		return (suscripcion.getEstado().getCodigo().equals(SuscripcionEstadoEnum.ACTIVA_NUEVA.codigo())
				|| suscripcion.getEstado().getCodigo().equals(SuscripcionEstadoEnum.ACTIVA.codigo())) && suscripcion.getFechaPeriodoHasta().isBefore(fechaReferencia);
	}

	/**
	 * Suscripcion caducada por cancelacion:
	 * <ul>
	 * <li>Estado cancelada</li>
	 * <li>Tiene fecha de baja y es anterior a la fecha de referencia</li>
	 * </ul>
	 * 
	 * @param suscripcion     la suscripcion a revisar
	 * @param fechaReferencia la fecha de revision
	 * @return verdadero si es una suscripcion caducada por cancelacion
	 */
	private boolean esSuscripcionCaducadaPorCancelacion(Suscripcion suscripcion, ZonedDateTime fechaReferencia) {
		LOGGER.debug("Revisando si es suscripcion cancelada: " + suscripcion.getCliente().getNombreCompleto() + " | Estado: " + suscripcion.getEstado().getNombre()
				+ " | Fecha baja: " + (suscripcion.getFechaBaja() != null ? suscripcion.getFechaBaja() : "Sin fecha baja"));
		return suscripcion.getEstado().getCodigo().equals(SuscripcionEstadoEnum.CANCELADA.codigo()) && suscripcion.getFechaBaja() != null
				&& suscripcion.getFechaBaja().isBefore(fechaReferencia);
	}

	/**
	 * Suscripcion caducada por impago en renovacion:
	 * <ul>
	 * <li>Estado impagada</li>
	 * <li>Tiene fecha de baja y es anterior a la fecha de referencia</li>
	 * </ul>
	 * 
	 * @param suscripcion     la suscripcion a revisar
	 * @param fechaReferencia la fecha de revision
	 * @return verdadero si es uan suscripcion caducada por impago en renovación
	 */
	private boolean esSuscripcionCaducadaPorImpagoRenovacion(Suscripcion suscripcion, ZonedDateTime fechaReferencia) {
		LOGGER.debug("Revisando si es suscripcion impagada en renovacion: " + suscripcion.getCliente().getNombreCompleto() + " | Estado: "
				+ suscripcion.getEstado().getNombre() + " | Fecha baja: " + (suscripcion.getFechaBaja() != null ? suscripcion.getFechaBaja() : "Sin fecha baja"));
		return suscripcion.getEstado().getCodigo().equals(SuscripcionEstadoEnum.IMPAGADA.codigo()) && suscripcion.getFechaBaja() != null
				&& suscripcion.getFechaBaja().isBefore(fechaReferencia);
	}

	/**
	 * Suscripcion caducada por impago inicial:
	 * <ul>
	 * <li>Estado impagada inicial</li>
	 * <li>Tiene fecha de baja y es anterior a la fecha de referencia</li>
	 * </ul>
	 * 
	 * @param suscripcion     la suscripcion a revisar
	 * @param fechaReferencia la fecha de revision
	 * @return verdadero si es una suscripcion caducada por impago inicial
	 */
	private boolean esSuscripcionCaducadaPorImpagoInicial(Suscripcion suscripcion, ZonedDateTime fechaReferencia) {
		LOGGER.debug("Revisando si es suscripcion impagada inicialmente: " + suscripcion.getCliente().getNombreCompleto() + " | Estado: "
				+ suscripcion.getEstado().getNombre() + " | Fecha baja: " + (suscripcion.getFechaBaja() != null ? suscripcion.getFechaBaja() : "Sin fecha baja"));
		return suscripcion.getEstado().getCodigo().equals(SuscripcionEstadoEnum.IMPAGADA_INICIAL.codigo()) && suscripcion.getFechaBaja() != null
				&& suscripcion.getFechaBaja().isBefore(fechaReferencia);
	}

	private List<Cliente> buscarClientesProximosARenovar() {
		return clienteRepositorio.buscarClienteActivoLista().stream().filter(it -> it.getSuscripcion().getFechaPeriodoHasta().plusDays(-5).isBefore(ZonedDateTime.now()))
				.collect(Collectors.toList());
	}

	private <T> void actualizarSet(Set<T> original, Set<T> nuevo) {
		if (nuevo == null && original != null) {
			original.clear();
		} else {
			original.addAll(nuevo);
			original.retainAll(nuevo);
		}
	}

	@Override
	public List<Cliente> buscarClientePorFiltro(BuscarClienteFormulario buscarClienteFormulario) {
		return clienteRepositorio.buscarClientePorFiltro(buscarClienteFormulario);
	}

	@Transactional
	@Override
	public void corregirFechasSuscripciones() {
		// Clientes activos
		var buscarClienteFormulario = new BuscarClienteFormulario();
		buscarClienteFormulario.setEstadoCodigoLista(List.of(ClienteEstadoEnum.ACTIVO.codigo(), ClienteEstadoEnum.PENDIENTE_BAJA.codigo()));
		var clienteActivoLista = buscarClientePorFiltro(buscarClienteFormulario);

		// Cambiar fechas de suscripciones y rutinas
		for (Cliente cliente : clienteActivoLista) {

			// Primero, encontrar rutinas actuales y posibles futuro
			final var fechaInicioFuturo = cliente.getSuscripcion().getFechaUltimaActivacion().plusMonths(cliente.getSuscripcion().getMesesActiva()).withHour(0)
					.withMinute(0).withSecond(0);

			var rutinaActualLista = cliente.getRutinaLista().parallelStream().filter(it -> it.getFechaCreacion().isEqual(cliente.getSuscripcion().getFechaPeriodoDesde()))
					.collect(Collectors.toList());
			var rutinaFuturoLista = cliente.getRutinaLista().parallelStream().filter(it -> it.getFechaCreacion().isEqual(fechaInicioFuturo)).collect(Collectors.toList());

			// Corregir rutinas
			for (Rutina rutina : rutinaActualLista) {
				rutina.setFechaCreacion(rutina.getFechaCreacion().withHour(0).withMinute(0).withSecond(0));
			}
			for (Rutina rutina : rutinaFuturoLista) {
				rutina.setFechaCreacion(rutina.getFechaCreacion().withHour(0).withMinute(0).withSecond(0));
			}

			cliente.getSuscripcion().setFechaPeriodoDesde(cliente.getSuscripcion().getFechaPeriodoDesde().withHour(0).withMinute(0).withSecond(0));
			cliente.getSuscripcion().setFechaPeriodoHasta(cliente.getSuscripcion().getFechaPeriodoHasta().minusDays(1).withHour(23).withMinute(59).withSecond(59));

		}
	}

	@Override
	public ZonedDateTime calcularProximaSuscripcionFechaPeriodoDesde(Suscripcion suscripcion) {
		return calcularProximaSuscripcionFechaPeriodoDesde(suscripcion.getFechaUltimaActivacion(), suscripcion.getMesesActiva());
	}

	@Override
	public ZonedDateTime calcularProximaSuscripcionFechaPeriodoDesde(ZonedDateTime fechaUltimaActivacion, Integer mesesActiva) {
		return fechaUltimaActivacion.plusMonths(mesesActiva).withHour(0).withMinute(0).withSecond(0).withNano(0);
	}

	@Override
	public ZonedDateTime calcularProximaSuscripcionFechaPeriodoHasta(Suscripcion suscripcion) {
		return suscripcion.getFechaUltimaActivacion().plusMonths(suscripcion.getMesesActiva() + 1).minusDays(1).withHour(23).withMinute(59).withSecond(59).withNano(0);
	}

	@Transactional
	@Override
	public void aceptaPoliticaPrivacidad(Long clienteId) {
		var cliente = buscarClientePorId(clienteId);
		cliente.setAceptaPoliticaPrivacidad(Boolean.TRUE);
		cliente.setFechaAceptaPoliticaPrivacidad(ZonedDateTime.now());

	}

	@Transactional
	@Override
	public void aceptaTerminosCondiciones(Long clienteId) {
		var cliente = buscarClientePorId(clienteId);
		cliente.setAceptaTerminosCondiciones(Boolean.TRUE);
		cliente.setFechaAceptaTerminosCondiciones(ZonedDateTime.now());
	}

	@Override
	public ClienteConfiguracion buscarClienteConfiguracionPorClienteId(Long clienteId) {
		return clienteConfiguracionRepositorio.findByCliente(buscarClientePorId(clienteId));
	}

	@Transactional
	@Override
	public void actualizarClienteConfiguracion(ClienteConfiguracion clienteConfiguracion) {

		// Actualizar configuracion
		var clienteConfiguracionActualizado = clienteConfiguracionRepositorio.findById(clienteConfiguracion.getId()).orElse(null);
		clienteConfiguracionActualizado.setTiempoDescansoEntreEjercicios(clienteConfiguracion.getTiempoDescansoEntreEjercicios());
		clienteConfiguracionActualizado.setTiempoDescansoEntreUnilaterales(clienteConfiguracion.getTiempoDescansoEntreUnilaterales());
		clienteConfiguracionActualizado.setTiempoEjercicioPorRepeticiones(clienteConfiguracion.getTiempoEjercicioPorRepeticiones());
		clienteConfiguracionActualizado.setEjecucionAutomatica(clienteConfiguracion.getEjecucionAutomatica());

	}

}
