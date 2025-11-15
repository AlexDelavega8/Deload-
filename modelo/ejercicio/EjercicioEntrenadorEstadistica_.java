package es.pocketrainer.modelo.ejercicio;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(EjercicioEntrenadorEstadistica.class)
public abstract class EjercicioEntrenadorEstadistica_ {

	public static volatile SingularAttribute<EjercicioEntrenadorEstadistica, Integer> numeroUsosPorTiempo;
	public static volatile SingularAttribute<EjercicioEntrenadorEstadistica, Integer> configuracionTiempoMasUsado;
	public static volatile SingularAttribute<EjercicioEntrenadorEstadistica, Integer> numeroUsosPorRepeticion;
	public static volatile SingularAttribute<EjercicioEntrenadorEstadistica, Integer> configuracionRepeticionesMasUsado;
	public static volatile SingularAttribute<EjercicioEntrenadorEstadistica, EjercicioEntrenadorEstadisticaId> id;

	public static final String NUMERO_USOS_POR_TIEMPO = "numeroUsosPorTiempo";
	public static final String CONFIGURACION_TIEMPO_MAS_USADO = "configuracionTiempoMasUsado";
	public static final String NUMERO_USOS_POR_REPETICION = "numeroUsosPorRepeticion";
	public static final String CONFIGURACION_REPETICIONES_MAS_USADO = "configuracionRepeticionesMasUsado";
	public static final String ID = "id";

}

