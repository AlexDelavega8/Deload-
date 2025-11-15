package es.pocketrainer.repositorio.ejercicio;

import org.springframework.data.jpa.repository.JpaRepository;

import es.pocketrainer.modelo.ejercicio.EjercicioEntrenadorEstadistica;

public interface EjercicioEntrenadorEstadisticaRepositorio extends JpaRepository<EjercicioEntrenadorEstadistica, Long> {

	EjercicioEntrenadorEstadistica findByIdEntrenadorIdAndIdEjercicioId(Long entrenadorId, Long ejercicioId);

}
