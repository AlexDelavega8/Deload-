package es.pocketrainer.repositorio.cliente;

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

import es.pocketrainer.formulario.BuscarClienteFormulario;
import es.pocketrainer.modelo.cliente.Cliente;
import es.pocketrainer.modelo.cliente.Cliente_;
import es.pocketrainer.modelo.cliente.Suscripcion_;
import es.pocketrainer.modelo.entrenador.Entrenador_;
import es.pocketrainer.modelo.valormaestro.ValorMaestro_;
import es.pocketrainer.util.Constantes.ClienteEstadoEnum;
import es.pocketrainer.util.Constantes.SuscripcionEstadoEnum;

public class ClienteRepositorioImpl implements ClienteRepositorioExtendido {

	@PersistenceContext
	EntityManager em;

	@Override
	public Cliente buscarClientePorUsuarioId(Long usuarioId) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Cliente> cq = cb.createQuery(Cliente.class);

		Root<Cliente> cliente = cq.from(Cliente.class);

		cq.where(cb.equal(cliente.get(Cliente_.USUARIO).get("id"), usuarioId));

		try {
			return em.createQuery(cq).getSingleResult();
		} catch (final NoResultException nre) {
			return null;
		}
	}

	@Override
	public Cliente buscarClientePorUsuarioNombre(String usuarioNombre) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Cliente> cq = cb.createQuery(Cliente.class);

		Root<Cliente> cliente = cq.from(Cliente.class);

		cq.where(cb.equal(cliente.get(Cliente_.USUARIO).get("usuarioNombre"), usuarioNombre));

		try {
			return em.createQuery(cq).getSingleResult();
		} catch (final NoResultException nre) {
			return null;
		}
	}

	@Override
	public List<Cliente> buscarClientePorEntrenador(Long entrenadorId) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Cliente> cq = cb.createQuery(Cliente.class);

		Root<Cliente> cliente = cq.from(Cliente.class);

		cq.where(cb.equal(cliente.get(Cliente_.ENTRENADOR).get("id"), entrenadorId));

		return em.createQuery(cq).getResultList();

	}

	@Override
	public List<Cliente> buscarClienteActivoLista() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Cliente> cq = cb.createQuery(Cliente.class);
		Root<Cliente> cliente = cq.from(Cliente.class);
		List<Predicate> predicates = new ArrayList<>();

		predicates.add(cb.equal(cliente.get(Cliente_.ESTADO).get(ValorMaestro_.CODIGO), ClienteEstadoEnum.ACTIVO.codigo()));
		predicates.add(cliente.get(Cliente_.SUSCRIPCION).get(Suscripcion_.ESTADO).get(ValorMaestro_.CODIGO).in(SuscripcionEstadoEnum.ACTIVA_NUEVA.codigo(),
				SuscripcionEstadoEnum.ACTIVA.codigo()));

		cq.where(predicates.toArray(new Predicate[predicates.size()]));
		return em.createQuery(cq).getResultList();
	}

	@Override
	public List<Cliente> buscarClientePorFiltro(BuscarClienteFormulario buscarClienteFormulario) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Cliente> cq = cb.createQuery(Cliente.class);

		Root<Cliente> cliente = cq.from(Cliente.class);
		List<Predicate> predicates = new ArrayList<>();

		if (StringUtils.isNotBlank(buscarClienteFormulario.getNombre())) {
			predicates.add(cb.like(cliente.get(Cliente_.NOMBRE), "%" + buscarClienteFormulario.getNombre() + "%"));
		}

		if (buscarClienteFormulario.getEntrenadorId() != null) {
			predicates.add(cb.equal(cliente.get(Cliente_.ENTRENADOR).get(Entrenador_.ID), buscarClienteFormulario.getEntrenadorId()));
		}

		if (buscarClienteFormulario.getEstadoCodigoLista() != null && !buscarClienteFormulario.getEstadoCodigoLista().isEmpty()) {
			predicates.add(cliente.get(Cliente_.ESTADO).get(ValorMaestro_.CODIGO).in(buscarClienteFormulario.getEstadoCodigoLista()));
		}

		if (buscarClienteFormulario.getSuscripcionEstadoCodigoLista() != null && !buscarClienteFormulario.getSuscripcionEstadoCodigoLista().isEmpty()) {
			predicates
					.add(cliente.get(Cliente_.SUSCRIPCION).get(Cliente_.ESTADO).get(ValorMaestro_.CODIGO).in(buscarClienteFormulario.getSuscripcionEstadoCodigoLista()));
		}

		cq.orderBy(cb.asc(cliente.get(Cliente_.ID)));
		cq.where(predicates.toArray(new Predicate[predicates.size()]));
		return em.createQuery(cq).getResultList();
	}
}
