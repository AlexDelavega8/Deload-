package es.pocketrainer.controlador;

import static es.pocketrainer.util.Constantes.FOTO_POR_DEFECTO;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import es.pocketrainer.formulario.ConfigurarRutinaFormulario;
import es.pocketrainer.formulario.NuevasCondicionesFormulario;
import es.pocketrainer.formulario.UsuarioCambioPasswordFormulario;
import es.pocketrainer.modelo.cliente.Cliente;
import es.pocketrainer.modelo.cliente.ClienteConfiguracion;
import es.pocketrainer.modelo.rutina.Rutina;
import es.pocketrainer.modelo.rutina.RutinaEjecucion;
import es.pocketrainer.modelo.rutina.RutinaFaseGrupoRepeticionEjercicio;
import es.pocketrainer.modelo.valormaestro.ValorMaestro;
import es.pocketrainer.modelo.videoconferencia.Videoconferencia;
import es.pocketrainer.seguridad.UsuarioSesion;
import es.pocketrainer.servicio.cliente.ClienteServicio;
import es.pocketrainer.servicio.cobro.CobroServicio;
import es.pocketrainer.servicio.ejercicio.EjercicioServicio;
import es.pocketrainer.servicio.migracion.MigracionServicio;
import es.pocketrainer.servicio.rutina.RutinaServicio;
import es.pocketrainer.servicio.usuario.UsuarioServicio;
import es.pocketrainer.servicio.valormaestro.ValorMaestroServicio;
import es.pocketrainer.servicio.videoconferencia.VideoconferenciaServicio;
import es.pocketrainer.util.Constantes;
import es.pocketrainer.util.Constantes.RutinaEstadoEnum;
import es.pocketrainer.util.Constantes.SuscripcionEstadoEnum;
import es.pocketrainer.util.Constantes.ValorMaestroTipoEnum;
import es.pocketrainer.util.Constantes.VideoconferenciaEstadoEnum;

@Controller
public class ClienteControlador {

	@Resource
	private UsuarioServicio usuarioServicio;

	@Resource
	private ClienteServicio clienteServicio;

	@Resource
	private RutinaServicio rutinaServicio;

	@Resource
	private ValorMaestroServicio valorMaestroServicio;

	@Resource
	private EjercicioServicio ejercicioServicio;

	@Resource
	private MigracionServicio migracionServicio;

	@Resource
	private CobroServicio cobroServicio;

	@Resource
	private VideoconferenciaServicio videoconferenciaServicio;

	private List<ValorMaestro> seleccion(ValorMaestroTipoEnum valorMaestroTipoEnum) {
		return valorMaestroServicio.buscarValorMaestroListaPorCodigoTipo(valorMaestroTipoEnum);
	}

//	@RequestMapping("/testRenovacion")
//	public String testRenovacion(Model model, Authentication authentication) {
//		UsuarioSesion usuarioSesion = (UsuarioSesion) authentication.getPrincipal();
//		clienteServicio.pruebaRenovacion(usuarioSesion.getClienteId());
//		return "redirect:/recepcion";
//	}

	@RequestMapping("/pagoSuscripcionClientePagina")
	public String crearCuentaUsuarioReintento(Model model, Authentication authentication) {
		var usuarioSesion = (UsuarioSesion) authentication.getPrincipal();
		var cliente = clienteServicio.buscarClientePorId(usuarioSesion.getClienteId());

		var enPeriodoPromocion = (ZonedDateTime.now().isBefore(ZonedDateTime.of(2020, 8, 1, 0, 0, 0, 0, ZoneId.of("Europe/Madrid"))));

		// Por seguridad, comprobar estados adecuados de impagado
		if (cliente.getSuscripcion().getEstado().getCodigo().equals(Constantes.SuscripcionEstadoEnum.IMPAGADA_INICIAL.codigo())
				|| cliente.getSuscripcion().getEstado().getCodigo().equals(Constantes.SuscripcionEstadoEnum.IMPAGADA.codigo())) {
			if (cliente.getSuscripcion().getEstado().getCodigo().equals(Constantes.SuscripcionEstadoEnum.IMPAGADA_INICIAL.codigo()) && enPeriodoPromocion) {
				model.addAttribute("servicio", "Servicio de entrenamiento de entrenamiento personalizado online (50% de descuento inicial aplicado)");
				model.addAttribute("subtotal", "16,52 €");
				model.addAttribute("iva", "3,47 €");
				model.addAttribute("total", "19,99 €");
			} else {
				model.addAttribute("servicio", "Servicio de entrenamiento de entrenamiento personalizado online");
				model.addAttribute("subtotal", "33,05 €");
				model.addAttribute("iva", "6,94 €");
				model.addAttribute("total", "39,99 €");
			}
			return "cliente/pagoSuscripcionClientePagina";
		} else {
			return "redirect:/recepcion";
		}

	}

