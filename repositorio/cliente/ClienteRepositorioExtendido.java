package es.pocketrainer.repositorio.cliente;

import java.util.List;

import es.pocketrainer.formulario.BuscarClienteFormulario;
import es.pocketrainer.modelo.cliente.Cliente;

public interface ClienteRepositorioExtendido {

	Cliente buscarClientePorUsuarioId(Long usuarioId);

	Cliente buscarClientePorUsuarioNombre(String usuarioNombre);

	List<Cliente> buscarClientePorEntrenador(Long entrenadorId);

	List<Cliente> buscarClienteActivoLista();

	List<Cliente> buscarClientePorFiltro(BuscarClienteFormulario buscarClienteFormulario);
}
