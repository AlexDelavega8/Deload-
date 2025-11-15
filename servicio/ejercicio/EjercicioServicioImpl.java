package es.pocketrainer.servicio.ejercicio;

import static es.pocketrainer.util.Constantes.CACHE_EJERCICIO;
import static es.pocketrainer.util.Constantes.CACHE_MATERIAL;
import static es.pocketrainer.util.Constantes.CACHE_MUSCULO;
import static es.pocketrainer.util.Constantes.CACHE_PARTE_CUERPO;
import static es.pocketrainer.util.Constantes.CACHE_PATRON;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import es.pocketrainer.formulario.BuscarRutinaFormulario;
import es.pocketrainer.modelo.ejercicio.Ejercicio;
import es.pocketrainer.modelo.ejercicio.EjercicioEntrenadorEstadistica;
import es.pocketrainer.modelo.ejercicio.EjercicioEntrenadorEstadisticaId;
import es.pocketrainer.modelo.ejercicio.EjercicioMusculo;
import es.pocketrainer.modelo.ejercicio.EjercicioParteCuerpo;
import es.pocketrainer.modelo.ejercicio.Material;
import es.pocketrainer.modelo.ejercicio.Material_;
import es.pocketrainer.modelo.ejercicio.Musculo;
import es.pocketrainer.modelo.ejercicio.ParteCuerpo;
import es.pocketrainer.modelo.ejercicio.Patron;
import es.pocketrainer.modelo.ejercicio.Zona;
import es.pocketrainer.repositorio.ejercicio.BuscarEjercicioFormulario;
import es.pocketrainer.repositorio.ejercicio.EjercicioEntrenadorEstadisticaRepositorio;
import es.pocketrainer.repositorio.ejercicio.EjercicioRepositorio;
import es.pocketrainer.repositorio.material.MaterialRepositorio;
import es.pocketrainer.repositorio.musculo.MusculoRepositorio;
import es.pocketrainer.repositorio.partecuerpo.ParteCuerpoRepositorio;
import es.pocketrainer.repositorio.patron.PatronRepositorio;
import es.pocketrainer.repositorio.zona.ZonaRepositorio;
import es.pocketrainer.servicio.cliente.ClienteServicio;
import es.pocketrainer.servicio.entrenador.EntrenadorServicio;
import es.pocketrainer.servicio.gestorficheros.GestorFicherosServicio;
import es.pocketrainer.servicio.rutina.EjercicioEstadisticaCache;
import es.pocketrainer.servicio.rutina.RutinaServicio;

@Service
public class EjercicioServicioImpl implements EjercicioServicio {

	@Value("${ruta.estaticos.ejercicios.fotos}")
	private String rutaEstaticosEjerciciosFotos;

	@Value("${ruta.estaticos.ejercicios.videos}")
	private String rutaEstaticosEjerciciosVideos;

	@Value("${ruta.estaticos.ejercicios.audios}")
	private String rutaEstaticosEjerciciosAudios;

	@Value("${ruta.estaticos.ejercicios.videos.480}")
	private String rutaEstaticosEjerciciosVideos480;

	@Resource
	private RutinaServicio rutinaServicio;

	@Resource
	private EjercicioRepositorio ejercicioRepositorio;

	@Resource
	private PatronRepositorio patronRepositorio;

	@Resource
	private MusculoRepositorio musculoRepositorio;

	@Resource
	private ParteCuerpoRepositorio parteCuerpoRepositorio;

	@Resource
	private MaterialRepositorio materialRepositorio;

	@Resource
	private ZonaRepositorio zonaRepositorio;

	@Resource
	private ClienteServicio clienteServicio;

	@Resource
	private GestorFicherosServicio gestorFicherosServicio;

	@Resource
	private EntrenadorServicio entrenadorServicio;

	@Resource
	private EjercicioEntrenadorEstadisticaRepositorio ejercicioEntrenadorEstadisticaRepositorio;

	@Resource
	private EjercicioEstadisticaCache ejercicioEstadisticaCache;

