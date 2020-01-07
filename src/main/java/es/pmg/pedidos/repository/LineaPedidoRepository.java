package es.pmg.pedidos.repository;

import es.pmg.pedidos.domain.LineaPedido;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the LineaPedido entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LineaPedidoRepository extends JpaRepository<LineaPedido, Long>, JpaSpecificationExecutor<LineaPedido> {

}
