package com.magadiflo.cart.service;

import java.util.List;

import com.magadiflo.cart.entity.Item;
import com.magadiflo.cart.entity.Product;

public interface ICartService {

	List<Item> findAll();

	Item findById(Integer id, Integer quantity);

	Product save(Product product);

	void delete(Integer id);

}
