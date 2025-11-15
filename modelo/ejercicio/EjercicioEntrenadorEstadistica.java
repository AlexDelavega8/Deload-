package es.pocketrainer.modelo.ejercicio;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Transient;

@Entity
public class EjercicioEntrenadorEstadistica {

	@EmbeddedId
	private EjercicioEntrenadorEstadisticaId id;

	private Integer numeroUsosPorRepeticion;
	private Integer numeroUsosPorTiempo;
	private Integer configuracionRepeticionesMasUsado;
	private Integer configuracionTiempoMasUsado;

	@Transient
	private Map<Integer, Integer> configuracionRepeticionesOcurrenciasMapa;

	@Transient
	private Map<Integer, Integer> configuracionTiempoOcurrenciasMapa;

	public EjercicioEntrenadorEstadisticaId getId() {
		return id;
	}

	public void setId(EjercicioEntrenadorEstadisticaId id) {
		this.id = id;
	}

//	public Ejercicio getEjercicio() {
//		return ejercicio;
//	}
//
//	public void setEjercicio(Ejercicio ejercicio) {
//		this.ejercicio = ejercicio;
//	}
//
//	public Entrenador getEntrenador() {
//		return entrenador;
//	}
//
//	public void setEntrenador(Entrenador entrenador) {
//		this.entrenador = entrenador;
//	}

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
