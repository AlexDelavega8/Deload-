package es.pocketrainer.util.tpv;

public class SolicitudPagoInicial {

	private String urlPago;
	private String dsSignatureVersion;
	private String dsMerchantParameters;
	private String dsSignature;

	public String getUrlPago() {
		return urlPago;
	}

	public void setUrlPago(String urlPago) {
		this.urlPago = urlPago;
	}

	public String getDsSignatureVersion() {
		return dsSignatureVersion;
	}

	public void setDsSignatureVersion(String dsSignatureVersion) {
		this.dsSignatureVersion = dsSignatureVersion;
	}

	public String getDsMerchantParameters() {
		return dsMerchantParameters;
	}

	public void setDsMerchantParameters(String dsMerchantParameters) {
		this.dsMerchantParameters = dsMerchantParameters;
	}

	public String getDsSignature() {
		return dsSignature;
	}

	public void setDsSignature(String dsSignature) {
		this.dsSignature = dsSignature;
	}

}
