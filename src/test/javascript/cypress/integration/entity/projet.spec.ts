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

describe('Projet e2e test', () => {
  const projetPageUrl = '/projet';
  const projetPageUrlPattern = new RegExp('/projet(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const projetSample = {};

  let projet: any;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/projets+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/projets').as('postEntityRequest');
    cy.intercept('DELETE', '/api/projets/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (projet) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/projets/${projet.id}`,
      }).then(() => {
        projet = undefined;
      });
    }
  });

  it('Projets menu should load Projets page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('projet');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Projet').should('exist');
    cy.url().should('match', projetPageUrlPattern);
  });

  describe('Projet page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(projetPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Projet page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/projet/new$'));
        cy.getEntityCreateUpdateHeading('Projet');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', projetPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/projets',
          body: projetSample,
        }).then(({ body }) => {
          projet = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/projets+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/projets?page=0&size=20>; rel="last",<http://localhost/api/projets?page=0&size=20>; rel="first"',
              },
              body: [projet],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(projetPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Projet page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('projet');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', projetPageUrlPattern);
      });

      it('edit button click should load edit Projet page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Projet');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', projetPageUrlPattern);
      });

      it('last delete button click should delete instance of Projet', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('projet').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', projetPageUrlPattern);

        projet = undefined;
      });
    });
  });

  describe('new Projet page', () => {
    beforeEach(() => {
      cy.visit(`${projetPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Projet');
    });

    it('should create an instance of Projet', () => {
      cy.get(`[data-cy="intitule"]`).type('Cambridgeshire').should('have.value', 'Cambridgeshire');

      cy.get(`[data-cy="descriptionProjet"]`).type('Baby').should('have.value', 'Baby');

      cy.get(`[data-cy="type"]`).select('DEVELOPPEMENT');

      cy.get(`[data-cy="etape"]`).select('RECETTE');

      cy.get(`[data-cy="dateCreation"]`).type('2022-08-24').should('have.value', '2022-08-24');

      cy.get(`[data-cy="dateFin"]`).type('2022-08-24').should('have.value', '2022-08-24');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        projet = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', projetPageUrlPattern);
    });
  });
});
