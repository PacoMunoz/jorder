export interface IFamilia {
  id?: number;
  codigo?: string;
  descripcion?: string;
}

export class Familia implements IFamilia {
  constructor(public id?: number, public codigo?: string, public descripcion?: string) {}
}
