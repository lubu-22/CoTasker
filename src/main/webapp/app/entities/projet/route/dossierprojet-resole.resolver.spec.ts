import { TestBed } from '@angular/core/testing';

import { DossierprojetResoleResolver } from './dossierprojet-resole.resolver';

describe('DossierprojetResoleResolver', () => {
  let resolver: DossierprojetResoleResolver;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    resolver = TestBed.inject(DossierprojetResoleResolver);
  });

  it('should be created', () => {
    expect(resolver).toBeTruthy();
  });
});
