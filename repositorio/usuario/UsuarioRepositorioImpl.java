package es.pocketrainer.repositorio.usuario;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import es.pocketrainer.modelo.usuario.Usuario_;

import es.pocketrainer.modelo.usuario.Usuario;

public class UsuarioRepositorioImpl implements UsuarioRepositorioExtendido {

	@PersistenceContext
	EntityManager em;

	@Override
	public Usuario buscarUsuarioPorUsuarioNombre(String usuarioNombre) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Usuario> cq = cb.createQuery(Usuario.class);

		Root<Usuario> usuario = cq.from(Usuario.class);

		cq.where(cb.equal(usuario.get(Usuario_.USUARIO_NOMBRE), usuarioNombre));

		try {
			return em.createQuery(cq).getSingleResult();
		} catch (final NoResultException nre) {
			return null;
		}
	}

}
