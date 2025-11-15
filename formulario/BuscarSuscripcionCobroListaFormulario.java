package es.pocketrainer.formulario;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import org.springframework.format.annotation.DateTimeFormat;

public class BuscarSuscripcionCobroListaFormulario {

	private Integer anho;
	private LocalDate mes;

	private ZonedDateTime fechaEmisionRangoInicio;
	private ZonedDateTime fechaEmisionRangoFin;

	private ZonedDateTime fechaCobroRangoInicio;
	private ZonedDateTime fechaCobroRangoFin;

	private List<String> estadoCodigoLista;

	public Integer getAnho() {
		return anho;
	}

	public void setAnho(Integer anho) {
		this.anho = anho;
	}

	@DateTimeFormat(pattern = "dd/MM/yyyy")
	public LocalDate getMes() {
		return mes;
	}

	public void setMes(LocalDate mes) {
		this.mes = mes;
	}

	public String getMesString() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM", new Locale("es"));
		return formatter.format(mes);
	}

	@DateTimeFormat(pattern = "dd/MM/yyyy")
	public ZonedDateTime getFechaEmisionRangoInicio() {
		return fechaEmisionRangoInicio;
	}

	public void setFechaEmisionRangoInicio(ZonedDateTime fechaEmisionRangoInicio) {
		this.fechaEmisionRangoInicio = fechaEmisionRangoInicio;
	}

	@DateTimeFormat(pattern = "dd/MM/yyyy")
	public ZonedDateTime getFechaEmisionRangoFin() {
		return fechaEmisionRangoFin;
	}

	public void setFechaEmisionRangoFin(ZonedDateTime fechaEmisionRangoFin) {
		this.fechaEmisionRangoFin = fechaEmisionRangoFin;
	}

	public ZonedDateTime getFechaCobroRangoInicio() {
		return fechaCobroRangoInicio;
	}

	public void setFechaCobroRangoInicio(ZonedDateTime fechaCobroRangoInicio) {
		this.fechaCobroRangoInicio = fechaCobroRangoInicio;
	}

	public ZonedDateTime getFechaCobroRangoFin() {
		return fechaCobroRangoFin;
	}

	public void setFechaCobroRangoFin(ZonedDateTime fechaCobroRangoFin) {
		this.fechaCobroRangoFin = fechaCobroRangoFin;
	}

	public List<String> getEstadoCodigoLista() {
		return estadoCodigoLista;
	}

	public void setEstadoCodigoLista(List<String> estadoCodigoLista) {
		this.estadoCodigoLista = estadoCodigoLista;
	}

}
