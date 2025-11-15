package es.pocketrainer.servicio.notificaciones;

import java.time.ZonedDateTime;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import es.pocketrainer.modelo.cliente.Cliente;
import es.pocketrainer.modelo.entrenador.Entrenador;
import es.pocketrainer.modelo.usuario.Usuario;
import es.pocketrainer.modelo.videoconferencia.Videoconferencia;
import es.pocketrainer.servicio.entrenador.EntrenadorServicio;
import es.pocketrainer.servicio.rutina.EmailServicio;
import es.pocketrainer.util.Constantes;

@Service
public class NotificacionServicioImpl implements NotificacionServicio {

	private final static String RUTA_CORREOS_GENERAL = "correo/";
	private final static String RUTA_CORREOS_ENTRENADOR = "correo/entrenador/";
	private final static String RUTA_CORREOS_CLIENTE = "correo/cliente/";

	private final static String CORREO_ERROR = "errorCorreo";
	private final static String CORREO_NOTIFICACION_TECNICO_PROBLEMAS_RENOVACION = "notificacionTecnicoProblemasRenovacionCorreo";

	private final static String CORREO_CLIENTE_NUEVO = "clienteNuevoCorreo";
	private final static String CORREO_CLIENTE_REACTIVA_SUSCRIPCION = "clienteReactivaSuscripcionCorreo";
	private final static String CORREO_CLIENTE_CANCELA_SUSCRIPCION = "clienteCancelaSuscripcionCorreo";
	private final static String CORREO_CLIENTE_BAJA = "clienteBajaCorreo";
	private final static String CORREO_CLIENTE_ASIGNADO = "clienteAsignadoCorreo";
	private final static String CORREO_CLIENTE_ACTUALIZA_DATOS = "clienteActualizaDatosCorreo";
	private final static String CORREO_CLIENTE_DEJA_COMENTARIO = "clienteDejaComentarioCorreo";
	private final static String CORREO_CLIENTE_RENOVACION_SUSCRIPCION_PREPARADA = "clienteRenovacionSuscripcionPreparadaCorreo";
	private final static String CORREO_MENSAJE_DE_CLIENTE = "mensajeDeClienteCorreo";
	private final static String CORREO_NUEVO_ENTRENADOR_POCKETRAINER = "nuevoEntrenadorPocketrainerCorreo";
	private final static String CORREO_NOTIFICACION_ENTRENADOR_VIDEOCONFERENCIA_EN_BREVES = "notificacionEntrenadorVideoconferenciaEnBrevesCorreo";

	private final static String CORREO_REGISTRO_CORRECTO = "registroCorrectoCorreo";
	private final static String CORREO_ENTRENADOR_ASIGNADO = "entrenadorAsignadoCorreo";
	private final static String CORREO_ENTRENADOR_CAMBIADO = "entrenadorCambiadoCorreo";
	private final static String CORREO_ENTRENAMIENTOS_PREPARADOS = "entrenamientosPreparadosCorreo";
	private final static String CORREO_VIDEOCONFERENCIA_PROGRAMADA = "videoconferenciaProgramadaCorreo";
	private final static String CORREO_VIDEOCONFERENCIA_REPROGRAMADA = "videoconferenciaReprogramadaCorreo";
	private final static String CORREO_VIDEOCONFERENCIA_CANCELADA = "videoconferenciaCanceladaCorreo";

	private final static String CORREO_NOTIFICACION_CLIENTE_VIDEOCONFERENCIA_EN_BREVES = "notificacionClienteVideoconferenciaEnBrevesCorreo";
	private final static String CORREO_NOTIFICACION_CLIENTE_RENOVACION_PROXIMA = "notificacionClienteRenovacionProximaCorreo";
	private final static String CORREO_FIN_SUSCRIPCION = "finSuscripcionCorreo";
	private final static String CORREO_REACTIVACION_SUSCRIPCION = "reactivacionSuscripcionCorreo";
	private final static String CORREO_CANCELACION_SUSCRIPCION = "cancelacionSuscripcionCorreo";
	private final static String CORREO_NOTIFICACION_CLIENTE_PROBLEMAS_RENOVACION = "notificacionClienteProblemasRenovacionCorreo";

