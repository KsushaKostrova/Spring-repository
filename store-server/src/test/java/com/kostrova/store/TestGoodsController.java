package com.kostrova.store;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Base64;
import java.util.List;

import javax.servlet.Filter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@ContextConfiguration(classes = { WebAppConfig.class, WebSecurity.class })
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class TestGoodsController {

	@Mock
	GoodService goodService;

	GoodsController goodsController;
	String base64ManagerCredentials;

	@Autowired
	private Filter springSecurityFilterChain;

	@Autowired
	private WebApplicationContext wac;

	private MockMvc mockMvc;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		goodsController = new GoodsController();
		goodsController.setGoodService(goodService);

		String plainCreds = "manager:managers";
		byte[] encodedBytes = Base64.getEncoder().encode(plainCreds.getBytes());
		base64ManagerCredentials = new String(encodedBytes);
		// mockMvc =
		// MockMvcBuilders.<StandaloneMockMvcBuilder>webAppContextSetup(wac).build();
	}

	@Test(expected = WrongPropertyValueException.class)
	public void testAddNewGood_wrongIdThrowsException() throws WrongPropertyValueException {
		Good good = new Good();
		good.setId(-1);
		good.setName("pen");
		good.setPrice(15.0);
		good.setQuantity(1);
		when(goodsController.addNewGood(good)).thenThrow(new WrongPropertyValueException("Impossible value of id"));
	}

	@Test(expected = WrongPropertyValueException.class)
	public void testAddNewGood_wrongQuantityThrowsException() throws WrongPropertyValueException {
		Good good = new Good();
		good.setId(1);
		good.setName("pen");
		good.setPrice(15.0);
		good.setQuantity(-1);
		when(goodsController.addNewGood(good))
				.thenThrow(new WrongPropertyValueException("Impossible value of quantity"));
	}

	@Test(expected = WrongPropertyValueException.class)
	public void testAddNewGood_wrongPriceThrowsException() throws WrongPropertyValueException {
		Good good = new Good();
		good.setId(1);
		good.setName("pen");
		good.setPrice(-1.0);
		good.setQuantity(1);
		when(goodsController.addNewGood(good)).thenThrow(new WrongPropertyValueException("Impossible value of price"));
	}

	@Test(expected = WrongPropertyValueException.class)
	public void testAddGood_wrongIdThrowsException() throws WrongPropertyValueException {
		Good good = new Good();
		good.setId(-1);
		good.setName("pen");
		good.setPrice(15.0);
		good.setQuantity(1);
		when(goodsController.addGood(good)).thenThrow(new WrongPropertyValueException("Impossible value of id"));
	}

	@Test(expected = WrongPropertyValueException.class)
	public void testAddGood_wrongPriceThrowsException() throws WrongPropertyValueException {
		Good good = new Good();
		good.setId(1);
		good.setName("pen");
		good.setPrice(-1.0);
		good.setQuantity(1);
		when(goodsController.addGood(good)).thenThrow(new WrongPropertyValueException("Impossible value of price"));
	}

	@Test(expected = WrongPropertyValueException.class)
	public void testAddGood_wrongQuantityThrowsException() throws WrongPropertyValueException {
		Good good = new Good();
		good.setId(1);
		good.setName("pen");
		good.setPrice(15.0);
		good.setQuantity(-1);
		when(goodsController.addGood(good)).thenThrow(new WrongPropertyValueException("Impossible value of quantity"));
	}

	@Test(expected = WrongPropertyValueException.class)
	public void testDeleteGood_wrongIdThrowsException() throws WrongPropertyValueException {
		Good good = new Good();
		good.setId(-1);
		goodsController.deleteGood(good.getId());
	}

	@Test(expected = WrongPropertyValueException.class)
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
	public void testGetInfoAboutAllGoods() throws Exception {
		Good good = new Good();
		good.setId(1);
		List<Good> actual = goodService.getAllGoods();
		List<Good> expected = goodsController.getInfoAboutAllGoods().getBody();
		when(goodService.getAllGoods()).thenReturn(actual);

		mockMvc = MockMvcBuilders.webAppContextSetup(wac).addFilters(springSecurityFilterChain).build();
		mockMvc.perform(get("/goods").with(user("manager").password("manager").roles("MANAGER")))
				.andExpect(status().isOk()).andExpect(authenticated().withUsername("manager"));
		
		assertEquals(expected, actual);
	}
}
