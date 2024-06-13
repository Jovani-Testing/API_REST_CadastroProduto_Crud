package com.tutofox.demo.controllers;

import com.tutofox.demo.dtos.ProductRecordDto;
import com.tutofox.demo.models.ProductModel;
import com.tutofox.demo.repositories.ProductRepository;
import io.micrometer.common.util.StringUtils;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
public class ProductController {

    @Autowired
    ProductRepository productRepository;

    @PostMapping("/products")
    public ResponseEntity<String> saveProduct(@RequestBody @Valid ProductRecordDto productRecordDto) {
        if (StringUtils.isBlank(productRecordDto.name())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro: O nome do produto não pode estar em branco");
        }

        var productModel = new ProductModel();
        BeanUtils.copyProperties(productRecordDto, productModel);
        productRepository.save(productModel);
        return ResponseEntity.status(HttpStatus.CREATED).body("Produto salvo com sucesso");
    }


    @GetMapping("/products")
    public ResponseEntity<List<ProductModel>> getAllProducts() {
        List<ProductModel> products = productRepository.findAll();
        return ResponseEntity.ok(products);
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<ProductModel> updateProduct(@PathVariable UUID id, @RequestBody @Valid ProductRecordDto productRecordDto) {
        // Busca o produto pelo ID fornecido na URL
        Optional<ProductModel> productOptional = productRepository.findById(id);

        // Verifica se o produto existe
        if (!productOptional.isPresent()) {
            // Retorna 404 se o produto não for encontrado
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        // Obtém o produto existente
        var productModel = productOptional.get();

        // Copia as propriedades do DTO para o modelo do produto
        BeanUtils.copyProperties(productRecordDto, productModel);

        // Salva o produto atualizado no repositório e retorna 200 OK
        return ResponseEntity.status(HttpStatus.OK).body(productRepository.save(productModel));
    }
}


