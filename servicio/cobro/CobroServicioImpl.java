package es.pocketrainer.servicio.cobro;

import static es.pocketrainer.util.Constantes.NOTIFICACION_ASINCRONA;
import static es.pocketrainer.util.Constantes.NOTIFICACION_SINCRONA_KO;
import static es.pocketrainer.util.Constantes.NOTIFICACION_SINCRONA_OK;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import es.pocketrainer.formulario.BuscarSuscripcionCobroListaFormulario;
import es.pocketrainer.modelo.cliente.Cliente;
import es.pocketrainer.modelo.cliente.ClienteDatosCobro;
import es.pocketrainer.modelo.cliente.Suscripcion;
import es.pocketrainer.modelo.cobro.SuscripcionCobro;
import es.pocketrainer.repositorio.cobro.SuscripcionCobroRepositorio;
import es.pocketrainer.servicio.cliente.ClienteServicio;
import es.pocketrainer.servicio.notificaciones.NotificacionServicio;
import es.pocketrainer.servicio.valormaestro.ValorMaestroServicio;
import es.pocketrainer.util.Constantes;
import es.pocketrainer.util.Constantes.SuscripcionCobroEstadoEnum;
import es.pocketrainer.util.Constantes.SuscripcionEstadoEnum;
import es.pocketrainer.util.FechaUtil;
import es.pocketrainer.util.NumeroUtil;
import es.pocketrainer.util.tpv.ApiMacSha256;
import es.pocketrainer.util.tpv.ParametrosNotificacionTpv;
import es.pocketrainer.util.tpv.RespuestaPagoTpv;
import es.pocketrainer.util.tpv.SolicitudPagoInicial;
import es.pocketrainer.util.tpv.SolicitudPagoRenovacion;

@Service
public class CobroServicioImpl implements CobroServicio {

	private static Logger LOGGER = LoggerFactory.getLogger(CobroServicioImpl.class);

	private final String SERVICIO_PAGO_INICIAL_SUFIJO = "pago inicial";
	private final String SERVICIO_PAGO_REACTIVACION_SUFIJO = "pago reactivacion";
	private final String SERVICIO_PAGO_RENOVACION_SUFIJO = "pago renovacion";
	private final DecimalFormat df = new DecimalFormat("#.00");

	@Resource
	private ObjectMapper objectMapper;

	@Resource
	private ValorMaestroServicio valorMaestroServicio;

	@Resource
	private SuscripcionCobroRepositorio suscripcionCobroRepositorio;

	@Resource
	private ClienteServicio clienteServicio;

	@Resource
	private NotificacionServicio notificacionServicio;

	@Value("${tpv.urlpago}")
	private String urlPago;

	@Value("${tpv.urlpagorest}")
	private String urlPagoRest;

	@Value("${tpv.clavesecreta}")
	private String claveSecreta;

	@Value("${tpv.codigocomercio}")
	private String codigoComercio;

	@Value("${tpv.terminal}")
	private String terminal;

	@Value("${tpv.moneda}")
	private String moneda;

	@Value("${tpv.url.notificacionasincrona}")
	private String urlNotificacionAsincrona;

	@Value("${tpv.url.notificacionsincronaok}")
	private String urlNotificacionSincronaOk;

	@Value("${tpv.url.notificacionsincronako}")
	private String urlNotificacionSincronaKo;

	@Transactional
	@Override
	public SolicitudPagoInicial generarSolicitudPagoInicialDesdeCliente(Cliente cliente) {
		return generarSolicitudPagoInicial(cliente, urlNotificacionAsincrona + "Cliente", urlNotificacionSincronaOk + "Cliente", urlNotificacionSincronaKo + "Cliente");
	}

	@Transactional
	@Override
	public SolicitudPagoInicial generarSolicitudPagoInicial(Cliente cliente) {
		return generarSolicitudPagoInicial(cliente, urlNotificacionAsincrona, urlNotificacionSincronaOk, urlNotificacionSincronaKo);
	}

	private void peticionCobroPorEntradaREST(Cliente cliente, SuscripcionCobro suscripcionCobro) {

		var solicitudPagoRenovacion = new SolicitudPagoRenovacion();
		solicitudPagoRenovacion.setUrlPago(urlPagoRest);

		var servicio = cliente.getSuscripcion().getServicio();
		var cantidad = df.format(servicio.getPrecio()).replaceAll("[.]", "").replaceAll(",", "");
		var numeroPedido = StringUtils.leftPad(suscripcionCobro.getId().toString(), 12, "0");
		var transaccionTipoAutorizacion = "0";
		var servicioDescripcion = servicio.getNombre().concat(" ").concat(SERVICIO_PAGO_RENOVACION_SUFIJO);
		var titular = cliente.getNombreCompleto();
		var urlNotificacionAsincronaFinal = urlNotificacionAsincrona + "Renovacion" + "/" + cliente.getId() + "/" + suscripcionCobro.getId();
		var tarjetaIdentificacionToken = cliente.getClienteDatosCobro().getTarjetaIdentificacionToken();
		var identificadorTransaccionCof = cliente.getClienteDatosCobro().getIdentificadorTransaccionCof() != null
				&& !cliente.getClienteDatosCobro().getIdentificadorTransaccionCof().isBlank() ? cliente.getClienteDatosCobro().getIdentificadorTransaccionCof()
						: "999999999999999";

		final ApiMacSha256 apiMacSha256 = new ApiMacSha256();

		// SIGNATURE VERSION
		solicitudPagoRenovacion.setDs_SignatureVersion("HMAC_SHA256_V1");

		// DATOS
		apiMacSha256.setParameter("DS_MERCHANT_AMOUNT", cantidad);
		apiMacSha256.setParameter("DS_MERCHANT_ORDER", numeroPedido);
		apiMacSha256.setParameter("DS_MERCHANT_MERCHANTCODE", codigoComercio);
		apiMacSha256.setParameter("DS_MERCHANT_CURRENCY", moneda);
		apiMacSha256.setParameter("DS_MERCHANT_TRANSACTIONTYPE", transaccionTipoAutorizacion);
		apiMacSha256.setParameter("DS_MERCHANT_TERMINAL", terminal);
		apiMacSha256.setParameter("DS_MERCHANT_MERCHANTURL", urlNotificacionAsincronaFinal);
		apiMacSha256.setParameter("Ds_Merchant_ProductDescription", servicioDescripcion);
		apiMacSha256.setParameter("Ds_Merchant_Titular", titular);
		// Enviar tokenizacion de tarjeta
		apiMacSha256.setParameter("DS_MERCHANT_IDENTIFIER", tarjetaIdentificacionToken);
		// Indicar que es pago sin autenticacion
		apiMacSha256.setParameter("Ds_Merchant_DirectPayment", "true");
		// Indica NO es primera transaccion COF
		apiMacSha256.setParameter("DS_MERCHANT_COF_INI", "N");
		// Tipo transaccion COF recurring (recurrente) *susceptible de ser opcional
		apiMacSha256.setParameter("DS_MERCHANT_COF_TYPE", "R");
		// Identificador transacción COF *susceptible de ser opcional
		apiMacSha256.setParameter("DS_MERCHANT_COF_TXNID", identificadorTransaccionCof);
		// Se indica excepcion MIT ya que no se va a autenticar
		apiMacSha256.setParameter("DS_MERCHANT_EXCEP_SCA", "MIT");

		try {
			solicitudPagoRenovacion.setDs_MerchantParameters(apiMacSha256.createMerchantParameters());
		} catch (final UnsupportedEncodingException e) {
			LOGGER.error("Error generando datos para cobro de renovacion de suscripcion: No se pudo completar la asignacion de parametros de la peticion", e);
		}

		// SIGNATURE
		try {
			solicitudPagoRenovacion.setDs_Signature(apiMacSha256.createMerchantSignature(claveSecreta));
		} catch (Exception e) {
			LOGGER.error("Error generando datos para cobro de renovacion de suscripcion: No se pudo firmar la peticion", e);
		}

		// Peticion post a servicio rest
		// Que puede pasar:
		// 1. Rompe petición directamente
		// 2. Rest responde directamente un json solo con errorCode
		// 3. Rest responde ok 200, pero se produce error mapeando campos o el codigo de
		// respuesta en parameters es distinto de OK
		var respuestaPagoTpvString = "";
		try {
			// Peticion
			var restTemplate = new RestTemplate();
			respuestaPagoTpvString = restTemplate.postForObject(solicitudPagoRenovacion.getUrlPago(), solicitudPagoRenovacion, String.class);
			LOGGER.info("Petición renovación rest: Respuesta de peticion REST en renovacion de suscripcion: " + respuestaPagoTpvString);

			// Mapeo json respuesta (automatico no iba bien)
			var respuestaPagoTpv = new RespuestaPagoTpv(objectMapper.readTree(respuestaPagoTpvString));

			// Respuesta rest: error o datos a procesar
			if (respuestaPagoTpv.getErrorCode() != null && !respuestaPagoTpv.getErrorCode().isBlank()) {
				LOGGER.info("Petición renovación rest: Código de error en la respuesta de la petición REST en renovación de suscripción: "
						+ respuestaPagoTpv.getErrorCode() + " - "
						+ (Constantes.REDSYS_ERROR_MENSAJE_MAPA.get(respuestaPagoTpv.getErrorCode()) != null
								? Constantes.REDSYS_ERROR_MENSAJE_MAPA.get(respuestaPagoTpv.getErrorCode())
								: "Error no conocido"));
				respuestaRenovacionKo(suscripcionCobro, respuestaPagoTpv.getErrorCode());
			} else {
				var parametrosRespuesta = crearParametrosNotificacionTpvRespuestaRest(decodificarParametros(respuestaPagoTpv));
				LOGGER.info("Petición renovación rest: Parametros extraidos de la respuesta a peticion REST en renovacion de suscripcion: " + parametrosRespuesta);

				// ¿Responde con codigo de respuesta distinto de ok?
				// Log y aviso al tecnico al menos al principio, luego podría valorarse quitar
				// la notificacion
				if (parametrosRespuesta.getCodigoRespuesta().equals("0000")) {
					notificacionSincronaRenovacionOk(suscripcionCobro, parametrosRespuesta);
				} else {
					var error = parametrosRespuesta.getCodigoRespuesta() + " - "
							+ (Constantes.REDSYS_ERROR_MENSAJE_MAPA.get(parametrosRespuesta.getCodigoRespuesta()) != null
									? Constantes.REDSYS_ERROR_MENSAJE_MAPA.get(parametrosRespuesta.getCodigoRespuesta())
									: "Código de respuesta no conocido");
					LOGGER.error("Petición renovación rest: Parece que hubo algun error en el cobro de renovacion de cuota para el cliente: " + cliente.getCodigo()
							+ ". Codigo de respuesta: " + error);
					notificacionSincronaRenovacionKo(suscripcionCobro, parametrosRespuesta);
					notificacionServicio.notificarEntrenadorErrorCobroRenovacion(cliente, error);
				}
			}
		} catch (Exception e) {
			// Cualquier error no controlado: log y aviso a tecnico
			LOGGER.error("Petición renovación rest: Error extrayendo datos de la respuesta a peticion REST en renovacion de suscripcion: "
					+ (respuestaPagoTpvString.isBlank() ? "No hay respuesta" : respuestaPagoTpvString), e);
			LOGGER.error("Petición renovación rest: Parece que hubo algun error en el cobro de renovacion de cuota para el cliente: " + cliente.getCodigo());
			respuestaRenovacionKo(suscripcionCobro, "Excepcion");
		}

	}

