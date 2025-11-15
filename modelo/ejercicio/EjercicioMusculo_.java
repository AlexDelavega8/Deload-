package es.pocketrainer.modelo.ejercicio;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(EjercicioMusculo.class)
public abstract class EjercicioMusculo_ {

	public static volatile SingularAttribute<EjercicioMusculo, Boolean> principal;
	public static volatile SingularAttribute<EjercicioMusculo, Musculo> musculo;
	public static volatile SingularAttribute<EjercicioMusculo, Long> id;
	public static volatile SingularAttribute<EjercicioMusculo, Ejercicio> ejercicio;

	public static final String PRINCIPAL = "principal";
	public static final String MUSCULO = "musculo";
	public static final String ID = "id";
	public static final String EJERCICIO = "ejercicio";

}