	private final static String CORREO_RECUPERACION_PASSWORD = "recuperacionPasswordCorreo";

	@Resource
	private EmailServicio emailServicio;

	@Resource
	private EntrenadorServicio entrenadorServicio;

	@Resource
	private SpringTemplateEngine springTemplateEngine;

	private String generarMensajeCorreoEntrenadorHtml(String plantilla, Context contexto, Cliente cliente) {
		contexto.setVariable("cliente", cliente);
		return generarMensajeCorreoEntrenadorHtml(plantilla, contexto);
	}

	private String generarMensajeCorreoEntrenadorHtml(String plantilla, Context contexto) {
		return springTemplateEngine.process(RUTA_CORREOS_ENTRENADOR + plantilla, contexto);
	}

	private String generarMensajeCorreoClienteHtml(String plantilla, Context contexto) {
		return springTemplateEngine.process(RUTA_CORREOS_CLIENTE + plantilla, contexto);
	}

	private String generarMensajeCorreoGeneralHtml(String plantilla, Context contexto) {
		return springTemplateEngine.process(RUTA_CORREOS_GENERAL + plantilla, contexto);
	}

	@Override
	public void notificarNuevoCliente(Entrenador administrador, Cliente cliente) {
		var contexto = new Context();
		contexto.setVariable("clienteNombre", cliente.getNombreCompleto());
		emailServicio.enviarEmail("Nuevo cliente en pocketrainer", administrador != null ? administrador.getEmail() : Constantes.CORREO_TECNICO, null, null, null,
				generarMensajeCorreoEntrenadorHtml(CORREO_CLIENTE_NUEVO, contexto, cliente));
	}

	@Override
	public void notificarClienteReactivaSuscripcion(Entrenador administrador, Cliente cliente) {
		var contexto = new Context();
		contexto.setVariable("clienteNombre", cliente.getNombreCompleto());
		emailServicio.enviarEmail("Cliente vuelve a pocketrainer", administrador != null ? administrador.getEmail() : Constantes.CORREO_TECNICO, null, null, null,
				generarMensajeCorreoEntrenadorHtml(CORREO_CLIENTE_REACTIVA_SUSCRIPCION, contexto, cliente));
	}

	@Override
	public void notificarClienteCancelaSuscripcion(Entrenador administrador, Cliente cliente) {
		var contexto = new Context();
		contexto.setVariable("clienteNombre", cliente.getNombreCompleto());
		emailServicio.enviarEmail("Cliente cancela suscripción", administrador != null ? administrador.getEmail() : Constantes.CORREO_TECNICO, null, null, null,
				generarMensajeCorreoEntrenadorHtml(CORREO_CLIENTE_CANCELA_SUSCRIPCION, contexto, cliente));
	}

	@Override
	public void notificarClienteBaja(Entrenador administrador, Cliente cliente) {
		var contexto = new Context();
		contexto.setVariable("clienteNombre", cliente.getNombreCompleto());
		emailServicio.enviarEmail("Cliente causa baja", administrador != null ? administrador.getEmail() : Constantes.CORREO_TECNICO,
				cliente.getEntrenador() != null ? cliente.getEntrenador().getEmail() : Constantes.CORREO_TECNICO, null, null,
				generarMensajeCorreoEntrenadorHtml(CORREO_CLIENTE_BAJA, contexto, cliente));
	}

	@Override
	public void notificarEntrenadorCreado(Entrenador entrenador, String passwordNueva) {
		var contexto = new Context();
		contexto.setVariable("entrenadorNombre", entrenador.getNombre());
		contexto.setVariable("correo", entrenador.getEmail());
		contexto.setVariable("password", passwordNueva);
		emailServicio.enviarEmail("Nuevo entrenador pocketrainer", entrenador.getEmail(), null, null, null,
				generarMensajeCorreoEntrenadorHtml(CORREO_NUEVO_ENTRENADOR_POCKETRAINER, contexto));
	}

