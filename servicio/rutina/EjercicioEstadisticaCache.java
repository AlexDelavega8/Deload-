package es.pocketrainer.servicio.rutina;

import static es.pocketrainer.util.Constantes.CACHE_EJERCICIO_ESTADISTICA;

import javax.annotation.Resource;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import es.pocketrainer.repositorio.ejercicio.EjercicioEntrenadorEstadisticaRepositorio;
import es.pocketrainer.repositorio.ejercicio.EjercicioRepositorio;;

@Component
@Transactional
public class EjercicioEstadisticaCache {

	@Resource
	private EjercicioRepositorio ejercicioRepositorio;

	@Resource
	private EjercicioEntrenadorEstadisticaRepositorio ejercicioEntrenadorEstadisticaRepositorio;

	@CacheEvict(value = CACHE_EJERCICIO_ESTADISTICA)
	public void borrarCacheUnaEntrada(Long entrenadorId, Long ejercicioId) {
		// Solo borra una entrada
	}

	@CacheEvict(value = CACHE_EJERCICIO_ESTADISTICA, allEntries = true)
	public void borrarCacheTodo() {
		// Borra todo
	}

	@Cacheable(value = CACHE_EJERCICIO_ESTADISTICA)
	public Pair<Integer, Integer> getEjercicioRepeticionesTiempoPorEstadistica(Long entrenadorId, Long ejercicioId) {
		var ejercicio = ejercicioRepositorio.findById(ejercicioId).orElse(null);
		var repeticiones = 0;
		var tiempo = 0;
		var ejercicioEntrenadorEstadistica = ejercicioEntrenadorEstadisticaRepositorio.findByIdEntrenadorIdAndIdEjercicioId(entrenadorId, ejercicioId);
		if (ejercicioEntrenadorEstadistica != null) {
			if (ejercicioEntrenadorEstadistica.getNumeroUsosPorRepeticion() > ejercicioEntrenadorEstadistica.getNumeroUsosPorTiempo()) {
				repeticiones = ejercicioEntrenadorEstadistica.getConfiguracionRepeticionesMasUsado();
			} else if (ejercicioEntrenadorEstadistica.getNumeroUsosPorRepeticion() < ejercicioEntrenadorEstadistica.getNumeroUsosPorTiempo()) {
				tiempo = ejercicioEntrenadorEstadistica.getConfiguracionTiempoMasUsado();
			}
		} else if (ejercicio.getNumeroUsosPorRepeticion() != null) {
			if (ejercicio.getNumeroUsosPorRepeticion() > ejercicio.getNumeroUsosPorTiempo()) {
				repeticiones = ejercicio.getConfiguracionRepeticionesMasUsado();
			} else if (ejercicio.getNumeroUsosPorRepeticion() < ejercicio.getNumeroUsosPorTiempo()) {
				tiempo = ejercicio.getConfiguracionTiempoMasUsado();
			}
		}

		return Pair.of(repeticiones, tiempo);
	}

}
