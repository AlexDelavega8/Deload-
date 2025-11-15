package es.pocketrainer.controlador;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.util.IOUtils;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import es.pocketrainer.formulario.BuscarClienteFormulario;
import es.pocketrainer.formulario.BuscarEntrenadorFormulario;
import es.pocketrainer.formulario.BuscarPlantillaFormulario;
import es.pocketrainer.formulario.BuscarSuscripcionCobroListaFormulario;
import es.pocketrainer.formulario.UsuarioCambioPasswordFormulario;
import es.pocketrainer.modelo.ejercicio.Ejercicio;
import es.pocketrainer.modelo.ejercicio.Material;
import es.pocketrainer.modelo.entrenador.Entrenador;
import es.pocketrainer.modelo.rutina.Rutina;
import es.pocketrainer.modelo.rutina.RutinaFaseGrupo;
import es.pocketrainer.modelo.rutina.RutinaFaseGrupoRepeticionEjercicio;
import es.pocketrainer.modelo.valormaestro.ValorMaestro;
import es.pocketrainer.modelo.videoconferencia.Videoconferencia;
import es.pocketrainer.repositorio.ejercicio.BuscarEjercicioFormulario;
import es.pocketrainer.seguridad.UsuarioSesion;
import es.pocketrainer.servicio.cliente.ClienteServicio;
import es.pocketrainer.servicio.cobro.CobroServicio;
import es.pocketrainer.servicio.ejercicio.EjercicioServicio;
import es.pocketrainer.servicio.entrenador.EntrenadorServicio;
import es.pocketrainer.servicio.migracion.MigracionServicio;
import es.pocketrainer.servicio.rutina.EjercicioUltimoUsoCache;
import es.pocketrainer.servicio.rutina.RutinaServicio;
import es.pocketrainer.servicio.usuario.UsuarioServicio;
import es.pocketrainer.servicio.valormaestro.ValorMaestroServicio;
import es.pocketrainer.servicio.videoconferencia.VideoconferenciaServicio;
import es.pocketrainer.util.Constantes;
import es.pocketrainer.util.Constantes.ClienteEstadoEnum;
import es.pocketrainer.util.Constantes.EjercicioUltimoUsoTipoEnum;
import es.pocketrainer.util.Constantes.EntrenadorEstadoEnum;
import es.pocketrainer.util.Constantes.SuscripcionEstadoEnum;
import es.pocketrainer.util.Constantes.ValorMaestroTipoEnum;
import es.pocketrainer.vista.ClienteVista;

@Controller
@SessionAttributes(value = { "ejercicioBusquedaFiltro", "buscarSuscripcionCobroListaFormulario", "rutinaFaseGrupoRepeticionEjercicioHistorial" })
public class EntrenadorControlador {

	@Resource
	private UsuarioServicio usuarioServicio;

	@Resource
	private ClienteServicio clienteServicio;

	@Resource
	private RutinaServicio rutinaServicio;

	@Resource
	private EjercicioServicio ejercicioServicio;

	@Resource
	private EntrenadorServicio entrenadorServicio;

	@Resource
	private ValorMaestroServicio valorMaestroServicio;

	@Resource
	private VideoconferenciaServicio videoconferenciaServicio;

	@Resource
	private MigracionServicio migracionServicio;

	@Resource
	private CobroServicio cobroServicio;

	@Resource
	private EjercicioUltimoUsoCache ejercicioUltimoUsoCache;

	@GetMapping("/miCuentaEntrenador")
	public String miCuenta(Model model, Authentication authentication) {

		model.addAttribute("usuarioCambioPasswordFormulario", new UsuarioCambioPasswordFormulario());

		return "entrenador/miCuentaEntrenadorPagina";
	}

	@PostMapping("/cambiarPasswordEntrenador")
	public String cambiarPassword(@ModelAttribute("usuarioCambioPasswordFormulario") UsuarioCambioPasswordFormulario usuarioCambioPasswordFormulario,
			BindingResult result, Model model, Authentication authentication, Locale locale) {

		// Comprobar validez de password previa
		if (result.hasErrors()) {
			return "entrenador/miCuentaEntrenadorPagina";
		}

		usuarioServicio.cambiarPassword(usuarioCambioPasswordFormulario.getPassword());

		return "redirect:/";
	}

	@PostMapping("/reiniciarFiltrosElaboracionRutinaAccion/{rutinaId}")
	public String reiniciarFiltrosElaboracionRutinaAccion(@PathVariable("rutinaId") Long rutinaId, Model model) {

		var rutina = rutinaServicio.buscarRutinaPorId(rutinaId);

		var ejercicioBusquedaFiltro = new BuscarEjercicioFormulario();
		ejercicioBusquedaFiltro.setFetchMaterial(true);
		ejercicioBusquedaFiltro.addEjercicioUltimoUsoTipo(EjercicioUltimoUsoTipoEnum.EJERCICIO_USO_CUATRO_DOCE_MESES.codigo());
		ejercicioBusquedaFiltro.setBusquedaPorFiltroCompleto(true);
		ejercicioBusquedaFiltro.setBusquedaPorFiltroSoloPorMaterial(false);
		ejercicioBusquedaFiltro.setRutinaId(rutinaId);
		ejercicioBusquedaFiltro.setFiltrarPorEjercicioUltimoUsoTipo(true);
		ejercicioBusquedaFiltro.setClasificarEjercicioSegunUsoPrevio(true);
		ejercicioBusquedaFiltro.setClienteId(rutina.getCliente().getId());

		model.addAttribute("rutina", rutina);
		model.addAttribute("patronLista", ejercicioServicio.buscarPatronTodos());
		model.addAttribute("musculoLista", ejercicioServicio.buscarMusculoTodos());
		model.addAttribute("parteCuerpoLista", ejercicioServicio.buscarParteCuerpoTodos());
		model.addAttribute("zonaLista", ejercicioServicio.buscarZonaTodas());
		model.addAttribute("materialSeleccionLista", ejercicioServicio.buscarMaterialTodos());
		model.addAttribute("ejercicioUltimoUsoTipoSeleccionLista", EjercicioUltimoUsoTipoEnum.values());
		if (ejercicioBusquedaFiltro.getMaterialIdLista() == null) {
			for (Material material : rutina.getCliente().getMaterialLista()) {
				ejercicioBusquedaFiltro.addMaterialId(material.getId());
			}
		}

		model.addAttribute("ejercicioBusquedaFiltro", ejercicioBusquedaFiltro);
		model.addAttribute("ejercicioLista", ejercicioServicio.buscarEjercicioPorFiltroParaElaboracionRutina(ejercicioBusquedaFiltro, false));
		return "entrenador/rutinaElaboracionPagina :: seccionFiltrosFragment";
	}

	@PostMapping("/reiniciarFiltrosElaboracionRutinaPlantillaAccion")
	public String reiniciarFiltrosElaboracionRutinaPlantillaAccion(Model model) {

		var ejercicioBusquedaFiltro = new BuscarEjercicioFormulario();
		ejercicioBusquedaFiltro.setFetchMaterial(false);
		ejercicioBusquedaFiltro.setClasificarEjercicioSegunUsoPrevio(false);
		ejercicioBusquedaFiltro.setFiltrarPorEjercicioUltimoUsoTipo(false);
		ejercicioBusquedaFiltro.setBusquedaPorFiltroCompleto(true);
		ejercicioBusquedaFiltro.setBusquedaPorFiltroSoloPorMaterial(false);

		model.addAttribute("patronLista", ejercicioServicio.buscarPatronTodos());
		model.addAttribute("musculoLista", ejercicioServicio.buscarMusculoTodos());
		model.addAttribute("parteCuerpoLista", ejercicioServicio.buscarParteCuerpoTodos());
		model.addAttribute("zonaLista", ejercicioServicio.buscarZonaTodas());
		model.addAttribute("materialSeleccionLista", ejercicioServicio.buscarMaterialTodos());

		model.addAttribute("ejercicioBusquedaFiltro", ejercicioBusquedaFiltro);
		model.addAttribute("ejercicioLista", ejercicioServicio.buscarEjercicioPorFiltroParaElaboracionRutina(ejercicioBusquedaFiltro, false));
		return "entrenador/rutinaPlantillaElaboracionPagina :: seccionFiltrosFragment";
	}

