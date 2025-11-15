package es.pocketrainer.controlador;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.LocaleResolver;

import es.pocketrainer.modelo.cliente.Cliente;
import es.pocketrainer.modelo.rutina.RutinaFaseGrupo;
import es.pocketrainer.modelo.rutina.RutinaFaseGrupoRepeticionEjercicio;
import es.pocketrainer.modelo.valormaestro.ValorMaestro;
import es.pocketrainer.servicio.cliente.ClienteServicio;
import es.pocketrainer.servicio.cobro.CobroServicio;
import es.pocketrainer.servicio.ejercicio.EjercicioServicio;
import es.pocketrainer.servicio.entrenador.EntrenadorServicio;
import es.pocketrainer.servicio.migracion.MigracionServicio;
import es.pocketrainer.servicio.notificaciones.NotificacionServicio;
import es.pocketrainer.servicio.rutina.RutinaServicio;
import es.pocketrainer.servicio.usuario.UsuarioServicio;
import es.pocketrainer.servicio.valormaestro.ValorMaestroServicio;
import es.pocketrainer.util.Constantes.RolEnum;
import es.pocketrainer.util.Constantes.ValorMaestroTipoEnum;
import es.pocketrainer.util.tpv.SolicitudPagoInicial;

@Controller
@SessionAttributes("cliente")
public class ControladorPrincipal {

	private static Logger LOGGER = LoggerFactory.getLogger(ControladorPrincipal.class);

	@Resource
	private UsuarioServicio usuarioServicio;

	@Resource
	private LocaleResolver localeResolver;

	@Resource
	private ValorMaestroServicio valorMaestroServicio;

	@Resource
	private RutinaServicio rutinaServicio;

	@Resource
	private EjercicioServicio ejercicioServicio;

	@Resource
	private ClienteServicio clienteServicio;

	@Resource
	private EntrenadorServicio entrenadorServicio;

	@Resource
	private NotificacionServicio notificacionService;

	@Resource
	private MigracionServicio migracionServicio;

	@Resource
	private CobroServicio cobroServicio;

	private List<ValorMaestro> seleccion(ValorMaestroTipoEnum valorMaestroTipoEnum) {
		return valorMaestroServicio.buscarValorMaestroListaPorCodigoTipo(valorMaestroTipoEnum);
	}

	@GetMapping("/")
	public String inicio(Model model, Authentication authentication) {
//		migracionServicio.migrarEjercicios();

		if (authentication != null && authentication.isAuthenticated()) {
			Map<String, String> roleTargetUrlMap = new HashMap<>();
			roleTargetUrlMap.put(RolEnum.CLIENTE.codigo(), "forward:/recepcion");
			roleTargetUrlMap.put(RolEnum.ENTRENADOR.codigo(), "forward:/entrenador");

			final Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
			for (final GrantedAuthority grantedAuthority : authorities) {
				String authorityName = grantedAuthority.getAuthority();
				if (roleTargetUrlMap.containsKey(authorityName)) {
					return roleTargetUrlMap.get(authorityName);
				}
			}
		}

		return "inicioPagina";

	}

	@GetMapping("/acceso-miembros")
	public String inicioSesionPagina() {
		return "inicioSesionPagina";
	}

	@PostMapping("/recuperarPasswordFormulario")
	public String recuperarPassword(@RequestParam("correo") String correo) {
		usuarioServicio.recuperarPassword(correo);
		return "redirect:/acceso-miembros";
	}

	@GetMapping("/crea-tu-cuenta-pocketrainer")
	public String creacionCuentaUsuarioPagina(Model model) {
		model.addAttribute("cliente", new Cliente());

		model.addAttribute("diaEntrenamientoSeleccionLista", seleccion(ValorMaestroTipoEnum.DIA_ENTRENAMIENTO));
		model.addAttribute("perfilSeleccionLista", seleccion(ValorMaestroTipoEnum.PERFIL));
		model.addAttribute("lugarEntrenamientoSeleccionLista", seleccion(ValorMaestroTipoEnum.LUGAR_ENTRENAMIENTO));
		model.addAttribute("materialSeleccionLista", ejercicioServicio.buscarMaterialTodos());
		model.addAttribute("cliente", new Cliente());

		return "creacionCuentaUsuarioPagina";
	}

