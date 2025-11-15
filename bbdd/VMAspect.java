package es.pocketrainer.util.bbdd;

import javax.annotation.Resource;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import es.pocketrainer.servicio.valormaestro.ValorMaestroServicio;
import jodd.bean.BeanUtil;

@Aspect
public class VMAspect {

	private static Logger LOGGER = LoggerFactory.getLogger(VMAspect.class);

	@Resource
	private ValorMaestroServicio valorMaestroServicio;

	@Around("@annotation(VM)")
	public Object gestionarTraduccion(ProceedingJoinPoint joinPoint) throws Throwable {
//		LOGGER.debug("Interviene VMAspect");

		var valorMaestroCodigoAtributo = ((MethodSignature) joinPoint.getSignature()).getMethod().getName().substring(3).concat("Codigo");

		String valorMaestroCodigo = BeanUtil.pojo.getProperty(joinPoint.getTarget(), StringUtils.uncapitalize(valorMaestroCodigoAtributo));

		return valorMaestroServicio.buscarValorMaestroPorCodigo(valorMaestroCodigo);

	}
}
