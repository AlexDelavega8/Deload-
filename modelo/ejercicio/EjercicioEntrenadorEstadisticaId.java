package es.pocketrainer.modelo.ejercicio;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import es.pocketrainer.modelo.entrenador.Entrenador;

@Embeddable
public class EjercicioEntrenadorEstadisticaId implements Serializable {

	private static final long serialVersionUID = -3037235893698229029L;

	@ManyToOne
	@JoinColumn(name = "ejercicio_id")
	private Ejercicio ejercicio;

	@ManyToOne
	@JoinColumn(name = "entrenador_id")
	private Entrenador entrenador;

	public EjercicioEntrenadorEstadisticaId() {
		super();
	}

	public EjercicioEntrenadorEstadisticaId(Ejercicio ejercicio, Entrenador entrenador) {
		super();
		this.ejercicio = ejercicio;
		this.entrenador = entrenador;
	}

	public Ejercicio getEjercicio() {
		return ejercicio;
	}

	public void setEjercicio(Ejercicio ejercicio) {
		this.ejercicio = ejercicio;
	}

	public Entrenador getEntrenador() {
		return entrenador;
	}

	public void setEntrenador(Entrenador entrenador) {
		this.entrenador = entrenador;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public int hashCode() {
		return Objects.hash(ejercicio, entrenador);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EjercicioEntrenadorEstadisticaId other = (EjercicioEntrenadorEstadisticaId) obj;
		return Objects.equals(ejercicio, other.ejercicio) && Objects.equals(entrenador, other.entrenador);
	}

}
