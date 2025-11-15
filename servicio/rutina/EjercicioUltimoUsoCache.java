package es.pocketrainer.servicio.rutina;

import static es.pocketrainer.util.Constantes.CACHE_ULTIMO_USO;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import es.pocketrainer.modelo.ejercicio.Ejercicio;
import es.pocketrainer.modelo.rutina.Rutina;
import es.pocketrainer.repositorio.rutina.RutinaRepositorio;
import es.pocketrainer.repositorio.valormaestro.ValorMaestroRepositorio;
import es.pocketrainer.servicio.cliente.ClienteServicio;;

@Component
@Transactional
public class EjercicioUltimoUsoCache {

	@Resource
	private ValorMaestroRepositorio valorMaestroRepositorio;

	@Resource
	private ClienteServicio clienteServicio;

	@Resource
	private RutinaRepositorio rutinaRepositorio;

	@CacheEvict(value = CACHE_ULTIMO_USO)
	public void borrarCacheUnaEntrada(Long clienteId, ZonedDateTime fechaUltimaActivacion, Integer mesesActiva, ZonedDateTime fechaPeriodoDesde, Long rutinaId) {
		// Solo borra una entrada
	}

	@CacheEvict(value = CACHE_ULTIMO_USO, allEntries = true)
	public void borrarCacheTodo() {
		// Borra todo
	}

