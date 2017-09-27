package com.kostrova.store;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GoodsController {

	@Autowired
	private GoodService goodService;

	@RequestMapping(method = RequestMethod.POST, path = "/good")
	public void addNewGood(@RequestBody Good good) throws WrongPropertyValueException {
		if (good.getId() <= 0) {
			throw new WrongPropertyValueException("Impossible value of id");
		} else if (good.getQuantity() < 0) {
			throw new WrongPropertyValueException("Impossible value of quantity");
		} else if (good.getPrice() < 0) {
			throw new WrongPropertyValueException("Impossible value of price");
		}
		goodService.addNewGood(good);
	}

	@RequestMapping(method = RequestMethod.PUT, path = "/good")
	public void addGood(@RequestBody Good good) throws WrongPropertyValueException {
		if (good.getQuantity() < 0) {
			throw new WrongPropertyValueException("Impossible value of quantity");
		}
		goodService.addExistingGood(good);
	}

	@RequestMapping(method = RequestMethod.DELETE, path = "/good/{goodId}")
	public void deleteGood(@RequestParam("goodId") Integer goodId) throws WrongPropertyValueException {
		if (goodId <= 0) {
			throw new WrongPropertyValueException("Impossible value of id");
		}
		goodService.deleteGood(goodId);
	}

	@RequestMapping(method = RequestMethod.GET, path = "/good/{goodId}")
	public Good getGoodInfo(@RequestParam("goodId") Integer goodId) throws WrongPropertyValueException {
		if (goodId <= 0) {
			throw new WrongPropertyValueException("Impossible value of id");
		}
		return goodService.getGood(goodId);
	}

	@RequestMapping(method = RequestMethod.GET, path = "/goods")
	public List<Good> getInfoAboutAllGoods() {
		return goodService.getAllGoods();
	}

}
