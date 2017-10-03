package com.kostrova.store;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class TestGoodsController {
	
	@Mock
	GoodService goodService;
	
	GoodsController goodsController;
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		goodsController = new GoodsController();
		goodsController.setGoodService(goodService);
	}

	@Test(expected=WrongPropertyValueException.class)
	public void testAddNewGood_wrongIdThrowsException() throws WrongPropertyValueException {
		Good good = new Good();
		good.setId(-1);
		good.setName("pen");
		good.setPrice(15.0);
		good.setQuantity(1);
		when(goodsController.addNewGood(good)).thenThrow(new WrongPropertyValueException("Impossible value of id"));
	}
	
	@Test(expected=WrongPropertyValueException.class)
	public void testAddNewGood_wrongQuantityThrowsException() throws WrongPropertyValueException {
		Good good = new Good();
		good.setId(1);
		good.setName("pen");
		good.setPrice(15.0);
		good.setQuantity(-1);
		when(goodsController.addNewGood(good)).thenThrow(new WrongPropertyValueException("Impossible value of quantity"));
	}
	
	@Test(expected=WrongPropertyValueException.class)
	public void testAddNewGood_wrongPriceThrowsException() throws WrongPropertyValueException {		
		Good good = new Good();		
		good.setId(1);
		good.setName("pen");
		good.setPrice(-1.0);
		good.setQuantity(1);
		when(goodsController.addNewGood(good)).thenThrow(new WrongPropertyValueException("Impossible value of price"));
	}
	
	@Test(expected=WrongPropertyValueException.class)
	public void testAddGood_wrongIdThrowsException() throws WrongPropertyValueException {
		Good good = new Good();
		good.setId(-1);
		good.setName("pen");
		good.setPrice(15.0);
		good.setQuantity(1);
		when(goodsController.addGood(good)).thenThrow(new WrongPropertyValueException("Impossible value of id"));
	}
	
	@Test(expected=WrongPropertyValueException.class)
	public void testAddGood_wrongPriceThrowsException() throws WrongPropertyValueException {		
		Good good = new Good();		
		good.setId(1);
		good.setName("pen");
		good.setPrice(-1.0);
		good.setQuantity(1);
		when(goodsController.addGood(good)).thenThrow(new WrongPropertyValueException("Impossible value of price"));
	}
	
	@Test(expected=WrongPropertyValueException.class)
	public void testAddGood_wrongQuantityThrowsException() throws WrongPropertyValueException {
		Good good = new Good();
		good.setId(1);
		good.setName("pen");
		good.setPrice(15.0);
		good.setQuantity(-1);
		when(goodsController.addGood(good)).thenThrow(new WrongPropertyValueException("Impossible value of quantity"));
	}
	
	@Test(expected=WrongPropertyValueException.class)
	public void testDeleteGood_wrongIdThrowsException() throws WrongPropertyValueException {
		Good good = new Good();
		good.setId(-1);
		goodsController.deleteGood(good.getId());
	}
	
	@Test(expected=WrongPropertyValueException.class)
	public void testgetGoodInfo_wrongIdThrowsException() throws WrongPropertyValueException {
		Good good = new Good();
		good.setId(-1);
		goodsController.getGoodInfo(good.getId());
	}
	
	@Test
	public void testAddNewGood() throws WrongPropertyValueException {
		Good expected = new Good();
		expected.setId(1);
		expected.setName("pen");
		expected.setPrice(15.0);
		expected.setQuantity(1);
		goodsController.addNewGood(expected);
		verify(goodService, times(1)).addNewGood(eq(expected));
	}
	
	@Test
	public void testAddGood() throws WrongPropertyValueException {
		Good expected = new Good();
		expected.setId(1);
		expected.setName("pen");
		expected.setPrice(15.0);
		expected.setQuantity(1);
		goodsController.addGood(expected);
		verify(goodService, times(1)).addExistingGood(eq(expected));
	}
	
	@Test
	public void testDeleteGood() throws WrongPropertyValueException {
		Good expected = new Good();
		expected.setId(1);
		goodsController.deleteGood(expected.getId());
		verify(goodService, times(1)).deleteGood(eq(expected.getId()));
	}
	
	@Test
	public void testGetGoodInfo() throws WrongPropertyValueException {
		Good good = new Good();
		good.setId(1);
		Good actual = goodService.getGood(good.getId());
		Good expected = goodsController.getGoodInfo(good.getId()).getBody();
		when(goodService.getGood(good.getId())).thenReturn(actual);
		assertEquals(expected, actual);
	}
	
	@Test
	public void testGetInfoAboutAllGoods() {
		Good good = new Good();
		good.setId(1);
		List<Good> actual = goodService.getAllGoods();
		List<Good> expected = goodsController.getInfoAboutAllGoods().getBody();
		when(goodService.getAllGoods()).thenReturn(actual);
		assertEquals(expected, actual);
	}
}
