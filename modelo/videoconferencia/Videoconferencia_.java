package es.pocketrainer.modelo.videoconferencia;

import es.pocketrainer.modelo.cliente.Cliente;
import es.pocketrainer.modelo.entrenador.Entrenador;
import es.pocketrainer.modelo.valormaestro.ValorMaestro;
import java.time.ZonedDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Videoconferencia.class)
public abstract class Videoconferencia_ {

	public static volatile SingularAttribute<Videoconferencia, Cliente> cliente;
	public static volatile SingularAttribute<Videoconferencia, ValorMaestro> estado;
	public static volatile SingularAttribute<Videoconferencia, Entrenador> entrenador;
	public static volatile SingularAttribute<Videoconferencia, ZonedDateTime> fechaHoraProgramada;
	public static volatile SingularAttribute<Videoconferencia, String> salaId;
	public static volatile SingularAttribute<Videoconferencia, Long> rutinaId;
	public static volatile SingularAttribute<Videoconferencia, Long> id;

	public static final String CLIENTE = "cliente";
	public static final String ESTADO = "estado";
	public static final String ENTRENADOR = "entrenador";
	public static final String FECHA_HORA_PROGRAMADA = "fechaHoraProgramada";
	public static final String SALA_ID = "salaId";
	public static final String RUTINA_ID = "rutinaId";
	public static final String ID = "id";

}

