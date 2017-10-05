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
import java.util.NoSuchElementException;

import javax.servlet.Filter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
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

import com.fasterxml.jackson.databind.ObjectMapper;

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

		mockMvc = MockMvcBuilders.standaloneSetup(goodsController)
				.setControllerAdvice(new GlobalControllerExceptionHandler()).addFilters(springSecurityFilterChain)
				.build();
	}

	@Test
	public void testAddNewGood_wrongIdThrowsException() throws Exception {
		Good good = new Good();
		good.setId(-1);
		good.setName("pen");
		good.setPrice(15.0);
		good.setQuantity(1);

		ObjectMapper mapper = new ObjectMapper();
		UserDetails userDetails = userDetailsService.loadUserByUsername("manager");
		mockMvc.perform(post("/good").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.content(mapper.writeValueAsString(good)).with(user(userDetails))).andExpect(status().isBadRequest());
	}

	@Test
	public void testAddNewGood_wrongQuantityThrowsException() throws Exception {
		Good good = new Good();
		good.setId(-1);
		good.setName("pen");
		good.setPrice(15.0);
		good.setQuantity(-1);

		ObjectMapper mapper = new ObjectMapper();
		UserDetails userDetails = userDetailsService.loadUserByUsername("manager");
		mockMvc.perform(post("/good").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.content(mapper.writeValueAsString(good)).with(user(userDetails))).andExpect(status().isBadRequest());
	}

	@Test
	public void testAddNewGood_wrongPriceThrowsException() throws Exception {
		Good good = new Good();
		good.setId(1);
		good.setName("pen");
		good.setPrice(-15.0);
		good.setQuantity(1);

		ObjectMapper mapper = new ObjectMapper();
		UserDetails userDetails = userDetailsService.loadUserByUsername("manager");
		mockMvc.perform(post("/good").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.content(mapper.writeValueAsString(good)).with(user(userDetails))).andExpect(status().isBadRequest());
	}
	
	@Test
	public void testAddNewGood_badAuthenticationThrowsException() throws Exception {		
		Good good = new Good();
		good.setId(1);
		good.setName("pen");
		good.setPrice(15.0);
		good.setQuantity(1);

		ObjectMapper mapper = new ObjectMapper();
		UserDetails userDetails = userDetailsService.loadUserByUsername("employee");
		mockMvc.perform(post("/good").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.content(mapper.writeValueAsString(good)).with(user(userDetails))).andExpect(status().isForbidden());
	}

	@Test
	public void testAddGood_wrongIdThrowsException() throws Exception {
		Good good = new Good();
		good.setId(-1);
		good.setName("pen");
		good.setPrice(15.0);
		good.setQuantity(1);

		ObjectMapper mapper = new ObjectMapper();
		UserDetails userDetails = userDetailsService.loadUserByUsername("manager");
		mockMvc.perform(put("/good").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.content(mapper.writeValueAsString(good)).with(user(userDetails))).andExpect(status().isBadRequest());
	}

	@Test
	public void testAddGood_wrongPriceThrowsException() throws Exception {
		Good good = new Good();
		good.setId(1);
		good.setName("pen");
		good.setPrice(-1.0);
		good.setQuantity(1);

		ObjectMapper mapper = new ObjectMapper();
		UserDetails userDetails = userDetailsService.loadUserByUsername("manager");
		mockMvc.perform(put("/good").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.content(mapper.writeValueAsString(good)).with(user(userDetails))).andExpect(status().isBadRequest());
	}

	@Test
	public void testAddGood_wrongQuantityThrowsException() throws Exception {
		Good good = new Good();
		good.setId(1);
		good.setName("pen");
		good.setPrice(15.0);
		good.setQuantity(-1);

		ObjectMapper mapper = new ObjectMapper();
		UserDetails userDetails = userDetailsService.loadUserByUsername("manager");
		mockMvc.perform(put("/good").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.content(mapper.writeValueAsString(good)).with(user(userDetails))).andExpect(status().isBadRequest());
	}

	@Test
	public void testAddGood_badAuthenticationThrowsException() throws Exception {		
		Good good = new Good();
		good.setId(1);
		good.setName("pen");
		good.setPrice(15.0);
		good.setQuantity(1);

		ObjectMapper mapper = new ObjectMapper();
		UserDetails userDetails = userDetailsService.loadUserByUsername("employee");
		mockMvc.perform(put("/good").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.content(mapper.writeValueAsString(good)).with(user(userDetails))).andExpect(status().isForbidden());
	}
	
	@Test
	public void testDeleteGood_wrongIdThrowsException() throws Exception {
		UserDetails userDetails = userDetailsService.loadUserByUsername("manager");
		mockMvc.perform(delete("/good/-1").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).with(user(userDetails)))
				.andExpect(status().isBadRequest());
	}
	
	@Test
	public void testDeleteGood_badAuthenticationThrowsException() throws Exception {		
		UserDetails userDetails = userDetailsService.loadUserByUsername("employee");
		MockHttpServletRequestBuilder deleteGood = delete("/good/1").with(user(userDetails));
		mockMvc.perform(deleteGood).andExpect(status().isForbidden());
	}
		

	@Test
	public void testGetGoodInfo_wrongIdThrowsException() throws Exception {
		UserDetails userDetails = userDetailsService.loadUserByUsername("manager");
		mockMvc.perform(get("/good/-1").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).with(user(userDetails)))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void testAddExistingGood_asManager() throws Exception {
		Good expected = new Good();
		expected.setId(1);
		expected.setName("pen");
		expected.setPrice(15.0);
		expected.setQuantity(1);
		UserDetails userDetails = userDetailsService.loadUserByUsername("manager");
		ObjectMapper mapper = new ObjectMapper();
		MockHttpServletRequestBuilder addNewGood = put("/good").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.content(mapper.writeValueAsString(expected)).with(user(userDetails));

		mockMvc.perform(addNewGood).andExpect(status().isOk());

		verify(goodService, times(1)).addExistingGood(eq(expected));
	}

	@Test
	public void testAddNewGood_asManager() throws Exception {
		Good expected = new Good();
		expected.setId(1);
		expected.setName("pencil");
		expected.setPrice(15.0);
		expected.setQuantity(2);
		UserDetails userDetails = userDetailsService.loadUserByUsername("manager");
		ObjectMapper mapper = new ObjectMapper();
		MockHttpServletRequestBuilder addNewGood = post("/good").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.content(mapper.writeValueAsString(expected)).with(user(userDetails));

		mockMvc.perform(addNewGood).andExpect(status().isOk());

		verify(goodService, times(1)).addNewGood(eq(expected));
	}

	@Test
	public void testDeleteGood_asManager() throws Exception {
		Good expected = new Good();
		expected.setId(1);
		UserDetails userDetails = userDetailsService.loadUserByUsername("manager");
		MockHttpServletRequestBuilder deleteGood = delete("/good/1").with(user(userDetails));
		mockMvc.perform(deleteGood).andExpect(status().isOk()).andExpect(authenticated().withUsername("manager"));
		verify(goodService, times(1)).deleteGood(eq(expected.getId()));
	}

	@Test
	public void testGetGoodInfo_asManager() throws Exception {

		Good expected = new Good();
		expected.setId(1);
		expected.setName("pencil");
		expected.setPrice(15.0);
		expected.setQuantity(2);
		when(goodService.getGood(expected.getId())).thenReturn(expected);
		UserDetails userDetails = userDetailsService.loadUserByUsername("manager");
		mockMvc.perform(get("/good/1").with(user(userDetails))).andExpect(status().isOk()).andExpect(content()
				.json("{" + "\"id\": 1," + "\"name\": \"pencil\"," + "\"price\": 15.0," + "\"quantity\": 2" + "}"));

		verify(goodService, times(1)).getGood(expected.getId());
	}

	@Test
	public void testGetGoodInfo_asEmployee() throws Exception {

		Good actual = new Good();
		actual.setId(1);
		actual.setName("pencil");
		actual.setPrice(15.0);
		actual.setQuantity(2);
		when(goodService.getGood(actual.getId())).thenReturn(actual);

		UserDetails userDetails = userDetailsService.loadUserByUsername("employee");
		mockMvc.perform(get("/good/1").with(user(userDetails))).andExpect(status().isOk()).andExpect(content()
				.json("{" + "\"id\": 1," + "\"name\": \"pencil\"," + "\"price\": 15.0," + "\"quantity\": 2" + "}"));

		verify(goodService, times(1)).getGood(actual.getId());
	}

	@Test
	public void testGetGoodInfo_badValueThrowsException() throws Exception {

		when(goodService.getGood(5)).thenThrow(new NoSuchElementException());

		UserDetails userDetails = userDetailsService.loadUserByUsername("employee");
		mockMvc.perform(get("/good/5").with(user(userDetails))).andExpect(status().isNotFound());
	}

	@Test
	public void testGetInfoAboutAllGoods_asManager() throws Exception {

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
	public void testGetInfoAboutAllGoods_asEmployee() throws Exception {

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
