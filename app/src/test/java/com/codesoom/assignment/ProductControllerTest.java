package com.codesoom.assignment;

import com.codesoom.assignment.controller.ProductController;
import com.codesoom.assignment.dto.ProductDTO;
import com.codesoom.assignment.model.Product;
import com.codesoom.assignment.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

@Nested
@DisplayName("ProductController 클래스는")
public class ProductControllerTest {
    private ProductService productService;
    private ProductController productController;

    @BeforeEach
    void setUp() {
        productService = mock(ProductService.class);
        productController = new ProductController(productService);

        sutUpFixture();
    }

    private void sutUpFixture() {
        ProductDTO.Response productOne = ProductDTO.Response.of(
                new Product("test name 1", 1000, "test imageUrl 1", "test maker 1"));
        ProductDTO.Response productTwo = ProductDTO.Response.of(
                new Product("test name 2", 2000, "test imageUrl 2", "test maker 2"));

        given(productService.createProduct(any(ProductDTO.CreateProduct.class))).will(invocation -> {
            ProductDTO.CreateProduct createResponse = invocation.getArgument(0);
            return ProductDTO.Response.of(new Product(createResponse.getName(), createResponse.getPrice(),
                    createResponse.getImageUrl(), createResponse.getMaker()));
        });
        given(productService.updateProduct(any(int.class), any(ProductDTO.UpdateProduct.class))).will(invocation -> {
            ProductDTO.UpdateProduct updateResponse = invocation.getArgument(1);
            return ProductDTO.Response.of(new Product().updateProduct(updateResponse.getName(),
                    updateResponse.getPrice(), updateResponse.getImageUrl(), updateResponse.getMaker()));
        });
        given(productService.getProducts()).willReturn(Arrays.asList(productOne, productTwo));
        given(productService.getProduct(1)).willReturn(productOne);
    }

    @Nested
    @DisplayName("GET /products URL 은")
    class getProductsTest {

        @Test
        @DisplayName("http status code 200 과 ProductDTO.Response List 를 반환한다")
        void getProductsTest() {
            List<ProductDTO.Response> responses = productController.getProducts();

            verify(productService).getProducts();

            assertThat(responses.size()).isEqualTo(2);
        }
    }

    @Nested
    @DisplayName("POST /products URL 은")
    class postMethodTest {
        @Test
        @DisplayName("http status code 201 과 ProductDTO.Response 를 반환한다")
        void postMethodTest() {
            ProductDTO.CreateProduct createProduct = new ProductDTO.CreateProduct("create test name 1",
                    "create test maker 1", 3000, "create test imageUrl 1");
            ResponseEntity<ProductDTO.Response> response = productController.createProduct(createProduct);

            verify(productService).createProduct(createProduct);

            assertThat(response.getStatusCode().value()).isEqualTo(201);
            assertThat(response.getBody().getName()).isEqualTo("create test name 1");
        }
    }


    @Nested
    @DisplayName("DELETE /products/{id}")
    class deleteMethodTest {
        @Test
        @DisplayName("http status code 204 를 반환한다")
        void deleteProductsTest() {
            ResponseEntity<?> response = productController.deleteProduct(1);

            verify(productService).deleteProduct(1);

            assertThat(response.getStatusCode().value()).isEqualTo(204);
        }
    }

    @Nested
    @DisplayName("GET /products/{id}")
    class getMethodTest {
        @Test
        @DisplayName("http status code 200 과 해당 ID의 ProductDTO.Response 를 반환한다")
        void deleteProductsTest() {
            ResponseEntity<ProductDTO.Response> response = productController.getProduct(1);

            verify(productService).getProduct(1);

            assertThat(response.getStatusCode().value()).isEqualTo(200);
            assertThat(response.getBody().getName()).isEqualTo("test name 1");
        }
    }

    @Nested
    @DisplayName("PUT /products/{id}")
    class putMethodTest {
        @Test
        @DisplayName("http status code 200 을 반환한다")
        void putProductsTest() {
            ProductDTO.UpdateProduct updateProduct = new ProductDTO.UpdateProduct("update test name 1",
                    "update test maker 1", 1000, "update test imageUrl 1");
            ResponseEntity<ProductDTO.Response> response = productController.putProduct(1, updateProduct);

            verify(productService).updateProduct(1, updateProduct);

            assertThat(response.getStatusCode().value()).isEqualTo(200);
            assertThat(response.getBody().getName()).isEqualTo(updateProduct.getName());
        }
    }

    @Nested
    @DisplayName("PATCH /products/{id}")
    class patchMethodTest {
        @Test
        @DisplayName("http status code 200 을 반환한다")
        void putProductsTest() {
            ProductDTO.UpdateProduct updateProduct = new ProductDTO.UpdateProduct("update test name 1",
                    "update test maker 1", 1000, "update test imageUrl 1");
            ResponseEntity<ProductDTO.Response> response = productController.patchProduct(1, updateProduct);

            verify(productService).updateProduct(1, updateProduct);

            assertThat(response.getStatusCode().value()).isEqualTo(200);
            assertThat(response.getBody().getName()).isEqualTo(updateProduct.getName());
        }
    }
}

