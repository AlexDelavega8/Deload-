package es.pocketrainer.modelo.valormaestro;

import javax.persistence.Entity;

import es.pocketrainer.util.bbdd.TraduccionI18N;

@Entity
public class ValorMaestroI18N extends TraduccionI18N {

	private String nombreCorto;
	private String nombre;
	private String descripcion;

	public String getNombreCorto() {
		return nombreCorto;
	}

	public void setNombreCorto(String nombreCorto) {
		this.nombreCorto = nombreCorto;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

}
