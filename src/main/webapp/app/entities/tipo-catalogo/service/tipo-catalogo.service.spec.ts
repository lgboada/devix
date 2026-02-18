import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { ITipoCatalogo } from '../tipo-catalogo.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../tipo-catalogo.test-samples';

import { TipoCatalogoService } from './tipo-catalogo.service';

const requireRestSample: ITipoCatalogo = {
  ...sampleWithRequiredData,
};

describe('TipoCatalogo Service', () => {
  let service: TipoCatalogoService;
  let httpMock: HttpTestingController;
  let expectedResult: ITipoCatalogo | ITipoCatalogo[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(TipoCatalogoService);
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

    it('should create a TipoCatalogo', () => {
      const tipoCatalogo = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(tipoCatalogo).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TipoCatalogo', () => {
      const tipoCatalogo = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(tipoCatalogo).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TipoCatalogo', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TipoCatalogo', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a TipoCatalogo', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addTipoCatalogoToCollectionIfMissing', () => {
      it('should add a TipoCatalogo to an empty array', () => {
        const tipoCatalogo: ITipoCatalogo = sampleWithRequiredData;
        expectedResult = service.addTipoCatalogoToCollectionIfMissing([], tipoCatalogo);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tipoCatalogo);
      });

      it('should not add a TipoCatalogo to an array that contains it', () => {
        const tipoCatalogo: ITipoCatalogo = sampleWithRequiredData;
        const tipoCatalogoCollection: ITipoCatalogo[] = [
          {
            ...tipoCatalogo,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addTipoCatalogoToCollectionIfMissing(tipoCatalogoCollection, tipoCatalogo);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TipoCatalogo to an array that doesn't contain it", () => {
        const tipoCatalogo: ITipoCatalogo = sampleWithRequiredData;
        const tipoCatalogoCollection: ITipoCatalogo[] = [sampleWithPartialData];
        expectedResult = service.addTipoCatalogoToCollectionIfMissing(tipoCatalogoCollection, tipoCatalogo);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tipoCatalogo);
      });

      it('should add only unique TipoCatalogo to an array', () => {
        const tipoCatalogoArray: ITipoCatalogo[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const tipoCatalogoCollection: ITipoCatalogo[] = [sampleWithRequiredData];
        expectedResult = service.addTipoCatalogoToCollectionIfMissing(tipoCatalogoCollection, ...tipoCatalogoArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const tipoCatalogo: ITipoCatalogo = sampleWithRequiredData;
        const tipoCatalogo2: ITipoCatalogo = sampleWithPartialData;
        expectedResult = service.addTipoCatalogoToCollectionIfMissing([], tipoCatalogo, tipoCatalogo2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tipoCatalogo);
        expect(expectedResult).toContain(tipoCatalogo2);
      });

      it('should accept null and undefined values', () => {
        const tipoCatalogo: ITipoCatalogo = sampleWithRequiredData;
        expectedResult = service.addTipoCatalogoToCollectionIfMissing([], null, tipoCatalogo, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tipoCatalogo);
      });

      it('should return initial array if no TipoCatalogo is added', () => {
        const tipoCatalogoCollection: ITipoCatalogo[] = [sampleWithRequiredData];
        expectedResult = service.addTipoCatalogoToCollectionIfMissing(tipoCatalogoCollection, undefined, null);
        expect(expectedResult).toEqual(tipoCatalogoCollection);
      });
    });

    describe('compareTipoCatalogo', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareTipoCatalogo(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 26545 };
        const entity2 = null;

        const compareResult1 = service.compareTipoCatalogo(entity1, entity2);
        const compareResult2 = service.compareTipoCatalogo(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 26545 };
        const entity2 = { id: 27572 };

        const compareResult1 = service.compareTipoCatalogo(entity1, entity2);
        const compareResult2 = service.compareTipoCatalogo(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 26545 };
        const entity2 = { id: 26545 };

        const compareResult1 = service.compareTipoCatalogo(entity1, entity2);
        const compareResult2 = service.compareTipoCatalogo(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
