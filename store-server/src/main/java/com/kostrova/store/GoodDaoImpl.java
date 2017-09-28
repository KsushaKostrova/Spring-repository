package com.kostrova.store;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository
public class GoodDaoImpl implements IGoodDao{
	Map<Integer, Good> goods = new HashMap<>();

	@Override
	public void addExistingGood(Good good) {
		goods.put(good.getId(), good);
	}

	@Override
	public void addNewGood(Good good) {
		goods.put(good.getId(), good);
	}

	@Override
	public void deleteGood(Integer goodId) {
		goods.remove(goodId);
	}

	@Override
	public Good getGood(Integer goodId) {
		return goods.get(goodId);
	}

	@Override
	public List<Good> getAllGoods() {
		return new ArrayList<>(goods.values());
	}
	
}
