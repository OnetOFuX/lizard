import { Component, signal, computed } from '@angular/core';
import { RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { Categoria, CategoriasService } from './categorias.service';

@Component({
  selector: 'app-categorias',
  imports: [RouterLink, FormsModule],
  templateUrl: './categorias.html',
  styleUrl: './categorias.scss',
})
export class CategoriasPage {
  categorias = signal<Categoria[]>([]);
  loading = signal(false);
  error = signal('');
  busqueda = signal('');

  categoriasFiltradas = computed(() => {
    const list = this.categorias();
    const query = this.busqueda().trim().toLowerCase();
    if (!query) {
      return list;
    }
    return list.filter(cat => 
      cat.nombre.toLowerCase().includes(query) || 
      cat.descripcion?.toLowerCase().includes(query)
    );
  });

  constructor(private categoriasService: CategoriasService) {
    this.cargarCategorias();
  }

  cargarCategorias() {
    this.loading.set(true);
    this.error.set('');

    this.categoriasService.listar().subscribe({
      next: categories => this.categorias.set(categories),
      error: () => {
        this.error.set('No se pudieron cargar las categorias');
        this.loading.set(false);
      },
      complete: () => this.loading.set(false),
    });
  }
}
