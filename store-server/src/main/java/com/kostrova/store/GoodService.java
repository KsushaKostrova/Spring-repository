package com.kostrova.store;

import java.util.List;

public interface GoodService {
	
	void addExistingGood(Good good);
	void addNewGood(Good good);
	void deleteGood(Integer goodId);
	Good getGood(Integer goodId);
	List<Good> getAllGoods();

}
