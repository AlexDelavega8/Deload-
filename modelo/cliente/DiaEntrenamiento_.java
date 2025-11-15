package es.pocketrainer.modelo.cliente;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(DiaEntrenamiento.class)
public abstract class DiaEntrenamiento_ {

	public static volatile SingularAttribute<DiaEntrenamiento, String> codigo;
	public static volatile SingularAttribute<DiaEntrenamiento, Integer> numero;
	public static volatile SingularAttribute<DiaEntrenamiento, Long> id;
	public static volatile SingularAttribute<DiaEntrenamiento, String> nombre;
	public static volatile SingularAttribute<DiaEntrenamiento, String> nombreCorto;

	public static final String CODIGO = "codigo";
	public static final String NUMERO = "numero";
	public static final String ID = "id";
	public static final String NOMBRE = "nombre";
	public static final String NOMBRE_CORTO = "nombreCorto";

}

