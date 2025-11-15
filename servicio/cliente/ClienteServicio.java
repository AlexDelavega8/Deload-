package es.pocketrainer.servicio.cliente;

import java.time.ZonedDateTime;
import java.util.List;

import es.pocketrainer.formulario.BuscarClienteFormulario;
import es.pocketrainer.modelo.cliente.Cliente;
import es.pocketrainer.modelo.cliente.ClienteConfiguracion;
import es.pocketrainer.modelo.cliente.Suscripcion;

public interface ClienteServicio {

	List<Cliente> buscarClienteTodos();

	Cliente buscarClientePorId(Long clienteId);

	void crearClienteNuevo(Cliente cliente);

	void actualizarCliente(Cliente cliente);

	Cliente actualizarClienteDesdeMisDatosPagina(Cliente cliente);

	void asignarEntrenador(Long clienteId, Long entrenadorId);

	void programarVideoconferencia(Long clienteId, Long videoconferenciaId, ZonedDateTime fechaHoraProgramada);

	void cancelarSuscripcion(Long clienteId);

	void suspenderCancelacionSuscripcion(Long clienteId);

	Cliente reactivarSuscripcion(Long clienteId);

	void revisarSuscripciones();

	void prepararRenovaciones();

	void enviarMensajeEntrenador(Long clienteId, String mensaje);

	List<Cliente> buscarClientePorEntrenador(Long entrenadorId);

	void realizarCobrosRenovacionesSuscripciones();

	List<Cliente> buscarClientePorFiltro(BuscarClienteFormulario buscarClienteFormulario);

	void corregirFechasSuscripciones();

	/**
	 * Calcula cual va a ser la fecha de inicio del siguiente periodo de
	 * suscripción. Ojo, la suscripcion debe tener el número de meses actuales, no
	 * actualizados para próximo periodo
	 * 
	 * @param suscripcion la suscripcion sobre la que se hace el calculo
	 * @return la fecha de inicio del periodo
	 */
	ZonedDateTime calcularProximaSuscripcionFechaPeriodoDesde(Suscripcion suscripcion);

	/**
	 * Calcula cual va a ser la fecha de fin del siguiente periodo de suscripción.
	 * Ojo, la suscripcion debe tener el número de meses actuales, no actualizados
	 * para próximo periodo
	 * 
	 * @param suscripcion la suscripción sobre la que se hace el cálculo
	 * @return la fecha de fin del periodo
	 */
	ZonedDateTime calcularProximaSuscripcionFechaPeriodoHasta(Suscripcion suscripcion);

//	void pruebaRenovacion(Long clienteId);

	void aceptaPoliticaPrivacidad(Long clienteId);

	void aceptaTerminosCondiciones(Long clienteId);

	ClienteConfiguracion buscarClienteConfiguracionPorClienteId(Long clienteId);

	void actualizarClienteConfiguracion(ClienteConfiguracion clienteConfiguracion);

	ZonedDateTime calcularProximaSuscripcionFechaPeriodoDesde(ZonedDateTime fechaUltimaActivacion, Integer mesesActiva);
}
