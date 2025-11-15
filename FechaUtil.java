package es.pocketrainer.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * A tener en cuenta:<br>
 * El formato de las fechas con la nueva api a partir de Java 8 es ISO 8601.
 * También se usará dicho formato en las comunicaciones de los servicios REST,
 * tanto al enviar objetos con fechas, como para recibirlas.<br>
 * <br>
 * Básico de la nueva api desde java 8: <br>
 * <ul>
 * <li>LocalDateTime: Fecha local, tal cual, la que ves en tu reloj, y sin
 * informacion de zona horaria</li>
 * <li>ZonedDateTime: Fecha local, tal cual, la que ves en tu reloj, pero CON
 * informacion de zona horaria</li>
 * <li>Instant: Es una hora en el tiempo, si importar zona horaria ni nada
 * más</li>
 * </ul>
 * Entonces, puedes tener una LocalDateTime, y querer ponerle una zona, pues:
 * {@code zonedTimeZone = localTimeZoneEjemplo.atZone(ZoneId.of("Asia/Kuala_Lumpur")); }
 * y ahora por ejemplo quieres esa misma hora, pero en otra zona horaria
 * {@code zonedTimeZone.withZoneSameInstant(ZoneId.of("Asia/Tokyo"))} Una zona
 * se puede poner a saco: {@code ZoneOffset.of("-05:00")} Y se supone que java
 * maneja bien el cambio de verano/invierno [ver
 * https://www.mkyong.com/java8/java-8-zoneddatetime-examples/] <br>
 * <br>
 * En el proyecto se va a trabajar en la medida de lo posible con la nueva api
 * de java 8, pero en la parte de android se usara Date debido a que por el api
 * minimo usado no se puede usar todavía la nueva api. En las comunicaciones
 * servidor - android la fecha se debe mover siempre en String formateado en
 * base a ISO 8601, siendo transparente la conversión. <br>
 * <br>
 * Respecto a como se formatean las fechas en las comunicaciones rest, ahora
 * mismo se indica como se recibe en el propio metodo, y como se envia, en la
 * entidad <br>
 * <br>
 * Pruebas en base de datos:<br>
 * Obtener un DateTime:<br>
 * 
 * <ul>
 * <li>LocalDateTime, le aplica la zona horaria, ahora mismo, pues le suma 1
 * hora
 * <li>ZonedDateTime, hace lo mismo, pero indica que es +1 en europa/madrid
 * <li>Date, lo saca también con hora local, e indica +1
 * <li>Instant, lo saca tal cual indicando Z (UTC)
 * <ul>
 * Obtener un DateTime:<br>
 * Todos escriben en UTC, doy por hecho que lo hace hibernate, no MySQL
 * 
 * @author Antonio FZ
 *
 */
public class FechaUtil {

	public static final String FORMATO_FECHA_HORA_REST_ISO_8601 = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";

	public static final LocalDateTime dateToLocalDateTime(Date fecha) {
		return LocalDateTime.ofInstant(fecha.toInstant(), ZoneId.systemDefault());
	}

	public static final ZonedDateTime dateToZonedDateTime(Date fecha) {
		return ZonedDateTime.ofInstant(fecha.toInstant(), ZoneId.systemDefault());
	}

	public static final ZonedDateTime aClienteZoneId(ZonedDateTime fechaHora) {
		if (fechaHora != null) {
			return fechaHora.withZoneSameInstant(ZoneId.of("Europe/Madrid"));
		} else {
			return ZonedDateTime.now();
		}

	}

	public static final String formatearFecha(ZonedDateTime fechaHora) {
		var formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		return fechaHora.format(formatter);
	}

	public static final String formatearFechaHora(ZonedDateTime fechaHora) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
		return fechaHora.format(formatter);
	}

}