	@Override
	public void notificarClienteAsignado(Cliente cliente) {
		var contexto = new Context();
		contexto.setVariable("clienteNombre", cliente.getNombreCompleto());
		contexto.setVariable("clienteTelefono", cliente.getTelefono());
		contexto.setVariable("clienteHorarioTelefonico",
				StringUtils.isNotBlank(cliente.getDisponibilidadTelefonica()) ? cliente.getDisponibilidadTelefonica() : "No indico preferencias");
		emailServicio.enviarEmail("Cliente asignado", cliente.getEntrenador().getEmail(), null, null, null,
				generarMensajeCorreoEntrenadorHtml(CORREO_CLIENTE_ASIGNADO, contexto, cliente));
	}

	@Override
	public void notificarEntrenadorVideollamadaEnBreves(Videoconferencia videoconferencia) {
		var contexto = new Context();
		contexto.setVariable("entrenadorNombre", videoconferencia.getEntrenador().getNombre());
		contexto.setVariable("clienteNombre", videoconferencia.getCliente().getNombreCompleto());
		contexto.setVariable("fecha", videoconferencia.getFechaHoraProgramada());
		emailServicio.enviarEmail("Videoconferencia con cliente", videoconferencia.getEntrenador().getEmail(), null, null, null,
				generarMensajeCorreoEntrenadorHtml(CORREO_NOTIFICACION_ENTRENADOR_VIDEOCONFERENCIA_EN_BREVES, contexto, videoconferencia.getCliente()));
	}

	@Override
	public void notificarClienteCambiaDatos(Cliente cliente) {
		var contexto = new Context();
		contexto.setVariable("clienteNombre", cliente.getNombreCompleto());
		emailServicio.enviarEmail("Un cliente actualizó sus datos", cliente.getEntrenador() != null ? cliente.getEntrenador().getEmail() : Constantes.CORREO_TECNICO,
				null, null, null, generarMensajeCorreoEntrenadorHtml(CORREO_CLIENTE_ACTUALIZA_DATOS, contexto, cliente));
	}

	@Override
	public void notificarMensajeDeCliente(Cliente cliente, String mensaje) {
		var contexto = new Context();
		contexto.setVariable("clienteNombre", cliente.getNombreCompleto());
		contexto.setVariable("clienteCorreo", cliente.getEmail());
		contexto.setVariable("entrenadorNombre", cliente.getEntrenador().getNombreCompleto());
		contexto.setVariable("mensaje", mensaje);
		emailServicio.enviarEmail("Mensaje de " + cliente.getNombreCompleto(),
				cliente.getEntrenador() != null ? cliente.getEntrenador().getEmail() : Constantes.CORREO_TECNICO, null, Constantes.CORREO_TECNICO, cliente.getEmail(),
				generarMensajeCorreoEntrenadorHtml(CORREO_MENSAJE_DE_CLIENTE, contexto, cliente));
	}

	@Override
	public void notificarClienteDejaComentario(Cliente cliente, String comentario, String rutinaNombre) {
		var contexto = new Context();
		contexto.setVariable("clienteNombre", cliente.getNombreCompleto());
		contexto.setVariable("clienteCorreo", cliente.getEmail());
		contexto.setVariable("comentario", comentario);
		contexto.setVariable("rutinaNombre", StringUtils.isNotBlank(rutinaNombre) ? rutinaNombre : "Sin nombre");
		emailServicio.enviarEmail("Un cliente dejó comentario", cliente.getEntrenador() != null ? cliente.getEntrenador().getEmail() : Constantes.CORREO_TECNICO, null,
				Constantes.CORREO_TECNICO, cliente.getEmail(), generarMensajeCorreoEntrenadorHtml(CORREO_CLIENTE_DEJA_COMENTARIO, contexto, cliente));
	}

	@Override
	public void notificarClienteRenovacionSuscripcionPreparada(Cliente cliente) {
		var contexto = new Context();
		contexto.setVariable("clienteNombre", cliente.getNombreCompleto());
		emailServicio.enviarEmail("Nuevos entrenamientos y videoconferencia",
				cliente.getEntrenador() != null ? cliente.getEntrenador().getEmail() : entrenadorServicio.buscarAdministrador().getEmail(), null, null, null,
				generarMensajeCorreoEntrenadorHtml(CORREO_CLIENTE_RENOVACION_SUSCRIPCION_PREPARADA, contexto, cliente));
	}