	@Transactional
	@Override
	public void generarSolicitudRenovacion(Long suscripcionCobroId) {

		// Recupero cobro con bloqueo pesimista
		var suscripcionCobro = suscripcionCobroRepositorio.buscarSuscripcionCobroPorIdConBloqueoPesimista(suscripcionCobroId);
		var cliente = suscripcionCobro.getSuscripcion().getCliente();

		// Cambiar estado de pendiente a emitido para el cobro. Segun vaya la peticion
		// rest, puede cambiar
		suscripcionCobro.setEstado(valorMaestroServicio.buscarValorMaestroPorCodigo(Constantes.SuscripcionCobroEstadoEnum.EMITIDO.codigo()));

		// Peticion por entrada rest
		peticionCobroPorEntradaREST(cliente, suscripcionCobro);

		LOGGER.info("Finalizado envio cobro de renovacion a TPV para cliente " + cliente.getCodigo());
	}

	private SolicitudPagoInicial generarSolicitudPagoInicial(Cliente cliente, String urlNotificacionAsincrona, String urlNotificacionSincronaOk,
			String urlNotificacionSincronaKo) {
		SolicitudPagoInicial solicitudPagoInicial = new SolicitudPagoInicial();
		solicitudPagoInicial.setUrlPago(urlPago);

		// Generar cobro (pensar como mandar a cancelado si el tio pasa de todo)
		var suscripcionCobro = generarSuscripcionCobro(cliente.getSuscripcion());

		// Rellenar datos para solicitud
		var servicio = cliente.getSuscripcion().getServicio();

		var cantidad = df.format(servicio.getPrecio()).replaceAll("[.]", "").replaceAll(",", "");

		// Promocion inicial
		var precioPromocion = new BigDecimal(19.99D);
		var enPeriodoPromocion = (ZonedDateTime.now().isBefore(ZonedDateTime.of(2020, 8, 1, 0, 0, 0, 0, ZoneId.of("Europe/Madrid"))));
		if (cliente.getSuscripcion().getEstado().getCodigo().equals(Constantes.SuscripcionEstadoEnum.IMPAGADA_INICIAL.codigo()) && enPeriodoPromocion) {
			cantidad = df.format(precioPromocion).replaceAll("[.]", "").replaceAll(",", "");
		}

		var numeroPedido = StringUtils.leftPad(suscripcionCobro.getId().toString(), 12, "0");
		var transaccionTipoAutorizacion = "0";
		var servicioDescripcion = servicio.getNombre().concat(" ").concat(SERVICIO_PAGO_INICIAL_SUFIJO);
		var titular = cliente.getNombreCompleto();

		var urlNotificacionAsincronaFinal = urlNotificacionAsincrona + "/" + cliente.getId() + "/" + suscripcionCobro.getId();
		var urlNotificacionSincronaOkFinal = urlNotificacionSincronaOk + "/" + cliente.getId() + "/" + suscripcionCobro.getId();
		var urlNotificacionSincronaKoFinal = urlNotificacionSincronaKo + "/" + cliente.getId() + "/" + suscripcionCobro.getId();

		final ApiMacSha256 apiMacSha256 = new ApiMacSha256();

		// SIGNATURE VERSION
		solicitudPagoInicial.setDsSignatureVersion("HMAC_SHA256_V1");

		// DATOS
		apiMacSha256.setParameter("DS_MERCHANT_AMOUNT", cantidad);
		apiMacSha256.setParameter("DS_MERCHANT_ORDER", numeroPedido);
		apiMacSha256.setParameter("DS_MERCHANT_MERCHANTCODE", codigoComercio);
		apiMacSha256.setParameter("DS_MERCHANT_CURRENCY", moneda);
		apiMacSha256.setParameter("DS_MERCHANT_TRANSACTIONTYPE", transaccionTipoAutorizacion);
		apiMacSha256.setParameter("DS_MERCHANT_TERMINAL", terminal);
		// Solicitar tokenizacion de tarjeta
		apiMacSha256.setParameter("DS_MERCHANT_IDENTIFIER", "REQUIRED");
		apiMacSha256.setParameter("DS_MERCHANT_MERCHANTURL", urlNotificacionAsincronaFinal);
		apiMacSha256.setParameter("DS_MERCHANT_URLOK", urlNotificacionSincronaOkFinal);
		apiMacSha256.setParameter("DS_MERCHANT_URLKO", urlNotificacionSincronaKoFinal);
		apiMacSha256.setParameter("Ds_Merchant_ProductDescription", servicioDescripcion);
		apiMacSha256.setParameter("Ds_Merchant_Titular", titular);
		// Indica primera transaccion COF
		apiMacSha256.setParameter("DS_MERCHANT_COF_INI", "S");
		// Tipo transaccion COF recurring (recurrente) *susceptible de ser opcional
		apiMacSha256.setParameter("DS_MERCHANT_COF_TYPE", "R");

		try {
			solicitudPagoInicial.setDsMerchantParameters(apiMacSha256.createMerchantParameters());
		} catch (final UnsupportedEncodingException e) {
			LOGGER.error("Error generando datos para cobro inicial de suscripcion: No se pudo completar la asignacion de parametros de la peticion", e);
		}

		// SIGNATURE
		try {
			solicitudPagoInicial.setDsSignature(apiMacSha256.createMerchantSignature(claveSecreta));
		} catch (Exception e) {
			LOGGER.error("Error generando datos para cobro inicial de suscripcion: No se pudo firmar la peticion", e);
		}

		return solicitudPagoInicial;
	}

