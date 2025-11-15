package es.pocketrainer.servicio.notificaciones;

import java.time.ZonedDateTime;
import java.util.Map;

import es.pocketrainer.modelo.cliente.Cliente;
import es.pocketrainer.modelo.entrenador.Entrenador;
import es.pocketrainer.modelo.usuario.Usuario;
import es.pocketrainer.modelo.videoconferencia.Videoconferencia;

public interface NotificacionServicio {

	void notificarNuevoCliente(Entrenador administrador, Cliente cliente);

	void notificarClienteReactivaSuscripcion(Entrenador administrador, Cliente cliente);

	void notificarClienteCancelaSuscripcion(Entrenador administrador, Cliente cliente);

	/**
	 * Administrador y entrenador
	 * 
	 * @param cliente
	 */
	void notificarClienteBaja(Entrenador administrador, Cliente cliente);

	/******* entrenador ****/
	void notificarClienteAsignado(Cliente cliente);

	void notificarEntrenadorVideollamadaEnBreves(Videoconferencia videoconferencia);

	void notificarEntrenadorCreado(Entrenador entrenador, String passwordNueva);

	void notificarClienteCambiaDatos(Cliente cliente);

	void notificarClienteDejaComentario(Cliente cliente, String comentario, String rutinaNombre);

	/**
	 * El entrenador ya puede comenzar a editar las rutinas del proximo peridoo e
	 * incluso hacer la video
	 * 
	 * @param cliente
	 */
	void notificarClienteRenovacionSuscripcionPreparada(Cliente cliente);

	/**
	 * Mensaje ordinario de cliente a entrenador
	 * 
	 * @param cliente el cliente que envia mensaje
	 * @param mensaje el mensaje
	 */
	void notificarMensajeDeCliente(Cliente cliente, String mensaje);

	/*********** cliente */

	void notificarRegistroCorrecto(Cliente cliente);

	void notificarEntrenadorAsignado(Cliente cliente);

	void notificarEntrenadorCambiado(Cliente cliente);

	void notificarEntrenamientoPreparado(Cliente cliente);

	void notificarClienteVideoconferenciaProgramada(Videoconferencia videoconferencia);

	void notificarClienteVideoconferenciaEnBreves(Videoconferencia videoconferencia);

	void notificarClienteVideoconferenciaReprogramada(Videoconferencia videoconferencia);

	void notificarClienteVideoconferenciaCancelada(Videoconferencia videoconferencia, ZonedDateTime fechaAnterior);

	void notificarFinSuscripcion(Cliente cliente);

	/**
	 * Cliente y gestor
	 * 
	 * @param cliente
	 */
	void notificarSuscripcionReactivada(Cliente cliente);

	/**
	 * A entrenador
	 * 
	 * @param cliente
	 */
	void notificarSuscripcionCancelada(Cliente cliente);

	/**
	 * Cliente y gestor: error cobrando
	 * 
	 * @param cliente
	 */
	void notificarSuscripcionImpagada(Cliente cliente);

	/**
	 * Envia un correo con informacion de une error producido en la aplicacion. El
	 * destinatario es el responsable tecnico.
	 * 
	 * @param datosError los datos del error
	 */
	void notificarError(Map<String, Object> datosError);

	/**
	 * Envia password tras solicitar recuperacion
	 * 
	 * @param usuario       destinatario
	 * @param nuevaPassword nueva password
	 */
	void notificarGeneracionNuevaPassword(Usuario usuario, String nuevaPassword);

	/**
	 * Avisa al cliente de que su renovacion esta proxima
	 * 
	 * @param cliente el cliente a notificar
	 */
	void notificarClienteRenovacionProxima(Cliente cliente);

	/**
	 * Notifica a soporte tecnico de que hubo un error renovando, para que se
	 * analice si es un error controlado o no
	 * 
	 * @param cliente el cliente para el que hubo error
	 * @param error   el detalle del error
	 */
	void notificarEntrenadorErrorCobroRenovacion(Cliente cliente, String error);

}