	@RequestMapping("/reactivarSuscripcion")
	public String reactivarSuscripcion(Model model, Authentication authentication) {
		var usuarioSesion = (UsuarioSesion) authentication.getPrincipal();
		var cliente = clienteServicio.reactivarSuscripcion(usuarioSesion.getClienteId());

		// Por seguridad, comprobar estados adecuados de impagado
		if (cliente.getSuscripcion().getEstado().getCodigo().equals(Constantes.SuscripcionEstadoEnum.IMPAGADA_INICIAL.codigo())
				|| cliente.getSuscripcion().getEstado().getCodigo().equals(Constantes.SuscripcionEstadoEnum.IMPAGADA.codigo())) {
			// Al pago
			var enPeriodoPromocion = (ZonedDateTime.now().isBefore(ZonedDateTime.of(2020, 8, 1, 0, 0, 0, 0, ZoneId.of("Europe/Madrid"))));

			if (cliente.getSuscripcion().getEstado().getCodigo().equals(Constantes.SuscripcionEstadoEnum.IMPAGADA_INICIAL.codigo()) && enPeriodoPromocion) {
				model.addAttribute("servicio", "Servicio de entrenamiento de entrenamiento personalizado online (50% de descuento inicial aplicado)");
				model.addAttribute("subtotal", "16,52 €");
				model.addAttribute("iva", "3,47 €");
				model.addAttribute("total", "19,99 €");
			} else {
				model.addAttribute("servicio", "Servicio de entrenamiento de entrenamiento personalizado online");
				model.addAttribute("subtotal", "33,05 €");
				model.addAttribute("iva", "6,94 €");
				model.addAttribute("total", "39,99 €");
			}
			return "cliente/pagoSuscripcionClientePagina";
		} else {
			return "redirect:/recepcion";
		}
	}

	@PostMapping("/pagoSuscripcionClienteGeneracionPago")
	public String generarFormularioPago(Model model, Authentication authentication) {

		var usuarioSesion = (UsuarioSesion) authentication.getPrincipal();
		var cliente = clienteServicio.buscarClientePorId(usuarioSesion.getClienteId());
		var solicitudPagoInicial = cobroServicio.generarSolicitudPagoInicialDesdeCliente(cliente);
		model.addAttribute("solicitudPagoInicial", solicitudPagoInicial);

		return "cliente/pagoSuscripcionClientePagina :: pagoFormularioFragment";

	}

	@PostMapping("/comprobarPasswordAnterior/{password}")
	@ResponseBody
	public Boolean comprobarPasswordAnterior(@PathVariable("password") String password, Authentication authentication) {
		return usuarioServicio.comprobarPassword(password);
	}

