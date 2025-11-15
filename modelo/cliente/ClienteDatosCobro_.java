package es.pocketrainer.modelo.cliente;

import java.time.ZonedDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ClienteDatosCobro.class)
public abstract class ClienteDatosCobro_ {

	public static volatile SingularAttribute<ClienteDatosCobro, Cliente> cliente;
	public static volatile SingularAttribute<ClienteDatosCobro, ZonedDateTime> tarjetaFechaCaducidad;
	public static volatile SingularAttribute<ClienteDatosCobro, Long> id;
	public static volatile SingularAttribute<ClienteDatosCobro, ZonedDateTime> fechaUltimaActualizacion;
	public static volatile SingularAttribute<ClienteDatosCobro, String> tarjetaIdentificacionToken;
	public static volatile SingularAttribute<ClienteDatosCobro, String> identificadorTransaccionCof;

	public static final String CLIENTE = "cliente";
	public static final String TARJETA_FECHA_CADUCIDAD = "tarjetaFechaCaducidad";
	public static final String ID = "id";
	public static final String FECHA_ULTIMA_ACTUALIZACION = "fechaUltimaActualizacion";
	public static final String TARJETA_IDENTIFICACION_TOKEN = "tarjetaIdentificacionToken";
	public static final String IDENTIFICADOR_TRANSACCION_COF = "identificadorTransaccionCof";

}

