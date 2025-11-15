package es.pocketrainer.servicio.gestorficheros;

import java.io.ByteArrayOutputStream;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface GestorFicherosServicio {

	String guardarFichero(MultipartFile fichero, String nombre, String rutaBase);

	Resource obtenerFichero(String nombre, String rutaBase);

	void borrarFichero(String nombre, String string);

	String guardarFichero(ByteArrayOutputStream baos, String nombre, String rutaBase);
}
