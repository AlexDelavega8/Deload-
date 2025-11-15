package es.pocketrainer.modelo.rutina;

import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;

import es.pocketrainer.modelo.ejercicio.Ejercicio;
import es.pocketrainer.modelo.ejercicio.Musculo;
import es.pocketrainer.modelo.ejercicio.ParteCuerpo;
import es.pocketrainer.modelo.ejercicio.Patron;
import es.pocketrainer.modelo.ejercicio.Zona;

@Entity
public class RutinaFaseGrupoRepeticionEjercicio {

	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne
	@JoinColumn(name = "rutina_fase_grupo_repeticion_id")
	private RutinaFaseGrupoRepeticion rutinaFaseGrupoRepeticion;

	@OneToOne
	@JoinColumn(name = "ejercicio_id")
	private Ejercicio ejercicio;
	private Integer posicion;
	private String comentarios;
	private Integer repeticiones;
	private Integer tiempo;

	private Boolean enEjecucion;
	@Transient
	private boolean ejercicioSinOtroCompatible;
	@Transient
	private boolean marcadoParaReemplazar;

	@ManyToMany
	@JoinTable(name = "rutina_fase_grupo_repeticion_ejercicio_patron", joinColumns = {
			@JoinColumn(name = "rutina_fase_grupo_repeticion_ejercicio_id", referencedColumnName = "id", nullable = false) }, inverseJoinColumns = {
					@JoinColumn(name = "patron_id", referencedColumnName = "id", nullable = false) })
	private Set<Patron> patronLista;

	@ManyToMany
	@JoinTable(name = "rutina_fase_grupo_repeticion_ejercicio_zona", joinColumns = {
			@JoinColumn(name = "rutina_fase_grupo_repeticion_ejercicio_id", referencedColumnName = "id", nullable = false) }, inverseJoinColumns = {
					@JoinColumn(name = "zona_id", referencedColumnName = "id", nullable = false) })
	private Set<Zona> zonaLista;

	@ManyToMany
	@JoinTable(name = "rutina_fase_grupo_repeticion_ejercicio_musculo", joinColumns = {
			@JoinColumn(name = "rutina_fase_grupo_repeticion_ejercicio_id", referencedColumnName = "id", nullable = false) }, inverseJoinColumns = {
					@JoinColumn(name = "musculo_id", referencedColumnName = "id", nullable = false) })
	private Set<Musculo> musculoLista;

	@ManyToMany
	@JoinTable(name = "rutina_fase_grupo_repeticion_ejercicio_parte_cuerpo", joinColumns = {
			@JoinColumn(name = "rutina_fase_grupo_repeticion_ejercicio_id", referencedColumnName = "id", nullable = false) }, inverseJoinColumns = {
					@JoinColumn(name = "parte_cuerpo_id", referencedColumnName = "id", nullable = false) })
	private Set<ParteCuerpo> parteCuerpoLista;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public RutinaFaseGrupoRepeticion getRutinaFaseGrupoRepeticion() {
		return rutinaFaseGrupoRepeticion;
	}

	public void setRutinaFaseGrupoRepeticion(RutinaFaseGrupoRepeticion rutinaFaseGrupoRepeticion) {
		this.rutinaFaseGrupoRepeticion = rutinaFaseGrupoRepeticion;
	}

	public Ejercicio getEjercicio() {
		return ejercicio;
	}

	public void setEjercicio(Ejercicio ejercicio) {
		this.ejercicio = ejercicio;
	}

	public Integer getPosicion() {
		return posicion;
	}

	public void setPosicion(Integer posicion) {
		this.posicion = posicion;
	}

	public String getComentarios() {
		return comentarios;
	}

	public void setComentarios(String comentarios) {
		this.comentarios = comentarios;
	}

	public Integer getRepeticiones() {
		return repeticiones;
	}

	public void setRepeticiones(Integer repeticiones) {
		this.repeticiones = repeticiones;
	}

	public Integer getTiempo() {
		return tiempo;
	}

	public void setTiempo(Integer tiempo) {
		this.tiempo = tiempo;
	}

	public Boolean getEnEjecucion() {
		return enEjecucion;
	}

	public void setEnEjecucion(Boolean enEjecucion) {
		this.enEjecucion = enEjecucion;
	}

	public Set<Patron> getPatronLista() {
		return patronLista;
	}

	public void setPatronLista(Set<Patron> patronLista) {
		this.patronLista = patronLista;
	}

	public Set<Zona> getZonaLista() {
		return zonaLista;
	}

	public void setZonaLista(Set<Zona> zonaLista) {
		this.zonaLista = zonaLista;
	}

	public Set<Musculo> getMusculoLista() {
		return musculoLista;
	}

	public void setMusculoLista(Set<Musculo> musculoLista) {
		this.musculoLista = musculoLista;
	}

	public Set<ParteCuerpo> getParteCuerpoLista() {
		return parteCuerpoLista;
	}

	public void setParteCuerpoLista(Set<ParteCuerpo> parteCuerpoLista) {
		this.parteCuerpoLista = parteCuerpoLista;
	}

