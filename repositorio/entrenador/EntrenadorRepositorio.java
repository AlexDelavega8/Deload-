package es.pocketrainer.repositorio.entrenador;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import es.pocketrainer.modelo.entrenador.Entrenador;
import es.pocketrainer.modelo.valormaestro.ValorMaestro;

public interface EntrenadorRepositorio extends JpaRepository<Entrenador, Long>, EntrenadorRepositorioExtendido {

	List<Entrenador> findByEstado(ValorMaestro estado);

}
