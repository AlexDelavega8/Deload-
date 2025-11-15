package es.pocketrainer.modelo.cliente;

import es.pocketrainer.modelo.ejercicio.Material;
import es.pocketrainer.modelo.entrenador.Entrenador;
import es.pocketrainer.modelo.rutina.Rutina;
import es.pocketrainer.modelo.usuario.Usuario;
import es.pocketrainer.modelo.valormaestro.ValorMaestro;
import es.pocketrainer.modelo.videoconferencia.Videoconferencia;
import java.time.ZonedDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Cliente.class)
public abstract class Cliente_ {

	public static volatile ListAttribute<Cliente, Rutina> rutinaLista;
	public static volatile SingularAttribute<Cliente, ValorMaestro> estado;
	public static volatile SetAttribute<Cliente, ValorMaestro> perfilLista;
	public static volatile SingularAttribute<Cliente, ZonedDateTime> fechaNacimiento;
	public static volatile SingularAttribute<Cliente, Suscripcion> suscripcion;
	public static volatile SetAttribute<Cliente, ValorMaestro> diaEntrenamientoLista;
	public static volatile SingularAttribute<Cliente, String> nif;
	public static volatile SingularAttribute<Cliente, String> disponibilidadTelefonica;
	public static volatile SingularAttribute<Cliente, String> nombre;
	public static volatile SingularAttribute<Cliente, String> clavesEntrenamiento;
	public static volatile SingularAttribute<Cliente, Boolean> aceptaTerminosCondiciones;
	public static volatile SingularAttribute<Cliente, String> lesiones;
	public static volatile SingularAttribute<Cliente, String> domicilio;
	public static volatile SingularAttribute<Cliente, Entrenador> entrenador;
	public static volatile SingularAttribute<Cliente, String> objetivos;
	public static volatile ListAttribute<Cliente, ClienteParq> clienteParqLista;
	public static volatile SingularAttribute<Cliente, String> localidad;
	public static volatile SingularAttribute<Cliente, Long> id;
	public static volatile SingularAttribute<Cliente, String> telefono;
	public static volatile SingularAttribute<Cliente, String> email;
	public static volatile SetAttribute<Cliente, ValorMaestro> lugarEntrenamientoLista;
	public static volatile SingularAttribute<Cliente, Boolean> aceptaPoliticaPrivacidad;
	public static volatile SingularAttribute<Cliente, String> apellido2;
	public static volatile SingularAttribute<Cliente, String> codigo;
	public static volatile SingularAttribute<Cliente, String> codigoPostal;
	public static volatile SingularAttribute<Cliente, Double> peso;
	public static volatile SingularAttribute<Cliente, String> apellido1;
	public static volatile SingularAttribute<Cliente, ZonedDateTime> fechaAceptaPoliticaPrivacidad;
	public static volatile SingularAttribute<Cliente, ZonedDateTime> fechaAceptaTerminosCondiciones;
	public static volatile SetAttribute<Cliente, Material> materialLista;
	public static volatile SetAttribute<Cliente, ValorMaestro> medioConocimientoLista;
	public static volatile SingularAttribute<Cliente, String> pais;
	public static volatile SingularAttribute<Cliente, ZonedDateTime> fechaRegistroInicial;
	public static volatile ListAttribute<Cliente, Videoconferencia> videoConferenciaLista;
	public static volatile SingularAttribute<Cliente, Integer> altura;
	public static volatile SingularAttribute<Cliente, Boolean> entrenaActualmente;
	public static volatile SingularAttribute<Cliente, String> actividadFisicaActual;
	public static volatile SingularAttribute<Cliente, Usuario> usuario;
	public static volatile SingularAttribute<Cliente, ClienteConfiguracion> clienteConfiguracion;
	public static volatile SingularAttribute<Cliente, ValorMaestro> sexo;
	public static volatile SingularAttribute<Cliente, ClienteDatosCobro> clienteDatosCobro;

	public static final String RUTINA_LISTA = "rutinaLista";
	public static final String ESTADO = "estado";
	public static final String PERFIL_LISTA = "perfilLista";
	public static final String FECHA_NACIMIENTO = "fechaNacimiento";
	public static final String SUSCRIPCION = "suscripcion";
	public static final String DIA_ENTRENAMIENTO_LISTA = "diaEntrenamientoLista";
	public static final String NIF = "nif";
	public static final String DISPONIBILIDAD_TELEFONICA = "disponibilidadTelefonica";
	public static final String NOMBRE = "nombre";
	public static final String CLAVES_ENTRENAMIENTO = "clavesEntrenamiento";
	public static final String ACEPTA_TERMINOS_CONDICIONES = "aceptaTerminosCondiciones";
	public static final String LESIONES = "lesiones";
	public static final String DOMICILIO = "domicilio";
	public static final String ENTRENADOR = "entrenador";
	public static final String OBJETIVOS = "objetivos";
	public static final String CLIENTE_PARQ_LISTA = "clienteParqLista";
	public static final String LOCALIDAD = "localidad";
	public static final String ID = "id";
	public static final String TELEFONO = "telefono";
	public static final String EMAIL = "email";
	public static final String LUGAR_ENTRENAMIENTO_LISTA = "lugarEntrenamientoLista";
	public static final String ACEPTA_POLITICA_PRIVACIDAD = "aceptaPoliticaPrivacidad";
	public static final String APELLIDO2 = "apellido2";
	public static final String CODIGO = "codigo";
	public static final String CODIGO_POSTAL = "codigoPostal";
	public static final String PESO = "peso";
	public static final String APELLIDO1 = "apellido1";
	public static final String FECHA_ACEPTA_POLITICA_PRIVACIDAD = "fechaAceptaPoliticaPrivacidad";
	public static final String FECHA_ACEPTA_TERMINOS_CONDICIONES = "fechaAceptaTerminosCondiciones";
	public static final String MATERIAL_LISTA = "materialLista";
	public static final String MEDIO_CONOCIMIENTO_LISTA = "medioConocimientoLista";
	public static final String PAIS = "pais";
	public static final String FECHA_REGISTRO_INICIAL = "fechaRegistroInicial";
	public static final String VIDEO_CONFERENCIA_LISTA = "videoConferenciaLista";
	public static final String ALTURA = "altura";
	public static final String ENTRENA_ACTUALMENTE = "entrenaActualmente";
	public static final String ACTIVIDAD_FISICA_ACTUAL = "actividadFisicaActual";
	public static final String USUARIO = "usuario";
	public static final String CLIENTE_CONFIGURACION = "clienteConfiguracion";
	public static final String SEXO = "sexo";
	public static final String CLIENTE_DATOS_COBRO = "clienteDatosCobro";

}

