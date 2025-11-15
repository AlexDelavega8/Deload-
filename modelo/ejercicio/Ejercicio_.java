package es.pocketrainer.modelo.ejercicio;

import java.time.ZonedDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Ejercicio.class)
public abstract class Ejercicio_ {

	public static volatile SingularAttribute<Ejercicio, String> descripcion;
	public static volatile SingularAttribute<Ejercicio, Integer> numeroUsosPorTiempo;
	public static volatile SingularAttribute<Ejercicio, Boolean> esUnilateral;
	public static volatile SetAttribute<Ejercicio, EjercicioParteCuerpo> ejercicioParteCuerpoLista;
	public static volatile SingularAttribute<Ejercicio, Integer> numeroUsosPorRepeticion;
	public static volatile SetAttribute<Ejercicio, Patron> patronLista;
	public static volatile SetAttribute<Ejercicio, Zona> zonaLista;
	public static volatile SingularAttribute<Ejercicio, Integer> configuracionRepeticionesMasUsado;
	public static volatile SingularAttribute<Ejercicio, ZonedDateTime> fechaHoraUltimaActualizacion;
	public static volatile SingularAttribute<Ejercicio, String> nombre;
	public static volatile SetAttribute<Ejercicio, Material> materialLista;
	public static volatile SingularAttribute<Ejercicio, Boolean> tieneAudio;
	public static volatile SingularAttribute<Ejercicio, String> nombreCorto;
	public static volatile SingularAttribute<Ejercicio, Integer> configuracionTiempoMasUsado;
	public static volatile SingularAttribute<Ejercicio, Long> id;
	public static volatile SetAttribute<Ejercicio, EjercicioMusculo> ejercicioMusculoLista;

	public static final String DESCRIPCION = "descripcion";
	public static final String NUMERO_USOS_POR_TIEMPO = "numeroUsosPorTiempo";
	public static final String ES_UNILATERAL = "esUnilateral";
	public static final String EJERCICIO_PARTE_CUERPO_LISTA = "ejercicioParteCuerpoLista";
	public static final String NUMERO_USOS_POR_REPETICION = "numeroUsosPorRepeticion";
	public static final String PATRON_LISTA = "patronLista";
	public static final String ZONA_LISTA = "zonaLista";
	public static final String CONFIGURACION_REPETICIONES_MAS_USADO = "configuracionRepeticionesMasUsado";
	public static final String FECHA_HORA_ULTIMA_ACTUALIZACION = "fechaHoraUltimaActualizacion";
	public static final String NOMBRE = "nombre";
	public static final String MATERIAL_LISTA = "materialLista";
	public static final String TIENE_AUDIO = "tieneAudio";
	public static final String NOMBRE_CORTO = "nombreCorto";
	public static final String CONFIGURACION_TIEMPO_MAS_USADO = "configuracionTiempoMasUsado";
	public static final String ID = "id";
	public static final String EJERCICIO_MUSCULO_LISTA = "ejercicioMusculoLista";

}

