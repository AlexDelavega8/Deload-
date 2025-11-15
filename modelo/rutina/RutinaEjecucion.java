package es.pocketrainer.modelo.rutina;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class RutinaEjecucion {

	private Long rutinaId;

	private Integer porcentajeEjecucion;
	private String faseColor;
	private String rutinaFaseActualInfo;
	private String rutinaFaseGrupoActualInfo;
	private String rutinaFaseGrupoEjercicioActualInfo;
	private String rutinaFaseGrupoEjercicioTotalesInfo;
	private String ejercicioNombre;
	private String ejercicioDescripcion;
	private String comentarios;
	private boolean ejercicioTieneAudio;
	private Long rutinaFaseGrupoEjercicioAnteriorId;
	private Long rutinaFaseGrupoEjercicioSiguienteId;
	private Integer tiempo;
	private Integer repeticiones;

	private Integer tiempoDescansoEntreEjercicios;
	private Integer tiempoDescansoEntreUnilaterales;
	private Integer tiempoEjercicioPorRepeticiones;

	private Integer tiempoDescansoEntreEjerciciosPorDefecto;
	private Integer tiempoDescansoEntreUnilateralesPorDefecto;
	private Integer tiempoEjercicioPorRepeticionesPorDefecto;

	private boolean ejecucionAutomatica;
	private String videoUrl;
	private String audioUrl;
	private String videoPrecargaUrl;
	private String ejercicioPresentacion;
	private boolean finalizado;

	@JsonIgnore
	private Rutina rutina;

	@JsonIgnore
	private RutinaFaseGrupoRepeticionEjercicio rutinaFaseGrupoEjercicioAnterior;

	@JsonIgnore
	private RutinaFaseGrupoRepeticionEjercicio rutinaFaseGrupoEjercicioActual;

	@JsonIgnore
	private RutinaFaseGrupoRepeticionEjercicio rutinaFaseGrupoEjercicioSiguiente;

	public Long getRutinaId() {
		return rutinaId;
	}

	public void setRutinaId(Long rutinaId) {
		this.rutinaId = rutinaId;
	}

	public Integer getPorcentajeEjecucion() {
		return porcentajeEjecucion;
	}

	public void setPorcentajeEjecucion(Integer porcentajeEjecucion) {
		this.porcentajeEjecucion = porcentajeEjecucion;
	}

	public String getFaseColor() {
		return faseColor;
	}

	public void setFaseColor(String faseColor) {
		this.faseColor = faseColor;
	}

	public String getRutinaFaseActualInfo() {
		return rutinaFaseActualInfo;
	}

	public void setRutinaFaseActualInfo(String rutinaFaseActualInfo) {
		this.rutinaFaseActualInfo = rutinaFaseActualInfo;
	}

	public String getRutinaFaseGrupoActualInfo() {
		return rutinaFaseGrupoActualInfo;
	}

	public void setRutinaFaseGrupoActualInfo(String rutinaFaseGrupoActualInfo) {
		this.rutinaFaseGrupoActualInfo = rutinaFaseGrupoActualInfo;
	}

	public String getRutinaFaseGrupoEjercicioActualInfo() {
		return rutinaFaseGrupoEjercicioActualInfo;
	}

	public void setRutinaFaseGrupoEjercicioActualInfo(String rutinaFaseGrupoEjercicioActualInfo) {
		this.rutinaFaseGrupoEjercicioActualInfo = rutinaFaseGrupoEjercicioActualInfo;
	}

	public String getRutinaFaseGrupoEjercicioTotalesInfo() {
		return rutinaFaseGrupoEjercicioTotalesInfo;
	}

	public void setRutinaFaseGrupoEjercicioTotalesInfo(String rutinaFaseGrupoEjercicioTotalesInfo) {
		this.rutinaFaseGrupoEjercicioTotalesInfo = rutinaFaseGrupoEjercicioTotalesInfo;
	}

	public Rutina getRutina() {
		return rutina;
	}

	public void setRutina(Rutina rutina) {
		this.rutina = rutina;
	}

	public RutinaFaseGrupoRepeticionEjercicio getRutinaFaseGrupoEjercicioAnterior() {
		return rutinaFaseGrupoEjercicioAnterior;
	}

	public void setRutinaFaseGrupoEjercicioAnterior(RutinaFaseGrupoRepeticionEjercicio rutinaFaseGrupoEjercicioAnterior) {
		this.rutinaFaseGrupoEjercicioAnterior = rutinaFaseGrupoEjercicioAnterior;
	}

	public RutinaFaseGrupoRepeticionEjercicio getRutinaFaseGrupoEjercicioActual() {
		return rutinaFaseGrupoEjercicioActual;
	}

	public void setRutinaFaseGrupoEjercicioActual(RutinaFaseGrupoRepeticionEjercicio rutinaFaseGrupoEjercicioActual) {
		this.rutinaFaseGrupoEjercicioActual = rutinaFaseGrupoEjercicioActual;
	}

	public RutinaFaseGrupoRepeticionEjercicio getRutinaFaseGrupoEjercicioSiguiente() {
		return rutinaFaseGrupoEjercicioSiguiente;
	}

	public void setRutinaFaseGrupoEjercicioSiguiente(RutinaFaseGrupoRepeticionEjercicio rutinaFaseGrupoEjercicioSiguiente) {
		this.rutinaFaseGrupoEjercicioSiguiente = rutinaFaseGrupoEjercicioSiguiente;
	}

	public String getEjercicioNombre() {
		return ejercicioNombre;
	}

	public void setEjercicioNombre(String ejercicioNombre) {
		this.ejercicioNombre = ejercicioNombre;
	}

	public String getEjercicioDescripcion() {
		return ejercicioDescripcion;
	}

	public void setEjercicioDescripcion(String ejercicioDescripcion) {
		this.ejercicioDescripcion = ejercicioDescripcion;
	}

	public String getComentarios() {
		return comentarios;
	}

	public void setComentarios(String comentarios) {
		this.comentarios = comentarios;
	}

	public boolean isEjercicioTieneAudio() {
		return ejercicioTieneAudio;
	}

	public void setEjercicioTieneAudio(boolean ejercicioTieneAudio) {
		this.ejercicioTieneAudio = ejercicioTieneAudio;
	}

	public Long getRutinaFaseGrupoEjercicioAnteriorId() {
		return rutinaFaseGrupoEjercicioAnteriorId;
	}

	public void setRutinaFaseGrupoEjercicioAnteriorId(Long rutinaFaseGrupoEjercicioAnteriorId) {
		this.rutinaFaseGrupoEjercicioAnteriorId = rutinaFaseGrupoEjercicioAnteriorId;
	}

	public Long getRutinaFaseGrupoEjercicioSiguienteId() {
		return rutinaFaseGrupoEjercicioSiguienteId;
	}

	public void setRutinaFaseGrupoEjercicioSiguienteId(Long rutinaFaseGrupoEjercicioSiguienteId) {
		this.rutinaFaseGrupoEjercicioSiguienteId = rutinaFaseGrupoEjercicioSiguienteId;
	}

	public Integer getTiempo() {
		return tiempo;
	}

	public void setTiempo(Integer tiempo) {
		this.tiempo = tiempo;
	}

	public Integer getRepeticiones() {
		return repeticiones;
	}

	public void setRepeticiones(Integer repeticiones) {
		this.repeticiones = repeticiones;
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

	public Integer getTiempoDescansoEntreEjerciciosPorDefecto() {
		return tiempoDescansoEntreEjerciciosPorDefecto;
	}

	public void setTiempoDescansoEntreEjerciciosPorDefecto(Integer tiempoDescansoEntreEjerciciosPorDefecto) {
		this.tiempoDescansoEntreEjerciciosPorDefecto = tiempoDescansoEntreEjerciciosPorDefecto;
	}

	public Integer getTiempoDescansoEntreUnilateralesPorDefecto() {
		return tiempoDescansoEntreUnilateralesPorDefecto;
	}

	public void setTiempoDescansoEntreUnilateralesPorDefecto(Integer tiempoDescansoEntreUnilateralesPorDefecto) {
		this.tiempoDescansoEntreUnilateralesPorDefecto = tiempoDescansoEntreUnilateralesPorDefecto;
	}

	public Integer getTiempoEjercicioPorRepeticionesPorDefecto() {
		return tiempoEjercicioPorRepeticionesPorDefecto;
	}

	public void setTiempoEjercicioPorRepeticionesPorDefecto(Integer tiempoEjercicioPorRepeticionesPorDefecto) {
		this.tiempoEjercicioPorRepeticionesPorDefecto = tiempoEjercicioPorRepeticionesPorDefecto;
	}

	public void setTiempoEjercicioPorRepeticiones(Integer tiempoEjercicioPorRepeticiones) {
		this.tiempoEjercicioPorRepeticiones = tiempoEjercicioPorRepeticiones;
	}

	public boolean isEjecucionAutomatica() {
		return ejecucionAutomatica;
	}

	public void setEjecucionAutomatica(boolean ejecucionAutomatica) {
		this.ejecucionAutomatica = ejecucionAutomatica;
	}

	public String getVideoUrl() {
		return videoUrl;
	}

	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}

	public String getAudioUrl() {
		return audioUrl;
	}

	public void setAudioUrl(String audioUrl) {
		this.audioUrl = audioUrl;
	}

	public String getVideoPrecargaUrl() {
		return videoPrecargaUrl;
	}

	public void setVideoPrecargaUrl(String videoPrecargaUrl) {
		this.videoPrecargaUrl = videoPrecargaUrl;
	}

	public String getEjercicioPresentacion() {
		return ejercicioPresentacion;
	}

	public void setEjercicioPresentacion(String ejercicioPresentacion) {
		this.ejercicioPresentacion = ejercicioPresentacion;
	}

	public boolean isFinalizado() {
		return finalizado;
	}

	public void setFinalizado(boolean finalizado) {
		this.finalizado = finalizado;
	}

}
