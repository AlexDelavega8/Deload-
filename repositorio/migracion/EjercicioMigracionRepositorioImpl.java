package es.pocketrainer.repositorio.migracion;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class EjercicioMigracionRepositorioImpl implements EjercicioMigracionRepositorioExtendido {

	@PersistenceContext
	EntityManager em;

}
