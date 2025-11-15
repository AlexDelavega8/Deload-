package es.pocketrainer.repositorio.partecuerpo;

import org.springframework.data.jpa.repository.JpaRepository;

import es.pocketrainer.modelo.ejercicio.ParteCuerpo;

public interface ParteCuerpoRepositorio extends JpaRepository<ParteCuerpo, Long>, ParteCuerpoRepositorioExtendido {

}
