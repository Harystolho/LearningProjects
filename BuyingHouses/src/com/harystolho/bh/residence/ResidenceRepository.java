package com.harystolho.bh.residence;

import java.math.BigDecimal;

import com.harystolho.bh.AbstractRepository;
import com.harystolho.bh.Money;

public class ResidenceRepository extends AbstractRepository<String, Residence> {

	public ResidenceRepository() {
		save(new House("p1-b2", new Money(BigDecimal.valueOf(150000.00))));
		save(new Apartment("p1-b4", new Money(BigDecimal.valueOf(75000.00))));
	}

}
