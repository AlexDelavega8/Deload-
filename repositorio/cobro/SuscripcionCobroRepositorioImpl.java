package es.pocketrainer.repositorio.cobro;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import es.pocketrainer.formulario.BuscarSuscripcionCobroListaFormulario;
import es.pocketrainer.modelo.cobro.SuscripcionCobro;
import es.pocketrainer.modelo.cobro.SuscripcionCobro_;
import es.pocketrainer.modelo.valormaestro.ValorMaestro_;

public class SuscripcionCobroRepositorioImpl implements SuscripcionCobroRepositorioExtendido {

	@PersistenceContext
	EntityManager em;

	@Override
	public List<SuscripcionCobro> buscarSuscripcionCobroPorFiltro(BuscarSuscripcionCobroListaFormulario buscarSuscripcionCobroListaFormulario) {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<SuscripcionCobro> cq = cb.createQuery(SuscripcionCobro.class);
//			cq.distinct(true);
		Root<SuscripcionCobro> suscripcionCobro = cq.from(SuscripcionCobro.class);
		List<Predicate> predicates = new ArrayList<>();

		if (buscarSuscripcionCobroListaFormulario.getFechaEmisionRangoInicio() != null) {
			predicates.add(cb.greaterThanOrEqualTo(suscripcionCobro.get(SuscripcionCobro_.FECHA_HORA_EMISION),
					buscarSuscripcionCobroListaFormulario.getFechaEmisionRangoInicio()));
		}

		if (buscarSuscripcionCobroListaFormulario.getFechaEmisionRangoFin() != null) {
			predicates.add(cb.lessThanOrEqualTo(suscripcionCobro.get(SuscripcionCobro_.FECHA_HORA_EMISION),
					buscarSuscripcionCobroListaFormulario.getFechaEmisionRangoFin()));
		}

		if (buscarSuscripcionCobroListaFormulario.getEstadoCodigoLista() != null
				&& !buscarSuscripcionCobroListaFormulario.getEstadoCodigoLista().isEmpty()) {
			predicates.add(suscripcionCobro.get(SuscripcionCobro_.ESTADO).get(ValorMaestro_.CODIGO)
					.in(buscarSuscripcionCobroListaFormulario.getEstadoCodigoLista()));
		}

//		predicates.add(cb.notEqual(suscripcionCobro.get(SuscripcionCobro_.SUSCRIPCION).get(Suscripcion_.ID), Constantes.SUSCRIPCION_PRUEBA));

		cq.where(predicates.toArray(new Predicate[predicates.size()]));
		return em.createQuery(cq).getResultList();

	}

	@Override
	public List<SuscripcionCobro> buscarSuscripcionCobroPorEstado(String codigo) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<SuscripcionCobro> cq = cb.createQuery(SuscripcionCobro.class);
		Root<SuscripcionCobro> suscripcionCobro = cq.from(SuscripcionCobro.class);
		List<Predicate> predicates = new ArrayList<>();

		predicates.add(cb.equal(suscripcionCobro.get(SuscripcionCobro_.ESTADO).get(ValorMaestro_.CODIGO), codigo));

		cq.where(predicates.toArray(new Predicate[predicates.size()]));
		return em.createQuery(cq).getResultList();
	}

	@Override
	public SuscripcionCobro buscarSuscripcionCobroPorIdConBloqueoPesimista(Long suscripcionCobroId) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<SuscripcionCobro> cq = cb.createQuery(SuscripcionCobro.class);
		Root<SuscripcionCobro> suscripcionCobro = cq.from(SuscripcionCobro.class);
		List<Predicate> predicates = new ArrayList<>();

		predicates.add(cb.equal(suscripcionCobro.get(SuscripcionCobro_.ID), suscripcionCobroId));

		cq.where(predicates.toArray(new Predicate[predicates.size()]));
		return em.createQuery(cq).setLockMode(LockModeType.PESSIMISTIC_WRITE).getSingleResult();
	}

}
