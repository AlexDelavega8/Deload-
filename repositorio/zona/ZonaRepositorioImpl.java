package es.pocketrainer.repositorio.zona;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import es.pocketrainer.repositorio.material.MaterialRepositorioExtendido;

public class ZonaRepositorioImpl implements MaterialRepositorioExtendido {

	@PersistenceContext
	EntityManager em;
}
