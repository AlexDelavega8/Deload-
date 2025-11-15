package es.pocketrainer.repositorio.material;

import org.springframework.data.jpa.repository.JpaRepository;

import es.pocketrainer.modelo.ejercicio.Material;

public interface MaterialRepositorio extends JpaRepository<Material, Long>, MaterialRepositorioExtendido {

}
