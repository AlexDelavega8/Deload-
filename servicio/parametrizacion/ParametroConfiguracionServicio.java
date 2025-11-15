package es.pocketrainer.servicio.parametrizacion;

import java.math.BigDecimal;

import es.pocketrainer.modelo.parametrizacion.ParametroConfiguracion;

public interface ParametroConfiguracionServicio {

	String buscarParametroConfiguracionValor(String codigo);

	BigDecimal buscarParametroConfiguracionBigDecimalValor(String codigo);

	Integer buscarParametroConfiguracionIntegerValor(String codigo);

	void actualizarParametroConfiguracionValor(String codigo, String valor);

	ParametroConfiguracion buscarParametroConfiguracionPorCodigo(String codigo);

	Boolean buscarParametroConfiguracionBooleanValor(String codigo);

}