	@Cacheable(value = CACHE_ULTIMO_USO)
	public Map<Integer, List<Long>> getClasificacionEjercicioUltimoUso(Long clienteId, ZonedDateTime fechaUltimaActivacion, Integer mesesActiva,
			ZonedDateTime fechaPeriodoDesde, Long rutinaId) {

		var mapaUltimoUso = new HashMap<Integer, List<Long>>();

		var ejercicioListaRutinaActual = new HashSet<Ejercicio>();

		// Partir de la suposición de que ya hay rutinas nuevas, que esta en los 5 dias
		// previos a renovación real
		var fechaInicioActual = clienteServicio.calcularProximaSuscripcionFechaPeriodoDesde(fechaUltimaActivacion, mesesActiva);
		int mesesReferencia = 0;
		var rutinaActualLista = buscarRutinaPorClienteFechaCreacion(clienteId, fechaInicioActual);

		// Si no hay rutinas, es que aun estamos en medio de suscripcion
		if (rutinaActualLista.isEmpty()) {
			// Estamos en periodo donde ya hay nuevas rutinas, calculamos fecha de nuevas
			// rutinas ya creadas
			fechaInicioActual = fechaPeriodoDesde;
			mesesReferencia = 1;
			rutinaActualLista = buscarRutinaPorClienteFechaCreacion(clienteId, fechaInicioActual);
		}

		for (Rutina rutina : rutinaActualLista) {
			// Descartar la que se va a elaborar claro
			if (rutinaId == null || !rutina.getId().equals(rutinaId)) {
				ejercicioListaRutinaActual.addAll(rutina.getEjercicioTodos());
			}
		}

		mapaUltimoUso.put(0, ejercicioListaRutinaActual.stream().map(it -> it.getId()).collect(Collectors.toList()));

		var ejercicioListaRutina1MesAntes = new HashSet<Ejercicio>();
		var fechaInicio1MesAntes = fechaPeriodoDesde.minusMonths(0 + mesesReferencia);
		var rutina1MesAntesLista = buscarRutinaPorClienteFechaCreacion(clienteId, fechaInicio1MesAntes);
		if (rutina1MesAntesLista.isEmpty()) {
			// Quiza paro unos dias y reactivo, le damos margen de 15 dias
			var fechaInicio1MesAntesConMargen = fechaInicio1MesAntes.minusDays(15);
			rutina1MesAntesLista = buscarRutinaPorClienteFechaCreacionEntreFechas(clienteId, fechaInicio1MesAntesConMargen, fechaInicio1MesAntes);
		}

		for (Rutina rutina : rutina1MesAntesLista) {
			ejercicioListaRutina1MesAntes.addAll(rutina.getEjercicioTodos());
		}

		mapaUltimoUso.put(1, ejercicioListaRutina1MesAntes.stream().map(it -> it.getId()).collect(Collectors.toList()));

		var ejercicioListaRutina2MesAntes = new HashSet<Ejercicio>();
		var fechaInicio2MesAntes = fechaPeriodoDesde.minusMonths(1 + mesesReferencia);
		var rutina2MesAntesLista = buscarRutinaPorClienteFechaCreacion(clienteId, fechaInicio2MesAntes);
		if (rutina2MesAntesLista.isEmpty()) {
			// Quiza paro unos dias y reactivo, le damos margen de 15 dias
			var fechaInicio2MesAntesConMargen = fechaInicio2MesAntes.minusDays(15);
			rutina2MesAntesLista = buscarRutinaPorClienteFechaCreacionEntreFechas(clienteId, fechaInicio2MesAntesConMargen, fechaInicio2MesAntes);
		}
		for (Rutina rutina : rutina2MesAntesLista) {
			ejercicioListaRutina2MesAntes.addAll(rutina.getEjercicioTodos());
		}

		mapaUltimoUso.put(2, ejercicioListaRutina2MesAntes.stream().map(it -> it.getId()).collect(Collectors.toList()));

		var ejercicioListaRutina3MesAntes = new HashSet<Ejercicio>();
		var fechaInicio3MesAntes = fechaPeriodoDesde.minusMonths(2 + mesesReferencia);
		var rutina3MesAntesLista = buscarRutinaPorClienteFechaCreacion(clienteId, fechaInicio3MesAntes);
		if (rutina3MesAntesLista.isEmpty()) {
			// Quiza paro unos dias y reactivo, le damos margen de 15 dias
			var fechaInicio3MesAntesConMargen = fechaInicio3MesAntes.minusDays(15);
			rutina3MesAntesLista = buscarRutinaPorClienteFechaCreacionEntreFechas(clienteId, fechaInicio3MesAntesConMargen, fechaInicio3MesAntes);
		}
		for (Rutina rutina : rutina3MesAntesLista) {
			ejercicioListaRutina3MesAntes.addAll(rutina.getEjercicioTodos());
		}

		mapaUltimoUso.put(3, ejercicioListaRutina3MesAntes.stream().map(it -> it.getId()).collect(Collectors.toList()));

		var ejercicioListaRutina4a12MesAntes = new HashSet<Ejercicio>();
		var fechaInicio4a12MesAntes = fechaPeriodoDesde.minusMonths(3 + mesesReferencia);
		var rutina4a12MesAntesLista = buscarRutinaPorClienteFechaCreacion(clienteId, fechaInicio4a12MesAntes);
		if (rutina4a12MesAntesLista.isEmpty()) {
			// Quiza paro unos dias y reactivo, le damos margen de 15 dias
			var fechaInicio4a12MesAntesConMargen = fechaInicio4a12MesAntes.minusDays(15);
			rutina4a12MesAntesLista = buscarRutinaPorClienteFechaCreacionEntreFechas(clienteId, fechaInicio4a12MesAntesConMargen, fechaInicio4a12MesAntes);
		}
		for (Rutina rutina : rutina4a12MesAntesLista) {
			ejercicioListaRutina4a12MesAntes.addAll(rutina.getEjercicioTodos());
		}

		mapaUltimoUso.put(4, ejercicioListaRutina4a12MesAntes.stream().map(it -> it.getId()).collect(Collectors.toList()));

		return mapaUltimoUso;
	}

	private List<Rutina> buscarRutinaPorClienteFechaCreacionEntreFechas(Long clienteId, ZonedDateTime fechaCreacionDesde, ZonedDateTime fechaCreacionHasta) {
		return rutinaRepositorio.buscarRutinaPorClienteFechaCreacionEntreFechas(clienteId, fechaCreacionDesde, fechaCreacionHasta);
	}

	private List<Rutina> buscarRutinaPorClienteFechaCreacion(Long clienteId, ZonedDateTime fechaCreacion) {
		return rutinaRepositorio.buscarRutinaPorClienteFechaCreacion(clienteId, fechaCreacion);
	}

}
