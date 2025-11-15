package es.pocketrainer.modelo.cobro;

import es.pocketrainer.modelo.cliente.Suscripcion;
import es.pocketrainer.modelo.entrenador.Entrenador;
import es.pocketrainer.modelo.valormaestro.ValorMaestro;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(SuscripcionCobro.class)
public abstract class SuscripcionCobro_ {

	public static volatile SingularAttribute<SuscripcionCobro, ZonedDateTime> fechaHoraNotificacionSincronaOk;
	public static volatile SingularAttribute<SuscripcionCobro, ValorMaestro> estado;
	public static volatile SingularAttribute<SuscripcionCobro, ZonedDateTime> fechaPeriodoDesde;
	public static volatile SingularAttribute<SuscripcionCobro, ZonedDateTime> fechaHoraEmision;
	public static volatile SingularAttribute<SuscripcionCobro, Suscripcion> suscripcion;
	public static volatile SingularAttribute<SuscripcionCobro, ZonedDateTime> fechaHoraNotificacionSincronaKo;
	public static volatile SingularAttribute<SuscripcionCobro, ZonedDateTime> fechaPeriodoHasta;
	public static volatile SingularAttribute<SuscripcionCobro, ZonedDateTime> fechaHoraNotificacionAsincrona;
	public static volatile SingularAttribute<SuscripcionCobro, ZonedDateTime> fechaHoraCobro;
	public static volatile SingularAttribute<SuscripcionCobro, Entrenador> entrenador;
	public static volatile SingularAttribute<SuscripcionCobro, String> transaccionTipo;
	public static volatile SingularAttribute<SuscripcionCobro, String> codigoAutorizacion;
	public static volatile SingularAttribute<SuscripcionCobro, String> numeroPedido;
	public static volatile SingularAttribute<SuscripcionCobro, Long> id;
	public static volatile SingularAttribute<SuscripcionCobro, BigDecimal> cantidad;
	public static volatile SingularAttribute<SuscripcionCobro, String> codigoRespuesta;

	public static final String FECHA_HORA_NOTIFICACION_SINCRONA_OK = "fechaHoraNotificacionSincronaOk";
	public static final String ESTADO = "estado";
	public static final String FECHA_PERIODO_DESDE = "fechaPeriodoDesde";
	public static final String FECHA_HORA_EMISION = "fechaHoraEmision";
	public static final String SUSCRIPCION = "suscripcion";
	public static final String FECHA_HORA_NOTIFICACION_SINCRONA_KO = "fechaHoraNotificacionSincronaKo";
	public static final String FECHA_PERIODO_HASTA = "fechaPeriodoHasta";
	public static final String FECHA_HORA_NOTIFICACION_ASINCRONA = "fechaHoraNotificacionAsincrona";
	public static final String FECHA_HORA_COBRO = "fechaHoraCobro";
	public static final String ENTRENADOR = "entrenador";
	public static final String TRANSACCION_TIPO = "transaccionTipo";
	public static final String CODIGO_AUTORIZACION = "codigoAutorizacion";
	public static final String NUMERO_PEDIDO = "numeroPedido";
	public static final String ID = "id";
	public static final String CANTIDAD = "cantidad";
	public static final String CODIGO_RESPUESTA = "codigoRespuesta";

}

