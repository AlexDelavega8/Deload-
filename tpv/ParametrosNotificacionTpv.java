package es.pocketrainer.util.tpv;

public class ParametrosNotificacionTpv {

	private String numeroPedido;
	private String codigoRespuesta;
	private String tipoOperacion;
	private String codigoAutorizacion;
	private String tarjetaIdentificacionToken;
	private String tarjetaFechaCaducidad;
	private String identificadorTransaccionCof;

	public String getNumeroPedido() {
		return numeroPedido;
	}

	public void setNumeroPedido(String numeroPedido) {
		this.numeroPedido = numeroPedido;
	}

	public String getCodigoRespuesta() {
		return codigoRespuesta;
	}

	public void setCodigoRespuesta(String codigoRespuesta) {
		this.codigoRespuesta = codigoRespuesta;
	}

	public String getTipoOperacion() {
		return tipoOperacion;
	}

	public void setTipoOperacion(String tipoOperacion) {
		this.tipoOperacion = tipoOperacion;
	}

	public String getCodigoAutorizacion() {
		return codigoAutorizacion;
	}

	public void setCodigoAutorizacion(String codigoAutorizacion) {
		this.codigoAutorizacion = codigoAutorizacion;
	}

	public String getTarjetaIdentificacionToken() {
		return tarjetaIdentificacionToken;
	}

	public void setTarjetaIdentificacionToken(String tarjetaIdentificacionToken) {
		this.tarjetaIdentificacionToken = tarjetaIdentificacionToken;
	}

	public String getTarjetaFechaCaducidad() {
		return tarjetaFechaCaducidad;
	}

	public void setTarjetaFechaCaducidad(String tarjetaFechaCaducidad) {
		this.tarjetaFechaCaducidad = tarjetaFechaCaducidad;
	}

	public String getIdentificadorTransaccionCof() {
		return identificadorTransaccionCof;
	}

	public void setIdentificadorTransaccionCof(String identificadorTransaccionCof) {
		this.identificadorTransaccionCof = identificadorTransaccionCof;
	}

	@Override
	public String toString() {
		return "ParametrosNotificacionTpv [" + (numeroPedido != null ? "numeroPedido=" + numeroPedido + ", " : "")
				+ (codigoRespuesta != null ? "codigoRespuesta=" + codigoRespuesta + ", " : "")
				+ (tipoOperacion != null ? "tipoOperacion=" + tipoOperacion + ", " : "")
				+ (codigoAutorizacion != null ? "codigoAutorizacion=" + codigoAutorizacion + ", " : "")
				+ (tarjetaIdentificacionToken != null ? "tarjetaIdentificacionToken=" + tarjetaIdentificacionToken + ", " : "")
				+ (tarjetaFechaCaducidad != null ? "tarjetaFechaCaducidad=" + tarjetaFechaCaducidad + ", " : "")
				+ (identificadorTransaccionCof != null ? "identificadorTransaccionCof=" + identificadorTransaccionCof : "") + "]";
	}

}
