package es.pocketrainer.servicio.tareaprogramada;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import es.pocketrainer.servicio.cliente.ClienteServicio;
import es.pocketrainer.servicio.ejercicio.EjercicioServicio;
import es.pocketrainer.servicio.entrenador.EntrenadorServicio;
import es.pocketrainer.servicio.notificaciones.NotificacionServicio;
import es.pocketrainer.servicio.rutina.EjercicioUltimoUsoCache;
import es.pocketrainer.servicio.videoconferencia.VideoconferenciaServicio;
import es.pocketrainer.util.ErrorUtil;

/**
 * CRON:
 * 
 * <pre>
 *┌───────────── second (0-59)
 *│ ┌───────────── minute (0 - 59)
 *│ │ ┌───────────── hour (0 - 23)
 *│ │ │ ┌───────────── day of the month (1 - 31)
 *│ │ │ │ ┌───────────── month (1 - 12) (or JAN-DEC)
 *│ │ │ │ │ ┌───────────── day of the week (0 - 7)
 *│ │ │ │ │ │          (0 or 7 is Sunday, or MON-SUN)
 *│ │ │ │ │ │
 * * * * * *
 * </pre>
 * 
 * @author Antonio FZ
 *
 */
@Component
public class TareaProgramadaServicio {

	private static Logger LOGGER = LoggerFactory.getLogger(TareaProgramadaServicio.class);
	private static final String TAREA_PROGRAMADA_REVISAR_SUSCRIPCIONES = "tarea_programada_revisar_suscripciones";
	private static final String TAREA_PROGRAMADA_REALIZAR_COBROS_RENOVACION_SUSCRIPCIONES = "tarea_programada_realizar_cobros_renovaciones_suscripciones";
	private static final String TAREA_PROGRAMADA_PREPARAR_RENOVACIONES = "tarea_programada_preparar_renovaciones";
	private static final String TAREA_PROGRAMADA_REVISAR_TRABAJO_ENTRENADOR = "tarea_programada_revisar_trabajo_entrenador";
	private static final String TAREA_PROGRAMADA_REVISAR_VIDEOCONFERENCIAS = "tarea_programada_revisar_videoconferencias";
	private static final String TAREA_PROGRAMADA_ACTUALIZAR_EJERCICIO_ESTADISTICAS = "tarea_programada_actualizar_ejercicio_estadisticas";
	private static final String TAREA_PROGRAMADA_BORRAR_EJERCICIO_ULTIMO_USO_CACHE = "tarea_programada_borrar_ejercicio_ultimo_uso_cache";

	@Value("${entorno}")
	private String entorno;

	@Resource
	private NotificacionServicio notificacionServicio;

	@Resource
	private ClienteServicio clienteServicio;

	@Resource
	private EntrenadorServicio entrenadorServicio;

	@Resource
	private VideoconferenciaServicio videoconferenciaServicio;

	@Resource
	private EjercicioServicio ejercicioServicio;

	@Resource
	private RevisionSuscripcionesTareaProgramada revisionSuscripcionesTareaProgramada;

	@Resource
	private EjercicioUltimoUsoCache ejercicioUltimoUsoCache;

	@PostConstruct
	public void postConstructor() {
		LOGGER.info("--------------- Comienzan las tareas programadas ---------------");
	}

	/**
	 * Tarea para actualizar las estadísticas de uso de ejercicios a nivel de
	 * entrenador y a nivel global
	 * 
	 * Todos los días a las 2 de la madrugada
	 * 
	 * @Scheduled(cron = "0 0 2 * * *")
	 */
	@Scheduled(cron = "0 0 2 * * *")
	public void actualizarEjercicioEstadistica() {
		ejecutarTareaProgramada(TAREA_PROGRAMADA_ACTUALIZAR_EJERCICIO_ESTADISTICAS, ejercicioServicio::actualizarEjercicioEstadistica);
	}

