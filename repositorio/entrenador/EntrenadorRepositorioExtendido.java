package es.pocketrainer.repositorio.entrenador;

import java.util.List;

import es.pocketrainer.formulario.BuscarEntrenadorFormulario;
import es.pocketrainer.modelo.entrenador.Entrenador;

public interface EntrenadorRepositorioExtendido {

	Entrenador buscarEntrenadorActivoPorUsuarioId(Long usuarioId);

	Entrenador buscarEntrenadorPorEmail(String correoAdministrador);

	List<Entrenador> buscarEntrenadorPorFiltro(BuscarEntrenadorFormulario buscarEntrenadorFormulario);
}
