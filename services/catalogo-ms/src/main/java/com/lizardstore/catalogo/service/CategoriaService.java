package com.lizardstore.catalogo.service;

import com.lizardstore.catalogo.dto.CategoriaRequest;
import com.lizardstore.catalogo.dto.CategoriaResponse;

import java.util.List;

public interface CategoriaService {

    CategoriaResponse create(CategoriaRequest request);

    List<CategoriaResponse> findAll();

    CategoriaResponse findById(Long id);

    CategoriaResponse update(Long id, CategoriaRequest request);

    void delete(Long id);
}
