package es.pocketrainer.controlador;

import java.time.ZonedDateTime;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.groovy.parser.antlr4.util.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import es.pocketrainer.Configuracion;
import es.pocketrainer.seguridad.CustomAccessDeniedHandler;
import es.pocketrainer.seguridad.CustomAutenticacionEntryPoint;
import es.pocketrainer.servicio.notificaciones.NotificacionServicio;
import es.pocketrainer.util.ConstantesGlobales;
import es.pocketrainer.util.ErrorUtil;

//CsrfToken token = (CsrfToken) request.getSession()
//.getAttribute("org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository.CSRF_TOKEN");
//System.out.println(token.getToken());
//System.out.println(request.getSession().getId());
/**
 * Controlador para peticiones relacionadas con errores: error por csrf no
 * valido, error por permisos insuficientes, error por credenciales
 * insuficientes y otros errores no controlados. Las peticiones para los errores
 * llegan mediante forward/redirect desde componentes de gestión. <br>
 * 
 * <lu>
 * <li>CSRF no válido: Si alguna página maliciosa intenta hacer un post o si,
 * desde nuestra aplicación, se intenta hacer un post y la sesión caducó (y por
 * tanto el csrf token)</li>
 * <li>Permisos insuficientes: Un usuario con sesión iniciada no tiene los
 * suficientes permisos requeridos para un recurso</li>
 * <li>Credenciales insuficientes: Un usuario sin sesión iniciada intenta
 * acceder a un recurso que necesita autenticació como mínimo</li> </lu>
 * <li>Otros errores: Errores inesperados en controlador, errores por url no
 * existente ...</li>
 * 
 * <br>
 * Se implementa {@link ErrorController} para gestionar aqui los errores no
 * controlados en vez de con el autoconfigurado {@link BasicErrorController}.
 * Spring tiene unos filtros iniciales para gestionar las excepciones, y en
 * ellos ya rellenan datos que se obtienen desde aqui para rellenar el informe
 * de error, muchos copiados y/o en base a la implementacion de
 * {@link BasicErrorController}
 * 
 * @see {@link CustomAccessDeniedHandler},
 *      {@link CustomAutenticacionEntryPoint}, {@link Configuracion},
 *      {@link ErrorController}
 * 
 * @author Antonio FZ
 *
 */
@Controller
public class CustomErrorController implements ErrorController {

	@Value("${error.mostrarinformacion}")
	private Boolean mostrarinformacion;

	@Value("${entorno}")
	private String entorno;

	@Resource
	private NotificacionServicio notificacionServicio;

	@Resource
	private ControladorPrincipal controladorPrincipal;

	@PostMapping("/csrfNoValido")
	public String csrfNoValido(HttpServletRequest request, Model model, Authentication authentication) {
		return controladorPrincipal.inicio(model, authentication);
	}

	@GetMapping("/permisosInsuficientes")
	public String permisosInsuficientesGet(HttpServletRequest request, Model model, Authentication authentication) {
		return controladorPrincipal.inicio(model, authentication);
	}

	@PostMapping("/permisosInsuficientes")
	public String permisosInsuficientesPost(HttpServletRequest request, Model model, Authentication authentication) {
		return controladorPrincipal.inicio(model, authentication);
	}

	@GetMapping("/credencialesInsuficientes")
	public String credencialesInsuficientesGet(HttpServletRequest request, Model model) {
		return "inicioSesionPagina";
	}

	@PostMapping("/credencialesInsuficientes")
	public String credencialesInsuficientesPost(HttpServletRequest request, Model model, Authentication authentication) {
		return "inicioSesionPagina";
	}

	@RequestMapping(value = "/error")
	public ModelAndView errorHtml(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		var modelAndView = generarPaginaError(request, response, null, authentication);
		if (!entorno.equals(ConstantesGlobales.ENTORNO_LOCAL)) {
			notificacionServicio.notificarError(modelAndView.getModel());
		}

		return modelAndView;
	}

	private ModelAndView generarPaginaError(HttpServletRequest request, HttpServletResponse response, Throwable e, Authentication authentication) {
		var modelAndView = new ModelAndView();

		modelAndView.setViewName("errorPagina");
		modelAndView.addObject("mostrarinformacion", mostrarinformacion);
		modelAndView.addObject("plantilla", "plantillaInicio");
		modelAndView.addObject("ip", getIp(request));
		modelAndView.addObject("urlError", getUrlError(request));
		modelAndView.addObject("estado", getEstado(request));
		modelAndView.addObject("error", "Error no controlado");
		modelAndView.addObject("entorno", entorno);

		if (authentication != null && authentication.isAuthenticated()) {
			modelAndView.addObject("sesion", authentication.getPrincipal());
		} else {
			modelAndView.addObject("sesion", "Sin datos");
		}

		if (e == null) {
			e = getException(request);
		}

		if (e != null) {
			if (e.getCause() != null) {
				modelAndView.addObject("errorOrigenTraza", ErrorUtil.generarExceptionStackTrace(e.getCause()));
			}
			modelAndView.addObject("errorTraza", ErrorUtil.generarExceptionStackTrace(e));

		}

		modelAndView.addObject("fecha", ZonedDateTime.now());
		modelAndView.addObject("urlVolver", getUrlParaVolver(request));
		return modelAndView;
	}

	private String getIp(HttpServletRequest request) {
		var direccionIp = request.getHeader("X-FORWARDED-FOR");
		if (StringUtils.isEmpty(direccionIp)) {
			direccionIp = request.getRemoteAddr();
		}
		return direccionIp;
	}

	private String getUrlError(HttpServletRequest request) {
		return (String) request.getAttribute("javax.servlet.error.request_uri");
	}

	private String getMensaje(HttpServletRequest request) {
		return (String) request.getAttribute("javax.servlet.error.message");
	}

	private HttpStatus getEstado(HttpServletRequest request) {
		Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
		if (statusCode == null) {
			return HttpStatus.INTERNAL_SERVER_ERROR;
		}
		try {
			return HttpStatus.valueOf(statusCode);
		} catch (Exception ex) {
			return HttpStatus.INTERNAL_SERVER_ERROR;
		}
	}

	private Throwable getException(HttpServletRequest request) {
		return (Throwable) request.getAttribute("javax.servlet.error.exception");
	}

	private String getUrlParaVolver(HttpServletRequest request) {
		return request.getHeader("Referer") != null ? request.getHeader("Referer") : "/";
	}

}