	/**
	 * Genera cobro de suscripcion y lo devuelve ya con su identificador generado
	 * 
	 * @param suscripcion la suscripcion para la cual se genera cobro
	 * @return el cobro generado en estado inicial pendiente
	 */
	private SuscripcionCobro generarSuscripcionCobro(Suscripcion suscripcion) {
		SuscripcionCobro suscripcionCobro = new SuscripcionCobro();

		suscripcionCobro.setCantidad(suscripcion.getServicio().getPrecio());

		// Promocion inicial
		var precioPromocion = new BigDecimal(19.99D);
		var enPeriodoPromocion = (ZonedDateTime.now().isBefore(ZonedDateTime.of(2020, 8, 1, 0, 0, 0, 0, ZoneId.of("Europe/Madrid"))));
		if (suscripcion.getEstado().getCodigo().equals(Constantes.SuscripcionEstadoEnum.IMPAGADA_INICIAL.codigo()) && enPeriodoPromocion) {
			suscripcionCobro.setCantidad(precioPromocion);
		}

		// Emitido, no es necesario pasar por pendiente como en las renovaciones
		// (recordar problemas transacion/respuesta asincrona)
		suscripcionCobro.setEstado(valorMaestroServicio.buscarValorMaestroPorCodigo(SuscripcionCobroEstadoEnum.EMITIDO.codigo()));
		suscripcionCobro.setFechaHoraEmision(ZonedDateTime.now());
		suscripcionCobro.setFechaPeriodoDesde(suscripcion.getFechaPeriodoDesde());
		suscripcionCobro.setFechaPeriodoHasta(suscripcion.getFechaPeriodoHasta());
		suscripcionCobro.setSuscripcion(suscripcion);
		suscripcionCobro.setEntrenador(suscripcion.getCliente().getEntrenador());

		return suscripcionCobroRepositorio.saveAndFlush(suscripcionCobro);
	}

	@Transactional
	@Override
	public SuscripcionCobro generarSuscripcionCobroRenovacion(Suscripcion suscripcion) {
		SuscripcionCobro suscripcionCobro = new SuscripcionCobro();

		suscripcionCobro.setCantidad(suscripcion.getServicio().getPrecio());
		// Pendiente y no emitido, ya que las renovaciones son en dos pasos, y primero
		// se pone pendiente para identificarlo en el segundo paso
		suscripcionCobro.setEstado(valorMaestroServicio.buscarValorMaestroPorCodigo(SuscripcionCobroEstadoEnum.PENDIENTE.codigo()));
		suscripcionCobro.setFechaHoraEmision(ZonedDateTime.now());
		suscripcionCobro.setFechaPeriodoDesde(suscripcion.getFechaPeriodoDesde());
		suscripcionCobro.setFechaPeriodoHasta(suscripcion.getFechaPeriodoHasta());
		suscripcionCobro.setSuscripcion(suscripcion);
		suscripcionCobro.setEntrenador(suscripcion.getCliente().getEntrenador());

		return suscripcionCobroRepositorio.saveAndFlush(suscripcionCobro);
	}

	@Transactional
	@Override
	public SuscripcionCobro generarSuscripcionCobroRenovacionYaPagada(Suscripcion suscripcion) {
		SuscripcionCobro suscripcionCobro = new SuscripcionCobro();

		suscripcionCobro.setCantidad(suscripcion.getServicio().getPrecio());
		suscripcionCobro.setEstado(valorMaestroServicio.buscarValorMaestroPorCodigo(SuscripcionCobroEstadoEnum.COBRADO.codigo()));
		suscripcionCobro.setFechaHoraEmision(ZonedDateTime.now());
		suscripcionCobro.setFechaPeriodoDesde(suscripcion.getFechaPeriodoDesde());
		suscripcionCobro.setFechaPeriodoHasta(suscripcion.getFechaPeriodoHasta());
		suscripcionCobro.setSuscripcion(suscripcion);
		suscripcionCobro.setEntrenador(suscripcion.getCliente().getEntrenador());

		return suscripcionCobroRepositorio.saveAndFlush(suscripcionCobro);
	}

	@Transactional
	@Override
	public void gestionarRespuestaTpvPagoRenovacion(Long clienteId, Long suscripcionCobroId, RespuestaPagoTpv respuestaPagoTpv, String tipoNotificacion) {

		JSONObject parametrosJson = decodificarParametros(respuestaPagoTpv);
		final int codigoDeRespuestaInt = Integer.valueOf(parametrosJson.getString("Ds_Response"));
		if (codigoDeRespuestaInt <= 99) {
			notificacionAsincronaRenovacionOk(clienteId, suscripcionCobroId, crearParametrosNotificacionTpvOk(parametrosJson));
		} else {
			notificacionAsincronaRenovacionKo(clienteId, suscripcionCobroId, crearParametrosNotificacionTpvKo(parametrosJson));
		}

	}

	@Transactional
	@Override
	public void gestionarRespuestaTpvPagoInicial(Long clienteId, Long suscripcionCobroId, RespuestaPagoTpv respuestaPagoTpv, String tipoNotificacion) {
		JSONObject parametrosJson = decodificarParametros(respuestaPagoTpv);
		final int codigoDeRespuestaInt = Integer.valueOf(parametrosJson.getString("Ds_Response"));
		if (codigoDeRespuestaInt <= 99) {
			notificacionOk(clienteId, suscripcionCobroId, crearParametrosNotificacionTpvOk(parametrosJson), tipoNotificacion);
		} else {
			notificacionKo(clienteId, suscripcionCobroId, crearParametrosNotificacionTpvKo(parametrosJson), tipoNotificacion);
		}
	}

