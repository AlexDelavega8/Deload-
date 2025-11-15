package es.pocketrainer.formulario;

public class ConfigurarRutinaFormulario {

	private Long rutinaId;
	private Integer tiempoDescansoEntreEjercicios;
	private Integer tiempoDescansoEntreUnilaterales;
	private Integer tiempoEjercicioPorRepeticiones;
	private Boolean ejecucionAutomatica;

	public Long getRutinaId() {
		return rutinaId;
	}

	public void setRutinaId(Long rutinaId) {
		this.rutinaId = rutinaId;
	}

	public Integer getTiempoDescansoEntreEjercicios() {
		return tiempoDescansoEntreEjercicios;
	}

	public void setTiempoDescansoEntreEjercicios(Integer tiempoDescansoEntreEjercicios) {
		this.tiempoDescansoEntreEjercicios = tiempoDescansoEntreEjercicios;
	}

	public Integer getTiempoDescansoEntreUnilaterales() {
		return tiempoDescansoEntreUnilaterales;
	}

	public void setTiempoDescansoEntreUnilaterales(Integer tiempoDescansoEntreUnilaterales) {
		this.tiempoDescansoEntreUnilaterales = tiempoDescansoEntreUnilaterales;
	}

	public Integer getTiempoEjercicioPorRepeticiones() {
		return tiempoEjercicioPorRepeticiones;
	}

	public void setTiempoEjercicioPorRepeticiones(Integer tiempoEjercicioPorRepeticiones) {
		this.tiempoEjercicioPorRepeticiones = tiempoEjercicioPorRepeticiones;
	}

	public Boolean getEjecucionAutomatica() {
		return ejecucionAutomatica;
	}

	public void setEjecucionAutomatica(Boolean ejecucionAutomatica) {
		this.ejecucionAutomatica = ejecucionAutomatica;
	}

}
