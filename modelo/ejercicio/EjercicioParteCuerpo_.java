package es.pocketrainer.modelo.ejercicio;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(EjercicioParteCuerpo.class)
public abstract class EjercicioParteCuerpo_ {

	public static volatile SingularAttribute<EjercicioParteCuerpo, Boolean> principal;
	public static volatile SingularAttribute<EjercicioParteCuerpo, Long> id;
	public static volatile SingularAttribute<EjercicioParteCuerpo, ParteCuerpo> parteCuerpo;
	public static volatile SingularAttribute<EjercicioParteCuerpo, Ejercicio> ejercicio;

	public static final String PRINCIPAL = "principal";
	public static final String ID = "id";
	public static final String PARTE_CUERPO = "parteCuerpo";
	public static final String EJERCICIO = "ejercicio";

}

