package es.pocketrainer.repositorio.rutina;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class RutinaFaseGrupoRepeticionEjercicioRepositorioImpl implements RutinaFaseGrupoRepeticionEjercicioRepositorioExtendido {

	@PersistenceContext
	EntityManager em;
}
