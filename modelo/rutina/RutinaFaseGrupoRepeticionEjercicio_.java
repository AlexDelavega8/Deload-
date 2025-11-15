package es.pocketrainer.modelo.rutina;

import es.pocketrainer.modelo.ejercicio.Ejercicio;
import es.pocketrainer.modelo.ejercicio.Musculo;
import es.pocketrainer.modelo.ejercicio.ParteCuerpo;
import es.pocketrainer.modelo.ejercicio.Patron;
import es.pocketrainer.modelo.ejercicio.Zona;
import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(RutinaFaseGrupoRepeticionEjercicio.class)
public abstract class RutinaFaseGrupoRepeticionEjercicio_ {

	public static volatile SingularAttribute<RutinaFaseGrupoRepeticionEjercicio, Integer> posicion;
	public static volatile SetAttribute<RutinaFaseGrupoRepeticionEjercicio, Musculo> musculoLista;
	public static volatile SetAttribute<RutinaFaseGrupoRepeticionEjercicio, ParteCuerpo> parteCuerpoLista;
	public static volatile SingularAttribute<RutinaFaseGrupoRepeticionEjercicio, Boolean> enEjecucion;
	public static volatile SingularAttribute<RutinaFaseGrupoRepeticionEjercicio, Integer> tiempo;
	public static volatile SetAttribute<RutinaFaseGrupoRepeticionEjercicio, Patron> patronLista;
	public static volatile SetAttribute<RutinaFaseGrupoRepeticionEjercicio, Zona> zonaLista;
	public static volatile SingularAttribute<RutinaFaseGrupoRepeticionEjercicio, Integer> repeticiones;
	public static volatile SingularAttribute<RutinaFaseGrupoRepeticionEjercicio, RutinaFaseGrupoRepeticion> rutinaFaseGrupoRepeticion;
	public static volatile SingularAttribute<RutinaFaseGrupoRepeticionEjercicio, Long> id;
	public static volatile SingularAttribute<RutinaFaseGrupoRepeticionEjercicio, String> comentarios;
	public static volatile SingularAttribute<RutinaFaseGrupoRepeticionEjercicio, Ejercicio> ejercicio;

	public static final String POSICION = "posicion";
	public static final String MUSCULO_LISTA = "musculoLista";
	public static final String PARTE_CUERPO_LISTA = "parteCuerpoLista";
	public static final String EN_EJECUCION = "enEjecucion";
	public static final String TIEMPO = "tiempo";
	public static final String PATRON_LISTA = "patronLista";
	public static final String ZONA_LISTA = "zonaLista";
	public static final String REPETICIONES = "repeticiones";
	public static final String RUTINA_FASE_GRUPO_REPETICION = "rutinaFaseGrupoRepeticion";
	public static final String ID = "id";
	public static final String COMENTARIOS = "comentarios";
	public static final String EJERCICIO = "ejercicio";

}

