import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PedidosTestModule } from '../../../test.module';
import { LineaPedidoDetailComponent } from 'app/entities/linea-pedido/linea-pedido-detail.component';
import { LineaPedido } from 'app/shared/model/linea-pedido.model';

describe('Component Tests', () => {
  describe('LineaPedido Management Detail Component', () => {
    let comp: LineaPedidoDetailComponent;
    let fixture: ComponentFixture<LineaPedidoDetailComponent>;
    const route = ({ data: of({ lineaPedido: new LineaPedido(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PedidosTestModule],
        declarations: [LineaPedidoDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(LineaPedidoDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(LineaPedidoDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load lineaPedido on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.lineaPedido).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
