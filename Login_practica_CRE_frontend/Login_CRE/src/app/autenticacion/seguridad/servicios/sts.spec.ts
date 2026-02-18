import { TestBed } from '@angular/core/testing';

import { STS } from './sts';

describe('STS', () => {
  let service: STS;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(STS);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
