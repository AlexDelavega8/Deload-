package es.pocketrainer.util;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Constantes {

	public static final String CORREO_TECNICO = "afz2010@yahoo.es";
	public static final String CORREO_ADMINISTRADOR = "alex@ezensr.com";
	public static final Long SUSCRIPCION_PRUEBA = 1L;

	public static final Locale LOCALE_POR_DEFECTO = new Locale("es", "ES");
	public static final ZoneId ZONE_POR_DEFECTO = ZoneId.of("Europe/Madrid");

	public static final BigDecimal IVA_DECIMAL = new BigDecimal(0.21D);
	public static final Integer IVA_PORCENTAJE = Integer.valueOf(21);

	public static final String CACHE_VALORES_MAESTROS = "cacheValoresMaestros";
	public static final String CACHE_PARAMETROS_CONFIGURACION = "cacheParametrosConfiguracion";
	public static final String CACHE_MATERIAL = "cacheMaterial";

	public static final String CACHE_EJERCICIO = "cacheEjercicio";
	public static final String CACHE_PATRON = "cachePatron";
	public static final String CACHE_MUSCULO = "cacheMusculo";
	public static final String CACHE_PARTE_CUERPO = "cacheParteCuerpo";
	public static final String CACHE_ULTIMO_USO = "cacheUltimoUso";
	public static final String CACHE_EJERCICIO_ESTADISTICA = "cacheEjercicioEstadistica";

	public static final String FASE_INICIAL_NOMBRE = "activacion";
	public static final String FASE_PRINCIPAL_NOMBRE = "principal";
	public static final String FASE_FINAL_NOMBRE = "final";

	public static final Integer FASE_INICIAL_TIPO = Integer.valueOf(1);
	public static final Integer FASE_PRINCIPAL_TIPO = Integer.valueOf(2);
	public static final Integer FASE_FINAL_TIPO = Integer.valueOf(3);

	public static final String COLOR_CORPORATIVO_AZUL = "#0F4C81";
	public static final String COLOR_CORPORATIVO_CORAL = "#FA7268";

	public static final String COLOR_FASE_INICIAL = "#cccc00"; // Pantone que decia david #ffe900, se gve demasiado claro
	public static final String COLOR_FASE_PRINCIPAL = "#FA7268";
	public static final String COLOR_FASE_FINAL = "#0F4C81";

	public static final String FOTO_POR_DEFECTO = "foto_defecto";

	public static final String NOTIFICACION_ASINCRONA = "asincrona";
	public static final String NOTIFICACION_SINCRONA_OK = "sincrona ok";
	public static final String NOTIFICACION_SINCRONA_KO = "sincronna ko";
	public static final Map<String, String> REDSYS_ERROR_MENSAJE_MAPA = new HashMap<>();

	public static final String PC_TIEMPO_DESCANSO_ENTRE_EJERCICIOS = "tiempo_descanso_entre_ejercicios";
	public static final String PC_TIEMPO_DESCANSO_ENTRE_UNILATERALES = "tiempo_descanso_entre_unilaterales";
	public static final String PC_TIEMPO_EJERCICIO_POR_REPETICIONES = "tiempo_ejercicio_por_repeticiones";

	static {
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0429", "Error en la versión enviada por el comercio en el parámetro Ds_SignatureVersion");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0430", "Error al decodificar el parámetro Ds_MerchantParameters");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0431", "Error del objeto JSON que se envía codificado en el parámetro Ds_MerchantParameters");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0432", "Error FUC del comercio erróneo");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0433", "Error Terminal del comercio erróneo");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0434", "Error ausencia de número de pedido en la operación enviada por el comercio");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0435", "Error en el cálculo de la firma");
		REDSYS_ERROR_MENSAJE_MAPA.put("0101", "Tarjeta Caducada.");
		REDSYS_ERROR_MENSAJE_MAPA.put("0102", "Tarjeta en excepción transitoria o bajo sospecha de fraude.");
		REDSYS_ERROR_MENSAJE_MAPA.put("0104", "Operación no permitida para esa tarjeta o terminal.");
		REDSYS_ERROR_MENSAJE_MAPA.put("0106", "Intentos de PIN excedidos.");
		REDSYS_ERROR_MENSAJE_MAPA.put("0116", "Disponible Insuficiente.");
		REDSYS_ERROR_MENSAJE_MAPA.put("0118", "Tarjeta no Registrada.");
		REDSYS_ERROR_MENSAJE_MAPA.put("0125", "Tarjeta no efectiva.");
		REDSYS_ERROR_MENSAJE_MAPA.put("0129", "Código de seguridad (CVV2/CVC2) incorrecto.");
		REDSYS_ERROR_MENSAJE_MAPA.put("0180", "Tarjeta ajena al servicio.");
		REDSYS_ERROR_MENSAJE_MAPA.put("0184", "Error en la autenticación del titular.");
		REDSYS_ERROR_MENSAJE_MAPA.put("0190", "Denegación sin especificar motivo.");
		REDSYS_ERROR_MENSAJE_MAPA.put("0191", "Fecha de caducidad errónea.");
		REDSYS_ERROR_MENSAJE_MAPA.put("0202", "Tarjeta en excepción transitoria o bajo sospecha de fraude con retirada de tarjeta.");
		REDSYS_ERROR_MENSAJE_MAPA.put("0904", "Comercio no registrado en FUC.");
		REDSYS_ERROR_MENSAJE_MAPA.put("0909", "Error de sistema.");
		REDSYS_ERROR_MENSAJE_MAPA.put("0912", "Emisor no disponible.");
		REDSYS_ERROR_MENSAJE_MAPA.put("0913", "Pedido repetido.");
		REDSYS_ERROR_MENSAJE_MAPA.put("0944", "Sesión Incorrecta.");
		REDSYS_ERROR_MENSAJE_MAPA.put("0950", "Operación de devolución no permitida.");
		REDSYS_ERROR_MENSAJE_MAPA.put("9064", "Número de posiciones de la tarjeta incorrecto.");
		REDSYS_ERROR_MENSAJE_MAPA.put("9078", "No existe método de pago válido para esa tarjeta.");
		REDSYS_ERROR_MENSAJE_MAPA.put("9093", "Tarjeta no existente.");
		REDSYS_ERROR_MENSAJE_MAPA.put("9094", "Rechazo servidores internacionales.");
		REDSYS_ERROR_MENSAJE_MAPA.put("9104", "Comercio con “titular seguro” y titular sin clave de compra segura.");
		REDSYS_ERROR_MENSAJE_MAPA.put("9218", "El comercio no permite op. seguras por entrada /operaciones.");
		REDSYS_ERROR_MENSAJE_MAPA.put("9253", "Tarjeta no cumple el check-digit.");
		REDSYS_ERROR_MENSAJE_MAPA.put("9256", "El comercio no puede realizar preautorizaciones.");
		REDSYS_ERROR_MENSAJE_MAPA.put("9257", "Esta tarjeta no permite operativa de preautorizaciones.");
		REDSYS_ERROR_MENSAJE_MAPA.put("9261", "Operación detenida por superar el control de restricciones en la entrada al SIS.");
		REDSYS_ERROR_MENSAJE_MAPA.put("9912", "Emisor no disponible.");
		REDSYS_ERROR_MENSAJE_MAPA.put("9913", "Error en la confirmación que el comercio envía al TPV Virtual (solo aplicable en la opción de sincronización SOAP).");
		REDSYS_ERROR_MENSAJE_MAPA.put("9914", "Confirmación “KO” del comercio (solo aplicable en la opción de sincronización SOAP).");
		REDSYS_ERROR_MENSAJE_MAPA.put("9915", "A petición del usuario se ha cancelado el pago.");
		REDSYS_ERROR_MENSAJE_MAPA.put("9928", "Anulación de autorización en diferido realizada por el SIS (proceso batch).");
		REDSYS_ERROR_MENSAJE_MAPA.put("9929", "Anulación de autorización en diferido realizada por el comercio.");
		REDSYS_ERROR_MENSAJE_MAPA.put("9997", "Se está procesando otra transacción en SIS con la misma tarjeta.");
		REDSYS_ERROR_MENSAJE_MAPA.put("9998", "Operación en proceso de solicitud de datos de tarjeta.");
		REDSYS_ERROR_MENSAJE_MAPA.put("9999", "Operación que ha sido redirigida al emisor a autenticar.");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0007", "Error al desmontar el XML de entrada.");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0008", "Error falta Ds_Merchant_MerchantCode.");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0009", "Error de formato en Ds_Merchant_MerchantCode.");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0010", "Error falta Ds_Merchant_Terminal.");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0011", "Error de formato en Ds_Merchant_Terminal.");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0014", "Error de formato en Ds_Merchant_Order.");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0015", "Error falta Ds_Merchant_Currency.");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0016", "Error de formato en Ds_Merchant_Currency.");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0017", "Error no se admiten operaciones en pesetas.");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0018", "Error falta Ds_Merchant_Amount.");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0019", "Error de formato en Ds_Merchant_Amount.");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0020", "Error falta Ds_Merchant_MerchantSignature.");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0021", "Error la Ds_Merchant_MerchantSignature viene vacía.");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0022", "Error de formato en Ds_Merchant_TransactionType.");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0023", "Error Ds_Merchant_TransactionType desconocido.");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0024", "Error Ds_Merchant_ConsumerLanguage tiene mas de 3 posiciones.");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0025", "Error de formato en Ds_Merchant_ConsumerLanguage.");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0026", "Error No existe el comercio / terminal enviado.");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0027", "Error Moneda enviada por el comercio es diferente a la que tiene asignada para ese terminal.");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0028", "Error Comercio / terminal está dado de baja.");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0030", "Error en un pago con tarjeta ha llegado un tipo de operación no valido.");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0031", "Método de pago no definido.");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0033", "Error en un pago con móvil ha llegado un tipo de operación que no es ni pago ni preautorización.");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0034", "Error de acceso a la Base de Datos.");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0037", "El número de teléfono no es válido.");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0038", "Error en java.");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0040", "Error el comercio / terminal no tiene ningún método de pago asignado.");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0041", "Error en el cálculo de la firma de datos del comercio.");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0042", "La firma enviada no es correcta.");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0043", "Error al realizar la notificación on-line.");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0046", "El BIN de la tarjeta no está dado de alta.");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0051", "Error número de pedido repetido.");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0054", "Error no existe operación sobre la que realizar la devolución.");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0055", "Error no existe más de un pago con el mismo número de pedido.");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0056", "La operación sobre la que se desea devolver no está autorizada.");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0057", "El importe a devolver supera el permitido.");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0058", "Inconsistencia de datos, en la validación de una confirmación.");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0059", "Error no existe operación sobre la que realizar la devolución.");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0060", "Ya existe una confirmación asociada a la preautorización.");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0061", "La preautorización sobre la que se desea confirmar no está autorizada.");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0062", "El importe a confirmar supera el permitido.");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0063", "Error. Número de tarjeta no disponible.");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0064", "Error. El número de tarjeta no puede tener más de 19 posiciones.");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0065", "Error. El número de tarjeta no es numérico.");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0066", "Error. Mes de caducidad no disponible.");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0067", "Error. El mes de la caducidad no es numérico.");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0068", "Error. El mes de la caducidad no es válido.");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0069", "Error. Año de caducidad no disponible.");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0070", "Error. El Año de la caducidad no es numérico.");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0071", "Tarjeta caducada.");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0072", "Operación no anulable.");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0074", "Error falta Ds_Merchant_Order.");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0075", "Error el Ds_Merchant_Order tiene menos de 4 posiciones o más de 12.");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0076", "Error el Ds_Merchant_Order no tiene las cuatro primeras posiciones numéricas.");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0078", "Método de pago no disponible.");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0079", "Error al realizar el pago con tarjeta.");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0081", "La sesión es nueva, se han perdido los datos almacenados.");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0084", "El valor de Ds_Merchant_Conciliation es nulo.");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0085", "El valor de Ds_Merchant_Conciliation no es numérico.");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0086", "El valor de Ds_Merchant_Conciliation no ocupa 6 posiciones.");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0089", "El valor de Ds_Merchant_ExpiryDate no ocupa 4 posiciones.");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0092", "El valor de Ds_Merchant_ExpiryDate es nulo.");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0093", "Tarjeta no encontrada en la tabla de rangos.");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0094", "La tarjeta no fue autenticada como 3D Secure.");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0097", "Valor del campo Ds_Merchant_CComercio no válido.");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0098", "Valor del campo Ds_Merchant_CVentana no válido.");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0112", "Error. El tipo de transacción especificado en Ds_Merchant_Transaction_Type no esta permitido.");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0113", "Excepción producida en el servlet de operaciones.");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0114", "Error, se ha llamado con un GET en lugar de un POST.");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0115", "Error no existe operación sobre la que realizar el pago de la cuota.");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0116", "La operación sobre la que se desea pagar una cuota no es una operación válida.");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0117", "La operación sobre la que se desea pagar una cuota no está autorizada.");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0118", "Se ha excedido el importe total de las cuotas.");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0119", "Valor del campo Ds_Merchant_DateFrecuency no válido.");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0120", "Valor del campo Ds_Merchant_CargeExpiryDate no válido.");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0121", "Valor del campo Ds_Merchant_SumTotal no válido.");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0122", "Valor del campo Ds_merchant_DateFrecuency o Ds_Merchant_SumTotal tiene formato incorrecto.");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0123", "Se ha excedido la fecha tope para realizar transacciones.");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0124", "No ha transcurrido la frecuencia mínima en un pago recurrente sucesivo.");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0132", "La fecha de Confirmación de Autorización no puede superar en más de 7 días a la de Preautorización.");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0133", "La fecha de Confirmación de Autenticación no puede superar en mas de 45 días a la de Autenticación Previa.");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0139", "Error el pago recurrente inicial está duplicado.");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0142", "Tiempo excedido para el pago.");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0197", "Error al obtener los datos de cesta de la compra en operación tipo pasarela.");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0198", "Error el importe supera el límite permitido para el comercio.");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0199", "Error el número de operaciones supera el límite permitido para el comercio.");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0200", "Error el importe acumulado supera el límite permitido para el comercio.");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0214", "El comercio no admite devoluciones.");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0216", "Error Ds_Merchant_CVV2 tiene mas de 3/4 posiciones.");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0217", "Error de formato en Ds_Merchant_CVV2.");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0218", "El comercio no permite operaciones seguras por la entrada /operaciones.");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0219", "Error el número de operaciones de la tarjeta supera el límite permitido para el comercio.");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0220", "Error el importe acumulado de la tarjeta supera el límite permitido para el comercio.");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0221", "Error el CVV2 es obligatorio.");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0222", "Ya existe una anulación asociada a la preautorización.");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0223", "La preautorización que se desea anular no está autorizada.");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0224", "El comercio no permite anulaciones por no tener firma ampliada.");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0225", "Error no existe operación sobre la que realizar la anulación.");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0226", "Inconsistencia de datos, en la validación de una anulación.");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0227", "Valor del campo Ds_Merchan_TransactionDate no válido.");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0229", "No existe el código de pago aplazado solicitado.");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0252", "El comercio no permite el envío de tarjeta.");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0253", "La tarjeta no cumple el check-digit.");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0254", "El número de operaciones de la IP supera el límite permitido por el comercio.");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0255", "El importe acumulado por la IP supera el límite permitido por el comercio.");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0256", "El comercio no puede realizar preautorizaciones.");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0257", "Esta tarjeta no permite operativa de preautorizaciones.");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0258", "Inconsistencia de datos, en la validación de una confirmación.");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0261", "Operación detenida por superar el control de restricciones en la entrada al SIS.");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0270", "El comercio no puede realizar autorizaciones en diferido.");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0274", "Tipo de operación desconocida o no permitida por esta entrada al SIS.");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0298", "El comercio no permite realizar operaciones de Tarjeta en Archivo.");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0319", "El comercio no pertenece al grupo especificado en Ds_Merchant_Group.");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0321", "La referencia indicada en Ds_Merchant_Identifier no está asociada al comercio.");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0322", "Error de formato en Ds_Merchant_Group.");
		REDSYS_ERROR_MENSAJE_MAPA.put("SIS0325", "Se ha pedido no mostrar pantallas pero no se ha enviado ninguna referencia de tarjeta.");
	}

	/**
	 * Tiempo de espera por precacuion en proceso de renovacion para evitar
	 * problemas de envio muy seguido de mails
	 */
	public static final int TIEMPO_ESPERA_RENOVACIONES = 5000;

	public enum ValorMaestroTipoEnum {

		CLIENTE_ESTADO("CLIENTEESTADO00"), ENTRENADOR_ESTADO("ENTRENADESTADO0"), SUSCRIPCION_ESTADDO("SUSCRIPESTADO00"), PERFIL("CLIENTEPERFIL00"),
		DIA_ENTRENAMIENTO("DIAENTRENO00000"), LUGAR_ENTRENAMIENTO("LUGARENTRENO000"), MATERIAL("MATERIAL0000000"), MEDIO_CONOCIMIENTO("MEDIOCONOCI0000"),
		SEXO("PERSONASEXO0000"), PARQ("PREGUNTAPARQ000"), RUTINA_ESTADO("RUTINAESTADO000"), SUSCRIPCION_COBRO_ESTADO("SUCOBROESTADO00");

		private final String codigo;

		private ValorMaestroTipoEnum(final String codigo) {
			this.codigo = codigo;
		}

		public String codigo() {
			return codigo;
		}

		public static ValorMaestroTipoEnum getEnum(String codigo) {
			return List.of(ValorMaestroTipoEnum.values()).stream().filter((it) -> it.codigo().equals(codigo)).findFirst().orElse(null);
		}

	}

	public enum RolEnum {

		CLIENTE("ROLE_CLIENTE"), ENTRENADOR("ROLE_ENTRENADOR");

		private final String codigo;

		private RolEnum(final String codigo) {
			this.codigo = codigo;
		}

		public String codigo() {
			return codigo;
		}

		public static RolEnum getEnum(String codigo) {
			return List.of(RolEnum.values()).stream().filter((it) -> it.codigo().equals(codigo)).findFirst().orElse(null);
		}

	}

	public enum VideoconferenciaEstadoEnum {

		SIN_PROGRAMAR("VIDEOESTADO0001"), PROGRAMADA("VIDEOESTADO0002"), EN_CURSO("VIDEOESTADO0003"), REALIZADA("VIDEOESTADO0004"), CADUCADA("VIDEOESTADO0005");

		private final String codigo;

		private VideoconferenciaEstadoEnum(final String codigo) {
			this.codigo = codigo;
		}

		public String codigo() {
			return codigo;
		}

		public static VideoconferenciaEstadoEnum getEnum(String codigo) {
			return List.of(VideoconferenciaEstadoEnum.values()).stream().filter((it) -> it.codigo().equals(codigo)).findFirst().orElse(null);
		}
	}

	public enum RutinaEstadoEnum {

		EN_ELABORACION("RUTINAESTADO001"), ACTIVA_NUEVA("RUTINAESTADO002"), ACTIVA("RUTINAESTADO003"), PASADA("RUTINAESTADO004");

		private final String codigo;

		private RutinaEstadoEnum(final String codigo) {
			this.codigo = codigo;
		}

		public String codigo() {
			return codigo;
		}

		public static RutinaEstadoEnum getEnum(String codigo) {
			return List.of(RutinaEstadoEnum.values()).stream().filter((it) -> it.codigo().equals(codigo)).findFirst().orElse(null);
		}
	}

	public enum RutinaTipoEnum {

		DE_CLIENTE("RUTINATIPO00001"), PLANTILLA("RUTINATIPO00002");

		private final String codigo;

		private RutinaTipoEnum(final String codigo) {
			this.codigo = codigo;
		}

		public String codigo() {
			return codigo;
		}

		public static RutinaTipoEnum getEnum(String codigo) {
			return List.of(RutinaTipoEnum.values()).stream().filter((it) -> it.codigo().equals(codigo)).findFirst().orElse(null);
		}
	}

	public enum SuscripcionEstadoEnum {

		IMPAGADA_INICIAL("SUSCRIPESTADO01"), ACTIVA_NUEVA("SUSCRIPESTADO02"), ACTIVA("SUSCRIPESTADO03"), CANCELADA("SUSCRIPESTADO04"), IMPAGADA("SUSCRIPESTADO05"),
		CADUCADA("SUSCRIPESTADO06");

		private final String codigo;

		private SuscripcionEstadoEnum(final String codigo) {
			this.codigo = codigo;
		}

		public String codigo() {
			return codigo;
		}

		public static RutinaEstadoEnum getEnum(String codigo) {
			return List.of(RutinaEstadoEnum.values()).stream().filter((it) -> it.codigo().equals(codigo)).findFirst().orElse(null);
		}
	}

	public enum SuscripcionCobroEstadoEnum {

		EMITIDO("SUCOBROESTADO01"), COBRADO("SUCOBROESTADO02"), RECHAZADO("SUCOBROESTADO03"), PENDIENTE("SUCOBROESTADO04");

		private final String codigo;

		private SuscripcionCobroEstadoEnum(final String codigo) {
			this.codigo = codigo;
		}

		public String codigo() {
			return codigo;
		}

		public static SuscripcionCobroEstadoEnum getEnum(String codigo) {
			return List.of(SuscripcionCobroEstadoEnum.values()).stream().filter((it) -> it.codigo().equals(codigo)).findFirst().orElse(null);
		}
	}

	public enum ClienteEstadoEnum {

		ACTIVO("CLIENTEESTADO01"), PENDIENTE_BAJA("CLIENTEESTADO02"), BAJA("CLIENTEESTADO03");

		private final String codigo;

		private ClienteEstadoEnum(final String codigo) {
			this.codigo = codigo;
		}

		public String codigo() {
			return codigo;
		}

		public static ClienteEstadoEnum getEnum(String codigo) {
			return List.of(ClienteEstadoEnum.values()).stream().filter((it) -> it.codigo().equals(codigo)).findFirst().orElse(null);
		}
	}

	public enum EntrenadorEstadoEnum {

		ACTIVO("ENTRENADESTADO1"), BAJA("ENTRENADESTADO2");

		private final String codigo;

		private EntrenadorEstadoEnum(final String codigo) {
			this.codigo = codigo;
		}

		public String codigo() {
			return codigo;
		}

		public static EntrenadorEstadoEnum getEnum(String codigo) {
			return List.of(EntrenadorEstadoEnum.values()).stream().filter((it) -> it.codigo().equals(codigo)).findFirst().orElse(null);
		}
	}

	public enum SexoEnum {

		HOMBRE("CLIENTEESTADO01"), MUJER("CLIENTEESTADO02"), OTRO("CLIENTEESTADO03");

		private final String codigo;

		private SexoEnum(final String codigo) {
			this.codigo = codigo;
		}

		public String codigo() {
			return codigo;
		}

		public static SexoEnum getEnum(String codigo) {
			return List.of(SexoEnum.values()).stream().filter((it) -> it.codigo().equals(codigo)).findFirst().orElse(null);
		}
	}

	public enum EjercicioUltimoUsoTipoEnum {

		EJERCICIO_USO_ACTUAL("Actuales", "ejercicio-uso-actual"), EJERCICIO_USO_UN_MES("1 mes", "ejercicio-uso-un-mes"),
		EJERCICIO_USO_DOS_MESES("2 meses", "ejercicio-uso-dos-meses"), EJERCICIO_USO_TRES_MESES("3 meses", "ejercicio-uso-tres-meses"),
		EJERCICIO_USO_CUATRO_DOCE_MESES("4 - 12 meses", "ejercicio-uso-cuatro-doce-meses");

		private final String codigo;
		private final String cssClass;

		private EjercicioUltimoUsoTipoEnum(final String codigo, final String cssClass) {
			this.codigo = codigo;
			this.cssClass = cssClass;
		}

		public String codigo() {
			return codigo;
		}

		public String getCssClass() {
			return cssClass;
		}

		public static EjercicioUltimoUsoTipoEnum getEnum(String codigo) {
			return List.of(EjercicioUltimoUsoTipoEnum.values()).stream().filter((it) -> it.codigo().equals(codigo)).findFirst().orElse(null);
		}
//
//		public static List<String> getRepresentacionLista() {
//			return List.of(EjercicioUltimoUsoTipoEnum.values()).stream().map(it -> Pair.of(it.codigo).collect(Collectors.toList());
//		}
	}

}
