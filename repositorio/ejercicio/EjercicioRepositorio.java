package es.pocketrainer.repositorio.ejercicio;

import org.springframework.data.jpa.repository.JpaRepository;

import es.pocketrainer.modelo.ejercicio.Ejercicio;

public interface EjercicioRepositorio extends JpaRepository<Ejercicio, Long>, EjercicioRepositorioExtendido {

}
