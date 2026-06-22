package com.lizardstore.catalogo.service.impl;

import com.lizardstore.catalogo.dto.CategoriaRequest;
import com.lizardstore.catalogo.dto.CategoriaResponse;
import com.lizardstore.catalogo.entity.Categoria;
import com.lizardstore.catalogo.exception.ResourceNotFoundException;
import com.lizardstore.catalogo.mapper.CategoriaMapper;
import com.lizardstore.catalogo.repository.CategoriaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoriaServiceImplTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    @Spy
    private CategoriaMapper categoriaMapper = new CategoriaMapper();

    @InjectMocks
    private CategoriaServiceImpl categoriaService;

    @Test
    void shouldCreateCategoria() {
        CategoriaRequest request = CategoriaRequest.builder()
                .nombre("Tecnologia")
                .descripcion("Productos tecnologicos")
                .build();
        Categoria savedEntity = Categoria.builder()
                .id(1L)
                .nombre("Tecnologia")
                .descripcion("Productos tecnologicos")
                .build();

        when(categoriaRepository.save(any(Categoria.class))).thenReturn(savedEntity);

        CategoriaResponse response = categoriaService.create(request);

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getNombre()).isEqualTo("Tecnologia");
    }

    @Test
    void shouldThrowWhenCategoriaNotFound() {
        when(categoriaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> categoriaService.findById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }
}
