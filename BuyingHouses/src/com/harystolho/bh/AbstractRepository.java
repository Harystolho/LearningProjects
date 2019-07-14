package com.harystolho.bh;

import java.util.HashMap;
import java.util.Map;

public class AbstractRepository<K, T extends AbstractEntity<K>> {

	private Map<K, T> entities;

	public AbstractRepository() {
		this.entities = new HashMap<>();
	}

	public T getById(K k) {
		return entities.get(k);
	}

	public void save(T t) {
		entities.put(t.getId(), t);
	}

}
