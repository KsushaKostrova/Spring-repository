package com.kostrova.store;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.servlet.Filter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = { WebAppConfig.class, WebSecurity.class })
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(GoodsController.class)
public class TestGoodsController {

	@Mock
	GoodService goodService;

	@InjectMocks
	GoodsController goodsController;
	String base64ManagerCredentials;

	@Autowired
	private Filter springSecurityFilterChain;

	@Autowired
	private UserDetailsService userDetailsService;

	private MockMvc mockMvc;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		goodsController = new GoodsController();
		goodsController.setGoodService(goodService);

		String plainCreds = "manager:managers";
		byte[] encodedBytes = Base64.getEncoder().encode(plainCreds.getBytes());
		base64ManagerCredentials = new String(encodedBytes);

	
		mockMvc = MockMvcBuilders.standaloneSetup(goodsController).addFilters(springSecurityFilterChain).build();
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
	public void testAddExistingGoodAsManager() throws Exception {
		Good expected = new Good();
		expected.setId(1);
		expected.setName("pen");
		expected.setPrice(15.0);
		expected.setQuantity(1);
		UserDetails userDetails = userDetailsService.loadUserByUsername("manager");
		MockHttpServletRequestBuilder addNewGood = put("/good").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.content(
						"{\r\n" + "	\"id\":\"1\", \"name\":\"pen\", \"price\":\"15.0\", \"quantity\":\"1\"\r\n" + "}")
				.with(user(userDetails));

		mockMvc.perform(addNewGood).andExpect(status().isOk());

		verify(goodService, times(1)).addExistingGood(eq(expected));
	}

	@Test
	public void testAddNewGoodAsManager() throws Exception {
		Good expected = new Good();
		expected.setId(1);
		expected.setName("pencil");
		expected.setPrice(15.0);
		expected.setQuantity(2);
		UserDetails userDetails = userDetailsService.loadUserByUsername("manager");
		MockHttpServletRequestBuilder addNewGood = post("/good")
				.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).content("{\r\n"
						+ "	\"id\":\"1\", \"name\":\"pencil\", \"price\":\"15.0\", \"quantity\":\"2\"\r\n" + "}")
				.with(user(userDetails));

		mockMvc.perform(addNewGood).andExpect(status().isOk());

		verify(goodService, times(1)).addNewGood(eq(expected));
	}

	@Test
	public void testDeleteGoodAsManager() throws Exception {
		Good expected = new Good();
		expected.setId(1);
		UserDetails userDetails = userDetailsService.loadUserByUsername("manager");
		MockHttpServletRequestBuilder addNewGood = delete("/good/1").with(user(userDetails));
		mockMvc.perform(addNewGood).andExpect(status().isOk()).andExpect(authenticated().withUsername("manager"));
		verify(goodService, times(1)).deleteGood(eq(expected.getId()));
	}

	@Test
	public void testGetGoodInfoAsManager() throws Exception {

		Good actual = new Good();
		actual.setId(1);
		actual.setName("pencil");
		actual.setPrice(15.0);
		actual.setQuantity(2);
		when(goodService.getGood(actual.getId())).thenReturn(actual);
		
		UserDetails userDetails = userDetailsService.loadUserByUsername("manager");
		mockMvc.perform(get("/good/1").with(user(userDetails))).andExpect(status().isOk())
				.andExpect(content().json(
						"{" + "\"id\": 1," + "\"name\": \"pencil\"," + "\"price\": 15.0," + "\"quantity\": 2" + "}"));

		verify(goodService, times(1)).getGood(actual.getId());
	}
	
	@Test
	public void testGetGoodInfoAsEmployee() throws Exception {

		Good actual = new Good();
		actual.setId(1);
		actual.setName("pencil");
		actual.setPrice(15.0);
		actual.setQuantity(2);
		when(goodService.getGood(actual.getId())).thenReturn(actual);
		
		UserDetails userDetails = userDetailsService.loadUserByUsername("employee");
		mockMvc.perform(get("/good/1").with(user(userDetails))).andExpect(status().isOk())
				.andExpect(content().json(
						"{" + "\"id\": 1," + "\"name\": \"pencil\"," + "\"price\": 15.0," + "\"quantity\": 2" + "}"));

		verify(goodService, times(1)).getGood(actual.getId());
	}

	@Test
	public void testGetInfoAboutAllGoodsAsManager() throws Exception {

		UserDetails userDetails = userDetailsService.loadUserByUsername("manager");
		Good good = new Good();
		good.setId(1);
		good.setName("pencil");
		good.setPrice(15.0);
		good.setQuantity(2);

		List<Good> actual = new ArrayList<>();
		actual.add(good);
		when(goodService.getAllGoods()).thenReturn(actual);

		mockMvc.perform(get("/goods").with(user(userDetails)).accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(status().isOk()).andExpect(content().json("[" + "{" + "\"id\": 1," + "\"name\": \"pencil\","
						+ "\"price\": 15.0," + "\"quantity\": 2" + "}]"));

		verify(goodService, times(1)).getAllGoods();
	}
	
	@Test
	public void testGetInfoAboutAllGoodsAsEmployee() throws Exception {

		UserDetails userDetails = userDetailsService.loadUserByUsername("employee");
		Good good = new Good();
		good.setId(1);
		good.setName("pencil");
		good.setPrice(15.0);
		good.setQuantity(2);

		List<Good> actual = new ArrayList<>();
		actual.add(good);
		when(goodService.getAllGoods()).thenReturn(actual);

		mockMvc.perform(get("/goods").with(user(userDetails)).accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(status().isOk()).andExpect(content().json("[" + "{" + "\"id\": 1," + "\"name\": \"pencil\","
						+ "\"price\": 15.0," + "\"quantity\": 2" + "}]"));

		verify(goodService, times(1)).getAllGoods();
	}
}
