package es.pocketrainer.modelo.valormaestro;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ValorMaestro.class)
public abstract class ValorMaestro_ extends es.pocketrainer.util.bbdd.TablaI18N_ {

	public static volatile SingularAttribute<ValorMaestro, String> codigoTipo;
	public static volatile SingularAttribute<ValorMaestro, String> descripcion;
	public static volatile SingularAttribute<ValorMaestro, String> codigo;
	public static volatile SingularAttribute<ValorMaestro, String> valor;
	public static volatile SingularAttribute<ValorMaestro, Long> id;
	public static volatile SingularAttribute<ValorMaestro, String> codigoPadre;
	public static volatile SingularAttribute<ValorMaestro, String> nombre;
	public static volatile SingularAttribute<ValorMaestro, String> nombreCorto;

	public static final String CODIGO_TIPO = "codigoTipo";
	public static final String DESCRIPCION = "descripcion";
	public static final String CODIGO = "codigo";
	public static final String VALOR = "valor";
	public static final String ID = "id";
	public static final String CODIGO_PADRE = "codigoPadre";
	public static final String NOMBRE = "nombre";
	public static final String NOMBRE_CORTO = "nombreCorto";

}

