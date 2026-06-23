import { Component, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Producto, ProductoRequest, ProductosService, Categoria as CatSimple } from '../productos/productos.service';
import { Categoria, CategoriaRequest, CategoriasService } from '../categorias/categorias.service';
import { Orden, OrdenesService } from '../ordenes/ordenes.service';
import { UsuariosService, UserResponse } from '../core/usuarios/usuarios.service';

type SeccionAdmin = 'productos' | 'categorias' | 'ordenes' | 'usuarios';

@Component({
  selector: 'app-admin',
  imports: [FormsModule],
  templateUrl: './admin.html',
  styleUrl: './admin.scss',
})
export class AdminPage {
  seccionActiva = signal<SeccionAdmin>('productos');

  /* Productos */
  productos = signal<Producto[]>([]);
  categoriasSimples = signal<CatSimple[]>([]);
  loadingProductos = signal(false);
  errorProductos = signal('');
  exitoProductos = signal('');
  prodNombre = '';
  prodDescripcion = '';
  prodSku = '';
  prodPrecio: number | null = null;
  prodStock: number | null = null;
  prodIdCategoria: number | null = null;
  prodImagenFile: File | null = null;
  editandoProductoId: number | null = null;

  /* Categorias */
  categorias = signal<Categoria[]>([]);
  loadingCategorias = signal(false);
  errorCategorias = signal('');
  exitoCategorias = signal('');
  catNombre = '';
  catDescripcion = '';
  editandoCategoriaId: number | null = null;

  /* Ordenes */
  ordenes = signal<Orden[]>([]);
  loadingOrdenes = signal(false);
  errorOrdenes = signal('');

  /* Usuarios */
  usuarios = signal<UserResponse[]>([]);
  loadingUsuarios = signal(false);
  errorUsuarios = signal('');

  constructor(
    private productosService: ProductosService,
    private categoriasService: CategoriasService,
    private ordenesService: OrdenesService,
    private usuariosService: UsuariosService,
  ) {
    this.cargarProductos();
    this.cargarCategoriasSimples();
    this.cargarCategorias();
    this.cargarOrdenes();
    this.cargarUsuarios();
  }

  cambiarSeccion(seccion: SeccionAdmin) {
    this.seccionActiva.set(seccion);
  }

  /* ========== PRODUCTOS ========== */

  cargarProductos() {
    this.loadingProductos.set(true);
    this.errorProductos.set('');
    this.productosService.listar().subscribe({
      next: p => this.productos.set(p),
      error: () => {
        this.errorProductos.set('Error al cargar productos');
        this.loadingProductos.set(false);
      },
      complete: () => this.loadingProductos.set(false),
    });
  }

  cargarCategoriasSimples() {
    this.productosService.listarCategorias().subscribe({
      next: c => this.categoriasSimples.set(c),
    });
  }

  guardarProducto() {
    const prod = this.validarProducto();
    if (!prod) return;

    this.exitoProductos.set('');
    this.errorProductos.set('');

    if (this.editandoProductoId != null) {
      this.productosService.actualizar(this.editandoProductoId, prod).subscribe({
        next: (p) => this.manejarExitoGuardarProducto(p.id),
        error: () => this.errorProductos.set('No se pudo actualizar el producto'),
      });
    } else {
      this.productosService.crear(prod).subscribe({
        next: (p) => this.manejarExitoGuardarProducto(p.id),
        error: () => this.errorProductos.set('No se pudo crear el producto'),
      });
    }
  }

  private manejarExitoGuardarProducto(id: number) {
    if (this.prodImagenFile) {
      this.productosService.subirImagen(id, this.prodImagenFile).subscribe({
        next: () => {
          this.exitoProductos.set('Producto e imagen guardados correctamente');
          this.cancelarEdicionProducto();
          this.cargarProductos();
        },
        error: () => {
          this.errorProductos.set('Producto guardado, pero fallo la subida de imagen (quizas es muy grande)');
          this.cargarProductos();
        }
      });
    } else {
      this.exitoProductos.set('Producto guardado correctamente');
      this.cancelarEdicionProducto();
      this.cargarProductos();
    }
  }

  onImageSelected(event: Event) {
    const file = (event.target as HTMLInputElement).files?.[0];
    if (file) {
      if (file.size > 10 * 1024 * 1024) {
        this.errorProductos.set('La imagen no puede pesar mas de 10MB');
        this.prodImagenFile = null;
        (event.target as HTMLInputElement).value = '';
        return;
      }
      this.prodImagenFile = file;
    }
  }

  editarProducto(p: Producto) {
    this.editandoProductoId = p.id;
    this.prodNombre = p.nombre;
    this.prodDescripcion = p.descripcion;
    this.prodSku = p.sku ?? '';
    this.prodPrecio = p.precio ?? null;
    this.prodStock = p.stock ?? null;
    this.prodIdCategoria = p.idCategoria;
    this.prodImagenFile = null;
    this.exitoProductos.set('');
  }

  cancelarEdicionProducto() {
    this.editandoProductoId = null;
    this.limpiarFormProducto();
  }

  eliminarProducto(id: number) {
    if (!confirm('Estas seguro de eliminar este producto?')) return;

    this.productosService.eliminar(id).subscribe({
      next: () => {
        this.exitoProductos.set('Producto eliminado');
        this.cargarProductos();
      },
      error: () => this.errorProductos.set('No se pudo eliminar el producto'),
    });
  }