	private ParametrosNotificacionTpv crearParametrosNotificacionTpvOk(JSONObject parametrosJson) {
		LOGGER.info("Leyendo parametros decodificados y creando ParametrosNotificacionTpv en notificacion ok. JSON origen:" + parametrosJson);
		ParametrosNotificacionTpv parametrosNotificacionTpv = new ParametrosNotificacionTpv();
		try {
			parametrosNotificacionTpv.setNumeroPedido(parametrosJson.getString("Ds_Order"));
			parametrosNotificacionTpv.setCodigoRespuesta(parametrosJson.getString("Ds_Response"));
			parametrosNotificacionTpv.setTipoOperacion(parametrosJson.getString("Ds_TransactionType"));
			parametrosNotificacionTpv.setCodigoAutorizacion(parametrosJson.getString("Ds_AuthorisationCode"));
			// Datos para MIT: tarjeta token, fecha caduc. y id transaccion cof
			parametrosNotificacionTpv.setTarjetaIdentificacionToken(parametrosJson.getString("Ds_Merchant_Identifier"));

			// Try especial, ya que en alguna ocasion se vio que no venia
			try {
				parametrosNotificacionTpv.setTarjetaFechaCaducidad(parametrosJson.getString("Ds_ExpiryDate"));
			} catch (Exception e) {
				LOGGER.warn("El parametro Ds_ExpiryDate no viene en la respuesta");
			}

			// Try especial, ya que no se bien si algun dia devolveran este campo
			try {
				parametrosNotificacionTpv.setIdentificadorTransaccionCof(parametrosJson.getString("DS_MERCHANT_COF_TXNID"));
			} catch (Exception e) {
				LOGGER.warn("El parametro DS_MERCHANT_COF_TXNID no viene en la respuesta");
			}

			LOGGER.info("Parametros leidos de los decodificados y asignados a un ParametrosNotificacionTpv: " + parametrosNotificacionTpv.toString());

		} catch (final Exception e) {
			LOGGER.error("Error obteniendo parametros de respuesta de pago TPV", e);
			throw new RuntimeException("Error obteniendo parametros de respuesta de pago TPV", e);
		}

		return parametrosNotificacionTpv;
	}

	private ParametrosNotificacionTpv crearParametrosNotificacionTpvKo(JSONObject parametrosJson) {
		LOGGER.info("Leyendo parametros decodificados y creando ParametrosNotificacionTpv en notificacion ko. JSON origen:" + parametrosJson);
		ParametrosNotificacionTpv parametrosNotificacionTpv = new ParametrosNotificacionTpv();
		try {
			parametrosNotificacionTpv.setNumeroPedido(parametrosJson.getString("Ds_Order"));
			parametrosNotificacionTpv.setCodigoRespuesta(parametrosJson.getString("Ds_Response"));
			parametrosNotificacionTpv.setTipoOperacion(parametrosJson.getString("Ds_TransactionType"));

			LOGGER.info("Parametros leidos de los decodificados y asignados a un ParametrosNotificacionTpv: " + parametrosNotificacionTpv.toString());

		} catch (final Exception e) {
			LOGGER.error("Error obteniendo parametros de respuesta de pago TPV", e);
			throw new RuntimeException("Error obteniendo parametros de respuesta de pago TPV", e);
		}

		return parametrosNotificacionTpv;
	}

	private ParametrosNotificacionTpv crearParametrosNotificacionTpvRespuestaRest(JSONObject parametrosJson) {
		LOGGER.info("Leyendo parametros decodificados y creando ParametrosNotificacionTpv en respuesta rest. JSON origen:" + parametrosJson);
		ParametrosNotificacionTpv parametrosNotificacionTpv = new ParametrosNotificacionTpv();
		try {
			parametrosNotificacionTpv.setNumeroPedido(parametrosJson.getString("Ds_Order"));
			parametrosNotificacionTpv.setCodigoRespuesta(parametrosJson.getString("Ds_Response"));
			parametrosNotificacionTpv.setTipoOperacion(parametrosJson.getString("Ds_TransactionType"));

			LOGGER.info("Parametros leidos de los decodificados y asignados a un ParametrosNotificacionTpv: " + parametrosNotificacionTpv.toString());

		} catch (final Exception e) {
			LOGGER.error("Error obteniendo parametros de respuesta de pago TPV", e);
			throw new RuntimeException("Error obteniendo parametros de respuesta de pago TPV", e);
		}

		return parametrosNotificacionTpv;
	}

	/**
	 * Decodifica los parametros de una respuesta de la TPV virtual. Segun
	 * documentación no sería necesario devolver un json, si no que se podría llamar
	 * a apiMacSha256.getParameter para obtener los parametros que ya fueron
	 * decodificados.
	 * 
	 * @param respuestaPagoTpv la respuesta de la TPV virtual
	 * @return un json son los parametros decodificados.
	 */
	private JSONObject decodificarParametros(RespuestaPagoTpv respuestaPagoTpv) {
		final ApiMacSha256 apiMacSha256 = new ApiMacSha256();
		String parametros;
		try {
			validarFirma(apiMacSha256, respuestaPagoTpv);
			parametros = apiMacSha256.decodeMerchantParameters(respuestaPagoTpv.getDs_MerchantParameters());
			LOGGER.info("Parametros decodificados desde MerchantParamters: " + parametros);
		} catch (UnsupportedEncodingException e) {
			LOGGER.error("Error decodificando parametros de respuesta de pago TPV", e);
			throw new RuntimeException("Error decodificando parametros de respuesta de pago TPV", e);
		}
		return new JSONObject(parametros);
	}

	/**
	 * Valida la firma de una respuesta de la TPV vitual.
	 * 
	 * @param respuestaPagoTpv la respuesta de la TPV virtual
	 * @return verdadero si la firma es correcta, falsa en otro caso
	 */
	private boolean validarFirma(ApiMacSha256 apiMacSha256, RespuestaPagoTpv respuestaPagoTpv) {
		boolean firmaValidada = true;
		try {
			var firmaCalculada = apiMacSha256.createMerchantSignatureNotif(claveSecreta, respuestaPagoTpv.getDs_MerchantParameters());
			LOGGER.info("Validación firma TPV virtual: Validando firma calculada con firma de la respuesta de la TPV Virtual. Firma calculada: " + firmaCalculada
					+ " - Firma recibida: " + respuestaPagoTpv.getDs_Signature());
			if (!firmaCalculada.equals(respuestaPagoTpv.getDs_Signature())) {
				firmaValidada = false;
				LOGGER.error("Validación firma TPV virtual: ¡Atención! La firma de respuesta de TPV vitual no es valida");
				// TODO DESCOMENTAR
//				throw new RuntimeException("¡Atención! La firma de respuesta de TPV vitual no es valida");			
			}
		} catch (Exception e) {
			LOGGER.error("Validación firma TPV virtual: Error inesperado validando firma de respuesta de TPV vitual", e);
			firmaValidada = false;
			// TODO DESCOMENTAR
//			throw new RuntimeException("Error validando firma de respuesta de TPV vitual", e);
		}

		return firmaValidada;
	}

