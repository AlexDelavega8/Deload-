package es.pocketrainer.servicio.rutina;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

import es.pocketrainer.formulario.BuscarPlantillaFormulario;
import es.pocketrainer.formulario.BuscarRutinaFormulario;
import es.pocketrainer.formulario.ConfigurarRutinaFormulario;
import es.pocketrainer.modelo.cliente.Cliente;
import es.pocketrainer.modelo.ejercicio.Ejercicio;
import es.pocketrainer.modelo.ejercicio.Material;
import es.pocketrainer.modelo.rutina.Rutina;
import es.pocketrainer.modelo.rutina.RutinaEjecucion;
import es.pocketrainer.modelo.rutina.RutinaFaseGrupo;
import es.pocketrainer.modelo.rutina.RutinaFaseGrupoRepeticionEjercicio;
import es.pocketrainer.repositorio.ejercicio.BuscarEjercicioFormulario;

public interface RutinaServicio {

	Rutina buscarRutinaPorId(Long rutinaId);

	Rutina prepararRutinaNueva(Cliente cliente, ZonedDateTime fechaReferencia);

	Rutina actualizarRutina(Rutina rutina);

	RutinaFaseGrupoRepeticionEjercicio anhadirEjercicio(BuscarEjercicioFormulario buscarEjercicioFormulario, Long entrenadorId, Long rutinaId, Long ejercicioId,
			Long rutinaFaseGrupoId, Integer posicion, boolean clasificarEjercicioSegunUsoPrevio);

	void moverEjercicio(Long rutinaId, Long rutinaFaseGrupoEjercicioId, Long rutinaFaseGrupoId, Integer posicion);

	void borrarEjercicio(Long rutinaFaseGrupoEjercicioId);

	RutinaEjecucion iniciarRutinaEjecucionDesdeInicio(Long rutinaId);

	RutinaEjecucion cambiarEjercicioDeRutinaEnEjecucion(Long rutinaId, Long rutinaFaseGrupoEjercicioId);

	RutinaFaseGrupo anhadirGrupo(Long rutinaId, Long rutinaFaseId);

	void borrarGrupo(Long rutinaId, Long rutinaFaseId, Long rutinaFaseGrupoId);

	List<RutinaFaseGrupoRepeticionEjercicio> agruparRutinaFaseGrupoEjercicio(Rutina rutina);

	Rutina actualizarRutinaDatosGenerales(Rutina rutina, Long entrenadorId);

	void actualizarRutinaFaseGrupo(RutinaFaseGrupo rutinaFaseGrupo);

	void actualizarRutinaFaseGrupoRepeticionEjercicio(RutinaFaseGrupoRepeticionEjercicio rutinaFaseGrupoRepeticionEjercicio);

	Set<Material> agruparRutinaFaseGrupoEjercicioMaterial(Rutina rutina);

	List<RutinaFaseGrupoRepeticionEjercicio> agruparRutinaFaseGrupoEjercicioSinRepeticiones(Rutina rutina);

	void valorarRutina(Long clienteId, Long rutinaId, String valoracion);

	void borrarRutina(Rutina rutina);

	List<Rutina> buscarRutinaPorClienteFechaCreacion(Long clienteId, ZonedDateTime fechaCreacion);

	Rutina buscarRutinaParaElaboracion(Long rutinaId);

	void clasificarEjercicioSegunUsoPrevio(Cliente cliente, Long rutinaId, List<Ejercicio> ejercicioLista, boolean clasificarRutinaEjercicioLista);

	List<Rutina> buscarPlantillaTodas();

	Rutina crearPlantilla(Rutina rutinaNueva, Long entrenadorId);

	Rutina copiarPlantilla(Rutina rutinaCopia, Long entrenadorId);

	Rutina crearPlantillaDesdeRutina(Rutina plantilla, Long entrenadorId);

	Rutina cargarPlantillaEnRutina(Long rutinaId, Long plantillaId);

	List<Rutina> buscarPlantillaPorFiltro(BuscarPlantillaFormulario buscarPlantillaFormulario);

	void borrarPlantilla(Long plantillaId);

	void corregirRutinas();

	void analizarCorreccionesRutinas(Long rutinaId);

	void analizarCorreccionesRutinas();

	void configurarRutinaFormulario(ConfigurarRutinaFormulario configurarRutinaFormulario);

	RutinaEjecucion iniciarRutinaEnEjercicioConcreto(Long rutinaId, Long nuevoRutinaFaseGrupoEjercicioId);

	List<Rutina> buscarRutinaPorFiltro(BuscarRutinaFormulario buscarRutinaFormulario);

	Rutina cambioAutomaticoEjerciciosRutinaEntera(Long entrenadorId, Long rutinaId, List<String> rutinaFaseGrupoRepeticionEjercicioIdLista,
			BuscarEjercicioFormulario buscarEjercicioFormulario, Map<Long, List<Long>> rutinaFaseGrupoRepeticionEjercicioHistorial);

	Rutina cambioAutomaticoEjerciciosRutinaFase(Long entrenadorId, Long rutinaId, Long rutinaFaseId, List<String> rutinaFaseGrupoRepeticionEjercicioIdLista,
			BuscarEjercicioFormulario buscarEjercicioFormulario, Map<Long, List<Long>> rutinaFaseGrupoRepeticionEjercicioHistorial);

	Rutina cambioAutomaticoEjerciciosRutinaFaseGrupo(Long entrenadorId, Long rutinaId, Long rutinaFaseId, Long rutinaFaseGrupoId,
			List<String> rutinaFaseGrupoRepeticionEjercicioIdLista, BuscarEjercicioFormulario buscarEjercicioFormulario,
			Map<Long, List<Long>> rutinaFaseGrupoRepeticionEjercicioHistorial);

}
