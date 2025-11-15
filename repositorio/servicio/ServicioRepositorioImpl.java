package es.pocketrainer.repositorio.servicio;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class ServicioRepositorioImpl implements ServicioRepositorioExtendido {

	@PersistenceContext
	EntityManager em;
}
