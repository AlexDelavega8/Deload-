package es.pocketrainer.servicio.cobro;

import java.io.InputStream;
import java.util.List;

import es.pocketrainer.formulario.BuscarSuscripcionCobroListaFormulario;
import es.pocketrainer.modelo.cliente.Cliente;
import es.pocketrainer.modelo.cliente.Suscripcion;
import es.pocketrainer.modelo.cobro.SuscripcionCobro;
import es.pocketrainer.util.tpv.RespuestaPagoTpv;
import es.pocketrainer.util.tpv.SolicitudPagoInicial;

public interface CobroServicio {

	/**
	 * Genera la solicitud para el cobro mediante pasarela tpv en el pago inicial de
	 * un nuevo cliente. Internamente genera la transaccion asociada.
	 * 
	 * @param cliente el cliente que esta pagando
	 * @return los datos para enviar a la TPV
	 */
	SolicitudPagoInicial generarSolicitudPagoInicial(Cliente cliente);

	void gestionarRespuestaTpvPagoInicial(Long clienteId, Long suscripcionCobroId, RespuestaPagoTpv respuestaPagoTpv, String tipoNotificacion);

	SolicitudPagoInicial generarSolicitudPagoInicialDesdeCliente(Cliente cliente);

	List<SuscripcionCobro> buscarSuscripcionCobroPorFiltro(BuscarSuscripcionCobroListaFormulario suscripcionCobroBusquedaFiltro);

	InputStream crearExcelSuscripcionCobroLista(BuscarSuscripcionCobroListaFormulario suscripcionCobroBusquedaFiltro);

	void generarSolicitudRenovacion(Long suscripcionCobroId);

	void gestionarRespuestaTpvPagoRenovacion(Long clienteId, Long suscripcionCobroId, RespuestaPagoTpv respuestaPagoTpv, String notificacionAsincrona);

	/**
	 * Genera cobro de renovacion de suscripcion y lo devuelve ya con su
	 * identificador generado
	 * 
	 * @param suscripcion la suscripcion para la cual se genera cobro
	 * @return el cobro generado en estado inicial pendiente
	 */
	SuscripcionCobro generarSuscripcionCobroRenovacion(Suscripcion suscripcion);

	/**
	 * Busca cobros pendientes, que son aquellos de renovaciones
	 * 
	 * @return la lista de cobros pendientes
	 */
	List<SuscripcionCobro> buscarSuscripcionCobroPendiente();

	/**
	 * Genera una renovacion ya pagada para casos especiales como puede ser el de
	 * Antonio
	 * 
	 * @param suscripcion la suscripcion que renueva
	 * @return el cobro generado
	 */
	SuscripcionCobro generarSuscripcionCobroRenovacionYaPagada(Suscripcion suscripcion);

	InputStream generarFacturacionHoldedXLS(BuscarSuscripcionCobroListaFormulario suscripcionCobroBusquedaFiltro);

}
