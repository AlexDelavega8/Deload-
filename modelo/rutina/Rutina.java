package es.pocketrainer.modelo.rutina;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;

import es.pocketrainer.modelo.cliente.Cliente;
import es.pocketrainer.modelo.ejercicio.Ejercicio;
import es.pocketrainer.modelo.entrenador.Entrenador;
import es.pocketrainer.modelo.valormaestro.ValorMaestro;

@Entity
public class Rutina {

	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "estado_codigo", referencedColumnName = "codigo")
	private ValorMaestro estado;

	private Boolean esPlantilla;
//
//	@ManyToOne(fetch = FetchType.EAGER)
//	@JoinColumn(name = "tipo_codigo", referencedColumnName = "codigo")
//	
//	@Transient
//	private ValorMaestro tipo;

	private String nombre;
	private String comentariosEntrenador;
	private String comentariosCliente;
	private ZonedDateTime fechaCreacion;

	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "fase_inicial_id")
	private RutinaFase faseInicial;

	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "fase_principal_id")
	private RutinaFase fasePrincipal;

	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "fase_final_id")
	private RutinaFase faseFinal;

	@ManyToOne
	@JoinColumn(name = "cliente_id")
	private Cliente cliente;

	@ManyToOne
	@JoinColumn(name = "entrenador_id")
	private Entrenador entrenador;

	private Boolean enEjecucion;

	@ElementCollection
	@CollectionTable(name = "rutina_fecha", joinColumns = @JoinColumn(name = "rutina_id"))
	@Column(name = "fecha")
	@OrderBy("fecha asc")
	private Set<ZonedDateTime> rutinaFechaLista;

	// Definidos por cliente
	private Integer clienteTiempoDescansoEntreEjercicios;
	private Integer clienteTiempoDescansoEntreUnilaterales;
	private Integer clienteTiempoEjercicioPorRepeticiones;
	private Boolean clienteEjecucionAutomatica;

	// Definidos por entrenador
	private Integer entrenadorTiempoDescansoEntreEjercicios;
	private Integer entrenadorTiempoDescansoEntreUnilaterales;
	private Integer entrenadorTiempoEjercicioPorRepeticiones;

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

	public String getComentariosEntrenador() {
		return comentariosEntrenador;
	}

	public void setComentariosEntrenador(String comentariosEntrenador) {
		this.comentariosEntrenador = comentariosEntrenador;
	}

	public String getComentariosCliente() {
		return comentariosCliente;
	}

	public void setComentariosCliente(String comentariosCliente) {
		this.comentariosCliente = comentariosCliente;
	}

