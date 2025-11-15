package es.pocketrainer.servicio.rutina;

import static es.pocketrainer.util.Constantes.FASE_FINAL_NOMBRE;
import static es.pocketrainer.util.Constantes.FASE_FINAL_TIPO;
import static es.pocketrainer.util.Constantes.FASE_INICIAL_NOMBRE;
import static es.pocketrainer.util.Constantes.FASE_INICIAL_TIPO;
import static es.pocketrainer.util.Constantes.FASE_PRINCIPAL_NOMBRE;
import static es.pocketrainer.util.Constantes.FASE_PRINCIPAL_TIPO;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.pocketrainer.formulario.BuscarPlantillaFormulario;
import es.pocketrainer.formulario.BuscarRutinaFormulario;
import es.pocketrainer.formulario.ConfigurarRutinaFormulario;
import es.pocketrainer.modelo.cliente.Cliente;
import es.pocketrainer.modelo.ejercicio.Ejercicio;
import es.pocketrainer.modelo.ejercicio.Material;
import es.pocketrainer.modelo.ejercicio.Musculo;
import es.pocketrainer.modelo.ejercicio.ParteCuerpo;
import es.pocketrainer.modelo.ejercicio.Patron;
import es.pocketrainer.modelo.ejercicio.Zona;
import es.pocketrainer.modelo.rutina.Rutina;
import es.pocketrainer.modelo.rutina.RutinaEjecucion;
import es.pocketrainer.modelo.rutina.RutinaFase;
import es.pocketrainer.modelo.rutina.RutinaFaseGrupo;
import es.pocketrainer.modelo.rutina.RutinaFaseGrupoRepeticion;
import es.pocketrainer.modelo.rutina.RutinaFaseGrupoRepeticionEjercicio;
import es.pocketrainer.repositorio.ejercicio.BuscarEjercicioFormulario;
import es.pocketrainer.repositorio.ejercicio.EjercicioRepositorio;
import es.pocketrainer.repositorio.rutina.RutinaFaseGrupoRepeticionEjercicioRepositorio;
import es.pocketrainer.repositorio.rutina.RutinaFaseGrupoRepositorio;
import es.pocketrainer.repositorio.rutina.RutinaRepositorio;
import es.pocketrainer.servicio.cliente.ClienteServicio;
import es.pocketrainer.servicio.ejercicio.EjercicioServicio;
import es.pocketrainer.servicio.entrenador.EntrenadorServicio;
import es.pocketrainer.servicio.notificaciones.NotificacionServicio;
import es.pocketrainer.servicio.parametrizacion.ParametroConfiguracionServicio;
import es.pocketrainer.servicio.valormaestro.ValorMaestroServicio;
import es.pocketrainer.util.Constantes;
import es.pocketrainer.util.Constantes.EjercicioUltimoUsoTipoEnum;
import es.pocketrainer.util.Constantes.RutinaEstadoEnum;
import es.pocketrainer.util.FechaUtil;

@Service
public class RutinaServicioImpl implements RutinaServicio {

	private static Logger LOGGER = LoggerFactory.getLogger(RutinaServicioImpl.class);

	@Resource
	private EntrenadorServicio entrenadorServicio;

	@Resource
	private ParametroConfiguracionServicio parametroConfiguracionServicio;

	@Resource
	private EjercicioRepositorio ejercicioRepositorio;

	@Resource
	private ClienteServicio clienteServicio;

	@Resource
	private ValorMaestroServicio valorMaestroServicio;

	@Resource
	private NotificacionServicio notificacionServicio;

	@Resource
	private RutinaRepositorio rutinaRepositorio;

	@Resource
	private RutinaFaseGrupoRepositorio rutinaFaseGrupoRepositorio;

	@Resource
	private RutinaFaseGrupoRepeticionEjercicioRepositorio rutinaFaseGrupoRepeticionEjercicioRepositorio;

	@Resource
	private EjercicioServicio ejercicioServicio;

	@Resource
	private EjercicioUltimoUsoCache ejercicioUltimoUsoCache;

	@Resource
	private EjercicioEstadisticaCache ejercicioEstadisticaCache;

	@Value("${url.estaticos.ejercicios.videos}")
	private String urlEstaticosEjerciciosVideos;

	@Value("${url.estaticos.ejercicios.audios}")
	private String urlEstaticosEjerciciosAudios;

	@Transactional
	@Override
	public Rutina buscarRutinaPorId(Long rutinaId) {
		return rutinaRepositorio.findById(rutinaId).orElse(null);
	}

	@Transactional
	@Override
	public Rutina actualizarRutina(Rutina rutina) {
		return rutinaRepositorio.saveAndFlush(rutina);
	}

	@Transactional
	@Override
	public Rutina prepararRutinaNueva(Cliente cliente, ZonedDateTime fechaReferencia) {
		Rutina rutina = new Rutina();
		rutina.setEsPlantilla(Boolean.FALSE);
		rutina.setFechaCreacion(fechaReferencia);
		rutina.setEnEjecucion(Boolean.FALSE);
		rutina.setEstado(valorMaestroServicio.buscarValorMaestroPorCodigo(RutinaEstadoEnum.EN_ELABORACION.codigo()));

		var rutinaFaseInicial = new RutinaFase();
		rutinaFaseInicial.setNombre(FASE_INICIAL_NOMBRE);
		rutinaFaseInicial.setTipo(FASE_INICIAL_TIPO);
		rutinaFaseInicial.addRutinaFaseGrupo(prepararRutinaFaseGrupo());
		rutina.setFaseInicial(rutinaFaseInicial);

		var rutinaFasePrincipal = new RutinaFase();
		rutinaFasePrincipal.setNombre(FASE_PRINCIPAL_NOMBRE);
		rutinaFasePrincipal.setTipo(FASE_PRINCIPAL_TIPO);
		rutinaFasePrincipal.addRutinaFaseGrupo(prepararRutinaFaseGrupo());
		rutina.setFasePrincipal(rutinaFasePrincipal);

		var rutinaFaseFinal = new RutinaFase();
		rutinaFaseFinal.setNombre(FASE_FINAL_NOMBRE);
		rutinaFaseFinal.setTipo(FASE_FINAL_TIPO);
		rutinaFaseFinal.addRutinaFaseGrupo(prepararRutinaFaseGrupo());
		rutina.setFaseFinal(rutinaFaseFinal);

		rutina.setEntrenadorTiempoDescansoEntreEjercicios(
				parametroConfiguracionServicio.buscarParametroConfiguracionIntegerValor(Constantes.PC_TIEMPO_DESCANSO_ENTRE_EJERCICIOS));
		rutina.setEntrenadorTiempoDescansoEntreUnilaterales(
				parametroConfiguracionServicio.buscarParametroConfiguracionIntegerValor(Constantes.PC_TIEMPO_DESCANSO_ENTRE_UNILATERALES));
		rutina.setEntrenadorTiempoEjercicioPorRepeticiones(
				parametroConfiguracionServicio.buscarParametroConfiguracionIntegerValor(Constantes.PC_TIEMPO_EJERCICIO_POR_REPETICIONES));

		return rutina;
	}

	private RutinaFaseGrupo prepararRutinaFaseGrupo() {
		var rutinaFaseGrupo = new RutinaFaseGrupo();
		var rutinaFaseGrupoRepeticion = new RutinaFaseGrupoRepeticion();

		rutinaFaseGrupo.setPosicion(1);
		rutinaFaseGrupo.setRepeticiones(1);
		rutinaFaseGrupoRepeticion.setNumeroRepeticion(1);
		rutinaFaseGrupo.addRutinaFaseGrupoRepeticionEstableciendoDatos(rutinaFaseGrupoRepeticion);

		return rutinaFaseGrupo;
	}

	@Transactional
	@Override
	public RutinaFaseGrupoRepeticionEjercicio anhadirEjercicio(BuscarEjercicioFormulario buscarEjercicioFormulario, Long entrenadorId, Long rutinaId, Long ejercicioId,
			Long rutinaFaseGrupoId, Integer posicion, boolean clasificarEjercicioSegunUsoPrevio) {

		RutinaFaseGrupoRepeticionEjercicio nuevoRutinaFaseGrupoEjercicio = null;
		var rutina = buscarRutinaPorId(rutinaId);

		// Encontrar grupo
		RutinaFaseGrupo rutinaFaseGrupoBuscado = Stream
				.concat(Stream.concat(rutina.getFaseInicial().getRutinaFaseGrupoLista().stream(), rutina.getFasePrincipal().getRutinaFaseGrupoLista().stream()),
						rutina.getFaseFinal().getRutinaFaseGrupoLista().stream())
				.filter((rutinaFaseGrupo) -> rutinaFaseGrupo.getId().equals(rutinaFaseGrupoId)).findAny().orElse(null);

		// Nuevo ejercicio
		RutinaFaseGrupoRepeticionEjercicio rutinaFaseGrupoEjercicio = new RutinaFaseGrupoRepeticionEjercicio();
		var ejercicio = ejercicioServicio.buscarEjercicioPorId(ejercicioId);
		rutinaFaseGrupoEjercicio.setEjercicio(ejercicio);
		rutinaFaseGrupoEjercicio.setPosicion(posicion);
		rutinaFaseGrupoEjercicio.setComentarios("");

		// Repeticiones y tiempo segun estadísticas de uso por entrenador o global
		var repeticiones = 0;
		var tiempo = 0;
		var ejercicioEntrenadorEstadistica = ejercicioServicio.buscarEjercicioEntrenadorEstadisticaPorEntrenadorIdEjercicioId(entrenadorId, ejercicioId);
		if (ejercicioEntrenadorEstadistica != null) {
			if (ejercicioEntrenadorEstadistica.getNumeroUsosPorRepeticion() > ejercicioEntrenadorEstadistica.getNumeroUsosPorTiempo()) {
				repeticiones = ejercicioEntrenadorEstadistica.getConfiguracionRepeticionesMasUsado();
			} else if (ejercicioEntrenadorEstadistica.getNumeroUsosPorRepeticion() < ejercicioEntrenadorEstadistica.getNumeroUsosPorTiempo()) {
				tiempo = ejercicioEntrenadorEstadistica.getConfiguracionTiempoMasUsado();
			}
		} else if (ejercicio.getNumeroUsosPorRepeticion() != null) {
			if (ejercicio.getNumeroUsosPorRepeticion() > ejercicio.getNumeroUsosPorTiempo()) {
				repeticiones = ejercicio.getConfiguracionRepeticionesMasUsado();
			} else if (ejercicio.getNumeroUsosPorRepeticion() < ejercicio.getNumeroUsosPorTiempo()) {
				tiempo = ejercicio.getConfiguracionTiempoMasUsado();
			}
		}
		rutinaFaseGrupoEjercicio.setRepeticiones(repeticiones);
		rutinaFaseGrupoEjercicio.setTiempo(tiempo);

		// Establecer parametros con el que fue buscado y añadido
		if (buscarEjercicioFormulario.isBusquedaPorFiltroCompleto()) {
			LOGGER.info("Parametros segun búsqueda con filtro completo");

			rutinaFaseGrupoEjercicio.setPatronLista(buscarEjercicioFormulario.getPatronIdLista() != null && !buscarEjercicioFormulario.getPatronIdLista().isEmpty()
					? ejercicio.getPatronLista().stream().filter(it -> buscarEjercicioFormulario.getPatronIdLista().contains(it.getId()))
							.map(it -> new Patron(it.getId())).collect(Collectors.toSet())
					: ejercicio.getPatronLista().stream().map(it -> new Patron(it.getId())).collect(Collectors.toSet()));

			rutinaFaseGrupoEjercicio.setZonaLista(buscarEjercicioFormulario.getZonaIdLista() != null ? ejercicio.getZonaLista().stream()
					.filter(it -> buscarEjercicioFormulario.getZonaIdLista().contains(it.getId())).map(it -> new Zona(it.getId())).collect(Collectors.toSet()) : null);

			rutinaFaseGrupoEjercicio.setMusculoLista(buscarEjercicioFormulario.getMusculoIdLista() != null
					? ejercicio.getEjercicioMusculoLista().stream().filter(it -> buscarEjercicioFormulario.getMusculoIdLista().contains(it.getMusculo().getId()))
							.map(it -> new Musculo(it.getMusculo().getId())).collect(Collectors.toSet())
					: null);

			rutinaFaseGrupoEjercicio.setParteCuerpoLista(buscarEjercicioFormulario.getParteCuerpoIdLista() != null ? ejercicio.getEjercicioParteCuerpoLista().stream()
					.filter(it -> buscarEjercicioFormulario.getParteCuerpoIdLista().contains(it.getParteCuerpo().getId()))
					.map(it -> new ParteCuerpo(it.getParteCuerpo().getId())).collect(Collectors.toSet()) : null);
		} else {
			LOGGER.info("Parametros segun búsqueda con filtro por nombre");
			rutinaFaseGrupoEjercicio.setPatronLista(ejercicio.getPatronLista().stream().map(it -> new Patron(it.getId())).collect(Collectors.toSet()));
		}

		rutinaFaseGrupoEjercicio.setEnEjecucion(Boolean.FALSE);

		// Ver si hay que hacer movimientos segun posicion
		if (rutinaFaseGrupoBuscado.getRutinaFaseGrupoEjercicioLista() != null && ((rutinaFaseGrupoBuscado.getRutinaFaseGrupoEjercicioLista().size() + 1) != posicion)) {
			// Hay que 'desplazar' hacia la derecha en todas las repeticiones ojo
			for (RutinaFaseGrupoRepeticionEjercicio rutinaFaseGrupoEjercicioRevision : rutinaFaseGrupoBuscado.getRutinaFaseGrupoEjercicioConRepeticionesLista()) {
				if (rutinaFaseGrupoEjercicioRevision.getPosicion() >= posicion) {
					rutinaFaseGrupoEjercicioRevision.setPosicion(rutinaFaseGrupoEjercicioRevision.getPosicion() + 1);
				}
			}

		}

		rutinaFaseGrupoBuscado.addRutinaFaseGrupoEjercicio(rutinaFaseGrupoEjercicio);

		actualizarRutina(rutina);

		rutinaFaseGrupoBuscado = Stream
				.concat(Stream.concat(rutina.getFaseInicial().getRutinaFaseGrupoLista().stream(), rutina.getFasePrincipal().getRutinaFaseGrupoLista().stream()),
						rutina.getFaseFinal().getRutinaFaseGrupoLista().stream())
				.filter((rutinaFaseGrupo) -> rutinaFaseGrupo.getId().equals(rutinaFaseGrupoId)).findAny().orElse(null);
		for (RutinaFaseGrupoRepeticionEjercicio rutinaFaseGrupoEjercicioRevision : rutinaFaseGrupoBuscado.getRutinaFaseGrupoEjercicioLista()) {
			if (rutinaFaseGrupoEjercicioRevision.getPosicion().equals(posicion)) {
				nuevoRutinaFaseGrupoEjercicio = rutinaFaseGrupoEjercicioRevision;
				break;
			}
		}

		if (clasificarEjercicioSegunUsoPrevio) {
			clasificarEjercicioSegunUsoPrevio(rutina.getCliente(), rutina.getId(), List.of(nuevoRutinaFaseGrupoEjercicio.getEjercicio()), true);
		}

		return nuevoRutinaFaseGrupoEjercicio;
	}

