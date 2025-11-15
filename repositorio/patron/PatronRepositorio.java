package es.pocketrainer.repositorio.patron;

import org.springframework.data.jpa.repository.JpaRepository;

import es.pocketrainer.modelo.ejercicio.Patron;
import es.pocketrainer.repositorio.material.MaterialRepositorioExtendido;

public interface PatronRepositorio extends JpaRepository<Patron, Long>, MaterialRepositorioExtendido {

}
