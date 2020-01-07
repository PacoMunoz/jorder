import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IFamilia, Familia } from 'app/shared/model/familia.model';
import { FamiliaService } from './familia.service';
import { FamiliaComponent } from './familia.component';
import { FamiliaDetailComponent } from './familia-detail.component';
import { FamiliaUpdateComponent } from './familia-update.component';

@Injectable({ providedIn: 'root' })
export class FamiliaResolve implements Resolve<IFamilia> {
  constructor(private service: FamiliaService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFamilia> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((familia: HttpResponse<Familia>) => {
          if (familia.body) {
            return of(familia.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Familia());
  }
}

export const familiaRoute: Routes = [
  {
    path: '',
    component: FamiliaComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Familias'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: FamiliaDetailComponent,
    resolve: {
      familia: FamiliaResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Familias'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: FamiliaUpdateComponent,
    resolve: {
      familia: FamiliaResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Familias'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: FamiliaUpdateComponent,
    resolve: {
      familia: FamiliaResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Familias'
    },
    canActivate: [UserRouteAccessService]
  }
];
