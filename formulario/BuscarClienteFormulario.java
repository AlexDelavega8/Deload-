package es.pocketrainer.formulario;

import java.util.List;

public class BuscarClienteFormulario {

	private String nombre;
	private Long entrenadorId;
	private List<String> estadoCodigoLista;
	private List<String> suscripcionEstadoCodigoLista;

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Long getEntrenadorId() {
		return entrenadorId;
	}

	public void setEntrenadorId(Long entrenadorId) {
		this.entrenadorId = entrenadorId;
	}

	public List<String> getEstadoCodigoLista() {
		return estadoCodigoLista;
	}

	public void setEstadoCodigoLista(List<String> estadoCodigoLista) {
		this.estadoCodigoLista = estadoCodigoLista;
	}

	public List<String> getSuscripcionEstadoCodigoLista() {
		return suscripcionEstadoCodigoLista;
	}

	public void setSuscripcionEstadoCodigoLista(List<String> suscripcionEstadoCodigoLista) {
		this.suscripcionEstadoCodigoLista = suscripcionEstadoCodigoLista;
	}

}