	private void notificacionSincronaRenovacionOk(SuscripcionCobro suscripcionCobro, ParametrosNotificacionTpv parametrosNotificacionTpv) {
		// TODO LEER MÁS DATOS Y QUIZA YA CONFIRMAR AQUI EL COBRO. VERIFICADO MIRANDO
		// LOGS QUE VIENEN TODOS LOS PARAMS. ADEMAS CON ESTOS DATOS DEBERIA FUNCIONAR LA
		// VERIFICACION DE LAFIRMA, QUE AHORA FALLA PORQUE LE FALTAN DATOS PARA
		// VERIFICAR.
		// Ejemplo
//		Petición renovación rest: Respuesta de peticion REST en renovacion de suscripcion: {"Ds_SignatureVersion":"HMAC_SHA256_V1","Ds_MerchantParameters":"eyJEc19BbW91bnQiOiIzOTk5IiwiRHNfQ3VycmVuY3kiOiI5NzgiLCJEc19PcmRlciI6IjAwMDAwMDAwMDE5NCIsIkRzX01lcmNoYW50Q29kZSI6IjM1MDQwMzA4NSIsIkRzX1Rlcm1pbmFsIjoiMSIsIkRzX1Jlc3BvbnNlIjoiMDAwMCIsIkRzX0F1dGhvcmlzYXRpb25Db2RlIjoiNTE4MDExIiwiRHNfVHJhbnNhY3Rpb25UeXBlIjoiMCIsIkRzX1NlY3VyZVBheW1lbnQiOiIwIiwiRHNfTGFuZ3VhZ2UiOiIxIiwiRHNfTWVyY2hhbnRfSWRlbnRpZmllciI6IjYyMGE3YTRmMTMxODZjZTFkOTQ3ZmU4NWFmMmVhY2M3NDZmYWUzNTkiLCJEc19DYXJkX1R5cGUiOiJDIiwiRHNfTWVyY2hhbnREYXRhIjoiIiwiRHNfQ2FyZF9Db3VudHJ5IjoiNzI0IiwiRHNfQ2FyZF9CcmFuZCI6IjEiLCJEc19Qcm9jZXNzZWRQYXlNZXRob2QiOiIxNCJ9","Ds_Signature":"9Y8JLa0A01J7AydlNBTxppq79NgmaihzWV6KLYlHFYI="}
//		2021-01-19 03:00:00,974 INFO es.pocketrainer.servicio.cobro.CobroServicioImpl [scheduling-1] Parametros decodificados desde MerchantParamters: {"Ds_Amount":"3999","Ds_Currency":"978","Ds_Order":"000000000194","Ds_MerchantCode":"350403085","Ds_Terminal":"1","Ds_Response":"0000","Ds_AuthorisationCode":"518011","Ds_TransactionType":"0","Ds_SecurePayment":"0","Ds_Language":"1","Ds_Merchant_Identifier":"620a7a4f13186ce1d947fe85af2eacc746fae359","Ds_Card_Type":"C","Ds_MerchantData":"","Ds_Card_Country":"724","Ds_Card_Brand":"1","Ds_ProcessedPayMethod":"14"}
//		2021-01-19 03:00:00,975 INFO es.pocketrainer.servicio.cobro.CobroServicioImpl [scheduling-1] Leyendo parametros decodificados y creando ParametrosNotificacionTpv en respuesta rest. JSON origen:{"Ds_Card_Brand":"1","Ds_Response":"0000","Ds_ProcessedPayMethod":"14","Ds_TransactionType":"0","Ds_Merchant_Identifier":"620a7a4f13186ce1d947fe85af2eacc746fae359","Ds_Language":"1","Ds_Currency":"978","Ds_MerchantCode":"350403085","Ds_Card_Country":"724","Ds_SecurePayment":"0","Ds_Amount":"3999","Ds_Card_Type":"C","Ds_MerchantData":"","Ds_AuthorisationCode":"518011","Ds_Terminal":"1","Ds_Order":"000000000194"}
//		2021-01-19 03:00:00,975 INFO es.pocketrainer.servicio.cobro.CobroServicioImpl [scheduling-1] Parametros leidos de los decodificados y asignados a un ParametrosNotificacionTpv: ParametrosNotificacionTpv [numeroPedido=000000000194, codigoRespuesta=0000, tipoOperacion=0, ]

		LOGGER.info("Sincrono: Procesando pago renovacion ok para cliente " + suscripcionCobro.getSuscripcion().getCliente().getNombreCompleto());

		notificacionRenovacionOk(suscripcionCobro, parametrosNotificacionTpv, NOTIFICACION_SINCRONA_OK);

		LOGGER.info("Sincrono: Finalizado procesando pago renovacion ok para cliente " + suscripcionCobro.getSuscripcion().getCliente().getNombreCompleto());
	}

	private void notificacionAsincronaRenovacionOk(Long clienteId, Long suscripcionCobroId, ParametrosNotificacionTpv parametrosNotificacionTpv) {

		LOGGER.info("Asincrono: Procesando pago renovacion ok para cliente " + clienteId);

		// Recupero cobro con bloqueo pesimista
		var suscripcionCobroActual = suscripcionCobroRepositorio.buscarSuscripcionCobroPorIdConBloqueoPesimista(suscripcionCobroId);
		notificacionRenovacionOk(suscripcionCobroActual, parametrosNotificacionTpv, NOTIFICACION_ASINCRONA);

		LOGGER.info("Asincrono: Finalizado procesando pago renovacion ok para cliente " + clienteId);
	}

	private void notificacionRenovacionOk(SuscripcionCobro suscripcionCobro, ParametrosNotificacionTpv parametrosNotificacionTpv, String tipoSincronaAsincrona) {

		var fechaHoraActual = ZonedDateTime.now();

		// En notificacion asincrona: Se confirma el pago ok
		if (tipoSincronaAsincrona.equals(NOTIFICACION_ASINCRONA)) {
			suscripcionCobro.setFechaHoraNotificacionAsincrona(fechaHoraActual);

			// Cliente: Se considera activo si esta renovando

			// Suscripcion: Pasa de IMPAGADA a ACTIVA y sin fecha de baja que se puso al
			// inicio de renovacion
			suscripcionCobro.getSuscripcion().setEstado(valorMaestroServicio.buscarValorMaestroPorCodigo(SuscripcionEstadoEnum.ACTIVA.codigo()));
			suscripcionCobro.getSuscripcion().setFechaBaja(null);

			// Cobro: Pasa de EMITIDO a COBRADO
			suscripcionCobro.setEstado(valorMaestroServicio.buscarValorMaestroPorCodigo(SuscripcionCobroEstadoEnum.COBRADO.codigo()));
			suscripcionCobro.setFechaHoraCobro(fechaHoraActual);
			suscripcionCobro.setCodigoAutorizacion(parametrosNotificacionTpv.getCodigoAutorizacion());
		}

		// En notificacion sincrona: Realmente no se confirma de todo que el pago fue ok
		if (tipoSincronaAsincrona.equals(NOTIFICACION_SINCRONA_OK)) {
			suscripcionCobro.setFechaHoraNotificacionSincronaOk(fechaHoraActual);
		}

		// Comun sincrona y asincrona
		suscripcionCobro.setCodigoRespuesta(parametrosNotificacionTpv.getCodigoRespuesta());
		suscripcionCobro.setNumeroPedido(parametrosNotificacionTpv.getNumeroPedido());
		suscripcionCobro.setTransaccionTipo(parametrosNotificacionTpv.getTipoOperacion());
	}

	private void notificacionSincronaRenovacionKo(SuscripcionCobro suscripcionCobro, ParametrosNotificacionTpv parametrosNotificacionTpv) {

		LOGGER.info("Sincrono: Procesando pago renovacion ko para cliente " + suscripcionCobro.getSuscripcion().getCliente().getNombreCompleto());

		notificacionRenovacionKo(suscripcionCobro, parametrosNotificacionTpv, NOTIFICACION_SINCRONA_KO);

		LOGGER.info("Sincrono: Finalizado procesando pago renovacion ok para cliente " + suscripcionCobro.getSuscripcion().getCliente().getNombreCompleto());
	}

	private void notificacionAsincronaRenovacionKo(Long clienteId, Long suscripcionCobroId, ParametrosNotificacionTpv parametrosNotificacionTpv) {

		LOGGER.info("Asincrono: Procesando pago renovacion ok para cliente " + clienteId);

		// Recupero cobro con bloqueo pesimista
		var suscripcionCobroActual = suscripcionCobroRepositorio.buscarSuscripcionCobroPorIdConBloqueoPesimista(suscripcionCobroId);
		notificacionRenovacionKo(suscripcionCobroActual, parametrosNotificacionTpv, NOTIFICACION_ASINCRONA);

		LOGGER.info("Asincrono: Finalizado procesando pago renovacion ok para cliente " + clienteId);

	}

