package es.pocketrainer.util.bbdd;

import java.lang.reflect.InvocationTargetException;
import java.util.Locale;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;

import es.pocketrainer.util.Constantes;

@Aspect
public class I18NAspect {

	private static Logger LOGGER = LoggerFactory.getLogger(I18NAspect.class);

	@Around("@annotation(I18N)")
	public Object gestionarTraduccion(ProceedingJoinPoint joinPoint) throws Throwable {
//		LOGGER.debug("Interviene I18NAspect");
		String traduccion = null;
		Locale localeActual = LocaleContextHolder.getLocale();

		if (!Constantes.LOCALE_POR_DEFECTO.equals(localeActual)) {
			TablaI18N<?> tablaI18N = (TablaI18N<?>) joinPoint.getTarget();
			TraduccionI18N traduccionI18N = tablaI18N.getTraducciones().stream()
					.filter((it) -> it.getIdioma().equals(LocaleContextHolder.getLocale().getLanguage())).findFirst().orElse(null);

			if (traduccionI18N != null) {
				String metodo = ((MethodSignature) joinPoint.getSignature()).getMethod().getName();
				try {
					traduccion = (String) traduccionI18N.getClass().getMethod(metodo).invoke(traduccionI18N);
				} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
						| InvocationTargetException e) {
					LOGGER.error("Error buscando traduccion en la entidad " + joinPoint.getTarget().getClass() + " para el metodo " + metodo, e);
				}
			}
		}

		if (traduccion == null) {
			traduccion = (String) joinPoint.proceed();
		}

		return traduccion;
	}
}
