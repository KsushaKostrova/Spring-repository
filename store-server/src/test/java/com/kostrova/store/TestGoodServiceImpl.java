package com.kostrova.store;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations; 

public class TestGoodServiceImpl {
	
	GoodServiceImpl goodService;
	@Mock
	IGoodDao goodDao;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		goodService = new GoodServiceImpl();
		goodService.setGoodRepository(goodDao);
	}
	
	@Test
	public void testAddExistingGood() {
	//	Mockito.spy(goodDao);
		Good expected = new Good();
		expected.setId(1);
		goodService.addExistingGood(expected);
		verify(goodDao, times(1)).addExistingGood(eq(expected));
	}
	
	@Test
	public void testAddNewGood() {
		Good expected = new Good();
		expected.setId(1);
		goodService.addNewGood(expected);
		verify(goodDao, times(1)).addNewGood(eq(expected));
	}
	
	@Test
	public void testGetGood() {
		Good expected = new Good();
		expected.setId(1);
		goodService.getGood(expected.getId());
		verify(goodDao, times(1)).getGood(eq(expected.getId()));
	}
	
	@Test
	public void testDeleteGood() {
		Good expected = new Good();
		expected.setId(1);
		goodService.deleteGood(1);
		verify(goodDao, times(1)).deleteGood(eq(expected.getId()));
	}
	
	@Test
	public void testGetAllGoods() {
		goodService.getAllGoods();
		verify(goodDao, times(1)).getAllGoods();
	}
}
