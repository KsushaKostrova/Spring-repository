package com.kostrova;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;

import java.util.ArrayList;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.web.client.RestTemplate;

public class TestApplication {

	Application app;
	HttpHeaders headers;

	MockRestServiceServer mockServer;

	// @Mock
	RestTemplate restTemplate;

	@Before
	public void setUp() {
		// RestTemplate restTemplate = new RestTemplate();

		MockitoAnnotations.initMocks(this);
		app = new Application();

		restTemplate = new RestTemplate();
		app.setRestTemplate(restTemplate);

		mockServer = MockRestServiceServer.createServer(app.getRestTemplate());

		String plainCreds = "manager:manager";
		byte[] encodedBytes = Base64.getEncoder().encode(plainCreds.getBytes());
		String base64ClientCredentials = new String(encodedBytes);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Basic " + base64ClientCredentials);
	}

	@Test
	public void testPrintGood() {
		Good good = new Good();
		good.setId(1);
		good.setName("pen");
		good.setPrice(15.0);
		good.setQuantity(1);
		mockServer.expect(requestTo("http://localhost:8080/store-server/good/1")).andExpect(method(HttpMethod.GET))
				.andRespond(MockRestResponseCreators.withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON)
						.body("{\"id\":1,\"name\":\"pen\",\"price\":15.0,\"quantity\":1}"));
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
				.andRespond(MockRestResponseCreators.withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON)
						.body("[{\"id\": 1,\"name\": \"pen\",\"price\": 15.0,\"quantity\": 1},{\"id\": 2,\"name\": \"pencil\",\"price\": 15.0,\"quantity\": 2}]"));

		List<LinkedHashMap<String, Object>> result = app.printAllGoodInfo();
		mockServer.verify();
		assertEquals(result, goodsMap);
	}
	
	@Test
	public void testDeleteGood() {
		Good good = new Good();
		good.setId(1);
		good.setName("pen");
		good.setPrice(15.0);
		good.setQuantity(1);
		
		mockServer.expect(requestTo("http://localhost:8080/store-server/good/1")).andExpect(method(HttpMethod.DELETE))
		.andRespond(MockRestResponseCreators.withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON));
		mockServer.verify();
	}
}
