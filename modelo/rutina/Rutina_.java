package es.pocketrainer.modelo.rutina;

import es.pocketrainer.modelo.cliente.Cliente;
import es.pocketrainer.modelo.entrenador.Entrenador;
import es.pocketrainer.modelo.valormaestro.ValorMaestro;
import java.time.ZonedDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Rutina.class)
public abstract class Rutina_ {

	public static volatile SingularAttribute<Rutina, RutinaFase> faseFinal;
	public static volatile SingularAttribute<Rutina, ValorMaestro> estado;
	public static volatile SingularAttribute<Rutina, Integer> entrenadorTiempoEjercicioPorRepeticiones;
	public static volatile SingularAttribute<Rutina, Integer> clienteTiempoDescansoEntreEjercicios;
	public static volatile SingularAttribute<Rutina, String> comentariosEntrenador;
	public static volatile SingularAttribute<Rutina, Boolean> esPlantilla;
	public static volatile SingularAttribute<Rutina, RutinaFase> fasePrincipal;
	public static volatile SingularAttribute<Rutina, Integer> entrenadorTiempoDescansoEntreEjercicios;
	public static volatile SingularAttribute<Rutina, String> nombre;
	public static volatile SingularAttribute<Rutina, String> comentariosCliente;
	public static volatile SingularAttribute<Rutina, Integer> entrenadorTiempoDescansoEntreUnilaterales;
	public static volatile SingularAttribute<Rutina, RutinaFase> faseInicial;
	public static volatile SingularAttribute<Rutina, Cliente> cliente;
	public static volatile SingularAttribute<Rutina, Integer> clienteTiempoDescansoEntreUnilaterales;
	public static volatile SingularAttribute<Rutina, Boolean> enEjecucion;
	public static volatile SingularAttribute<Rutina, Entrenador> entrenador;
	public static volatile SingularAttribute<Rutina, ZonedDateTime> fechaCreacion;
	public static volatile SingularAttribute<Rutina, Long> id;
	public static volatile SingularAttribute<Rutina, Integer> clienteTiempoEjercicioPorRepeticiones;
	public static volatile SetAttribute<Rutina, ZonedDateTime> rutinaFechaLista;
	public static volatile SingularAttribute<Rutina, Boolean> clienteEjecucionAutomatica;

	public static final String FASE_FINAL = "faseFinal";
	public static final String ESTADO = "estado";
	public static final String ENTRENADOR_TIEMPO_EJERCICIO_POR_REPETICIONES = "entrenadorTiempoEjercicioPorRepeticiones";
	public static final String CLIENTE_TIEMPO_DESCANSO_ENTRE_EJERCICIOS = "clienteTiempoDescansoEntreEjercicios";
	public static final String COMENTARIOS_ENTRENADOR = "comentariosEntrenador";
	public static final String ES_PLANTILLA = "esPlantilla";
	public static final String FASE_PRINCIPAL = "fasePrincipal";
	public static final String ENTRENADOR_TIEMPO_DESCANSO_ENTRE_EJERCICIOS = "entrenadorTiempoDescansoEntreEjercicios";
	public static final String NOMBRE = "nombre";
	public static final String COMENTARIOS_CLIENTE = "comentariosCliente";
	public static final String ENTRENADOR_TIEMPO_DESCANSO_ENTRE_UNILATERALES = "entrenadorTiempoDescansoEntreUnilaterales";
	public static final String FASE_INICIAL = "faseInicial";
	public static final String CLIENTE = "cliente";
	public static final String CLIENTE_TIEMPO_DESCANSO_ENTRE_UNILATERALES = "clienteTiempoDescansoEntreUnilaterales";
	public static final String EN_EJECUCION = "enEjecucion";
	public static final String ENTRENADOR = "entrenador";
	public static final String FECHA_CREACION = "fechaCreacion";
	public static final String ID = "id";
	public static final String CLIENTE_TIEMPO_EJERCICIO_POR_REPETICIONES = "clienteTiempoEjercicioPorRepeticiones";
	public static final String RUTINA_FECHA_LISTA = "rutinaFechaLista";
	public static final String CLIENTE_EJECUCION_AUTOMATICA = "clienteEjecucionAutomatica";

}

