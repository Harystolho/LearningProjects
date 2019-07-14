package com.harystolho.bh.user;

import com.harystolho.bh.AbstractRepository;

public class UserRepository extends AbstractRepository<Integer, User> {

	public UserRepository() {
		save(new User(1, "Jackson"));
		save(new User(2, "Marillu"));
		save(new User(3, "Opmiller"));
	}

}
