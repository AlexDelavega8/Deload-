package es.pocketrainer.modelo.usuario;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Usuario.class)
public abstract class Usuario_ {

	public static volatile SetAttribute<Usuario, Rol> rolLista;
	public static volatile SingularAttribute<Usuario, String> password;
	public static volatile SingularAttribute<Usuario, Long> id;
	public static volatile SingularAttribute<Usuario, String> usuarioNombre;

	public static final String ROL_LISTA = "rolLista";
	public static final String PASSWORD = "password";
	public static final String ID = "id";
	public static final String USUARIO_NOMBRE = "usuarioNombre";

}

