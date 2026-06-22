import { Component, signal } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { Carrito, CarritoService } from './carrito.service';
import { AuthService } from '../core/auth/auth.service';
import { environment } from '../../environments/environment';
import { ProductosService, Producto } from '../productos/productos.service';

@Component({
  selector: 'app-carrito',
  imports: [RouterLink],
  templateUrl: './carrito.html',
  styleUrl: './carrito.scss',
})
export class CarritoPage {
  carrito = signal<Carrito | null>(null);
  productos = signal<Producto[]>([]);
  loading = signal(false);
  error = signal('');
  checkoutOk = signal('');
  apiBaseUrl = environment.apiBaseUrl;

  constructor(
    private carritoService: CarritoService,
    private productosService: ProductosService,
    private auth: AuthService,
    private router: Router,
  ) {
    this.cargar();
  }

  cargar() {
    const userId = this.auth.userId();
    if (userId == null) {
      this.router.navigate(['/auth'], { queryParams: { returnUrl: '/carrito' } });
      return;
    }

    this.loading.set(true);
    this.error.set('');
    
    this.productosService.listar().subscribe({
      next: prods => this.productos.set(prods),
    });

    this.carritoService.obtener(userId).subscribe({
      next: c => this.carrito.set(c),
      error: () => {
        this.error.set('No se pudo cargar el carrito');
        this.loading.set(false);
      },
      complete: () => this.loading.set(false),
    });
  }

  tieneImagen(productoId: number): boolean {
    const prod = this.productos().find(p => p.id === productoId);
    return prod?.hasImagen ?? false;
  }


  cambiarCantidad(productoId: number, cantidad: number) {
    const userId = this.auth.userId();
    if (userId == null || cantidad < 1) return;

    this.carritoService.actualizarCantidad(userId, productoId, cantidad).subscribe({
      next: c => this.carrito.set(c),
      error: () => this.error.set('No se pudo actualizar la cantidad'),
    });
  }

  quitar(productoId: number) {
    const userId = this.auth.userId();
    if (userId == null) return;

    this.carritoService.eliminarItem(userId, productoId).subscribe({
      next: c => this.carrito.set(c),
      error: () => this.error.set('No se pudo eliminar el producto'),
    });
  }

  vaciar() {
    const userId = this.auth.userId();
    if (userId == null) return;

    this.carritoService.vaciar(userId).subscribe({
      next: () => {
        this.carrito.set(null);
        this.cargar();
      },
      error: () => this.error.set('No se pudo vaciar el carrito'),
    });
  }

  confirmarCompra() {
    const userId = this.auth.userId();
    if (userId == null) return;

    this.checkoutOk.set('');
    this.error.set('');
    this.carritoService.checkout(userId).subscribe({
      next: () => {
        this.checkoutOk.set('Orden creada. Te redirigimos para completar el pago.');
        this.cargar();
        setTimeout(() => this.router.navigate(['/ordenes']), 1500);
      },
      error: () => this.error.set('No se pudo confirmar la compra'),
    });
  }

  formatearPrecio(precio: number): string {
    return `S/ ${precio.toFixed(2)}`;
  }
}
