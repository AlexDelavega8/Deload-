package es.pocketrainer.repositorio.videoconferencia;

import org.springframework.data.jpa.repository.JpaRepository;

import es.pocketrainer.modelo.videoconferencia.Videoconferencia;

public interface VideoconferenciaRepositorio extends JpaRepository<Videoconferencia, Long>, VideoconferenciaRepositorioExtendido {

}