	@Transactional
	@Override
	public void moverEjercicio(Long rutinaId, Long rutinaFaseGrupoRepeticionEjercicioId, Long rutinaFaseGrupoNuevoId, Integer posicionNueva) {

		var rutinaFaseGrupoRepeticionEjercicio = rutinaFaseGrupoRepeticionEjercicioRepositorio.findById(rutinaFaseGrupoRepeticionEjercicioId).orElse(null);

		// ¿Se mueve dentro del mismo grupo o a otro grupo?
		if (rutinaFaseGrupoRepeticionEjercicio.getRutinaFaseGrupoRepeticion().getRutinaFaseGrupo().getId().equals(rutinaFaseGrupoNuevoId)) {
			// Grupo actual
			moverEjercicioDentroDelMismoFaseGrupo(rutinaFaseGrupoRepeticionEjercicio, posicionNueva);
		} else {
			// Otro grupo
			moverEjercicioEnOtroFaseGrupo(rutinaFaseGrupoRepositorio.findById(rutinaFaseGrupoNuevoId).orElse(null), rutinaFaseGrupoRepeticionEjercicio, posicionNueva);
		}
	}

	private void moverEjercicioEnOtroFaseGrupo(RutinaFaseGrupo rutinaFaseGrupoNuevo, RutinaFaseGrupoRepeticionEjercicio rutinaFaseGrupoEjercicioBuscado,
			Integer posicion) {

		var posicionAnterior = rutinaFaseGrupoEjercicioBuscado.getPosicion();
		var rutinaFaseGrupoAnterior = rutinaFaseGrupoEjercicioBuscado.getRutinaFaseGrupoRepeticion().getRutinaFaseGrupo();

		// Grupo actual
		// Desplazar ejercicios que estuvieran a la derecha
		rutinaFaseGrupoAnterior.getRutinaFaseGrupoEjercicioConRepeticionesLista().stream()
				.filter(rutinaFaseGrupoEjercicioRevision -> rutinaFaseGrupoEjercicioRevision.getPosicion() > posicionAnterior)
				.forEach(rutinaFaseGrupoEjercicioRevision -> {
					rutinaFaseGrupoEjercicioRevision.setPosicion(rutinaFaseGrupoEjercicioRevision.getPosicion() - 1);
				});
		// Borrar ejercicio movido
		rutinaFaseGrupoAnterior.removeRutinaFaseGrupoEjercicio(rutinaFaseGrupoEjercicioBuscado);

		// Grupo nuevo
		// Desplazar ejercicios
		rutinaFaseGrupoNuevo.getRutinaFaseGrupoEjercicioConRepeticionesLista().stream()
				.filter(rutinaFaseGrupoEjercicioRevision -> rutinaFaseGrupoEjercicioRevision.getPosicion() >= posicion).forEach(rutinaFaseGrupoEjercicioRevision -> {
					rutinaFaseGrupoEjercicioRevision.setPosicion(rutinaFaseGrupoEjercicioRevision.getPosicion() + 1);
				});

		// Añadir ejercicio movido
		rutinaFaseGrupoEjercicioBuscado.setPosicion(posicion);
		rutinaFaseGrupoNuevo.addRutinaFaseGrupoEjercicio(rutinaFaseGrupoEjercicioBuscado);

	}

	private void moverEjercicioDentroDelMismoFaseGrupo(RutinaFaseGrupoRepeticionEjercicio rutinaFaseGrupoRepeticionEjercicio, Integer posicion) {

		var rutinaFaseGrupo = rutinaFaseGrupoRepeticionEjercicio.getRutinaFaseGrupoRepeticion().getRutinaFaseGrupo();

		// Grupo actual
		// Desplazar ejercicios a derecha o izquierda
		if (rutinaFaseGrupoRepeticionEjercicio.getPosicion() > posicion) {
			// Se movio hacia la izquierda
			// Hay que 'desplazar' hacia la derecha
			rutinaFaseGrupo.getRutinaFaseGrupoEjercicioConRepeticionesLista().stream()
					.filter(rutinaFaseGrupoEjercicioRevision -> rutinaFaseGrupoEjercicioRevision.getPosicion() >= posicion
							&& rutinaFaseGrupoEjercicioRevision.getPosicion() < rutinaFaseGrupoRepeticionEjercicio.getPosicion())
					.forEach(rutinaFaseGrupoEjercicioRevision -> {
						rutinaFaseGrupoEjercicioRevision.setPosicion(rutinaFaseGrupoEjercicioRevision.getPosicion() + 1);
					});

		} else {
			// Se movio hacia la derecha
			// Hay que 'desplazar' hacia la izquierda
			rutinaFaseGrupo.getRutinaFaseGrupoEjercicioConRepeticionesLista().stream()
					.filter(rutinaFaseGrupoEjercicioRevision -> rutinaFaseGrupoEjercicioRevision.getPosicion() <= posicion
							&& rutinaFaseGrupoEjercicioRevision.getPosicion() > rutinaFaseGrupoRepeticionEjercicio.getPosicion())
					.forEach(rutinaFaseGrupoEjercicioRevision -> {
						rutinaFaseGrupoEjercicioRevision.setPosicion(rutinaFaseGrupoEjercicioRevision.getPosicion() - 1);
					});
		}

		// Desplazar las repeticiones del ejercicio movido
		for (RutinaFaseGrupoRepeticionEjercicio rutinaFaseGrupoRepeticionEjercicioOtrasRepeticiones : rutinaFaseGrupo.getRutinaFaseGrupoEjercicioConRepeticionesLista()) {

			if (!rutinaFaseGrupoRepeticionEjercicioOtrasRepeticiones.getId().equals(rutinaFaseGrupoRepeticionEjercicio.getId())
					&& rutinaFaseGrupoRepeticionEjercicioOtrasRepeticiones.getEjercicio().getId().equals(rutinaFaseGrupoRepeticionEjercicio.getEjercicio().getId())
					&& rutinaFaseGrupoRepeticionEjercicioOtrasRepeticiones.getPosicion().equals(rutinaFaseGrupoRepeticionEjercicio.getPosicion())) {
				rutinaFaseGrupoRepeticionEjercicioOtrasRepeticiones.setPosicion(posicion);
			}

		}

		rutinaFaseGrupoRepeticionEjercicio.setPosicion(posicion);
	}

	@Transactional
	@Override
	public void borrarEjercicio(Long rutinaFaseGrupoEjercicioId) {
		var rutinaFaseGrupoRepeticionEjercicio = rutinaFaseGrupoRepeticionEjercicioRepositorio.findById(rutinaFaseGrupoEjercicioId).orElse(null);
		rutinaFaseGrupoRepeticionEjercicio.getRutinaFaseGrupoRepeticion().getRutinaFaseGrupo().removeRutinaFaseGrupoEjercicio(rutinaFaseGrupoRepeticionEjercicio);
	}

	@Transactional
	@Override
	public RutinaFaseGrupo anhadirGrupo(Long rutinaId, Long rutinaFaseId) {
		Rutina rutina = buscarRutinaPorId(rutinaId);
		RutinaFase rutinaFase = null;
		RutinaFaseGrupo rutinaFaseGrupo = new RutinaFaseGrupo();
		rutinaFaseGrupo.setRepeticiones(1);
		RutinaFaseGrupoRepeticion rutinaFaseGrupoRepeticion = new RutinaFaseGrupoRepeticion();
		rutinaFaseGrupo.addRutinaFaseGrupoRepeticionEstableciendoDatos(rutinaFaseGrupoRepeticion);

		if (rutina.getFaseInicial() != null && rutina.getFaseInicial().getId().equals(rutinaFaseId)) {
			rutinaFase = rutina.getFaseInicial();
		} else if (rutina.getFasePrincipal() != null && rutina.getFasePrincipal().getId().equals(rutinaFaseId)) {
			rutinaFase = rutina.getFasePrincipal();
		} else if (rutina.getFaseFinal() != null && rutina.getFaseFinal().getId().equals(rutinaFaseId)) {
			rutinaFase = rutina.getFaseFinal();
		}
		rutinaFaseGrupo.setPosicion(rutinaFase.getRutinaFaseGrupoLista() != null ? rutinaFase.getRutinaFaseGrupoLista().size() + 1 : 1);
		rutinaFase.addRutinaFaseGrupo(rutinaFaseGrupo);

		actualizarRutina(rutina);

		return rutinaFase.getRutinaFaseGrupoLista().get(rutinaFase.getRutinaFaseGrupoLista().size() - 1);
	}

	@Transactional
	@Override
	public void borrarGrupo(Long rutinaId, Long rutinaFaseId, Long rutinaFaseGrupoId) {
		Rutina rutina = buscarRutinaPorId(rutinaId);
		RutinaFase rutinaFase = null;

		if (rutina.getFaseInicial() != null && rutina.getFaseInicial().getId().equals(rutinaFaseId)) {
			rutinaFase = rutina.getFaseInicial();
		} else if (rutina.getFasePrincipal() != null && rutina.getFasePrincipal().getId().equals(rutinaFaseId)) {
			rutinaFase = rutina.getFasePrincipal();
		} else if (rutina.getFaseFinal() != null && rutina.getFaseFinal().getId().equals(rutinaFaseId)) {
			rutinaFase = rutina.getFaseFinal();
		}

		for (RutinaFaseGrupo rutinaFaseGrupoBorrar : rutinaFase.getRutinaFaseGrupoLista()) {
			if (rutinaFaseGrupoBorrar.getId().equals(rutinaFaseGrupoId)) {
				rutinaFase.removeRutinaFaseGrupo(rutinaFaseGrupoBorrar);
				break;
			}
		}

		// Actualizar lista
		int numeroRutinaFaseGrupo = 1;
		for (RutinaFaseGrupo rutinaFaseGrupo : rutinaFase.getRutinaFaseGrupoLista()) {
			rutinaFaseGrupo.setPosicion(numeroRutinaFaseGrupo);
			numeroRutinaFaseGrupo++;
		}

		actualizarRutina(rutina);
	}

	@Transactional
	@Override
	public RutinaEjecucion iniciarRutinaEjecucionDesdeInicio(Long rutinaId) {
		var rutinaEjecucion = new RutinaEjecucion();
		rutinaEjecucion.setRutinaId(rutinaId);
		var rutina = buscarRutinaPorId(rutinaId);
		rutinaEjecucion.setRutina(rutina);
		var cliente = rutina.getCliente();

		rutina.setClienteEjecucionAutomatica(
				cliente.getClienteConfiguracion().getEjecucionAutomatica() != null ? cliente.getClienteConfiguracion().getEjecucionAutomatica() : Boolean.TRUE);

		RutinaFaseGrupoRepeticionEjercicio rutinaFaseGrupoEjercicioAnterior = null;
		RutinaFaseGrupoRepeticionEjercicio rutinaFaseGrupoEjercicioActual = null;
		RutinaFaseGrupoRepeticionEjercicio rutinaFaseGrupoEjercicioSiguiente = null;

		// Agrupar todos los ejercicios
		var rutinaFaseGrupoEjercicioTodosLista = agruparRutinaFaseGrupoEjercicio(rutina);

		// Buscar actual y siguiente
		rutinaFaseGrupoEjercicioActual = rutinaFaseGrupoEjercicioTodosLista.get(0);
		rutinaFaseGrupoEjercicioSiguiente = rutinaFaseGrupoEjercicioTodosLista.size() > 1 ? rutinaFaseGrupoEjercicioTodosLista.get(1) : null;

		generarRutinaEjecucion(rutinaEjecucion, rutinaFaseGrupoEjercicioAnterior, rutinaFaseGrupoEjercicioActual, rutinaFaseGrupoEjercicioSiguiente,
				rutinaFaseGrupoEjercicioTodosLista, 0);

		return rutinaEjecucion;
	}

