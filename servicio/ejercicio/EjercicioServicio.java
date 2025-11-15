package es.pocketrainer.servicio.ejercicio;

import java.util.List;

import es.pocketrainer.modelo.ejercicio.Ejercicio;
import es.pocketrainer.modelo.ejercicio.EjercicioEntrenadorEstadistica;
import es.pocketrainer.modelo.ejercicio.Material;
import es.pocketrainer.modelo.ejercicio.Musculo;
import es.pocketrainer.modelo.ejercicio.ParteCuerpo;
import es.pocketrainer.modelo.ejercicio.Patron;
import es.pocketrainer.modelo.ejercicio.Zona;
import es.pocketrainer.repositorio.ejercicio.BuscarEjercicioFormulario;

public interface EjercicioServicio {

	Ejercicio buscarEjercicioPorId(Long ejercicioId);

	List<Ejercicio> buscarEjercicioTodos();

	List<Patron> buscarPatronTodos();

	List<Musculo> buscarMusculoTodos();

	List<ParteCuerpo> buscarParteCuerpoTodos();

	List<Material> buscarMaterialTodos();

	List<Ejercicio> buscarEjercicioPorFiltro(BuscarEjercicioFormulario ejercicioBusquedaFiltro);

	List<Ejercicio> buscarEjercicioPorFiltroParaElaboracionRutina(BuscarEjercicioFormulario ejercicioBusquedaFiltro, boolean clasificarRutinaEjercicios);

	List<Zona> buscarZonaTodas();

	void crearEjercicio(Ejercicio ejercicio);

	void actualizarEjercicio(Ejercicio ejercicio);

	void cambiarEjercicioUnilateral(Long ejercicioId);

	void actualizarEjercicioEstadistica();

	EjercicioEntrenadorEstadistica buscarEjercicioEntrenadorEstadisticaPorEntrenadorIdEjercicioId(Long entrenadorId, Long ejercicioId);

}
