package com.tutofox.demo;

import com.tutofox.demo.controllers.ProductController;
import com.tutofox.demo.dtos.ProductRecordDto;
import com.tutofox.demo.models.ProductModel;
import com.tutofox.demo.repositories.ProductRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ProductControllerTest {

    @InjectMocks
    private ProductController productController;

    @Mock
    private ProductRepository productRepository;

    public ProductControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void test_SavarProduto_ComSucesso() {
        // Cria um DTO de produto de exemplo
        ProductRecordDto productDto = new ProductRecordDto("Produto Teste Melancia", new BigDecimal("99.99"));

        // Cria um modelo de produto de exemplo
        ProductModel productModel = new ProductModel();
        BeanUtils.copyProperties(productDto, productModel);
        productModel.setIdProduct(UUID.randomUUID());

        // Configura o comportamento do repositório simulado
        when(productRepository.save(any(ProductModel.class))).thenReturn(productModel);

        // Chama o método saveProduct do controlador
        ResponseEntity<String> response = productController.saveProduct(productDto);

        // Log de informações para visualizar o produto salvo
        assertEquals("Produto Teste Melancia", productModel.getName());
        assertEquals(new BigDecimal("99.99"), productModel.getValue());
        System.out.println(productModel.getName());
    }

    @Test
    public void Test_SavarProduto_SemPassarNome_BadRequest() {
        // Cria um DTO de produto de exemplo
        ProductRecordDto productDto = new ProductRecordDto("", new BigDecimal("99.99"));

        // Cria um modelo de produto de exemplo
        ProductModel productModel = new ProductModel();
        BeanUtils.copyProperties(productDto, productModel);
        productModel.setIdProduct(UUID.randomUUID());

        // Configura o comportamento do repositório simulado
        when(productRepository.save(any(ProductModel.class))).thenReturn(productModel);

        // Chama o método saveProduct do controlador
        ResponseEntity<String> response = productController.saveProduct(productDto);

        // Verifica se a resposta está correta
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        //assertEquals(productModel.getName(), response.getBody().getName());

        System.out.println("Nome ="+ productModel.getName());

    }

    @Test
    public void Test_SavarProduto_SemPassar_Valor_BadRequest() {
        // Cria um DTO de produto de exemplo
        ProductRecordDto productDto = new ProductRecordDto("Morango", new BigDecimal("null"));

        // Cria um modelo de produto de exemplo
        ProductModel productModel = new ProductModel();
        BeanUtils.copyProperties(productDto, productModel);
        productModel.setIdProduct(UUID.randomUUID());

        // Configura o comportamento do repositório simulado
        when(productRepository.save(any(ProductModel.class))).thenReturn(productModel);

        // Chama o método saveProduct do controlador
        ResponseEntity<String> response = productController.saveProduct(productDto);

        // Verifica se a resposta está correta
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        //assertEquals(productModel.getName(), response.getBody().getName());

        System.out.println("Valor ="+ productModel.getValue());

    }

    @Test
    public void testGetAllProducts() {
        // Cria uma lista de produtos de exemplo
        List<ProductModel> products = Arrays.asList(
                new ProductModel (UUID.randomUUID(), "Produto 1", new BigDecimal("49.99")),
                new ProductModel(UUID.randomUUID(), "Produto 2", new BigDecimal("79.99"))
        );

        // Configura o comportamento do repositório simulado
        when(productRepository.findAll()).thenReturn(products);

        // Chama o método getAllProducts do controlador
        ResponseEntity<List<ProductModel>> response = productController.getAllProducts();

        // Verifica se a resposta está correta
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(products.size(), response.getBody().size());

        System.out.println( response.getBody());
    }

    @Test
    public void testUpdateProduct() {
        // Cria um DTO de produto de exemplo
        ProductRecordDto productDto = new ProductRecordDto("Produto Atualizado", new BigDecimal("109.99"));

        // Cria um modelo de produto existente de exemplo
        ProductModel existingProduct = new ProductModel();
        existingProduct.setIdProduct(UUID.randomUUID());
        existingProduct.setName("Produto Original");
        existingProduct.setValue(new BigDecimal("99.99"));

        // Configura o comportamento do repositório simulado
        when(productRepository.findById(existingProduct.getIdProduct())).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(any(ProductModel.class))).thenReturn(existingProduct);

        // Chama o método updateProduct do controlador
        ResponseEntity<ProductModel> response = productController.updateProduct(existingProduct.getIdProduct(), productDto);

        // Verifica se a resposta está correta
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(productDto.name(), response.getBody().getName());
        assertEquals(productDto.value(), response.getBody().getValue());
    }
}