	@Override
	public void notificarRegistroCorrecto(Cliente cliente) {
		var contexto = new Context();
		contexto.setVariable("clienteNombre", cliente.getNombre());
		contexto.setVariable("usuario", cliente.getUsuario().getUsuarioNombre());
		emailServicio.enviarEmail("¡Bienvenid@ a pocketrainer!", cliente.getEmail(), null, null, null,
				generarMensajeCorreoClienteHtml(CORREO_REGISTRO_CORRECTO, contexto));
	}

	@Override
	public void notificarEntrenadorAsignado(Cliente cliente) {
		var contexto = new Context();
		contexto.setVariable("clienteNombre", cliente.getNombre());
		contexto.setVariable("entrenadorNombre", cliente.getEntrenador().getNombreCompleto());
		contexto.setVariable("entrenadorMail", cliente.getEntrenador().getEmail());
		emailServicio.enviarEmail("Ya tienes tu entrenador de bolsillo", cliente.getEmail(), null, null, null,
				generarMensajeCorreoClienteHtml(CORREO_ENTRENADOR_ASIGNADO, contexto));
	}

	@Override
	public void notificarEntrenadorCambiado(Cliente cliente) {
		var contexto = new Context();
		contexto.setVariable("clienteNombre", cliente.getNombre());
		contexto.setVariable("entrenadorNombre", cliente.getEntrenador().getNombreCompleto());
		emailServicio.enviarEmail("Tienes nuevo entrenador", cliente.getEmail(), null, null, null, generarMensajeCorreoClienteHtml(CORREO_ENTRENADOR_CAMBIADO, contexto));
	}

	@Override
	public void notificarEntrenamientoPreparado(Cliente cliente) {
		var contexto = new Context();
		contexto.setVariable("clienteNombre", cliente.getNombre());
		emailServicio.enviarEmail("Entrenamientos preparados", cliente.getEmail(), null, null, null,
				generarMensajeCorreoClienteHtml(CORREO_ENTRENAMIENTOS_PREPARADOS, contexto));

	}

	@Override
	public void notificarClienteVideoconferenciaProgramada(Videoconferencia videoconferencia) {
		var contexto = new Context();
		contexto.setVariable("clienteNombre", videoconferencia.getCliente().getNombre());
		contexto.setVariable("fecha", videoconferencia.getFechaHoraProgramada());
		emailServicio.enviarEmail("Videoconferencia programada", videoconferencia.getCliente().getEmail(), null, null, null,
				generarMensajeCorreoClienteHtml(CORREO_VIDEOCONFERENCIA_PROGRAMADA, contexto));

	}

	@Override
	public void notificarClienteVideoconferenciaReprogramada(Videoconferencia videoconferencia) {
		var contexto = new Context();
		contexto.setVariable("clienteNombre", videoconferencia.getCliente().getNombre());
		contexto.setVariable("fecha", videoconferencia.getFechaHoraProgramada());
		emailServicio.enviarEmail("Videoconferencia reprogramada", videoconferencia.getCliente().getEmail(), null, null, null,
				generarMensajeCorreoClienteHtml(CORREO_VIDEOCONFERENCIA_REPROGRAMADA, contexto));
	}

	@Override
	public void notificarClienteVideoconferenciaCancelada(Videoconferencia videoconferencia, ZonedDateTime fechaAnterior) {
		var contexto = new Context();
		contexto.setVariable("clienteNombre", videoconferencia.getCliente().getNombre());
		contexto.setVariable("fecha", fechaAnterior);
		emailServicio.enviarEmail("Videoconferencia cancelada", videoconferencia.getCliente().getEmail(), null, null, null,
				generarMensajeCorreoClienteHtml(CORREO_VIDEOCONFERENCIA_CANCELADA, contexto));
	}

