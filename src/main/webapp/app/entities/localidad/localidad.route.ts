import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ILocalidad, Localidad } from 'app/shared/model/localidad.model';
import { LocalidadService } from './localidad.service';
import { LocalidadComponent } from './localidad.component';
import { LocalidadDetailComponent } from './localidad-detail.component';
import { LocalidadUpdateComponent } from './localidad-update.component';

@Injectable({ providedIn: 'root' })
export class LocalidadResolve implements Resolve<ILocalidad> {
  constructor(private service: LocalidadService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ILocalidad> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((localidad: HttpResponse<Localidad>) => {
          if (localidad.body) {
            return of(localidad.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Localidad());
  }
}

export const localidadRoute: Routes = [
  {
    path: '',
    component: LocalidadComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Localidads'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: LocalidadDetailComponent,
    resolve: {
      localidad: LocalidadResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Localidads'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: LocalidadUpdateComponent,
    resolve: {
      localidad: LocalidadResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Localidads'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: LocalidadUpdateComponent,
    resolve: {
      localidad: LocalidadResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Localidads'
    },
    canActivate: [UserRouteAccessService]
  }
];
