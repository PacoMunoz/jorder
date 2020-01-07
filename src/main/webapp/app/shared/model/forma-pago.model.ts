export interface IFormaPago {
  id?: number;
  codigo?: string;
  descripcion?: string;
}

export class FormaPago implements IFormaPago {
  constructor(public id?: number, public codigo?: string, public descripcion?: string) {}
}