	@PostMapping("/zonaSeleccionado")
	public String zonaSeleccionado(@RequestBody Long zonaId, @ModelAttribute("ejercicioBusquedaFiltro") BuscarEjercicioFormulario ejercicioBusquedaFiltro, Model model) {
		ejercicioBusquedaFiltro.addZonaId(zonaId);
		model.addAttribute("ejercicioLista", ejercicioServicio.buscarEjercicioPorFiltroParaElaboracionRutina(ejercicioBusquedaFiltro, false));
		return "entrenador/rutinaElaboracionPagina :: ejercicioListaFragment";
	}

	@PostMapping("/iniciarBusquedaPorFiltros")
	public String iniciarBusquedaPorFiltros(@ModelAttribute("ejercicioBusquedaFiltro") BuscarEjercicioFormulario ejercicioBusquedaFiltro, Model model) {
		ejercicioBusquedaFiltro.setBusquedaPorFiltroCompleto(true);
		ejercicioBusquedaFiltro.setBusquedaPorFiltroSoloPorMaterial(false);

		model.addAttribute("ejercicioLista", ejercicioServicio.buscarEjercicioPorFiltroParaElaboracionRutina(ejercicioBusquedaFiltro, false));
		return "entrenador/rutinaElaboracionPagina :: ejercicioListaFragment";
	}

	@PostMapping("/iniciarBusquedaPorNombre")
	public String iniciarBusquedaPorNombre(@ModelAttribute("ejercicioBusquedaFiltro") BuscarEjercicioFormulario ejercicioBusquedaFiltro, Model model) {
		ejercicioBusquedaFiltro.setBusquedaPorFiltroCompleto(false);
		ejercicioBusquedaFiltro.setBusquedaPorFiltroSoloPorMaterial(true);

		var ejercicioBusquedaFiltroSoloPorMaterial = new BuscarEjercicioFormulario();
		ejercicioBusquedaFiltroSoloPorMaterial.setFetchMaterial(true);
		ejercicioBusquedaFiltroSoloPorMaterial.setMaterialIdLista(ejercicioBusquedaFiltro.getMaterialIdLista());
		ejercicioBusquedaFiltroSoloPorMaterial.setClienteId(ejercicioBusquedaFiltro.getClienteId());
		ejercicioBusquedaFiltroSoloPorMaterial.setFiltrarPorEjercicioUltimoUsoTipo(false);
		if (ejercicioBusquedaFiltro.getClienteId() != null) {
			ejercicioBusquedaFiltroSoloPorMaterial.setClasificarEjercicioSegunUsoPrevio(true);
		}
		ejercicioBusquedaFiltroSoloPorMaterial.setRutinaId(ejercicioBusquedaFiltro.getRutinaId());
		model.addAttribute("ejercicioLista", ejercicioServicio.buscarEjercicioPorFiltroParaElaboracionRutina(ejercicioBusquedaFiltroSoloPorMaterial, false));
		return "entrenador/rutinaElaboracionPagina :: ejercicioListaFragment";
	}

	@PostMapping("/patronSeleccionado")
	public String patronSeleccionado(@RequestBody Long patronId, @ModelAttribute("ejercicioBusquedaFiltro") BuscarEjercicioFormulario ejercicioBusquedaFiltro,
			Model model) {
		ejercicioBusquedaFiltro.setBusquedaPorFiltroCompleto(true);
		ejercicioBusquedaFiltro.setBusquedaPorFiltroSoloPorMaterial(false);
		ejercicioBusquedaFiltro.addPatronId(patronId);
		model.addAttribute("ejercicioLista", ejercicioServicio.buscarEjercicioPorFiltroParaElaboracionRutina(ejercicioBusquedaFiltro, false));
		return "entrenador/rutinaElaboracionPagina :: ejercicioListaFragment";
	}

	@PostMapping("/musculoSeleccionado")
	public String musculoSeleccionado(@RequestBody Long musculoId, @ModelAttribute("ejercicioBusquedaFiltro") BuscarEjercicioFormulario ejercicioBusquedaFiltro,
			Model model) {
		ejercicioBusquedaFiltro.setBusquedaPorFiltroCompleto(true);
		ejercicioBusquedaFiltro.setBusquedaPorFiltroSoloPorMaterial(false);
		ejercicioBusquedaFiltro.addMusculoId(musculoId);
		model.addAttribute("ejercicioLista", ejercicioServicio.buscarEjercicioPorFiltroParaElaboracionRutina(ejercicioBusquedaFiltro, false));
		return "entrenador/rutinaElaboracionPagina :: ejercicioListaFragment";
	}

	@PostMapping("/parteCuerpoSeleccionado")
	public String parteCuerpoSeleccionado(@RequestBody Long parteCuerpoId, @ModelAttribute("ejercicioBusquedaFiltro") BuscarEjercicioFormulario ejercicioBusquedaFiltro,
			Model model) {
		ejercicioBusquedaFiltro.setBusquedaPorFiltroCompleto(true);
		ejercicioBusquedaFiltro.setBusquedaPorFiltroSoloPorMaterial(false);
		ejercicioBusquedaFiltro.addParteCuerpoId(parteCuerpoId);
		model.addAttribute("ejercicioLista", ejercicioServicio.buscarEjercicioPorFiltroParaElaboracionRutina(ejercicioBusquedaFiltro, false));
		return "entrenador/rutinaElaboracionPagina :: ejercicioListaFragment";
	}

	@PostMapping("/materialSeleccionado")
	public String materialSeleccionado(@RequestBody Long materialId, @ModelAttribute("ejercicioBusquedaFiltro") BuscarEjercicioFormulario ejercicioBusquedaFiltro,
			Model model) {
		ejercicioBusquedaFiltro.setBusquedaPorFiltroCompleto(true);
		ejercicioBusquedaFiltro.setBusquedaPorFiltroSoloPorMaterial(false);
		ejercicioBusquedaFiltro.addMaterialId(materialId);
		model.addAttribute("ejercicioLista", ejercicioServicio.buscarEjercicioPorFiltroParaElaboracionRutina(ejercicioBusquedaFiltro, false));
		return "entrenador/rutinaElaboracionPagina :: ejercicioListaFragment";
	}

	@PostMapping("/materialTodoDeseleccionadoAccion")
	public String materialTodoDeseleccionadoAccion(@ModelAttribute("ejercicioBusquedaFiltro") BuscarEjercicioFormulario ejercicioBusquedaFiltro, Model model) {
		ejercicioBusquedaFiltro.setMaterialIdLista(new ArrayList<>());
		ejercicioBusquedaFiltro.setBusquedaPorFiltroCompleto(true);
		ejercicioBusquedaFiltro.setBusquedaPorFiltroSoloPorMaterial(false);
		model.addAttribute("ejercicioLista", ejercicioServicio.buscarEjercicioPorFiltroParaElaboracionRutina(ejercicioBusquedaFiltro, false));
		return "entrenador/rutinaElaboracionPagina :: ejercicioListaFragment";
	}