	@Transactional
	@Override
	public RutinaEjecucion iniciarRutinaEnEjercicioConcreto(Long rutinaId, Long nuevoRutinaFaseGrupoEjercicioId) {
		var rutina = buscarRutinaPorId(rutinaId);
		var cliente = rutina.getCliente();
		rutina.setClienteEjecucionAutomatica(
				cliente.getClienteConfiguracion().getEjecucionAutomatica() != null ? cliente.getClienteConfiguracion().getEjecucionAutomatica() : Boolean.TRUE);
		return iniciarRutinaEjecucionEnEjercicio(rutina, nuevoRutinaFaseGrupoEjercicioId);
	}

	@Transactional
	@Override
	public RutinaEjecucion cambiarEjercicioDeRutinaEnEjecucion(Long rutinaId, Long nuevoRutinaFaseGrupoEjercicioId) {
		return iniciarRutinaEjecucionEnEjercicio(buscarRutinaPorId(rutinaId), nuevoRutinaFaseGrupoEjercicioId);
	}

	private RutinaEjecucion iniciarRutinaEjecucionEnEjercicio(Rutina rutina, Long nuevoRutinaFaseGrupoEjercicioId) {
		var rutinaEjecucion = new RutinaEjecucion();
		rutinaEjecucion.setRutinaId(rutina.getId());
		rutinaEjecucion.setRutina(rutina);

		RutinaFaseGrupoRepeticionEjercicio rutinaFaseGrupoEjercicioAnterior = null;
		RutinaFaseGrupoRepeticionEjercicio rutinaFaseGrupoEjercicioActual = null;
		RutinaFaseGrupoRepeticionEjercicio rutinaFaseGrupoEjercicioSiguiente = null;

		// Agrupar todos los ejercicios
		var rutinaFaseGrupoEjercicioTodosLista = agruparRutinaFaseGrupoEjercicio(rutinaEjecucion.getRutina());

		// Buscar anterior, actual y siguiente
		var listIterator = rutinaFaseGrupoEjercicioTodosLista.listIterator();
		int indiceActual = 0;
		while (listIterator.hasNext()) {

			if (listIterator.hasPrevious()) {
				rutinaFaseGrupoEjercicioAnterior = rutinaFaseGrupoEjercicioTodosLista.get(listIterator.previousIndex());
			}

			indiceActual = listIterator.nextIndex();
			rutinaFaseGrupoEjercicioActual = listIterator.next();

			if (listIterator.hasNext()) {
				rutinaFaseGrupoEjercicioSiguiente = rutinaFaseGrupoEjercicioTodosLista.get(listIterator.nextIndex());
			}

			if (rutinaFaseGrupoEjercicioActual.getId().equals(nuevoRutinaFaseGrupoEjercicioId)) {
				break;
			} else {
				rutinaFaseGrupoEjercicioAnterior = null;
				rutinaFaseGrupoEjercicioActual = null;
				rutinaFaseGrupoEjercicioSiguiente = null;
			}

		}

		generarRutinaEjecucion(rutinaEjecucion, rutinaFaseGrupoEjercicioAnterior, rutinaFaseGrupoEjercicioActual, rutinaFaseGrupoEjercicioSiguiente,
				rutinaFaseGrupoEjercicioTodosLista, indiceActual);

		return rutinaEjecucion;
	}

	private void generarRutinaEjecucion(RutinaEjecucion rutinaEjecucion, RutinaFaseGrupoRepeticionEjercicio rutinaFaseGrupoEjercicioAnterior,
			RutinaFaseGrupoRepeticionEjercicio rutinaFaseGrupoEjercicioActual, RutinaFaseGrupoRepeticionEjercicio rutinaFaseGrupoEjercicioSiguiente,
			List<RutinaFaseGrupoRepeticionEjercicio> rutinaFaseGrupoEjercicioTodosLista, int indiceActual) {
		var rutina = rutinaEjecucion.getRutina();
		var cliente = rutina.getCliente();

		if (rutina.getFaseInicial() != null
				&& rutina.getFaseInicial().getId().equals(rutinaFaseGrupoEjercicioActual.getRutinaFaseGrupoRepeticion().getRutinaFaseGrupo().getRutinaFase().getId())) {
			rutinaEjecucion.setFaseColor(Constantes.COLOR_FASE_INICIAL);
		}

		if (rutina.getFasePrincipal() != null
				&& rutina.getFasePrincipal().getId().equals(rutinaFaseGrupoEjercicioActual.getRutinaFaseGrupoRepeticion().getRutinaFaseGrupo().getRutinaFase().getId())) {
			rutinaEjecucion.setFaseColor(Constantes.COLOR_FASE_PRINCIPAL);
		}

		if (rutina.getFaseFinal() != null
				&& rutina.getFaseFinal().getId().equals(rutinaFaseGrupoEjercicioActual.getRutinaFaseGrupoRepeticion().getRutinaFaseGrupo().getRutinaFase().getId())) {
			rutinaEjecucion.setFaseColor(Constantes.COLOR_FASE_FINAL);
		}

		rutinaEjecucion.setPorcentajeEjecucion(Integer.valueOf(((indiceActual + 1) * 100) / rutinaFaseGrupoEjercicioTodosLista.size()));

		rutinaEjecucion.setRutinaFaseActualInfo(rutinaFaseGrupoEjercicioActual.getRutinaFaseGrupoRepeticion().getRutinaFaseGrupo().getRutinaFase().getNombre());

		String rutinaFaseGrupoActualInfo = "Circuito " + rutinaFaseGrupoEjercicioActual.getRutinaFaseGrupoRepeticion().getRutinaFaseGrupo().getPosicion().toString();

		if (rutinaFaseGrupoEjercicioActual.getRutinaFaseGrupoRepeticion().getRutinaFaseGrupo().getRutinaFase().getRutinaFaseGrupoLista().size() > 1) {
			rutinaFaseGrupoActualInfo = rutinaFaseGrupoActualInfo + "/"
					+ rutinaFaseGrupoEjercicioActual.getRutinaFaseGrupoRepeticion().getRutinaFaseGrupo().getRutinaFase().getRutinaFaseGrupoLista().size();
		}

		rutinaFaseGrupoActualInfo = rutinaFaseGrupoActualInfo + " Serie " + rutinaFaseGrupoEjercicioActual.getRutinaFaseGrupoRepeticion().getNumeroRepeticion() + "/"
				+ rutinaFaseGrupoEjercicioActual.getRutinaFaseGrupoRepeticion().getRutinaFaseGrupo().getRepeticiones();

		rutinaEjecucion.setRutinaFaseGrupoActualInfo(rutinaFaseGrupoActualInfo);

		rutinaEjecucion.setRutinaFaseGrupoEjercicioActualInfo(rutinaFaseGrupoEjercicioActual.getPosicion() + "/"
				+ rutinaFaseGrupoEjercicioActual.getRutinaFaseGrupoRepeticion().getRutinaFaseGrupoRepeticionEjercicioLista().size());
		rutinaEjecucion.setRutinaFaseGrupoEjercicioTotalesInfo((indiceActual + 1) + "/" + rutinaFaseGrupoEjercicioTodosLista.size());
		rutinaEjecucion.setRutinaFaseGrupoEjercicioAnterior(rutinaFaseGrupoEjercicioAnterior);
		rutinaEjecucion.setRutinaFaseGrupoEjercicioActual(rutinaFaseGrupoEjercicioActual);
		rutinaEjecucion.setRutinaFaseGrupoEjercicioSiguiente(rutinaFaseGrupoEjercicioSiguiente);

		var ejercicioPresentacionSb = new StringBuilder();
		if (rutinaEjecucion.getRutinaFaseGrupoEjercicioActual().getEjercicio().getEsUnilateral()) {
			if (rutinaEjecucion.getRutinaFaseGrupoEjercicioActual().getRepeticiones() > 1) {
				ejercicioPresentacionSb.append(rutinaEjecucion.getRutinaFaseGrupoEjercicioActual().getRepeticiones() + "+"
						+ rutinaEjecucion.getRutinaFaseGrupoEjercicioActual().getRepeticiones() + " Rep.");
			} else {
				ejercicioPresentacionSb.append(rutinaEjecucion.getRutinaFaseGrupoEjercicioActual().getTiempo() + "+"
						+ rutinaEjecucion.getRutinaFaseGrupoEjercicioActual().getTiempo() + " Seg.");
			}
		} else {
			if (rutinaEjecucion.getRutinaFaseGrupoEjercicioActual().getRepeticiones() > 1) {
				ejercicioPresentacionSb.append(rutinaEjecucion.getRutinaFaseGrupoEjercicioActual().getRepeticiones() + " Rep.");
			} else {
				ejercicioPresentacionSb.append(rutinaEjecucion.getRutinaFaseGrupoEjercicioActual().getTiempo() + " Seg.");
			}
		}

		rutinaEjecucion.setEjercicioPresentacion(ejercicioPresentacionSb.toString());

		rutinaEjecucion.setEjercicioNombre(rutinaEjecucion.getRutinaFaseGrupoEjercicioActual().getEjercicio().getNombre());
		rutinaEjecucion.setEjercicioDescripcion(rutinaEjecucion.getRutinaFaseGrupoEjercicioActual().getEjercicio().getDescripcion());
		rutinaEjecucion.setEjercicioTieneAudio(rutinaEjecucion.getRutinaFaseGrupoEjercicioActual().getEjercicio().getTieneAudio());
		rutinaEjecucion.setComentarios(rutinaEjecucion.getRutinaFaseGrupoEjercicioActual().getComentarios());

		var tiempoDescansoEntreEjercicios = rutina.getClienteTiempoDescansoEntreEjercicios();
		if (tiempoDescansoEntreEjercicios == null) {
			tiempoDescansoEntreEjercicios = cliente.getClienteConfiguracion().getTiempoDescansoEntreEjercicios();
			if (tiempoDescansoEntreEjercicios == null) {
				tiempoDescansoEntreEjercicios = rutina.getEntrenadorTiempoDescansoEntreEjercicios() != null ? rutina.getEntrenadorTiempoDescansoEntreEjercicios()
						: parametroConfiguracionServicio.buscarParametroConfiguracionIntegerValor(Constantes.PC_TIEMPO_DESCANSO_ENTRE_EJERCICIOS);
			}
		}

		var tiempoDescansoEntreUnilaterales = rutina.getClienteTiempoDescansoEntreUnilaterales();
		if (tiempoDescansoEntreUnilaterales == null) {
			tiempoDescansoEntreUnilaterales = cliente.getClienteConfiguracion().getTiempoDescansoEntreUnilaterales();
			if (tiempoDescansoEntreUnilaterales == null) {
				tiempoDescansoEntreUnilaterales = rutina.getEntrenadorTiempoDescansoEntreUnilaterales() != null ? rutina.getEntrenadorTiempoDescansoEntreUnilaterales()
						: parametroConfiguracionServicio.buscarParametroConfiguracionIntegerValor(Constantes.PC_TIEMPO_DESCANSO_ENTRE_UNILATERALES);
			}
		}

		var tiempoEjercicioPorRepeticiones = rutina.getClienteTiempoEjercicioPorRepeticiones();
		if (tiempoEjercicioPorRepeticiones == null) {
			tiempoEjercicioPorRepeticiones = cliente.getClienteConfiguracion().getTiempoEjercicioPorRepeticiones();
			if (tiempoEjercicioPorRepeticiones == null) {
				tiempoEjercicioPorRepeticiones = rutina.getEntrenadorTiempoEjercicioPorRepeticiones() != null ? rutina.getEntrenadorTiempoEjercicioPorRepeticiones()
						: parametroConfiguracionServicio.buscarParametroConfiguracionIntegerValor(Constantes.PC_TIEMPO_EJERCICIO_POR_REPETICIONES);
			}
		}

		rutinaEjecucion.setTiempoDescansoEntreEjercicios(tiempoDescansoEntreEjercicios);
		rutinaEjecucion.setTiempoDescansoEntreUnilaterales(tiempoDescansoEntreUnilaterales);
		rutinaEjecucion.setTiempoEjercicioPorRepeticiones(tiempoEjercicioPorRepeticiones);

		var tiempoDescansoEntreEjerciciosPorDefecto = cliente.getClienteConfiguracion().getTiempoDescansoEntreEjercicios();
		if (tiempoDescansoEntreEjerciciosPorDefecto == null) {
			tiempoDescansoEntreEjerciciosPorDefecto = rutina.getEntrenadorTiempoDescansoEntreEjercicios() != null ? rutina.getEntrenadorTiempoDescansoEntreEjercicios()
					: parametroConfiguracionServicio.buscarParametroConfiguracionIntegerValor(Constantes.PC_TIEMPO_DESCANSO_ENTRE_EJERCICIOS);
		}

		var tiempoDescansoEntreUnilateralesPorDefecto = cliente.getClienteConfiguracion().getTiempoDescansoEntreUnilaterales();
		if (tiempoDescansoEntreUnilateralesPorDefecto == null) {
			tiempoDescansoEntreUnilateralesPorDefecto = rutina.getEntrenadorTiempoDescansoEntreUnilaterales() != null
					? rutina.getEntrenadorTiempoDescansoEntreUnilaterales()
					: parametroConfiguracionServicio.buscarParametroConfiguracionIntegerValor(Constantes.PC_TIEMPO_DESCANSO_ENTRE_UNILATERALES);
		}

		var tiempoEjercicioPorRepeticionesPorDefecto = cliente.getClienteConfiguracion().getTiempoEjercicioPorRepeticiones();
		if (tiempoEjercicioPorRepeticionesPorDefecto == null) {
			tiempoEjercicioPorRepeticionesPorDefecto = rutina.getEntrenadorTiempoEjercicioPorRepeticiones() != null ? rutina.getEntrenadorTiempoEjercicioPorRepeticiones()
					: parametroConfiguracionServicio.buscarParametroConfiguracionIntegerValor(Constantes.PC_TIEMPO_EJERCICIO_POR_REPETICIONES);
		}

		rutinaEjecucion.setTiempoDescansoEntreEjerciciosPorDefecto(tiempoDescansoEntreEjerciciosPorDefecto);
		rutinaEjecucion.setTiempoDescansoEntreUnilateralesPorDefecto(tiempoDescansoEntreUnilateralesPorDefecto);
		rutinaEjecucion.setTiempoEjercicioPorRepeticionesPorDefecto(tiempoEjercicioPorRepeticionesPorDefecto);

		rutinaEjecucion
				.setTiempo(rutinaEjecucion.getRutinaFaseGrupoEjercicioActual().getTiempo() != null && rutinaEjecucion.getRutinaFaseGrupoEjercicioActual().getTiempo() > 0
						? rutinaEjecucion.getRutinaFaseGrupoEjercicioActual().getTiempo()
						: tiempoEjercicioPorRepeticiones);
		rutinaEjecucion.setRepeticiones(rutinaEjecucion.getRutinaFaseGrupoEjercicioActual().getRepeticiones());
		rutinaEjecucion.setEjecucionAutomatica(rutina.getClienteEjecucionAutomatica());

		var videoUrl = urlEstaticosEjerciciosVideos + rutinaEjecucion.getRutinaFaseGrupoEjercicioActual().getEjercicio().getId() + "_x264.mp4?"
				+ rutinaEjecucion.getRutinaFaseGrupoEjercicioActual().getEjercicio().getRefrescoMultimedia();
		rutinaEjecucion.setVideoUrl(videoUrl);

		if (rutinaEjecucion.isEjercicioTieneAudio()) {
			var audioUrl = urlEstaticosEjerciciosAudios + rutinaEjecucion.getRutinaFaseGrupoEjercicioActual().getEjercicio().getId() + ".mp3";
			rutinaEjecucion.setAudioUrl(audioUrl);
		}

		if (rutinaEjecucion.getRutinaFaseGrupoEjercicioSiguiente() != null) {
			var videoPrecargaUrl = urlEstaticosEjerciciosVideos + rutinaEjecucion.getRutinaFaseGrupoEjercicioSiguiente().getEjercicio().getId() + "_x264.mp4?"
					+ rutinaEjecucion.getRutinaFaseGrupoEjercicioActual().getEjercicio().getRefrescoMultimedia();
			rutinaEjecucion.setVideoPrecargaUrl(videoPrecargaUrl);
			rutinaEjecucion.setRutinaFaseGrupoEjercicioSiguienteId(rutinaEjecucion.getRutinaFaseGrupoEjercicioSiguiente().getId());
		}

		if (rutinaEjecucion.getRutinaFaseGrupoEjercicioAnterior() != null) {
			rutinaEjecucion.setRutinaFaseGrupoEjercicioAnteriorId(rutinaEjecucion.getRutinaFaseGrupoEjercicioAnterior().getId());
		}

		if (rutinaEjecucion.getRutinaFaseGrupoEjercicioActual().getEjercicio().getEsUnilateral()) {
			rutinaEjecucion.setFinalizado(false);
		} else {
			rutinaEjecucion.setFinalizado(true);
		}
	}

