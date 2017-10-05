package com.kostrova.store;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GoodsController {

	@Autowired
	private GoodService goodService;

	@RequestMapping(method = RequestMethod.POST, path = "/good", consumes = { MediaType.APPLICATION_JSON_VALUE })
	public Good addNewGood(@RequestBody Good good) throws WrongPropertyValueException {
		if (good.getId() <= 0 || good.getQuantity() < 0 || good.getPrice() < 0) {
			throw new IllegalArgumentException();
		}
		goodService.addNewGood(good);
		return good;
	}

	@RequestMapping(method = RequestMethod.PUT, path = "/good")
	public Good addGood(@RequestBody Good good) throws WrongPropertyValueException {
		if (good.getId() <= 0 || good.getQuantity() < 0 || good.getPrice() < 0) {
			throw new IllegalArgumentException();
		}
		goodService.addExistingGood(good);
		return good;
	}

	@RequestMapping(method = RequestMethod.DELETE, path = "/good/{goodId}")
	public void deleteGood(@PathVariable("goodId") Integer goodId) throws WrongPropertyValueException {
		if (goodId <= 0) {
			throw new IllegalArgumentException();
		}
		goodService.deleteGood(goodId);
	}

	@RequestMapping(method = RequestMethod.GET, path = "/good/{goodId}")
	public ResponseEntity<Good> getGoodInfo(@PathVariable("goodId") Integer goodId) throws NoSuchElementException, WrongPropertyValueException {
		if (goodId <= 0) {
			throw new IllegalArgumentException();
		}
		Good good = goodService.getGood(goodId);
		if (good != null) {
			return new ResponseEntity<Good>(good, HttpStatus.OK);
		} else {
			throw new NoSuchElementException();
		}
	}

	@RequestMapping(method = RequestMethod.GET, path = "/goods", produces = { MediaType.APPLICATION_JSON_UTF8_VALUE })
	public ResponseEntity<List<Good>> getInfoAboutAllGoods() {
		List<Good> goods = goodService.getAllGoods();
		return new ResponseEntity<List<Good>>(goods, HttpStatus.OK);
	}

	public GoodService getGoodService() {
		return goodService;
	}

	public void setGoodService(GoodService goodService) {
		this.goodService = goodService;
	}

}
