package es.pocketrainer.servicio.rutina;

import java.nio.charset.StandardCharsets;

import javax.annotation.Resource;
import javax.mail.MessagingException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import es.pocketrainer.servicio.gestorficheros.GestorFicherosServicio;
import es.pocketrainer.util.Constantes;
import es.pocketrainer.util.ConstantesGlobales;

@Service
public class EmailServicioImpl implements EmailServicio {

	private static Logger LOGGER = LoggerFactory.getLogger(EmailServicioImpl.class);

	@Value("${ruta.estaticos.imagenes}")
	private String urlEstaticosImagenes;

	@Resource
	private GestorFicherosServicio gestorFicherosServicio;

	@Resource
	private JavaMailSender emailSender;

	@Value("${spring.mail.username}")
	private String appEmail;

	@Value("${entorno}")
	private String entorno;

	@Override
	@Async
	public void enviarEmail(String asunto, String destinatario, String copia, String copiaOculta, String destinatarioRespuesta, String html) {
		// Pongo restriccion a entorno de pro para que no me lleguen tantos mensajes
		// Lo ideal sera poner esto configurable para habilitar en caso de pruebas
		if (entorno.equals(ConstantesGlobales.ENTORNO_PRO)) {
			LOGGER.info("Enviado mail: " + asunto + " | " + destinatario + " | " + (copia != null ? copia : " - ") + " | " + (copiaOculta != null ? copiaOculta : " - "));

			var message = emailSender.createMimeMessage();
			try {
				var helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

				if (entorno.equals(ConstantesGlobales.ENTORNO_LOCAL)) {
					asunto = "[LOCAL] " + asunto;
					destinatario = Constantes.CORREO_TECNICO;
				} else if (entorno.equals(ConstantesGlobales.ENTORNO_DES)) {
					asunto = "[DES] " + asunto;
				} else if (entorno.equals(ConstantesGlobales.ENTORNO_PRE)) {
					asunto = "[PRE] " + asunto;
				}

				helper.setSubject(asunto);
				helper.setTo(destinatario);
				helper.setFrom(appEmail, "Pocketrainer");

				if (StringUtils.isNotBlank(destinatarioRespuesta)) {
					helper.setReplyTo(destinatarioRespuesta);
				}

				if (StringUtils.isNotBlank(copia)) {
					helper.setCc(copia);
				}

				if (StringUtils.isNotBlank(copiaOculta)) {
					helper.addBcc(copiaOculta);
				}

				helper.setText(html, true);
				helper.addInline("logo", gestorFicherosServicio.obtenerFichero("logo_correo.png", urlEstaticosImagenes));

				emailSender.send(message);

				// Si hay errores: logger e intentar enviar email tecnico
			} catch (MessagingException e) {
				LOGGER.error("Error enviado mail: " + asunto + " | " + destinatario + " | " + (copia != null ? copia : " - ") + " | "
						+ (copiaOculta != null ? copiaOculta : " - "), e);
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e1) {
					LOGGER.error("Error en sleep esperando un poco para enviar correo de error tras fallo en envio normal de correo");
				}
				enviarEmailError("[ERROR ENVIANDO ESTE MAIL]" + asunto, Constantes.CORREO_TECNICO, copia, copiaOculta, html);
			} catch (Exception e) {
				LOGGER.error("Error enviado mail: " + asunto + " | " + destinatario + " | " + (copia != null ? copia : " - ") + " | "
						+ (copiaOculta != null ? copiaOculta : " - "), e);
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e1) {
					LOGGER.error("Error en sleep esperando un poco para enviar correo de error tras fallo en envio normal de correo");
				}
				enviarEmailError("[ERROR ENVIANDO ESTE MAIL]" + asunto, Constantes.CORREO_TECNICO, copia, copiaOculta, html);
			}
		}
	}

	@Override
	@Async
	public void enviarEmailError(String asunto, String destinatario, String copia, String copiaOculta, String html) {
		if (!entorno.equals(ConstantesGlobales.ENTORNO_LOCAL)) {
			LOGGER.info("Enviado mail de error: " + asunto + " | " + destinatario + " | " + (copia != null ? copia : " - ") + " | "
					+ (copiaOculta != null ? copiaOculta : " - "));
			destinatario = Constantes.CORREO_TECNICO;
			var message = emailSender.createMimeMessage();
			try {
				var helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

				if (entorno.equals(ConstantesGlobales.ENTORNO_LOCAL)) {
					asunto = "[LOCAL] " + asunto;
				} else if (entorno.equals(ConstantesGlobales.ENTORNO_DES)) {
					asunto = "[DES] " + asunto;
				} else if (entorno.equals(ConstantesGlobales.ENTORNO_PRE)) {
					asunto = "[PRE] " + asunto;
				}

				helper.setSubject(asunto);

				helper.setTo(destinatario);

				if (StringUtils.isNotBlank(copia)) {
					helper.setCc(copia);
				}

				if (StringUtils.isNotBlank(copiaOculta)) {
					helper.addBcc(copiaOculta);
				}

				helper.addBcc(Constantes.CORREO_TECNICO);

				helper.setText(html, true);
				helper.addInline("logo", gestorFicherosServicio.obtenerFichero("logo_correo.png", urlEstaticosImagenes));

				emailSender.send(message);

			} catch (MessagingException e) {
				LOGGER.error("Error enviado mail de error: " + asunto + " | " + destinatario + " | " + (copia != null ? copia : " - ") + " | "
						+ (copiaOculta != null ? copiaOculta : " - "), e);
			} catch (Exception e) {
				LOGGER.error("Error enviado mail de error: " + asunto + " | " + destinatario + " | " + (copia != null ? copia : " - ") + " | "
						+ (copiaOculta != null ? copiaOculta : " - "), e);
			}
		}
	}

}
