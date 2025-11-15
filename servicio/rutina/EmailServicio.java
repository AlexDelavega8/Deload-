package es.pocketrainer.servicio.rutina;

public interface EmailServicio {

	/**
	 * Envio general de correos
	 * 
	 * @param asunto                el asunto
	 * @param destinatario          el destinatario
	 * @param copia                 destinatario en copia
	 * @param copiaOculta           destinatario en copia oculta
	 * @param destinatarioRespuesta destinatario para la respuesta
	 * @param plantilla             la plantilla del correo
	 */
	void enviarEmail(String asunto, String destinatario, String copia, String copiaOculta, String destinatarioRespuesta, String plantilla);

	/**
	 * Envio especial de correos de error
	 * 
	 * @param asunto       el asunto
	 * @param destinatario el destinatario
	 * @param copia        destinatario en copia
	 * @param copiaOculta  destinatario en copia oculta
	 * @param plantilla    la plantilla del correo
	 */
	void enviarEmailError(String asunto, String destinatario, String copia, String copiaOculta, String html);
}
