package com.magadiflo.cart.entity;

public class Item {

	private Product product;
	private Integer quantity;

	public Item() {
	}

	public Item(Product product, Integer quantity) {
		this.product = product;
		this.quantity = quantity;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Double getTotal() {
		return this.quantity.doubleValue() * this.product.getPrice();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Item from cart [product=");
		builder.append(product);
		builder.append(", quantity=");
		builder.append(quantity);
		builder.append(", total()=");
		builder.append(this.getTotal());
		builder.append("]");
		return builder.toString();
	}
}
