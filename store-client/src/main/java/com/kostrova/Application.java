package com.kostrova;

import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class Application {

	private static final Logger log = LoggerFactory.getLogger(Application.class);

	public static void main(String args[]) {
		SpringApplication.run(Application.class);
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

	@Bean
	public CommandLineRunner run(RestTemplate restTemplate) throws Exception {
		return args -> {
			Good good = new Good();
			good.setId(30);
			good.setName("pen");
			good.setPrice(15.0);
			good.setQuantity(1);
			addNewGood(restTemplate, good);
			good.setQuantity(5);
			addExistingGood(good);
		//	deleteGood(30);
			printGood(30);
		};
	}

	private static HttpHeaders getHeaders() {
		String plainCreds = "manager:manager";
		byte[] encodedBytes = Base64.getEncoder().encode(plainCreds.getBytes());
		String base64ClientCredentials = new String(encodedBytes);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Basic " + base64ClientCredentials);
		return headers;
	}

	public void printGood(Integer goodId) {
		RestTemplate restTemplate = new RestTemplate();

		String url = "http://localhost:8080/store-server/good/{goodId}";
//		MultiValueMap<String, Integer> params = new LinkedMultiValueMap<>();
//		params.set("goodId", id);

		HttpEntity<Object> request = new HttpEntity<Object>(getHeaders());
		ResponseEntity<Good> response = restTemplate.exchange(url, HttpMethod.GET, request, Good.class, goodId);
		Good good = response.getBody();
		System.out.println(good.toString());
		log.info(good.toString());

		// HttpEntity<String> request = new HttpEntity<String>(headers);
		// restTemplate.exchange(url, HttpMethod.GET, request,
		// Application.class).getBody();

		// Good good =
		// restTemplate.getForObject("http://localhost:8080/store-server/good/{id}",
		// Good.class, params);
		// log.info(good.toString());
	}

	public void addNewGood(RestTemplate restTemplate, Good good) {
		String url = "http://localhost:8080/store-server/good/";

		HttpEntity<Object> request = new HttpEntity<Object>(good, getHeaders());
		
		restTemplate.exchange(url, HttpMethod.POST, request, Good.class);
		
		System.out.println(good.toString());
		log.info(good.toString());
	}

	public void deleteGood(Integer goodId) {
		RestTemplate restTemplate = new RestTemplate();
		String url = "http://localhost:8080/store-server/good/{goodId}";
//		MultiValueMap<String, Integer> params = new LinkedMultiValueMap<>();
//		params.set("goodId", goodId);

		HttpEntity<Object> request = new HttpEntity<Object>(getHeaders());
	//	restTemplate.delete(url, request, void.class);
		restTemplate.exchange(url, HttpMethod.DELETE, request, void.class, goodId);
		
//		System.out.println(good.toString());
//		log.info(good.toString());
	
		
	//	restTemplate.delete("http://localhost:8080/store-server/good/{id}", params);
	}

	public void printAllGoodInfo() {
		String url = "http://localhost:8080/store-server/goods";
		
		RestTemplate restTemplate = new RestTemplate(); 
		
	//	List<Good> goods = restTemplate.getForObject("http://localhost:8080/store-server/goods/", List.class);
		
        HttpEntity<String> request = new HttpEntity<String>(getHeaders());
        ResponseEntity<List> response = restTemplate.exchange(url, HttpMethod.GET, request, List.class);
        List<LinkedHashMap<String, Object>> goodMap = (List<LinkedHashMap<String, Object>>)response.getBody();
         
        if(goodMap!=null){
            for(LinkedHashMap<String, Object> map : goodMap){
            	log.info("Good : id="+map.get("id")+", Name="+map.get("name")+", Price="+map.get("price")+", Quantity="+map.get("quantity"));
            }
        }
		
//		if (!goods.isEmpty()) {
//			for (Good good : goods) {
//				log.info(good.toString());
//			}
//		}
	}

	public void addExistingGood(Good good) {
		String url = "http://localhost:8080/store-server/good/";
		
		HttpEntity<Object> request = new HttpEntity<Object>(good, getHeaders());
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.exchange(url, HttpMethod.POST, request, Good.class);
		
		System.out.println(good.toString());
		log.info(good.toString());
	}
}