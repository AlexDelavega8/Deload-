package es.pocketrainer;

import javax.annotation.Resource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import es.pocketrainer.seguridad.CustomAccessDeniedHandler;
import es.pocketrainer.seguridad.CustomAutenticacionEntryPoint;
import es.pocketrainer.seguridad.LocaleAndRoleAwareAuthenticationSuccessHandler;
import es.pocketrainer.util.Constantes;

@Configuration
@PropertySources({ @PropertySource(value = "classpath:app.properties", encoding = "UTF-8"), @PropertySource(value = "${tpv.properties}", encoding = "UTF-8") })
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class Configuracion extends ConfiguracionBase {

	@Resource
	private LocaleAndRoleAwareAuthenticationSuccessHandler localeAndSavedRequestAwareAuthenticationSuccessHandler;

	@Resource
	private CustomAutenticacionEntryPoint customAutenticacionEntryPoint;

	@Resource
	private CustomAccessDeniedHandler customAccessDeniedHandler;

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.requiresChannel().and().authorizeRequests()
				.antMatchers("/", "/acceso-miembros", "/recuperarPasswordFormulario", "/comprobarPasswordAnterior/{password}", "/creacionCuentaUsuarioPagoOk",
						"/creacionCuentaUsuarioPagoKo/{clienteId}", "/error", "/crea-tu-cuenta-pocketrainer", "/creacionCuentaUsuarioPagoPagina",
						"/creacionCuentaUsuarioPagoReintentoPagina", "/creacionCuentaUsuarioGeneracionPago",
						"/tpv/notificacionAsincrona/{clienteId}/{suscripcionCobroId}", "/tpv/notificacionSincronaOk/{clienteId}/{suscripcionCobroId}",
						"/tpv/notificacionSincronaKo/{clienteId}/{suscripcionCobroId}", "/tpv/notificacionAsincronaCliente/{clienteId}/{suscripcionCobroId}",
						"/tpv/notificacionSincronaOkCliente/{clienteId}/{suscripcionCobroId}", "/tpv/notificacionSincronaKoCliente/{clienteId}/{suscripcionCobroId}",
						"/tpv/notificacionAsincronaRenovacion/{clienteId}/{suscripcionCobroId}", "/avisoLegal", "/terminosCondiciones", "/politicaPrivacidad",
						"/politicaCookies", "/css/**", "/js/**", "/images/**", "/webjars/**")
				.permitAll().anyRequest().authenticated().and().formLogin().loginPage("/acceso-miembros").defaultSuccessUrl("/", true)
				.successHandler(localeAndSavedRequestAwareAuthenticationSuccessHandler).permitAll().and().logout().logoutSuccessUrl("/acceso-miembros").permitAll().and()
				.exceptionHandling().accessDeniedHandler(customAccessDeniedHandler).authenticationEntryPoint(customAutenticacionEntryPoint);

		http.csrf().ignoringAntMatchers("/tpv/notificacionSincronaOk/{clienteId}/{suscripcionCobroId}", "/tpv/notificacionSincronaKo/{clienteId}/{suscripcionCobroId}",
				"/tpv/notificacionAsincrona/{clienteId}/{suscripcionCobroId}", "/tpv/notificacionAsincronaCliente/{clienteId}/{suscripcionCobroId}",
				"/tpv/notificacionSincronaOkCliente/{clienteId}/{suscripcionCobroId}", "/tpv/notificacionSincronaKoCliente/{clienteId}/{suscripcionCobroId}",
				"/tpv/notificacionAsincronaRenovacion/{clienteId}/{suscripcionCobroId}");
	}

	/**
	 * Proporciona el locale actual en donde se necesita, por ejemplo, para los
	 * mensajes internacionalizados. Ahora mismo se usa {@link CookieLocaleResolver}
	 * para que no sea necesario esperar a inicio de sesion para poner el locale en
	 * principio adecuado, que sera el ultimo obtenido de la sesion del cliente.
	 * {@link SessionLocaleResolver} podria valer si nos da igual usar el locale por
	 * defecto hasta tener una sesion.</br>
	 * </br>
	 * {@link LocaleAndRoleAwareAuthenticationSuccessHandler} se encarga de setear
	 * el locale cada vez que se inicia sesion a partir de la informacion de locale
	 * del usuario.</br>
	 * </br>
	 * Otra opcion puede ser crear nuestro propio LocaleResolver que obtenga el
	 * locale directamente de la informacion de la sesion, por ejemplo así:
	 * SecurityContextHolder.getContext().getAuthentication()
	 * 
	 * @return
	 */
	@Bean
	public LocaleResolver localeResolver() {

		// Mediante cookie: Ojo politica de cookies, lo complica
//		CookieLocaleResolver localeResolver = new CookieLocaleResolver();
//		localeResolver.setCookieName("POCKETRAINER_IDIOMA");
//		localeResolver.setCookieMaxAge(31536000);

		// Mediante sesion:
		SessionLocaleResolver localeResolver = new SessionLocaleResolver();
		localeResolver.setDefaultLocale(Constantes.LOCALE_POR_DEFECTO);
		return localeResolver;
	}

}
