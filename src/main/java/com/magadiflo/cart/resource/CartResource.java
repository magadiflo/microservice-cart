package com.magadiflo.cart.resource;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.magadiflo.cart.entity.Item;
import com.magadiflo.cart.entity.Product;
import com.magadiflo.cart.service.ICartService;

@RestController
@RequestMapping(path = "/carts")
public class CartResource {

	private final ICartService cartService;

	public CartResource(ICartService cartService) {
		this.cartService = cartService;
	}

	@GetMapping
	public List<Item> toList() {
		return this.cartService.findAll();
	}

	@GetMapping(path = "/product/{id}/quantity/{quantity}")
	public Item showItem(@PathVariable Integer id, @PathVariable Integer quantity) {
		return this.cartService.findById(id, quantity);
	}

	@PostMapping(path = "/product")
	@ResponseStatus(code = HttpStatus.CREATED)
	public Product saveProduct(@RequestBody Product product) {
		return this.cartService.save(product);
	}

	@DeleteMapping(path = "/product/{id}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void deleteProduct(@PathVariable Integer id) {
		this.cartService.delete(id);
	}

}