	@Override
	public Ejercicio buscarEjercicioPorId(Long ejercicioId) {
		return ejercicioRepositorio.findById(ejercicioId).orElse(null);
	}

	@Cacheable(value = CACHE_EJERCICIO, key = "#root.methodName")
	@Override
	public List<Ejercicio> buscarEjercicioTodos() {
		return ejercicioRepositorio.findAll();
	}

	@Cacheable(value = CACHE_PATRON, key = "#root.methodName")
	@Override
	public List<Patron> buscarPatronTodos() {
		return patronRepositorio.findAll();
	}

	@Cacheable(value = CACHE_MUSCULO, key = "#root.methodName")
	@Override
	public List<Musculo> buscarMusculoTodos() {
		return musculoRepositorio.findAll();
	}

	@Cacheable(value = CACHE_PARTE_CUERPO, key = "#root.methodName")
	@Override
	public List<ParteCuerpo> buscarParteCuerpoTodos() {
		return parteCuerpoRepositorio.findAll();
	}

	@Cacheable(value = CACHE_MATERIAL, key = "#root.methodName")
	@Override
	public List<Material> buscarMaterialTodos() {
		return materialRepositorio.findAll(Sort.by(Direction.ASC, Material_.NOMBRE));
	}

	@Override
	public List<Ejercicio> buscarEjercicioPorFiltro(BuscarEjercicioFormulario ejercicioBusquedaFiltro) {
		return ejercicioRepositorio.buscarPorFiltro(ejercicioBusquedaFiltro);
	}

	@Override
	public List<Ejercicio> buscarEjercicioPorFiltroParaElaboracionRutina(BuscarEjercicioFormulario ejercicioBusquedaFiltro, boolean clasificarRutinaEjercicios) {

		var ejercicioLista = ejercicioRepositorio.buscarPorFiltroParaElaboracionRutina(ejercicioBusquedaFiltro);
		var ejercicioListaFinal = ejercicioLista;

		if (ejercicioBusquedaFiltro.isClasificarEjercicioSegunUsoPrevio()) {

			var cliente = clienteServicio.buscarClientePorId(ejercicioBusquedaFiltro.getClienteId());
			rutinaServicio.clasificarEjercicioSegunUsoPrevio(cliente, ejercicioBusquedaFiltro.getRutinaId(), ejercicioLista, clasificarRutinaEjercicios);

			// Eliminar segun clasificacion anterior y si esta activo el tipo de filtro
			if (ejercicioBusquedaFiltro.isFiltrarPorEjercicioUltimoUsoTipo()) {
				if (ejercicioBusquedaFiltro.getEjercicioUltimoUsoTipoLista() != null && !ejercicioBusquedaFiltro.getEjercicioUltimoUsoTipoLista().isEmpty()) {
					ejercicioListaFinal = ejercicioLista.parallelStream()
							.filter(it -> it.getEjercicioUltimoUsoTipoEnum() == null
									|| ejercicioBusquedaFiltro.getEjercicioUltimoUsoTipoLista().contains(it.getEjercicioUltimoUsoTipoEnum().codigo()))
							.collect(Collectors.toList());
				} else {
					ejercicioListaFinal = ejercicioLista.parallelStream().filter(it -> it.getEjercicioUltimoUsoTipoEnum() == null).collect(Collectors.toList());

				}
			}
		}

		return ejercicioListaFinal;
	}

	@Override
	public List<Zona> buscarZonaTodas() {
		return zonaRepositorio.findAll();
	}

