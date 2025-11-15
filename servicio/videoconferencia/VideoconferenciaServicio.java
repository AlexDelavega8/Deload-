package es.pocketrainer.servicio.videoconferencia;

import es.pocketrainer.modelo.videoconferencia.Videoconferencia;

public interface VideoconferenciaServicio {

	Videoconferencia prepararVideoconferenciaNueva();

	Videoconferencia buscarVideoConferenciaPorId(Long videoconferenciaId);

	void establecerVideoconferenciaRealizada(Long videoconferenciaId);

	void revisarVideoconferencias();

	Videoconferencia establecerVideoconferenciaEnCurso(Long videoconferenciaId);

	Videoconferencia actualizarVideoconferenciaRutina(Long videoconferenciaId, Long rutinaId);

}
