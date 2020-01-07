export interface ILocalidad {
  id?: number;
  codigo?: string;
  nombre?: string;
  codigoPostal?: number;
}

export class Localidad implements ILocalidad {
  constructor(public id?: number, public codigo?: string, public nombre?: string, public codigoPostal?: number) {}
}
