package es.pocketrainer.repositorio.entrenador;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;

import es.pocketrainer.formulario.BuscarEntrenadorFormulario;
import es.pocketrainer.modelo.entrenador.Entrenador;
import es.pocketrainer.modelo.entrenador.Entrenador_;
import es.pocketrainer.modelo.valormaestro.ValorMaestro_;
import es.pocketrainer.util.Constantes.EntrenadorEstadoEnum;

public class EntrenadorRepositorioImpl implements EntrenadorRepositorioExtendido {

	@PersistenceContext
	EntityManager em;

	@Override
	public Entrenador buscarEntrenadorActivoPorUsuarioId(Long usuarioId) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Entrenador> cq = cb.createQuery(Entrenador.class);

		Root<Entrenador> entrenador = cq.from(Entrenador.class);
		List<Predicate> predicates = new ArrayList<>();

		predicates.add(cb.equal(entrenador.get(Entrenador_.USUARIO).get("id"), usuarioId));
		predicates.add(cb.equal(entrenador.get(Entrenador_.ESTADO).get(ValorMaestro_.CODIGO), EntrenadorEstadoEnum.ACTIVO.codigo()));

		cq.where(predicates.toArray(new Predicate[predicates.size()]));
		try {
			return em.createQuery(cq).getSingleResult();
		} catch (final NoResultException nre) {
			return null;
		}
	}

	@Override
	public Entrenador buscarEntrenadorPorEmail(String email) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Entrenador> cq = cb.createQuery(Entrenador.class);

		Root<Entrenador> entrenador = cq.from(Entrenador.class);

		cq.where(cb.equal(entrenador.get(Entrenador_.EMAIL), email));
		try {
			return em.createQuery(cq).getSingleResult();
		} catch (final NoResultException nre) {
			return null;
		}
	}

	@Override
	public List<Entrenador> buscarEntrenadorPorFiltro(BuscarEntrenadorFormulario buscarEntrenadorFormulario) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Entrenador> cq = cb.createQuery(Entrenador.class);

		Root<Entrenador> entrenador = cq.from(Entrenador.class);
		List<Predicate> predicates = new ArrayList<>();

		if (StringUtils.isNotBlank(buscarEntrenadorFormulario.getNombre())) {
			predicates.add(cb.like(entrenador.get(Entrenador_.NOMBRE), "%" + buscarEntrenadorFormulario.getNombre() + "%"));
		}

		if (buscarEntrenadorFormulario.getEstadoCodigoLista() != null && !buscarEntrenadorFormulario.getEstadoCodigoLista().isEmpty()) {
			predicates.add(entrenador.get(Entrenador_.ESTADO).get(ValorMaestro_.CODIGO).in(buscarEntrenadorFormulario.getEstadoCodigoLista()));
		}

		cq.where(predicates.toArray(new Predicate[predicates.size()]));
		return em.createQuery(cq).getResultList();
	}

}
