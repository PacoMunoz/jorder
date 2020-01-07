import { ICliente } from 'app/shared/model/cliente.model';
import { ILocalidad } from 'app/shared/model/localidad.model';

export interface IDireccion {
  id?: number;
  nombreVia?: string;
  numero?: number;
  piso?: string;
  bloque?: string;
  puerta?: string;
  escalera?: string;
  usuario?: ICliente;
  localidad?: ILocalidad;
}

export class Direccion implements IDireccion {
  constructor(
    public id?: number,
    public nombreVia?: string,
    public numero?: number,
    public piso?: string,
    public bloque?: string,
    public puerta?: string,
    public escalera?: string,
    public usuario?: ICliente,
    public localidad?: ILocalidad
  ) {}
}
