package es.pocketrainer.modelo.servicio;

import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Servicio.class)
public abstract class Servicio_ {

	public static volatile SingularAttribute<Servicio, String> descripcion;
	public static volatile SingularAttribute<Servicio, BigDecimal> precio;
	public static volatile SingularAttribute<Servicio, Long> id;
	public static volatile SingularAttribute<Servicio, String> nombre;
	public static volatile SingularAttribute<Servicio, String> nombreCorto;

	public static final String DESCRIPCION = "descripcion";
	public static final String PRECIO = "precio";
	public static final String ID = "id";
	public static final String NOMBRE = "nombre";
	public static final String NOMBRE_CORTO = "nombreCorto";

}

