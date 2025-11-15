package es.pocketrainer.servicio.usuario;

import org.springframework.security.core.userdetails.UserDetailsService;

import es.pocketrainer.modelo.usuario.Usuario;

public interface UsuarioServicio extends UserDetailsService {

	void crearNuevoUsuario(Usuario usuario);

	Boolean existeUsuario(Usuario usuario);

	Usuario buscarUsuarioPorUsuarioNombre(String usuarioNombre);

	boolean comprobarPassword(String password);

	void cambiarPassword(String password);

	void recuperarPassword(String correo);

	String generateCommonLangPassword();

}
