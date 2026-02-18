import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { ITipoProducto } from '../tipo-producto.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../tipo-producto.test-samples';

import { TipoProductoService } from './tipo-producto.service';

const requireRestSample: ITipoProducto = {
  ...sampleWithRequiredData,
};

describe('TipoProducto Service', () => {
  let service: TipoProductoService;
  let httpMock: HttpTestingController;
  let expectedResult: ITipoProducto | ITipoProducto[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(TipoProductoService);
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

    it('should create a TipoProducto', () => {
      const tipoProducto = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(tipoProducto).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TipoProducto', () => {
      const tipoProducto = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(tipoProducto).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TipoProducto', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TipoProducto', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a TipoProducto', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addTipoProductoToCollectionIfMissing', () => {
      it('should add a TipoProducto to an empty array', () => {
        const tipoProducto: ITipoProducto = sampleWithRequiredData;
        expectedResult = service.addTipoProductoToCollectionIfMissing([], tipoProducto);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tipoProducto);
      });

      it('should not add a TipoProducto to an array that contains it', () => {
        const tipoProducto: ITipoProducto = sampleWithRequiredData;
        const tipoProductoCollection: ITipoProducto[] = [
          {
            ...tipoProducto,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addTipoProductoToCollectionIfMissing(tipoProductoCollection, tipoProducto);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TipoProducto to an array that doesn't contain it", () => {
        const tipoProducto: ITipoProducto = sampleWithRequiredData;
        const tipoProductoCollection: ITipoProducto[] = [sampleWithPartialData];
        expectedResult = service.addTipoProductoToCollectionIfMissing(tipoProductoCollection, tipoProducto);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tipoProducto);
      });

      it('should add only unique TipoProducto to an array', () => {
        const tipoProductoArray: ITipoProducto[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const tipoProductoCollection: ITipoProducto[] = [sampleWithRequiredData];
        expectedResult = service.addTipoProductoToCollectionIfMissing(tipoProductoCollection, ...tipoProductoArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const tipoProducto: ITipoProducto = sampleWithRequiredData;
        const tipoProducto2: ITipoProducto = sampleWithPartialData;
        expectedResult = service.addTipoProductoToCollectionIfMissing([], tipoProducto, tipoProducto2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tipoProducto);
        expect(expectedResult).toContain(tipoProducto2);
      });

      it('should accept null and undefined values', () => {
        const tipoProducto: ITipoProducto = sampleWithRequiredData;
        expectedResult = service.addTipoProductoToCollectionIfMissing([], null, tipoProducto, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tipoProducto);
      });

      it('should return initial array if no TipoProducto is added', () => {
        const tipoProductoCollection: ITipoProducto[] = [sampleWithRequiredData];
        expectedResult = service.addTipoProductoToCollectionIfMissing(tipoProductoCollection, undefined, null);
        expect(expectedResult).toEqual(tipoProductoCollection);
      });
    });

    describe('compareTipoProducto', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareTipoProducto(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 6329 };
        const entity2 = null;

        const compareResult1 = service.compareTipoProducto(entity1, entity2);
        const compareResult2 = service.compareTipoProducto(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 6329 };
        const entity2 = { id: 23322 };

        const compareResult1 = service.compareTipoProducto(entity1, entity2);
        const compareResult2 = service.compareTipoProducto(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 6329 };
        const entity2 = { id: 6329 };

        const compareResult1 = service.compareTipoProducto(entity1, entity2);
        const compareResult2 = service.compareTipoProducto(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
