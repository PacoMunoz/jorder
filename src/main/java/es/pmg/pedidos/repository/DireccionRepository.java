package es.pmg.pedidos.repository;

import es.pmg.pedidos.domain.Direccion;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Direccion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DireccionRepository extends JpaRepository<Direccion, Long>, JpaSpecificationExecutor<Direccion> {

}
