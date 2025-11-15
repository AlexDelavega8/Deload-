package es.pocketrainer.repositorio.servicio;

import org.springframework.data.jpa.repository.JpaRepository;

import es.pocketrainer.modelo.servicio.Servicio;

public interface ServicioRepositorio extends JpaRepository<Servicio, Long>, ServicioRepositorioExtendido {

}
