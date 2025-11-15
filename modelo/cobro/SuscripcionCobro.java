package es.pocketrainer.modelo.cobro;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import es.pocketrainer.modelo.cliente.Suscripcion;
import es.pocketrainer.modelo.entrenador.Entrenador;
import es.pocketrainer.modelo.valormaestro.ValorMaestro;

@Entity
public class SuscripcionCobro {

	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne
	@JoinColumn(name = "suscripcion_id")
	private Suscripcion suscripcion;

	@ManyToOne
	@JoinColumn(name = "entrenador_id")
	private Entrenador entrenador;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "estado_codigo", referencedColumnName = "codigo")
	private ValorMaestro estado;
	private BigDecimal cantidad;
	private ZonedDateTime fechaPeriodoDesde;
	private ZonedDateTime fechaPeriodoHasta;
	private ZonedDateTime fechaHoraEmision;
	private ZonedDateTime fechaHoraCobro;
	private ZonedDateTime fechaHoraNotificacionAsincrona;
	private ZonedDateTime fechaHoraNotificacionSincronaOk;
	private ZonedDateTime fechaHoraNotificacionSincronaKo;
	private String transaccionTipo;
	private String numeroPedido;
	private String codigoRespuesta;
	private String codigoAutorizacion;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Suscripcion getSuscripcion() {
		return suscripcion;
	}

	public void setSuscripcion(Suscripcion suscripcion) {
		this.suscripcion = suscripcion;
	}

	public Entrenador getEntrenador() {
		return entrenador;
	}

	public void setEntrenador(Entrenador entrenador) {
		this.entrenador = entrenador;
	}

	public ValorMaestro getEstado() {
		return estado;
	}

	public void setEstado(ValorMaestro estado) {
		this.estado = estado;
	}

	public BigDecimal getCantidad() {
		return cantidad;
	}

	public void setCantidad(BigDecimal cantidad) {
		this.cantidad = cantidad;
	}

	public ZonedDateTime getFechaPeriodoDesde() {
		return fechaPeriodoDesde;
	}

	public void setFechaPeriodoDesde(ZonedDateTime fechaPeriodoDesde) {
		this.fechaPeriodoDesde = fechaPeriodoDesde;
	}

	public ZonedDateTime getFechaPeriodoHasta() {
		return fechaPeriodoHasta;
	}

	public void setFechaPeriodoHasta(ZonedDateTime fechaPeriodoHasta) {
		this.fechaPeriodoHasta = fechaPeriodoHasta;
	}

	public ZonedDateTime getFechaHoraEmision() {
		return fechaHoraEmision;
	}

	public void setFechaHoraEmision(ZonedDateTime fechaHoraEmision) {
		this.fechaHoraEmision = fechaHoraEmision;
	}

	public ZonedDateTime getFechaHoraCobro() {
		return fechaHoraCobro;
	}

	public void setFechaHoraCobro(ZonedDateTime fechaHoraCobro) {
		this.fechaHoraCobro = fechaHoraCobro;
	}

	public ZonedDateTime getFechaHoraNotificacionAsincrona() {
		return fechaHoraNotificacionAsincrona;
	}

	public void setFechaHoraNotificacionAsincrona(ZonedDateTime fechaHoraNotificacionAsincrona) {
		this.fechaHoraNotificacionAsincrona = fechaHoraNotificacionAsincrona;
	}

	public ZonedDateTime getFechaHoraNotificacionSincronaOk() {
		return fechaHoraNotificacionSincronaOk;
	}

	public void setFechaHoraNotificacionSincronaOk(ZonedDateTime fechaHoraNotificacionSincronaOk) {
		this.fechaHoraNotificacionSincronaOk = fechaHoraNotificacionSincronaOk;
	}

	public ZonedDateTime getFechaHoraNotificacionSincronaKo() {
		return fechaHoraNotificacionSincronaKo;
	}

	public void setFechaHoraNotificacionSincronaKo(ZonedDateTime fechaHoraNotificacionSincronaKo) {
		this.fechaHoraNotificacionSincronaKo = fechaHoraNotificacionSincronaKo;
	}

	public String getTransaccionTipo() {
		return transaccionTipo;
	}

	public void setTransaccionTipo(String transaccionTipo) {
		this.transaccionTipo = transaccionTipo;
	}

	public String getNumeroPedido() {
		return numeroPedido;
	}

	public void setNumeroPedido(String numeroPedido) {
		this.numeroPedido = numeroPedido;
	}

	public String getCodigoRespuesta() {
		return codigoRespuesta;
	}

	public void setCodigoRespuesta(String codigoRespuesta) {
		this.codigoRespuesta = codigoRespuesta;
	}

	public String getCodigoAutorizacion() {
		return codigoAutorizacion;
	}

	public void setCodigoAutorizacion(String codigoAutorizacion) {
		this.codigoAutorizacion = codigoAutorizacion;
	}

}
