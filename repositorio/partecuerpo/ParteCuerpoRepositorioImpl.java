package es.pocketrainer.repositorio.partecuerpo;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class ParteCuerpoRepositorioImpl implements ParteCuerpoRepositorioExtendido {

	@PersistenceContext
	EntityManager em;
}
