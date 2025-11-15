package es.pocketrainer.seguridad;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.csrf.InvalidCsrfTokenException;
import org.springframework.security.web.csrf.MissingCsrfTokenException;
import org.springframework.stereotype.Component;

/**
 * Gestión a medida de las excepciones de permiso denegado.
 * 
 * @author Antonio FZ
 *
 */
@Component
public class CustomAccessDeniedHandler extends AccessDeniedHandlerImpl {

	private static Logger LOGGER = LoggerFactory.getLogger(CustomAccessDeniedHandler.class);

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException)
			throws IOException, ServletException {

		LOGGER.warn("Intento de acceso a recurso sin los permisos necesarios", accessDeniedException);

		response.setStatus(HttpStatus.FORBIDDEN.value());
		if (accessDeniedException instanceof InvalidCsrfTokenException || accessDeniedException instanceof MissingCsrfTokenException) {
			// Si es por csrf incorrecto: Forward a /csrfNoValido
			setErrorPage("/csrfNoValido");
		} else {
			// Si es por falta de permisos: Forward a /permisosInsuficientes
			setErrorPage("/permisosInsuficientes");
		}

		super.handle(request, response, accessDeniedException);
	}

}
