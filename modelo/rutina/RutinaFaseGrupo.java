package es.pocketrainer.modelo.rutina;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Transient;

import es.pocketrainer.modelo.ejercicio.Ejercicio;

@Entity
public class RutinaFaseGrupo {

	@Id
	@GeneratedValue
	private Long id;
	@ManyToOne
	@JoinColumn(name = "rutina_fase_id")
	private RutinaFase rutinaFase;
	private Integer posicion;
	private Integer repeticiones;
	private String comentarios;
	@OneToMany(mappedBy = "rutinaFaseGrupo", cascade = CascadeType.ALL, orphanRemoval = true)
	@OrderBy("numeroRepeticion asc")
	private List<RutinaFaseGrupoRepeticion> rutinaFaseGrupoRepeticionLista;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public RutinaFase getRutinaFase() {
		return rutinaFase;
	}

	public void setRutinaFase(RutinaFase rutinaFase) {
		this.rutinaFase = rutinaFase;
	}

	public Integer getPosicion() {
		return posicion;
	}

	public void setPosicion(Integer posicion) {
		this.posicion = posicion;
	}

	public Integer getRepeticiones() {
		return repeticiones;
	}

	public void setRepeticiones(Integer repeticiones) {
		this.repeticiones = repeticiones;
	}

	public String getComentarios() {
		return comentarios;
	}

	public void setComentarios(String comentarios) {
		this.comentarios = comentarios;
	}

	public List<RutinaFaseGrupoRepeticion> getRutinaFaseGrupoRepeticionLista() {
		return rutinaFaseGrupoRepeticionLista;
	}

	public void setRutinaFaseGrupoRepeticionLista(List<RutinaFaseGrupoRepeticion> rutinaFaseGrupoRepeticionLista) {
		this.rutinaFaseGrupoRepeticionLista = rutinaFaseGrupoRepeticionLista;
	}

	public void addRutinaFaseGrupoRepeticion(RutinaFaseGrupoRepeticion rutinaFaseGrupoRepeticion) {
		if (rutinaFaseGrupoRepeticionLista == null) {
			rutinaFaseGrupoRepeticionLista = new ArrayList<>();
		}

		rutinaFaseGrupoRepeticion.setRutinaFaseGrupo(this);
		rutinaFaseGrupoRepeticionLista.add(rutinaFaseGrupoRepeticion);
	}

	public void addRutinaFaseGrupoRepeticionEstableciendoDatos(RutinaFaseGrupoRepeticion rutinaFaseGrupoRepeticion) {
		if (rutinaFaseGrupoRepeticionLista == null) {
			rutinaFaseGrupoRepeticionLista = new ArrayList<>();
		} else {
			for (RutinaFaseGrupoRepeticionEjercicio rutinaFaseGrupoRepeticionEjercicio : getRutinaFaseGrupoEjercicioLista()) {
				rutinaFaseGrupoRepeticion.addRutinaFaseGrupoRepeticionEjercicio(rutinaFaseGrupoRepeticionEjercicio.copiar());
			}
		}
		rutinaFaseGrupoRepeticion.setNumeroRepeticion(rutinaFaseGrupoRepeticionLista.size() + 1);

		rutinaFaseGrupoRepeticion.setRutinaFaseGrupo(this);
		rutinaFaseGrupoRepeticionLista.add(rutinaFaseGrupoRepeticion);
	}

	public void addRutinaFaseGrupoEjercicio(RutinaFaseGrupoRepeticionEjercicio rutinaFaseGrupoEjercicio) {
		int repeticion = 0;
		for (RutinaFaseGrupoRepeticion rutinaFaseGrupoRepeticion : rutinaFaseGrupoRepeticionLista) {
			if (repeticion == 0) {
				rutinaFaseGrupoRepeticion.addRutinaFaseGrupoRepeticionEjercicio(rutinaFaseGrupoEjercicio);
			} else {
				rutinaFaseGrupoRepeticion.addRutinaFaseGrupoRepeticionEjercicio(rutinaFaseGrupoEjercicio.copiar());
			}

			repeticion++;
		}
	}

	public void removeRutinaFaseGrupoEjercicio(RutinaFaseGrupoRepeticionEjercicio rutinaFaseGrupoRepeticionEjercicio) {
		int repeticion = 0;
		for (RutinaFaseGrupoRepeticion rutinaFaseGrupoRepeticion : rutinaFaseGrupoRepeticionLista) {
			if (repeticion == 0) {
				rutinaFaseGrupoRepeticion.removeRutinaFaseGrupoRepeticionEjercicio(rutinaFaseGrupoRepeticionEjercicio);
			} else {
				for (RutinaFaseGrupoRepeticionEjercicio rutinaFaseGrupoEjercicioRepeticion : rutinaFaseGrupoRepeticion.getRutinaFaseGrupoRepeticionEjercicioLista()) {
					if (rutinaFaseGrupoEjercicioRepeticion.getEjercicio().getId().equals(rutinaFaseGrupoRepeticionEjercicio.getEjercicio().getId())) {
						rutinaFaseGrupoRepeticion.removeRutinaFaseGrupoRepeticionEjercicio(rutinaFaseGrupoEjercicioRepeticion);
						break;
					}
				}
			}

			repeticion++;
		}
	}

	@Transient
	public List<RutinaFaseGrupoRepeticionEjercicio> getRutinaFaseGrupoEjercicioLista() {
		return rutinaFaseGrupoRepeticionLista.get(0).getRutinaFaseGrupoRepeticionEjercicioLista();
	}

	@Transient
	public List<RutinaFaseGrupoRepeticionEjercicio> getRutinaFaseGrupoEjercicioConRepeticionesLista() {
		var rutinaFaseGrupoEjercicioConRepeticionesLista = new ArrayList<RutinaFaseGrupoRepeticionEjercicio>();

		for (RutinaFaseGrupoRepeticion rutinaFaseGrupoRepeticion : rutinaFaseGrupoRepeticionLista) {
			rutinaFaseGrupoEjercicioConRepeticionesLista.addAll(rutinaFaseGrupoRepeticion.getRutinaFaseGrupoRepeticionEjercicioLista());
		}

		return rutinaFaseGrupoEjercicioConRepeticionesLista;
	}

	public RutinaFaseGrupo copiarParaCrearPlantilla() {
		var rutinaFaseGrupoCopia = new RutinaFaseGrupo();
		rutinaFaseGrupoCopia.setPosicion(this.getPosicion());
		rutinaFaseGrupoCopia.setRepeticiones(this.getRepeticiones());

		for (RutinaFaseGrupoRepeticion rutinaFaseGrupoRepeticion : this.getRutinaFaseGrupoRepeticionLista()) {
			rutinaFaseGrupoCopia.addRutinaFaseGrupoRepeticion(rutinaFaseGrupoRepeticion.copiarParaCrearPlantilla());
		}

		return rutinaFaseGrupoCopia;
	}

	public List<Ejercicio> getEjercicioTodos() {
		return getRutinaFaseGrupoEjercicioConRepeticionesLista().stream().map(it -> it.getEjercicio()).distinct().collect(Collectors.toList());
	}

}
