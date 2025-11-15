package es.pocketrainer.repositorio.rutina;

import org.springframework.data.jpa.repository.JpaRepository;

import es.pocketrainer.modelo.rutina.RutinaFaseGrupo;

public interface RutinaFaseGrupoRepositorio extends JpaRepository<RutinaFaseGrupo, Long>, RutinaFaseGrupoRepositorioExtendido {

}
