import { IFamilia } from 'app/shared/model/familia.model';

export interface IProducto {
  id?: number;
  codigo?: string;
  descripcion?: string;
  precio?: number;
  imagenContentType?: string;
  imagen?: any;
  disponible?: boolean;
  familia?: IFamilia;
}

export class Producto implements IProducto {
  constructor(
    public id?: number,
    public codigo?: string,
    public descripcion?: string,
    public precio?: number,
    public imagenContentType?: string,
    public imagen?: any,
    public disponible?: boolean,
    public familia?: IFamilia
  ) {
    this.disponible = this.disponible || false;
  }
}
