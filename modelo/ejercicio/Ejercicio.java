package es.pocketrainer.modelo.ejercicio;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import es.pocketrainer.util.Constantes.EjercicioUltimoUsoTipoEnum;

@Entity
public class Ejercicio {

	@Id
	@GeneratedValue
	private Long id;
	private String nombreCorto;
	private String nombre;
	private String descripcion;

	@ManyToMany
	@JoinTable(name = "ejercicio_patron", joinColumns = { @JoinColumn(name = "ejercicio_id", referencedColumnName = "id", nullable = false) }, inverseJoinColumns = {
			@JoinColumn(name = "patron_id", referencedColumnName = "id", nullable = false) })
	private Set<Patron> patronLista;

	@ManyToMany
	@JoinTable(name = "ejercicio_zona", joinColumns = { @JoinColumn(name = "ejercicio_id", referencedColumnName = "id", nullable = false) }, inverseJoinColumns = {
			@JoinColumn(name = "zona_id", referencedColumnName = "id", nullable = false) })
	private Set<Zona> zonaLista;

	@OneToMany(mappedBy = "ejercicio", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<EjercicioMusculo> ejercicioMusculoLista;

	@OneToMany(mappedBy = "ejercicio", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<EjercicioParteCuerpo> ejercicioParteCuerpoLista;

	@ManyToMany
	@JoinTable(name = "ejercicio_material", joinColumns = { @JoinColumn(name = "ejercicio_id", referencedColumnName = "id", nullable = false) }, inverseJoinColumns = {
			@JoinColumn(name = "material_id", referencedColumnName = "id", nullable = false) })
	private Set<Material> materialLista;

	private Boolean esUnilateral;
	private Boolean tieneAudio;
	private ZonedDateTime fechaHoraUltimaActualizacion;

	private Integer numeroUsosPorRepeticion;
	private Integer numeroUsosPorTiempo;
	private Integer configuracionRepeticionesMasUsado;
	private Integer configuracionTiempoMasUsado;

	@Transient
	private Map<Integer, Integer> configuracionRepeticionesOcurrenciasMapa;

	@Transient
	private Map<Integer, Integer> configuracionTiempoOcurrenciasMapa;

	@Transient
	private String htmlClass;

	@Transient
	private EjercicioUltimoUsoTipoEnum ejercicioUltimoUsoTipoEnum;

	@Transient
	private boolean clienteNoTieneTodoMaterial;

	@Transient
	private List<Musculo> musculoPrincipalLista;

	@Transient
	private List<Musculo> musculoSecundarioLista;

	@Transient
	private List<ParteCuerpo> parteCuerpoPrincipalLista;

	@Transient
	private List<ParteCuerpo> parteCuerpoSecundarioLista;

	@Transient
	private MultipartFile video;

	@Transient
	private MultipartFile video480;

	@Transient
	private MultipartFile foto;

	@Transient
	private MultipartFile audio;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombreCorto() {
		return nombreCorto;
	}

	public void setNombreCorto(String nombreCorto) {
		this.nombreCorto = nombreCorto;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Set<Patron> getPatronLista() {
		return patronLista;
	}

	public void setPatronLista(Set<Patron> patronLista) {
		this.patronLista = patronLista;
	}

	public void addPatron(Patron patron) {
		if (patronLista == null) {
			patronLista = new HashSet<>();
		}
		patronLista.add(patron);
	}

	public Set<Zona> getZonaLista() {
		return zonaLista;
	}

	public void setZonaLista(Set<Zona> zonaLista) {
		this.zonaLista = zonaLista;
	}

	public void addZona(Zona zona) {
		if (zonaLista == null) {
			zonaLista = new HashSet<>();
		}
		zonaLista.add(zona);
	}

	public Set<EjercicioMusculo> getEjercicioMusculoLista() {
		return ejercicioMusculoLista;
	}

	public void setEjercicioMusculoLista(Set<EjercicioMusculo> ejercicioMusculoLista) {
		this.ejercicioMusculoLista = ejercicioMusculoLista;
	}

	public void addEjercicioMusculo(EjercicioMusculo ejercicioMusculo) {
		if (ejercicioMusculoLista == null) {
			ejercicioMusculoLista = new HashSet<>();
		}
		ejercicioMusculo.setEjercicio(this);
		ejercicioMusculoLista.add(ejercicioMusculo);
	}

	public Set<EjercicioParteCuerpo> getEjercicioParteCuerpoLista() {
		return ejercicioParteCuerpoLista;
	}

	public void setEjercicioParteCuerpoLista(Set<EjercicioParteCuerpo> ejercicioParteCuerpoLista) {
		this.ejercicioParteCuerpoLista = ejercicioParteCuerpoLista;
	}

	public void addEjercicioParteCuerpo(EjercicioParteCuerpo ejercicioParteCuerpo) {
		if (ejercicioParteCuerpoLista == null) {
			ejercicioParteCuerpoLista = new HashSet<>();
		}
		ejercicioParteCuerpo.setEjercicio(this);
		ejercicioParteCuerpoLista.add(ejercicioParteCuerpo);
	}

	public Set<Material> getMaterialLista() {
		return materialLista;
	}

	public void setMaterialLista(Set<Material> materialLista) {
		this.materialLista = materialLista;
	}

	public void addMaterial(Material material) {
		if (materialLista == null) {
			materialLista = new HashSet<>();
		}
		materialLista.add(material);
	}

	public Boolean getTieneAudio() {
		return tieneAudio;
	}

	public void setTieneAudio(Boolean tieneAudio) {
		this.tieneAudio = tieneAudio;
	}

	public Boolean getEsUnilateral() {
		return esUnilateral;
	}

	public void setEsUnilateral(Boolean esUnilateral) {
		this.esUnilateral = esUnilateral;
	}

	public String getRutaFoto() {
		return "/images/foto_" + id + ".png";
	}

	public String getClavesBusqueda() {
		return StringUtils.isNotBlank(nombreCorto) ? StringUtils.stripAccents(nombreCorto).toLowerCase() : "";
	}

	public String getHtmlClass() {
		return htmlClass;
	}

	public void setHtmlClass(String htmlClass) {
		this.htmlClass = htmlClass;
	}

	public EjercicioUltimoUsoTipoEnum getEjercicioUltimoUsoTipoEnum() {
		return ejercicioUltimoUsoTipoEnum;
	}

	public void setEjercicioUltimoUsoTipoEnum(EjercicioUltimoUsoTipoEnum ejercicioUltimoUsoTipoEnum) {
		this.ejercicioUltimoUsoTipoEnum = ejercicioUltimoUsoTipoEnum;
	}

	public boolean isClienteNoTieneTodoMaterial() {
		return clienteNoTieneTodoMaterial;
	}

	public void setClienteNoTieneTodoMaterial(boolean clienteNoTieneTodoMaterial) {
		this.clienteNoTieneTodoMaterial = clienteNoTieneTodoMaterial;
	}

	public List<Musculo> getMusculoPrincipalLista() {
		if (musculoPrincipalLista == null && ejercicioMusculoLista != null) {
			musculoPrincipalLista = ejercicioMusculoLista.parallelStream().filter(it -> it.getPrincipal() != null && it.getPrincipal()).map(it -> it.getMusculo())
					.collect(Collectors.toList());
		}

		return musculoPrincipalLista;
	}

	public void setMusculoPrincipalLista(List<Musculo> musculoPrincipalLista) {
		this.musculoPrincipalLista = musculoPrincipalLista;
	}

	public List<Musculo> getMusculoSecundarioLista() {
		if (musculoSecundarioLista == null && ejercicioMusculoLista != null) {
			musculoSecundarioLista = ejercicioMusculoLista.parallelStream().filter(it -> it.getPrincipal() == null || !it.getPrincipal()).map(it -> it.getMusculo())
					.collect(Collectors.toList());
		}

		return musculoSecundarioLista;
	}

	public void setMusculoSecundarioLista(List<Musculo> musculoSecundarioLista) {
		this.musculoSecundarioLista = musculoSecundarioLista;
	}

	public List<ParteCuerpo> getParteCuerpoPrincipalLista() {
		if (parteCuerpoPrincipalLista == null && ejercicioParteCuerpoLista != null) {
			parteCuerpoPrincipalLista = ejercicioParteCuerpoLista.parallelStream().filter(it -> it.getPrincipal() != null && it.getPrincipal())
					.map(it -> it.getParteCuerpo()).collect(Collectors.toList());
		}

		return parteCuerpoPrincipalLista;
	}

	public void setParteCuerpoPrincipalLista(List<ParteCuerpo> parteCuerpoPrincipalLista) {
		this.parteCuerpoPrincipalLista = parteCuerpoPrincipalLista;
	}

	public List<ParteCuerpo> getParteCuerpoSecundarioLista() {
		if (parteCuerpoSecundarioLista == null && ejercicioParteCuerpoLista != null) {
			parteCuerpoSecundarioLista = ejercicioParteCuerpoLista.parallelStream().filter(it -> it.getPrincipal() == null || !it.getPrincipal())
					.map(it -> it.getParteCuerpo()).collect(Collectors.toList());
		}
		return parteCuerpoSecundarioLista;
	}

	public void setParteCuerpoSecundarioLista(List<ParteCuerpo> parteCuerpoSecundarioLista) {
		this.parteCuerpoSecundarioLista = parteCuerpoSecundarioLista;
	}

	public MultipartFile getVideo() {
		return video;
	}

	public void setVideo(MultipartFile video) {
		this.video = video;
	}

	public MultipartFile getVideo480() {
		return video480;
	}

	public void setVideo480(MultipartFile video480) {
		this.video480 = video480;
	}

	public MultipartFile getFoto() {
		return foto;
	}

	public void setFoto(MultipartFile foto) {
		this.foto = foto;
	}

	public MultipartFile getAudio() {
		return audio;
	}

	public void setAudio(MultipartFile audio) {
		this.audio = audio;
	}

	public ZonedDateTime getFechaHoraUltimaActualizacion() {
		return fechaHoraUltimaActualizacion;
	}

	public void setFechaHoraUltimaActualizacion(ZonedDateTime fechaHoraUltimaActualizacion) {
		this.fechaHoraUltimaActualizacion = fechaHoraUltimaActualizacion;
	}

	public String getRefrescoMultimedia() {
		return fechaHoraUltimaActualizacion.format(DateTimeFormatter.ofPattern("ddMMyyyyHHmmss"));
	}

	public Integer getNumeroUsosPorRepeticion() {
		return numeroUsosPorRepeticion;
	}

	public void setNumeroUsosPorRepeticion(Integer numeroUsosPorRepeticion) {
		this.numeroUsosPorRepeticion = numeroUsosPorRepeticion;
	}

	public Integer getNumeroUsosPorTiempo() {
		return numeroUsosPorTiempo;
	}

	public void setNumeroUsosPorTiempo(Integer numeroUsosPorTiempo) {
		this.numeroUsosPorTiempo = numeroUsosPorTiempo;
	}

	public Integer getConfiguracionRepeticionesMasUsado() {
		return configuracionRepeticionesMasUsado;
	}

	public void setConfiguracionRepeticionesMasUsado(Integer configuracionRepeticionesMasUsado) {
		this.configuracionRepeticionesMasUsado = configuracionRepeticionesMasUsado;
	}

	public Integer getConfiguracionTiempoMasUsado() {
		return configuracionTiempoMasUsado;
	}

	public void setConfiguracionTiempoMasUsado(Integer configuracionTiempoMasUsado) {
		this.configuracionTiempoMasUsado = configuracionTiempoMasUsado;
	}

	public Map<Integer, Integer> getConfiguracionRepeticionesOcurrenciasMapa() {
		return configuracionRepeticionesOcurrenciasMapa;
	}

	public void setConfiguracionRepeticionesOcurrenciasMapa(Map<Integer, Integer> configuracionRepeticionesOcurrenciasMapa) {
		this.configuracionRepeticionesOcurrenciasMapa = configuracionRepeticionesOcurrenciasMapa;
	}

	public void addConfiguracionRepeticionesOcurrencia(Integer repeticiones) {
		if (configuracionRepeticionesOcurrenciasMapa == null) {
			configuracionRepeticionesOcurrenciasMapa = new HashMap<>();
		}

		var ocurrencias = configuracionRepeticionesOcurrenciasMapa.get(repeticiones);
		if (ocurrencias == null) {
			ocurrencias = 0;
		}
		configuracionRepeticionesOcurrenciasMapa.put(repeticiones, ocurrencias + 1);
	}

	public Map<Integer, Integer> getConfiguracionTiempoOcurrenciasMapa() {
		return configuracionTiempoOcurrenciasMapa;
	}

	public void setConfiguracionTiempoOcurrenciasMapa(Map<Integer, Integer> configuracionTiempoOcurrenciasMapa) {
		this.configuracionTiempoOcurrenciasMapa = configuracionTiempoOcurrenciasMapa;
	}

	public void addConfiguracionTiempoOcurrencia(Integer tiempo) {
		if (configuracionTiempoOcurrenciasMapa == null) {
			configuracionTiempoOcurrenciasMapa = new HashMap<>();
		}

		var ocurrencias = configuracionTiempoOcurrenciasMapa.get(tiempo);
		if (ocurrencias == null) {
			ocurrencias = 0;
		}
		configuracionTiempoOcurrenciasMapa.put(tiempo, ocurrencias + 1);
	}
}
