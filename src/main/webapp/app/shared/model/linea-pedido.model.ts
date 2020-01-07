import { IPedido } from 'app/shared/model/pedido.model';
import { IProducto } from 'app/shared/model/producto.model';

export interface ILineaPedido {
  id?: number;
  cantidad?: number;
  pedido?: IPedido;
  producto?: IProducto;
}

export class LineaPedido implements ILineaPedido {
  constructor(public id?: number, public cantidad?: number, public pedido?: IPedido, public producto?: IProducto) {}
}