	@PostMapping("/ejercicioUltimoUsoTipoSeleccionado")
	public String ejercicioUltimoUsoTipoSeleccionado(@RequestBody String ejercicioUltimoUsoTipo,
			@ModelAttribute("ejercicioBusquedaFiltro") BuscarEjercicioFormulario ejercicioBusquedaFiltro, Model model) {
		ejercicioBusquedaFiltro.setBusquedaPorFiltroCompleto(true);
		ejercicioBusquedaFiltro.setBusquedaPorFiltroSoloPorMaterial(false);
		ejercicioBusquedaFiltro.addEjercicioUltimoUsoTipo(ejercicioUltimoUsoTipo);
		model.addAttribute("ejercicioLista", ejercicioServicio.buscarEjercicioPorFiltroParaElaboracionRutina(ejercicioBusquedaFiltro, false));
		return "entrenador/rutinaElaboracionPagina :: ejercicioListaFragment";
	}

	@PostMapping("/borrarEjercicio/{rutinaFaseGrupoEjercicioId}")
	@ResponseBody
	public void borrarEjercicio(@PathVariable("rutinaFaseGrupoEjercicioId") Long rutinaFaseGrupoEjercicioId) {
		rutinaServicio.borrarEjercicio(rutinaFaseGrupoEjercicioId);
	}

	@PostMapping("/anhadirEjercicio/{rutinaId}/{ejercicioId}/{rutinaFaseGrupoId}/{posicion}")
	public String anhadirEjercicio(@PathVariable("rutinaId") Long rutinaId, @PathVariable("ejercicioId") Long ejercicioId,
			@PathVariable("rutinaFaseGrupoId") Long rutinaFaseGrupoId, @PathVariable("posicion") Integer posicion,
			@ModelAttribute("ejercicioBusquedaFiltro") BuscarEjercicioFormulario ejercicioBusquedaFiltro, Model model, Authentication authentication) {

		var usuarioSesion = (UsuarioSesion) authentication.getPrincipal();

		var rutinaFaseGrupoRepeticionEjercicio = rutinaServicio.anhadirEjercicio(ejercicioBusquedaFiltro, usuarioSesion.getEntrenadorId(), rutinaId, ejercicioId,
				rutinaFaseGrupoId, posicion, ejercicioBusquedaFiltro.isClasificarEjercicioSegunUsoPrevio());

		model.addAttribute("rutina", rutinaServicio.buscarRutinaPorId(rutinaId));
		model.addAttribute("rutinaFase", rutinaFaseGrupoRepeticionEjercicio.getRutinaFaseGrupoRepeticion().getRutinaFaseGrupo().getRutinaFase());
		model.addAttribute("rutinaFaseGrupo", rutinaFaseGrupoRepeticionEjercicio.getRutinaFaseGrupoRepeticion().getRutinaFaseGrupo());
		model.addAttribute("rutinaFaseGrupoEjercicio", rutinaFaseGrupoRepeticionEjercicio);

		return "fragmentos/rutinaFaseFragment :: rutinaFaseGrupoEjercicio";
	}

	@PostMapping("/moverEjercicio/{rutinaId}/{rutinaFaseGrupoEjercicioId}/{rutinaFaseGrupoId}/{posicion}")
	@ResponseBody
	public void moverEjercicio(@PathVariable("rutinaId") Long rutinaId, @PathVariable("rutinaFaseGrupoEjercicioId") Long rutinaFaseGrupoEjercicioId,
			@PathVariable("rutinaFaseGrupoId") Long rutinaFaseGrupoId, @PathVariable("posicion") Integer posicion) {
		rutinaServicio.moverEjercicio(rutinaId, rutinaFaseGrupoEjercicioId, rutinaFaseGrupoId, posicion);
	}

	@PostMapping("/anhadirGrupo/{rutinaId}/{rutinaFaseId}")
	public String anhadirGrupo(@PathVariable("rutinaId") Long rutinaId, @PathVariable("rutinaFaseId") Long rutinaFaseId, Model model) {

		var rutinaFaseGrupo = rutinaServicio.anhadirGrupo(rutinaId, rutinaFaseId);

		model.addAttribute("rutina", rutinaServicio.buscarRutinaPorId(rutinaId));
		model.addAttribute("rutinaFase", rutinaFaseGrupo.getRutinaFase());
		model.addAttribute("rutinaFaseGrupo", rutinaFaseGrupo);

		return "fragmentos/rutinaFaseFragment :: rutinaFaseGrupo";
	}

	@PostMapping("/borrarGrupo/{rutinaId}/{rutinaFaseId}/{rutinaFaseGrupoId}")
	@ResponseBody
	public void borrarGrupo(@PathVariable("rutinaId") Long rutinaId, @PathVariable("rutinaFaseId") Long rutinaFaseId,
			@PathVariable("rutinaFaseGrupoId") Long rutinaFaseGrupoId, HttpServletRequest request, HttpServletResponse response) {

		rutinaServicio.borrarGrupo(rutinaId, rutinaFaseId, rutinaFaseGrupoId);

	}

	private List<ValorMaestro> seleccion(ValorMaestroTipoEnum valorMaestroTipoEnum) {
		return valorMaestroServicio.buscarValorMaestroListaPorCodigoTipo(valorMaestroTipoEnum);
	}

	@GetMapping("/entrenador")
	public String entrenador(Model model, Authentication authentication, Locale locale) {
		return "inicioEntrenadorPagina";
	}

	@PostMapping("/cambioAutomaticoEjerciciosRutinaEntera/{rutinaId}/{rutinaFaseGrupoRepeticionEjercicioIdLista}")
	public String cambioAutomaticoEjerciciosRutinaEntera(@PathVariable Long rutinaId, @PathVariable List<String> rutinaFaseGrupoRepeticionEjercicioIdLista,
			@ModelAttribute("ejercicioBusquedaFiltro") BuscarEjercicioFormulario buscarEjercicioFormulario,
			@ModelAttribute("rutinaFaseGrupoRepeticionEjercicioHistorial") Map<Long, List<Long>> rutinaFaseGrupoRepeticionEjercicioHistorial, Model model,
			Authentication authentication, Locale locale) {

		var usuarioSesion = (UsuarioSesion) authentication.getPrincipal();
		var rutina = rutinaServicio.cambioAutomaticoEjerciciosRutinaEntera(usuarioSesion.getEntrenadorId(), rutinaId, rutinaFaseGrupoRepeticionEjercicioIdLista,
				buscarEjercicioFormulario, rutinaFaseGrupoRepeticionEjercicioHistorial);
		model.addAttribute("rutina", rutina);
		return "entrenador/rutinaElaboracionPagina :: elaboracionRutinaSeccionCentralFragment";
	}

