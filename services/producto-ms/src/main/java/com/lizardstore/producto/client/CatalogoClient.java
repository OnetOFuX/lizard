package com.lizardstore.producto.client;

import com.lizardstore.producto.dto.CategoriaDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "catalogo-ms")
public interface CatalogoClient {

    @GetMapping("/api/v1/categorias/{id}")
    CategoriaDto findCategoriaById(@PathVariable("id") Long id);
}