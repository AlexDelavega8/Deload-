package es.pocketrainer.repositorio.material;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class MaterialRepositorioImpl implements MaterialRepositorioExtendido {

	@PersistenceContext
	EntityManager em;
}
