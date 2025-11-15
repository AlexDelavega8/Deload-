package es.pocketrainer.formulario;

import java.time.ZonedDateTime;

public class BuscarRutinaFormulario {

	private Long entrenadorId;
	private ZonedDateTime fechaCreacionDesde;
	private ZonedDateTime fechaCreacionHasta;

	public Long getEntrenadorId() {
		return entrenadorId;
	}

	public void setEntrenadorId(Long entrenadorId) {
		this.entrenadorId = entrenadorId;
	}

	public ZonedDateTime getFechaCreacionDesde() {
		return fechaCreacionDesde;
	}

	public void setFechaCreacionDesde(ZonedDateTime fechaCreacionDesde) {
		this.fechaCreacionDesde = fechaCreacionDesde;
	}

	public ZonedDateTime getFechaCreacionHasta() {
		return fechaCreacionHasta;
	}

	public void setFechaCreacionHasta(ZonedDateTime fechaCreacionHasta) {
		this.fechaCreacionHasta = fechaCreacionHasta;
	}

}
