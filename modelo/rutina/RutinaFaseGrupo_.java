package es.pocketrainer.modelo.rutina;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(RutinaFaseGrupo.class)
public abstract class RutinaFaseGrupo_ {

	public static volatile SingularAttribute<RutinaFaseGrupo, Integer> posicion;
	public static volatile SingularAttribute<RutinaFaseGrupo, RutinaFase> rutinaFase;
	public static volatile SingularAttribute<RutinaFaseGrupo, Integer> repeticiones;
	public static volatile SingularAttribute<RutinaFaseGrupo, Long> id;
	public static volatile SingularAttribute<RutinaFaseGrupo, String> comentarios;
	public static volatile ListAttribute<RutinaFaseGrupo, RutinaFaseGrupoRepeticion> rutinaFaseGrupoRepeticionLista;

	public static final String POSICION = "posicion";
	public static final String RUTINA_FASE = "rutinaFase";
	public static final String REPETICIONES = "repeticiones";
	public static final String ID = "id";
	public static final String COMENTARIOS = "comentarios";
	public static final String RUTINA_FASE_GRUPO_REPETICION_LISTA = "rutinaFaseGrupoRepeticionLista";

}

