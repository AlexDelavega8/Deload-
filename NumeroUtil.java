package es.pocketrainer.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class NumeroUtil {

	/**
	 * Formato #,###.00
	 * 
	 * @param numeroDecimal el numero a formatear
	 * @return string con numero formateado
	 */
	public static final String formatearDecimalMonedaConComa(BigDecimal numeroDecimal) {
		return new DecimalFormat("#,###.00", new DecimalFormatSymbols(Constantes.LOCALE_POR_DEFECTO)).format(numeroDecimal);
	}

	/**
	 * Formato #,###.00
	 * 
	 * @param numeroDecimal el numero a formatear
	 * @return string con numero formateado
	 */
	public static final String formatearDecimalMonedaConPunto(BigDecimal numeroDecimal) {
		return new DecimalFormat("#,###.00", new DecimalFormatSymbols(Locale.ENGLISH)).format(numeroDecimal);
	}

	/**
	 * Formato #,###.000000
	 * 
	 * @param numeroDecimal el numero a formatear
	 * @return string con numero formateado
	 */
	public static final String formatearDecimalMonedaSeisDecimalesConPunto(BigDecimal numeroDecimal) {
		return new DecimalFormat("#,###.000000", new DecimalFormatSymbols(Locale.ENGLISH)).format(numeroDecimal);
	}
}