	@CacheEvict(value = CACHE_EJERCICIO, allEntries = true)
	@Transactional
	@Override
	public void crearEjercicio(Ejercicio ejercicio) {

		ejercicio.setEjercicioMusculoLista(new HashSet<>());

		ejercicio.getMusculoPrincipalLista().stream().forEach(it -> {
			var ejercicioMusculo = ejercicio.getEjercicioMusculoLista().parallelStream().filter(em -> em.getMusculo().getId().equals(it.getId())).findAny().orElse(null);
			if (ejercicioMusculo == null) {
				ejercicioMusculo = new EjercicioMusculo();
				ejercicioMusculo.setMusculo(it);
				ejercicio.addEjercicioMusculo(ejercicioMusculo);
			}
			ejercicioMusculo.setPrincipal(Boolean.TRUE);
		});

		ejercicio.getMusculoSecundarioLista().stream().forEach(it -> {
			var ejercicioMusculo = ejercicio.getEjercicioMusculoLista().parallelStream().filter(em -> em.getMusculo().getId().equals(it.getId())).findAny().orElse(null);
			if (ejercicioMusculo == null) {
				ejercicioMusculo = new EjercicioMusculo();
				ejercicioMusculo.setMusculo(it);
				ejercicio.addEjercicioMusculo(ejercicioMusculo);
			}
			ejercicioMusculo.setPrincipal(Boolean.FALSE);
		});

		ejercicio.setEjercicioParteCuerpoLista(new HashSet<>());

		ejercicio.getParteCuerpoPrincipalLista().stream().forEach(it -> {
			var ejercicioParteCuerpo = ejercicio.getEjercicioParteCuerpoLista().parallelStream().filter(em -> em.getParteCuerpo().getId().equals(it.getId())).findAny()
					.orElse(null);
			if (ejercicioParteCuerpo == null) {
				ejercicioParteCuerpo = new EjercicioParteCuerpo();
				ejercicioParteCuerpo.setParteCuerpo(it);
				ejercicio.addEjercicioParteCuerpo(ejercicioParteCuerpo);
			}

			ejercicioParteCuerpo.setPrincipal(Boolean.TRUE);
		});
		ejercicio.getParteCuerpoSecundarioLista().stream().forEach(it -> {
			var ejercicioParteCuerpo = ejercicio.getEjercicioParteCuerpoLista().parallelStream().filter(em -> em.getParteCuerpo().getId().equals(it.getId())).findAny()
					.orElse(null);
			if (ejercicioParteCuerpo == null) {
				ejercicioParteCuerpo = new EjercicioParteCuerpo();
				ejercicioParteCuerpo.setParteCuerpo(it);
				ejercicio.addEjercicioParteCuerpo(ejercicioParteCuerpo);
			}
			ejercicioParteCuerpo.setPrincipal(Boolean.FALSE);
		});

		ejercicio.setFechaHoraUltimaActualizacion(ZonedDateTime.now());

		if (ejercicio.getAudio() != null) {
			ejercicio.setTieneAudio(Boolean.TRUE);
		}

		var ejercicioCreado = ejercicioRepositorio.saveAndFlush(ejercicio);

		if (ejercicio.getAudio() != null) {
			guardarEjercicioMultimediaFichero(ejercicio.getAudio(), ejercicioCreado.getId(), rutaEstaticosEjerciciosAudios, "");
		}

		if (ejercicio.getFoto() != null) {
			guardarEjercicioMultimediaFichero(ejercicio.getFoto(), ejercicioCreado.getId(), rutaEstaticosEjerciciosFotos, "");
		}

		if (ejercicio.getVideo() != null) {
			guardarEjercicioMultimediaFichero(ejercicio.getVideo(), ejercicioCreado.getId(), rutaEstaticosEjerciciosVideos, "_x264");
		}

		if (ejercicio.getVideo480() != null) {
			guardarEjercicioMultimediaFichero(ejercicio.getVideo480(), ejercicioCreado.getId(), rutaEstaticosEjerciciosVideos480, "_x264_480");
		}

	}

