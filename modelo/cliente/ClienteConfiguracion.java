package es.pocketrainer.modelo.cliente;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class ClienteConfiguracion {

	@Id
	@GeneratedValue
	private Long id;
	private Integer tiempoDescansoEntreEjercicios;
	private Integer tiempoDescansoEntreUnilaterales;
	private Integer tiempoEjercicioPorRepeticiones;
	private Boolean ejecucionAutomatica;

	@OneToOne
	@JoinColumn(name = "cliente_id")
	private Cliente cliente;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Boolean getEjecucionAutomatica() {
		return ejecucionAutomatica;
	}

	public void setEjecucionAutomatica(Boolean ejecucionAutomatica) {
		this.ejecucionAutomatica = ejecucionAutomatica;
	}

}
