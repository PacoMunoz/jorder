package es.pmg.pedidos.repository;

import es.pmg.pedidos.domain.FormaPago;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the FormaPago entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FormaPagoRepository extends JpaRepository<FormaPago, Long>, JpaSpecificationExecutor<FormaPago> {

}
