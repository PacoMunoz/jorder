export interface IEstadoPedido {
  id?: number;
  codigo?: string;
  descripcion?: string;
}

export class EstadoPedido implements IEstadoPedido {
  constructor(public id?: number, public codigo?: string, public descripcion?: string) {}
}