	@CacheEvict(value = CACHE_EJERCICIO, allEntries = true)
	@Transactional
	@Override
	public void actualizarEjercicio(Ejercicio ejercicio) {
		var ejercicioActualizado = buscarEjercicioPorId(ejercicio.getId());
		ejercicioActualizado.setNombre(ejercicio.getNombre());
		ejercicioActualizado.setNombreCorto(ejercicio.getNombreCorto());
		ejercicioActualizado.setDescripcion(ejercicio.getDescripcion());
		ejercicioActualizado.setEsUnilateral(ejercicio.getEsUnilateral());

		ejercicioActualizado.getZonaLista().clear();
		ejercicioActualizado.getZonaLista().addAll(ejercicio.getZonaLista());

		ejercicioActualizado.getPatronLista().clear();
		ejercicioActualizado.getPatronLista().addAll(ejercicio.getPatronLista());

		ejercicioActualizado.getMaterialLista().clear();
		ejercicioActualizado.getMaterialLista().addAll(ejercicio.getMaterialLista());

		var ejercicioMusculoParaBorrarLista = new ArrayList<EjercicioMusculo>();
		ejercicioActualizado.getEjercicioMusculoLista().parallelStream().forEach(it -> {
			var musculoPrincipal = ejercicio.getMusculoPrincipalLista().parallelStream().filter(mp -> mp.getId().equals(it.getMusculo().getId())).findAny().orElse(null);
			var musculoSecundario = ejercicio.getMusculoSecundarioLista().parallelStream().filter(mp -> mp.getId().equals(it.getMusculo().getId())).findAny()
					.orElse(null);
			if (musculoPrincipal == null && musculoSecundario == null) {
				ejercicioMusculoParaBorrarLista.add(it);
			}
		});
		ejercicioMusculoParaBorrarLista.removeAll(ejercicioMusculoParaBorrarLista);

		ejercicio.getMusculoPrincipalLista().stream().forEach(it -> {
			var ejercicioMusculo = ejercicioActualizado.getEjercicioMusculoLista().parallelStream().filter(em -> em.getMusculo().getId().equals(it.getId())).findAny()
					.orElse(null);
			if (ejercicioMusculo == null) {
				ejercicioMusculo = new EjercicioMusculo();
				ejercicioMusculo.setMusculo(it);
				ejercicioActualizado.addEjercicioMusculo(ejercicioMusculo);
			}
			ejercicioMusculo.setPrincipal(Boolean.TRUE);
		});

		ejercicio.getMusculoSecundarioLista().stream().forEach(it -> {
			var ejercicioMusculo = ejercicioActualizado.getEjercicioMusculoLista().parallelStream().filter(em -> em.getMusculo().getId().equals(it.getId())).findAny()
					.orElse(null);
			if (ejercicioMusculo == null) {
				ejercicioMusculo = new EjercicioMusculo();
				ejercicioMusculo.setMusculo(it);
				ejercicioActualizado.addEjercicioMusculo(ejercicioMusculo);
			}
			ejercicioMusculo.setPrincipal(Boolean.FALSE);
		});

		var ejercicioParteCuerpoParaBorrarLista = new ArrayList<EjercicioParteCuerpo>();
		ejercicioActualizado.getEjercicioParteCuerpoLista().parallelStream().forEach(it -> {
			var parteCuerpoPrincipal = ejercicio.getParteCuerpoPrincipalLista().parallelStream().filter(mp -> mp.getId().equals(it.getParteCuerpo().getId())).findAny()
					.orElse(null);
			var parteCuerpoSecundario = ejercicio.getParteCuerpoSecundarioLista().parallelStream().filter(mp -> mp.getId().equals(it.getParteCuerpo().getId())).findAny()
					.orElse(null);
			if (parteCuerpoPrincipal == null && parteCuerpoSecundario == null) {
				ejercicioParteCuerpoParaBorrarLista.add(it);
			}
		});
		ejercicioParteCuerpoParaBorrarLista.removeAll(ejercicioParteCuerpoParaBorrarLista);

		ejercicio.getParteCuerpoPrincipalLista().stream().forEach(it -> {
			var ejercicioParteCuerpo = ejercicioActualizado.getEjercicioParteCuerpoLista().parallelStream().filter(em -> em.getParteCuerpo().getId().equals(it.getId()))
					.findAny().orElse(null);
			if (ejercicioParteCuerpo == null) {
				ejercicioParteCuerpo = new EjercicioParteCuerpo();
				ejercicioParteCuerpo.setParteCuerpo(it);
				ejercicioActualizado.addEjercicioParteCuerpo(ejercicioParteCuerpo);
			}

			ejercicioParteCuerpo.setPrincipal(Boolean.TRUE);
		});
		ejercicio.getParteCuerpoSecundarioLista().stream().forEach(it -> {
			var ejercicioParteCuerpo = ejercicioActualizado.getEjercicioParteCuerpoLista().parallelStream().filter(em -> em.getParteCuerpo().getId().equals(it.getId()))
					.findAny().orElse(null);
			if (ejercicioParteCuerpo == null) {
				ejercicioParteCuerpo = new EjercicioParteCuerpo();
				ejercicioParteCuerpo.setParteCuerpo(it);
				ejercicioActualizado.addEjercicioParteCuerpo(ejercicioParteCuerpo);
			}
			ejercicioParteCuerpo.setPrincipal(Boolean.FALSE);
		});

		if (ejercicio.getAudio() != null && StringUtils.isNotBlank(ejercicio.getAudio().getOriginalFilename())) {
			ejercicioActualizado.setTieneAudio(Boolean.TRUE);
			guardarEjercicioMultimediaFichero(ejercicio.getAudio(), ejercicio.getId(), rutaEstaticosEjerciciosAudios, "");
		}

		if (ejercicio.getFoto() != null && StringUtils.isNotBlank(ejercicio.getFoto().getOriginalFilename())) {
			guardarEjercicioMultimediaFichero(ejercicio.getFoto(), ejercicio.getId(), rutaEstaticosEjerciciosFotos, "");
		}

		if (ejercicio.getVideo() != null && StringUtils.isNotBlank(ejercicio.getVideo().getOriginalFilename())) {
			guardarEjercicioMultimediaFichero(ejercicio.getVideo(), ejercicio.getId(), rutaEstaticosEjerciciosVideos, "_x264");
		}

		if (ejercicio.getVideo480() != null && StringUtils.isNotBlank(ejercicio.getVideo480().getOriginalFilename())) {
			guardarEjercicioMultimediaFichero(ejercicio.getVideo480(), ejercicio.getId(), rutaEstaticosEjerciciosVideos480, "_x264_480");
		}

		ejercicioActualizado.setFechaHoraUltimaActualizacion(ZonedDateTime.now());
	}

