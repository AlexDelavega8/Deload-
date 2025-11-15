package es.pocketrainer.repositorio.musculo;

import org.springframework.data.jpa.repository.JpaRepository;

import es.pocketrainer.modelo.ejercicio.Musculo;
import es.pocketrainer.repositorio.material.MaterialRepositorioExtendido;

public interface MusculoRepositorio extends JpaRepository<Musculo, Long>, MaterialRepositorioExtendido {

}
