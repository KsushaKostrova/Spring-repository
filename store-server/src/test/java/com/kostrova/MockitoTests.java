package com.kostrova;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.junit.Assert.*;

import org.junit.Test;

import com.kostrova.store.Good;
import com.kostrova.store.GoodsController;
import com.kostrova.store.WrongPropertyValueException;

public class MockitoTests {
	
	GoodsController mock = mock(GoodsController.class);

	@Test
	public void testWrongIdThrowsException() throws WrongPropertyValueException {
		Good good = new Good();
		good.setId(-1);
		doThrow(new WrongPropertyValueException("Impossible value of id")).when(mock)
		.addNewGood(good);
	}
	
	@Test
	public void testWrongQuantityThrowsException() throws WrongPropertyValueException {
		Good good = new Good();
		good.setQuantity(-1);
		doThrow(new WrongPropertyValueException("Impossible value of quantity")).when(mock)
		.addNewGood(good);
	}
	
	@Test
	public void testWrongPriceThrowsException() throws WrongPropertyValueException {		
		Good good = new Good();
		good.setPrice(-1.0);
		doThrow(new WrongPropertyValueException("Impossible value of price")).when(mock)
		.addNewGood(good);
	}
	
//	@Test
//	public void testAddNewGood() throws WrongPropertyValueException {
//		Good good = new Good();
//		good.setId(30);
//		good.setName("pen");
//		good.setPrice(15.0);
//		good.setQuantity(1);
//		assertEquals(good, mock.addNewGood(good));
//	}
	
	@Test
	public void testGetGoodInfo() throws WrongPropertyValueException {
		Good good = new Good();
		good.setId(30);
		good.setName("pen");
		good.setPrice(15.0);
		good.setQuantity(1);
		assertEquals(mock.getGoodInfo(30), good);
	}
}