	@Transactional
	@Override
	public List<RutinaFaseGrupoRepeticionEjercicio> agruparRutinaFaseGrupoEjercicio(Rutina rutina) {
		List<RutinaFaseGrupoRepeticionEjercicio> rutinaFaseGrupoEjercicioTodosLista = new ArrayList<>();

		if (rutina.getFaseInicial() != null && !rutina.getFaseInicial().getRutinaFaseGrupoLista().isEmpty()) {
			rutina.getFaseInicial().getRutinaFaseGrupoLista().stream()
					.forEach(it -> rutinaFaseGrupoEjercicioTodosLista.addAll(it.getRutinaFaseGrupoEjercicioConRepeticionesLista()));
		}

		if (rutina.getFasePrincipal() != null && !rutina.getFasePrincipal().getRutinaFaseGrupoLista().isEmpty()) {
			rutina.getFasePrincipal().getRutinaFaseGrupoLista().stream()
					.forEach(it -> rutinaFaseGrupoEjercicioTodosLista.addAll(it.getRutinaFaseGrupoEjercicioConRepeticionesLista()));
		}

		if (rutina.getFaseFinal() != null && !rutina.getFaseFinal().getRutinaFaseGrupoLista().isEmpty()) {
			rutina.getFaseFinal().getRutinaFaseGrupoLista().stream()
					.forEach(it -> rutinaFaseGrupoEjercicioTodosLista.addAll(it.getRutinaFaseGrupoEjercicioConRepeticionesLista()));
		}

		return rutinaFaseGrupoEjercicioTodosLista;
	}

	@Transactional
	@Override
	public List<RutinaFaseGrupoRepeticionEjercicio> agruparRutinaFaseGrupoEjercicioSinRepeticiones(Rutina rutina) {
		List<RutinaFaseGrupoRepeticionEjercicio> rutinaFaseGrupoEjercicioTodosLista = new ArrayList<>();

		if (rutina.getFaseInicial() != null && !rutina.getFaseInicial().getRutinaFaseGrupoLista().isEmpty()) {
			rutina.getFaseInicial().getRutinaFaseGrupoLista().stream().forEach(it -> rutinaFaseGrupoEjercicioTodosLista.addAll(it.getRutinaFaseGrupoEjercicioLista()));
		}

		if (rutina.getFasePrincipal() != null && !rutina.getFasePrincipal().getRutinaFaseGrupoLista().isEmpty()) {
			rutina.getFasePrincipal().getRutinaFaseGrupoLista().stream().forEach(it -> rutinaFaseGrupoEjercicioTodosLista.addAll(it.getRutinaFaseGrupoEjercicioLista()));
		}

		if (rutina.getFaseFinal() != null && !rutina.getFaseFinal().getRutinaFaseGrupoLista().isEmpty()) {
			rutina.getFaseFinal().getRutinaFaseGrupoLista().stream().forEach(it -> rutinaFaseGrupoEjercicioTodosLista.addAll(it.getRutinaFaseGrupoEjercicioLista()));
		}

		return rutinaFaseGrupoEjercicioTodosLista;
	}

	@Transactional
	@Override
	public Rutina actualizarRutinaDatosGenerales(Rutina rutina, Long entrenadorId) {
		var rutinaParaActualizar = buscarRutinaPorId(rutina.getId());

		rutinaParaActualizar.setNombre(rutina.getNombre());
		rutinaParaActualizar.setComentariosEntrenador(rutina.getComentariosEntrenador());
		rutinaParaActualizar.setEntrenadorTiempoDescansoEntreEjercicios(rutina.getEntrenadorTiempoDescansoEntreEjercicios());

		if (!rutinaParaActualizar.getEsPlantilla()) {
			rutinaParaActualizar.setEstado(rutina.getEstado());
			actualizarSet(rutinaParaActualizar.getRutinaFechaLista(), rutina.getRutinaFechaLista());
			rutinaParaActualizar.setEntrenador(entrenadorServicio.buscarEntrenadorPorId(entrenadorId));
		}

		return rutinaParaActualizar;
	}

	private <T> void actualizarSet(Set<T> original, Set<T> nuevo) {
		original.addAll(nuevo);
		original.retainAll(nuevo);
	}

	@Transactional
	@Override
	public void actualizarRutinaFaseGrupo(RutinaFaseGrupo rutinaFaseGrupo) {
		RutinaFaseGrupo rutinaFaseGrupoParaActualizar = rutinaFaseGrupoRepositorio.findById(rutinaFaseGrupo.getId()).get();

		// Se anhadieron repeticiones
		if (rutinaFaseGrupoParaActualizar.getRepeticiones() < rutinaFaseGrupo.getRepeticiones()) {

			for (int i = 0; i < (rutinaFaseGrupo.getRepeticiones() - rutinaFaseGrupoParaActualizar.getRepeticiones()); i++) {
				RutinaFaseGrupoRepeticion rutinaFaseGrupoRepeticion = new RutinaFaseGrupoRepeticion();
				rutinaFaseGrupoParaActualizar.addRutinaFaseGrupoRepeticionEstableciendoDatos(rutinaFaseGrupoRepeticion);
			}
			rutinaFaseGrupoParaActualizar.setRepeticiones(rutinaFaseGrupo.getRepeticiones());
			rutinaFaseGrupoRepositorio.save(rutinaFaseGrupoParaActualizar);
		}

		// Se quitaron repeticiones
		if (rutinaFaseGrupoParaActualizar.getRepeticiones() > rutinaFaseGrupo.getRepeticiones()) {

			// Ver repeticiones a borrar
			rutinaFaseGrupoParaActualizar.getRutinaFaseGrupoRepeticionLista().removeIf(it -> it.getNumeroRepeticion() > rutinaFaseGrupo.getRepeticiones());
			rutinaFaseGrupoParaActualizar.setRepeticiones(rutinaFaseGrupo.getRepeticiones());
			rutinaFaseGrupoRepositorio.save(rutinaFaseGrupoParaActualizar);
		}
	}

	@Transactional
	@Override
	public void actualizarRutinaFaseGrupoRepeticionEjercicio(RutinaFaseGrupoRepeticionEjercicio rutinaFaseGrupoRepeticionEjercicio) {

		RutinaFaseGrupoRepeticionEjercicio rutinaFaseGrupoRepeticionEjercicioParaActualizar = rutinaFaseGrupoRepeticionEjercicioRepositorio
				.findById(rutinaFaseGrupoRepeticionEjercicio.getId()).get();

		// Actualizo de la repeticion principal
		rutinaFaseGrupoRepeticionEjercicioParaActualizar.setRepeticiones(rutinaFaseGrupoRepeticionEjercicio.getRepeticiones());
		rutinaFaseGrupoRepeticionEjercicioParaActualizar.setTiempo(rutinaFaseGrupoRepeticionEjercicio.getTiempo());
		rutinaFaseGrupoRepeticionEjercicioParaActualizar.setComentarios(rutinaFaseGrupoRepeticionEjercicio.getComentarios());
		rutinaFaseGrupoRepeticionEjercicioRepositorio.save(rutinaFaseGrupoRepeticionEjercicioParaActualizar);

		// Actualizo del resto de peticiones
		for (RutinaFaseGrupoRepeticionEjercicio rutinaFaseGrupoRepeticionEjercicioOtrasRepeticiones : rutinaFaseGrupoRepeticionEjercicioParaActualizar
				.getRutinaFaseGrupoRepeticion().getRutinaFaseGrupo().getRutinaFaseGrupoEjercicioConRepeticionesLista()) {

			if (!rutinaFaseGrupoRepeticionEjercicioOtrasRepeticiones.getId().equals(rutinaFaseGrupoRepeticionEjercicioParaActualizar.getId())
					&& rutinaFaseGrupoRepeticionEjercicioOtrasRepeticiones.getEjercicio().getId()
							.equals(rutinaFaseGrupoRepeticionEjercicioParaActualizar.getEjercicio().getId())
					&& rutinaFaseGrupoRepeticionEjercicioOtrasRepeticiones.getPosicion().equals(rutinaFaseGrupoRepeticionEjercicioParaActualizar.getPosicion())) {
				rutinaFaseGrupoRepeticionEjercicioOtrasRepeticiones.setRepeticiones(rutinaFaseGrupoRepeticionEjercicio.getRepeticiones());
				rutinaFaseGrupoRepeticionEjercicioOtrasRepeticiones.setTiempo(rutinaFaseGrupoRepeticionEjercicio.getTiempo());
				rutinaFaseGrupoRepeticionEjercicioOtrasRepeticiones.setComentarios(rutinaFaseGrupoRepeticionEjercicio.getComentarios());
				rutinaFaseGrupoRepeticionEjercicioRepositorio.save(rutinaFaseGrupoRepeticionEjercicioOtrasRepeticiones);
			}

		}

	}

	@Transactional
	@Override
	public Set<Material> agruparRutinaFaseGrupoEjercicioMaterial(Rutina rutina) {
		Set<Material> materialLista = new HashSet<>();
		for (RutinaFaseGrupoRepeticionEjercicio rutinaFaseGrupoRepeticionEjercicio : agruparRutinaFaseGrupoEjercicio(rutina)) {
			materialLista.addAll(rutinaFaseGrupoRepeticionEjercicio.getEjercicio().getMaterialLista());
		}
		return materialLista;
	}

