import { Component, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { ProductosService, Producto } from '../productos/productos.service';
import { CategoriasService, Categoria } from '../categorias/categorias.service';
import { environment } from '../../environments/environment';

@Component({
  selector: 'app-home',
  imports: [RouterLink],
  templateUrl: './home.html',
  styleUrl: './home.scss',
})
export class HomePage {
  productosDestacados = signal<Producto[]>([]);
  categorias = signal<Categoria[]>([]);
  cargando = signal(true);
  apiBaseUrl = environment.apiBaseUrl;

  constructor(
    private productosService: ProductosService,
    private categoriasService: CategoriasService,
  ) {
    this.cargarDatos();
  }

  private cargarDatos() {
    this.productosService.listar().subscribe({
      next: productos => this.productosDestacados.set(productos.slice(0, 8)),
      complete: () => this.cargando.set(false),
      error: () => this.cargando.set(false),
    });

    this.categoriasService.listar().subscribe({
      next: categorias => this.categorias.set(categorias),
    });
  }

  formatearPrecio(precio?: number): string {
    if (precio == null) return 'S/ 0.00';
    return `S/ ${precio.toFixed(2)}`;
  }
}