//	public ValorMaestro getTipo() {
//		return tipo;
//	}
//
//	public void setTipo(ValorMaestro tipo) {
//		this.tipo = tipo;
//	}

	public ValorMaestro getEstado() {
		return estado;
	}

	public void setEstado(ValorMaestro estado) {
		this.estado = estado;
	}

	public Boolean getEsPlantilla() {
		return esPlantilla;
	}

	public void setEsPlantilla(Boolean esPlantilla) {
		this.esPlantilla = esPlantilla;
	}

	public ZonedDateTime getFechaCreacion() {
		return fechaCreacion;
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

	public void setFechaCreacion(ZonedDateTime fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public RutinaFase getFaseInicial() {
		return faseInicial;
	}

	public void setFaseInicial(RutinaFase faseInicial) {
		this.faseInicial = faseInicial;
	}

	public RutinaFase getFasePrincipal() {
		return fasePrincipal;
	}

	public void setFasePrincipal(RutinaFase fasePrincipal) {
		this.fasePrincipal = fasePrincipal;
	}

	public RutinaFase getFaseFinal() {
		return faseFinal;
	}

	public void setFaseFinal(RutinaFase faseFinal) {
		this.faseFinal = faseFinal;
	}

	public Boolean getEnEjecucion() {
		return enEjecucion;
	}

	public void setEnEjecucion(Boolean enEjecucion) {
		this.enEjecucion = enEjecucion;
	}

	public Set<ZonedDateTime> getRutinaFechaLista() {
		return rutinaFechaLista;
	}

	public void setRutinaFechaLista(Set<ZonedDateTime> rutinaFechaLista) {
		this.rutinaFechaLista = rutinaFechaLista;
	}

	public Integer getTiempo() {
		List<RutinaFaseGrupoRepeticionEjercicio> rutinaFaseGrupoEjercicioTodosLista = new ArrayList<>();

		if (getFaseInicial() != null && !getFaseInicial().getRutinaFaseGrupoLista().isEmpty()) {
			getFaseInicial().getRutinaFaseGrupoLista().stream()
					.forEach(it -> rutinaFaseGrupoEjercicioTodosLista.addAll(it.getRutinaFaseGrupoEjercicioConRepeticionesLista()));
		}

		if (getFasePrincipal() != null && !getFasePrincipal().getRutinaFaseGrupoLista().isEmpty()) {
			getFasePrincipal().getRutinaFaseGrupoLista().stream()
					.forEach(it -> rutinaFaseGrupoEjercicioTodosLista.addAll(it.getRutinaFaseGrupoEjercicioConRepeticionesLista()));
		}

		if (getFaseFinal() != null && !getFaseFinal().getRutinaFaseGrupoLista().isEmpty()) {
			getFaseFinal().getRutinaFaseGrupoLista().stream()
					.forEach(it -> rutinaFaseGrupoEjercicioTodosLista.addAll(it.getRutinaFaseGrupoEjercicioConRepeticionesLista()));
		}

		return (rutinaFaseGrupoEjercicioTodosLista.size() * 90) / 60;
	}

	public Integer getNumeroEjercicios() {
		List<RutinaFaseGrupoRepeticionEjercicio> rutinaFaseGrupoEjercicioTodosLista = new ArrayList<>();

		if (getFaseInicial() != null && !getFaseInicial().getRutinaFaseGrupoLista().isEmpty()) {
			getFaseInicial().getRutinaFaseGrupoLista().stream()
					.forEach(it -> rutinaFaseGrupoEjercicioTodosLista.addAll(it.getRutinaFaseGrupoEjercicioConRepeticionesLista()));
		}

		if (getFasePrincipal() != null && !getFasePrincipal().getRutinaFaseGrupoLista().isEmpty()) {
			getFasePrincipal().getRutinaFaseGrupoLista().stream()
					.forEach(it -> rutinaFaseGrupoEjercicioTodosLista.addAll(it.getRutinaFaseGrupoEjercicioConRepeticionesLista()));
		}

		if (getFaseFinal() != null && !getFaseFinal().getRutinaFaseGrupoLista().isEmpty()) {
			getFaseFinal().getRutinaFaseGrupoLista().stream()
					.forEach(it -> rutinaFaseGrupoEjercicioTodosLista.addAll(it.getRutinaFaseGrupoEjercicioConRepeticionesLista()));
		}

		return rutinaFaseGrupoEjercicioTodosLista.size();
	}

	public List<Ejercicio> getEjercicioTodos() {
		List<RutinaFaseGrupoRepeticionEjercicio> rutinaFaseGrupoEjercicioTodosLista = new ArrayList<>();

		if (getFaseInicial() != null && !getFaseInicial().getRutinaFaseGrupoLista().isEmpty()) {
			getFaseInicial().getRutinaFaseGrupoLista().stream()
					.forEach(it -> rutinaFaseGrupoEjercicioTodosLista.addAll(it.getRutinaFaseGrupoEjercicioConRepeticionesLista()));
		}

		if (getFasePrincipal() != null && !getFasePrincipal().getRutinaFaseGrupoLista().isEmpty()) {
			getFasePrincipal().getRutinaFaseGrupoLista().stream()
					.forEach(it -> rutinaFaseGrupoEjercicioTodosLista.addAll(it.getRutinaFaseGrupoEjercicioConRepeticionesLista()));
		}

		if (getFaseFinal() != null && !getFaseFinal().getRutinaFaseGrupoLista().isEmpty()) {
			getFaseFinal().getRutinaFaseGrupoLista().stream()
					.forEach(it -> rutinaFaseGrupoEjercicioTodosLista.addAll(it.getRutinaFaseGrupoEjercicioConRepeticionesLista()));
		}

		return rutinaFaseGrupoEjercicioTodosLista.parallelStream().map(it -> it.getEjercicio()).distinct().collect(Collectors.toList());
	}

	public List<RutinaFaseGrupoRepeticionEjercicio> getRutinaFaseGrupoRepeticionEjercicioTodos() {
		List<RutinaFaseGrupoRepeticionEjercicio> rutinaFaseGrupoEjercicioTodosLista = new ArrayList<>();

		if (getFaseInicial() != null && !getFaseInicial().getRutinaFaseGrupoLista().isEmpty()) {
			getFaseInicial().getRutinaFaseGrupoLista().stream()
					.forEach(it -> rutinaFaseGrupoEjercicioTodosLista.addAll(it.getRutinaFaseGrupoEjercicioConRepeticionesLista()));
		}

		if (getFasePrincipal() != null && !getFasePrincipal().getRutinaFaseGrupoLista().isEmpty()) {
			getFasePrincipal().getRutinaFaseGrupoLista().stream()
					.forEach(it -> rutinaFaseGrupoEjercicioTodosLista.addAll(it.getRutinaFaseGrupoEjercicioConRepeticionesLista()));
		}

		if (getFaseFinal() != null && !getFaseFinal().getRutinaFaseGrupoLista().isEmpty()) {
			getFaseFinal().getRutinaFaseGrupoLista().stream()
					.forEach(it -> rutinaFaseGrupoEjercicioTodosLista.addAll(it.getRutinaFaseGrupoEjercicioConRepeticionesLista()));
		}

		return rutinaFaseGrupoEjercicioTodosLista;
	}

	public Rutina copiarParaCrearPlantilla() {
		var rutinaCopia = new Rutina();
		rutinaCopia.setEsPlantilla(Boolean.TRUE);
		rutinaCopia.setFechaCreacion(ZonedDateTime.now());
		rutinaCopia.setEnEjecucion(Boolean.FALSE);
		rutinaCopia.setFaseInicial(this.getFaseInicial().copiarParaCrearPlantilla());
		rutinaCopia.setFasePrincipal(this.getFasePrincipal().copiarParaCrearPlantilla());
		rutinaCopia.setFaseFinal(this.getFaseFinal().copiarParaCrearPlantilla());
		rutinaCopia.setEntrenadorTiempoDescansoEntreEjercicios(this.getEntrenadorTiempoDescansoEntreEjercicios());
		rutinaCopia.setEntrenadorTiempoDescansoEntreUnilaterales(this.getEntrenadorTiempoDescansoEntreUnilaterales());
		rutinaCopia.setEntrenadorTiempoEjercicioPorRepeticiones(this.getEntrenadorTiempoEjercicioPorRepeticiones());

		return rutinaCopia;
	}

	public Integer getClienteTiempoDescansoEntreEjercicios() {
		return clienteTiempoDescansoEntreEjercicios;
	}

	public void setClienteTiempoDescansoEntreEjercicios(Integer clienteTiempoDescansoEntreEjercicios) {
		this.clienteTiempoDescansoEntreEjercicios = clienteTiempoDescansoEntreEjercicios;
	}

	public Integer getClienteTiempoDescansoEntreUnilaterales() {
		return clienteTiempoDescansoEntreUnilaterales;
	}

	public void setClienteTiempoDescansoEntreUnilaterales(Integer clienteTiempoDescansoEntreUnilaterales) {
		this.clienteTiempoDescansoEntreUnilaterales = clienteTiempoDescansoEntreUnilaterales;
	}

	public Integer getClienteTiempoEjercicioPorRepeticiones() {
		return clienteTiempoEjercicioPorRepeticiones;
	}

	public void setClienteTiempoEjercicioPorRepeticiones(Integer clienteTiempoEjercicioPorRepeticiones) {
		this.clienteTiempoEjercicioPorRepeticiones = clienteTiempoEjercicioPorRepeticiones;
	}

	public Boolean getClienteEjecucionAutomatica() {
		return clienteEjecucionAutomatica;
	}

	public void setClienteEjecucionAutomatica(Boolean clienteEjecucionAutomatica) {
		this.clienteEjecucionAutomatica = clienteEjecucionAutomatica;
	}

	public Integer getEntrenadorTiempoDescansoEntreEjercicios() {
		return entrenadorTiempoDescansoEntreEjercicios;
	}

	public void setEntrenadorTiempoDescansoEntreEjercicios(Integer entrenadorTiempoDescansoEntreEjercicios) {
		this.entrenadorTiempoDescansoEntreEjercicios = entrenadorTiempoDescansoEntreEjercicios;
	}

	public Integer getEntrenadorTiempoDescansoEntreUnilaterales() {
		return entrenadorTiempoDescansoEntreUnilaterales;
	}

	public void setEntrenadorTiempoDescansoEntreUnilaterales(Integer entrenadorTiempoDescansoEntreUnilaterales) {
		this.entrenadorTiempoDescansoEntreUnilaterales = entrenadorTiempoDescansoEntreUnilaterales;
	}

	public Integer getEntrenadorTiempoEjercicioPorRepeticiones() {
		return entrenadorTiempoEjercicioPorRepeticiones;
	}

	public void setEntrenadorTiempoEjercicioPorRepeticiones(Integer entrenadorTiempoEjercicioPorRepeticiones) {
		this.entrenadorTiempoEjercicioPorRepeticiones = entrenadorTiempoEjercicioPorRepeticiones;
	}

}
