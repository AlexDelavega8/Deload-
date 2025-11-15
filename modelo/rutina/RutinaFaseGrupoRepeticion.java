package es.pocketrainer.modelo.rutina;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

@Entity
public class RutinaFaseGrupoRepeticion {

	@Id
	@GeneratedValue
	private Long id;
	@ManyToOne
	@JoinColumn(name = "rutina_fase_grupo_id")
	private RutinaFaseGrupo rutinaFaseGrupo;
	private Integer numeroRepeticion;
	@OneToMany(mappedBy = "rutinaFaseGrupoRepeticion", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	@OrderBy("posicion asc")
	private List<RutinaFaseGrupoRepeticionEjercicio> rutinaFaseGrupoRepeticionEjercicioLista;

	public RutinaFaseGrupoRepeticion() {
		super();
	}

	public RutinaFaseGrupoRepeticion(Integer numeroRepeticion) {
		super();
		this.numeroRepeticion = numeroRepeticion;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public RutinaFaseGrupo getRutinaFaseGrupo() {
		return rutinaFaseGrupo;
	}

	public void setRutinaFaseGrupo(RutinaFaseGrupo rutinaFaseGrupo) {
		this.rutinaFaseGrupo = rutinaFaseGrupo;
	}

	public Integer getNumeroRepeticion() {
		return numeroRepeticion;
	}

	public void setNumeroRepeticion(Integer numeroRepeticion) {
		this.numeroRepeticion = numeroRepeticion;
	}

	public List<RutinaFaseGrupoRepeticionEjercicio> getRutinaFaseGrupoRepeticionEjercicioLista() {
		return rutinaFaseGrupoRepeticionEjercicioLista;
	}

	public void setRutinaFaseGrupoRepeticionEjercicioLista(List<RutinaFaseGrupoRepeticionEjercicio> rutinaFaseGrupoRepeticionEjercicio) {
		this.rutinaFaseGrupoRepeticionEjercicioLista = rutinaFaseGrupoRepeticionEjercicio;
	}

	public void addRutinaFaseGrupoRepeticionEjercicio(RutinaFaseGrupoRepeticionEjercicio rutinaFaseGrupoRepeticionEjercicio) {
		if (rutinaFaseGrupoRepeticionEjercicioLista == null) {
			rutinaFaseGrupoRepeticionEjercicioLista = new ArrayList<>();
		}
		rutinaFaseGrupoRepeticionEjercicio.setRutinaFaseGrupoRepeticion(this);
		rutinaFaseGrupoRepeticionEjercicioLista.add(rutinaFaseGrupoRepeticionEjercicio);
	}

	public void removeRutinaFaseGrupoRepeticionEjercicio(RutinaFaseGrupoRepeticionEjercicio rutinaFaseGrupoRepeticionEjercicio) {
		rutinaFaseGrupoRepeticionEjercicioLista.remove(rutinaFaseGrupoRepeticionEjercicio);
		// Establecer posiciones correctas
		int posicion = 1;
		for (RutinaFaseGrupoRepeticionEjercicio rutinaFaseGrupoRepeticionEjercicioReordenar : rutinaFaseGrupoRepeticionEjercicioLista) {
			rutinaFaseGrupoRepeticionEjercicioReordenar.setPosicion(posicion);
			posicion++;
		}

	}

	public RutinaFaseGrupoRepeticion copiarParaCrearPlantilla() {
		var rutinaFasegrupoRepeticionCopia = new RutinaFaseGrupoRepeticion();
		rutinaFasegrupoRepeticionCopia.setNumeroRepeticion(this.getNumeroRepeticion());

		for (RutinaFaseGrupoRepeticionEjercicio rutinaFaseGrupoRepeticionEjercicio : this.getRutinaFaseGrupoRepeticionEjercicioLista()) {
			rutinaFasegrupoRepeticionCopia.addRutinaFaseGrupoRepeticionEjercicio(rutinaFaseGrupoRepeticionEjercicio.copiarParaCrearPlantilla());
		}

		return rutinaFasegrupoRepeticionCopia;
	}

}
