package es.pocketrainer.repositorio.parametrizacion;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class ParametroConfiguracionRepositorioImpl implements ParametroConfiguracionRepositorioExtendido {

	@PersistenceContext
	EntityManager em;

}
