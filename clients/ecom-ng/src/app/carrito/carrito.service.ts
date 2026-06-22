import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApiService } from '../core/services/api.service';

export interface CarritoItem {
  productoId: number;
  nombreProducto: string;
  precioUnitario: number;
  cantidad: number;
  subtotalLinea: number;
}

export interface Carrito {
  id: number;
  usuarioId: number;
  estado: string;
  subtotal: number;
  items: CarritoItem[];
}

export interface AgregarItemRequest {
  productoId: number;
  cantidad: number;
}

@Injectable({ providedIn: 'root' })
export class CarritoService {
  constructor(
    private http: HttpClient,
    private api: ApiService,
  ) {}

  obtener(usuarioId: number): Observable<Carrito> {
    return this.http.get<Carrito>(this.api.buildUrl(`/api/v1/carritos/usuario/${usuarioId}`));
  }

  agregarItem(usuarioId: number, body: AgregarItemRequest): Observable<Carrito> {
    return this.http.post<Carrito>(this.api.buildUrl(`/api/v1/carritos/usuario/${usuarioId}/items`), body);
  }

  actualizarCantidad(usuarioId: number, productoId: number, cantidad: number): Observable<Carrito> {
    return this.http.put<Carrito>(
      this.api.buildUrl(`/api/v1/carritos/usuario/${usuarioId}/items/${productoId}`),
      { cantidad },
    );
  }

  eliminarItem(usuarioId: number, productoId: number): Observable<Carrito> {
    return this.http.delete<Carrito>(
      this.api.buildUrl(`/api/v1/carritos/usuario/${usuarioId}/items/${productoId}`),
    );
  }

  vaciar(usuarioId: number): Observable<void> {
    return this.http.delete<void>(this.api.buildUrl(`/api/v1/carritos/usuario/${usuarioId}`));
  }

  checkout(usuarioId: number): Observable<Carrito> {
    return this.http.post<Carrito>(this.api.buildUrl(`/api/v1/carritos/usuario/${usuarioId}/checkout`), {});
  }
}
