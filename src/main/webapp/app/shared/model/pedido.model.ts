import { Moment } from 'moment';
import { IDireccion } from 'app/shared/model/direccion.model';
import { ICliente } from 'app/shared/model/cliente.model';
import { IFormaPago } from 'app/shared/model/forma-pago.model';
import { IEstadoPedido } from 'app/shared/model/estado-pedido.model';

export interface IPedido {
  id?: number;
  codigo?: string;
  aDomicilio?: boolean;
  fechaPedido?: Moment;
  direccion?: IDireccion;
  cliente?: ICliente;
  formaPago?: IFormaPago;
  estadoPedido?: IEstadoPedido;
}

export class Pedido implements IPedido {
  constructor(
    public id?: number,
    public codigo?: string,
    public aDomicilio?: boolean,
    public fechaPedido?: Moment,
    public direccion?: IDireccion,
    public cliente?: ICliente,
    public formaPago?: IFormaPago,
    public estadoPedido?: IEstadoPedido
  ) {
    this.aDomicilio = this.aDomicilio || false;
  }
}
