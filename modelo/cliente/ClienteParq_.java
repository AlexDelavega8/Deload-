package es.pocketrainer.modelo.cliente;

import es.pocketrainer.modelo.valormaestro.ValorMaestro;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ClienteParq.class)
public abstract class ClienteParq_ {

	public static volatile SingularAttribute<ClienteParq, Cliente> cliente;
	public static volatile SingularAttribute<ClienteParq, ValorMaestro> parq;
	public static volatile SingularAttribute<ClienteParq, Long> id;
	public static volatile SingularAttribute<ClienteParq, Boolean> respuesta;

	public static final String CLIENTE = "cliente";
	public static final String PARQ = "parq";
	public static final String ID = "id";
	public static final String RESPUESTA = "respuesta";

}