	@Override
	public void notificarClienteVideoconferenciaEnBreves(Videoconferencia videoconferencia) {
		var contexto = new Context();
		contexto.setVariable("clienteNombre", videoconferencia.getCliente().getNombre());
		contexto.setVariable("fecha", videoconferencia.getFechaHoraProgramada());
		emailServicio.enviarEmail("Videoconferencia con entrenador", videoconferencia.getCliente().getEmail(), null, null, null,
				generarMensajeCorreoClienteHtml(CORREO_NOTIFICACION_CLIENTE_VIDEOCONFERENCIA_EN_BREVES, contexto));
	}

	@Override
	public void notificarFinSuscripcion(Cliente cliente) {
		var contexto = new Context();
		contexto.setVariable("clienteNombre", cliente.getNombre());
		emailServicio.enviarEmail("Te echaremos de menos", cliente.getEmail(), null, null, null, generarMensajeCorreoClienteHtml(CORREO_FIN_SUSCRIPCION, contexto));

	}

	@Override
	public void notificarSuscripcionReactivada(Cliente cliente) {
		var contexto = new Context();
		contexto.setVariable("clienteNombre", cliente.getNombre());
		emailServicio.enviarEmail("¡Bienvenid@ de nuevo!", cliente.getEmail(), null, null, null,
				generarMensajeCorreoClienteHtml(CORREO_REACTIVACION_SUSCRIPCION, contexto));
	}

	@Override
	public void notificarSuscripcionCancelada(Cliente cliente) {
		var contexto = new Context();
		contexto.setVariable("clienteNombre", cliente.getNombre());
		emailServicio.enviarEmail("Cancelación de suscripción", cliente.getEmail(), null, null, null,
				generarMensajeCorreoClienteHtml(CORREO_CANCELACION_SUSCRIPCION, contexto));
	}

	@Override
	public void notificarSuscripcionImpagada(Cliente cliente) {
		var contexto = new Context();
		contexto.setVariable("clienteNombre", cliente.getNombre());
		emailServicio.enviarEmail("Problemas en la renovación de suscripción", cliente.getEmail(), null, null, null,
				generarMensajeCorreoClienteHtml(CORREO_NOTIFICACION_CLIENTE_PROBLEMAS_RENOVACION, contexto));
	}

	@Override
	public void notificarEntrenadorErrorCobroRenovacion(Cliente cliente, String error) {
		var contexto = new Context();
		contexto.setVariable("clienteNombre", cliente.getId() + " - " + cliente.getCodigo());
		contexto.setVariable("error", error);
		emailServicio.enviarEmail("Problemas en la renovación de suscripción", Constantes.CORREO_TECNICO, null, null, null,
				generarMensajeCorreoGeneralHtml(CORREO_NOTIFICACION_TECNICO_PROBLEMAS_RENOVACION, contexto));
	}

	@Override
	public void notificarClienteRenovacionProxima(Cliente cliente) {
		var contexto = new Context();
		contexto.setVariable("clienteNombre", cliente.getNombre());
		emailServicio.enviarEmail("Próxima renovación", cliente.getEmail(), null, null, null,
				generarMensajeCorreoClienteHtml(CORREO_NOTIFICACION_CLIENTE_RENOVACION_PROXIMA, contexto));
	}

	@Override
	public void notificarError(Map<String, Object> datosError) {
		var contexto = new Context();
		contexto.setVariables(datosError);
		emailServicio.enviarEmailError("Error pocketrainer " + datosError.get("entorno"), Constantes.CORREO_TECNICO, null, null,
				generarMensajeCorreoGeneralHtml(CORREO_ERROR, contexto));
	}

	@Override
	public void notificarGeneracionNuevaPassword(Usuario usuario, String nuevaPassword) {
		var contexto = new Context();
		contexto.setVariable("password", nuevaPassword);
		emailServicio.enviarEmail("Aquí tienes tu nueva contraseña", usuario.getUsuarioNombre(), null, null, null,
				generarMensajeCorreoClienteHtml(CORREO_RECUPERACION_PASSWORD, contexto));

	}

}
