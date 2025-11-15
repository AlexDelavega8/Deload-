package es.pocketrainer.repositorio.musculo;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import es.pocketrainer.repositorio.material.MaterialRepositorioExtendido;

public class MusculoRepositorioImpl implements MaterialRepositorioExtendido {

	@PersistenceContext
	EntityManager em;
}
