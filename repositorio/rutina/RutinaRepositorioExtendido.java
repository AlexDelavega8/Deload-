package es.pocketrainer.repositorio.rutina;

import java.time.ZonedDateTime;
import java.util.List;

import es.pocketrainer.formulario.BuscarPlantillaFormulario;
import es.pocketrainer.formulario.BuscarRutinaFormulario;
import es.pocketrainer.modelo.rutina.Rutina;

public interface RutinaRepositorioExtendido {

	List<Rutina> buscarRutinaPorClienteFechaCreacion(Long clienteId, ZonedDateTime fechaCreacion);

	List<Rutina> buscarRutinaPorClienteFechaCreacionEntreFechas(Long clienteId, ZonedDateTime fechaCreacionDesde, ZonedDateTime fechaCreacionHasta);

	List<Rutina> buscarPlantillaPorFiltro(BuscarPlantillaFormulario buscarPlantillaFormulario);

	List<Rutina> buscarRutinaPorFiltro(BuscarRutinaFormulario buscarRutinaFormulario);
}
