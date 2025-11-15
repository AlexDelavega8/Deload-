package es.pocketrainer.modelo.cliente;

import es.pocketrainer.modelo.cobro.SuscripcionCobro;
import es.pocketrainer.modelo.servicio.Servicio;
import es.pocketrainer.modelo.valormaestro.ValorMaestro;
import java.time.ZonedDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Suscripcion.class)
public abstract class Suscripcion_ {

	public static volatile SingularAttribute<Suscripcion, Cliente> cliente;
	public static volatile SingularAttribute<Suscripcion, ZonedDateTime> fechaBaja;
	public static volatile SingularAttribute<Suscripcion, ValorMaestro> estado;
	public static volatile SingularAttribute<Suscripcion, Servicio> servicio;
	public static volatile SingularAttribute<Suscripcion, ZonedDateTime> fechaPeriodoDesde;
	public static volatile ListAttribute<Suscripcion, SuscripcionCobro> suscripcionCobroLista;
	public static volatile SingularAttribute<Suscripcion, Integer> mesesActiva;
	public static volatile SingularAttribute<Suscripcion, ZonedDateTime> fechaUltimaActivacion;
	public static volatile SingularAttribute<Suscripcion, Long> id;
	public static volatile SingularAttribute<Suscripcion, ZonedDateTime> fechaPeriodoHasta;
	public static volatile SingularAttribute<Suscripcion, ZonedDateTime> fechaPrimeraSuscripcion;

	public static final String CLIENTE = "cliente";
	public static final String FECHA_BAJA = "fechaBaja";
	public static final String ESTADO = "estado";
	public static final String SERVICIO = "servicio";
	public static final String FECHA_PERIODO_DESDE = "fechaPeriodoDesde";
	public static final String SUSCRIPCION_COBRO_LISTA = "suscripcionCobroLista";
	public static final String MESES_ACTIVA = "mesesActiva";
	public static final String FECHA_ULTIMA_ACTIVACION = "fechaUltimaActivacion";
	public static final String ID = "id";
	public static final String FECHA_PERIODO_HASTA = "fechaPeriodoHasta";
	public static final String FECHA_PRIMERA_SUSCRIPCION = "fechaPrimeraSuscripcion";

}

