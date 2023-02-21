package com.example.testwiremock;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.HttpStatusCode;

import static org.junit.jupiter.api.Assertions.assertEquals;


import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

@SpringBootTest
@AutoConfigureWireMock(port = 0)
@AutoConfigureWebClient
class TestWiremockApplicationTests {

	@Autowired
	RestTemplateBuilder restTemplateBuilder;

	@Value("${wiremock.server.port}")
	String wiremockPort;

	@Test
	void contextLoads() {
		stubFor(get(urlEqualTo("/resource"))
				.willReturn(aResponse().withHeader("Content-Type", "text/plain").withBody("Hello World!")));

		var response = restTemplateBuilder.build()
				.getForEntity("http://localhost:"+wiremockPort+"/resource", String.class);

		assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
		assertEquals("Hello World!", response.getBody());
	}

}
