package es.pocketrainer.util.tpv;

import com.fasterxml.jackson.databind.JsonNode;

public class RespuestaPagoTpv {

	private String errorCode;
	private String Ds_SignatureVersion;
	private String Ds_MerchantParameters;
	private String Ds_Signature;

	public RespuestaPagoTpv(JsonNode respuestaPagoTpvJson) {
		this.errorCode = respuestaPagoTpvJson.get("errorCode") != null ?respuestaPagoTpvJson.get("errorCode").asText() : null;
		this.Ds_SignatureVersion = respuestaPagoTpvJson.get("Ds_SignatureVersion") != null ? respuestaPagoTpvJson.get("Ds_SignatureVersion").asText() : null;
		this.Ds_MerchantParameters = respuestaPagoTpvJson.get("Ds_MerchantParameters") != null ?respuestaPagoTpvJson.get("Ds_MerchantParameters").asText() : null;
		this.Ds_Signature = respuestaPagoTpvJson.get("Ds_Signature") != null ?respuestaPagoTpvJson.get("Ds_Signature").asText() : null;
	}

	public RespuestaPagoTpv() {
		super();
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
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
		return "RespuestaPagoTpv [" + (Ds_SignatureVersion != null ? "Ds_SignatureVersion=" + Ds_SignatureVersion + ", " : "")
				+ (Ds_MerchantParameters != null ? "Ds_MerchantParameters=" + Ds_MerchantParameters + ", " : "")
				+ (Ds_Signature != null ? "Ds_Signature=" + Ds_Signature : "") + "]";
	}

}
