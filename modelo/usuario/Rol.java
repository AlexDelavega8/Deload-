package es.pocketrainer.modelo.usuario;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

@Entity
public class Rol {

	private Long id;
	private String codigo;
	private String nombre;
	private Set<Permiso> permisoLista;

	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "ROL_PERMISO", joinColumns = {
			@JoinColumn(name = "id_rol", referencedColumnName = "id", nullable = false) }, inverseJoinColumns = {
					@JoinColumn(name = "id_permiso", referencedColumnName = "id", nullable = false) })
	public Set<Permiso> getPermisoLista() {
		return permisoLista;
	}

	public void setPermisoLista(Set<Permiso> permisoLista) {
		this.permisoLista = permisoLista;
	}

}
