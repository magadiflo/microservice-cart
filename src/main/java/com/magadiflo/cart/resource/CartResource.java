package com.magadiflo.cart.resource;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
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

	private static final Logger LOGGER = LoggerFactory.getLogger(CartResource.class);

	private final ICartService cartService;
	private final CircuitBreakerFactory cbFactory;

	public CartResource(ICartService cartService, CircuitBreakerFactory cbFactory) {
		this.cartService = cartService;
		this.cbFactory = cbFactory;
	}

	@GetMapping
	public List<Item> toList() {
		return this.cartService.findAll();
	}

	@GetMapping(path = "/product/{id}/quantity/{quantity}")
	public Item showItem(@PathVariable Integer id, @PathVariable Integer quantity) {
		return this.cbFactory.create("cards") // cards, nombre que le damos al corto circuito
				.run(() -> this.cartService.findById(id, quantity), t -> this.alternativeMethod(id, quantity, t));
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

	private Item alternativeMethod(Integer id, Integer quantity, Throwable t) {
		LOGGER.info("Applying alternative method. The error produced is: {}", t.getMessage());
		Product product = new Product();
		product.setId(null);
		product.setName("Default product");
		product.setPrice(0D);

		Item item = new Item();
		item.setProduct(product);
		item.setQuantity(0);

		return item;
	}

}