	private void notificacionRenovacionKo(SuscripcionCobro suscripcionCobro, ParametrosNotificacionTpv parametrosNotificacionTpv, String tipoSincronaAsincrona) {

		var fechaHoraActual = ZonedDateTime.now();

		if (tipoSincronaAsincrona.equals(NOTIFICACION_ASINCRONA)) {
			suscripcionCobro.setFechaHoraNotificacionAsincrona(fechaHoraActual);
			// Notificar al cliente de la situacion de impago
			notificacionServicio.notificarSuscripcionImpagada(suscripcionCobro.getSuscripcion().getCliente());
		}

		if (tipoSincronaAsincrona.equals(NOTIFICACION_SINCRONA_KO)) {
			suscripcionCobro.setFechaHoraNotificacionSincronaKo(fechaHoraActual);
		}

		// Cliente: Se considera activo si esta renovando

		// Suscripcion: Ya se cambio al inicio de la renovacion a estado IMPAGADA y con
		// fecha de baja a 48 horas

		// Cobro: Pasa de EMITIDO a RECHAZADO
		suscripcionCobro.setEstado(valorMaestroServicio.buscarValorMaestroPorCodigo(SuscripcionCobroEstadoEnum.RECHAZADO.codigo()));
		suscripcionCobro.setFechaHoraNotificacionSincronaKo(fechaHoraActual);
		suscripcionCobro.setCodigoRespuesta(parametrosNotificacionTpv.getCodigoRespuesta());
		suscripcionCobro.setNumeroPedido(parametrosNotificacionTpv.getNumeroPedido());
		suscripcionCobro.setTransaccionTipo(parametrosNotificacionTpv.getTipoOperacion());
	}

	/**
	 * Gestion de error en la respuesta de la llamada al servicio rest de
	 * renovacion. Se considera que si sucede este error es cosa de la aplicacin u
	 * otra situaion excepcional, en principio ajena al cliente. Se avisa al tecnico
	 * para que revise que paso y tambien al cliente ya que aunque no fuera cosa
	 * suya, la suscripcion ya esta como impago
	 * 
	 * @param suscripcionCobro el cobro relacionado
	 * @param codigoError      el codigo de error
	 */
	private void respuestaRenovacionKo(SuscripcionCobro suscripcionCobro, String codigoError) {

		LOGGER.info("Sincrono: Procesando pago renovacion Ko para cliente por respuesta con error en peticion rest "
				+ suscripcionCobro.getSuscripcion().getCliente().getNombreCompleto());

		// Cobro: a RECHAZADO
		suscripcionCobro.setEstado(valorMaestroServicio.buscarValorMaestroPorCodigo(SuscripcionCobroEstadoEnum.RECHAZADO.codigo()));
		suscripcionCobro.setCodigoRespuesta(codigoError);

		// Aviso al tecnico
		var error = codigoError + " - " + (Constantes.REDSYS_ERROR_MENSAJE_MAPA.get(codigoError) != null ? Constantes.REDSYS_ERROR_MENSAJE_MAPA.get(codigoError)
				: "Código de respuesta no conocido");
		notificacionServicio.notificarEntrenadorErrorCobroRenovacion(suscripcionCobro.getSuscripcion().getCliente(), error);

		// Notificar al cliente de la situacion de impago
		notificacionServicio.notificarSuscripcionImpagada(suscripcionCobro.getSuscripcion().getCliente());

		LOGGER.info("Sincrono: Finalizando procesado pago renovacion Ko para cliente por respuesta con error en peticion rest "
				+ suscripcionCobro.getSuscripcion().getCliente().getNombreCompleto());
	}

	private void notificacionOk(Long clienteId, Long suscripcionCobroId, ParametrosNotificacionTpv parametrosNotificacionTpv, String tipoSincronaAsincrona) {

		LOGGER.info("Procesando pago ok para cliente " + (clienteId != null ? clienteId : "Desconocido"));

		var cliente = clienteServicio.buscarClientePorId(clienteId);
		var fechaHoraActual = ZonedDateTime.now();

		// Fecha de tipo de notificacion
		var suscripcionCobroActual = cliente.getSuscripcion().getSuscripcionCobroLista().stream().filter(it -> it.getId().equals(suscripcionCobroId)).findAny()
				.orElseThrow();
		if (tipoSincronaAsincrona.equals(NOTIFICACION_ASINCRONA)) {
			suscripcionCobroActual.setFechaHoraNotificacionAsincrona(fechaHoraActual);
		}

		if (tipoSincronaAsincrona.equals(NOTIFICACION_SINCRONA_OK)) {
			suscripcionCobroActual.setFechaHoraNotificacionSincronaOk(fechaHoraActual);
		}

		// Solo actualizar mas si es la primera notificacion (sincrona vs sincrona ok)
		if (suscripcionCobroActual.getEstado().getCodigo().equals(SuscripcionCobroEstadoEnum.EMITIDO.codigo())) {

			// Cliente: Se considera activo y ya fue creado como ACTIVO

			// Suscripcion: Pasa de IMPAGADA_INICIAL a ACTIVA_NUEVA (alta
			// inicial/reactivacion), o, de IMPAGADA a ACTIVA (renovacion cuya renovacion
			// automatica falló) y sin fecha de baja que
			// se puso al crearla inicialmente
			if (cliente.getSuscripcion().getEstado().getCodigo().equals(SuscripcionEstadoEnum.IMPAGADA_INICIAL.codigo())) {
				cliente.getSuscripcion().setEstado(valorMaestroServicio.buscarValorMaestroPorCodigo(SuscripcionEstadoEnum.ACTIVA_NUEVA.codigo()));
			} else {
				cliente.getSuscripcion().setEstado(valorMaestroServicio.buscarValorMaestroPorCodigo(SuscripcionEstadoEnum.ACTIVA.codigo()));
			}

			cliente.getSuscripcion().setFechaBaja(null);

			// Cobro: Pasa de EMITIDO a COBRADO
			suscripcionCobroActual.setEstado(valorMaestroServicio.buscarValorMaestroPorCodigo(SuscripcionCobroEstadoEnum.COBRADO.codigo()));
			suscripcionCobroActual.setFechaHoraCobro(fechaHoraActual);

			suscripcionCobroActual.setCodigoAutorizacion(parametrosNotificacionTpv.getCodigoAutorizacion());
			suscripcionCobroActual.setCodigoRespuesta(parametrosNotificacionTpv.getCodigoRespuesta());
			suscripcionCobroActual.setNumeroPedido(parametrosNotificacionTpv.getNumeroPedido());
			suscripcionCobroActual.setTransaccionTipo(parametrosNotificacionTpv.getTipoOperacion());

			// Datos cobro para recurrente - pago 1 click -
			var clienteDatosCobro = new ClienteDatosCobro();
			clienteDatosCobro.setFechaUltimaActualizacion(fechaHoraActual);
			clienteDatosCobro.setTarjetaIdentificacionToken(parametrosNotificacionTpv.getTarjetaIdentificacionToken());
			clienteDatosCobro.setIdentificadorTransaccionCof(parametrosNotificacionTpv.getIdentificadorTransaccionCof());

			Integer anho = Integer.valueOf(parametrosNotificacionTpv.getTarjetaFechaCaducidad().substring(0, 2)) + 2000;
			Integer mes = Integer.valueOf(parametrosNotificacionTpv.getTarjetaFechaCaducidad().substring(2, 4));
			var tarjetaFechaCaducidadZonedDatetime = ZonedDateTime.of(anho, mes, 1, 0, 0, 0, 0, ZoneId.systemDefault()).plusMonths(1);

			clienteDatosCobro.setTarjetaFechaCaducidad(tarjetaFechaCaducidadZonedDatetime);

			clienteDatosCobro.setCliente(cliente);
			cliente.setClienteDatosCobro(clienteDatosCobro);

		}

		// Actualizar
		clienteServicio.actualizarCliente(cliente);

		LOGGER.info("Finalizado procesado pago ok para cliente " + (clienteId != null ? clienteId : "Desconocido"));
	}

