package es.pocketrainer.servicio.parametrizacion;

import java.math.BigDecimal;
import java.util.Optional;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import es.pocketrainer.modelo.parametrizacion.ParametroConfiguracion;
import es.pocketrainer.repositorio.parametrizacion.ParametroConfiguracionRepositorio;

@Service("ParametroConfiguracionServicio")
public class ParametroConfiguracionServiciompl implements ParametroConfiguracionServicio {

	@Resource
	private ParametroConfiguracionRepositorio parametroConfiguracionRepositorio;

	@Override
	public String buscarParametroConfiguracionValor(String codigo) {
		String valor = null;
		final Optional<ParametroConfiguracion> parametroConfiguracion = parametroConfiguracionRepositorio.findById(codigo);

		if (parametroConfiguracion.isPresent()) {
			valor = parametroConfiguracion.get().getValor();
		}
		return valor;
	}

	@Override
	public BigDecimal buscarParametroConfiguracionBigDecimalValor(String codigo) {
		var valorString = buscarParametroConfiguracionValor(codigo);
		return valorString != null ? new BigDecimal(valorString) : null;
	}

	@Override
	public Integer buscarParametroConfiguracionIntegerValor(String codigo) {
		var valorString = buscarParametroConfiguracionValor(codigo);
		return valorString != null ? Integer.valueOf(valorString) : null;
	}

	@Override
	public Boolean buscarParametroConfiguracionBooleanValor(String codigo) {
		var valorString = buscarParametroConfiguracionValor(codigo);
		return valorString != null ? Boolean.valueOf(valorString) : null;
	}

	@Transactional
	@Override
	public void actualizarParametroConfiguracionValor(String codigo, String valor) {
		final ParametroConfiguracion parametroConfiguracion = parametroConfiguracionRepositorio.findById(codigo).get();
		parametroConfiguracion.setValor(valor);
	}

	@Override
	public ParametroConfiguracion buscarParametroConfiguracionPorCodigo(String codigo) {
		return parametroConfiguracionRepositorio.findByCodigo(codigo);
	}

}
