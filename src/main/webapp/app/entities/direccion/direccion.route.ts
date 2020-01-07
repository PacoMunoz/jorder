import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IDireccion, Direccion } from 'app/shared/model/direccion.model';
import { DireccionService } from './direccion.service';
import { DireccionComponent } from './direccion.component';
import { DireccionDetailComponent } from './direccion-detail.component';
import { DireccionUpdateComponent } from './direccion-update.component';

@Injectable({ providedIn: 'root' })
export class DireccionResolve implements Resolve<IDireccion> {
  constructor(private service: DireccionService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDireccion> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((direccion: HttpResponse<Direccion>) => {
          if (direccion.body) {
            return of(direccion.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Direccion());
  }
}

export const direccionRoute: Routes = [
  {
    path: '',
    component: DireccionComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Direccions'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: DireccionDetailComponent,
    resolve: {
      direccion: DireccionResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Direccions'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: DireccionUpdateComponent,
    resolve: {
      direccion: DireccionResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Direccions'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: DireccionUpdateComponent,
    resolve: {
      direccion: DireccionResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Direccions'
    },
    canActivate: [UserRouteAccessService]
  }
];