	@Transactional
	@Override
	public void valorarRutina(Long clienteId, Long rutinaId, String valoracion) {
		var cliente = clienteServicio.buscarClientePorId(clienteId);
		var rutina = buscarRutinaPorId(rutinaId);

		if (StringUtils.isBlank(valoracion)) {
			valoracion = "Sin valoración";
		} else {
			if (rutina.getComentariosCliente() != null) {
				rutina.setComentariosCliente("[" + FechaUtil.formatearFecha(ZonedDateTime.now()) + "] " + valoracion + "\r\n" + rutina.getComentariosCliente());
			} else {
				rutina.setComentariosCliente("[" + FechaUtil.formatearFecha(ZonedDateTime.now()) + "] " + valoracion);
			}
			notificacionServicio.notificarClienteDejaComentario(cliente, "[" + FechaUtil.formatearFecha(ZonedDateTime.now()) + "] " + valoracion, rutina.getNombre());
		}

	}

	@Transactional
	@Override
	public void borrarRutina(Rutina rutina) {
		rutinaRepositorio.delete(rutina);
	}

	@Transactional
	@Override
	public List<Rutina> buscarRutinaPorClienteFechaCreacion(Long clienteId, ZonedDateTime fechaCreacion) {
		return rutinaRepositorio.buscarRutinaPorClienteFechaCreacion(clienteId, fechaCreacion);
	}

	private List<Rutina> buscarRutinaPorClienteFechaCreacionEntreFechas(Long clienteId, ZonedDateTime fechaCreacionDesde, ZonedDateTime fechaCreacionHasta) {
		return rutinaRepositorio.buscarRutinaPorClienteFechaCreacionEntreFechas(clienteId, fechaCreacionDesde, fechaCreacionHasta);
	}

	@Transactional
	@Override
	public Rutina buscarRutinaParaElaboracion(Long rutinaId) {
		var rutinaParaElaborar = buscarRutinaPorId(rutinaId);
		return rutinaParaElaborar;
	}

//	@Transactional
//	@Override
//	public void clasificarEjercicioSegunUsoPrevio(Cliente cliente, Long rutinaId, List<Ejercicio> ejercicioLista) {
//		var ejercicioListaRutinaActual = new HashSet<Ejercicio>();
//		Rutina rutinaActual = null;
//
//		// Partir de la suposición de que ya hay rutinas nuevas, que esta en los 5 dias
//		// previos a renovación real
//		var fechaInicioActual = clienteServicio.calcularProximaSuscripcionFechaPeriodoDesde(cliente.getSuscripcion());
//		int mesesReferencia = 0;
//		var rutinaActualLista = buscarRutinaPorClienteFechaCreacion(cliente.getId(), fechaInicioActual);
//
//		// Si no hay rutinas, es que aun estamos en medio de suscripcion
//		if (rutinaActualLista.isEmpty()) {
//			// Estamos en periodo donde ya hay nuevas rutinas, calculamos fecha de nuevas
//			// rutinas ya creadas
//			fechaInicioActual = cliente.getSuscripcion().getFechaPeriodoDesde();
//			mesesReferencia = 1;
//			rutinaActualLista = buscarRutinaPorClienteFechaCreacion(cliente.getId(), fechaInicioActual);
//		}
//
//		for (Rutina rutina : rutinaActualLista) {
//			// Descartar la que se va a elaborar claro
//			if (rutinaId == null || !rutina.getId().equals(rutinaId)) {
//				ejercicioListaRutinaActual.addAll(rutina.getEjercicioTodos());
//			} else {
//				rutinaActual = rutina;
//			}
//		}
//
//		var ejercicioListaRutina1MesAntes = new HashSet<Ejercicio>();
//		var fechaInicio1MesAntes = cliente.getSuscripcion().getFechaPeriodoDesde().minusMonths(0 + mesesReferencia);
//		var rutina1MesAntesLista = buscarRutinaPorClienteFechaCreacion(cliente.getId(), fechaInicio1MesAntes);
//		if (rutina1MesAntesLista.isEmpty()) {
//			// Quiza paro unos dias y reactivo, le damos margen de 15 dias
//			var fechaInicio1MesAntesConMargen = fechaInicio1MesAntes.minusDays(15);
//			rutina1MesAntesLista = buscarRutinaPorClienteFechaCreacionEntreFechas(cliente.getId(), fechaInicio1MesAntesConMargen, fechaInicio1MesAntes);
//		}
//
//		for (Rutina rutina : rutina1MesAntesLista) {
//			ejercicioListaRutina1MesAntes.addAll(rutina.getEjercicioTodos());
//		}
//
//		var ejercicioListaRutina2MesAntes = new HashSet<Ejercicio>();
//		var fechaInicio2MesAntes = cliente.getSuscripcion().getFechaPeriodoDesde().minusMonths(1 + mesesReferencia);
//		var rutina2MesAntesLista = buscarRutinaPorClienteFechaCreacion(cliente.getId(), fechaInicio2MesAntes);
//		if (rutina2MesAntesLista.isEmpty()) {
//			// Quiza paro unos dias y reactivo, le damos margen de 15 dias
//			var fechaInicio2MesAntesConMargen = fechaInicio2MesAntes.minusDays(15);
//			rutina2MesAntesLista = buscarRutinaPorClienteFechaCreacionEntreFechas(cliente.getId(), fechaInicio2MesAntesConMargen, fechaInicio2MesAntes);
//		}
//		for (Rutina rutina : rutina2MesAntesLista) {
//			ejercicioListaRutina2MesAntes.addAll(rutina.getEjercicioTodos());
//		}
//
//		var ejercicioListaRutina3MesAntes = new HashSet<Ejercicio>();
//		var fechaInicio3MesAntes = cliente.getSuscripcion().getFechaPeriodoDesde().minusMonths(2 + mesesReferencia);
//		var rutina3MesAntesLista = buscarRutinaPorClienteFechaCreacion(cliente.getId(), fechaInicio3MesAntes);
//		if (rutina3MesAntesLista.isEmpty()) {
//			// Quiza paro unos dias y reactivo, le damos margen de 15 dias
//			var fechaInicio3MesAntesConMargen = fechaInicio3MesAntes.minusDays(15);
//			rutina3MesAntesLista = buscarRutinaPorClienteFechaCreacionEntreFechas(cliente.getId(), fechaInicio3MesAntesConMargen, fechaInicio3MesAntes);
//		}
//		for (Rutina rutina : rutina3MesAntesLista) {
//			ejercicioListaRutina3MesAntes.addAll(rutina.getEjercicioTodos());
//		}
//
//		var ejercicioListaRutina4a12MesAntes = new HashSet<Ejercicio>();
//		var fechaInicio4a12MesAntes = cliente.getSuscripcion().getFechaPeriodoDesde().minusMonths(3 + mesesReferencia);
//		var rutina4a12MesAntesLista = buscarRutinaPorClienteFechaCreacion(cliente.getId(), fechaInicio4a12MesAntes);
//		if (rutina4a12MesAntesLista.isEmpty()) {
//			// Quiza paro unos dias y reactivo, le damos margen de 15 dias
//			var fechaInicio4a12MesAntesConMargen = fechaInicio4a12MesAntes.minusDays(15);
//			rutina4a12MesAntesLista = buscarRutinaPorClienteFechaCreacionEntreFechas(cliente.getId(), fechaInicio4a12MesAntesConMargen, fechaInicio4a12MesAntes);
//		}
//		for (Rutina rutina : rutina4a12MesAntesLista) {
//			ejercicioListaRutina4a12MesAntes.addAll(rutina.getEjercicioTodos());
//		}
//
//		// A la lista de ejercicios hay que añadir los de la rutina actual, por que si
//		// algún ejercicio de la rutina actual no aparece en la busqueda por filtros, no
//		// se marcara nunca. Esto puede suceder fácilmente en la carga de una plantilla.
//		var ejercicioListaConEjerciciosDeRutina = new ArrayList<>(ejercicioLista);
//		ejercicioListaConEjerciciosDeRutina.addAll(rutinaActual.getEjercicioTodos());
//
//		for (Ejercicio ejercicio : ejercicioListaConEjerciciosDeRutina) {
//			var yaEncontrado = false;
//			for (Ejercicio ejercicioActual : ejercicioListaRutinaActual) {
//				if (ejercicioActual.getId().equals(ejercicio.getId())) {
//
//					// Marcar
//					ejercicio.setHtmlClass(EjercicioUltimoUsoTipoEnum.EJERCICIO_USO_ACTUAL.getCssClass());
//					ejercicio.setEjercicioUltimoUsoTipoEnum(EjercicioUltimoUsoTipoEnum.EJERCICIO_USO_ACTUAL);
//					yaEncontrado = true;
//					break;
//				}
//			}
//
//			if (!yaEncontrado) {
//				for (Ejercicio ejercicio1mes : ejercicioListaRutina1MesAntes) {
//					if (ejercicio1mes.getId().equals(ejercicio.getId())) {
//
//						// Marcar
//						ejercicio.setHtmlClass(EjercicioUltimoUsoTipoEnum.EJERCICIO_USO_UN_MES.getCssClass());
//						ejercicio.setEjercicioUltimoUsoTipoEnum(EjercicioUltimoUsoTipoEnum.EJERCICIO_USO_UN_MES);
//						yaEncontrado = true;
//						break;
//					}
//				}
//			}
//
//			if (!yaEncontrado) {
//				for (Ejercicio ejercicio2mes : ejercicioListaRutina2MesAntes) {
//					if (ejercicio2mes.getId().equals(ejercicio.getId())) {
//
//						// Marcar
//						ejercicio.setHtmlClass(EjercicioUltimoUsoTipoEnum.EJERCICIO_USO_DOS_MESES.getCssClass());
//						ejercicio.setEjercicioUltimoUsoTipoEnum(EjercicioUltimoUsoTipoEnum.EJERCICIO_USO_DOS_MESES);
//						yaEncontrado = true;
//						break;
//					}
//				}
//			}
//
//			if (!yaEncontrado) {
//				for (Ejercicio ejercicio3mes : ejercicioListaRutina3MesAntes) {
//					if (ejercicio3mes.getId().equals(ejercicio.getId())) {
//
//						// Marcar
//						ejercicio.setHtmlClass(EjercicioUltimoUsoTipoEnum.EJERCICIO_USO_TRES_MESES.getCssClass());
//						ejercicio.setEjercicioUltimoUsoTipoEnum(EjercicioUltimoUsoTipoEnum.EJERCICIO_USO_TRES_MESES);
//						yaEncontrado = true;
//						break;
//					}
//				}
//			}
//
//			if (!yaEncontrado) {
//				for (Ejercicio ejercicio4a12mes : ejercicioListaRutina4a12MesAntes) {
//					if (ejercicio4a12mes.getId().equals(ejercicio.getId())) {
//
//						// Marcar
//						ejercicio.setHtmlClass(EjercicioUltimoUsoTipoEnum.EJERCICIO_USO_CUATRO_DOCE_MESES.getCssClass());
//						ejercicio.setEjercicioUltimoUsoTipoEnum(EjercicioUltimoUsoTipoEnum.EJERCICIO_USO_CUATRO_DOCE_MESES);
//						yaEncontrado = true;
//						break;
//					}
//				}
//			}
//
//			// Ahora marcar si el cliente no tiene el material
//			if (ejercicio.getMaterialLista() != null && !ejercicio.getMaterialLista().isEmpty()
//					&& !cliente.getMaterialLista().containsAll(ejercicio.getMaterialLista())) {
//				ejercicio.setClienteNoTieneTodoMaterial(true);
//			}
//		}
//	}

