package com.harystolho.bh.user;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import com.harystolho.bh.AbstractEntity;
import com.harystolho.bh.Money;
import com.harystolho.bh.Money.InvalidMonetaryValueException;
import com.harystolho.bh.Money.NotEnoughMoneyException;
import com.harystolho.bh.residence.Residence;

public class User extends AbstractEntity<Integer> {

	private final int id;
	private String name;

	private Money money;
	private Set<Residence> properties;

	public User(int id, String name) {
		this.id = id;
		this.name = name;
		this.money = new Money(BigDecimal.ZERO);

		this.properties = new HashSet<>();
	}

	public void buyResidence(Residence residence) throws RuntimeException {
		try {
			reduceMoney(residence.getPrice());
		} catch (NotEnoughMoneyException e) {
			throw new RuntimeException("User doesn't have enough money to buy the residence");
		}

		properties.add(residence);
	}

	public void reduceMoney(Money reduce) throws NotEnoughMoneyException {
		money = money.subtract(reduce);
	}

}
