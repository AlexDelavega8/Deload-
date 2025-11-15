package es.pocketrainer.repositorio.valormaestro;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class ValorMaestroRepositorioImpl implements ValorMaestroRepositorioExtendido {

	@PersistenceContext
	EntityManager em;

}
