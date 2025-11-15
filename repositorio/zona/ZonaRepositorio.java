package es.pocketrainer.repositorio.zona;

import org.springframework.data.jpa.repository.JpaRepository;

import es.pocketrainer.modelo.ejercicio.Zona;

public interface ZonaRepositorio extends JpaRepository<Zona, Long>, ZonaRepositorioExtendido {

}