	@PostMapping("/cambioAutomaticoEjerciciosRutinaFase/{rutinaId}/{rutinaFaseId}/{rutinaFaseGrupoRepeticionEjercicioIdLista}")
	public String cambioAutomaticoEjerciciosRutinaFase(@PathVariable Long rutinaId,
			@ModelAttribute("ejercicioBusquedaFiltro") BuscarEjercicioFormulario buscarEjercicioFormulario,
			@ModelAttribute("rutinaFaseGrupoRepeticionEjercicioHistorial") Map<Long, List<Long>> rutinaFaseGrupoRepeticionEjercicioHistorial,
			@PathVariable Long rutinaFaseId, @PathVariable List<String> rutinaFaseGrupoRepeticionEjercicioIdLista, Model model, Authentication authentication,
			Locale locale) {

		var usuarioSesion = (UsuarioSesion) authentication.getPrincipal();
		var rutina = rutinaServicio.cambioAutomaticoEjerciciosRutinaFase(usuarioSesion.getEntrenadorId(), rutinaId, rutinaFaseId,
				rutinaFaseGrupoRepeticionEjercicioIdLista, buscarEjercicioFormulario, rutinaFaseGrupoRepeticionEjercicioHistorial);
		var rutinaFase = rutina.getFaseInicial().getId().equals(rutinaFaseId) ? rutina.getFaseInicial()
				: (rutina.getFasePrincipal().getId().equals(rutinaFaseId) ? rutina.getFasePrincipal() : rutina.getFaseFinal());

		model.addAttribute("rutina", rutina);
		model.addAttribute("rutinaFase", rutinaFase);
		return "fragmentos/rutinaFaseFragment :: rutinaFase";
	}

	@PostMapping("/cambioAutomaticoEjerciciosRutinaFaseGrupo/{rutinaId}/{rutinaFaseId}/{rutinaFaseGrupoId}/{rutinaFaseGrupoRepeticionEjercicioIdLista}")
	public String cambioAutomaticoEjerciciosRutinaFaseGrupo(@PathVariable Long rutinaId,
			@ModelAttribute("ejercicioBusquedaFiltro") BuscarEjercicioFormulario buscarEjercicioFormulario,
			@ModelAttribute("rutinaFaseGrupoRepeticionEjercicioHistorial") Map<Long, List<Long>> rutinaFaseGrupoRepeticionEjercicioHistorial,
			@PathVariable Long rutinaFaseId, @PathVariable Long rutinaFaseGrupoId, @PathVariable List<String> rutinaFaseGrupoRepeticionEjercicioIdLista, Model model,
			Authentication authentication, Locale locale) {

		var usuarioSesion = (UsuarioSesion) authentication.getPrincipal();
		var rutina = rutinaServicio.cambioAutomaticoEjerciciosRutinaFaseGrupo(usuarioSesion.getEntrenadorId(), rutinaId, rutinaFaseId, rutinaFaseGrupoId,
				rutinaFaseGrupoRepeticionEjercicioIdLista, buscarEjercicioFormulario, rutinaFaseGrupoRepeticionEjercicioHistorial);
		var rutinaFase = rutina.getFaseInicial().getId().equals(rutinaFaseId) ? rutina.getFaseInicial()
				: (rutina.getFasePrincipal().getId().equals(rutinaFaseId) ? rutina.getFasePrincipal() : rutina.getFaseFinal());
		var rutinaFaseGrupo = rutinaFase.getRutinaFaseGrupoLista().stream().filter(it -> it.getId().equals(rutinaFaseGrupoId)).findFirst().orElse(null);
		model.addAttribute("rutina", rutinaServicio.buscarRutinaPorId(rutinaId));
		model.addAttribute("rutinaFase", rutinaFase);
		model.addAttribute("rutinaFaseGrupo", rutinaFaseGrupo);
		return "fragmentos/rutinaFaseFragment :: rutinaFaseGrupo";
	}

	@GetMapping("/rutinaElaboracion/{rutinaId}")
	public String rutinaElaboracionPagina(@PathVariable("rutinaId") Long rutinaId, Model model, Authentication authentication, Locale locale) {

		// Se crea siempre nuevo aqui y esta como atributo de sesion
		var ejercicioBusquedaFiltro = new BuscarEjercicioFormulario();
		ejercicioBusquedaFiltro.setFetchMaterial(true);
		ejercicioBusquedaFiltro.addEjercicioUltimoUsoTipo(EjercicioUltimoUsoTipoEnum.EJERCICIO_USO_CUATRO_DOCE_MESES.codigo());
		ejercicioBusquedaFiltro.setBusquedaPorFiltroCompleto(true);
		ejercicioBusquedaFiltro.setBusquedaPorFiltroSoloPorMaterial(false);
		ejercicioBusquedaFiltro.setRutinaId(rutinaId);
		ejercicioBusquedaFiltro.setFiltrarPorEjercicioUltimoUsoTipo(true);
		ejercicioBusquedaFiltro.setClasificarEjercicioSegunUsoPrevio(true);
		model.addAttribute("ejercicioBusquedaFiltro", ejercicioBusquedaFiltro);

		var rutina = rutinaServicio.buscarRutinaParaElaboracion(rutinaId);

		// Reiniciar cache de clasificación por colores
		var cliente = rutina.getCliente();
		ejercicioUltimoUsoCache.borrarCacheUnaEntrada(cliente.getId(), cliente.getSuscripcion().getFechaUltimaActivacion(), cliente.getSuscripcion().getMesesActiva(),
				cliente.getSuscripcion().getFechaPeriodoDesde(), rutinaId);
		ejercicioUltimoUsoCache.getClasificacionEjercicioUltimoUso(cliente.getId(), cliente.getSuscripcion().getFechaUltimaActivacion(),
				cliente.getSuscripcion().getMesesActiva(), cliente.getSuscripcion().getFechaPeriodoDesde(), rutinaId);

		ejercicioBusquedaFiltro.setClienteId(rutina.getCliente().getId());
		model.addAttribute("rutina", rutina);

		model.addAttribute("patronLista", ejercicioServicio.buscarPatronTodos());
		model.addAttribute("musculoLista", ejercicioServicio.buscarMusculoTodos());
		model.addAttribute("parteCuerpoLista", ejercicioServicio.buscarParteCuerpoTodos());
		model.addAttribute("zonaLista", ejercicioServicio.buscarZonaTodas());
		model.addAttribute("materialSeleccionLista", ejercicioServicio.buscarMaterialTodos());
		model.addAttribute("estadoSeleccionLista", seleccion(ValorMaestroTipoEnum.RUTINA_ESTADO));
		model.addAttribute("ejercicioUltimoUsoTipoSeleccionLista", Constantes.EjercicioUltimoUsoTipoEnum.values());
		model.addAttribute("diaEntrenamientoSeleccionLista", seleccion(ValorMaestroTipoEnum.DIA_ENTRENAMIENTO));
		model.addAttribute("lugarEntrenamientoSeleccionLista", seleccion(ValorMaestroTipoEnum.LUGAR_ENTRENAMIENTO));
		model.addAttribute("medioConocimientoSeleccionLista", seleccion(ValorMaestroTipoEnum.MEDIO_CONOCIMIENTO));

		List<ZonedDateTime> fechaEntrenamientoLista = new ArrayList<>();

		var fechaDesde = rutina.getCliente().getSuscripcion().getFechaPeriodoDesde();
		var fechaHasta = rutina.getCliente().getSuscripcion().getFechaPeriodoHasta();

		// Ojo, pueden ser rutinas ya del siguiente periodo
		if (!rutina.getCliente().getSuscripcion().getFechaPeriodoDesde().isEqual(rutina.getFechaCreacion())) {
			// Futuro periodo
			fechaDesde = clienteServicio.calcularProximaSuscripcionFechaPeriodoDesde(rutina.getCliente().getSuscripcion());
			fechaHasta = clienteServicio.calcularProximaSuscripcionFechaPeriodoHasta(rutina.getCliente().getSuscripcion());
		}

		var fechaEntrenamiento = fechaDesde;
		while (fechaEntrenamiento.isBefore(fechaHasta)) {
			Iterator<ValorMaestro> diaEntrenamientoIterator = rutina.getCliente().getDiaEntrenamientoLista().iterator();
			while (diaEntrenamientoIterator.hasNext()) {
				if (Integer.valueOf(diaEntrenamientoIterator.next().getValor()).equals(fechaEntrenamiento.getDayOfWeek().getValue())) {
					fechaEntrenamientoLista.add(fechaEntrenamiento);
					break;
				}
			}
			fechaEntrenamiento = fechaEntrenamiento.plusDays(1);
		}
		model.addAttribute("fechaEntrenamientoLista", fechaEntrenamientoLista);

		if (ejercicioBusquedaFiltro.getMaterialIdLista() == null) {
			for (Material material : rutina.getCliente().getMaterialLista()) {
				ejercicioBusquedaFiltro.addMaterialId(material.getId());
			}
		}

		model.addAttribute("rutinaFaseGrupoRepeticionEjercicioHistorial", new HashMap<Long, List<Long>>());

		model.addAttribute("ejercicioLista", ejercicioServicio.buscarEjercicioPorFiltroParaElaboracionRutina(ejercicioBusquedaFiltro, true));

		return "entrenador/rutinaElaboracionPagina";
	}

