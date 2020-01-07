package es.pmg.pedidos.repository;

import es.pmg.pedidos.domain.EstadoPedido;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the EstadoPedido entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EstadoPedidoRepository extends JpaRepository<EstadoPedido, Long>, JpaSpecificationExecutor<EstadoPedido> {

}
