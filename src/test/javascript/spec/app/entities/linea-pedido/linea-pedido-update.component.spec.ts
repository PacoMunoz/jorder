import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { PedidosTestModule } from '../../../test.module';
import { LineaPedidoUpdateComponent } from 'app/entities/linea-pedido/linea-pedido-update.component';
import { LineaPedidoService } from 'app/entities/linea-pedido/linea-pedido.service';
import { LineaPedido } from 'app/shared/model/linea-pedido.model';

describe('Component Tests', () => {
  describe('LineaPedido Management Update Component', () => {
    let comp: LineaPedidoUpdateComponent;
    let fixture: ComponentFixture<LineaPedidoUpdateComponent>;
    let service: LineaPedidoService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PedidosTestModule],
        declarations: [LineaPedidoUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(LineaPedidoUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(LineaPedidoUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(LineaPedidoService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new LineaPedido(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new LineaPedido();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
