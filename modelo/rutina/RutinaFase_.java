package es.pocketrainer.modelo.rutina;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(RutinaFase.class)
public abstract class RutinaFase_ {

	public static volatile SingularAttribute<RutinaFase, Integer> tipo;
	public static volatile SingularAttribute<RutinaFase, Long> id;
	public static volatile SingularAttribute<RutinaFase, String> nombre;
	public static volatile ListAttribute<RutinaFase, RutinaFaseGrupo> rutinaFaseGrupoLista;

	public static final String TIPO = "tipo";
	public static final String ID = "id";
	public static final String NOMBRE = "nombre";
	public static final String RUTINA_FASE_GRUPO_LISTA = "rutinaFaseGrupoLista";

}

