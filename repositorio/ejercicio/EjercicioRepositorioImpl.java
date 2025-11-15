package es.pocketrainer.repositorio.ejercicio;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;

import es.pocketrainer.modelo.ejercicio.Ejercicio;
import es.pocketrainer.modelo.ejercicio.EjercicioMusculo_;
import es.pocketrainer.modelo.ejercicio.EjercicioParteCuerpo_;
import es.pocketrainer.modelo.ejercicio.Ejercicio_;
import es.pocketrainer.modelo.ejercicio.Material_;
import es.pocketrainer.modelo.ejercicio.Musculo_;
import es.pocketrainer.modelo.ejercicio.ParteCuerpo_;

public class EjercicioRepositorioImpl implements EjercicioRepositorioExtendido {

	@PersistenceContext
	EntityManager em;

	@Override
	public List<Ejercicio> buscarPorFiltroParaElaboracionRutina(BuscarEjercicioFormulario ejercicioBusquedaFiltro) {
		var cb = em.getCriteriaBuilder();
		var cq = cb.createQuery(Ejercicio.class);
		var ejercicio = cq.from(Ejercicio.class);
		var predicates = new ArrayList<>();

		cq.distinct(true);

		if (StringUtils.isNotBlank(ejercicioBusquedaFiltro.getNombre())) {
			predicates.add(cb.like(ejercicio.get(Ejercicio_.NOMBRE), "%" + ejercicioBusquedaFiltro.getNombre() + "%"));
		}

		if (StringUtils.isNotBlank(ejercicioBusquedaFiltro.getNombreCorto())) {
			predicates.add(cb.like(ejercicio.get(Ejercicio_.NOMBRE_CORTO), "%" + ejercicioBusquedaFiltro.getNombreCorto() + "%"));
		}

		if (StringUtils.isNotBlank(ejercicioBusquedaFiltro.getDescripcion())) {
			predicates.add(cb.like(ejercicio.get(Ejercicio_.DESCRIPCION), "%" + ejercicioBusquedaFiltro.getDescripcion() + "%"));
		}

		if (ejercicioBusquedaFiltro.getEsUnilateral() != null) {
			predicates.add(cb.equal(ejercicio.get(Ejercicio_.ES_UNILATERAL), ejercicioBusquedaFiltro.getEsUnilateral()));
		}

		if (ejercicioBusquedaFiltro.getTieneAudio() != null) {
			predicates.add(cb.equal(ejercicio.get(Ejercicio_.TIENE_AUDIO), ejercicioBusquedaFiltro.getTieneAudio()));
		}

		if (ejercicioBusquedaFiltro.getPatronIdLista() != null && !ejercicioBusquedaFiltro.getPatronIdLista().isEmpty()) {
			predicates.add(ejercicio.join(Ejercicio_.PATRON_LISTA).get("id").in(ejercicioBusquedaFiltro.getPatronIdLista()));
		}

		if (ejercicioBusquedaFiltro.getZonaIdLista() != null && !ejercicioBusquedaFiltro.getZonaIdLista().isEmpty()) {
			predicates.add(ejercicio.join(Ejercicio_.ZONA_LISTA).get("id").in(ejercicioBusquedaFiltro.getZonaIdLista()));
		}

		Join<Object, Object> materialLista = null;
		if (ejercicioBusquedaFiltro.isFetchMaterial()) {
			materialLista = (Join<Object, Object>) ejercicio.fetch(Ejercicio_.MATERIAL_LISTA, JoinType.LEFT);
		}

		if (ejercicioBusquedaFiltro.getMaterialIdLista() != null && !ejercicioBusquedaFiltro.getMaterialIdLista().isEmpty()) {
			if (!ejercicioBusquedaFiltro.isFetchMaterial()) {
				materialLista = ejercicio.join(Ejercicio_.MATERIAL_LISTA, JoinType.LEFT);
			}
			var materialId = materialLista.get(Material_.ID);
			predicates.add(cb.or(materialId.in(ejercicioBusquedaFiltro.getMaterialIdLista()), materialId.isNull()));

//			predicates.add(cb.or(ejercicio.join(Ejercicio_.MATERIAL_LISTA, JoinType.LEFT).get("id").in(ejercicioBusquedaFiltro.getMaterialIdLista()),
//					cb.isEmpty(ejercicio.get(Ejercicio_.MATERIAL_LISTA))));
		} else {
			predicates.add(cb.isEmpty(ejercicio.get(Ejercicio_.MATERIAL_LISTA)));
		}

		if (ejercicioBusquedaFiltro.getMusculoIdLista() != null && !ejercicioBusquedaFiltro.getMusculoIdLista().isEmpty()) {
			var ejercicioMusculo = ejercicio.join(Ejercicio_.EJERCICIO_MUSCULO_LISTA);
			predicates.add(ejercicioMusculo.join(EjercicioMusculo_.MUSCULO).get(Musculo_.ID).in(ejercicioBusquedaFiltro.getMusculoIdLista()));
			predicates.add(cb.equal(ejercicioMusculo.get(EjercicioMusculo_.PRINCIPAL), true));
		}

		if (ejercicioBusquedaFiltro.getParteCuerpoIdLista() != null && !ejercicioBusquedaFiltro.getParteCuerpoIdLista().isEmpty()) {
			var ejercicioParteCuerpo = ejercicio.join(Ejercicio_.EJERCICIO_PARTE_CUERPO_LISTA);
			predicates.add(ejercicioParteCuerpo.join(EjercicioParteCuerpo_.PARTE_CUERPO).get(ParteCuerpo_.ID).in(ejercicioBusquedaFiltro.getParteCuerpoIdLista()));
			predicates.add(cb.equal(ejercicioParteCuerpo.get(EjercicioParteCuerpo_.PRINCIPAL), true));
		}

		cq.where(predicates.toArray(new Predicate[predicates.size()]));

		return em.createQuery(cq).getResultList();
	}

