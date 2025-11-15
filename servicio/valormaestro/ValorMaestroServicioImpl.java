package es.pocketrainer.servicio.valormaestro;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.pocketrainer.modelo.valormaestro.ValorMaestro;
import es.pocketrainer.repositorio.valormaestro.ValorMaestroRepositorio;
import es.pocketrainer.util.Constantes.ValorMaestroTipoEnum;

@Service("ValorMaestroServicio")

public class ValorMaestroServicioImpl implements ValorMaestroServicio {

	@Resource
	private ValorMaestroRepositorio valorMaestroRepositorio;

	@Resource
	private ValorMaestroCache valorMaestroCache;

	@Transactional
	@EventListener(ApplicationReadyEvent.class)
	public void inicializarCache() {
		buscarValorMaestroTodos();
	}

	@Override
	public List<ValorMaestro> buscarValorMaestroTodos() {
		return valorMaestroCache.buscarValorMaestroTodos();
	}

	@Override
	public ValorMaestro buscarValorMaestroPorCodigo(String codigo) {
		return valorMaestroCache.buscarValorMaestroTodos().stream().filter((it) -> it.getCodigo().equals(codigo)).findFirst().orElse(null);
	}

	@Override
	public List<ValorMaestro> buscarValorMaestroListaPorCodigoTipo(ValorMaestroTipoEnum valorMaestroTipoEnum) {
		return valorMaestroCache.buscarValorMaestroTodos().stream()
				.filter((it) -> it.getCodigoTipo().equals(valorMaestroTipoEnum.codigo()) && !it.getCodigo().equals(valorMaestroTipoEnum.codigo()))
				.collect(Collectors.toList());
	}

	@Override
	public List<ValorMaestro> buscarValorMaestroListaPorCodigoPadre(String codigoPadre) {
		return valorMaestroCache.buscarValorMaestroTodos().stream().filter((it) -> it.getCodigoPadre().equals(codigoPadre))
				.collect(Collectors.toList());
	}

}
