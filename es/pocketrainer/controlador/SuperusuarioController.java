package es.pocketrainer.controlador;

import javax.annotation.Resource;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import es.pocketrainer.servicio.cliente.ClienteServicio;
import es.pocketrainer.servicio.ejercicio.EjercicioServicio;
import es.pocketrainer.servicio.rutina.RutinaServicio;

@Controller
public class SuperusuarioController {

	@Resource
	private ClienteServicio clienteServicio;

	@Resource
	private RutinaServicio rutinaServicio;

	@Resource
	private EjercicioServicio ejercicioServicio;

	@GetMapping("/lanzarRenovacion")
	public String lanzarRenovacion(Model model, Authentication authentication) {
		clienteServicio.revisarSuscripciones();
		clienteServicio.realizarCobrosRenovacionesSuscripciones();
		return "inicioEntrenadorPagina";
	}

	@GetMapping("/corregirRutinas")
	public String corregirRutinas(Model model, Authentication authentication) {
		rutinaServicio.corregirRutinas();
		return "inicioEntrenadorPagina";
	}

	@GetMapping("/analizarCorreccionesRutinas")
	public String analizarCorreccionesRutinas(Model model, Authentication authentication) {
		rutinaServicio.analizarCorreccionesRutinas();
		return "inicioEntrenadorPagina";
	}

	@PostMapping("/analizarCorreccionesRutinas/{rutinaId}")
	public String analizarCorreccionesRutinas(@PathVariable Long rutinaId, Model model, Authentication authentication) {
		rutinaServicio.analizarCorreccionesRutinas(rutinaId);
		return "redirect:/rutinaElaboracion/" + rutinaId;
	}

	@GetMapping("/corregirFechasSuscripciones")
	public String corregirFechasSuscripciones(Model model, Authentication authentication) {
		clienteServicio.corregirFechasSuscripciones();
		return "inicioEntrenadorPagina";
	}

	@GetMapping("/actualizarEjercicioEntrenadorEstadistica")
	public String actualizarEjercicioEntrenadorEstadistica(Model model, Authentication authentication) {
		ejercicioServicio.actualizarEjercicioEstadistica();
		return "inicioEntrenadorPagina";
	}

}