	@GetMapping("/recepcion")
	public String recepcion(Model model, Authentication authentication, Locale locale) {
		var usuarioSesion = (UsuarioSesion) authentication.getPrincipal();
		var cliente = clienteServicio.buscarClientePorId(usuarioSesion.getClienteId());
		var conProblemasPagoRenovacion = false;
		var conProblemasPagoInicial = false;
		var sinEntrenadorSinrutinas = false;
		var conEntrenadorNuevoSinrutinas = false;
		var conEntrenadorEsperandoNuevasRutinas = false;
		var conEntrenadorConrutinas = false;
		var conEntrenadorSinRutinas = false;
		var mostrarBotonVideoconferencia = false;
		var suscripcionCaducada = false;
		List<Long> rutinasHoyIdLista = new ArrayList<>();
		List<String> rutinasHoyNombreLista = new ArrayList<>();

		List<Rutina> rutinasPreparadasActuales = null;

		// Identificar situacion actual

		// Caducada
		if (cliente.getSuscripcion().getEstado().getCodigo().equals(SuscripcionEstadoEnum.CADUCADA.codigo())) {
			suscripcionCaducada = true;
		} else {
			// Sin suscripcion por problemas con el pago renovacion
			if (cliente.getSuscripcion().getEstado().getCodigo().equals(SuscripcionEstadoEnum.IMPAGADA.codigo())) {
				conProblemasPagoRenovacion = true;
			}

			// Sin suscripcion por problemas con el pago inicial
			if (cliente.getSuscripcion().getEstado().getCodigo().equals(SuscripcionEstadoEnum.IMPAGADA_INICIAL.codigo())) {
				conProblemasPagoInicial = true;
			}

			// Sin entrenador ni rutinas
			if (cliente.getEntrenador() == null) {
				sinEntrenadorSinrutinas = true;
			}

			// Con entrenador
			if (cliente.getEntrenador() != null) {

				mostrarBotonVideoconferencia = true;
				// Rutinas activas del periodo actual
				rutinasPreparadasActuales = cliente.getRutinaLista().stream().filter((it) -> (it.getEstado().getCodigo().equals(RutinaEstadoEnum.ACTIVA.codigo()))
						&& it.getFechaCreacion().isEqual(cliente.getSuscripcion().getFechaPeriodoDesde())).collect(Collectors.toList());

				// Solo mostrarlas si estan las 4 activas
				if (rutinasPreparadasActuales.size() == 4) {
					conEntrenadorConrutinas = true;
				} else if (cliente.getSuscripcion().getEstado().getCodigo().equals(SuscripcionEstadoEnum.ACTIVA_NUEVA.codigo())
						|| cliente.getSuscripcion().getEstado().getCodigo().equals(SuscripcionEstadoEnum.IMPAGADA_INICIAL.codigo())) {
					conEntrenadorNuevoSinrutinas = true;
				} else if (cliente.getSuscripcion().getEstado().getCodigo().equals(SuscripcionEstadoEnum.ACTIVA.codigo())
						|| cliente.getSuscripcion().getEstado().getCodigo().equals(SuscripcionEstadoEnum.IMPAGADA.codigo())
						|| cliente.getSuscripcion().getEstado().getCodigo().equals(SuscripcionEstadoEnum.CANCELADA.codigo())) {
					conEntrenadorEsperandoNuevasRutinas = true;
				}
			}

			// Para presentar las rutinas si hay
			if (conEntrenadorConrutinas) {
				int numeroEntrenamiento = 1;
				Map<Long, List<String>> rutinaFotosMapa = new HashMap<>();
				for (Rutina rutina : rutinasPreparadasActuales) {
					List<String> fotos = new ArrayList<>();
					rutinaFotosMapa.put(rutina.getId(), fotos);
					for (RutinaFaseGrupoRepeticionEjercicio rutinaFaseGrupoRepeticionEjercicio : rutinaServicio.agruparRutinaFaseGrupoEjercicioSinRepeticiones(rutina)) {
						fotos.add(rutinaFaseGrupoRepeticionEjercicio.getEjercicio().getId().toString());
						if (fotos.size() == 4) {
							break;
						}
					}

					if (fotos.size() < 4) {
						for (int i = fotos.size(); i < 4; i++) {
							fotos.add(FOTO_POR_DEFECTO);
						}
					}
					ZonedDateTime ahora = ZonedDateTime.now();
					for (ZonedDateTime rutinaFecha : rutina.getRutinaFechaLista()) {
						if (rutinaFecha.toLocalDate().isEqual(ahora.toLocalDate())) {
							rutinasHoyIdLista.add(rutina.getId());
							rutinasHoyNombreLista.add(rutina.getNombre());
						}
					}
					numeroEntrenamiento++;
				}

				model.addAttribute("rutinasActuales", rutinasPreparadasActuales);

				model.addAttribute("rutinaFotosMapa", rutinaFotosMapa);
			}
		}

		model.addAttribute("rutinasHoyIdLista", rutinasHoyIdLista);
		model.addAttribute("rutinasHoyNombreLista", rutinasHoyNombreLista);

		model.addAttribute("conProblemasPagoInicial", conProblemasPagoInicial);
		model.addAttribute("conProblemasPagoRenovacion", conProblemasPagoRenovacion);
		model.addAttribute("sinEntrenadorSinrutinas", sinEntrenadorSinrutinas);
		model.addAttribute("conEntrenadorNuevoSinrutinas", conEntrenadorNuevoSinrutinas);
		model.addAttribute("conEntrenadorEsperandoNuevasRutinas", conEntrenadorEsperandoNuevasRutinas);
		model.addAttribute("conEntrenadorConrutinas", conEntrenadorConrutinas);
		model.addAttribute("conEntrenadorSinRutinas", conEntrenadorSinRutinas);
		model.addAttribute("mostrarBotonVideoconferencia", mostrarBotonVideoconferencia);
		model.addAttribute("suscripcionCaducada", suscripcionCaducada);

		model.addAttribute("cliente", cliente);

		model.addAttribute("nuevasCondicionesFormulario", new NuevasCondicionesFormulario());

		return "cliente/recepcionPagina";
	}