	public String getParametros() {
		var parametros = StringUtils.EMPTY;
		var parametrosLista = new ArrayList<String>();

		if (patronLista != null && !patronLista.isEmpty()) {
			parametrosLista.add(parametros.concat(patronLista.stream().map(it -> it.getCodigo()).collect(Collectors.joining(","))));
		}

		if (zonaLista != null && !zonaLista.isEmpty()) {
			parametrosLista.add(parametros.concat(zonaLista.stream().map(it -> it.getCodigo()).collect(Collectors.joining(","))));
		}

		if (musculoLista != null && !musculoLista.isEmpty()) {
			parametrosLista.add(parametros.concat(musculoLista.stream().map(it -> it.getCodigo()).collect(Collectors.joining(","))));
		}

		if (parteCuerpoLista != null && !parteCuerpoLista.isEmpty()) {
			parametrosLista.add(parametros.concat(parteCuerpoLista.stream().map(it -> it.getCodigo()).collect(Collectors.joining(","))));
		}

		return parametrosLista.stream().collect(Collectors.joining(","));
	}

	public RutinaFaseGrupoRepeticionEjercicio copiar() {
		var rutinaFaseGrupoRepeticionEjercicioCopia = new RutinaFaseGrupoRepeticionEjercicio();
		rutinaFaseGrupoRepeticionEjercicioCopia.setEjercicio(ejercicio);
		rutinaFaseGrupoRepeticionEjercicioCopia.setPosicion(posicion);
		rutinaFaseGrupoRepeticionEjercicioCopia.setComentarios(comentarios);
		rutinaFaseGrupoRepeticionEjercicioCopia.setRepeticiones(repeticiones);
		rutinaFaseGrupoRepeticionEjercicioCopia.setTiempo(tiempo);

		rutinaFaseGrupoRepeticionEjercicioCopia
				.setPatronLista(patronLista != null ? patronLista.stream().map(it -> new Patron(it.getId())).collect(Collectors.toSet()) : null);
		rutinaFaseGrupoRepeticionEjercicioCopia.setZonaLista(zonaLista != null ? zonaLista.stream().map(it -> new Zona(it.getId())).collect(Collectors.toSet()) : null);
		rutinaFaseGrupoRepeticionEjercicioCopia
				.setMusculoLista(musculoLista != null ? musculoLista.stream().map(it -> new Musculo(it.getId())).collect(Collectors.toSet()) : null);
		rutinaFaseGrupoRepeticionEjercicioCopia
				.setParteCuerpoLista(parteCuerpoLista != null ? parteCuerpoLista.stream().map(it -> new ParteCuerpo(it.getId())).collect(Collectors.toSet()) : null);

		rutinaFaseGrupoRepeticionEjercicioCopia.setEnEjecucion(enEjecucion);

		return rutinaFaseGrupoRepeticionEjercicioCopia;
	}

	public RutinaFaseGrupoRepeticionEjercicio copiarParaCrearPlantilla() {
		var rutinaFaseGrupoRepeticionEjercicioCopia = new RutinaFaseGrupoRepeticionEjercicio();
		rutinaFaseGrupoRepeticionEjercicioCopia.setEjercicio(ejercicio);
		rutinaFaseGrupoRepeticionEjercicioCopia.setPosicion(posicion);
		rutinaFaseGrupoRepeticionEjercicioCopia.setComentarios(comentarios);
		rutinaFaseGrupoRepeticionEjercicioCopia.setRepeticiones(repeticiones);
		rutinaFaseGrupoRepeticionEjercicioCopia.setTiempo(tiempo);

		rutinaFaseGrupoRepeticionEjercicioCopia
				.setPatronLista(patronLista != null ? patronLista.stream().map(it -> new Patron(it.getId())).collect(Collectors.toSet()) : null);
		rutinaFaseGrupoRepeticionEjercicioCopia.setZonaLista(zonaLista != null ? zonaLista.stream().map(it -> new Zona(it.getId())).collect(Collectors.toSet()) : null);
		rutinaFaseGrupoRepeticionEjercicioCopia
				.setMusculoLista(musculoLista != null ? musculoLista.stream().map(it -> new Musculo(it.getId())).collect(Collectors.toSet()) : null);
		rutinaFaseGrupoRepeticionEjercicioCopia
				.setParteCuerpoLista(parteCuerpoLista != null ? parteCuerpoLista.stream().map(it -> new ParteCuerpo(it.getId())).collect(Collectors.toSet()) : null);

		rutinaFaseGrupoRepeticionEjercicioCopia.setEnEjecucion(Boolean.FALSE);

		return rutinaFaseGrupoRepeticionEjercicioCopia;
	}

	public boolean isEjercicioSinOtroCompatible() {
		return ejercicioSinOtroCompatible;
	}

	public void setEjercicioSinOtroCompatible(boolean ejercicioSinOtroCompatible) {
		this.ejercicioSinOtroCompatible = ejercicioSinOtroCompatible;
	}

	public boolean isMarcadoParaReemplazar() {
		return marcadoParaReemplazar;
	}

	public void setMarcadoParaReemplazar(boolean marcadoParaReemplazar) {
		this.marcadoParaReemplazar = marcadoParaReemplazar;
	}

}
