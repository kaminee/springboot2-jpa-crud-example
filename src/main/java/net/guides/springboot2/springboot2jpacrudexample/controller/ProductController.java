package net.guides.springboot2.springboot2jpacrudexample.controller;

import java.util.HashMap;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.guides.springboot2.springboot2jpacrudexample.exception.ResourceNotFoundException;
import net.guides.springboot2.springboot2jpacrudexample.model.Product;
import net.guides.springboot2.springboot2jpacrudexample.repository.ProductRepository;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/v1")
public class ProductController {
	@Autowired
	private ProductRepository productRepository;

	@GetMapping("/products")
	public List<Product> getAllproducts() {
		return productRepository.findAll();
	}

	@GetMapping("/products/{id}")
	public ResponseEntity<Product> getProductById(@PathVariable(value = "id") Long ProductId)
			throws ResourceNotFoundException {
		Product Product = productRepository.findById(ProductId)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found for this id :: " + ProductId));
		return ResponseEntity.ok().body(Product);
	}

	@PostMapping("/products")
	public Product createProduct(@Valid @RequestBody Product Product) {
		return productRepository.save(Product);
	}

	@PutMapping("/products/{id}")
	public ResponseEntity<Product> updateProduct(@PathVariable(value = "id") Long ProductId,
			@Valid @RequestBody Product ProductDetails) throws ResourceNotFoundException {
		Product Product = productRepository.findById(ProductId)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found for this id :: " + ProductId));

		Product.setProductName(ProductDetails.getProductName());
		Product.setProductType(ProductDetails.getProductType());
		Product.setDescription(ProductDetails.getDescription());
		final Product updatedProduct = productRepository.save(Product);
		return ResponseEntity.ok(updatedProduct);
	}

	@DeleteMapping("/products/{id}")
	public Map<String, Boolean> deleteProduct(@PathVariable(value = "id") Long ProductId)
			throws ResourceNotFoundException {
		Product Product = productRepository.findById(ProductId)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found for this id :: " + ProductId));

		productRepository.delete(Product);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}
}
