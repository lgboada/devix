import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { ITipoCliente } from '../tipo-cliente.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../tipo-cliente.test-samples';

import { TipoClienteService } from './tipo-cliente.service';

const requireRestSample: ITipoCliente = {
  ...sampleWithRequiredData,
};

describe('TipoCliente Service', () => {
  let service: TipoClienteService;
  let httpMock: HttpTestingController;
  let expectedResult: ITipoCliente | ITipoCliente[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(TipoClienteService);
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

    it('should create a TipoCliente', () => {
      const tipoCliente = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(tipoCliente).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TipoCliente', () => {
      const tipoCliente = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(tipoCliente).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TipoCliente', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TipoCliente', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a TipoCliente', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addTipoClienteToCollectionIfMissing', () => {
      it('should add a TipoCliente to an empty array', () => {
        const tipoCliente: ITipoCliente = sampleWithRequiredData;
        expectedResult = service.addTipoClienteToCollectionIfMissing([], tipoCliente);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tipoCliente);
      });

      it('should not add a TipoCliente to an array that contains it', () => {
        const tipoCliente: ITipoCliente = sampleWithRequiredData;
        const tipoClienteCollection: ITipoCliente[] = [
          {
            ...tipoCliente,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addTipoClienteToCollectionIfMissing(tipoClienteCollection, tipoCliente);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TipoCliente to an array that doesn't contain it", () => {
        const tipoCliente: ITipoCliente = sampleWithRequiredData;
        const tipoClienteCollection: ITipoCliente[] = [sampleWithPartialData];
        expectedResult = service.addTipoClienteToCollectionIfMissing(tipoClienteCollection, tipoCliente);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tipoCliente);
      });

      it('should add only unique TipoCliente to an array', () => {
        const tipoClienteArray: ITipoCliente[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const tipoClienteCollection: ITipoCliente[] = [sampleWithRequiredData];
        expectedResult = service.addTipoClienteToCollectionIfMissing(tipoClienteCollection, ...tipoClienteArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const tipoCliente: ITipoCliente = sampleWithRequiredData;
        const tipoCliente2: ITipoCliente = sampleWithPartialData;
        expectedResult = service.addTipoClienteToCollectionIfMissing([], tipoCliente, tipoCliente2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tipoCliente);
        expect(expectedResult).toContain(tipoCliente2);
      });

      it('should accept null and undefined values', () => {
        const tipoCliente: ITipoCliente = sampleWithRequiredData;
        expectedResult = service.addTipoClienteToCollectionIfMissing([], null, tipoCliente, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tipoCliente);
      });

      it('should return initial array if no TipoCliente is added', () => {
        const tipoClienteCollection: ITipoCliente[] = [sampleWithRequiredData];
        expectedResult = service.addTipoClienteToCollectionIfMissing(tipoClienteCollection, undefined, null);
        expect(expectedResult).toEqual(tipoClienteCollection);
      });
    });

    describe('compareTipoCliente', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareTipoCliente(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 28838 };
        const entity2 = null;

        const compareResult1 = service.compareTipoCliente(entity1, entity2);
        const compareResult2 = service.compareTipoCliente(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 28838 };
        const entity2 = { id: 25421 };

        const compareResult1 = service.compareTipoCliente(entity1, entity2);
        const compareResult2 = service.compareTipoCliente(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 28838 };
        const entity2 = { id: 28838 };

        const compareResult1 = service.compareTipoCliente(entity1, entity2);
        const compareResult2 = service.compareTipoCliente(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
