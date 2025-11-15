package es.pocketrainer.repositorio.valormaestro;

import org.springframework.data.jpa.repository.JpaRepository;

import es.pocketrainer.modelo.valormaestro.ValorMaestro;

public interface ValorMaestroRepositorio extends JpaRepository<ValorMaestro, Long>, ValorMaestroRepositorioExtendido {

}