	@Transactional
	@Override
	public void clasificarEjercicioSegunUsoPrevio(Cliente cliente, Long rutinaId, List<Ejercicio> ejercicioLista, boolean clasificarRutinaEjercicioLista) {

		var ejercicioClasificacionUltimoUso = ejercicioUltimoUsoCache.getClasificacionEjercicioUltimoUso(cliente.getId(),
				cliente.getSuscripcion().getFechaUltimaActivacion(), cliente.getSuscripcion().getMesesActiva(), cliente.getSuscripcion().getFechaPeriodoDesde(),
				rutinaId);

		var ejercicioListaRutinaActual = ejercicioClasificacionUltimoUso.get(0);
		var ejercicioListaRutina1MesAntes = ejercicioClasificacionUltimoUso.get(1);
		var ejercicioListaRutina2MesAntes = ejercicioClasificacionUltimoUso.get(2);
		var ejercicioListaRutina3MesAntes = ejercicioClasificacionUltimoUso.get(3);
		var ejercicioListaRutina4a12MesAntes = ejercicioClasificacionUltimoUso.get(4);

		// A la lista de ejercicios hay que añadir los de la rutina actual, por que si
		// algún ejercicio de la rutina actual no aparece en la busqueda por filtros, no
		// se marcara nunca. Esto puede suceder fácilmente en la carga de una plantilla.
		var ejercicioListaFinalParaClasificar = new ArrayList<>(ejercicioLista);
		if (clasificarRutinaEjercicioLista) {
			ejercicioListaFinalParaClasificar.addAll(buscarRutinaPorId(rutinaId).getEjercicioTodos());
		}

		for (Ejercicio ejercicio : ejercicioListaFinalParaClasificar) {
			var yaEncontrado = false;

			for (Long ejercicioActualId : ejercicioListaRutinaActual) {
				if (ejercicioActualId.equals(ejercicio.getId())) {

					// Marcar
					ejercicio.setHtmlClass(EjercicioUltimoUsoTipoEnum.EJERCICIO_USO_ACTUAL.getCssClass());
					ejercicio.setEjercicioUltimoUsoTipoEnum(EjercicioUltimoUsoTipoEnum.EJERCICIO_USO_ACTUAL);
					yaEncontrado = true;
					break;
				}
			}

			if (!yaEncontrado) {
				for (Long ejercicio1mesId : ejercicioListaRutina1MesAntes) {
					if (ejercicio1mesId.equals(ejercicio.getId())) {

						// Marcar
						ejercicio.setHtmlClass(EjercicioUltimoUsoTipoEnum.EJERCICIO_USO_UN_MES.getCssClass());
						ejercicio.setEjercicioUltimoUsoTipoEnum(EjercicioUltimoUsoTipoEnum.EJERCICIO_USO_UN_MES);
						yaEncontrado = true;
						break;
					}
				}
			}

			if (!yaEncontrado) {
				for (Long ejercicio2mesId : ejercicioListaRutina2MesAntes) {
					if (ejercicio2mesId.equals(ejercicio.getId())) {

						// Marcar
						ejercicio.setHtmlClass(EjercicioUltimoUsoTipoEnum.EJERCICIO_USO_DOS_MESES.getCssClass());
						ejercicio.setEjercicioUltimoUsoTipoEnum(EjercicioUltimoUsoTipoEnum.EJERCICIO_USO_DOS_MESES);
						yaEncontrado = true;
						break;
					}
				}
			}

			if (!yaEncontrado) {
				for (Long ejercicio3mesId : ejercicioListaRutina3MesAntes) {
					if (ejercicio3mesId.equals(ejercicio.getId())) {

						// Marcar
						ejercicio.setHtmlClass(EjercicioUltimoUsoTipoEnum.EJERCICIO_USO_TRES_MESES.getCssClass());
						ejercicio.setEjercicioUltimoUsoTipoEnum(EjercicioUltimoUsoTipoEnum.EJERCICIO_USO_TRES_MESES);
						yaEncontrado = true;
						break;
					}
				}
			}

			if (!yaEncontrado) {
				for (Long ejercicio4a12mesId : ejercicioListaRutina4a12MesAntes) {
					if (ejercicio4a12mesId.equals(ejercicio.getId())) {

						// Marcar
						ejercicio.setHtmlClass(EjercicioUltimoUsoTipoEnum.EJERCICIO_USO_CUATRO_DOCE_MESES.getCssClass());
						ejercicio.setEjercicioUltimoUsoTipoEnum(EjercicioUltimoUsoTipoEnum.EJERCICIO_USO_CUATRO_DOCE_MESES);
						yaEncontrado = true;
						break;
					}
				}
			}

			// Ahora marcar si el cliente no tiene el material
			if (ejercicio.getMaterialLista() != null && !ejercicio.getMaterialLista().isEmpty()
					&& !cliente.getMaterialLista().containsAll(ejercicio.getMaterialLista())) {
				ejercicio.setClienteNoTieneTodoMaterial(true);
			}
		}
	}

	@Transactional
	@Override
	public List<Rutina> buscarPlantillaTodas() {
		return rutinaRepositorio.findAll();
	}

	@Transactional
	@Override
	public Rutina crearPlantilla(Rutina plantilla, Long entrenadorId) {
		var plantillaNueva = new Rutina();
		plantillaNueva.setEsPlantilla(Boolean.TRUE);
		plantillaNueva.setFechaCreacion(ZonedDateTime.now());
		plantillaNueva.setEnEjecucion(Boolean.FALSE);
//		plantillaNueva.setEstado(valorMaestroServicio.buscarValorMaestroPorCodigo(RutinaEstadoEnum.EN_ELABORACION.codigo()));
		plantillaNueva.setNombre(plantilla.getNombre());
		plantillaNueva.setComentariosEntrenador(plantilla.getComentariosEntrenador());

		var rutinaFaseInicial = new RutinaFase();
		rutinaFaseInicial.setNombre(FASE_INICIAL_NOMBRE);
		rutinaFaseInicial.setTipo(FASE_INICIAL_TIPO);
		rutinaFaseInicial.addRutinaFaseGrupo(prepararRutinaFaseGrupo());
		plantillaNueva.setFaseInicial(rutinaFaseInicial);

		var rutinaFasePrincipal = new RutinaFase();
		rutinaFasePrincipal.setNombre(FASE_PRINCIPAL_NOMBRE);
		rutinaFasePrincipal.setTipo(FASE_PRINCIPAL_TIPO);
		rutinaFasePrincipal.addRutinaFaseGrupo(prepararRutinaFaseGrupo());
		plantillaNueva.setFasePrincipal(rutinaFasePrincipal);

		var rutinaFaseFinal = new RutinaFase();
		rutinaFaseFinal.setNombre(FASE_FINAL_NOMBRE);
		rutinaFaseFinal.setTipo(FASE_FINAL_TIPO);
		rutinaFaseFinal.addRutinaFaseGrupo(prepararRutinaFaseGrupo());
		plantillaNueva.setFaseFinal(rutinaFaseFinal);

		plantillaNueva.setEntrenador(entrenadorServicio.buscarEntrenadorPorId(entrenadorId));

		plantillaNueva.setEntrenadorTiempoDescansoEntreEjercicios(
				parametroConfiguracionServicio.buscarParametroConfiguracionIntegerValor(Constantes.PC_TIEMPO_DESCANSO_ENTRE_EJERCICIOS));
		plantillaNueva.setEntrenadorTiempoDescansoEntreUnilaterales(
				parametroConfiguracionServicio.buscarParametroConfiguracionIntegerValor(Constantes.PC_TIEMPO_DESCANSO_ENTRE_UNILATERALES));
		plantillaNueva.setEntrenadorTiempoEjercicioPorRepeticiones(
				parametroConfiguracionServicio.buscarParametroConfiguracionIntegerValor(Constantes.PC_TIEMPO_EJERCICIO_POR_REPETICIONES));

		return rutinaRepositorio.saveAndFlush(plantillaNueva);
	}

	@Override
	public Rutina copiarPlantilla(Rutina plantillaOrigen, Long entrenadorId) {
		var plantillaOrigenEntera = buscarRutinaPorId(plantillaOrigen.getId());
		var plantillaNueva = plantillaOrigenEntera.copiarParaCrearPlantilla();
		plantillaNueva.setNombre(plantillaOrigen.getNombre());
		plantillaNueva.setComentariosEntrenador(plantillaOrigen.getComentariosEntrenador());
		plantillaNueva.setEntrenador(entrenadorServicio.buscarEntrenadorPorId(entrenadorId));
		plantillaNueva.setEntrenadorTiempoDescansoEntreEjercicios(plantillaOrigenEntera.getEntrenadorTiempoDescansoEntreEjercicios());
		plantillaNueva.setEntrenadorTiempoDescansoEntreUnilaterales(plantillaOrigenEntera.getEntrenadorTiempoDescansoEntreUnilaterales());
		plantillaNueva.setEntrenadorTiempoEjercicioPorRepeticiones(plantillaOrigenEntera.getEntrenadorTiempoEjercicioPorRepeticiones());
		return rutinaRepositorio.saveAndFlush(plantillaNueva);
	}

	@Transactional
	@Override
	public Rutina crearPlantillaDesdeRutina(Rutina rutinaOrigen, Long entrenadorId) {
		var rutinaOrigenEntera = buscarRutinaPorId(rutinaOrigen.getId());
		var plantillaNueva = rutinaOrigenEntera.copiarParaCrearPlantilla();
		plantillaNueva.setNombre(rutinaOrigen.getNombre());
		plantillaNueva.setComentariosEntrenador(rutinaOrigen.getComentariosEntrenador());
		plantillaNueva.setEntrenador(entrenadorServicio.buscarEntrenadorPorId(entrenadorId));
		plantillaNueva.setEntrenadorTiempoDescansoEntreEjercicios(rutinaOrigenEntera.getEntrenadorTiempoDescansoEntreEjercicios());
		plantillaNueva.setEntrenadorTiempoDescansoEntreUnilaterales(rutinaOrigenEntera.getEntrenadorTiempoDescansoEntreUnilaterales());
		plantillaNueva.setEntrenadorTiempoEjercicioPorRepeticiones(rutinaOrigenEntera.getEntrenadorTiempoEjercicioPorRepeticiones());
		return rutinaRepositorio.saveAndFlush(plantillaNueva);
	}

	@Transactional
	@Override
	public Rutina cargarPlantillaEnRutina(Long rutinaId, Long plantillaId) {
		var rutina = buscarRutinaPorId(rutinaId);
		var plantilla = buscarRutinaPorId(plantillaId);

		rutina.setFaseInicial(plantilla.getFaseInicial().copiarParaCrearPlantilla());
		rutina.setFasePrincipal(plantilla.getFasePrincipal().copiarParaCrearPlantilla());
		rutina.setFaseFinal(plantilla.getFaseFinal().copiarParaCrearPlantilla());

		if (StringUtils.isBlank(rutina.getNombre())) {
			rutina.setNombre(plantilla.getNombre());
		}

		if (StringUtils.isBlank(rutina.getComentariosEntrenador())) {
			rutina.setComentariosEntrenador(plantilla.getComentariosEntrenador());
		}

		return rutina;
	}

	@Override
	public List<Rutina> buscarPlantillaPorFiltro(BuscarPlantillaFormulario buscarPlantillaFormulario) {
		return rutinaRepositorio.buscarPlantillaPorFiltro(buscarPlantillaFormulario);
	}

	@Override
	public List<Rutina> buscarRutinaPorFiltro(BuscarRutinaFormulario buscarRutinaFormulario) {
		return rutinaRepositorio.buscarRutinaPorFiltro(buscarRutinaFormulario);
	}

	@Override
	public void borrarPlantilla(Long plantillaId) {
		rutinaRepositorio.deleteById(plantillaId);
	}

	@Override
	public void analizarCorreccionesRutinas(Long rutinaId) {
		var rutina = rutinaRepositorio.findById(rutinaId).get();
		var rutinasConError = new ArrayList<Long>();
		analizarRutina(rutina, rutinasConError);
		LOGGER.info("Rutinas con error: " + rutinasConError.size() + ": " + rutinasConError);
	}

	@Override
	public void analizarCorreccionesRutinas() {
		var rutinaLista = rutinaRepositorio.findAll();
		var rutinasConError = new ArrayList<Long>();
		// Primero voy a lanzar un analisis
		for (Rutina rutina : rutinaLista) {
			analizarRutina(rutina, rutinasConError);
		}

		LOGGER.info("Rutinas con error: " + rutinasConError.size() + ": " + rutinasConError);
	}

