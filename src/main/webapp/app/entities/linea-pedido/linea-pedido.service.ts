import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ILineaPedido } from 'app/shared/model/linea-pedido.model';

type EntityResponseType = HttpResponse<ILineaPedido>;
type EntityArrayResponseType = HttpResponse<ILineaPedido[]>;

@Injectable({ providedIn: 'root' })
export class LineaPedidoService {
  public resourceUrl = SERVER_API_URL + 'api/linea-pedidos';

  constructor(protected http: HttpClient) {}

  create(lineaPedido: ILineaPedido): Observable<EntityResponseType> {
    return this.http.post<ILineaPedido>(this.resourceUrl, lineaPedido, { observe: 'response' });
  }

  update(lineaPedido: ILineaPedido): Observable<EntityResponseType> {
    return this.http.put<ILineaPedido>(this.resourceUrl, lineaPedido, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ILineaPedido>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILineaPedido[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