	@GetMapping("/videoconferenciaSalaEntrenador/{videoconferenciaId}")
	public String videoconferenciaSalaEntrenador(@PathVariable("videoconferenciaId") Long videoconferenciaId, Model model) {

		var videoconferencia = videoconferenciaServicio.buscarVideoConferenciaPorId(videoconferenciaId);
		model.addAttribute("videoconferencia", videoconferencia);

		var clienteVista = new ClienteVista(videoconferencia.getCliente());
		model.addAttribute("cliente", clienteVista);

		if (videoconferencia.getRutinaId() != null) {
			var rutina = rutinaServicio.buscarRutinaPorId(videoconferencia.getRutinaId());
			model.addAttribute("rutina", rutina);
			model.addAttribute("rutinaFaseGrupoRepeticionEjercicioLista", rutinaServicio.agruparRutinaFaseGrupoEjercicio(rutina));
			model.addAttribute("rutinaFaseGrupoRepeticionEjercicioSinRepeticionesLista", rutinaServicio.agruparRutinaFaseGrupoEjercicioSinRepeticiones(rutina));
			model.addAttribute("materialNecesario", rutinaServicio.agruparRutinaFaseGrupoEjercicioMaterial(rutina));
		}

		return "entrenador/videoconferenciaSalaEntrenadorPagina";
	}

	@PostMapping("/seleccionarVideoconferenciaRutina/{videoconferenciaId}/{rutinaId}")
	public String obtenerVideoconferenciaRutina(@PathVariable("videoconferenciaId") Long videoconferenciaId, @PathVariable("rutinaId") Long rutinaId, Model model,
			Authentication authentication) {

		Videoconferencia videoconferencia = null;
		if (rutinaId > 0) {
			videoconferencia = videoconferenciaServicio.actualizarVideoconferenciaRutina(videoconferenciaId, rutinaId);
			var rutina = rutinaServicio.buscarRutinaPorId(videoconferencia.getRutinaId());
			model.addAttribute("rutina", rutina);
			model.addAttribute("rutinaFaseGrupoRepeticionEjercicioLista", rutinaServicio.agruparRutinaFaseGrupoEjercicio(rutina));
			model.addAttribute("rutinaFaseGrupoRepeticionEjercicioSinRepeticionesLista", rutinaServicio.agruparRutinaFaseGrupoEjercicioSinRepeticiones(rutina));
			model.addAttribute("materialNecesario", rutinaServicio.agruparRutinaFaseGrupoEjercicioMaterial(rutina));
		} else {
			videoconferencia = videoconferenciaServicio.actualizarVideoconferenciaRutina(videoconferenciaId, null);
		}

		model.addAttribute("videoconferencia", videoconferencia);
		return "cliente/videoconferenciaSalaClientePagina :: videoconferenciaRutinaFragment";
	}

	@PostMapping("/videoconferenciaIniciada/{videoconferenciaId}")
	@ResponseBody
	public boolean videoconferenciaIniciada(@PathVariable("videoconferenciaId") Long videoconferenciaId, Model model) {
		model.addAttribute("videoconferencia", videoconferenciaServicio.establecerVideoconferenciaEnCurso(videoconferenciaId));
		return true;
	}

	@GetMapping("/videoconferenciaFinalizada/{videoconferenciaId}")
	public String videoconferenciaFinalizada(@PathVariable("videoconferenciaId") Long videoconferenciaId, Model model) {
		videoconferenciaServicio.establecerVideoconferenciaRealizada(videoconferenciaId);
		return "redirect:/";
	}

	@GetMapping("/clientes")
	public String clientes(Model model) {
		var buscarClienteFormulario = new BuscarClienteFormulario();
		buscarClienteFormulario.setEstadoCodigoLista(List.of(ClienteEstadoEnum.ACTIVO.codigo(), ClienteEstadoEnum.PENDIENTE_BAJA.codigo()));
		model.addAttribute("buscarClienteFormulario", buscarClienteFormulario);
		model.addAttribute("clienteLista", ClienteVista.transformarParaVista(clienteServicio.buscarClientePorFiltro(buscarClienteFormulario)));
		model.addAttribute("entrenadorSeleccionLista", entrenadorServicio.buscarEntrenadorActivoTodos());
		model.addAttribute("estadoSeleccionLista", seleccion(ValorMaestroTipoEnum.CLIENTE_ESTADO));
		model.addAttribute("suscripcionEstadoSeleccionLista", seleccion(ValorMaestroTipoEnum.SUSCRIPCION_ESTADDO));
		return "entrenador/clientesPagina";
	}

	@PostMapping("/buscarClienteFormulario")
	public String buscarClienteFormulario(@ModelAttribute BuscarClienteFormulario buscarClienteFormulario, Model model) {
		model.addAttribute("clienteLista", ClienteVista.transformarParaVista(clienteServicio.buscarClientePorFiltro(buscarClienteFormulario)));
		model.addAttribute("entrenadorSeleccionLista", entrenadorServicio.buscarEntrenadorActivoTodos());
		return "entrenador/clientesPagina :: clienteListaFragment";
	}

	@GetMapping("/misClientes")
	public String misClientes(Model model, Authentication authentication) {
		var usuarioSesion = (UsuarioSesion) authentication.getPrincipal();

		var buscarClienteFormulario = new BuscarClienteFormulario();
		buscarClienteFormulario.setEstadoCodigoLista(List.of(ClienteEstadoEnum.ACTIVO.codigo(), ClienteEstadoEnum.PENDIENTE_BAJA.codigo()));
		buscarClienteFormulario.setEntrenadorId(usuarioSesion.getEntrenadorId());
		buscarClienteFormulario.setSuscripcionEstadoCodigoLista(List.of(SuscripcionEstadoEnum.ACTIVA.codigo(), SuscripcionEstadoEnum.ACTIVA_NUEVA.codigo(),
				SuscripcionEstadoEnum.CANCELADA.codigo(), SuscripcionEstadoEnum.IMPAGADA.codigo()));
		model.addAttribute("suscripcionEstadoSeleccionLista", seleccion(ValorMaestroTipoEnum.SUSCRIPCION_ESTADDO));
		model.addAttribute("buscarClienteFormulario", buscarClienteFormulario);
		model.addAttribute("clienteLista", ClienteVista.transformarParaVista(clienteServicio.buscarClientePorFiltro(buscarClienteFormulario)));
		return "entrenador/misClientesPagina";
	}