  nombreCategoriaProducto(idCat: number): string {
    return this.categoriasSimples().find(c => c.id === idCat)?.nombre ?? `${idCat}`;
  }

  private validarProducto(): ProductoRequest | null {
    const nombre = this.prodNombre.trim();
    const descripcion = this.prodDescripcion.trim();
    const sku = this.prodSku.trim();
    const idCategoria = Number(this.prodIdCategoria);
    const precio = Number(this.prodPrecio);
    const stock = Number(this.prodStock);

    if (!nombre || !sku || !idCategoria || !precio || stock == null || stock < 0) {
      this.errorProductos.set('Todos los campos son obligatorios');
      return null;
    }

    return { nombre, descripcion, idCategoria, sku, precio, stock, activo: true };
  }

  private limpiarFormProducto() {
    this.prodNombre = '';
    this.prodDescripcion = '';
    this.prodSku = '';
    this.prodPrecio = null;
    this.prodStock = null;
    this.prodIdCategoria = null;
    this.prodImagenFile = null;
  }

  /* ========== CATEGORIAS ========== */

  cargarCategorias() {
    this.loadingCategorias.set(true);
    this.errorCategorias.set('');
    this.categoriasService.listar().subscribe({
      next: c => this.categorias.set(c),
      error: () => {
        this.errorCategorias.set('Error al cargar categorias');
        this.loadingCategorias.set(false);
      },
      complete: () => this.loadingCategorias.set(false),
    });
  }

  guardarCategoria() {
    const nombre = this.catNombre.trim();
    const descripcion = this.catDescripcion.trim();

    if (!nombre || !descripcion) {
      this.errorCategorias.set('Nombre y descripcion son obligatorios');
      return;
    }

    const body: CategoriaRequest = { nombre, descripcion };
    this.exitoCategorias.set('');
    this.errorCategorias.set('');

    if (this.editandoCategoriaId != null) {
      this.categoriasService.actualizar(this.editandoCategoriaId, body).subscribe({
        next: () => {
          this.exitoCategorias.set('Categoria actualizada correctamente');
          this.cancelarEdicionCategoria();
          this.cargarCategorias();
          this.cargarCategoriasSimples();
        },
        error: () => this.errorCategorias.set('No se pudo actualizar la categoria'),
      });
    } else {
      this.categoriasService.crear(body).subscribe({
        next: () => {
          this.exitoCategorias.set('Categoria creada correctamente');
          this.limpiarFormCategoria();
          this.cargarCategorias();
          this.cargarCategoriasSimples();
        },
        error: () => this.errorCategorias.set('No se pudo crear la categoria'),
      });
    }
  }

  editarCategoria(c: Categoria) {
    this.editandoCategoriaId = c.id;
    this.catNombre = c.nombre;
    this.catDescripcion = c.descripcion;
    this.exitoCategorias.set('');
  }

  cancelarEdicionCategoria() {
    this.editandoCategoriaId = null;
    this.limpiarFormCategoria();
  }

  eliminarCategoria(id: number) {
    if (!confirm('Estas seguro de eliminar esta categoria?')) return;

    this.categoriasService.eliminar(id).subscribe({
      next: () => {
        this.exitoCategorias.set('Categoria eliminada');
        this.cargarCategorias();
        this.cargarCategoriasSimples();
      },
      error: () => this.errorCategorias.set('No se pudo eliminar la categoria'),
    });
  }

  private limpiarFormCategoria() {
    this.catNombre = '';
    this.catDescripcion = '';
  }

  /* ========== ORDENES ========== */

  cargarOrdenes() {
    this.loadingOrdenes.set(true);
    this.errorOrdenes.set('');
    this.ordenesService.listar().subscribe({
      next: o => this.ordenes.set(o),
      error: () => {
        this.errorOrdenes.set('Error al cargar ordenes');
        this.loadingOrdenes.set(false);
      },
      complete: () => this.loadingOrdenes.set(false),
    });
  }

  claseBadgeOrden(estado: string): string {
    const e = estado?.toLowerCase() ?? '';
    if (e.includes('confirm') || e.includes('complet')) return 'badge-success';
    if (e.includes('pendiente') || e.includes('proceso')) return 'badge-warning';
    if (e.includes('cancel')) return 'badge-danger';
    return 'badge-info';
  }

  formatearEstado(estado: string): string {
    return estado?.replace(/_/g, ' ') ?? '';
  }

  formatearPrecio(precio: number): string {
    return `S/ ${precio.toFixed(2)}`;
  }

  /* ========== USUARIOS ========== */
  cargarUsuarios() {
    this.loadingUsuarios.set(true);
    this.errorUsuarios.set('');
    this.usuariosService.listar().subscribe({
      next: u => this.usuarios.set(u),
      error: () => {
        this.errorUsuarios.set('Error al cargar usuarios');
        this.loadingUsuarios.set(false);
      },
      complete: () => this.loadingUsuarios.set(false),
    });
  }

  formatearFecha(fechaStr?: string): string {
    if (!fechaStr) return '';
    try {
      const date = new Date(fechaStr);
      return date.toLocaleDateString('es-ES', {
        year: 'numeric',
        month: 'short',
        day: 'numeric',
        hour: '2-digit',
        minute: '2-digit',
      });
    } catch {
      return fechaStr;
    }
  }
}
