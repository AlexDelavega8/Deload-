package es.pocketrainer.repositorio.videoconferencia;

import java.util.List;

import es.pocketrainer.modelo.videoconferencia.Videoconferencia;

public interface VideoconferenciaRepositorioExtendido {

	List<Videoconferencia> buscarProximasVideoConferencias();

}
