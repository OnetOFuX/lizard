import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApiService } from '../core/services/api.service';

export interface OrdenItem {
  productoId: number;
  nombreProducto: string;
  precioUnitario: number;
  cantidad: number;
  subtotalLinea: number;
}

export interface Orden {
  id: number;
  usuarioId: number;
  carritoId?: number;
  total: number;
  estado: string;
  items?: OrdenItem[];
}

@Injectable({ providedIn: 'root' })
export class OrdenesService {
  constructor(
    private http: HttpClient,
    private api: ApiService,
  ) {}

  listar(): Observable<Orden[]> {
    return this.http.get<Orden[]>(this.api.buildUrl('/api/v1/ordenes'));
  }

  listarPorUsuario(usuarioId: number): Observable<Orden[]> {
    return this.http.get<Orden[]>(this.api.buildUrl(`/api/v1/ordenes/usuario/${usuarioId}`));
  }
}
