package es.pocketrainer.repositorio.usuario;

import org.springframework.data.jpa.repository.JpaRepository;

import es.pocketrainer.modelo.usuario.Rol;

public interface RolRepositorio extends JpaRepository<Rol, Long>, RolRepositorioExtendido {

}
