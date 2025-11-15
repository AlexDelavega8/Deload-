package es.pocketrainer.modelo.videoconferencia;

import java.time.ZonedDateTime;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import es.pocketrainer.modelo.cliente.Cliente;
import es.pocketrainer.modelo.entrenador.Entrenador;
import es.pocketrainer.modelo.valormaestro.ValorMaestro;

@Entity
public class Videoconferencia {

	@Id
	@GeneratedValue
	private Long id;
	private Long rutinaId;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "estado_codigo", referencedColumnName = "codigo")
	private ValorMaestro estado;
	@ManyToOne
	@JoinColumn(name = "cliente_id")
	private Cliente cliente;
	@ManyToOne
	@JoinColumn(name = "entrenador_id")
	private Entrenador entrenador;
	private ZonedDateTime fechaHoraProgramada;
	private String salaId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getRutinaId() {
		return rutinaId;
	}

	public void setRutinaId(Long rutinaId) {
		this.rutinaId = rutinaId;
	}

	public ValorMaestro getEstado() {
		return estado;
	}

	public void setEstado(ValorMaestro estado) {
		this.estado = estado;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Entrenador getEntrenador() {
		return entrenador;
	}

	public void setEntrenador(Entrenador entrenador) {
		this.entrenador = entrenador;
	}

	public ZonedDateTime getFechaHoraProgramada() {
		return fechaHoraProgramada;
	}

	public void setFechaHoraProgramada(ZonedDateTime fechaHoraProgramada) {
		this.fechaHoraProgramada = fechaHoraProgramada;
	}

	public String getSalaId() {
		return salaId;
	}

	public void setSalaId(String salaId) {
		this.salaId = salaId;
	}

}
