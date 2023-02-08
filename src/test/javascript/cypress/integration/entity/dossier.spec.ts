import { entityItemSelector } from '../../support/commands';
import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityCreateCancelButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('Dossier e2e test', () => {
  const dossierPageUrl = '/dossier';
  const dossierPageUrlPattern = new RegExp('/dossier(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const dossierSample = {};

  let dossier: any;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/dossiers+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/dossiers').as('postEntityRequest');
    cy.intercept('DELETE', '/api/dossiers/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (dossier) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/dossiers/${dossier.id}`,
      }).then(() => {
        dossier = undefined;
      });
    }
  });

  it('Dossiers menu should load Dossiers page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('dossier');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Dossier').should('exist');
    cy.url().should('match', dossierPageUrlPattern);
  });

  describe('Dossier page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(dossierPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Dossier page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/dossier/new$'));
        cy.getEntityCreateUpdateHeading('Dossier');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', dossierPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/dossiers',
          body: dossierSample,
        }).then(({ body }) => {
          dossier = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/dossiers+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/dossiers?page=0&size=20>; rel="last",<http://localhost/api/dossiers?page=0&size=20>; rel="first"',
              },
              body: [dossier],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(dossierPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Dossier page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('dossier');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', dossierPageUrlPattern);
      });

      it('edit button click should load edit Dossier page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Dossier');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', dossierPageUrlPattern);
      });

      it('last delete button click should delete instance of Dossier', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('dossier').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', dossierPageUrlPattern);

        dossier = undefined;
      });
    });
  });

  describe('new Dossier page', () => {
    beforeEach(() => {
      cy.visit(`${dossierPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Dossier');
    });

    it('should create an instance of Dossier', () => {
      cy.get(`[data-cy="cheminD"]`).type('b').should('have.value', 'b');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        dossier = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', dossierPageUrlPattern);
    });
  });
});
