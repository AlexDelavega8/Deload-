package es.pocketrainer.modelo.rutina;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(RutinaFaseGrupoRepeticion.class)
public abstract class RutinaFaseGrupoRepeticion_ {

	public static volatile SingularAttribute<RutinaFaseGrupoRepeticion, RutinaFaseGrupo> rutinaFaseGrupo;
	public static volatile SingularAttribute<RutinaFaseGrupoRepeticion, Long> id;
	public static volatile ListAttribute<RutinaFaseGrupoRepeticion, RutinaFaseGrupoRepeticionEjercicio> rutinaFaseGrupoRepeticionEjercicioLista;
	public static volatile SingularAttribute<RutinaFaseGrupoRepeticion, Integer> numeroRepeticion;

	public static final String RUTINA_FASE_GRUPO = "rutinaFaseGrupo";
	public static final String ID = "id";
	public static final String RUTINA_FASE_GRUPO_REPETICION_EJERCICIO_LISTA = "rutinaFaseGrupoRepeticionEjercicioLista";
	public static final String NUMERO_REPETICION = "numeroRepeticion";

}

