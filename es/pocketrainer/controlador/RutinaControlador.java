package es.pocketrainer.controlador;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import es.pocketrainer.servicio.rutina.RutinaServicio;
import es.pocketrainer.servicio.valormaestro.ValorMaestroServicio;

@Controller
public class RutinaControlador {

	@Resource
	private ValorMaestroServicio valorMaestroServicio;

	@Resource
	private RutinaServicio rutinaServicio;

}
