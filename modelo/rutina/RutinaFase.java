package es.pocketrainer.modelo.rutina;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import es.pocketrainer.modelo.ejercicio.Ejercicio;

@Entity
public class RutinaFase {

	@Id
	@GeneratedValue
	private Long id;
	private String nombre;
	private Integer tipo;

	@OneToMany(mappedBy = "rutinaFase", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	@OrderBy("posicion asc")
	private List<RutinaFaseGrupo> rutinaFaseGrupoLista;

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

	public Integer getTipo() {
		return tipo;
	}

	public void setTipo(Integer tipo) {
		this.tipo = tipo;
	}

	public List<RutinaFaseGrupo> getRutinaFaseGrupoLista() {
		return rutinaFaseGrupoLista;
	}

	public void setRutinaFaseGrupoLista(List<RutinaFaseGrupo> rutinaFaseGrupoLista) {
		this.rutinaFaseGrupoLista = rutinaFaseGrupoLista;
	}

	public void addRutinaFaseGrupo(RutinaFaseGrupo rutinaFaseGrupo) {
		if (rutinaFaseGrupoLista == null) {
			rutinaFaseGrupoLista = new ArrayList<>();
		}
		rutinaFaseGrupo.setRutinaFase(this);
		rutinaFaseGrupoLista.add(rutinaFaseGrupo);
	}

	public void removeRutinaFaseGrupo(RutinaFaseGrupo rutinaFaseGrupo) {
		rutinaFaseGrupoLista.remove(rutinaFaseGrupo);
	}

	public RutinaFase copiarParaCrearPlantilla() {
		var rutinaFaseCopia = new RutinaFase();
		rutinaFaseCopia.setNombre(this.getNombre());
		rutinaFaseCopia.setTipo(this.getTipo());
		for (RutinaFaseGrupo rutinaFaseGrupo : this.rutinaFaseGrupoLista) {
			rutinaFaseCopia.addRutinaFaseGrupo(rutinaFaseGrupo.copiarParaCrearPlantilla());
		}
		return rutinaFaseCopia;
	}

	public List<RutinaFaseGrupoRepeticionEjercicio> getRutinaFaseGrupoRepeticionEjercicioTodos() {
		var rutinaFaseGrupoEjercicioTodosLista = new ArrayList<RutinaFaseGrupoRepeticionEjercicio>();
		getRutinaFaseGrupoLista().stream().forEach(it -> rutinaFaseGrupoEjercicioTodosLista.addAll(it.getRutinaFaseGrupoEjercicioConRepeticionesLista()));
		return rutinaFaseGrupoEjercicioTodosLista;
	}

	public List<Ejercicio> getEjercicioTodos() {
		return getRutinaFaseGrupoLista().stream().flatMap(it -> it.getRutinaFaseGrupoEjercicioConRepeticionesLista().stream()).map(it -> it.getEjercicio()).distinct()
				.collect(Collectors.toList());
	}

}
