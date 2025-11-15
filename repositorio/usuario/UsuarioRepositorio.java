package es.pocketrainer.repositorio.usuario;

import org.springframework.data.jpa.repository.JpaRepository;

import es.pocketrainer.modelo.usuario.Usuario;

public interface UsuarioRepositorio extends JpaRepository<Usuario, Long>, UsuarioRepositorioExtendido {

}
