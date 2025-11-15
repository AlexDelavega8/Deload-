package es.pocketrainer.servicio.migracion;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import es.pocketrainer.modelo.ejercicio.Ejercicio;
import es.pocketrainer.modelo.ejercicio.EjercicioMusculo;
import es.pocketrainer.modelo.ejercicio.EjercicioParteCuerpo;
import es.pocketrainer.modelo.migracion.EjercicioMigracion;
import es.pocketrainer.repositorio.ejercicio.EjercicioRepositorio;
import es.pocketrainer.repositorio.material.MaterialRepositorio;
import es.pocketrainer.repositorio.migracion.EjercicioMigracionRepositorio;
import es.pocketrainer.repositorio.musculo.MusculoRepositorio;
import es.pocketrainer.repositorio.partecuerpo.ParteCuerpoRepositorio;
import es.pocketrainer.repositorio.patron.PatronRepositorio;
import es.pocketrainer.repositorio.zona.ZonaRepositorio;
import es.pocketrainer.servicio.valormaestro.ValorMaestroServicio;

@Service
@Transactional
public class MigracionServicioImpl implements MigracionServicio {

	@Resource
	private EjercicioMigracionRepositorio ejercicioMigracionRepositorio;

	@Resource
	private PatronRepositorio patronRepositorio;

	@Resource
	private MaterialRepositorio materialRepositorio;

	@Resource
	private MusculoRepositorio musculoRepositorio;

	@Resource
	private ParteCuerpoRepositorio parteCuerpoRepositorio;

	@Resource
	private ZonaRepositorio zonaRepositorio;

	@Resource
	private EjercicioRepositorio ejercicioRepositorio;

	@Resource
	private ValorMaestroServicio valorMaestroServicio;

	@Override
	public void migrarEjercicios() {
		for (EjercicioMigracion ejercicioMigracion : ejercicioMigracionRepositorio.findAll()) {
			var ejercicio = new Ejercicio();
			ejercicio.setId(ejercicioMigracion.getId());
			ejercicio.setNombre(ejercicioMigracion.getNombreLargo());
			ejercicio.setNombreCorto(ejercicioMigracion.getNombre());
			ejercicio.setDescripcion(ejercicioMigracion.getDescripcion());

			for (Long id : obtenerIdentificadores(ejercicioMigracion.getMaterial())) {
				if (id != null) {
					ejercicio.addMaterial(materialRepositorio.findById(id).get());
				}

			}

			for (Long id : obtenerIdentificadores(ejercicioMigracion.getMusculos())) {
				if (id != null) {
					EjercicioMusculo ejercicioMusculo = new EjercicioMusculo();
					ejercicioMusculo.setMusculo(musculoRepositorio.findById(id).get());
					ejercicioMusculo.setPrincipal(false);
					ejercicio.addEjercicioMusculo(ejercicioMusculo);
				}
			}

			for (Long id : obtenerIdentificadores(ejercicioMigracion.getMusculosObjetivo())) {
				if (id != null) {
					EjercicioMusculo ejercicioMusculo = new EjercicioMusculo();
					ejercicioMusculo.setMusculo(musculoRepositorio.findById(id).get());
					ejercicioMusculo.setPrincipal(true);
					ejercicio.addEjercicioMusculo(ejercicioMusculo);
				}
			}

			for (Long id : obtenerIdentificadores(ejercicioMigracion.getPartes())) {
				if (id != null) {
					EjercicioParteCuerpo ejercicioParteCuerpo = new EjercicioParteCuerpo();
					ejercicioParteCuerpo.setParteCuerpo(parteCuerpoRepositorio.findById(id).get());
					ejercicioParteCuerpo.setPrincipal(false);
					ejercicio.addEjercicioParteCuerpo(ejercicioParteCuerpo);
				}
			}

			for (Long id : obtenerIdentificadores(ejercicioMigracion.getPartesObjetivo())) {
				if (id != null) {
					EjercicioParteCuerpo ejercicioParteCuerpo = new EjercicioParteCuerpo();
					System.out.println(id);
					ejercicioParteCuerpo.setParteCuerpo(parteCuerpoRepositorio.findById(id).get());
					ejercicioParteCuerpo.setPrincipal(true);
					ejercicio.addEjercicioParteCuerpo(ejercicioParteCuerpo);
				}
			}

			for (Long id : obtenerIdentificadores(ejercicioMigracion.getZonas())) {
				if (id != null) {
					ejercicio.addZona(zonaRepositorio.findById(id).get());
				}
			}

			for (Long id : obtenerIdentificadores(ejercicioMigracion.getPatrones())) {
				if (id != null) {
					ejercicio.addPatron(patronRepositorio.findById(id).get());
				}
			}

			ejercicioRepositorio.save(ejercicio);

		}
	}

	private List<Long> obtenerIdentificadores(String listaIdentificadores) {
		return Arrays.asList(listaIdentificadores.split(",")).stream()
				.map(idString -> idString.isBlank() || idString.contains("a") || idString.contains("A") ? null : Long.valueOf(idString.trim()))
				.collect(Collectors.toList());
	}

}
