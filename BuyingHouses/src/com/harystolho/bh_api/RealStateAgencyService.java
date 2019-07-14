package com.harystolho.bh_api;

import com.harystolho.bh.residence.Residence;
import com.harystolho.bh.residence.ResidenceRepository;
import com.harystolho.bh.user.User;
import com.harystolho.bh.user.UserRepository;

public class RealStateAgencyService {

	private UserRepository userRepository;
	private ResidenceRepository residenceRepository;

	public RealStateAgencyService(UserRepository userRepository, ResidenceRepository residenceRepository) {
		this.userRepository = userRepository;
		this.residenceRepository = residenceRepository;
	}

	public void buyResidenceForUser(String residenceId, int userId) {
		User user = userRepository.getById(userId);
		Residence residence = residenceRepository.getById(residenceId);

		user.buyResidence(residence);

		userRepository.save(user);
	}

}
