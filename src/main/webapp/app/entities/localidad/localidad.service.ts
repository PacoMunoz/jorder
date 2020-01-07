import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ILocalidad } from 'app/shared/model/localidad.model';

type EntityResponseType = HttpResponse<ILocalidad>;
type EntityArrayResponseType = HttpResponse<ILocalidad[]>;

@Injectable({ providedIn: 'root' })
export class LocalidadService {
  public resourceUrl = SERVER_API_URL + 'api/localidads';

  constructor(protected http: HttpClient) {}

  create(localidad: ILocalidad): Observable<EntityResponseType> {
    return this.http.post<ILocalidad>(this.resourceUrl, localidad, { observe: 'response' });
  }

  update(localidad: ILocalidad): Observable<EntityResponseType> {
    return this.http.put<ILocalidad>(this.resourceUrl, localidad, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ILocalidad>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILocalidad[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
