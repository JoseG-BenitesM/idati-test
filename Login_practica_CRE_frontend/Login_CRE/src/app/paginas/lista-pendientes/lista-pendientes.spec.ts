import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListaPendientes } from './lista-pendientes';

describe('ListaPendientes', () => {
  let component: ListaPendientes;
  let fixture: ComponentFixture<ListaPendientes>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ListaPendientes]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ListaPendientes);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