	@GetMapping("/previsualizarEntrenamiento/{rutinaId}")
	public String previsualizarEntrenamiento(@PathVariable("rutinaId") Long rutinaId, Model model, Authentication authentication) {

		var rutina = rutinaServicio.buscarRutinaPorId(rutinaId);

		model.addAttribute("rutina", rutina);
		model.addAttribute("rutinaFaseGrupoRepeticionEjercicioLista", rutinaServicio.agruparRutinaFaseGrupoEjercicio(rutina));
		model.addAttribute("rutinaFaseGrupoRepeticionEjercicioSinRepeticionesLista", rutinaServicio.agruparRutinaFaseGrupoEjercicioSinRepeticiones(rutina));
		model.addAttribute("materialNecesario", rutinaServicio.agruparRutinaFaseGrupoEjercicioMaterial(rutina));

		return "cliente/entrenamientoPrevisualizacionPagina";
	}

	@GetMapping("/iniciarEntrenamiento/{rutinaId}")
	public String iniciarEntrenamiento(@PathVariable("rutinaId") Long rutinaId, Model model, Authentication authentication) {

		var rutinaEjecucion = rutinaServicio.iniciarRutinaEjecucionDesdeInicio(rutinaId);

		model.addAttribute("rutinaEjecucion", rutinaEjecucion);
		var configurarRutinaFormulario = new ConfigurarRutinaFormulario();
		var rutina = rutinaEjecucion.getRutina();
		configurarRutinaFormulario.setRutinaId(rutinaId);
		configurarRutinaFormulario.setEjecucionAutomatica(rutina.getClienteEjecucionAutomatica());
		configurarRutinaFormulario.setTiempoDescansoEntreEjercicios(rutina.getClienteTiempoDescansoEntreEjercicios());
		configurarRutinaFormulario.setTiempoDescansoEntreUnilaterales(rutina.getEntrenadorTiempoDescansoEntreUnilaterales());
		configurarRutinaFormulario.setTiempoEjercicioPorRepeticiones(rutina.getClienteTiempoEjercicioPorRepeticiones());
		model.addAttribute("configurarRutinaFormulario", configurarRutinaFormulario);

		return "cliente/entrenamientoPagina";
	}

	@GetMapping("/iniciarEntrenamientoEnEjercicioConcreto/{rutinaId}/{rutinaFaseGrupoEjercicioId}")
	public String iniciarEntrenamientoEnEjercicioConcreto(@PathVariable Long rutinaId, @PathVariable Long rutinaFaseGrupoEjercicioId, Model model,
			Authentication authentication) {

		var rutinaEjecucion = rutinaServicio.iniciarRutinaEnEjercicioConcreto(rutinaId, rutinaFaseGrupoEjercicioId);

		model.addAttribute("rutinaEjecucion", rutinaEjecucion);
		var configurarRutinaFormulario = new ConfigurarRutinaFormulario();
		var rutina = rutinaEjecucion.getRutina();
		configurarRutinaFormulario.setRutinaId(rutinaId);
		configurarRutinaFormulario.setEjecucionAutomatica(rutina.getClienteEjecucionAutomatica());
		configurarRutinaFormulario.setTiempoDescansoEntreEjercicios(rutina.getClienteTiempoDescansoEntreEjercicios());
		configurarRutinaFormulario.setTiempoDescansoEntreUnilaterales(rutina.getEntrenadorTiempoDescansoEntreUnilaterales());
		configurarRutinaFormulario.setTiempoEjercicioPorRepeticiones(rutina.getClienteTiempoEjercicioPorRepeticiones());
		model.addAttribute("configurarRutinaFormulario", configurarRutinaFormulario);

		return "cliente/entrenamientoPagina";
	}

