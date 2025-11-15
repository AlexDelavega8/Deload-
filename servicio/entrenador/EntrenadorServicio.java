package es.pocketrainer.servicio.entrenador;

import java.util.List;

import es.pocketrainer.formulario.BuscarEntrenadorFormulario;
import es.pocketrainer.modelo.entrenador.Entrenador;

public interface EntrenadorServicio {

	List<Entrenador> buscarEntrenadorActivoTodos();

	List<Entrenador> buscarEntrenadorTodos();

	Entrenador buscarEntrenadorPorId(Long entrenadorId);

	Entrenador buscarAdministrador();

	void revisarTrabajoDeEntrenador();

	void crearEntrenadorNuevo(Entrenador entrenador);

	void darBajaEntrenadorAccion(Long entrenadorId);

	void activarEntrenadorAccion(Long entrenadorId);

	List<Entrenador> buscarEntrenadorPorFiltro(BuscarEntrenadorFormulario buscarEntrenadorFormulario);

}
