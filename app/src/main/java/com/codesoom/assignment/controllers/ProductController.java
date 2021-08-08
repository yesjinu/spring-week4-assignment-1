// REST API
// 1. GET /products
// 2. GET /products/{id}
// 3. POST /products
// 4. PUT/PATCH /products/{id}
// 5. DELETE /products/{id}

package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.ProductService;
import com.codesoom.assignment.domain.Product;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<Product> list(){
        return productService.getProducts();
    }
    @GetMapping("{id}")
    public Product detail(@PathVariable Long id){
        return productService.getProduct(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Product create(@RequestBody Product product){
        return productService.createProduct(product);
    }

    @PatchMapping("{id}")
    public Product update(@PathVariable Long id, @RequestBody Product product){
        return productService.updateProduct(id,product);
    }

    @DeleteMapping("{id}")
    public void destroy(@PathVariable Long id){
       productService.deleteProduct(id);
    }
}