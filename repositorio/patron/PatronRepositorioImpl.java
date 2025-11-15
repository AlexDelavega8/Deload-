package es.pocketrainer.repositorio.patron;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import es.pocketrainer.repositorio.material.MaterialRepositorioExtendido;

public class PatronRepositorioImpl implements MaterialRepositorioExtendido {

	@PersistenceContext
	EntityManager em;
}
