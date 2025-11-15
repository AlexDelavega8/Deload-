package es.pocketrainer.repositorio.videoconferencia;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import es.pocketrainer.modelo.valormaestro.ValorMaestro_;
import es.pocketrainer.modelo.videoconferencia.Videoconferencia;
import es.pocketrainer.modelo.videoconferencia.Videoconferencia_;
import es.pocketrainer.util.Constantes;

public class VideoconferenciaRepositorioImpl implements VideoconferenciaRepositorioExtendido {

	@PersistenceContext
	EntityManager em;

	@Override
	public List<Videoconferencia> buscarProximasVideoConferencias() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Videoconferencia> cq = cb.createQuery(Videoconferencia.class);
		cq.distinct(true);
		Root<Videoconferencia> videoconferencia = cq.from(Videoconferencia.class);
		List<Predicate> predicates = new ArrayList<>();

		// Estado programada o en curso por si el entrenador se le dio por darle antes
		predicates.add(videoconferencia.get(Videoconferencia_.ESTADO).get(ValorMaestro_.CODIGO)
				.in(Constantes.VideoconferenciaEstadoEnum.PROGRAMADA.codigo(), Constantes.VideoconferenciaEstadoEnum.EN_CURSO.codigo()));

		var fechaAhora = ZonedDateTime.now();

		// Menos de 15 minutos para comienzo
		predicates.add(
				cb.between(videoconferencia.get(Videoconferencia_.FECHA_HORA_PROGRAMADA), fechaAhora.plusMinutes(10), fechaAhora.plusMinutes(15)));

		cq.where(predicates.toArray(new Predicate[predicates.size()]));
		return em.createQuery(cq).getResultList();
	}

}
