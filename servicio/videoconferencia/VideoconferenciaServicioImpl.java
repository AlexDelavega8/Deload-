package es.pocketrainer.servicio.videoconferencia;

import java.util.UUID;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import es.pocketrainer.modelo.videoconferencia.Videoconferencia;
import es.pocketrainer.repositorio.videoconferencia.VideoconferenciaRepositorio;
import es.pocketrainer.servicio.notificaciones.NotificacionServicio;
import es.pocketrainer.servicio.valormaestro.ValorMaestroServicio;
import es.pocketrainer.util.Constantes;
import es.pocketrainer.util.Constantes.VideoconferenciaEstadoEnum;

@Service
@Transactional
public class VideoconferenciaServicioImpl implements VideoconferenciaServicio {

	private static Logger LOGGER = LoggerFactory.getLogger(VideoconferenciaServicioImpl.class);

	@Resource
	private VideoconferenciaRepositorio videoconferenciaRepositorio;

	@Resource
	private NotificacionServicio notificacionServicio;

	@Resource
	private ValorMaestroServicio valorMaestroServicio;

	@Override
	public Videoconferencia prepararVideoconferenciaNueva() {
		Videoconferencia videoconferencia = new Videoconferencia();
		videoconferencia.setEstado(valorMaestroServicio.buscarValorMaestroPorCodigo(VideoconferenciaEstadoEnum.SIN_PROGRAMAR.codigo()));
		videoconferencia.setSalaId(UUID.randomUUID().toString());
		return videoconferencia;
	}

	@Override
	public Videoconferencia buscarVideoConferenciaPorId(Long videoconferenciaId) {
		return videoconferenciaRepositorio.findById(videoconferenciaId).get();
	}

	@Override
	public void revisarVideoconferencias() {
		var videoconferenciaProgramadaCercanaLista = videoconferenciaRepositorio.buscarProximasVideoConferencias();
		if (videoconferenciaProgramadaCercanaLista != null) {
			videoconferenciaProgramadaCercanaLista.stream().forEach(it -> {
				notificacionServicio.notificarClienteVideoconferenciaEnBreves(it);
				notificacionServicio.notificarEntrenadorVideollamadaEnBreves(it);
			});
		}

	}

	@Override
	public void establecerVideoconferenciaRealizada(Long videoconferenciaId) {
		videoconferenciaRepositorio.findById(videoconferenciaId).get()
				.setEstado(valorMaestroServicio.buscarValorMaestroPorCodigo(Constantes.VideoconferenciaEstadoEnum.REALIZADA.codigo()));
	}

	@Override
	public Videoconferencia establecerVideoconferenciaEnCurso(Long videoconferenciaId) {
		var Videoconferencia = buscarVideoConferenciaPorId(videoconferenciaId);
		Videoconferencia.setEstado(valorMaestroServicio.buscarValorMaestroPorCodigo(Constantes.VideoconferenciaEstadoEnum.EN_CURSO.codigo()));

		return Videoconferencia;
	}

	@Override
	public Videoconferencia actualizarVideoconferenciaRutina(Long videoconferenciaId, Long rutinaId) {
		var videoconferencia = buscarVideoConferenciaPorId(videoconferenciaId);
		videoconferencia.setRutinaId(rutinaId);
		return videoconferencia;
	}

}
