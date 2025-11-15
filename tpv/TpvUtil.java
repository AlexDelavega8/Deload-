package es.pocketrainer.util.tpv;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class TpvUtil {

	public static SolicitudPagoInicial rellenarDatosPagoTPV(Long clienteServicioId, Long clienteServicioCobroId, String cantidad,
			String numeroDePedido, String servicioDescripcion, String titular, String urlNotificacionOnline, String urlPagoTpvOk, String urlPagoTpvKo,
			String transacionTipoAutorizacion, String claveSecreta) {

		SolicitudPagoInicial solicitudPagoInicial = new SolicitudPagoInicial();

		final String codigoComercio = "344799168";
		final String terminal = "001";
		final String moneda = "978";

		urlNotificacionOnline = urlNotificacionOnline + "/" + clienteServicioId + "/" + clienteServicioCobroId;
		urlPagoTpvOk = urlPagoTpvOk + "/" + clienteServicioId + "/" + clienteServicioCobroId;
		urlPagoTpvKo = urlPagoTpvKo + "/" + clienteServicioId + "/" + clienteServicioCobroId;

		final ApiMacSha256 apiMacSha256 = new ApiMacSha256();

		// SIGNATURE VERSION
		solicitudPagoInicial.setDsSignatureVersion("HMAC_SHA256_V1");

		// DATOS
		apiMacSha256.setParameter("DS_MERCHANT_AMOUNT", cantidad);
		apiMacSha256.setParameter("DS_MERCHANT_ORDER", numeroDePedido);
		apiMacSha256.setParameter("DS_MERCHANT_MERCHANTCODE", codigoComercio);
		apiMacSha256.setParameter("DS_MERCHANT_CURRENCY", moneda);
		apiMacSha256.setParameter("DS_MERCHANT_TRANSACTIONTYPE", transacionTipoAutorizacion);
		apiMacSha256.setParameter("DS_MERCHANT_TERMINAL", terminal);
		apiMacSha256.setParameter("DS_MERCHANT_MERCHANTURL", urlNotificacionOnline);
		apiMacSha256.setParameter("DS_MERCHANT_URLOK", urlPagoTpvOk);
		apiMacSha256.setParameter("DS_MERCHANT_URLKO", urlPagoTpvKo);
		apiMacSha256.setParameter("Ds_Merchant_ProductDescription", servicioDescripcion);
		apiMacSha256.setParameter("Ds_Merchant_Titular", titular);

		try {
			solicitudPagoInicial.setDsMerchantParameters(apiMacSha256.createMerchantParameters());
		} catch (final UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		// SIGNATURE
		try {
			solicitudPagoInicial.setDsSignature(apiMacSha256.createMerchantSignature(claveSecreta));
		} catch (InvalidKeyException | UnsupportedEncodingException | NoSuchAlgorithmException | IllegalStateException | NoSuchPaddingException
				| InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
		}

		return solicitudPagoInicial;
	}
}
