package com.kostrova;

import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;

import java.util.ArrayList;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TestApplication {

	Application app;
	MockRestServiceServer mockServer;

	@Mock
	RestTemplate restTemplate;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		app = new Application();

		restTemplate = new RestTemplate();
		app.setRestTemplate(restTemplate);

		mockServer = MockRestServiceServer.createServer(app.getRestTemplate());
	}
	
	private String getCredentials(String user) {
		String plainCreds = user + ":" + user;
		byte[] encodedBytes = Base64.getEncoder().encode(plainCreds.getBytes());
		String credentials = new String(encodedBytes);
		return credentials;
	}

	@Test
	public void testPrintGood() throws WrongPropertyValueException, NotExistingGoodException, JsonProcessingException {
		Good good = new Good();
		good.setId(1);
		good.setName("pen");
		good.setPrice(15.0);
		good.setQuantity(1);
		ObjectMapper mapper = new ObjectMapper();
		mapper.writeValueAsString(good);
		mockServer.expect(requestTo("http://localhost:8080/store-server/good/1")).andExpect(method(HttpMethod.GET))
				.andExpect(header("Authorization", anyOf(equalTo("Basic " + getCredentials("manager")),
						equalTo("Basic " + getCredentials("employee")))))
				.andRespond(MockRestResponseCreators.withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(mapper.writeValueAsString(good)));
			//			.body("{\"id\":1,\"name\":\"pen\",\"price\":15.0,\"quantity\":1}"));
		Good result = app.printGood(1);
		mockServer.verify();
		assertEquals(result, good);
	}

	@Test
	public void testPrintAllGoodInfo() {
		Good good1 = new Good();
		good1.setId(1);
		good1.setName("pen");
		good1.setPrice(15.0);
		good1.setQuantity(1);

		Good good2 = new Good();
		good2.setId(2);
		good2.setName("pencil");
		good2.setPrice(15.0);
		good2.setQuantity(2);

		LinkedHashMap<String, Object> mapGood1 = new LinkedHashMap<>();
		mapGood1.put("id", good1.getId());
		mapGood1.put("name", good1.getName());
		mapGood1.put("price", good1.getPrice());
		mapGood1.put("quantity", good1.getQuantity());

		LinkedHashMap<String, Object> mapGood2 = new LinkedHashMap<>();
		mapGood2.put("id", good2.getId());
		mapGood2.put("name", good2.getName());
		mapGood2.put("price", good2.getPrice());
		mapGood2.put("quantity", good2.getQuantity());

		List<LinkedHashMap<String, Object>> goodsMap = new ArrayList<>();
		goodsMap.add(mapGood1);
		goodsMap.add(mapGood2);

		mockServer.expect(requestTo("http://localhost:8080/store-server/goods")).andExpect(method(HttpMethod.GET))
				.andExpect(header("Authorization",
						anyOf(equalTo("Basic " + getCredentials("manager")),
								equalTo("Basic " + getCredentials("employee")))))
				.andRespond(MockRestResponseCreators.withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON)
						.body("[{\"id\": 1,\"name\": \"pen\",\"price\": 15.0,\"quantity\": 1},{\"id\": 2,\"name\": \"pencil\",\"price\": 15.0,\"quantity\": 2}]"));

		List<LinkedHashMap<String, Object>> result = app.printAllGoodInfo();
		mockServer.verify();
		assertEquals(result, goodsMap);
	}

	@Test
	public void testDeleteGood() throws WrongPropertyValueException {
		Good good = new Good();
		good.setId(1);
		good.setName("pen");
		good.setPrice(15.0);
		good.setQuantity(1);

		mockServer.expect(requestTo("http://localhost:8080/store-server/good/1")).andExpect(method(HttpMethod.DELETE))
				.andExpect(header("Authorization", "Basic " + getCredentials("manager")))
				.andRespond(MockRestResponseCreators.withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON)
						.body(""));
		app.deleteGood(1);
		mockServer.verify();
	}

	@Test
	public void testAddNewGood() throws WrongPropertyValueException {
		Good good = new Good();
		good.setId(3);
		good.setName("knife");
		good.setPrice(35.0);
		good.setQuantity(1);

		mockServer.expect(requestTo("http://localhost:8080/store-server/good/")).andExpect(method(HttpMethod.POST))
				.andExpect(header("Authorization", "Basic " + getCredentials("manager")))
				.andRespond(MockRestResponseCreators.withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON));
		app.addNewGood(good);
		mockServer.verify();
	}

	@Test
	public void testAddExistingGood() throws WrongPropertyValueException {
		Good good = new Good();
		good.setId(3);
		good.setName("knife");
		good.setPrice(35.0);
		good.setQuantity(4);

		mockServer.expect(requestTo("http://localhost:8080/store-server/good/")).andExpect(method(HttpMethod.PUT))
				.andExpect(header("Authorization", "Basic " + getCredentials("manager")))
				.andRespond(MockRestResponseCreators.withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON));
		app.addExistingGood(good);
		mockServer.verify();
	}

	@Test(expected = WrongPropertyValueException.class)
	public void testPrintGood_badArgument() throws WrongPropertyValueException, NotExistingGoodException {
		Good good = new Good();
		good.setId(-1);
		app.printGood(good.getId());
	}

	@Test(expected = NotExistingGoodException.class)
	public void testPrintGood_notExistingGood()
			throws com.kostrova.NotExistingGoodException, WrongPropertyValueException {
		Good good = new Good();
		good.setId(40);
		mockServer.expect(requestTo("http://localhost:8080/store-server/good/40")).andExpect(method(HttpMethod.GET))
				.andExpect(header("Authorization",
						anyOf(equalTo("Basic " + getCredentials("manager")),
								equalTo("Basic " + getCredentials("employee")))))
				.andRespond(MockRestResponseCreators.withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON));
		app.printGood(good.getId());
	}

	@Test(expected = WrongPropertyValueException.class)
	public void testAddNewGood_badArgumentId() throws WrongPropertyValueException {
		Good good = new Good();
		good.setId(-1);
		good.setName("knife");
		good.setPrice(35.0);
		good.setQuantity(4);
		app.addNewGood(good);
	}

	@Test(expected = WrongPropertyValueException.class)
	public void testAddNewGood_badArgumentPrice() throws WrongPropertyValueException {
		Good good = new Good();
		good.setId(1);
		good.setName("knife");
		good.setPrice(-1.0);
		good.setQuantity(4);
		app.addNewGood(good);
	}

	@Test(expected = WrongPropertyValueException.class)
	public void testAddNewGood_badArgumentQuantity() throws WrongPropertyValueException {
		Good good = new Good();
		good.setId(1);
		good.setName("knife");
		good.setPrice(35.0);
		good.setQuantity(-4);
		app.addNewGood(good);
	}

	@Test(expected = WrongPropertyValueException.class)
	public void testDeleteGood_badArgument() throws WrongPropertyValueException {
		Good good = new Good();
		good.setId(-1);
		app.deleteGood(good.getId());
	}

	@Test
	public void testDeleteGood_notExistingGood() throws com.kostrova.WrongPropertyValueException {
		Good good = new Good();
		good.setId(40);
		mockServer.expect(requestTo("http://localhost:8080/store-server/good/40")).andExpect(method(HttpMethod.DELETE))
				.andExpect(header("Authorization", "Basic " + getCredentials("manager")))
				.andRespond(MockRestResponseCreators.withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON));
		app.deleteGood(good.getId());
	}

	@Test(expected = WrongPropertyValueException.class)
	public void testExistingNewGood_badArgumentId() throws WrongPropertyValueException {
		Good good = new Good();
		good.setId(-1);
		good.setName("knife");
		good.setPrice(35.0);
		good.setQuantity(4);
		app.addExistingGood(good);
	}

	@Test(expected = WrongPropertyValueException.class)
	public void testAddExistingGood_badArgumentPrice() throws WrongPropertyValueException {
		Good good = new Good();
		good.setId(1);
		good.setName("knife");
		good.setPrice(-1.0);
		good.setQuantity(4);
		app.addExistingGood(good);
	}

	@Test(expected = WrongPropertyValueException.class)
	public void testAddExistingGood_badArgumentQuantity() throws WrongPropertyValueException {
		Good good = new Good();
		good.setId(1);
		good.setName("knife");
		good.setPrice(35.0);
		good.setQuantity(-4);
		app.addExistingGood(good);
	}
}