	@PostMapping("/buscarClienteDesdeMisClientesFormulario")
	public String buscarClienteDesdeMisClientesFormulario(@ModelAttribute BuscarClienteFormulario buscarClienteFormulario, Model model, Authentication authentication) {
		var usuarioSesion = (UsuarioSesion) authentication.getPrincipal();

		buscarClienteFormulario.setEstadoCodigoLista(List.of(ClienteEstadoEnum.ACTIVO.codigo()));
		buscarClienteFormulario.setEntrenadorId(usuarioSesion.getEntrenadorId());
		model.addAttribute("clienteLista", ClienteVista.transformarParaVista(clienteServicio.buscarClientePorFiltro(buscarClienteFormulario)));
		return "entrenador/misClientesPagina :: clienteListaFragment";
	}

	@GetMapping("/facturacion")
	public String facturacion(Model model) {
		var suscripcionCobroBusquedaFiltro = new BuscarSuscripcionCobroListaFormulario();
		suscripcionCobroBusquedaFiltro.setMes(LocalDate.now());
		suscripcionCobroBusquedaFiltro.setAnho(LocalDate.now().getYear());
		model.addAttribute("suscripcionCobroLista", cobroServicio.buscarSuscripcionCobroPorFiltro(suscripcionCobroBusquedaFiltro));
		suscripcionCobroBusquedaFiltro.setFechaEmisionRangoInicio(null);
		suscripcionCobroBusquedaFiltro.setFechaEmisionRangoFin(null);
		model.addAttribute("buscarSuscripcionCobroListaFormulario", suscripcionCobroBusquedaFiltro);
		model.addAttribute("estadoSeleccionLista", seleccion(ValorMaestroTipoEnum.SUSCRIPCION_COBRO_ESTADO));
		return "entrenador/facturacionPagina";
	}

	@PostMapping("/buscarSuscripcionCobroListaFormulario")
	public String buscarFacturaListaFormulario(
			@ModelAttribute("buscarSuscripcionCobroListaFormulario") BuscarSuscripcionCobroListaFormulario buscarSuscripcionCobroListaFormulario, BindingResult result,
			Model model) {
		model.addAttribute("suscripcionCobroLista", cobroServicio.buscarSuscripcionCobroPorFiltro(buscarSuscripcionCobroListaFormulario));
		return "entrenador/facturacionPagina :: suscripcionCobroListaFragment";
	}