	@PostMapping("/cambiarEjercicio/{rutinaId}/{rutinaFaseGrupoEjercicioId}")
	@ResponseBody
	public RutinaEjecucion cambiarEjercicio(@PathVariable Long rutinaId, @PathVariable Long rutinaFaseGrupoEjercicioId, Authentication authentication, Model model) {
		return rutinaServicio.cambiarEjercicioDeRutinaEnEjecucion(rutinaId, rutinaFaseGrupoEjercicioId);
	}

	@GetMapping("/finalizarEntrenamiento/{rutinaId}")
	public String finalizarEntrenamiento(@PathVariable("rutinaId") Long rutinaId, Authentication authentication, Model model) {
		UsuarioSesion usuarioSesion = (UsuarioSesion) authentication.getPrincipal();
		Cliente cliente = clienteServicio.buscarClientePorId(usuarioSesion.getClienteId());

		model.addAttribute("cliente", cliente);
		model.addAttribute("rutinaId", rutinaId);
		return "cliente/entrenamientoFinalizadoPagina";
	}

	@PostMapping("/valorarRutinaFormulario")
	public Object valorarRutinaFormulario(@RequestParam("rutinaId") Long rutinaId, @RequestParam("valoracion") String valoracion, Authentication authentication,
			Model model, RedirectAttributes attributes) {
		UsuarioSesion usuarioSesion = (UsuarioSesion) authentication.getPrincipal();
		rutinaServicio.valorarRutina(usuarioSesion.getClienteId(), rutinaId, valoracion);

		if (StringUtils.isNotBlank(valoracion)) {
			attributes.addFlashAttribute("mensajeExito", "¡Tu mensaje ha sido enviado con éxito!");
		}

		return new RedirectView("/");

	}

	@GetMapping("/videoconferencia")
	public String videoconferencia(Model model, Authentication authentication) {
		UsuarioSesion usuarioSesion = (UsuarioSesion) authentication.getPrincipal();

		Videoconferencia videoconferencia = clienteServicio.buscarClientePorId(usuarioSesion.getClienteId()).getVideoConferenciaLista().stream()
				.filter((it) -> !it.getEstado().getCodigo().equals(VideoconferenciaEstadoEnum.CADUCADA.codigo())).findFirst().orElse(null);

		model.addAttribute("videoconferencia", videoconferencia);

		if (videoconferencia.getRutinaId() != null) {
			var rutina = rutinaServicio.buscarRutinaPorId(videoconferencia.getRutinaId());
			model.addAttribute("rutina", rutina);
			model.addAttribute("rutinaFaseGrupoRepeticionEjercicioLista", rutinaServicio.agruparRutinaFaseGrupoEjercicio(rutina));
			model.addAttribute("rutinaFaseGrupoRepeticionEjercicioSinRepeticionesLista", rutinaServicio.agruparRutinaFaseGrupoEjercicioSinRepeticiones(rutina));
			model.addAttribute("materialNecesario", rutinaServicio.agruparRutinaFaseGrupoEjercicioMaterial(rutina));
		}

		return "cliente/videoconferenciaSalaClientePagina";
	}

	@PostMapping("/comprobarVideoconferenciaRutina/{videoconferenciaId}/{rutinaId}")
	@ResponseBody
	public boolean comprobarVideoconferenciaRutina(@PathVariable("videoconferenciaId") Long videoconferenciaId, @PathVariable("rutinaId") Long rutinaId, Model model,
			Authentication authentication) {

		var videoconferencia = videoconferenciaServicio.buscarVideoConferenciaPorId(videoconferenciaId);

		return videoconferencia.getRutinaId() == null || !videoconferencia.getRutinaId().equals(rutinaId);
	}

