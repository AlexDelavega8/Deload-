package es.pocketrainer.modelo.usuario;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Transient;

@Entity
public class Usuario {

	@Id
	@GeneratedValue
	private Long id;
	private String usuarioNombre;
	@Transient
	private String usuarioNombreRepeticion;
	private String password;
	@Transient
	private String passwordRepeticion;
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "USUARIO_ROL", joinColumns = {
			@JoinColumn(name = "id_usuario", referencedColumnName = "id", nullable = false) }, inverseJoinColumns = {
					@JoinColumn(name = "id_rol", referencedColumnName = "id", nullable = false) })
	private Set<Rol> rolLista;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsuarioNombre() {
		return usuarioNombre;
	}

	public void setUsuarioNombre(String usuarioNombre) {
		this.usuarioNombre = usuarioNombre;
	}

	public String getUsuarioNombreRepeticion() {
		return usuarioNombreRepeticion;
	}

	public void setUsuarioNombreRepeticion(String usuarioNombreRepeticion) {
		this.usuarioNombreRepeticion = usuarioNombreRepeticion;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<Rol> getRolLista() {
		return rolLista;
	}

	public void setRolLista(Set<Rol> rolLista) {
		this.rolLista = rolLista;
	}

	public String getPasswordRepeticion() {
		return passwordRepeticion;
	}

	public void setPasswordRepeticion(String passwordRepeticion) {
		this.passwordRepeticion = passwordRepeticion;
	}

}
