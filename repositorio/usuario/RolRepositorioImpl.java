package es.pocketrainer.repositorio.usuario;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import es.pocketrainer.modelo.usuario.Rol_;

import es.pocketrainer.modelo.usuario.Rol;

public class RolRepositorioImpl implements RolRepositorioExtendido {

	@PersistenceContext
	EntityManager em;

	@Override
	public Rol buscarRolPorCodigo(String codigo) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Rol> cq = cb.createQuery(Rol.class);

		Root<Rol> rol = cq.from(Rol.class);

		cq.where(cb.equal(rol.get(Rol_.CODIGO), codigo));

		try {
			return em.createQuery(cq).getSingleResult();
		} catch (final NoResultException nre) {
			return null;
		}
	}

}
