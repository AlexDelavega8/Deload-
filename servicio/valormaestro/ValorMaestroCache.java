package es.pocketrainer.servicio.valormaestro;

import static es.pocketrainer.util.Constantes.CACHE_VALORES_MAESTROS;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import es.pocketrainer.modelo.valormaestro.ValorMaestro;
import es.pocketrainer.repositorio.valormaestro.ValorMaestroRepositorio;

@Component
@Transactional
public class ValorMaestroCache {

	@Resource
	private ValorMaestroRepositorio valorMaestroRepositorio;

	@Cacheable(value = CACHE_VALORES_MAESTROS, key = "#root.methodName")
	public List<ValorMaestro> buscarValorMaestroTodos() {
		return valorMaestroRepositorio.findAll();
	}

}
