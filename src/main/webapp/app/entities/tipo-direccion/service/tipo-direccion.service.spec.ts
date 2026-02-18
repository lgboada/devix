import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { ITipoDireccion } from '../tipo-direccion.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../tipo-direccion.test-samples';

import { TipoDireccionService } from './tipo-direccion.service';

const requireRestSample: ITipoDireccion = {
  ...sampleWithRequiredData,
};

describe('TipoDireccion Service', () => {
  let service: TipoDireccionService;
  let httpMock: HttpTestingController;
  let expectedResult: ITipoDireccion | ITipoDireccion[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(TipoDireccionService);
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

    it('should create a TipoDireccion', () => {
      const tipoDireccion = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(tipoDireccion).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TipoDireccion', () => {
      const tipoDireccion = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(tipoDireccion).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TipoDireccion', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TipoDireccion', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a TipoDireccion', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addTipoDireccionToCollectionIfMissing', () => {
      it('should add a TipoDireccion to an empty array', () => {
        const tipoDireccion: ITipoDireccion = sampleWithRequiredData;
        expectedResult = service.addTipoDireccionToCollectionIfMissing([], tipoDireccion);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tipoDireccion);
      });

      it('should not add a TipoDireccion to an array that contains it', () => {
        const tipoDireccion: ITipoDireccion = sampleWithRequiredData;
        const tipoDireccionCollection: ITipoDireccion[] = [
          {
            ...tipoDireccion,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addTipoDireccionToCollectionIfMissing(tipoDireccionCollection, tipoDireccion);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TipoDireccion to an array that doesn't contain it", () => {
        const tipoDireccion: ITipoDireccion = sampleWithRequiredData;
        const tipoDireccionCollection: ITipoDireccion[] = [sampleWithPartialData];
        expectedResult = service.addTipoDireccionToCollectionIfMissing(tipoDireccionCollection, tipoDireccion);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tipoDireccion);
      });

      it('should add only unique TipoDireccion to an array', () => {
        const tipoDireccionArray: ITipoDireccion[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const tipoDireccionCollection: ITipoDireccion[] = [sampleWithRequiredData];
        expectedResult = service.addTipoDireccionToCollectionIfMissing(tipoDireccionCollection, ...tipoDireccionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const tipoDireccion: ITipoDireccion = sampleWithRequiredData;
        const tipoDireccion2: ITipoDireccion = sampleWithPartialData;
        expectedResult = service.addTipoDireccionToCollectionIfMissing([], tipoDireccion, tipoDireccion2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tipoDireccion);
        expect(expectedResult).toContain(tipoDireccion2);
      });

      it('should accept null and undefined values', () => {
        const tipoDireccion: ITipoDireccion = sampleWithRequiredData;
        expectedResult = service.addTipoDireccionToCollectionIfMissing([], null, tipoDireccion, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tipoDireccion);
      });

      it('should return initial array if no TipoDireccion is added', () => {
        const tipoDireccionCollection: ITipoDireccion[] = [sampleWithRequiredData];
        expectedResult = service.addTipoDireccionToCollectionIfMissing(tipoDireccionCollection, undefined, null);
        expect(expectedResult).toEqual(tipoDireccionCollection);
      });
    });

    describe('compareTipoDireccion', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareTipoDireccion(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 22481 };
        const entity2 = null;

        const compareResult1 = service.compareTipoDireccion(entity1, entity2);
        const compareResult2 = service.compareTipoDireccion(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 22481 };
        const entity2 = { id: 18715 };

        const compareResult1 = service.compareTipoDireccion(entity1, entity2);
        const compareResult2 = service.compareTipoDireccion(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 22481 };
        const entity2 = { id: 22481 };

        const compareResult1 = service.compareTipoDireccion(entity1, entity2);
        const compareResult2 = service.compareTipoDireccion(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