	@PostMapping("/obtenerVideoconferenciaRutina/{videoconferenciaId}")
	public String obtenerVideoconferenciaRutina(@PathVariable("videoconferenciaId") Long videoconferenciaId, Model model, Authentication authentication) {

		var videoconferencia = videoconferenciaServicio.buscarVideoConferenciaPorId(videoconferenciaId);
		model.addAttribute("videoconferencia", videoconferencia);

		if (videoconferencia.getRutinaId() != null) {
			var rutina = rutinaServicio.buscarRutinaPorId(videoconferencia.getRutinaId());
			model.addAttribute("rutina", rutina);
			model.addAttribute("rutinaFaseGrupoRepeticionEjercicioLista", rutinaServicio.agruparRutinaFaseGrupoEjercicio(rutina));
			model.addAttribute("rutinaFaseGrupoRepeticionEjercicioSinRepeticionesLista", rutinaServicio.agruparRutinaFaseGrupoEjercicioSinRepeticiones(rutina));
			model.addAttribute("materialNecesario", rutinaServicio.agruparRutinaFaseGrupoEjercicioMaterial(rutina));
		}

		return "cliente/videoconferenciaSalaClientePagina :: videoconferenciaRutinaFragment";
	}

	@GetMapping("/miSuscripcion")
	public String miSuscripcion(Model model, Authentication authentication) {
		UsuarioSesion usuarioSesion = (UsuarioSesion) authentication.getPrincipal();
		model.addAttribute("cliente", clienteServicio.buscarClientePorId(usuarioSesion.getClienteId()));

		return "cliente/miSuscripcionPagina";
	}

	@GetMapping("/miConfiguracion")
	public String miConfiguracion(Model model, Authentication authentication) {
		var usuarioSesion = (UsuarioSesion) authentication.getPrincipal();
		model.addAttribute("clienteConfiguracion", clienteServicio.buscarClienteConfiguracionPorClienteId(usuarioSesion.getClienteId()));
		return "cliente/miConfiguracionPagina";
	}

	@PostMapping("/clienteConfiguracionFormulario")
	@ResponseBody
	public boolean clienteConfiguracionFormulario(@ModelAttribute ClienteConfiguracion clienteConfiguracion, Authentication authentication) {
		clienteServicio.actualizarClienteConfiguracion(clienteConfiguracion);
		return true;
	}

	@PostMapping("/configurarRutinaFormulario")
	@ResponseBody
	public boolean configurarRutinaFormulario(@ModelAttribute ConfigurarRutinaFormulario configurarRutinaFormulario, Authentication authentication) {
		rutinaServicio.configurarRutinaFormulario(configurarRutinaFormulario);
		return true;
	}

	@GetMapping("/misDatos")
	public String misDatos(Model model, Authentication authentication) {
//		migracionServicio.migrarEjercicios();

		var usuarioSesion = (UsuarioSesion) authentication.getPrincipal();

		model.addAttribute("diaEntrenamientoSeleccionLista", seleccion(ValorMaestroTipoEnum.DIA_ENTRENAMIENTO));
		model.addAttribute("perfilSeleccionLista", seleccion(ValorMaestroTipoEnum.PERFIL));
		model.addAttribute("lugarEntrenamientoSeleccionLista", seleccion(ValorMaestroTipoEnum.LUGAR_ENTRENAMIENTO));
		model.addAttribute("materialSeleccionLista", ejercicioServicio.buscarMaterialTodos());
		model.addAttribute("sexoSeleccionLista", seleccion(ValorMaestroTipoEnum.SEXO));
		model.addAttribute("medioConocimientoSeleccionLista", seleccion(ValorMaestroTipoEnum.MEDIO_CONOCIMIENTO));

		model.addAttribute("cliente", clienteServicio.buscarClientePorId(usuarioSesion.getClienteId()));

		return "cliente/misDatosPagina";
	}

