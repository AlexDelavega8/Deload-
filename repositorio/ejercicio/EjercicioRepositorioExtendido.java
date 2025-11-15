package es.pocketrainer.repositorio.ejercicio;

import java.util.List;

import es.pocketrainer.modelo.ejercicio.Ejercicio;

public interface EjercicioRepositorioExtendido {

	List<Ejercicio> buscarPorFiltroParaElaboracionRutina(BuscarEjercicioFormulario ejercicioBusquedaFiltro);

	List<Ejercicio> buscarPorFiltro(BuscarEjercicioFormulario ejercicioBusquedaFiltro);

}
