package com.magadiflo.cart.service;

import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.magadiflo.cart.entity.Item;
import com.magadiflo.cart.entity.Product;

@Service
public class CartServiceImpl implements ICartService {

	private final RestTemplate client;
	private final DiscoveryClient discoveryClient;
	private static final Logger LOGGER = LoggerFactory.getLogger(CartServiceImpl.class);

	public CartServiceImpl(RestTemplate client, DiscoveryClient discoveryClient) {
		this.client = client;
		this.discoveryClient = discoveryClient;
	}

	// Buscando en el servidor eureka el microservicio del que queremos su path
	private URI getUri(String serviceName) {
		List<ServiceInstance> instances = this.discoveryClient.getInstances(serviceName);
		if (instances != null && instances.size() > 0) {
			// Hasta el momento solo generamos una instancia del microservicio productos, 
			// ya que el puerto está definido de forma explícita. Por eso que de la lista
			// obtenemos la primera y única instancia que nos devuelve
			return instances.get(0).getUri();
		}
		return null;
	}

	private String urlProducts() {
		URI productsURI = this.getUri("microservice-products");
		String uriProducts = productsURI.toString();

		LOGGER.info("Product path from cart: {}", uriProducts);
		return uriProducts;
	}

	private Map<String, Object> pathVariable(Integer id) {
		Map<String, Object> pathVariable = new HashMap<>();
		pathVariable.put("id", id);
		return pathVariable;
	}

	@Override
	public List<Item> findAll() {
		ResponseEntity<Product[]> response;
		response = this.client.getForEntity(this.urlProducts().concat("/api/v1/products"), Product[].class);

		return Arrays.asList(response.getBody()).stream().map(product -> new Item(product, 1))
				.collect(Collectors.toList());
	}

	@Override
	public Item findById(Integer id, Integer quantity) {
		ResponseEntity<Product> response = this.client.getForEntity(this.urlProducts().concat("/api/v1/products/{id}"),
				Product.class, this.pathVariable(id));

		return new Item(response.getBody(), quantity);
	}

	@Override
	public Product save(Product product) {
		HttpEntity<Product> body = new HttpEntity<Product>(product);

		ResponseEntity<Product> response;
		response = this.client.exchange(this.urlProducts().concat("/api/v1/products"), HttpMethod.POST, body,
				Product.class);

		return response.getBody();
	}

	@Override
	public void delete(Integer id) {
		this.client.delete(this.urlProducts().concat("/api/v1/products/{id}"), this.pathVariable(id));
	}

}