	@PostMapping("/actualizarClienteDatos")
	public Object actualizarClienteDatos(@ModelAttribute("cliente") Cliente cliente, BindingResult result, Model model, RedirectAttributes attributes) {

		var clienteActualizado = clienteServicio.actualizarClienteDesdeMisDatosPagina(cliente);

		model.addAttribute("diaEntrenamientoSeleccionLista", seleccion(ValorMaestroTipoEnum.DIA_ENTRENAMIENTO));
		model.addAttribute("perfilSeleccionLista", seleccion(ValorMaestroTipoEnum.PERFIL));
		model.addAttribute("lugarEntrenamientoSeleccionLista", seleccion(ValorMaestroTipoEnum.LUGAR_ENTRENAMIENTO));
		model.addAttribute("materialSeleccionLista", ejercicioServicio.buscarMaterialTodos());
		model.addAttribute("sexoSeleccionLista", seleccion(ValorMaestroTipoEnum.SEXO));
		model.addAttribute("medioConocimientoSeleccionLista", seleccion(ValorMaestroTipoEnum.MEDIO_CONOCIMIENTO));

		model.addAttribute("cliente", clienteActualizado);

		if (result.hasErrors()) {
			return "cliente/misDatosPagina";
		}

		attributes.addFlashAttribute("mensajeExito", "¡Tus cambios se han guardado con éxito!");
		return new RedirectView("/misDatos");
	}

	@GetMapping("/miEntrenador")
	public String miEntrenador(Model model, Authentication authentication) {
		UsuarioSesion usuarioSesion = (UsuarioSesion) authentication.getPrincipal();
		Cliente cliente = clienteServicio.buscarClientePorId(usuarioSesion.getClienteId());
		model.addAttribute("cliente", cliente);
		return "cliente/miEntrenadorPagina";
	}

	@GetMapping("/misEntrenamientos")
	public String misEntrenamientos(Model model, Authentication authentication) {

		UsuarioSesion usuarioSesion = (UsuarioSesion) authentication.getPrincipal();
		Cliente cliente = clienteServicio.buscarClientePorId(usuarioSesion.getClienteId());

		Map<ZonedDateTime, List<Rutina>> fechaRutinaMapa = new HashMap<>();
		Map<Long, List<String>> rutinaFotosMapa = new HashMap<>();

		// Impagado no mostramos todos los entrenamientos, si no podria estar intentando
		// renovar seguido y aprovechar ahi las 48 horas. Caducado obviamente ya nada.
		if (!cliente.getSuscripcion().getEstado().getCodigo().equals(SuscripcionEstadoEnum.IMPAGADA.codigo())
				&& !cliente.getSuscripcion().getEstado().getCodigo().equals(SuscripcionEstadoEnum.CADUCADA.codigo())) {

			// Solo entrenamientos dos meses anteriors
			var fechaReferencia = ZonedDateTime.now().minusMonths(2);

			// Todas menos elaboracion desde hace dos meses
			var rutinasPreparadas = cliente.getRutinaLista().stream()
					.filter((it) -> !it.getEstado().getCodigo().equals(RutinaEstadoEnum.EN_ELABORACION.codigo()) && it.getFechaCreacion().isAfter(fechaReferencia))
					.collect(Collectors.toList());

			// 1. Agrupar por fecha
			for (Rutina rutina : rutinasPreparadas) {
				List<Rutina> rutinaLista = fechaRutinaMapa.get(rutina.getFechaCreacion());
				if (rutinaLista == null) {
					rutinaLista = new ArrayList<>();
					fechaRutinaMapa.put(rutina.getFechaCreacion(), rutinaLista);
				}
				rutinaLista.add(rutina);
			}

			// 2. Para cada rutina vamos a seleccionar ya aqui sus fotos de presentacion
			for (Rutina rutina : cliente.getRutinaLista()) {
				List<String> fotos = new ArrayList<>();
				rutinaFotosMapa.put(rutina.getId(), fotos);
				for (RutinaFaseGrupoRepeticionEjercicio rutinaFaseGrupoRepeticionEjercicio : rutinaServicio.agruparRutinaFaseGrupoEjercicioSinRepeticiones(rutina)) {
					fotos.add(rutinaFaseGrupoRepeticionEjercicio.getEjercicio().getId().toString());
					if (fotos.size() == 4) {
						break;
					}
				}

				if (fotos.size() < 4) {
					for (int i = fotos.size(); i < 4; i++) {
						fotos.add(FOTO_POR_DEFECTO);
					}
				}
			}

		}

		model.addAttribute("fechaRutinaMapa", fechaRutinaMapa);
		model.addAttribute("rutinaFotosMapa", rutinaFotosMapa);
		model.addAttribute("cliente", cliente);

		return "cliente/misEntrenamientosPagina";
	}

