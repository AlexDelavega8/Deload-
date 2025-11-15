package es.pocketrainer.modelo.cliente;

import java.time.ZonedDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import es.pocketrainer.modelo.cobro.SuscripcionCobro;
import es.pocketrainer.modelo.servicio.Servicio;
import es.pocketrainer.modelo.valormaestro.ValorMaestro;

@Entity
public class Suscripcion {

	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "estado_codigo", referencedColumnName = "codigo")
	private ValorMaestro estado;

	@OneToOne
	@JoinColumn(name = "cliente_id")
	private Cliente cliente;

	@ManyToOne
	@JoinColumn(name = "servicio_id")
	private Servicio servicio;

	/** Fecha registro inicial */
	private ZonedDateTime fechaPrimeraSuscripcion;

	/** Fecha ultima reactivacion */
	private ZonedDateTime fechaUltimaActivacion;

	/** Meses desde ultima reactivacion */
	private int mesesActiva;

	/**
	 * Periodos: En base a fechaUltimaActivacion y mesesActiva
	 */
	private ZonedDateTime fechaPeriodoDesde;
	private ZonedDateTime fechaPeriodoHasta;
	private ZonedDateTime fechaBaja;

	@OneToMany(mappedBy = "suscripcion", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<SuscripcionCobro> suscripcionCobroLista;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ValorMaestro getEstado() {
		return estado;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Servicio getServicio() {
		return servicio;
	}

	public void setServicio(Servicio servicio) {
		this.servicio = servicio;
	}

	public ZonedDateTime getFechaPrimeraSuscripcion() {
		return fechaPrimeraSuscripcion;
	}

	public void setFechaPrimeraSuscripcion(ZonedDateTime fechaPrimeraSuscripcion) {
		this.fechaPrimeraSuscripcion = fechaPrimeraSuscripcion;
	}

	public ZonedDateTime getFechaUltimaActivacion() {
		return fechaUltimaActivacion;
	}

	public void setFechaUltimaActivacion(ZonedDateTime fechaUltimaActivacion) {
		this.fechaUltimaActivacion = fechaUltimaActivacion;
	}

	public int getMesesActiva() {
		return mesesActiva;
	}

	public void setMesesActiva(int mesesActiva) {
		this.mesesActiva = mesesActiva;
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

	public ZonedDateTime getFechaBaja() {
		return fechaBaja;
	}

	public void setFechaBaja(ZonedDateTime fechaBaja) {
		this.fechaBaja = fechaBaja;
	}

	public List<SuscripcionCobro> getSuscripcionCobroLista() {
		return suscripcionCobroLista;
	}

	public void setSuscripcionCobroLista(List<SuscripcionCobro> suscripcionCobroLista) {
		this.suscripcionCobroLista = suscripcionCobroLista;
	}

	public void setEstado(ValorMaestro estado) {
		this.estado = estado;
	}

}
