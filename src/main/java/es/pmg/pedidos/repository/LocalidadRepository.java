package es.pmg.pedidos.repository;

import es.pmg.pedidos.domain.Localidad;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Localidad entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LocalidadRepository extends JpaRepository<Localidad, Long>, JpaSpecificationExecutor<Localidad> {

}
