package com.kostrova.store;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations; 

public class TestGoodDaoImpl {

	GoodDaoImpl goodDao;
	@Mock
	Map<Integer, Good> goods;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		goodDao = new GoodDaoImpl();
		goodDao.setGoods(goods);
	}
	
	@Test
	public void testAddExistingGood() {
		Good expected = new Good();
		expected.setId(1);
		goodDao.addExistingGood(expected);
		verify(goods, times(1)).put(eq(expected.getId()), eq(expected));
	}
	
	@Test
	public void testAddNewGood() {
		Good expected = new Good();
		expected.setId(1);
		goodDao.addExistingGood(expected);
		verify(goods, times(1)).put(eq(expected.getId()), eq(expected));
	}
	
	@Test
	public void testDeleteGood() {
		Good expected = new Good();
		expected.setId(1);
		goodDao.deleteGood(expected.getId());
		verify(goods, times(1)).remove(eq(expected.getId()));
	}
	
	@Test
	public void testGetGood() {
		Good expected = new Good();
		expected.setId(1);
		goodDao.getGood(expected.getId());
		verify(goods, times(1)).get(eq(expected.getId()));
	}
	
	@Test
	public void testGetAllGoods() {		
		ArrayList<Good> expected = new ArrayList<>(goods.values());
		ArrayList<Good> actual = (ArrayList<Good>) goodDao.getAllGoods();
	//	ArrayList<Good> result = new ArrayList<>(goodDao.getGoods().values());
		when(goodDao.getAllGoods()).thenReturn(actual);
		assertEquals(expected, actual);
	}
}