	private void guardarEjercicioMultimediaFichero(MultipartFile fichero, Long ejercicioId, String ruta, String sufijo) {
		var extension = fichero.getOriginalFilename().lastIndexOf(".") > 0
				? fichero.getOriginalFilename().substring(fichero.getOriginalFilename().lastIndexOf("."), fichero.getOriginalFilename().length()).toLowerCase()
				: "";
		var nombreFinal = ejercicioId + sufijo + extension;
		gestorFicherosServicio.guardarFichero(fichero, nombreFinal, ruta);
	}

	@Transactional
	@Override
	public void cambiarEjercicioUnilateral(Long ejercicioId) {
		var ejercicio = buscarEjercicioPorId(ejercicioId);
		ejercicio.setEsUnilateral(ejercicio.getEsUnilateral() ? Boolean.FALSE : Boolean.TRUE);
	}

	@Transactional
	@Override
	public void actualizarEjercicioEstadistica() {
		var entrenadorActivoLista = entrenadorServicio.buscarEntrenadorActivoTodos();
//		var ejercicioEstadisticaLista = new ArrayList<EjercicioEntrenadorEstadistica>(); //** Alternativa en base a estadistica entrenador
		var ejercicioLista = new ArrayList<Ejercicio>();

		// Para cada entrenador
		entrenadorActivoLista.forEach(entrenador -> {
			var ejercicioEntrenadorEstadisticaMapa = new HashMap<Long, EjercicioEntrenadorEstadistica>();
			var buscarRutinaFormulario = new BuscarRutinaFormulario();
			buscarRutinaFormulario.setEntrenadorId(entrenador.getId());
			buscarRutinaFormulario.setFechaCreacionDesde(ZonedDateTime.now().minusMonths(12));
			var rutinaLista = rutinaServicio.buscarRutinaPorFiltro(buscarRutinaFormulario);

			// Para cada rutina
			rutinaLista.forEach(rutina -> {

				// Para cada ejercicio configurado en la rutina
				rutina.getRutinaFaseGrupoRepeticionEjercicioTodos().forEach(rutinaFaseGrupoRepeticionEjercicio -> {

					// Configuracion entrenador - ejercicio
					var ejercicioEntrenadorEstadistica = ejercicioEntrenadorEstadisticaMapa.get(rutinaFaseGrupoRepeticionEjercicio.getEjercicio().getId());
					if (ejercicioEntrenadorEstadistica == null) {
						ejercicioEntrenadorEstadistica = new EjercicioEntrenadorEstadistica();
						ejercicioEntrenadorEstadistica.setId(new EjercicioEntrenadorEstadisticaId(rutinaFaseGrupoRepeticionEjercicio.getEjercicio(), entrenador));
						ejercicioEntrenadorEstadistica.setNumeroUsosPorRepeticion(Integer.valueOf(0));
						ejercicioEntrenadorEstadistica.setNumeroUsosPorTiempo(Integer.valueOf(0));
						ejercicioEntrenadorEstadistica.setConfiguracionRepeticionesMasUsado(Integer.valueOf(0));
						ejercicioEntrenadorEstadistica.setConfiguracionTiempoMasUsado(Integer.valueOf(0));
						ejercicioEntrenadorEstadistica.setConfiguracionRepeticionesOcurrenciasMapa(new HashMap<>());
						ejercicioEntrenadorEstadistica.setConfiguracionTiempoOcurrenciasMapa(new HashMap<>());
						ejercicioEntrenadorEstadisticaMapa.put(rutinaFaseGrupoRepeticionEjercicio.getEjercicio().getId(), ejercicioEntrenadorEstadistica);
					}
					var repeticiones = rutinaFaseGrupoRepeticionEjercicio.getRepeticiones();
					var tiempo = rutinaFaseGrupoRepeticionEjercicio.getTiempo();

					if (repeticiones != null && repeticiones > 0) {
						ejercicioEntrenadorEstadistica.setNumeroUsosPorRepeticion(ejercicioEntrenadorEstadistica.getNumeroUsosPorRepeticion() + 1);
						ejercicioEntrenadorEstadistica.addConfiguracionRepeticionesOcurrencia(repeticiones);
					}

					if (tiempo != null && tiempo > 0) {
						ejercicioEntrenadorEstadistica.setNumeroUsosPorTiempo(ejercicioEntrenadorEstadistica.getNumeroUsosPorTiempo() + 1);
						ejercicioEntrenadorEstadistica.addConfiguracionTiempoOcurrencia(tiempo);
					}

					// Configuracion ejercicio
					var ejercicio = rutinaFaseGrupoRepeticionEjercicio.getEjercicio();
					if (!ejercicioLista.contains(ejercicio)) {
						ejercicio.setNumeroUsosPorRepeticion(Integer.valueOf(0));
						ejercicio.setNumeroUsosPorTiempo(Integer.valueOf(0));
						ejercicio.setConfiguracionRepeticionesMasUsado(Integer.valueOf(0));
						ejercicio.setConfiguracionTiempoMasUsado(Integer.valueOf(0));
						ejercicio.setConfiguracionRepeticionesOcurrenciasMapa(new HashMap<>());
						ejercicio.setConfiguracionTiempoOcurrenciasMapa(new HashMap<>());
					}
					if (repeticiones != null && repeticiones > 0) {
						ejercicio.setNumeroUsosPorRepeticion(ejercicio.getNumeroUsosPorRepeticion() + 1);
						ejercicio.addConfiguracionRepeticionesOcurrencia(repeticiones);
					}

					if (tiempo != null && tiempo > 0) {
						ejercicio.setNumeroUsosPorTiempo(ejercicio.getNumeroUsosPorTiempo() + 1);
						ejercicio.addConfiguracionTiempoOcurrencia(tiempo);
					}
					ejercicioLista.add(ejercicio);

				});
			});

			// Cálculos finales estadisticas por entrenador-ejercicio
			ejercicioEntrenadorEstadisticaMapa.values().forEach(ejercicioEntrenadorEstadistica -> {
				ejercicioEntrenadorEstadistica
						.setConfiguracionRepeticionesMasUsado(calcularValorMasRepetido(ejercicioEntrenadorEstadistica.getConfiguracionRepeticionesOcurrenciasMapa()));
				ejercicioEntrenadorEstadistica
						.setConfiguracionTiempoMasUsado(calcularValorMasRepetido(ejercicioEntrenadorEstadistica.getConfiguracionTiempoOcurrenciasMapa()));

//				ejercicioEstadisticaLista.add(ejercicioEntrenadorEstadistica);** Alternativa en base a estadistica entrenador
				ejercicioEntrenadorEstadisticaRepositorio.save(ejercicioEntrenadorEstadistica);
			});
		});

		// Cálculos finales estadisticas por ejercicio
		ejercicioLista.forEach(ejercicio -> {
			ejercicio.setConfiguracionRepeticionesMasUsado(calcularValorMasRepetido(ejercicio.getConfiguracionRepeticionesOcurrenciasMapa()));
			ejercicio.setConfiguracionTiempoMasUsado(calcularValorMasRepetido(ejercicio.getConfiguracionTiempoOcurrenciasMapa()));
		});

//		// Y ahora actualizar estadistica global ** Alternativa en base a estadistica entrenador
//		// Agrupar por ejercicio
//		var ejercicioMapa = ejercicioEstadisticaLista.stream().collect(Collectors.groupingBy(it -> it.getId().getEjercicio()));
//
//		// Recorrer
//		ejercicioMapa.keySet().forEach(ejercicio -> {
//			ejercicio.setNumeroUsosPorRepeticion(Integer.valueOf(0));
//			ejercicio.setNumeroUsosPorTiempo(Integer.valueOf(0));
//			ejercicio.setConfiguracionRepeticionesMasUsado(Integer.valueOf(0));
//			ejercicio.setConfiguracionTiempoMasUsado(Integer.valueOf(0));
//			var ejercicioEntrenadorEstadisticaLista = ejercicioMapa.get(ejercicio);
//			ejercicioEntrenadorEstadisticaLista.forEach(ejercicioEntrenadorEstadistica -> {
//				ejercicio.setNumeroUsosPorRepeticion(ejercicio.getNumeroUsosPorRepeticion() + ejercicioEntrenadorEstadistica.getNumeroUsosPorRepeticion());
//				ejercicio.setNumeroUsosPorTiempo(ejercicio.getNumeroUsosPorTiempo() + ejercicioEntrenadorEstadistica.getNumeroUsosPorTiempo());
//				ejercicio.addConfiguracionRepeticionesOcurrencia(ejercicioEntrenadorEstadistica.getConfiguracionRepeticionesMasUsado());
//				ejercicio.addConfiguracionTiempoOcurrencia(ejercicioEntrenadorEstadistica.getConfiguracionTiempoMasUsado());
//			});
//			ejercicio.setConfiguracionRepeticionesMasUsado(calcularValorMasRepetido(ejercicio.getConfiguracionRepeticionesOcurrenciasMapa()));
//			ejercicio.setConfiguracionTiempoMasUsado(calcularValorMasRepetido(ejercicio.getConfiguracionTiempoOcurrenciasMapa()));
//		});

		ejercicioEstadisticaCache.borrarCacheTodo();

	}

	public <K, V extends Comparable<V>> K calcularValorMasRepetido(Map<K, V> map) {
		var maxEntry = map.entrySet().stream().max((Entry<K, V> e1, Entry<K, V> e2) -> e1.getValue().compareTo(e2.getValue()));
		return maxEntry.isPresent() ? maxEntry.get().getKey() : null;
	}

	@Override
	public EjercicioEntrenadorEstadistica buscarEjercicioEntrenadorEstadisticaPorEntrenadorIdEjercicioId(Long entrenadorId, Long ejercicioId) {
		return ejercicioEntrenadorEstadisticaRepositorio.findByIdEntrenadorIdAndIdEjercicioId(entrenadorId, ejercicioId);
	}
}
