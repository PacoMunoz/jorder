import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IFormaPago, FormaPago } from 'app/shared/model/forma-pago.model';
import { FormaPagoService } from './forma-pago.service';
import { FormaPagoComponent } from './forma-pago.component';
import { FormaPagoDetailComponent } from './forma-pago-detail.component';
import { FormaPagoUpdateComponent } from './forma-pago-update.component';

@Injectable({ providedIn: 'root' })
export class FormaPagoResolve implements Resolve<IFormaPago> {
  constructor(private service: FormaPagoService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFormaPago> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((formaPago: HttpResponse<FormaPago>) => {
          if (formaPago.body) {
            return of(formaPago.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new FormaPago());
  }
}

export const formaPagoRoute: Routes = [
  {
    path: '',
    component: FormaPagoComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'FormaPagos'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: FormaPagoDetailComponent,
    resolve: {
      formaPago: FormaPagoResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'FormaPagos'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: FormaPagoUpdateComponent,
    resolve: {
      formaPago: FormaPagoResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'FormaPagos'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: FormaPagoUpdateComponent,
    resolve: {
      formaPago: FormaPagoResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'FormaPagos'
    },
    canActivate: [UserRouteAccessService]
  }
];
