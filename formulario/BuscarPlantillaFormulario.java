package es.pocketrainer.formulario;

public class BuscarPlantillaFormulario {

	private String nombre;
	private String comentariosEntrenador;
	private Long entrenadorId;
	private Boolean esPlantilla;

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getComentariosEntrenador() {
		return comentariosEntrenador;
	}

	public void setComentariosEntrenador(String comentariosEntrenador) {
		this.comentariosEntrenador = comentariosEntrenador;
	}

	public Boolean getEsPlantilla() {
		return esPlantilla;
	}

	public void setEsPlantilla(Boolean esPlantilla) {
		this.esPlantilla = esPlantilla;
	}

	public Long getEntrenadorId() {
		return entrenadorId;
	}

	public void setEntrenadorId(Long entrenadorId) {
		this.entrenadorId = entrenadorId;
	}

}
