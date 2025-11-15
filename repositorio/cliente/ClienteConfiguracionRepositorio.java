package es.pocketrainer.repositorio.cliente;

import org.springframework.data.jpa.repository.JpaRepository;

import es.pocketrainer.modelo.cliente.Cliente;
import es.pocketrainer.modelo.cliente.ClienteConfiguracion;

public interface ClienteConfiguracionRepositorio extends JpaRepository<ClienteConfiguracion, Long> {

	ClienteConfiguracion findByCliente(Cliente cliente);
}
