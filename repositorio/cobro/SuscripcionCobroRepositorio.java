package es.pocketrainer.repositorio.cobro;

import org.springframework.data.jpa.repository.JpaRepository;

import es.pocketrainer.modelo.cobro.SuscripcionCobro;

public interface SuscripcionCobroRepositorio extends JpaRepository<SuscripcionCobro, Long>, SuscripcionCobroRepositorioExtendido {

}
