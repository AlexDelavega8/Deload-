package es.pocketrainer.repositorio.rutina;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;

import es.pocketrainer.formulario.BuscarPlantillaFormulario;
import es.pocketrainer.formulario.BuscarRutinaFormulario;
import es.pocketrainer.modelo.cliente.Cliente_;
import es.pocketrainer.modelo.entrenador.Entrenador_;
import es.pocketrainer.modelo.rutina.Rutina;
import es.pocketrainer.modelo.rutina.Rutina_;

public class RutinaRepositorioImpl implements RutinaRepositorioExtendido {

	@PersistenceContext
	EntityManager em;

	@Override
	public List<Rutina> buscarRutinaPorClienteFechaCreacion(Long clienteId, ZonedDateTime fechaCreacion) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Rutina> cq = cb.createQuery(Rutina.class);
		Root<Rutina> rutina = cq.from(Rutina.class);
		List<Predicate> predicates = new ArrayList<>();

		predicates.add(cb.equal(rutina.get(Rutina_.CLIENTE).get("id"), clienteId));
		predicates.add(cb.equal(rutina.get(Rutina_.FECHA_CREACION), fechaCreacion));

		cq.where(predicates.toArray(new Predicate[predicates.size()]));
		return em.createQuery(cq).getResultList();
	}

	@Override
	public List<Rutina> buscarRutinaPorClienteFechaCreacionEntreFechas(Long clienteId, ZonedDateTime fechaCreacionDesde, ZonedDateTime fechaCreacionHasta) {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Rutina> cq = cb.createQuery(Rutina.class);
		Root<Rutina> rutina = cq.from(Rutina.class);
		List<Predicate> predicates = new ArrayList<>();

		predicates.add(cb.equal(rutina.get(Rutina_.CLIENTE).get("id"), clienteId));
		predicates.add(cb.greaterThan(rutina.get(Rutina_.FECHA_CREACION), fechaCreacionDesde));
		predicates.add(cb.lessThan(rutina.get(Rutina_.FECHA_CREACION), fechaCreacionHasta));

		cq.where(predicates.toArray(new Predicate[predicates.size()]));
		return em.createQuery(cq).getResultList();

	}

	@Override
	public List<Rutina> buscarPlantillaPorFiltro(BuscarPlantillaFormulario buscarPlantillaFormulario) {
		var cb = em.getCriteriaBuilder();
		var cq = cb.createQuery(Rutina.class);
		var rutina = cq.from(Rutina.class);
		var predicates = new ArrayList<>();

		predicates.add(cb.equal(rutina.get(Rutina_.ES_PLANTILLA), Boolean.TRUE));

		if (StringUtils.isNotBlank(buscarPlantillaFormulario.getNombre())) {
			predicates.add(cb.like(rutina.get(Rutina_.NOMBRE), "%" + buscarPlantillaFormulario.getNombre() + "%"));
		}

		if (StringUtils.isNotBlank(buscarPlantillaFormulario.getComentariosEntrenador())) {
			predicates.add(cb.like(rutina.get(Rutina_.COMENTARIOS_ENTRENADOR), "%" + buscarPlantillaFormulario.getComentariosEntrenador() + "%"));
		}

		if (buscarPlantillaFormulario.getEntrenadorId() != null) {
			predicates.add(cb.equal(rutina.get(Cliente_.ENTRENADOR).get(Entrenador_.ID), buscarPlantillaFormulario.getEntrenadorId()));
		}

		cq.where(predicates.toArray(new Predicate[predicates.size()]));
		return em.createQuery(cq).getResultList();
	}

	@Override
	public List<Rutina> buscarRutinaPorFiltro(BuscarRutinaFormulario buscarRutinaFormulario) {
		var cb = em.getCriteriaBuilder();
		var cq = cb.createQuery(Rutina.class);
		var rutina = cq.from(Rutina.class);
		var predicates = new ArrayList<>();

		predicates.add(cb.equal(rutina.get(Rutina_.ES_PLANTILLA), Boolean.FALSE));

		if (buscarRutinaFormulario.getEntrenadorId() != null) {
			var entrenador = rutina.join(Rutina_.ENTRENADOR);
			predicates.add(cb.equal(entrenador.get(Entrenador_.ID), buscarRutinaFormulario.getEntrenadorId()));
		}

		if (buscarRutinaFormulario.getFechaCreacionDesde() != null) {
			predicates.add(cb.greaterThanOrEqualTo(rutina.get(Rutina_.FECHA_CREACION), buscarRutinaFormulario.getFechaCreacionDesde()));
		}

		if (buscarRutinaFormulario.getFechaCreacionHasta() != null) {
			predicates.add(cb.lessThanOrEqualTo(rutina.get(Rutina_.FECHA_CREACION), buscarRutinaFormulario.getFechaCreacionHasta()));
		}

		cq.where(predicates.toArray(new Predicate[predicates.size()]));
		return em.createQuery(cq).getResultList();
	}
}
