import { TestBed } from '@angular/core/testing';

import { NavExtraDataService } from './nav-extra-data.service';

describe('NavExtraDataService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: NavExtraDataService = TestBed.get(NavExtraDataService);
    expect(service).toBeTruthy();
  });
});
