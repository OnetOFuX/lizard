import { Component, inject, signal, computed } from '@angular/core';
import { Router, RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { AuthService } from './core/auth/auth.service';
import { CarritoService } from './carrito/carrito.service';

@Component({
  selector: 'app-root',
  imports: [RouterLink, RouterLinkActive, RouterOutlet],
  templateUrl: './app.html',
  styleUrl: './app.scss',
})
export class App {
  protected readonly auth = inject(AuthService);
  private readonly router = inject(Router);
  private readonly carritoService = inject(CarritoService);

  menuAbierto = signal(false);
  cantidadCarrito = signal(0);

  protected readonly inicialUsuario = computed(() => {
    const nombre = this.auth.username();
    return nombre ? nombre.charAt(0).toUpperCase() : '';
  });

  constructor() {
    this.actualizarCarrito();
  }

  actualizarCarrito() {
    const userId = this.auth.userId();
    if (userId != null) {
      this.carritoService.obtener(userId).subscribe({
        next: c => {
          const total = c.items?.reduce((sum, item) => sum + item.cantidad, 0) ?? 0;
          this.cantidadCarrito.set(total);
        },
        error: () => this.cantidadCarrito.set(0),
      });
    }
  }

  toggleMenu() {
    this.menuAbierto.update(v => !v);
  }

  cerrarMenu() {
    this.menuAbierto.set(false);
  }

  logout() {
    this.auth.logout();
    this.cantidadCarrito.set(0);
    this.cerrarMenu();
    this.router.navigateByUrl('/auth');
  }
}