	private void notificacionKo(Long clienteId, Long suscripcionCobroId, ParametrosNotificacionTpv parametrosNotificacionTpv, String tipoSincronaAsincrona) {

		LOGGER.info("Procesando pago Ko para cliente " + (clienteId != null ? clienteId : "Desconocido"));

		var cliente = clienteServicio.buscarClientePorId(clienteId);
		var fechHoraActual = ZonedDateTime.now();

		var suscripcionCobroActual = cliente.getSuscripcion().getSuscripcionCobroLista().stream().filter(it -> it.getId().equals(suscripcionCobroId)).findAny()
				.orElseThrow();
		if (tipoSincronaAsincrona.equals(NOTIFICACION_ASINCRONA)) {
			suscripcionCobroActual.setFechaHoraNotificacionAsincrona(fechHoraActual);
		}

		if (tipoSincronaAsincrona.equals(NOTIFICACION_SINCRONA_KO)) {
			suscripcionCobroActual.setFechaHoraNotificacionSincronaKo(fechHoraActual);
		}

		// Solo actualizar mas si es la primera notificacion (sincrona vs sincrona ok)
		if (suscripcionCobroActual.getEstado().getCodigo().equals(SuscripcionCobroEstadoEnum.EMITIDO.codigo())) {

			// Cliente: Se considera activo y ya fue creado como ACTIVO

			// Suscripcion: Ya se creo con estado IMPAGADA_INICIAL y fecha de baja a 48
			// horas

			// Cobro: Pasa de EMITIDO a RECHAZADO
			suscripcionCobroActual.setEstado(valorMaestroServicio.buscarValorMaestroPorCodigo(SuscripcionCobroEstadoEnum.RECHAZADO.codigo()));
			suscripcionCobroActual.setFechaHoraNotificacionSincronaKo(fechHoraActual);
			suscripcionCobroActual.setCodigoRespuesta(parametrosNotificacionTpv.getCodigoRespuesta());
			suscripcionCobroActual.setNumeroPedido(parametrosNotificacionTpv.getNumeroPedido());
			suscripcionCobroActual.setTransaccionTipo(parametrosNotificacionTpv.getTipoOperacion());

		}
		// Actualizar
		clienteServicio.actualizarCliente(cliente);

		LOGGER.info("Finalizado procesado pago ko para cliente " + (clienteId != null ? clienteId : "Desconocido"));
	}

	@Transactional
	@Override
	public List<SuscripcionCobro> buscarSuscripcionCobroPorFiltro(BuscarSuscripcionCobroListaFormulario suscripcionCobroBusquedaFiltro) {
		if (suscripcionCobroBusquedaFiltro.getMes() != null) {

			suscripcionCobroBusquedaFiltro.setFechaEmisionRangoInicio(suscripcionCobroBusquedaFiltro.getMes().withYear(suscripcionCobroBusquedaFiltro.getAnho())
					.atStartOfDay(ZoneId.systemDefault()).with(TemporalAdjusters.firstDayOfMonth()));
			suscripcionCobroBusquedaFiltro.setFechaEmisionRangoFin(suscripcionCobroBusquedaFiltro.getMes().withYear(suscripcionCobroBusquedaFiltro.getAnho())
					.atStartOfDay(ZoneId.systemDefault()).with(TemporalAdjusters.firstDayOfNextMonth()));
		} else if (suscripcionCobroBusquedaFiltro.getAnho() != null) {
			suscripcionCobroBusquedaFiltro
					.setFechaEmisionRangoInicio(ZonedDateTime.of(suscripcionCobroBusquedaFiltro.getAnho(), 1, 1, 0, 0, 0, 0, Constantes.ZONE_POR_DEFECTO));
			suscripcionCobroBusquedaFiltro
					.setFechaEmisionRangoFin(ZonedDateTime.of(suscripcionCobroBusquedaFiltro.getAnho(), 12, 31, 23, 59, 59, 0, Constantes.ZONE_POR_DEFECTO));
		} else if (suscripcionCobroBusquedaFiltro.getFechaEmisionRangoFin() != null) {
			// Ajustar para que se coja bien los cobros del dia fin seleccionado
			suscripcionCobroBusquedaFiltro.setFechaEmisionRangoFin(suscripcionCobroBusquedaFiltro.getFechaEmisionRangoFin().plusDays(1));
		}

		return suscripcionCobroRepositorio.buscarSuscripcionCobroPorFiltro(suscripcionCobroBusquedaFiltro);
	}

	@Transactional
	@Override
	public InputStream generarFacturacionHoldedXLS(BuscarSuscripcionCobroListaFormulario suscripcionCobroBusquedaFiltro) {
		var suscripcionCobrolista = buscarSuscripcionCobroPorFiltro(suscripcionCobroBusquedaFiltro);

		var workbook = new HSSFWorkbook();
		var sheet = workbook.createSheet("Facturación");

		var facturaLineaCeldaEstilo = workbook.createCellStyle();
		facturaLineaCeldaEstilo.setFillForegroundColor(IndexedColors.CORAL.getIndex());
		facturaLineaCeldaEstilo.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		var celdaNumericaEstilo = workbook.createCellStyle();
		celdaNumericaEstilo.setDataFormat(workbook.createDataFormat().getFormat("#,###.00"));

		var celdaNumericaEstilo2 = workbook.createCellStyle();
		celdaNumericaEstilo2.setFillForegroundColor(IndexedColors.CORAL.getIndex());
		celdaNumericaEstilo2.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		celdaNumericaEstilo.setDataFormat(workbook.createDataFormat().getFormat("#,###.00"));

		// Titulos
		var headers = new String[] { "Num factura", "Formato de numeración", "Fecha dd/mm/yyyy", "Fecha de vencimiento dd/mm/yyyy", "Descripción", "Nombre del contacto",
				"NIF", "Dirección", "Población", "Código postal", "Provincia", "País", "Concepto", "Descripción del producto", "SKU", "Precio unidad", "Unidades",
				"Descuento %", "IVA %", "Retención %", "Rec. de eq. %", "Operación", "Forma de pago (ID)", "Cantidad cobrada", "Fecha de cobro", "Cuenta de pago",
				"Tags separados por -", "Nombre canal de venta", "Cuenta canal de venta", "Moneda", "Cambio de moneda" };
		var headerRow = sheet.createRow(0);
		for (int i = 0; i < headers.length; ++i) {
			var header = headers[i];
			var cell = headerRow.createCell(i);
			cell.setCellValue(header);
		}

		// Filas
		int i = 0;

		for (SuscripcionCobro suscripcionCobro : suscripcionCobrolista) {

			var dataRow = sheet.createRow(i + 1);

			dataRow.createCell(0).setCellValue("");
			dataRow.createCell(1).setCellValue("2023F10");
			dataRow.createCell(2).setCellValue(FechaUtil.formatearFecha(suscripcionCobro.getFechaHoraEmision()));
			dataRow.createCell(3).setCellValue(FechaUtil.formatearFecha(suscripcionCobro.getFechaHoraEmision().plusDays(1)));
			var cliente = suscripcionCobro.getSuscripcion().getCliente();
			dataRow.createCell(5).setCellValue(cliente.getNombreCompleto());
			dataRow.createCell(6).setCellValue(cliente.getNif());
			dataRow.createCell(7).setCellValue(cliente.getDomicilio());
			dataRow.createCell(8).setCellValue(cliente.getLocalidad());
			dataRow.createCell(9).setCellValue(cliente.getCodigoPostal());
			dataRow.createCell(10).setCellValue("");
			dataRow.createCell(11).setCellValue("España");
			dataRow.createCell(12).setCellValue("Entrenamiento Ezen Online");
			var precioUnidad = suscripcionCobro.getCantidad().setScale(6).divide(Constantes.IVA_DECIMAL.add(BigDecimal.ONE), RoundingMode.DOWN);
			dataRow.createCell(15).setCellValue(NumeroUtil.formatearDecimalMonedaSeisDecimalesConPunto(precioUnidad));
			dataRow.createCell(16).setCellValue(1);
			dataRow.createCell(18).setCellValue(Constantes.IVA_PORCENTAJE);
			dataRow.createCell(22).setCellValue("FORMAPAGO3");
			dataRow.createCell(23).setCellValue(NumeroUtil.formatearDecimalMonedaConPunto(suscripcionCobro.getCantidad()));
			dataRow.createCell(28).setCellValue("");
			dataRow.createCell(29).setCellValue("eur");
			i++;
		}

		for (i = 0; i <= headers.length; i++) {
			sheet.autoSizeColumn(i);
		}

		final FileOutputStream file = null;
		InputStream excelInputStream = null;

		try {
			var bos = new ByteArrayOutputStream();
			workbook.write(bos);
			var barray = bos.toByteArray();
			excelInputStream = new ByteArrayInputStream(barray);
		} catch (final IOException e) {
			e.printStackTrace();
		} finally {
			if (file != null) {
				try {
					file.close();
				} catch (final IOException e) {
					e.printStackTrace();
				}
			}

			if (workbook != null) {
				try {
					workbook.close();
				} catch (final IOException e) {
					e.printStackTrace();
				}
			}

		}

		return excelInputStream;
	}

