package es.pocketrainer.modelo.parametrizacion;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ParametroConfiguracion {

	private String codigo;
	private String valor;

	@Id
	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

}
