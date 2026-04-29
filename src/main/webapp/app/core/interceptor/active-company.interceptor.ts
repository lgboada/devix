import { Injectable, inject } from '@angular/core';
import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Observable } from 'rxjs';

import { ActiveCompanyService } from 'app/core/auth/active-company.service';
import { ActiveCentroService } from 'app/core/auth/active-centro.service';
import { ActiveBodegaService } from 'app/core/auth/active-bodega.service';

@Injectable()
export class ActiveCompanyInterceptor implements HttpInterceptor {
  private readonly activeCompanyService = inject(ActiveCompanyService);
  private readonly activeCentroService = inject(ActiveCentroService);
  private readonly activeBodegaService = inject(ActiveBodegaService);

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const activeCompany = this.activeCompanyService.trackActiveCompany()();
    if (!activeCompany || !this.isApiRequest(request.url)) {
      return next.handle(request);
    }

    let requestToSend = request;

    if (!requestToSend.headers.has('X-Company-Id')) {
      requestToSend = requestToSend.clone({ setHeaders: { 'X-Company-Id': `${activeCompany.noCia}` } });
    }

    const activeCentro = this.activeCentroService.trackActiveCentro()();
    if (activeCentro && !requestToSend.headers.has('X-Centro-Id')) {
      requestToSend = requestToSend.clone({ setHeaders: { 'X-Centro-Id': `${activeCentro.centroId}` } });
    }

    const activeBodega = this.activeBodegaService.trackActiveBodega()();
    if (activeBodega && !requestToSend.headers.has('X-Bodega-Id')) {
      requestToSend = requestToSend.clone({ setHeaders: { 'X-Bodega-Id': `${activeBodega.bodegaId}` } });
    }

    if (this.shouldAttachNoCiaInBody(requestToSend)) {
      requestToSend = requestToSend.clone({
        body: {
          ...(requestToSend.body as Record<string, unknown>),
          noCia: activeCompany.noCia,
        },
      });
    }

    return next.handle(requestToSend);
  }

  private isApiRequest(url: string): boolean {
    // En este proyecto `ApplicationConfigService.getEndpointFor('api/...')` devuelve `api/...` (sin slash inicial).
    // También puede venir como URL absoluta. Cubrimos ambos casos.
    return /(^|\/)api\//.test(url);
  }

  private shouldAttachNoCiaInBody(request: HttpRequest<any>): boolean {
    if (!['POST', 'PUT', 'PATCH'].includes(request.method)) {
      return false;
    }
    if (!request.body || request.body instanceof FormData || typeof request.body !== 'object') {
      return false;
    }
    if (request.url.includes('/api/account') || request.url.includes('/api/authenticate')) {
      return false;
    }
    // `Compania` define los tenants; no se debe forzar noCia desde la sesión.
    if (request.url.includes('/api/companias')) {
      return false;
    }
    return true;
  }
}
