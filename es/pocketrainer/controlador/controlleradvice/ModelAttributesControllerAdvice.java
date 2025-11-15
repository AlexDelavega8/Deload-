package es.pocketrainer.controlador.controlleradvice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class ModelAttributesControllerAdvice {

	@Value("${url.estaticos.imagenes}")
	private String urlEstaticosImagenes;

	@Value("${url.estaticos.videos}")
	private String urlEstaticosVideos;

	@Value("${url.estaticos.material.fotos}")
	private String urlEstaticosMaterialFotos;

	@Value("${url.estaticos.ejercicios.fotos}")
	private String urlEstaticosEjerciciosFotos;

	@Value("${url.estaticos.ejercicios.videos}")
	private String urlEstaticosEjerciciosVideos;

	@Value("${url.estaticos.ejercicios.audios}")
	private String urlEstaticosEjerciciosAudios;

	@Value("${url.estaticos.ejercicios.videos.480}")
	private String urlEstaticosEjerciciosVideos480;

	@Value("${meta.robots}")
	private String metaRobots;

	@Value("${meta.robots.noindex}")
	private String metaRobotsNoindex;

	@ModelAttribute("urlEstaticosImagenes")
	public String getUrlEstaticosImagenes() {
		return urlEstaticosImagenes;
	}

	@ModelAttribute("urlEstaticosVideos")
	public String getUrlEstaticosVideos() {
		return urlEstaticosVideos;
	}

	@ModelAttribute("urlEstaticosMaterialFotos")
	public String getUrlEstaticosMaterialFotos() {
		return urlEstaticosMaterialFotos;
	}

	@ModelAttribute("urlEstaticosEjerciciosFotos")
	public String getUrlEstaticosEjerciciosFotos() {
		return urlEstaticosEjerciciosFotos;
	}

	@ModelAttribute("urlEstaticosEjerciciosVideos")
	public String getUrlEstaticosEjerciciosVideos() {
		return urlEstaticosEjerciciosVideos;
	}

	@ModelAttribute("urlEstaticosEjerciciosVideos480")
	public String getUrlEstaticosEjerciciosVideos480() {
		return urlEstaticosEjerciciosVideos480;
	}

	@ModelAttribute("urlEstaticosEjerciciosAudios")
	public String getUrlEstaticosEjerciciosAudios() {
		return urlEstaticosEjerciciosAudios;
	}

	@ModelAttribute("metaRobots")
	public String getMetaRobots() {
		return metaRobots;
	}

	@ModelAttribute("metaRobotsNoindex")
	public String getMetaRobotsNoindex() {
		return metaRobotsNoindex;
	}

}