	@RequestMapping("/creacionCuentaUsuarioPagoPagina")
	public String crearCuentaUsuario(@ModelAttribute("cliente") Cliente cliente, BindingResult result, Model model) {

		if (usuarioServicio.buscarUsuarioPorUsuarioNombre(cliente.getUsuario().getUsuarioNombre()) != null) {
			result.addError(new FieldError("cliente", "usuario.usuarioNombre", "Ya existe un usuario con ese correo"));
			model.addAttribute("diaEntrenamientoSeleccionLista", seleccion(ValorMaestroTipoEnum.DIA_ENTRENAMIENTO));
			model.addAttribute("perfilSeleccionLista", seleccion(ValorMaestroTipoEnum.PERFIL));
			model.addAttribute("lugarEntrenamientoSeleccionLista", seleccion(ValorMaestroTipoEnum.LUGAR_ENTRENAMIENTO));
			model.addAttribute("materialSeleccionLista", ejercicioServicio.buscarMaterialTodos());
			return "creacionCuentaUsuarioPagina";
		}

		if (result.hasErrors()) {
			return "creacionCuentaUsuarioPagina";
		}

		return "creacionCuentaUsuarioPagoPagina";
	}

	@RequestMapping("/creacionCuentaUsuarioPagoReintentoPagina")
	public String crearCuentaUsuarioReintento(@ModelAttribute("cliente") Cliente cliente, BindingResult result, Model model) {
		return "creacionCuentaUsuarioPagoPagina";
	}

	@GetMapping("/creacionCuentaUsuarioPagoOk")
	public String pagoOk(Model model) {
		return "creacionCuentaUsuarioPagoOkPagina";
	}

	@GetMapping("/creacionCuentaUsuarioPagoKo/{clienteId}")
	public String pagoKo(@PathVariable("clienteId") Long clienteId, Model model) {

		if (clienteId != null) {
			model.addAttribute("cliente", clienteServicio.buscarClientePorId(clienteId));
			return "creacionCuentaUsuarioPagoKoPagina";
		} else {
			return "redirect:/";
		}

	}

	@PostMapping("/creacionCuentaUsuarioGeneracionPago")
	public String generarFormularioPago(@ModelAttribute("cliente") Cliente cliente, Model model) {

		// Si el cliente existe es que es reintento
		if (cliente.getId() == null) {
			clienteServicio.crearClienteNuevo(cliente);
		}

		SolicitudPagoInicial solicitudPagoInicial = cobroServicio.generarSolicitudPagoInicial(cliente);
		model.addAttribute("solicitudPagoInicial", solicitudPagoInicial);

		return "creacionCuentaUsuarioPagoPagina :: pagoFormularioFragment";

	}

	@GetMapping("/cuentaUsuario")
	public String cuentaUsuario(Model model) {
//		try {
//			Thread.sleep(3000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		return "cuentaUsuarioFragment :: cuentaUsuarioFragment";
	}

	@PostMapping("/cambiarIdioma")
	public void cambiarIdioma(@RequestBody String locale, HttpServletRequest request, HttpServletResponse response) {
		localeResolver.setLocale(request, response, new Locale(locale.substring(0, 2), locale.substring(3, 5)));
	}

	@PostMapping("/actualizarRutinaFaseGrupo")
	public void actualizarRutinaFaseGrupo(@ModelAttribute RutinaFaseGrupo rutinaFaseGrupo, HttpServletRequest request, HttpServletResponse response) {
		System.out.println(rutinaFaseGrupo.getId() + " - " + rutinaFaseGrupo.getRepeticiones());
	}

	@PostMapping("/actualizarRutinaFaseGrupoEjercicio")
	public void actualizarRutinaFaseGrupoEjercicio(@ModelAttribute RutinaFaseGrupoRepeticionEjercicio rutinaFaseGrupoRepeticionEjercicio, HttpServletRequest request,
			HttpServletResponse response) {
		System.out.println(rutinaFaseGrupoRepeticionEjercicio.getId());
	}

	@GetMapping("/avisoLegal")
	public String avisoLegal(Model model) {
		return "avisoLegalPagina";
	}

	@GetMapping("/terminosCondiciones")
	public String terminosCondiciones(Model model) {
		return "terminosCondicionesPagina";
	}

	@GetMapping("/politicaPrivacidad")
	public String politicaPrivacidad(Model model) {
		return "politicaPrivacidadPagina";
	}

	@GetMapping("/politicaCookies")
	public String politicaCookies(Model model) {
		return "politicaCookiesPagina";
	}

}