	@Transactional
	@Override
	public InputStream crearExcelSuscripcionCobroLista(BuscarSuscripcionCobroListaFormulario suscripcionCobroBusquedaFiltro) {
		var suscripcionCobrolista = buscarSuscripcionCobroPorFiltro(suscripcionCobroBusquedaFiltro);

		final HSSFWorkbook workbook = new HSSFWorkbook();
		final HSSFSheet sheet = workbook.createSheet();
		workbook.setSheetName(0, "Facturacion Pocketrainer");

		// Filtros
		final String[] headersFiltros = new String[] { "FECHA EMISIÓN DESDE", "FECHA EMISIÓN HASTA", "ESTADOS" };

		HSSFRow headerRow = sheet.createRow(0);
		for (int i = 0; i < headersFiltros.length; ++i) {
			final String header = headersFiltros[i];
			final HSSFCell cell = headerRow.createCell(i);
			cell.setCellValue(header);
		}

		final HSSFRow dataRowFiltros = sheet.createRow(1);
		dataRowFiltros.createCell(0)
				.setCellValue(suscripcionCobroBusquedaFiltro.getFechaEmisionRangoInicio() != null
						? FechaUtil.formatearFecha(suscripcionCobroBusquedaFiltro.getFechaEmisionRangoInicio())
						: "-");
		dataRowFiltros.createCell(1)
				.setCellValue(suscripcionCobroBusquedaFiltro.getFechaEmisionRangoFin() != null
						? FechaUtil.formatearFecha(suscripcionCobroBusquedaFiltro.getFechaEmisionRangoFin())
						: "-");
		dataRowFiltros.createCell(2)
				.setCellValue(suscripcionCobroBusquedaFiltro.getEstadoCodigoLista() != null
						? suscripcionCobroBusquedaFiltro.getEstadoCodigoLista().stream().map(it -> valorMaestroServicio.buscarValorMaestroPorCodigo(it).getNombre())
								.collect(Collectors.toList()).toString()
						: "Todos");

		headerRow = sheet.createRow(2);

		// Lista de cobros
		final String[] headers = new String[] { "NOMBRE", "APELLIDO1", "APELLIDO2", "DNI", "TELEFONO", "EMAIL", "DOMICILIO", "LOCALIDAD", "CP", "PAIS", "SERVICIO",
				"CANTIDAD", "ESTADO", "FECHA EMISIÓN", "FECHA COBRO", "ENTRENADOR" };

		headerRow = sheet.createRow(3);
		for (int i = 0; i < headers.length; ++i) {
			final String header = headers[i];
			final HSSFCell cell = headerRow.createCell(i);
			cell.setCellValue(header);
		}

		int i = 3;
		for (final SuscripcionCobro suscripcionCobro : suscripcionCobrolista) {
			final HSSFRow dataRow = sheet.createRow(i + 1);
			var cliente = suscripcionCobro.getSuscripcion().getCliente();
			dataRow.createCell(0).setCellValue(cliente.getNombre());
			dataRow.createCell(1).setCellValue(cliente.getApellido1());
			dataRow.createCell(2).setCellValue(cliente.getApellido2());
			dataRow.createCell(3).setCellValue(cliente.getNif());
			dataRow.createCell(4).setCellValue(cliente.getTelefono());
			dataRow.createCell(5).setCellValue(cliente.getEmail());
			dataRow.createCell(6).setCellValue(cliente.getDomicilio());
			dataRow.createCell(7).setCellValue(cliente.getLocalidad());
			dataRow.createCell(8).setCellValue(cliente.getCodigoPostal());
			dataRow.createCell(9).setCellValue(cliente.getPais());
			dataRow.createCell(10).setCellValue(cliente.getSuscripcion().getServicio().getNombre());
			dataRow.createCell(11).setCellValue(suscripcionCobro.getCantidad().toString().replace(".", ","));
			dataRow.createCell(12).setCellValue(suscripcionCobro.getEstado().getNombre());
			dataRow.createCell(13).setCellValue(suscripcionCobro.getFechaHoraEmision() != null ? FechaUtil.formatearFecha(suscripcionCobro.getFechaHoraEmision()) : "");
			dataRow.createCell(14).setCellValue(suscripcionCobro.getFechaHoraCobro() != null ? FechaUtil.formatearFecha(suscripcionCobro.getFechaHoraCobro()) : "");
			dataRow.createCell(15).setCellValue(
					suscripcionCobro.getEntrenador() != null ? suscripcionCobro.getEntrenador().getNombreCompleto() : "Todavía no se asignó un entrenador al cobro");

			i++;
		}

		// final HSSFRow dataRow = sheet.createRow(i + 1);
		// dataRow.createCell(0).setCellValue(filtroBusqueda.toString());

		sheet.autoSizeColumn(0);
		sheet.autoSizeColumn(1);
		sheet.autoSizeColumn(2);
		sheet.autoSizeColumn(3);
		sheet.autoSizeColumn(4);
		sheet.autoSizeColumn(5);
		sheet.autoSizeColumn(6);
		sheet.autoSizeColumn(7);
		sheet.autoSizeColumn(8);
		sheet.autoSizeColumn(9);
		sheet.autoSizeColumn(10);
		sheet.autoSizeColumn(11);
		sheet.autoSizeColumn(12);
		sheet.autoSizeColumn(13);
		sheet.autoSizeColumn(14);
		sheet.autoSizeColumn(15);

		final FileOutputStream file = null;
		InputStream excelInputStream = null;

		try {
			// file = new FileOutputStream("D:\\ClientesEzen.xls");
			// workbook.write(file);

			final ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			final byte[] barray = bos.toByteArray();
			excelInputStream = new ByteArrayInputStream(barray);
		} catch (final IOException e) {
			e.printStackTrace();
		} finally {

			if (file != null) {
				try {
					file.close();
				} catch (final IOException e) {
					e.printStackTrace();
				}
			}

			if (workbook != null) {
				try {
					workbook.close();
				} catch (final IOException e) {
					e.printStackTrace();
				}
			}

		}

		return excelInputStream;
	}

	@Transactional
	@Override
	public List<SuscripcionCobro> buscarSuscripcionCobroPendiente() {
		return suscripcionCobroRepositorio.buscarSuscripcionCobroPorEstado(Constantes.SuscripcionCobroEstadoEnum.PENDIENTE.codigo());
	}

}
