package br.fileTransfer.client.entities;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Body {
	@JsonProperty("access_token")
	private String token;
	@JsonProperty("expires_in")
	private Long expires;

	public Body() {
	}

	public Body(String json) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			Body body = mapper.readValue(json, Body.class);
			this.token = body.getToken();
			this.expires = body.getExpires();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Long getExpires() {
		return expires;
	}

	public void setExpires(Long expires) {
		this.expires = expires;
	}
}