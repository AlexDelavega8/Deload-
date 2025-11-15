package es.pocketrainer.repositorio.cobro;

import java.util.List;

import es.pocketrainer.formulario.BuscarSuscripcionCobroListaFormulario;
import es.pocketrainer.modelo.cobro.SuscripcionCobro;

public interface SuscripcionCobroRepositorioExtendido {

	List<SuscripcionCobro> buscarSuscripcionCobroPorFiltro(BuscarSuscripcionCobroListaFormulario suscripcionCobroBusquedaFiltro);

	List<SuscripcionCobro> buscarSuscripcionCobroPorEstado(String codigo);

	SuscripcionCobro buscarSuscripcionCobroPorIdConBloqueoPesimista(Long suscripcionCobroId);
}
