package es.pocketrainer.repositorio.rutina;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class RutinaFaseGrupoRepositorioImpl implements RutinaFaseGrupoRepositorioExtendido {

	@PersistenceContext
	EntityManager em;
}
