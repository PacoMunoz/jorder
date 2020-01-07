import { IUser } from 'app/core/user/user.model';

export interface ICliente {
  id?: number;
  nombre?: string;
  apellido?: string;
  dni?: string;
  user?: IUser;
}

export class Cliente implements ICliente {
  constructor(public id?: number, public nombre?: string, public apellido?: string, public dni?: string, public user?: IUser) {}
}
