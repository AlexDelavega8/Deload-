package es.pocketrainer.repositorio.parametrizacion;

import org.springframework.data.jpa.repository.JpaRepository;

import es.pocketrainer.modelo.parametrizacion.ParametroConfiguracion;

public interface ParametroConfiguracionRepositorio extends JpaRepository<ParametroConfiguracion, String>, ParametroConfiguracionRepositorioExtendido {

	ParametroConfiguracion findByCodigo(String codigo);
}
