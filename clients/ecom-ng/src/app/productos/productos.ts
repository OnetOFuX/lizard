import { Component, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Categoria, Producto, ProductosService } from './productos.service';
import { AuthService } from '../core/auth/auth.service';
import { CarritoService } from '../carrito/carrito.service';
import { environment } from '../../environments/environment';

@Component({
  selector: 'app-productos',
  imports: [FormsModule],
  templateUrl: './productos.html',
  styleUrl: './productos.scss',
})
export class ProductosPage implements OnInit {
  productos = signal<Producto[]>([]);
  productosFiltrados = signal<Producto[]>([]);
  categorias = signal<Categoria[]>([]);
  loading = signal(false);
  error = signal('');
  info = signal('');
  busqueda = '';
  categoriaSeleccionada: number | null = null;
  apiBaseUrl = environment.apiBaseUrl;

  // Categoria selector dropdown state
  busquedaCategoria = '';
  categoriasDropdownOpen = false;

  constructor(
    private productosService: ProductosService,
    private carritoService: CarritoService,
    private router: Router,
    private route: ActivatedRoute,
    protected auth: AuthService,
  ) {
    this.cargarCategorias();
    this.cargarProductos();
  }

  ngOnInit() {
    this.route.queryParamMap.subscribe(params => {
      const catParam = params.get('categoria');
      if (catParam) {
        const catId = Number(catParam);
        if (!Number.isNaN(catId)) {
          this.categoriaSeleccionada = catId;
        } else {
          this.categoriaSeleccionada = null;
        }
      } else {
        this.categoriaSeleccionada = null;
      }
      this.aplicarFiltros();
    });
  }

  cargarCategorias() {
    this.productosService.listarCategorias().subscribe({
      next: categories => this.categorias.set(categories),
    });
  }

  cargarProductos() {
    this.loading.set(true);
    this.error.set('');

    this.productosService.listar().subscribe({
      next: products => {
        this.productos.set(products);
        this.aplicarFiltros();
      },
      error: () => {
        this.error.set('No se pudieron cargar los productos');
        this.loading.set(false);
      },
      complete: () => this.loading.set(false),
    });
  }

  aplicarFiltros() {
    let resultado = this.productos();

    if (this.categoriaSeleccionada) {
      resultado = resultado.filter(p => p.idCategoria === this.categoriaSeleccionada);
    }

    if (this.busqueda.trim()) {
      const termino = this.busqueda.trim().toLowerCase();
      resultado = resultado.filter(p =>
        p.nombre.toLowerCase().includes(termino) ||
        p.descripcion?.toLowerCase().includes(termino) ||
        p.sku?.toLowerCase().includes(termino)
      );
    }

    this.productosFiltrados.set(resultado);
  }

  filtrarPorCategoria(id: number | null) {
    this.categoriaSeleccionada = id;
    this.aplicarFiltros();
  }

  // Dropdown methods
  toggleCategoriasDropdown() {
    this.categoriasDropdownOpen = !this.categoriasDropdownOpen;
  }

  cerrarCategoriasDropdown() {
    this.categoriasDropdownOpen = false;
  }

  seleccionarCategoria(id: number | null) {
    this.categoriaSeleccionada = id;
    this.categoriasDropdownOpen = false;
    this.busquedaCategoria = ''; // reset search in dropdown
    this.aplicarFiltros();
  }

  categoriasFiltradas(): Categoria[] {
    const term = this.busquedaCategoria.trim().toLowerCase();
    if (!term) return this.categorias();
    return this.categorias().filter(c => c.nombre.toLowerCase().includes(term));
  }

  nombreCategoriaSeleccionada(): string {
    if (this.categoriaSeleccionada === null) return 'Todas las categorias';
    return this.nombreCategoria(this.categoriaSeleccionada) || `Categoria #${this.categoriaSeleccionada}`;
  }

  agregarAlCarrito(producto: Producto) {
    if (!this.auth.isAuthenticated() || this.auth.userId() == null) {
      this.router.navigate(['/auth'], { queryParams: { returnUrl: '/productos' } });
      return;
    }

    this.info.set('');
    this.error.set('');
    this.carritoService.agregarItem(this.auth.userId()!, {
      productoId: producto.id,
      cantidad: 1,
    }).subscribe({
      next: () => this.info.set(`"${producto.nombre}" agregado al carrito`),
      error: () => this.error.set('No se pudo agregar al carrito'),
    });
  }

  nombreCategoria(idCategoria: number): string {
    return this.categorias().find(c => c.id === idCategoria)?.nombre ?? '';
  }

  formatearPrecio(precio?: number): string {
    if (precio == null) return 'S/ 0.00';
    return `S/ ${precio.toFixed(2)}`;
  }
}
