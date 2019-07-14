package com.harystolho.bh.residence;

import com.harystolho.bh.AbstractEntity;
import com.harystolho.bh.Money;

public abstract class Residence extends AbstractEntity<String> {

	private String id;
	private final Money price;

	public Residence(String id, Money price) {
		this.price = price;
	}

	public Money getPrice() {
		return price;
	}

}
