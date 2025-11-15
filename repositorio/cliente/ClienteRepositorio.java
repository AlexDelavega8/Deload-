package es.pocketrainer.repositorio.cliente;

import org.springframework.data.jpa.repository.JpaRepository;

import es.pocketrainer.modelo.cliente.Cliente;

public interface ClienteRepositorio extends JpaRepository<Cliente, Long>, ClienteRepositorioExtendido {

}
