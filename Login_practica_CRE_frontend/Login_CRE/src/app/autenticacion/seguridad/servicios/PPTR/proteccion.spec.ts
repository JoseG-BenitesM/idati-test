import { TestBed } from '@angular/core/testing';

import { Proteccion } from './proteccion';

describe('Proteccion', () => {
  let service: Proteccion;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(Proteccion);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
