package es.pocketrainer.modelo.entrenador;

import es.pocketrainer.modelo.cliente.Cliente;
import es.pocketrainer.modelo.rutina.Rutina;
import es.pocketrainer.modelo.usuario.Usuario;
import es.pocketrainer.modelo.valormaestro.ValorMaestro;
import es.pocketrainer.modelo.videoconferencia.Videoconferencia;
import java.time.ZonedDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Entrenador.class)
public abstract class Entrenador_ {

	public static volatile SingularAttribute<Entrenador, String> apellido2;
	public static volatile ListAttribute<Entrenador, Rutina> rutinaLista;
	public static volatile SingularAttribute<Entrenador, ValorMaestro> estado;
	public static volatile SingularAttribute<Entrenador, String> codigo;
	public static volatile SingularAttribute<Entrenador, String> apellido1;
	public static volatile SingularAttribute<Entrenador, String> nombre;
	public static volatile SingularAttribute<Entrenador, ZonedDateTime> fechaRegistroInicial;
	public static volatile ListAttribute<Entrenador, Cliente> clienteLista;
	public static volatile ListAttribute<Entrenador, Videoconferencia> videoConferenciaLista;
	public static volatile SingularAttribute<Entrenador, Usuario> usuario;
	public static volatile SingularAttribute<Entrenador, Long> id;
	public static volatile SingularAttribute<Entrenador, String> telefono;
	public static volatile SingularAttribute<Entrenador, String> email;

	public static final String APELLIDO2 = "apellido2";
	public static final String RUTINA_LISTA = "rutinaLista";
	public static final String ESTADO = "estado";
	public static final String CODIGO = "codigo";
	public static final String APELLIDO1 = "apellido1";
	public static final String NOMBRE = "nombre";
	public static final String FECHA_REGISTRO_INICIAL = "fechaRegistroInicial";
	public static final String CLIENTE_LISTA = "clienteLista";
	public static final String VIDEO_CONFERENCIA_LISTA = "videoConferenciaLista";
	public static final String USUARIO = "usuario";
	public static final String ID = "id";
	public static final String TELEFONO = "telefono";
	public static final String EMAIL = "email";

}

