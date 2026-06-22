import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApiService } from '../core/services/api.service';

export interface PreferenciaPago {
  ordenId: number;
  preferenciaId: string;
  initPoint: string;
  estado: string;
  publicKey?: string;
  simulacion?: boolean;
}

export interface Pago {
  id: number;
  ordenId: number;
  monto: number;
  estado: string;
  preferenciaId?: string;
  initPoint?: string;
}

@Injectable({ providedIn: 'root' })
export class PagosService {
  constructor(
    private http: HttpClient,
    private api: ApiService,
  ) {}

  obtenerPreferencia(ordenId: number, monto?: number): Observable<PreferenciaPago> {
    let url = `/api/v1/pagos/orden/${ordenId}/preferencia`;
    if (monto != null) {
      url += `?monto=${monto}`;
    }
    return this.http.get<PreferenciaPago>(this.api.buildUrl(url));
  }

  simularAprobacion(ordenId: number): Observable<PreferenciaPago> {
    return this.http.post<PreferenciaPago>(
      this.api.buildUrl(`/api/v1/pagos/orden/${ordenId}/simular-aprobacion`),
      {},
    );
  }

  buscarPorOrden(ordenId: number): Observable<Pago> {
    return this.http.get<Pago>(this.api.buildUrl(`/api/v1/pagos/orden/${ordenId}`));
  }
}
