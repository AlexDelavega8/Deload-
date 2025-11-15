package es.pocketrainer.controlador;

import static es.pocketrainer.util.Constantes.NOTIFICACION_ASINCRONA;
import static es.pocketrainer.util.Constantes.NOTIFICACION_SINCRONA_KO;
import static es.pocketrainer.util.Constantes.NOTIFICACION_SINCRONA_OK;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import es.pocketrainer.servicio.cliente.ClienteServicio;
import es.pocketrainer.servicio.cobro.CobroServicio;
import es.pocketrainer.util.tpv.RespuestaPagoTpv;

@Controller
@RequestMapping("/tpv")
public class TpvControlador {

	private static Logger LOGGER = LoggerFactory.getLogger(TpvControlador.class);

	@Resource
	private CobroServicio cobroServicio;

	@Resource
	private ClienteServicio clienteServicio;

	@PostMapping("/notificacionAsincrona/{clienteId}/{suscripcionCobroId}")
	@ResponseBody
	public void notificacionAsincrona(@PathVariable("clienteId") Long clienteId, @PathVariable("suscripcionCobroId") Long suscripcionCobroId,
			@ModelAttribute RespuestaPagoTpv respuestaPagoTpv, Model model, HttpServletRequest request) {

		LOGGER.info("Recibida notificacion asincrona de tpv para cliente " + (clienteId != null ? clienteId : "Desconocido"));

		cobroServicio.gestionarRespuestaTpvPagoInicial(clienteId, suscripcionCobroId, respuestaPagoTpv, NOTIFICACION_ASINCRONA);

		LOGGER.info("Fin procesado notificacion asincrona de tpv para cliente " + (clienteId != null ? clienteId : "Desconocido"));
	}

	@GetMapping("/notificacionSincronaOk/{clienteId}/{suscripcionCobroId}")
	public String notificacionSincronaOk(@PathVariable("clienteId") Long clienteId, @PathVariable("suscripcionCobroId") Long suscripcionCobroId,
			@ModelAttribute RespuestaPagoTpv respuestaPagoTpv, Model model, HttpServletRequest request) {

		LOGGER.info("Recibida notificacion ok sincrona de tpv para cliente " + (clienteId != null ? clienteId : "Desconocido"));

		cobroServicio.gestionarRespuestaTpvPagoInicial(clienteId, suscripcionCobroId, respuestaPagoTpv, NOTIFICACION_SINCRONA_OK);

		LOGGER.info("Fin procesado notificacion ok sincrona de tpv para cliente " + (clienteId != null ? clienteId : "Desconocido"));
		return "redirect:/creacionCuentaUsuarioPagoOk";
	}

	@GetMapping("/notificacionSincronaKo/{clienteId}/{suscripcionCobroId}")
	public String notificacionSincronaKo(@PathVariable("clienteId") Long clienteId, @PathVariable("suscripcionCobroId") Long suscripcionCobroId,
			@ModelAttribute RespuestaPagoTpv respuestaPagoTpv, Model model, HttpServletRequest request) {

		LOGGER.info("Recibida notificacion ko sincrona de tpv para cliente " + (clienteId != null ? clienteId : "Desconocido"));

		cobroServicio.gestionarRespuestaTpvPagoInicial(clienteId, suscripcionCobroId, respuestaPagoTpv, NOTIFICACION_SINCRONA_KO);

		LOGGER.info("Fin procesado notificacion ko sincrona de tpv para cliente " + (clienteId != null ? clienteId : "Desconocido"));
		return "redirect:/creacionCuentaUsuarioPagoKo/" + clienteId;
	}

	@PostMapping("/notificacionAsincronaCliente/{clienteId}/{suscripcionCobroId}")
	@ResponseBody
	public void notificacionAsincronaCliente(@PathVariable("clienteId") Long clienteId, @PathVariable("suscripcionCobroId") Long suscripcionCobroId,
			@ModelAttribute RespuestaPagoTpv respuestaPagoTpv, Model model, HttpServletRequest request) {

		LOGGER.info("Recibida notificacion asincrona de tpv para cliente " + (clienteId != null ? clienteId : "Desconocido"));

		cobroServicio.gestionarRespuestaTpvPagoInicial(clienteId, suscripcionCobroId, respuestaPagoTpv, NOTIFICACION_ASINCRONA);

		LOGGER.info("Fin procesado notificacion asincrona de tpv para cliente " + (clienteId != null ? clienteId : "Desconocido"));
	}

	@GetMapping("/notificacionSincronaOkCliente/{clienteId}/{suscripcionCobroId}")
	public String notificacionSincronaOkCliente(@PathVariable("clienteId") Long clienteId,
			@PathVariable("suscripcionCobroId") Long suscripcionCobroId, @ModelAttribute RespuestaPagoTpv respuestaPagoTpv, Model model,
			HttpServletRequest request) {

		LOGGER.info("Recibida notificacion ok sincrona de tpv para cliente " + (clienteId != null ? clienteId : "Desconocido"));

		cobroServicio.gestionarRespuestaTpvPagoInicial(clienteId, suscripcionCobroId, respuestaPagoTpv, NOTIFICACION_SINCRONA_OK);

		LOGGER.info("Fin procesado notificacion ok sincrona de tpv para cliente " + (clienteId != null ? clienteId : "Desconocido"));
		return "redirect:/miSuscripcion";
	}

	@GetMapping("/notificacionSincronaKoCliente/{clienteId}/{suscripcionCobroId}")
	public String notificacionSincronaKoCliente(@PathVariable("clienteId") Long clienteId,
			@PathVariable("suscripcionCobroId") Long suscripcionCobroId, @ModelAttribute RespuestaPagoTpv respuestaPagoTpv, Model model,
			HttpServletRequest request) {

		LOGGER.info("Recibida notificacion ko sincrona de tpv para cliente " + (clienteId != null ? clienteId : "Desconocido"));

		cobroServicio.gestionarRespuestaTpvPagoInicial(clienteId, suscripcionCobroId, respuestaPagoTpv, NOTIFICACION_SINCRONA_KO);

		LOGGER.info("Fin procesado notificacion ko sincrona de tpv para cliente " + (clienteId != null ? clienteId : "Desconocido"));
		return "redirect:/miSuscripcion";
	}

	@PostMapping("/notificacionAsincronaRenovacion/{clienteId}/{suscripcionCobroId}")
	@ResponseBody
	public void notificacionAsincronaRenovacion(@PathVariable("clienteId") Long clienteId,
			@PathVariable("suscripcionCobroId") Long suscripcionCobroId, @ModelAttribute RespuestaPagoTpv respuestaPagoTpv, Model model,
			HttpServletRequest request) {

		LOGGER.info("Recibida notificacion asincrona de tpv para cliente " + (clienteId != null ? clienteId : "Desconocido"));

		cobroServicio.gestionarRespuestaTpvPagoRenovacion(clienteId, suscripcionCobroId, respuestaPagoTpv, NOTIFICACION_ASINCRONA);

		LOGGER.info("Fin procesado notificacion asincrona de tpv para cliente " + (clienteId != null ? clienteId : "Desconocido"));
	}

}
