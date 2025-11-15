package es.pocketrainer.modelo.cliente;

import java.time.ZonedDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class ClienteDatosCobro {

	@Id
	@GeneratedValue
	private Long id;

	@OneToOne
	@JoinColumn(name = "cliente_id")
	private Cliente cliente;

	private String tarjetaIdentificacionToken;
	private String identificadorTransaccionCof;
	private ZonedDateTime tarjetaFechaCaducidad;
	private ZonedDateTime fechaUltimaActualizacion;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public String getTarjetaIdentificacionToken() {
		return tarjetaIdentificacionToken;
	}

	public void setTarjetaIdentificacionToken(String tarjetaIdentificacionToken) {
		this.tarjetaIdentificacionToken = tarjetaIdentificacionToken;
	}

	public String getIdentificadorTransaccionCof() {
		return identificadorTransaccionCof;
	}

	public void setIdentificadorTransaccionCof(String identificadorTransaccionCof) {
		this.identificadorTransaccionCof = identificadorTransaccionCof;
	}

	public ZonedDateTime getTarjetaFechaCaducidad() {
		return tarjetaFechaCaducidad;
	}

	public void setTarjetaFechaCaducidad(ZonedDateTime tarjetaFechaCaducidad) {
		this.tarjetaFechaCaducidad = tarjetaFechaCaducidad;
	}

	public ZonedDateTime getFechaUltimaActualizacion() {
		return fechaUltimaActualizacion;
	}

	public void setFechaUltimaActualizacion(ZonedDateTime fechaUltimaActualizacion) {
		this.fechaUltimaActualizacion = fechaUltimaActualizacion;
	}

}