	private void analizarRutina(Rutina rutina, ArrayList<Long> rutinasConError) {

		var conError = false;
		if (rutina.getFaseInicial() != null) {
			for (RutinaFaseGrupo rutinaFaseGrupo : rutina.getFaseInicial().getRutinaFaseGrupoLista()) {
				var rutinaFaseGrupoRepeticion = rutinaFaseGrupo.getRutinaFaseGrupoRepeticionLista().parallelStream()
						.filter(it -> it.getNumeroRepeticion().equals(Integer.valueOf(1))).findAny();

				if (rutinaFaseGrupoRepeticion.isPresent()) {
					var rutinaFaseGrupoRepeticionPrimera = rutinaFaseGrupoRepeticion.get();

					var rutinaFaseGrupoRepeticionPrimeraEjercicioLista = rutinaFaseGrupoRepeticionPrimera.getRutinaFaseGrupoRepeticionEjercicioLista().stream()
							.sorted((it1, it2) -> it1.getPosicion().compareTo(it2.getPosicion())).mapToLong(it -> it.getEjercicio().getId()).boxed()
							.collect(Collectors.toList());

					for (RutinaFaseGrupoRepeticion rutinaFaseGrupoRepeticionAnalizar : rutinaFaseGrupo.getRutinaFaseGrupoRepeticionLista()) {
						if (!rutinaFaseGrupoRepeticionAnalizar.getId().equals(rutinaFaseGrupoRepeticionPrimera.getId())) {
							var rutinaFaseGrupoRepeticionAnalizarEjercicioLista = rutinaFaseGrupoRepeticionAnalizar.getRutinaFaseGrupoRepeticionEjercicioLista().stream()
									.sorted((it1, it2) -> it1.getPosicion().compareTo(it2.getPosicion())).mapToLong(it -> it.getEjercicio().getId()).boxed()
									.collect(Collectors.toList());
							if (rutinaFaseGrupoRepeticionAnalizarEjercicioLista.size() == rutinaFaseGrupoRepeticionPrimeraEjercicioLista.size()) {

								for (int i = 0; i < rutinaFaseGrupoRepeticionAnalizarEjercicioLista.size(); i++) {
									if (!rutinaFaseGrupoRepeticionAnalizarEjercicioLista.get(i).equals(rutinaFaseGrupoRepeticionPrimeraEjercicioLista.get(i))) {
										LOGGER.error("En la rutina " + rutina.getId() + " (es plantilla " + rutina.getEsPlantilla() + "), el grupo "
												+ rutinaFaseGrupo.getId() + " difiere en orden/ejercicio entre sus repeticiones");
										conError = true;
									}
								}
							} else {
								LOGGER.error("En la rutina " + rutina.getId() + " (es plantilla " + rutina.getEsPlantilla() + "), el grupo " + rutinaFaseGrupo.getId()
										+ " difiere en numero de ejercicios entre sus repeticiones");
								conError = true;
							}
						}

					}

				} else {
					LOGGER.error("En la rutina " + rutina.getId() + " (es plantilla " + rutina.getEsPlantilla() + "), el grupo " + rutinaFaseGrupo.getId()
							+ " no tiene repeticion 1");
					conError = true;
				}

			}
		}

		if (rutina.getFasePrincipal() != null) {
			for (RutinaFaseGrupo rutinaFaseGrupo : rutina.getFasePrincipal().getRutinaFaseGrupoLista()) {
				var rutinaFaseGrupoRepeticion = rutinaFaseGrupo.getRutinaFaseGrupoRepeticionLista().parallelStream()
						.filter(it -> it.getNumeroRepeticion().equals(Integer.valueOf(1))).findAny();

				if (rutinaFaseGrupoRepeticion.isPresent()) {
					var rutinaFaseGrupoRepeticionPrimera = rutinaFaseGrupoRepeticion.get();
					var rutinaFaseGrupoRepeticionPrimeraEjercicioLista = rutinaFaseGrupoRepeticionPrimera.getRutinaFaseGrupoRepeticionEjercicioLista().stream()
							.sorted((it1, it2) -> it1.getPosicion().compareTo(it2.getPosicion())).mapToLong(it -> it.getEjercicio().getId()).boxed()
							.collect(Collectors.toList());
					for (RutinaFaseGrupoRepeticion rutinaFaseGrupoRepeticionAnalizar : rutinaFaseGrupo.getRutinaFaseGrupoRepeticionLista()) {
						if (!rutinaFaseGrupoRepeticionAnalizar.getId().equals(rutinaFaseGrupoRepeticionPrimera.getId())) {
							var rutinaFaseGrupoRepeticionAnalizarEjercicioLista = rutinaFaseGrupoRepeticionAnalizar.getRutinaFaseGrupoRepeticionEjercicioLista().stream()
									.sorted((it1, it2) -> it1.getPosicion().compareTo(it2.getPosicion())).mapToLong(it -> it.getEjercicio().getId()).boxed()
									.collect(Collectors.toList());
							if (rutinaFaseGrupoRepeticionAnalizarEjercicioLista.size() == rutinaFaseGrupoRepeticionPrimeraEjercicioLista.size()) {
								for (int i = 0; i < rutinaFaseGrupoRepeticionAnalizarEjercicioLista.size(); i++) {
									if (!rutinaFaseGrupoRepeticionAnalizarEjercicioLista.get(i).equals(rutinaFaseGrupoRepeticionPrimeraEjercicioLista.get(i))) {
										LOGGER.error("En la rutina " + rutina.getId() + " (es plantilla " + rutina.getEsPlantilla() + "), el grupo "
												+ rutinaFaseGrupo.getId() + " difiere en orden/ejercicio entre sus repeticiones");
										conError = true;
									}
								}
							} else {
								LOGGER.error("En la rutina " + rutina.getId() + " (es plantilla " + rutina.getEsPlantilla() + "), el grupo " + rutinaFaseGrupo.getId()
										+ " difiere en numero de ejercicios entre sus repeticiones");
								conError = true;
							}
						}

					}

				} else {
					LOGGER.error("En la rutina " + rutina.getId() + " (es plantilla " + rutina.getEsPlantilla() + "), el grupo " + rutinaFaseGrupo.getId()
							+ " no tiene repeticion 1");
					conError = true;
				}

			}
		}

		if (rutina.getFaseFinal() != null) {
			for (RutinaFaseGrupo rutinaFaseGrupo : rutina.getFaseFinal().getRutinaFaseGrupoLista()) {
				var rutinaFaseGrupoRepeticion = rutinaFaseGrupo.getRutinaFaseGrupoRepeticionLista().parallelStream()
						.filter(it -> it.getNumeroRepeticion().equals(Integer.valueOf(1))).findAny();

				if (rutinaFaseGrupoRepeticion.isPresent()) {
					var rutinaFaseGrupoRepeticionPrimera = rutinaFaseGrupoRepeticion.get();
					var rutinaFaseGrupoRepeticionPrimeraEjercicioLista = rutinaFaseGrupoRepeticionPrimera.getRutinaFaseGrupoRepeticionEjercicioLista().stream()
							.sorted((it1, it2) -> it1.getPosicion().compareTo(it2.getPosicion())).mapToLong(it -> it.getEjercicio().getId()).boxed()
							.collect(Collectors.toList());
					for (RutinaFaseGrupoRepeticion rutinaFaseGrupoRepeticionAnalizar : rutinaFaseGrupo.getRutinaFaseGrupoRepeticionLista()) {
						if (!rutinaFaseGrupoRepeticionAnalizar.getId().equals(rutinaFaseGrupoRepeticionPrimera.getId())) {
							var rutinaFaseGrupoRepeticionAnalizarEjercicioLista = rutinaFaseGrupoRepeticionAnalizar.getRutinaFaseGrupoRepeticionEjercicioLista().stream()
									.sorted((it1, it2) -> it1.getPosicion().compareTo(it2.getPosicion())).mapToLong(it -> it.getEjercicio().getId()).boxed()
									.collect(Collectors.toList());
							if (rutinaFaseGrupoRepeticionAnalizarEjercicioLista.size() == rutinaFaseGrupoRepeticionPrimeraEjercicioLista.size()) {
								for (int i = 0; i < rutinaFaseGrupoRepeticionAnalizarEjercicioLista.size(); i++) {
									if (!rutinaFaseGrupoRepeticionAnalizarEjercicioLista.get(i).equals(rutinaFaseGrupoRepeticionPrimeraEjercicioLista.get(i))) {
										LOGGER.error("En la rutina " + rutina.getId() + " (es plantilla " + rutina.getEsPlantilla() + "), el grupo "
												+ rutinaFaseGrupo.getId() + " difiere en orden/ejercicio entre sus repeticiones");
										conError = true;
									}
								}
							} else {
								LOGGER.error("En la rutina " + rutina.getId() + " (es plantilla " + rutina.getEsPlantilla() + "), el grupo " + rutinaFaseGrupo.getId()
										+ " difiere en numero de ejercicios entre sus repeticiones");
								conError = true;
							}
						}

					}

				} else {
					LOGGER.error("En la rutina " + rutina.getId() + " (es plantilla " + rutina.getEsPlantilla() + "), el grupo " + rutinaFaseGrupo.getId()
							+ " no tiene repeticion 1");
					conError = true;
				}

			}
		}

		if (conError) {
			rutinasConError.add(rutina.getId());
		}
	}

	@Transactional
	@Override
	public void corregirRutinas() {
		var rutinaLista = rutinaRepositorio.findAll();

		for (Rutina rutina : rutinaLista) {
			if (rutina.getFaseInicial() != null) {
				for (RutinaFaseGrupo rutinaFaseGrupo : rutina.getFaseInicial().getRutinaFaseGrupoLista()) {
					// Borrar repeticiones que no son la primera (verificando que existe)
					var rutinaFaseGrupoRepeticion = rutinaFaseGrupo.getRutinaFaseGrupoRepeticionLista().parallelStream()
							.filter(it -> it.getNumeroRepeticion().equals(Integer.valueOf(1))).findAny();
					if (rutinaFaseGrupoRepeticion.isPresent()) {
						rutinaFaseGrupo.getRutinaFaseGrupoRepeticionLista().removeIf(it -> !it.getId().equals(rutinaFaseGrupoRepeticion.get().getId()));
					} else {
						throw new RuntimeException("No existe repetición 1");
					}

					// Generar de nuevo repeticiones
					for (int i = 1; i < rutinaFaseGrupo.getRepeticiones(); i++) {
						rutinaFaseGrupo.addRutinaFaseGrupoRepeticionEstableciendoDatos(new RutinaFaseGrupoRepeticion());
					}
				}
			}

			if (rutina.getFasePrincipal() != null) {
				for (RutinaFaseGrupo rutinaFaseGrupo : rutina.getFasePrincipal().getRutinaFaseGrupoLista()) {
					// Borrar repeticiones que no son la primera (verificando que existe)
					var rutinaFaseGrupoRepeticion = rutinaFaseGrupo.getRutinaFaseGrupoRepeticionLista().parallelStream()
							.filter(it -> it.getNumeroRepeticion().equals(Integer.valueOf(1))).findAny();
					if (rutinaFaseGrupoRepeticion.isPresent()) {
						rutinaFaseGrupo.getRutinaFaseGrupoRepeticionLista().removeIf(it -> !it.getId().equals(rutinaFaseGrupoRepeticion.get().getId()));
					} else {
						throw new RuntimeException("No existe repetición 1");
					}

					// Generar de nuevo repeticiones
					for (int i = 1; i < rutinaFaseGrupo.getRepeticiones(); i++) {
						rutinaFaseGrupo.addRutinaFaseGrupoRepeticionEstableciendoDatos(new RutinaFaseGrupoRepeticion());
					}
				}
			}

			if (rutina.getFaseFinal() != null) {
				for (RutinaFaseGrupo rutinaFaseGrupo : rutina.getFaseFinal().getRutinaFaseGrupoLista()) {
					// Borrar repeticiones que no son la primera (verificando que existe)
					var rutinaFaseGrupoRepeticion = rutinaFaseGrupo.getRutinaFaseGrupoRepeticionLista().parallelStream()
							.filter(it -> it.getNumeroRepeticion().equals(Integer.valueOf(1))).findAny();
					if (rutinaFaseGrupoRepeticion.isPresent()) {
						rutinaFaseGrupo.getRutinaFaseGrupoRepeticionLista().removeIf(it -> !it.getId().equals(rutinaFaseGrupoRepeticion.get().getId()));
					} else {
						throw new RuntimeException("No existe repetición 1");
					}

					// Generar de nuevo repeticiones
					for (int i = 1; i < rutinaFaseGrupo.getRepeticiones(); i++) {
						rutinaFaseGrupo.addRutinaFaseGrupoRepeticionEstableciendoDatos(new RutinaFaseGrupoRepeticion());
					}
				}
			}
		}

	}

	@Transactional
	@Override
	public void configurarRutinaFormulario(ConfigurarRutinaFormulario configurarRutinaFormulario) {
		var rutina = buscarRutinaPorId(configurarRutinaFormulario.getRutinaId());
		rutina.setClienteTiempoDescansoEntreEjercicios(configurarRutinaFormulario.getTiempoDescansoEntreEjercicios());
		rutina.setClienteTiempoDescansoEntreUnilaterales(configurarRutinaFormulario.getTiempoDescansoEntreUnilaterales());
		rutina.setClienteTiempoEjercicioPorRepeticiones(configurarRutinaFormulario.getTiempoEjercicioPorRepeticiones());
		rutina.setClienteEjecucionAutomatica(configurarRutinaFormulario.getEjecucionAutomatica());
	}

	@Transactional
	@Override
	public Rutina cambioAutomaticoEjerciciosRutinaEntera(Long entrenadorId, Long rutinaId, List<String> rutinaFaseGrupoRepeticionEjercicioIdLista,
			BuscarEjercicioFormulario buscarEjercicioFormulario, Map<Long, List<Long>> rutinaFaseGrupoRepeticionEjercicioHistorial) {
		var rutina = buscarRutinaPorId(rutinaId);
		var rutinaFaseGrupoRepeticionEjercicioLista = rutina.getRutinaFaseGrupoRepeticionEjercicioTodos();
		if (rutinaFaseGrupoRepeticionEjercicioIdLista.size() > 1) {
			rutinaFaseGrupoRepeticionEjercicioLista = rutinaFaseGrupoRepeticionEjercicioLista.stream().filter(
					it -> rutinaFaseGrupoRepeticionEjercicioIdLista.contains(it.getRutinaFaseGrupoRepeticion().getRutinaFaseGrupo().getId() + "-" + it.getPosicion()))
					.collect(Collectors.toList());
			rutinaFaseGrupoRepeticionEjercicioLista.forEach(it -> it.setMarcadoParaReemplazar(true));
		}

		buscarRutinaFaseGrupoRepeticionEjercicioListaEjerciciosCompatibles(entrenadorId, rutinaFaseGrupoRepeticionEjercicioLista, rutinaId, rutina.getEsPlantilla(),
				rutina.getCliente(), buscarEjercicioFormulario, rutinaFaseGrupoRepeticionEjercicioHistorial);
		if (rutina.getEsPlantilla() == null || !rutina.getEsPlantilla()) {
			clasificarEjercicioSegunUsoPrevio(rutina.getCliente(), rutinaId, rutina.getEjercicioTodos(), false);
		}

		return rutina;
	}