	@GetMapping("/miCuenta")
	public String miCuenta(Model model, Authentication authentication) {

		model.addAttribute("usuarioCambioPasswordFormulario", new UsuarioCambioPasswordFormulario());

		return "cliente/miCuentaPagina";
	}

	@PostMapping("/cambiarPassword")
	public Object cambiarPassword(@ModelAttribute("usuarioCambioPasswordFormulario") UsuarioCambioPasswordFormulario usuarioCambioPasswordFormulario,
			BindingResult result, Model model, Authentication authentication, Locale locale, RedirectAttributes attributes) {

		// Comprobar validez de password previa
		if (result.hasErrors()) {
			return "cliente/miCuentaPagina";
		}

		usuarioServicio.cambiarPassword(usuarioCambioPasswordFormulario.getPassword());

		attributes.addFlashAttribute("mensajeExito", "¡Contraseña cambiada con éxito!");
		return new RedirectView("/");

	}

	@GetMapping("/cancelarSuscripcion")
	public String cancelarSuscripcion(Model model, Authentication authentication) {
		UsuarioSesion usuarioSesion = (UsuarioSesion) authentication.getPrincipal();
		clienteServicio.cancelarSuscripcion(usuarioSesion.getClienteId());
		model.addAttribute("cliente", clienteServicio.buscarClientePorId(usuarioSesion.getClienteId()));
		return "redirect:/miSuscripcion";
	}

	@GetMapping("/suspenderCancelacionSuscripcion")
	public String suspenderCancelacionSuscripcion(Model model, Authentication authentication) {
		UsuarioSesion usuarioSesion = (UsuarioSesion) authentication.getPrincipal();
		clienteServicio.suspenderCancelacionSuscripcion(usuarioSesion.getClienteId());
		model.addAttribute("cliente", clienteServicio.buscarClientePorId(usuarioSesion.getClienteId()));
		return "redirect:/miSuscripcion";
	}

	@PostMapping("/pagarSuscripcion")
	@ResponseBody
	public Boolean pagarSuscripcion(Model model, Authentication authentication) {
		UsuarioSesion usuarioSesion = (UsuarioSesion) authentication.getPrincipal();
		clienteServicio.suspenderCancelacionSuscripcion(usuarioSesion.getClienteId());
		return true;
	}

	@PostMapping("/contactoEntrenadorFormulario")
	public Object generarFormularioPago(@RequestParam("mensaje") String mensaje, Model model, Authentication authentication, RedirectAttributes attributes) {
		UsuarioSesion usuarioSesion = (UsuarioSesion) authentication.getPrincipal();
		clienteServicio.enviarMensajeEntrenador(usuarioSesion.getClienteId(), mensaje);

		attributes.addFlashAttribute("mensajeExito", "¡Tu mensaje ha sido enviado con éxito!");
		return new RedirectView("/miEntrenador");
	}

	@GetMapping("/avisoLegalCliente")
	public String avisoLegal(Model model) {
		return "cliente/avisoLegalClientePagina";
	}

	@GetMapping("/terminosCondicionesCliente")
	public String terminosCondiciones(Model model) {
		return "cliente/terminosCondicionesClientePagina";
	}

	@GetMapping("/politicaPrivacidadCliente")
	public String politicaPrivacidad(Model model) {
		return "cliente/politicaPrivacidadClientePagina";
	}

	@GetMapping("/politicaCookiesCliente")
	public String politicaCookies(Model model) {
		return "cliente/politicaCookiesClientePagina";
	}

	@PostMapping("/nuevasCondicionesFormulario")
	public String nuevasCondicionesFormulario(@ModelAttribute("nuevasCondicionesFormulario") NuevasCondicionesFormulario nuevasCondicionesFormulario,
			Authentication authentication) {
		var usuarioSesion = (UsuarioSesion) authentication.getPrincipal();
		if (nuevasCondicionesFormulario.isAceptaPoliticaPrivacidad() && nuevasCondicionesFormulario.isAceptaTerminosCondiciones()) {
			clienteServicio.aceptaPoliticaPrivacidad(usuarioSesion.getClienteId());
			clienteServicio.aceptaTerminosCondiciones(usuarioSesion.getClienteId());
		}

		return "redirect:/recepcion";
	}

}
