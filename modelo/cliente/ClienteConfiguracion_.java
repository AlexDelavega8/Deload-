package es.pocketrainer.modelo.cliente;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ClienteConfiguracion.class)
public abstract class ClienteConfiguracion_ {

	public static volatile SingularAttribute<ClienteConfiguracion, Cliente> cliente;
	public static volatile SingularAttribute<ClienteConfiguracion, Boolean> ejecucionAutomatica;
	public static volatile SingularAttribute<ClienteConfiguracion, Integer> tiempoDescansoEntreUnilaterales;
	public static volatile SingularAttribute<ClienteConfiguracion, Integer> tiempoDescansoEntreEjercicios;
	public static volatile SingularAttribute<ClienteConfiguracion, Long> id;
	public static volatile SingularAttribute<ClienteConfiguracion, Integer> tiempoEjercicioPorRepeticiones;

	public static final String CLIENTE = "cliente";
	public static final String EJECUCION_AUTOMATICA = "ejecucionAutomatica";
	public static final String TIEMPO_DESCANSO_ENTRE_UNILATERALES = "tiempoDescansoEntreUnilaterales";
	public static final String TIEMPO_DESCANSO_ENTRE_EJERCICIOS = "tiempoDescansoEntreEjercicios";
	public static final String ID = "id";
	public static final String TIEMPO_EJERCICIO_POR_REPETICIONES = "tiempoEjercicioPorRepeticiones";

}