	@Transactional
	@Override
	public Rutina cambioAutomaticoEjerciciosRutinaFase(Long entrenadorId, Long rutinaId, Long rutinaFaseId, List<String> rutinaFaseGrupoRepeticionEjercicioIdLista,
			BuscarEjercicioFormulario buscarEjercicioFormulario, Map<Long, List<Long>> rutinaFaseGrupoRepeticionEjercicioHistorial) {
		var rutina = buscarRutinaPorId(rutinaId);
		var rutinaFase = rutina.getFaseInicial().getId().equals(rutinaFaseId) ? rutina.getFaseInicial()
				: (rutina.getFasePrincipal().getId().equals(rutinaFaseId) ? rutina.getFasePrincipal() : rutina.getFaseFinal());

		var rutinaFaseGrupoRepeticionEjercicioLista = rutinaFase.getRutinaFaseGrupoRepeticionEjercicioTodos();
		if (rutinaFaseGrupoRepeticionEjercicioIdLista.size() > 1) {
			rutinaFaseGrupoRepeticionEjercicioLista = rutinaFaseGrupoRepeticionEjercicioLista.stream().filter(
					it -> rutinaFaseGrupoRepeticionEjercicioIdLista.contains(it.getRutinaFaseGrupoRepeticion().getRutinaFaseGrupo().getId() + "-" + it.getPosicion()))
					.collect(Collectors.toList());
			rutinaFaseGrupoRepeticionEjercicioLista.forEach(it -> it.setMarcadoParaReemplazar(true));
		}

		buscarRutinaFaseGrupoRepeticionEjercicioListaEjerciciosCompatibles(entrenadorId, rutinaFaseGrupoRepeticionEjercicioLista, rutinaId, rutina.getEsPlantilla(),
				rutina.getCliente(), buscarEjercicioFormulario, rutinaFaseGrupoRepeticionEjercicioHistorial);
		if (rutina.getEsPlantilla() == null || !rutina.getEsPlantilla()) {
			clasificarEjercicioSegunUsoPrevio(rutina.getCliente(), rutinaId, rutinaFase.getEjercicioTodos(), false);
		}

		return rutina;
	}

	@Transactional
	@Override
	public Rutina cambioAutomaticoEjerciciosRutinaFaseGrupo(Long entrenadorId, Long rutinaId, Long rutinaFaseId, Long rutinaFaseGrupoId,
			List<String> rutinaFaseGrupoRepeticionEjercicioIdLista, BuscarEjercicioFormulario buscarEjercicioFormulario,
			Map<Long, List<Long>> rutinaFaseGrupoRepeticionEjercicioHistorial) {
		var rutina = buscarRutinaPorId(rutinaId);
		var rutinaFase = rutina.getFaseInicial().getId().equals(rutinaFaseId) ? rutina.getFaseInicial()
				: (rutina.getFasePrincipal().getId().equals(rutinaFaseId) ? rutina.getFasePrincipal() : rutina.getFaseFinal());
		var rutinaFaseGrupo = rutinaFase.getRutinaFaseGrupoLista().stream().filter(it -> it.getId().equals(rutinaFaseGrupoId)).findFirst().orElse(null);

		var rutinaFaseGrupoRepeticionEjercicioLista = rutinaFaseGrupo.getRutinaFaseGrupoEjercicioConRepeticionesLista();
		if (rutinaFaseGrupoRepeticionEjercicioIdLista.size() > 1) {
			rutinaFaseGrupoRepeticionEjercicioLista = rutinaFaseGrupoRepeticionEjercicioLista.stream().filter(
					it -> rutinaFaseGrupoRepeticionEjercicioIdLista.contains(it.getRutinaFaseGrupoRepeticion().getRutinaFaseGrupo().getId() + "-" + it.getPosicion()))
					.collect(Collectors.toList());
			rutinaFaseGrupoRepeticionEjercicioLista.forEach(it -> it.setMarcadoParaReemplazar(true));
		}
		buscarRutinaFaseGrupoRepeticionEjercicioListaEjerciciosCompatibles(entrenadorId, rutinaFaseGrupoRepeticionEjercicioLista, rutinaId, rutina.getEsPlantilla(),
				rutina.getCliente(), buscarEjercicioFormulario, rutinaFaseGrupoRepeticionEjercicioHistorial);
		if (rutina.getEsPlantilla() == null || !rutina.getEsPlantilla()) {
			clasificarEjercicioSegunUsoPrevio(rutina.getCliente(), rutinaId, rutinaFaseGrupo.getEjercicioTodos(), false);
		}

		return rutina;
	}

	private void buscarRutinaFaseGrupoRepeticionEjercicioListaEjerciciosCompatibles(Long entrenadorId,
			List<RutinaFaseGrupoRepeticionEjercicio> rutinaFaseGrupoRepeticionEjercicioLista, Long rutinaId, Boolean esPlantilla, Cliente cliente,
			BuscarEjercicioFormulario buscarEjercicioFormulario, Map<Long, List<Long>> rutinaFaseGrupoRepeticionEjercicioHistorial) {
		var ejercicioAnteriorEjercicioNuevoMapa = new HashMap<String, Ejercicio>();
//		LOGGER.debug("Rutina ejercicios totales a analizar = " + rutinaFaseGrupoRepeticionEjercicioLista.size());
		rutinaFaseGrupoRepeticionEjercicioLista.forEach(it -> {
			Ejercicio ejercicioNuevo = null;

			// Clave por grupo y posicion para siempre asignar ejercicio nuevo igual a todas
			// las repeticiones del mismo grupo y posición
			var clave = it.getRutinaFaseGrupoRepeticion().getRutinaFaseGrupo().getId() + "-" + it.getPosicion();
			var existeEjercicioNuevoPreviamenteLocalizado = ejercicioAnteriorEjercicioNuevoMapa.containsKey(clave);
			if (!existeEjercicioNuevoPreviamenteLocalizado) {
				// Primera vez que se busca para ese grupo y posición
				ejercicioNuevo = buscarEjercicioCompatible(it, rutinaId, esPlantilla, cliente, buscarEjercicioFormulario, rutinaFaseGrupoRepeticionEjercicioHistorial);
				ejercicioAnteriorEjercicioNuevoMapa.put(clave, ejercicioNuevo);
			} else {
				// Ya se buscó antes (y puede ser null, por eso uso containsKey)
				ejercicioNuevo = ejercicioAnteriorEjercicioNuevoMapa.get(clave);
			}

			if (ejercicioNuevo != null) {
				it.setEjercicio(ejercicioNuevo);
				it.setComentarios(null);
				asignarEjercicioRepeticionesTiempo(entrenadorId, ejercicioNuevo.getId(), it, ejercicioNuevo);
			} else {
				it.setEjercicioSinOtroCompatible(true);
			}
		});
	}

	private Ejercicio buscarEjercicioCompatible(RutinaFaseGrupoRepeticionEjercicio rutinaFaseGrupoRepeticionEjercicio, Long rutinaId, Boolean esPlantilla,
			Cliente cliente, BuscarEjercicioFormulario buscarEjercicioFormulario, Map<Long, List<Long>> rutinaFaseGrupoRepeticionEjercicioHistorial) {

		if (rutinaFaseGrupoRepeticionEjercicioHistorial == null) {
			rutinaFaseGrupoRepeticionEjercicioHistorial = new HashMap<>();
		}

		// Patron, musculo, zona y parte cuerpo: Según parametros del ejercicio actual
		var buscarEjercicioCompatibleFormulario = new BuscarEjercicioFormulario();
		if (esPlantilla == null || !esPlantilla) {
			buscarEjercicioCompatibleFormulario.setFetchMaterial(true);
		}
		buscarEjercicioCompatibleFormulario
				.setPatronIdLista(rutinaFaseGrupoRepeticionEjercicio.getPatronLista().stream().map(it -> it.getId()).collect(Collectors.toList()));
		buscarEjercicioCompatibleFormulario
				.setMusculoIdLista(rutinaFaseGrupoRepeticionEjercicio.getMusculoLista().stream().map(it -> it.getId()).collect(Collectors.toList()));
		buscarEjercicioCompatibleFormulario.setZonaIdLista(rutinaFaseGrupoRepeticionEjercicio.getZonaLista().stream().map(it -> it.getId()).collect(Collectors.toList()));
		buscarEjercicioCompatibleFormulario
				.setParteCuerpoIdLista(rutinaFaseGrupoRepeticionEjercicio.getParteCuerpoLista().stream().map(it -> it.getId()).collect(Collectors.toList()));

		// Material: El que viene del filtro de la elaboración
		buscarEjercicioCompatibleFormulario.setMaterialIdLista(buscarEjercicioFormulario.getMaterialIdLista());
		var ejercicioCompatibleLista = ejercicioServicio.buscarEjercicioPorFiltroParaElaboracionRutina(buscarEjercicioCompatibleFormulario, false);

		// Según uso: Mínimo cuatro doce meses, tres meses y dos meses
		// Opcional: Actual y un mes si viene del filtro de la elaboración
		// Solo filtrar y clasificar si no es plantilla
		if (esPlantilla == null || !esPlantilla) {
			var ejercicioUltimoUsoTipoLista = new ArrayList<String>();
			ejercicioUltimoUsoTipoLista.add(EjercicioUltimoUsoTipoEnum.EJERCICIO_USO_CUATRO_DOCE_MESES.codigo());
			ejercicioUltimoUsoTipoLista.add(EjercicioUltimoUsoTipoEnum.EJERCICIO_USO_TRES_MESES.codigo());
			ejercicioUltimoUsoTipoLista.add(EjercicioUltimoUsoTipoEnum.EJERCICIO_USO_DOS_MESES.codigo());
			if (buscarEjercicioFormulario.getEjercicioUltimoUsoTipoLista().contains(EjercicioUltimoUsoTipoEnum.EJERCICIO_USO_ACTUAL.codigo())) {
				ejercicioUltimoUsoTipoLista.add(EjercicioUltimoUsoTipoEnum.EJERCICIO_USO_ACTUAL.codigo());
			}
			if (buscarEjercicioFormulario.getEjercicioUltimoUsoTipoLista().contains(EjercicioUltimoUsoTipoEnum.EJERCICIO_USO_UN_MES.codigo())) {
				ejercicioUltimoUsoTipoLista.add(EjercicioUltimoUsoTipoEnum.EJERCICIO_USO_UN_MES.codigo());
			}
			buscarEjercicioCompatibleFormulario.setEjercicioUltimoUsoTipoLista(ejercicioUltimoUsoTipoLista);

			clasificarEjercicioSegunUsoPrevio(cliente, rutinaId, ejercicioCompatibleLista, false);

		}

		// Se intenta buscar sin repetir
		if (!rutinaFaseGrupoRepeticionEjercicioHistorial.containsKey(rutinaFaseGrupoRepeticionEjercicio.getId())) {
			rutinaFaseGrupoRepeticionEjercicioHistorial.put(rutinaFaseGrupoRepeticionEjercicio.getId(), new ArrayList<Long>());
		}
		var ejercicioCompatibleYaUsadoLista = rutinaFaseGrupoRepeticionEjercicioHistorial.get(rutinaFaseGrupoRepeticionEjercicio.getId());
		ejercicioCompatibleYaUsadoLista.add(rutinaFaseGrupoRepeticionEjercicio.getEjercicio().getId());

//		LOGGER.debug("Ejercicios ya utilizados: " + ejercicioCompatibleYaUsadoLista);
		var ejercicioCompatibleListaFinal = ejercicioCompatibleLista.parallelStream()
				.filter(it -> (it.getEjercicioUltimoUsoTipoEnum() == null
						|| buscarEjercicioCompatibleFormulario.getEjercicioUltimoUsoTipoLista().contains(it.getEjercicioUltimoUsoTipoEnum().codigo()))
						&& !ejercicioCompatibleYaUsadoLista.contains(it.getId()))
				.collect(Collectors.toList());

//		LOGGER.debug("Busqueda sin repetir, cantidad: " + ejercicioCompatibleListaFinal.size());

		// Y si no, pues se reinicia lista de repetidos y se elige desde 0
		if (ejercicioCompatibleListaFinal.isEmpty()) {
			ejercicioCompatibleListaFinal = ejercicioCompatibleLista.parallelStream()
					.filter(it -> (it.getEjercicioUltimoUsoTipoEnum() == null
							|| buscarEjercicioCompatibleFormulario.getEjercicioUltimoUsoTipoLista().contains(it.getEjercicioUltimoUsoTipoEnum().codigo()))
							&& !it.getId().equals(rutinaFaseGrupoRepeticionEjercicio.getEjercicio().getId()))
					.collect(Collectors.toList());
//			LOGGER.debug("Busqueda reiniciando, cantidad: " + ejercicioCompatibleListaFinal.size());
			ejercicioCompatibleYaUsadoLista.clear();
		}

		var ejercicioCompatible = !ejercicioCompatibleListaFinal.isEmpty() ? ejercicioCompatibleListaFinal.get(new Random().nextInt(ejercicioCompatibleListaFinal.size()))
				: null;
//		var ejercicioCompatible = !ejercicioCompatibleListaFinal.isEmpty() ? ejercicioCompatibleListaFinal.get(0) : null;
//		if (ejercicioCompatible != null) {
//			ejercicioCompatibleYaUsadoLista.add(ejercicioCompatible.getId());
//		}

		return ejercicioCompatible;
	}

	private void asignarEjercicioRepeticionesTiempo(Long entrenadorId, Long ejercicioId, RutinaFaseGrupoRepeticionEjercicio rutinaFaseGrupoRepeticionEjercicio,
			Ejercicio ejercicio) {
		var ejercicioRepeticionesTiempo = ejercicioEstadisticaCache.getEjercicioRepeticionesTiempoPorEstadistica(entrenadorId, ejercicioId);
		rutinaFaseGrupoRepeticionEjercicio.setRepeticiones(ejercicioRepeticionesTiempo.getFirst());
		rutinaFaseGrupoRepeticionEjercicio.setTiempo(ejercicioRepeticionesTiempo.getSecond());
	}
}
