package es.pocketrainer.seguridad;

import java.io.IOException;
import java.io.Serializable;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/**
 * Gestión a medida de las excepciones de autenticación.
 * 
 * @author Antonio FZ
 *
 */
@Component
public class CustomAutenticacionEntryPoint implements AuthenticationEntryPoint, Serializable {

	private static Logger LOGGER = LoggerFactory.getLogger(CustomAccessDeniedHandler.class);
	private static final long serialVersionUID = -7858869558953243875L;

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
			throws IOException, ServletException {

//		LOGGER.warn("Intento de acceso a recurso sin estar autenticado", authException);

		// Forward a /credencialesInsuficientes
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		RequestDispatcher dispatcher = request.getRequestDispatcher("/credencialesInsuficientes");
		dispatcher.forward(request, response);
	}

}