	@Override
	public List<Ejercicio> buscarPorFiltro(BuscarEjercicioFormulario ejercicioBusquedaFiltro) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Ejercicio> cq = cb.createQuery(Ejercicio.class);
		cq.distinct(true);
		Root<Ejercicio> ejercicio = cq.from(Ejercicio.class);
		List<Predicate> predicates = new ArrayList<>();

		if (StringUtils.isNotBlank(ejercicioBusquedaFiltro.getNombre())) {
			predicates.add(cb.or(cb.like(ejercicio.get(Ejercicio_.NOMBRE), "%" + ejercicioBusquedaFiltro.getNombre() + "%"),
					cb.like(ejercicio.get(Ejercicio_.NOMBRE_CORTO), "%" + ejercicioBusquedaFiltro.getNombre() + "%")));
		}

		if (StringUtils.isNotBlank(ejercicioBusquedaFiltro.getDescripcion())) {
			predicates.add(cb.like(ejercicio.get(Ejercicio_.DESCRIPCION), "%" + ejercicioBusquedaFiltro.getDescripcion() + "%"));
		}

		if (ejercicioBusquedaFiltro.getEsUnilateral() != null) {
			predicates.add(cb.equal(ejercicio.get(Ejercicio_.ES_UNILATERAL), ejercicioBusquedaFiltro.getEsUnilateral()));
		}

		if (ejercicioBusquedaFiltro.getTieneAudio() != null) {
			predicates.add(cb.equal(ejercicio.get(Ejercicio_.TIENE_AUDIO), ejercicioBusquedaFiltro.getTieneAudio()));
		}

		if (ejercicioBusquedaFiltro.getPatronIdLista() != null && !ejercicioBusquedaFiltro.getPatronIdLista().isEmpty()) {
			predicates.add(ejercicio.join(Ejercicio_.PATRON_LISTA).get("id").in(ejercicioBusquedaFiltro.getPatronIdLista()));
		}

		if (ejercicioBusquedaFiltro.getZonaIdLista() != null && !ejercicioBusquedaFiltro.getZonaIdLista().isEmpty()) {
			predicates.add(ejercicio.join(Ejercicio_.ZONA_LISTA).get("id").in(ejercicioBusquedaFiltro.getZonaIdLista()));
		}

		if (ejercicioBusquedaFiltro.getMaterialIdLista() != null && !ejercicioBusquedaFiltro.getMaterialIdLista().isEmpty()) {
			predicates.add(ejercicio.join(Ejercicio_.MATERIAL_LISTA, JoinType.LEFT).get("id").in(ejercicioBusquedaFiltro.getMaterialIdLista()));
		}

		if (ejercicioBusquedaFiltro.getMusculoIdLista() != null && !ejercicioBusquedaFiltro.getMusculoIdLista().isEmpty()) {
			var ejercicioMusculo = ejercicio.join(Ejercicio_.EJERCICIO_MUSCULO_LISTA);
			predicates.add(ejercicioMusculo.join(EjercicioMusculo_.MUSCULO).get(Musculo_.ID).in(ejercicioBusquedaFiltro.getMusculoIdLista()));
		}

		if (ejercicioBusquedaFiltro.getParteCuerpoIdLista() != null && !ejercicioBusquedaFiltro.getParteCuerpoIdLista().isEmpty()) {
			var ejercicioParteCuerpo = ejercicio.join(Ejercicio_.EJERCICIO_PARTE_CUERPO_LISTA);
			predicates.add(ejercicioParteCuerpo.join(EjercicioParteCuerpo_.PARTE_CUERPO).get(ParteCuerpo_.ID).in(ejercicioBusquedaFiltro.getParteCuerpoIdLista()));
		}

		cq.orderBy(cb.asc(ejercicio.get(Ejercicio_.NOMBRE)));
		cq.where(predicates.toArray(new Predicate[predicates.size()]));
		return em.createQuery(cq).getResultList();
	}
}
