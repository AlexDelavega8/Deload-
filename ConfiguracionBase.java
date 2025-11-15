package es.pocketrainer;

import java.util.Locale;
import java.util.TimeZone;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.aspectj.lang.Aspects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.envers.repository.support.EnversRevisionRepositoryFactoryBean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import es.pocketrainer.servicio.usuario.UsuarioServicio;
import es.pocketrainer.util.Constantes;
import es.pocketrainer.util.bbdd.AuditorAwareImpl;
import es.pocketrainer.util.bbdd.I18NAspect;
import es.pocketrainer.util.bbdd.VMAspect;

@EnableCaching
@EnableScheduling
@EnableAsync
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
@EnableJpaRepositories(repositoryFactoryBeanClass = EnversRevisionRepositoryFactoryBean.class)
@Order(1)
public class ConfiguracionBase extends WebSecurityConfigurerAdapter {

	private static Logger LOGGER = LoggerFactory.getLogger(ConfiguracionBase.class);

	@Resource
	private UsuarioServicio usuarioServicio;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(usuarioServicio);
		authProvider.setPasswordEncoder(encoder());
		return authProvider;
	}

	/**
	 * Establezco timezone por defecto para todas las fechas manejadas en la
	 * aplicacion. Por ahora la espanhola, pero lo logico sera usar UTC y mostrar a
	 * cada cliente la hora en base a su timezone.</br>
	 * Tambien se indica el locale por defecto. Ver localeResolver en la
	 * configuracion del proyecto web y servicios, donde tambien se indica el locale
	 * por defecto, pero es a nivel de spring, y aqui estamos a nivel de jvm
	 */
	@PostConstruct
	void started() {
		TimeZone.setDefault(TimeZone.getTimeZone(Constantes.ZONE_POR_DEFECTO));
		Locale.setDefault(Constantes.LOCALE_POR_DEFECTO);
	}

	@Bean
	public PasswordEncoder encoder() {
		return new BCryptPasswordEncoder(11);
	}

	@Bean
	public VMAspect vmAspect() {
		return Aspects.aspectOf(VMAspect.class);
	}

	@Bean
	public I18NAspect i18nAspect() {
		return Aspects.aspectOf(I18NAspect.class);
	}

	@Bean
	AuditorAware<String> auditorProvider() {
		return new AuditorAwareImpl();
	}

	@Configuration
	@Order(2)
	public static class AuthenticationMananagerProvider extends WebSecurityConfigurerAdapter {

		@Bean
		@Override
		public AuthenticationManager authenticationManagerBean() throws Exception {
			return super.authenticationManagerBean();
		}

	}

}