	@GetMapping(value = "/facturacion/exportacionCobros", produces = "application/vnd.ms-excel")
	public @ResponseBody byte[] exportacionCobros(
			@ModelAttribute("buscarSuscripcionCobroListaFormulario") BuscarSuscripcionCobroListaFormulario buscarSuscripcionCobroListaFormulario, Model model) {
		try {
			return IOUtils.toByteArray(cobroServicio.crearExcelSuscripcionCobroLista(buscarSuscripcionCobroListaFormulario));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	@GetMapping(value = "/facturacion/exportacionCobrosHolded", produces = "application/vnd.ms-excel")
	public @ResponseBody byte[] exportacionCobrosHolded(
			@ModelAttribute("buscarSuscripcionCobroListaFormulario") BuscarSuscripcionCobroListaFormulario buscarSuscripcionCobroListaFormulario, Model model) {
		try {
			return IOUtils.toByteArray(cobroServicio.generarFacturacionHoldedXLS(buscarSuscripcionCobroListaFormulario));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	@PostMapping("/cambiarEntrenadorFormulario")
	public String cambiarEntrenador(@RequestParam("clienteId") Long clienteId, @RequestParam("entrenadorId") Long entrenadorId, Model model) {
		clienteServicio.asignarEntrenador(clienteId, entrenadorId);
		return "redirect:/clientes";
	}

	@PostMapping("/programarVideoConferenciaFormulario")
	public String programarVideoConferenciaFormulario(@RequestParam("clienteId") Long clienteId, @RequestParam("videoconferenciaId") Long videoconferenciaId,
			@RequestParam("fechaHoraProgramada") ZonedDateTime fechaHoraProgramada, Model model, Authentication authentication) {
		clienteServicio.programarVideoconferencia(clienteId, videoconferenciaId, fechaHoraProgramada);

		return "redirect:/clientes";
	}

	@PostMapping("/programarVideoConferenciaMiClienteFormulario")
	public String programarVideoConferenciaMiClienteFormulario(@RequestParam("clienteId") Long clienteId, @RequestParam("videoconferenciaId") Long videoconferenciaId,
			@RequestParam("fechaHoraProgramada") ZonedDateTime fechaHoraProgramada, Model model, Authentication authentication) {
		clienteServicio.programarVideoconferencia(clienteId, videoconferenciaId, fechaHoraProgramada);

		return "redirect:/misClientes";
	}

	@GetMapping("/clienteDetalle/{clienteId}")
	public String clienteDetalle(@PathVariable("clienteId") Long clienteId, Model model) {

		model.addAttribute("cliente", clienteServicio.buscarClientePorId(clienteId));

		model.addAttribute("diaEntrenamientoSeleccionLista", seleccion(ValorMaestroTipoEnum.DIA_ENTRENAMIENTO));
		model.addAttribute("perfilSeleccionLista", seleccion(ValorMaestroTipoEnum.PERFIL));
		model.addAttribute("lugarEntrenamientoSeleccionLista", seleccion(ValorMaestroTipoEnum.LUGAR_ENTRENAMIENTO));
		model.addAttribute("materialSeleccionLista", ejercicioServicio.buscarMaterialTodos());
		model.addAttribute("sexoSeleccionLista", seleccion(ValorMaestroTipoEnum.SEXO));
		model.addAttribute("medioConocimientoSeleccionLista", seleccion(ValorMaestroTipoEnum.MEDIO_CONOCIMIENTO));

		return "entrenador/clienteDetallePagina";
	}

	@GetMapping("/entrenadores")
	public String entrenadores(Model model) {
		var buscarEntrenadorFormulario = new BuscarEntrenadorFormulario();
		buscarEntrenadorFormulario.setEstadoCodigoLista(List.of(EntrenadorEstadoEnum.ACTIVO.codigo()));

		model.addAttribute("estadoSeleccionLista", seleccion(ValorMaestroTipoEnum.ENTRENADOR_ESTADO));
		model.addAttribute("buscarEntrenadorFormulario", buscarEntrenadorFormulario);
		model.addAttribute("entrenador", new Entrenador());
		model.addAttribute("entrenadorLista", entrenadorServicio.buscarEntrenadorPorFiltro(buscarEntrenadorFormulario));
		return "entrenador/entrenadoresPagina";
	}

	@PostMapping("/buscarEntrenadorFormulario")
	public String buscarEntrenadorFormulario(@ModelAttribute BuscarEntrenadorFormulario buscarEntrenadorFormulario, Model model) {
		model.addAttribute("entrenadorLista", entrenadorServicio.buscarEntrenadorPorFiltro(buscarEntrenadorFormulario));
		return "entrenador/entrenadoresPagina :: entrenadorListaFragment";
	}

	@PostMapping("/darBajaEntrenadorAccion/{entrenadorId}")
	@ResponseBody
	public boolean darBajaEntrenadorAccion(@PathVariable("entrenadorId") Long entrenadorId, Model model) {
		entrenadorServicio.darBajaEntrenadorAccion(entrenadorId);
		return true;
	}

	@PostMapping("/activarEntrenadorAccion/{entrenadorId}")
	@ResponseBody
	public boolean activarEntrenadorAccion(@PathVariable("entrenadorId") Long entrenadorId, Model model) {
		entrenadorServicio.activarEntrenadorAccion(entrenadorId);
		return true;
	}

	@RequestMapping("/creacionEntrenadorFormulario")
	public String crearCuentaUsuario(@ModelAttribute("entrenador") Entrenador entrenador, BindingResult result, Model model) {

//		if (usuarioServicio.buscarUsuarioPorUsuarioNombre(cliente.getUsuario().getUsuarioNombre()) != null) {
//			result.addError(new FieldError("cliente", "usuario.usuarioNombre", "Ya existe un usuario con ese correo"));
//			model.addAttribute("diaEntrenamientoSeleccionLista", seleccion(ValorMaestroTipoEnum.DIA_ENTRENAMIENTO));
//			model.addAttribute("perfilSeleccionLista", seleccion(ValorMaestroTipoEnum.PERFIL));
//			model.addAttribute("lugarEntrenamientoSeleccionLista", seleccion(ValorMaestroTipoEnum.LUGAR_ENTRENAMIENTO));
//			model.addAttribute("materialSeleccionLista", ejercicioServicio.buscarMaterialTodos());
//			return "creacionCuentaUsuarioPagina";
//		}

		entrenadorServicio.crearEntrenadorNuevo(entrenador);

		if (result.hasErrors()) {
			return "entrenador/entrenadoresPagina";
		}

		return "redirect:/entrenadores";
	}

	@PostMapping("/actualizarRutinaDatosGeneralesFormulario")
	@ResponseBody
	public void actualizarRutinaDatosGeneralesFormulario(@ModelAttribute("rutina") Rutina rutina, Authentication authentication) {
		var usuarioSesion = (UsuarioSesion) authentication.getPrincipal();
		rutinaServicio.actualizarRutinaDatosGenerales(rutina, usuarioSesion.getEntrenadorId());
	}

	@PostMapping("/actualizarRutinaFechasFormulario")
	@ResponseBody
	public void actualizarRutinaFechasFormulario(@ModelAttribute("fechaEntrenamientoSeleccionadaLista") List<String> fechaEntrenamientoSeleccionadaLista) {
		System.out.println(fechaEntrenamientoSeleccionadaLista);
	}

	@PostMapping("/actualizarRutinaFaseGrupoFormulario")
	@ResponseBody
	public void actualizarRutinaFaseGrupo(@ModelAttribute RutinaFaseGrupo rutinaFaseGrupo) {
		rutinaServicio.actualizarRutinaFaseGrupo(rutinaFaseGrupo);
	}

	@PostMapping("/actualizarRutinaFaseGrupoEjercicioFormulario")
	@ResponseBody
	public void actualizarRutinaFaseGrupoEjercicio(@ModelAttribute RutinaFaseGrupoRepeticionEjercicio rutinaFaseGrupoRepeticionEjercicio) {
		rutinaServicio.actualizarRutinaFaseGrupoRepeticionEjercicio(rutinaFaseGrupoRepeticionEjercicio);
	}

	@GetMapping("/plantillasPagina")
	public String plantillasPagina(Model model, Authentication authentication) {
		var usuarioSesion = (UsuarioSesion) authentication.getPrincipal();
		var buscarPlantillaFormulario = new BuscarPlantillaFormulario();
		buscarPlantillaFormulario.setEntrenadorId(usuarioSesion.getEntrenadorId());
		model.addAttribute("buscarPlantillaFormulario", buscarPlantillaFormulario);
		model.addAttribute("plantillaLista", rutinaServicio.buscarPlantillaPorFiltro(buscarPlantillaFormulario));
		model.addAttribute("entrenadorSeleccionLista", entrenadorServicio.buscarEntrenadorActivoTodos());

		var rutinaNueva = new Rutina();
		model.addAttribute("rutinaNueva", rutinaNueva);
		return "entrenador/plantillasPagina";
	}

	@GetMapping("/ejerciciosPagina")
	public String ejerciciosPagina(Model model) {
		var buscarEjercicioFormulario = new BuscarEjercicioFormulario();
		model.addAttribute("buscarEjercicioFormulario", buscarEjercicioFormulario);
//		model.addAttribute("ejercicioLista", ejercicioServicio.buscarEjercicioPorFiltro(buscarEjercicioFormulario));
		model.addAttribute("patronSeleccionLista", ejercicioServicio.buscarPatronTodos());
		model.addAttribute("zonaSeleccionLista", ejercicioServicio.buscarZonaTodas());
		model.addAttribute("musculoSeleccionLista", ejercicioServicio.buscarMusculoTodos());
		model.addAttribute("parteCuerpoSeleccionLista", ejercicioServicio.buscarParteCuerpoTodos());
		model.addAttribute("materialSeleccionLista", ejercicioServicio.buscarMaterialTodos());
		return "entrenador/ejerciciosPagina";
	}

	@PostMapping("/comenzarCreacionEjercicioAccion")
	public String comenzarEdicionEjercicioAccion(Model model) {
		var ejercicio = new Ejercicio();
		ejercicio.setFechaHoraUltimaActualizacion(ZonedDateTime.now());
		model.addAttribute("ejercicio", ejercicio);
		model.addAttribute("patronSeleccionLista", ejercicioServicio.buscarPatronTodos());
		model.addAttribute("zonaSeleccionLista", ejercicioServicio.buscarZonaTodas());
		model.addAttribute("musculoSeleccionLista", ejercicioServicio.buscarMusculoTodos());
		model.addAttribute("parteCuerpoSeleccionLista", ejercicioServicio.buscarParteCuerpoTodos());
		model.addAttribute("materialSeleccionLista", ejercicioServicio.buscarMaterialTodos());
		return "entrenador/ejerciciosPagina :: ejercicioModalFragment";
	}

	@PostMapping("/cambiarEjercicioUnilateralAccion/{ejercicioId}")
	@ResponseBody
	public boolean cambiarEjercicioUnilateralAccion(@PathVariable("ejercicioId") Long ejercicioId, Model model) {
		ejercicioServicio.cambiarEjercicioUnilateral(ejercicioId);
		return true;
	}

	@PostMapping("/comenzarEdicionEjercicioAccion/{ejercicioId}")
	public String comenzarEdicionEjercicioAccion(@PathVariable("ejercicioId") Long ejercicioId, Model model) {
		var ejercicio = ejercicioServicio.buscarEjercicioPorId(ejercicioId);
		model.addAttribute("ejercicio", ejercicio);
		model.addAttribute("patronSeleccionLista", ejercicioServicio.buscarPatronTodos());
		model.addAttribute("zonaSeleccionLista", ejercicioServicio.buscarZonaTodas());
		model.addAttribute("musculoSeleccionLista", ejercicioServicio.buscarMusculoTodos());
		model.addAttribute("parteCuerpoSeleccionLista", ejercicioServicio.buscarParteCuerpoTodos());
		model.addAttribute("materialSeleccionLista", ejercicioServicio.buscarMaterialTodos());
		return "entrenador/ejerciciosPagina :: ejercicioModalFragment";
	}

	@PostMapping("/ejercicioFormulario")
	@ResponseBody
	public boolean ejercicioFormulario(@ModelAttribute Ejercicio ejercicio, Model model) {

		if (ejercicio.getId() == null) {
			ejercicioServicio.crearEjercicio(ejercicio);
		} else {
			ejercicioServicio.actualizarEjercicio(ejercicio);
		}
		return true;
	}

	@PostMapping("/buscarEjercicioFormulario")
	public String buscarEjercicioFormulario(@ModelAttribute BuscarEjercicioFormulario buscarEjercicioFormulario, Model model) {
		model.addAttribute("ejercicioLista", ejercicioServicio.buscarEjercicioPorFiltro(buscarEjercicioFormulario));
		return "entrenador/ejerciciosPagina :: ejercicioListaFragment";
	}

	@PostMapping("/buscarPlantillaFormulario")
	public String buscarPlantillaFormulario(@ModelAttribute BuscarPlantillaFormulario buscarPlantillaFormulario, Model model) {
		model.addAttribute("plantillaLista", rutinaServicio.buscarPlantillaPorFiltro(buscarPlantillaFormulario));
		return "entrenador/plantillasPagina :: plantillaListaFragment";
	}

	@PostMapping("/crearPlantillaFormulario")
	public String crearPlantillaFormulario(@ModelAttribute("rutinaNueva") Rutina rutinaNueva, BindingResult result, Model model, Authentication authentication) {
		var usuarioSesion = (UsuarioSesion) authentication.getPrincipal();
		var rutina = rutinaServicio.crearPlantilla(rutinaNueva, usuarioSesion.getEntrenadorId());
		return "redirect:/rutinaPlantillaElaboracionPagina/" + rutina.getId();
	}

	@PostMapping("/comenzarCopiarPlantillaAccion/{rutinaId}")
	public String comenzarCopiarPlantillaAccion(@PathVariable("rutinaId") Long rutinaId, Model model) {
		var rutinaCopia = rutinaServicio.buscarRutinaPorId(rutinaId);
		model.addAttribute("rutinaCopia", rutinaCopia);
		return "entrenador/plantillasPagina :: copiarPlantillaModalFragment";
	}

	@PostMapping("/copiarPlantillaFormulario")
	public Object copiarPlantillaFormulario(@ModelAttribute("rutinaCopia") Rutina rutinaCopia, BindingResult result, Model model, Authentication authentication) {
		var usuarioSesion = (UsuarioSesion) authentication.getPrincipal();

		var rutina = rutinaServicio.copiarPlantilla(rutinaCopia, usuarioSesion.getEntrenadorId());
		return "redirect:/rutinaPlantillaElaboracionPagina/" + rutina.getId();
	}

	@PostMapping("/borrarPlantillaAccion/{plantillaId}")
	@ResponseBody
	public boolean borrarPlantillaAccion(@PathVariable("plantillaId") Long plantillaId, Model model) {
		rutinaServicio.borrarPlantilla(plantillaId);
		return true;
	}

	@PostMapping("/comenzarCrearPlantillaDesdeRutinaAccion/{rutinaId}")
	public String comenzarCrearPlantillaDesdeRutinaAccion(@PathVariable("rutinaId") Long rutinaId, Model model) {
		var plantilla = rutinaServicio.buscarRutinaPorId(rutinaId);
		model.addAttribute("plantilla", plantilla);
		return "entrenador/rutinaElaboracionPagina :: crearPlantillaDesdeRutinaModalFragment";
	}

	@PostMapping("/crearPlantillaDesdeRutinaFormulario")
	@ResponseBody
	public boolean crearPlantillaDesdeRutinaFormulario(@ModelAttribute("plantilla") Rutina plantilla, BindingResult result, Model model, Authentication authentication) {
		var usuarioSesion = (UsuarioSesion) authentication.getPrincipal();
		rutinaServicio.crearPlantillaDesdeRutina(plantilla, usuarioSesion.getEntrenadorId());
		return true;
	}

	@PostMapping("/comenzarCargarPlantillaAccion/{rutinaId}")
	public String comenzarCargarPlantillaAccion(@PathVariable("rutinaId") Long rutinaId, Model model, Authentication authentication) {
		var usuarioSesion = (UsuarioSesion) authentication.getPrincipal();
		var buscarPlantillaFormulario = new BuscarPlantillaFormulario();
		buscarPlantillaFormulario.setEntrenadorId(usuarioSesion.getEntrenadorId());
		model.addAttribute("entrenadorSeleccionLista", entrenadorServicio.buscarEntrenadorActivoTodos());
		model.addAttribute("buscarPlantillaFormulario", buscarPlantillaFormulario);
		model.addAttribute("plantillaLista", rutinaServicio.buscarPlantillaPorFiltro(buscarPlantillaFormulario));
		model.addAttribute("rutinaId", rutinaId);
		return "entrenador/rutinaElaboracionPagina :: cargarPlantillaModalFragment";
	}

	@PostMapping("/buscarPlantillaDesdeElaboracionRutinaFormulario")
	public String buscarPlantillaDesdeElaboracionRutinaFormulario(@ModelAttribute BuscarPlantillaFormulario buscarPlantillaFormulario, Model model) {
		model.addAttribute("plantillaLista", rutinaServicio.buscarPlantillaPorFiltro(buscarPlantillaFormulario));
		return "entrenador/rutinaElaboracionPagina :: plantillaListaFragment";
	}

	@GetMapping("/cargarPlantillaAccion/{rutinaId}/{plantillaId}")
	public Object cargarPlantillaAccion(@PathVariable("rutinaId") Long rutinaId, @PathVariable("plantillaId") Long plantillaId, Model model,
			RedirectAttributes attributes) {

		var rutina = rutinaServicio.cargarPlantillaEnRutina(rutinaId, plantillaId);

		attributes.addFlashAttribute("mensajeExito", "Plantilla cargada correctamente");

		return new RedirectView("/rutinaElaboracion/" + rutina.getId());
	}

	@GetMapping("/rutinaPlantillaElaboracionPagina/{rutinaId}")
	public String rutinaPlantillaElaboracionPagina(@PathVariable("rutinaId") Long rutinaId, Model model) {

		var rutina = rutinaServicio.buscarRutinaParaElaboracion(rutinaId);
		model.addAttribute("rutina", rutina);
		model.addAttribute("patronLista", ejercicioServicio.buscarPatronTodos());
		model.addAttribute("musculoLista", ejercicioServicio.buscarMusculoTodos());
		model.addAttribute("parteCuerpoLista", ejercicioServicio.buscarParteCuerpoTodos());
		model.addAttribute("zonaLista", ejercicioServicio.buscarZonaTodas());
		var materialLista = ejercicioServicio.buscarMaterialTodos();
		model.addAttribute("materialSeleccionLista", materialLista);

		// Se crea siempre nuevo aqui y esta como atributo de sesion
		var ejercicioBusquedaFiltro = new BuscarEjercicioFormulario();
		ejercicioBusquedaFiltro.setFetchMaterial(false);
		ejercicioBusquedaFiltro.setClasificarEjercicioSegunUsoPrevio(false);
		ejercicioBusquedaFiltro.setFiltrarPorEjercicioUltimoUsoTipo(false);
		for (Material material : materialLista) {
			ejercicioBusquedaFiltro.addMaterialId(material.getId());
		}
		model.addAttribute("ejercicioBusquedaFiltro", ejercicioBusquedaFiltro);
		model.addAttribute("ejercicioLista", ejercicioServicio.buscarEjercicioPorFiltroParaElaboracionRutina(ejercicioBusquedaFiltro, false));

		model.addAttribute("rutinaFaseGrupoRepeticionEjercicioHistorial", new HashMap<Long, List<Long>>());

		return "entrenador/rutinaPlantillaElaboracionPagina";
	}

}
