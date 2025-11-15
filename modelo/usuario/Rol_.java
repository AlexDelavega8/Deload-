package es.pocketrainer.modelo.usuario;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Rol.class)
public abstract class Rol_ {

	public static volatile SingularAttribute<Rol, String> codigo;
	public static volatile SetAttribute<Rol, Permiso> permisoLista;
	public static volatile SingularAttribute<Rol, Long> id;
	public static volatile SingularAttribute<Rol, String> nombre;

	public static final String CODIGO = "codigo";
	public static final String PERMISO_LISTA = "permisoLista";
	public static final String ID = "id";
	public static final String NOMBRE = "nombre";

}

