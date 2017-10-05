package com.kostrova;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class Application {

	private static final Logger log = LoggerFactory.getLogger(Application.class);

	@Autowired
	private RestTemplate restTemplate;

	public static void main(String args[]) {
		SpringApplication.run(Application.class);
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Bean
	public CommandLineRunner run() throws Exception {
		return args -> {
			Good good = new Good();
			good.setId(30);
			good.setName("pen");
			good.setPrice(15.0);
			good.setQuantity(1);
			try {
				addNewGood(good);
				printGood(30);
				good.setQuantity(5);
				addExistingGood(good);
				printGood(30);
				deleteGood(30);
				printAllGoodInfo();
			} catch (WrongPropertyValueException e) {
				e.printStackTrace();
			} catch (NotExistingGoodException ex) {
				ex.printStackTrace();
			}
		};
	}

	private static HttpHeaders getHeaders(String role) {
		HttpHeaders headers = null;
		if (role.equals("manager") || role.equals("employee")) {
			String plainCreds = role + ":" + role;
			byte[] encodedBytes = Base64.getEncoder().encode(plainCreds.getBytes());
			String base64ClientCredentials = new String(encodedBytes);
			headers = new HttpHeaders();
			headers.add("Authorization", "Basic " + base64ClientCredentials);
		}
		return headers;
	}

	public Good printGood(Integer goodId) throws WrongPropertyValueException, NotExistingGoodException {
		if (goodId <= 0) {
			throw new WrongPropertyValueException("Impossible value of id");
		}
		String url = "http://localhost:8080/store-server/good/{goodId}";
		HttpEntity<Object> request = new HttpEntity<Object>(getHeaders("employee"));

		Good good = null;
		ResponseEntity<Good> response = restTemplate.exchange(url, HttpMethod.GET, request, Good.class, goodId);
		good = response.getBody();
		if (good == null) {
			throw new NotExistingGoodException("there is no good with id " + goodId);
		}
		try {
			Files.write(Paths.get("src/main/resources/goodFile.txt"), good.toString().getBytes(),
					StandardOpenOption.APPEND);
		} catch (IOException e) {
		}
		log.info(good.toString());
		return good;
	}

	public void addNewGood(Good good) throws WrongPropertyValueException {
		if (good.getId() <= 0) {
			throw new WrongPropertyValueException("Impossible value of id");
		} else if (good.getQuantity() < 0) {
			throw new WrongPropertyValueException("Impossible value of quantity");
		} else if (good.getPrice() < 0) {
			throw new WrongPropertyValueException("Impossible value of price");
		}
		String url = "http://localhost:8080/store-server/good/";

		HttpEntity<Object> request = new HttpEntity<Object>(good, getHeaders("manager"));

		restTemplate.exchange(url, HttpMethod.POST, request, Good.class);

		System.out.println(good.toString());
		log.info(good.toString());
	}

	public void deleteGood(Integer goodId) throws WrongPropertyValueException {
		if (goodId <= 0) {
			throw new WrongPropertyValueException("Impossible value of id");
		}
		String url = "http://localhost:8080/store-server/good/{goodId}";

		HttpEntity<Object> request = new HttpEntity<Object>(getHeaders("manager"));
		restTemplate.exchange(url, HttpMethod.DELETE, request, void.class, goodId);
	}

	public List<LinkedHashMap<String, Object>> printAllGoodInfo() {
		String url = "http://localhost:8080/store-server/goods";

		HttpEntity<String> request = new HttpEntity<String>(getHeaders("employee"));
		ResponseEntity<List> response = restTemplate.exchange(url, HttpMethod.GET, request, List.class);
		List<LinkedHashMap<String, Object>> goodMap = (List<LinkedHashMap<String, Object>>) response.getBody();
		try (FileWriter fw = new FileWriter("src/main/resources/goodFile.txt", true);
				BufferedWriter bw = new BufferedWriter(fw);
				PrintWriter out = new PrintWriter(bw)) {
			if (goodMap != null) {
				for (LinkedHashMap<String, Object> map : goodMap) {
					out.println("Good : id=" + map.get("id") + ", Name=" + map.get("name") + ", Price="
							+ map.get("price") + ", Quantity=" + map.get("quantity"));
					log.info("Good : id=" + map.get("id") + ", Name=" + map.get("name") + ", Price=" + map.get("price")
							+ ", Quantity=" + map.get("quantity"));
				}
			}
			out.println("there's nothing in the store");
		} catch (IOException e) {

		}
		return goodMap;
	}

	public void addExistingGood(Good good) throws WrongPropertyValueException {
		if (good.getId() <= 0) {
			throw new WrongPropertyValueException("Impossible value of id");
		} else if (good.getQuantity() < 0) {
			throw new WrongPropertyValueException("Impossible value of quantity");
		} else if (good.getPrice() < 0) {
			throw new WrongPropertyValueException("Impossible value of price");
		}
		String url = "http://localhost:8080/store-server/good/";

		HttpEntity<Object> request = new HttpEntity<Object>(good, getHeaders("manager"));
		restTemplate.exchange(url, HttpMethod.PUT, request, Good.class);

		System.out.println(good.toString());
		log.info(good.toString());
	}

	public RestTemplate getRestTemplate() {
		return restTemplate;
	}

	public void setRestTemplate(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}
}