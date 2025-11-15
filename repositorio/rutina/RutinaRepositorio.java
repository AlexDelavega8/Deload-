package es.pocketrainer.repositorio.rutina;

import org.springframework.data.jpa.repository.JpaRepository;

import es.pocketrainer.modelo.rutina.Rutina;

public interface RutinaRepositorio extends JpaRepository<Rutina, Long>, RutinaRepositorioExtendido {

}
