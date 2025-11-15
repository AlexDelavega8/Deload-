package es.pocketrainer.repositorio.rutina;

import org.springframework.data.jpa.repository.JpaRepository;

import es.pocketrainer.modelo.rutina.RutinaFaseGrupoRepeticionEjercicio;

public interface RutinaFaseGrupoRepeticionEjercicioRepositorio
		extends JpaRepository<RutinaFaseGrupoRepeticionEjercicio, Long>, RutinaFaseGrupoRepeticionEjercicioRepositorioExtendido {

}
