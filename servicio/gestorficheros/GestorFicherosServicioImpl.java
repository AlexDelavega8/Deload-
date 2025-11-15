package es.pocketrainer.servicio.gestorficheros;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class GestorFicherosServicioImpl implements GestorFicherosServicio {

	private static Logger LOGGER = LoggerFactory.getLogger(GestorFicherosServicioImpl.class);

	@Override
	public String guardarFichero(MultipartFile fichero, String nombre, String rutaBase) {
		try {
			return guardarFichero(rutaBase, nombre, fichero.getBytes());
		} catch (IOException e) {
			LOGGER.error("Error obteniendo bytes del fichero multipart ", e);
			throw new RuntimeException("Error obteniendo bytes del fichero multipart ", e);
		}
	}

	@Override
	public String guardarFichero(ByteArrayOutputStream baos, String nombre, String rutaBase) {
		return guardarFichero(rutaBase, nombre, baos.toByteArray());
	}

	@Override
	public Resource obtenerFichero(String nombre, String rutaBase) {
		Path path = Paths.get(rutaBase + nombre);
		return new FileSystemResource(path);
	}

	@Override
	public void borrarFichero(String nombre, String rutaBase) {
		Path path = Paths.get(rutaBase + nombre);
		path.toFile().delete();
	}

	private String guardarFichero(String rutaBase, String nombre, byte[] bytes) {
		var pathCompleto = obtenerFicheroPathCreandoCarpetasSiNecesario(rutaBase, nombre);
		try {
			Files.write(pathCompleto, bytes);
			try {
				var permisos = Set.of(PosixFilePermission.OTHERS_READ, PosixFilePermission.GROUP_READ, PosixFilePermission.OWNER_READ, PosixFilePermission.OWNER_WRITE);
				Files.setPosixFilePermissions(pathCompleto, permisos);
			} catch (Exception e) {
				LOGGER.error("Error guardando fichero, no fue posible establecer permisos lectura para others ", e);
			}
		} catch (IOException e) {
			LOGGER.error("Error guardando fichero ", e);
			throw new RuntimeException("Error guardando fichero ", e);
		}
		return pathCompleto.toString();
	}

	private Path obtenerFicheroPathCreandoCarpetasSiNecesario(String rutaBase, String nombre) {
		try {
			var pathRutaBase = Paths.get(rutaBase);
			if (!Files.exists(pathRutaBase)) {
				Files.createDirectories(pathRutaBase);
			}
		} catch (IOException e) {
			LOGGER.error("Error guardando fichero ", e);
			throw new RuntimeException("Error guardando fichero ", e);
		}

		return Paths.get(rutaBase + nombre);
	}

}