	/**
	 * Revisión de suscripciones. Se hacen dos pasos, ya que en
	 * revisarSuscripciones, se hacian renovaciones a inmediatamente se cobraban,
	 * dando lugar problemas al no haber finalizado la transaccion y recibir ya
	 * notificaciones asincronas de tpv
	 * 
	 * Todos los días a las 3 de la madrugada
	 * 
	 * @Scheduled(cron = "0 0 3 * * *")
	 */
	@Scheduled(cron = "0 0 3 * * *")
	public void revisarSuscripciones() {
		ejecutarTareaProgramada(TAREA_PROGRAMADA_REVISAR_SUSCRIPCIONES, clienteServicio::revisarSuscripciones);
		ejecutarTareaProgramada(TAREA_PROGRAMADA_REALIZAR_COBROS_RENOVACION_SUSCRIPCIONES, clienteServicio::realizarCobrosRenovacionesSuscripciones);
	}

	/**
	 * Preparar renovaciones: Precrear rutinas y avisar entrenadores de que tienen
	 * que ponerse manos a la obra. quiza aqui vaya la reivsion también de las
	 * tarjetas, SI -> Revisar fechas de caducidad para avisar con tiempo.
	 * 
	 * Todos los días a las 4 de la madrugada
	 */
	@Scheduled(cron = "0 0 4 * * *")
	public void prepararRenovaciones() {
		ejecutarTareaProgramada(TAREA_PROGRAMADA_PREPARAR_RENOVACIONES, clienteServicio::prepararRenovaciones);
	}

	/**
	 * Revisar si tiene los deberes hechos o hay que darle un toque en plan oye haz
	 * las rutinas, oye haz la video.
	 * 
	 * Todos los días al as 5 de la madrugada
	 */
	@Scheduled(cron = "0 0 5 * * *")
	public void revisarTrabajoDeEntrenador() {
		ejecutarTareaProgramada(TAREA_PROGRAMADA_REVISAR_TRABAJO_ENTRENADOR, entrenadorServicio::revisarTrabajoDeEntrenador);
	}

	/**
	 * Tarea para borrar cache de ultimo uso de ejercicios
	 * 
	 * Todos los días a las 6 de la madrugada
	 * 
	 * @Scheduled(cron = "0 0 6 * * *")
	 */
	@Scheduled(cron = "0 0 6 * * *")
	public void borrarEjercicioUltimoUsoCache() {
		ejecutarTareaProgramada(TAREA_PROGRAMADA_BORRAR_EJERCICIO_ULTIMO_USO_CACHE, ejercicioUltimoUsoCache::borrarCacheTodo);
	}

	/**
	 * Revisa si se acerca una video para avisar a cliente y entrenador con cierta
	 * antelacion
	 * 
	 * Cada 5 minutos
	 */
	@Scheduled(fixedRate = 300000)
	public void revisarVideoconferencias() {
		ejecutarTareaProgramada(TAREA_PROGRAMADA_REVISAR_VIDEOCONFERENCIAS, videoconferenciaServicio::revisarVideoconferencias);
	}

	private void ejecutarTareaProgramada(String nombreTareaProgramada, TareaProgramada tareaProgramada) {
		LOGGER.info("Ejecutando tarea programada " + nombreTareaProgramada);
		try {
			tareaProgramada.ejecutarTareaProgramada();
		} catch (Exception e) {
			var error = "Error ejecutando tarea programada " + nombreTareaProgramada;
			LOGGER.error(error, e);

			Map<String, Object> datosError = new HashMap<>();
			datosError.put("urlError", nombreTareaProgramada);
			datosError.put("error", error);
			datosError.put("entorno", entorno);
			datosError.put("sesion", "Sistema");
			datosError.put("errorTraza", ErrorUtil.generarExceptionStackTrace(e));
			datosError.put("fecha", ZonedDateTime.now());

			notificacionServicio.notificarError(datosError);
		}
		LOGGER.info("Finalizando tarea programada " + nombreTareaProgramada);
	}

}
