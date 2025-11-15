package es.pocketrainer.util.tpv;

public class SolicitudPagoRenovacion {

	private String urlPago;
	private String Ds_SignatureVersion;
	private String Ds_MerchantParameters;
	private String Ds_Signature;

	public String getUrlPago() {
		return urlPago;
	}

	public void setUrlPago(String urlPago) {
		this.urlPago = urlPago;
	}

	public String getDs_SignatureVersion() {
		return Ds_SignatureVersion;
	}

	public void setDs_SignatureVersion(String ds_SignatureVersion) {
		Ds_SignatureVersion = ds_SignatureVersion;
	}

	public String getDs_MerchantParameters() {
		return Ds_MerchantParameters;
	}

	public void setDs_MerchantParameters(String ds_MerchantParameters) {
		Ds_MerchantParameters = ds_MerchantParameters;
	}

	public String getDs_Signature() {
		return Ds_Signature;
	}

	public void setDs_Signature(String ds_Signature) {
		Ds_Signature = ds_Signature;
	}

	@Override
	public String toString() {
		return "SolicitudPagoRenovacion [" + (Ds_SignatureVersion != null ? "Ds_SignatureVersion=" + Ds_SignatureVersion + ", " : "")
				+ (Ds_MerchantParameters != null ? "Ds_MerchantParameters=" + Ds_MerchantParameters + ", " : "")
				+ (Ds_Signature != null ? "Ds_Signature=" + Ds_Signature : "") + "]";
	}

}
