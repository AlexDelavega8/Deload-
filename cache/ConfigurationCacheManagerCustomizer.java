package es.pocketrainer.util.cache;

import static es.pocketrainer.util.Constantes.CACHE_EJERCICIO;
import static es.pocketrainer.util.Constantes.CACHE_EJERCICIO_ESTADISTICA;
import static es.pocketrainer.util.Constantes.CACHE_MATERIAL;
import static es.pocketrainer.util.Constantes.CACHE_MUSCULO;
import static es.pocketrainer.util.Constantes.CACHE_PARAMETROS_CONFIGURACION;
import static es.pocketrainer.util.Constantes.CACHE_PARTE_CUERPO;
import static es.pocketrainer.util.Constantes.CACHE_PATRON;
import static es.pocketrainer.util.Constantes.CACHE_ULTIMO_USO;
import static es.pocketrainer.util.Constantes.CACHE_VALORES_MAESTROS;

import java.util.Arrays;

import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizer;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.stereotype.Component;

@Component
public class ConfigurationCacheManagerCustomizer implements CacheManagerCustomizer<ConcurrentMapCacheManager> {

	@Override
	public void customize(ConcurrentMapCacheManager cacheManager) {
		cacheManager.setCacheNames(Arrays.asList(CACHE_VALORES_MAESTROS, CACHE_PARAMETROS_CONFIGURACION, CACHE_MATERIAL, CACHE_EJERCICIO, CACHE_PATRON, CACHE_MUSCULO,
				CACHE_PARTE_CUERPO, CACHE_ULTIMO_USO, CACHE_EJERCICIO_ESTADISTICA));
	}

}
