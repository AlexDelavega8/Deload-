package es.pocketrainer.servicio.valormaestro;

import java.util.List;

import es.pocketrainer.modelo.valormaestro.ValorMaestro;
import es.pocketrainer.util.Constantes.ValorMaestroTipoEnum;

public interface ValorMaestroServicio {

	List<ValorMaestro> buscarValorMaestroTodos();

	ValorMaestro buscarValorMaestroPorCodigo(String codigo);

	List<ValorMaestro> buscarValorMaestroListaPorCodigoTipo(ValorMaestroTipoEnum valorMaestroTipoEnum);

	List<ValorMaestro> buscarValorMaestroListaPorCodigoPadre(String codigoPadre);

}
