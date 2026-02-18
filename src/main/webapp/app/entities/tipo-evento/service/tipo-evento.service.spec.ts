import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { ITipoEvento } from '../tipo-evento.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../tipo-evento.test-samples';

import { TipoEventoService } from './tipo-evento.service';

const requireRestSample: ITipoEvento = {
  ...sampleWithRequiredData,
};

describe('TipoEvento Service', () => {
  let service: TipoEventoService;
  let httpMock: HttpTestingController;
  let expectedResult: ITipoEvento | ITipoEvento[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(TipoEventoService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a TipoEvento', () => {
      const tipoEvento = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(tipoEvento).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TipoEvento', () => {
      const tipoEvento = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(tipoEvento).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TipoEvento', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TipoEvento', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a TipoEvento', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addTipoEventoToCollectionIfMissing', () => {
      it('should add a TipoEvento to an empty array', () => {
        const tipoEvento: ITipoEvento = sampleWithRequiredData;
        expectedResult = service.addTipoEventoToCollectionIfMissing([], tipoEvento);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tipoEvento);
      });

      it('should not add a TipoEvento to an array that contains it', () => {
        const tipoEvento: ITipoEvento = sampleWithRequiredData;
        const tipoEventoCollection: ITipoEvento[] = [
          {
            ...tipoEvento,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addTipoEventoToCollectionIfMissing(tipoEventoCollection, tipoEvento);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TipoEvento to an array that doesn't contain it", () => {
        const tipoEvento: ITipoEvento = sampleWithRequiredData;
        const tipoEventoCollection: ITipoEvento[] = [sampleWithPartialData];
        expectedResult = service.addTipoEventoToCollectionIfMissing(tipoEventoCollection, tipoEvento);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tipoEvento);
      });

      it('should add only unique TipoEvento to an array', () => {
        const tipoEventoArray: ITipoEvento[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const tipoEventoCollection: ITipoEvento[] = [sampleWithRequiredData];
        expectedResult = service.addTipoEventoToCollectionIfMissing(tipoEventoCollection, ...tipoEventoArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const tipoEvento: ITipoEvento = sampleWithRequiredData;
        const tipoEvento2: ITipoEvento = sampleWithPartialData;
        expectedResult = service.addTipoEventoToCollectionIfMissing([], tipoEvento, tipoEvento2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tipoEvento);
        expect(expectedResult).toContain(tipoEvento2);
      });

      it('should accept null and undefined values', () => {
        const tipoEvento: ITipoEvento = sampleWithRequiredData;
        expectedResult = service.addTipoEventoToCollectionIfMissing([], null, tipoEvento, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tipoEvento);
      });

      it('should return initial array if no TipoEvento is added', () => {
        const tipoEventoCollection: ITipoEvento[] = [sampleWithRequiredData];
        expectedResult = service.addTipoEventoToCollectionIfMissing(tipoEventoCollection, undefined, null);
        expect(expectedResult).toEqual(tipoEventoCollection);
      });
    });

    describe('compareTipoEvento', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareTipoEvento(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 26103 };
        const entity2 = null;

        const compareResult1 = service.compareTipoEvento(entity1, entity2);
        const compareResult2 = service.compareTipoEvento(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 26103 };
        const entity2 = { id: 19774 };

        const compareResult1 = service.compareTipoEvento(entity1, entity2);
        const compareResult2 = service.compareTipoEvento(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 26103 };
        const entity2 = { id: 26103 };

        const compareResult1 = service.compareTipoEvento(entity1, entity2);
        const compareResult2 = service.compareTipoEvento(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
