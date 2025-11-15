package es.pocketrainer.repositorio.ejercicio;

import java.util.ArrayList;
import java.util.List;

public class BuscarEjercicioFormulario {

	private Long clienteId;
	private Long rutinaId;

	private String nombreCorto;
	private String nombre;
	private String descripcion;
	private Boolean esUnilateral;
	private Boolean tieneAudio;

	private List<Long> patronIdLista;
	private List<Long> zonaIdLista;
	private List<Long> parteCuerpoIdLista;
	private List<Long> musculoIdLista;
	private List<Long> materialIdLista;
	private List<String> ejercicioUltimoUsoTipoLista;
	private boolean filtrarPorEjercicioUltimoUsoTipo;
	private boolean clasificarEjercicioSegunUsoPrevio;

	private boolean busquedaPorFiltroCompleto;
	private boolean busquedaPorFiltroSoloPorMaterial;

	private boolean fetchMaterial;

	public Long getClienteId() {
		return clienteId;
	}

	public void setClienteId(Long clienteId) {
		this.clienteId = clienteId;
	}

	public Long getRutinaId() {
		return rutinaId;
	}

	public void setRutinaId(Long rutinaId) {
		this.rutinaId = rutinaId;
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

	public Boolean getEsUnilateral() {
		return esUnilateral;
	}

	public void setEsUnilateral(Boolean esUnilateral) {
		this.esUnilateral = esUnilateral;
	}

	public Boolean getTieneAudio() {
		return tieneAudio;
	}

	public void setTieneAudio(Boolean tieneAudio) {
		this.tieneAudio = tieneAudio;
	}

	public List<Long> getPatronIdLista() {
		return patronIdLista;
	}

	public void setPatronIdLista(List<Long> patronIdLista) {
		this.patronIdLista = patronIdLista;
	}

	public List<Long> getZonaIdLista() {
		return zonaIdLista;
	}

	public void setZonaIdLista(List<Long> zonaIdLista) {
		this.zonaIdLista = zonaIdLista;
	}

	public List<Long> getParteCuerpoIdLista() {
		return parteCuerpoIdLista;
	}

	public void setParteCuerpoIdLista(List<Long> parteCuerpoIdLista) {
		this.parteCuerpoIdLista = parteCuerpoIdLista;
	}

	public List<Long> getMusculoIdLista() {
		return musculoIdLista;
	}

	public void setMusculoIdLista(List<Long> musculoIdLista) {
		this.musculoIdLista = musculoIdLista;
	}

	public List<Long> getMaterialIdLista() {
		return materialIdLista;
	}

	public void setMaterialIdLista(List<Long> materialIdLista) {
		this.materialIdLista = materialIdLista;
	}

	public void addPatronId(Long patronId) {
		if (patronIdLista == null) {
			patronIdLista = new ArrayList<>();
		}
		if (patronIdLista.contains(patronId)) {
			patronIdLista.remove(patronId);
		} else {
			patronIdLista.add(patronId);
		}
	}

	public void addZonaId(Long zonaId) {
		if (zonaIdLista == null) {
			zonaIdLista = new ArrayList<>();
		}
		if (zonaIdLista.contains(zonaId)) {
			zonaIdLista.remove(zonaId);
		} else {
			zonaIdLista.add(zonaId);
		}
	}

	public void addMusculoId(Long musculoId) {
		if (musculoIdLista == null) {
			musculoIdLista = new ArrayList<>();
		}
		if (musculoIdLista.contains(musculoId)) {
			musculoIdLista.remove(musculoId);
		} else {
			musculoIdLista.add(musculoId);
		}
	}

	public void addParteCuerpoId(Long parteCuerpoId) {
		if (parteCuerpoIdLista == null) {
			parteCuerpoIdLista = new ArrayList<>();
		}
		if (parteCuerpoIdLista.contains(parteCuerpoId)) {
			parteCuerpoIdLista.remove(parteCuerpoId);
		} else {
			parteCuerpoIdLista.add(parteCuerpoId);
		}
	}

	public void addMaterialId(Long materialId) {
		if (materialIdLista == null) {
			materialIdLista = new ArrayList<>();
		}
		if (materialIdLista.contains(materialId)) {
			materialIdLista.remove(materialId);
		} else {
			materialIdLista.add(materialId);
		}
	}

	public void addEjercicioUltimoUsoTipo(String ejercicioUltimoUsoTipo) {
		if (ejercicioUltimoUsoTipoLista == null) {
			ejercicioUltimoUsoTipoLista = new ArrayList<>();
		}
		if (ejercicioUltimoUsoTipoLista.contains(ejercicioUltimoUsoTipo)) {
			ejercicioUltimoUsoTipoLista.remove(ejercicioUltimoUsoTipo);
		} else {
			ejercicioUltimoUsoTipoLista.add(ejercicioUltimoUsoTipo);
		}
	}

	public List<String> getEjercicioUltimoUsoTipoLista() {
		return ejercicioUltimoUsoTipoLista;
	}

	public void setEjercicioUltimoUsoTipoLista(List<String> ejercicioUltimoUsoTipoLista) {
		this.ejercicioUltimoUsoTipoLista = ejercicioUltimoUsoTipoLista;
	}

	public boolean isFiltrarPorEjercicioUltimoUsoTipo() {
		return filtrarPorEjercicioUltimoUsoTipo;
	}

	public void setFiltrarPorEjercicioUltimoUsoTipo(boolean filtrarPorEjercicioUltimoUsoTipo) {
		this.filtrarPorEjercicioUltimoUsoTipo = filtrarPorEjercicioUltimoUsoTipo;
	}

	public boolean isClasificarEjercicioSegunUsoPrevio() {
		return clasificarEjercicioSegunUsoPrevio;
	}

	public void setClasificarEjercicioSegunUsoPrevio(boolean clasificarEjercicioSegunUsoPrevio) {
		this.clasificarEjercicioSegunUsoPrevio = clasificarEjercicioSegunUsoPrevio;
	}

	public boolean isBusquedaPorFiltroCompleto() {
		return busquedaPorFiltroCompleto;
	}

	public void setBusquedaPorFiltroCompleto(boolean busquedaPorFiltroCompleto) {
		this.busquedaPorFiltroCompleto = busquedaPorFiltroCompleto;
	}

	public boolean isBusquedaPorFiltroSoloPorMaterial() {
		return busquedaPorFiltroSoloPorMaterial;
	}

	public void setBusquedaPorFiltroSoloPorMaterial(boolean busquedaPorFiltroSoloPorMaterial) {
		this.busquedaPorFiltroSoloPorMaterial = busquedaPorFiltroSoloPorMaterial;
	}

	public boolean isFetchMaterial() {
		return fetchMaterial;
	}

	public void setFetchMaterial(boolean fetchMaterial) {
		this.fetchMaterial = fetchMaterial;
	}

}