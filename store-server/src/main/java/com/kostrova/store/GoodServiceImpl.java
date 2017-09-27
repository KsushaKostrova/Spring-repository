package com.kostrova.store;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GoodServiceImpl implements GoodService {

	@Autowired
	private IGoodDao goodRepository;

	@Override
	public void addExistingGood(Good good) {
		goodRepository.addExistingGood(good);
	}

	@Override
	public void addNewGood(Good good) {
		goodRepository.addNewGood(good);
	}

	@Override
	public void deleteGood(Integer goodId) {
		goodRepository.deleteGood(goodId);
	}

	@Override
	public Good getGood(Integer goodId) {
		return goodRepository.getGood(goodId);
	}

	@Override
	public List<Good> getAllGoods() {
		return goodRepository.getAllGoods();
	}
}
