import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PedidosTestModule } from '../../../test.module';
import { FormaPagoDetailComponent } from 'app/entities/forma-pago/forma-pago-detail.component';
import { FormaPago } from 'app/shared/model/forma-pago.model';

describe('Component Tests', () => {
  describe('FormaPago Management Detail Component', () => {
    let comp: FormaPagoDetailComponent;
    let fixture: ComponentFixture<FormaPagoDetailComponent>;
    const route = ({ data: of({ formaPago: new FormaPago(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PedidosTestModule],
        declarations: [FormaPagoDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(FormaPagoDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(FormaPagoDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load formaPago on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.formaPago).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
