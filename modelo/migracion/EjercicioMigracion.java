package es.pocketrainer.modelo.migracion;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class EjercicioMigracion {

	@Id
	private Long id;
	private String nombre;
	private String nombreLargo;
	private String descripcion;
	private String patrones;
	private String zonas;
	private String partesObjetivo;
	private String partes;
	private String musculosObjetivo;
	private String musculos;
	private String material;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getNombreLargo() {
		return nombreLargo;
	}

	public void setNombreLargo(String nombreLargo) {
		this.nombreLargo = nombreLargo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getPatrones() {
		return patrones;
	}

	public void setPatrones(String patrones) {
		this.patrones = patrones;
	}

	public String getZonas() {
		return zonas;
	}

	public void setZonas(String zonas) {
		this.zonas = zonas;
	}

	public String getPartesObjetivo() {
		return partesObjetivo;
	}

	public void setPartesObjetivo(String partesObjetivo) {
		this.partesObjetivo = partesObjetivo;
	}

	public String getPartes() {
		return partes;
	}

	public void setPartes(String partes) {
		this.partes = partes;
	}

	public String getMusculosObjetivo() {
		return musculosObjetivo;
	}

	public void setMusculosObjetivo(String musculosObjetivo) {
		this.musculosObjetivo = musculosObjetivo;
	}

	public String getMusculos() {
		return musculos;
	}

	public void setMusculos(String musculos) {
		this.musculos = musculos;
	}

	public String getMaterial() {
		return material;
	}

	public void setMaterial(String material) {
		this.material = material;
	}

}
