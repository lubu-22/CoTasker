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

describe('Fichier e2e test', () => {
  const fichierPageUrl = '/fichier';
  const fichierPageUrlPattern = new RegExp('/fichier(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const fichierSample = {};

  let fichier: any;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/fichiers+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/fichiers').as('postEntityRequest');
    cy.intercept('DELETE', '/api/fichiers/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (fichier) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/fichiers/${fichier.id}`,
      }).then(() => {
        fichier = undefined;
      });
    }
  });

  it('Fichiers menu should load Fichiers page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('fichier');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Fichier').should('exist');
    cy.url().should('match', fichierPageUrlPattern);
  });

  describe('Fichier page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(fichierPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Fichier page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/fichier/new$'));
        cy.getEntityCreateUpdateHeading('Fichier');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', fichierPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/fichiers',
          body: fichierSample,
        }).then(({ body }) => {
          fichier = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/fichiers+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/fichiers?page=0&size=20>; rel="last",<http://localhost/api/fichiers?page=0&size=20>; rel="first"',
              },
              body: [fichier],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(fichierPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Fichier page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('fichier');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', fichierPageUrlPattern);
      });

      it('edit button click should load edit Fichier page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Fichier');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', fichierPageUrlPattern);
      });

      it('last delete button click should delete instance of Fichier', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('fichier').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', fichierPageUrlPattern);

        fichier = undefined;
      });
    });
  });

  describe('new Fichier page', () => {
    beforeEach(() => {
      cy.visit(`${fichierPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Fichier');
    });

    it('should create an instance of Fichier', () => {
      cy.get(`[data-cy="nomF"]`).type('Gloves Music open-source').should('have.value', 'Gloves Music open-source');

      cy.get(`[data-cy="cheminF"]`).type('Tenge').should('have.value', 'Tenge');

      cy.get(`[data-cy="codeF"]`)
        .type('multi-state Poitou-Charentes foreground')
        .should('have.value', 'multi-state Poitou-Charentes foreground');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        fichier = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', fichierPageUrlPattern);
    });
  });
});
