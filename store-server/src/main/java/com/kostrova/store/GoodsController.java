package com.kostrova.store;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GoodsController {

	@Autowired
	private GoodService goodService;

	@RequestMapping(method = RequestMethod.POST, path = "/good", consumes = {MediaType.APPLICATION_JSON_VALUE})
	public Good addNewGood(@RequestBody Good good) throws WrongPropertyValueException {
		if (good.getId() <= 0) {
			throw new WrongPropertyValueException("Impossible value of id");
		} else if (good.getQuantity() < 0) {
			throw new WrongPropertyValueException("Impossible value of quantity");
		} else if (good.getPrice() < 0) {
			throw new WrongPropertyValueException("Impossible value of price");
		}
		goodService.addNewGood(good);
		return good;
	}

	
	@RequestMapping(method = RequestMethod.PUT, path = "/good")
	public Good addGood(@RequestBody Good good) throws WrongPropertyValueException {
		if (good.getQuantity() < 0) {
			throw new WrongPropertyValueException("Impossible value of quantity");
		}
		goodService.addExistingGood(good);
		return good;
	}

	@RequestMapping(method = RequestMethod.DELETE, path = "/good/{goodId}")
	public void deleteGood(@PathVariable("goodId") Integer goodId) throws WrongPropertyValueException {
		if (goodId <= 0) {
			throw new WrongPropertyValueException("Impossible value of id");
		}
		goodService.deleteGood(goodId);
	}

	@Secured({"ROLE_MANAGER", "ROLE_EMPLOYEE"})
	@RequestMapping(method = RequestMethod.GET, path = "/good/{goodId}")
	public ResponseEntity<Good> getGoodInfo(@PathVariable("goodId") Integer goodId) throws WrongPropertyValueException {
		if (goodId <= 0) {
			throw new WrongPropertyValueException("Impossible value of id");
		}
		return new ResponseEntity<Good>(goodService.getGood(goodId), HttpStatus.OK);
	//	return goodService.getGood(goodId);
	}

	@RequestMapping(method = RequestMethod.GET, path = "/goods", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
	public ResponseEntity<List<Good>> getInfoAboutAllGoods() {
		return new ResponseEntity<List<Good>>(goodService.getAllGoods(), HttpStatus.OK);
	//	return goodService.getAllGoods();
	}


	public GoodService getGoodService() {
		return goodService;
	}


	public void setGoodService(GoodService goodService) {
		this.goodService = goodService;
	}

}
