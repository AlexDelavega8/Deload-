package es.pocketrainer.repositorio.migracion;

import org.springframework.data.jpa.repository.JpaRepository;

import es.pocketrainer.modelo.migracion.EjercicioMigracion;

public interface EjercicioMigracionRepositorio extends JpaRepository<EjercicioMigracion, String>, EjercicioMigracionRepositorioExtendido {

}
