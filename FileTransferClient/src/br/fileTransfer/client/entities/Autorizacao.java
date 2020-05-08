package br.fileTransfer.client.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Autorizacao {
	@JsonProperty("responseStatus")
	private Integer responseStatus;
	@JsonProperty("locationUri")
	private String locationUri;
	@JsonProperty("headers")
	private JsonNode headers;
	@JsonProperty("body")
	private Body body;

	public Integer getResponseStatus() {
		return responseStatus;
	}

	public void setResponseStatus(Integer responseStatus) {
		this.responseStatus = responseStatus;
	}

	public String getLocationUri() {
		return locationUri;
	}

	public void setLocationUri(String locationUri) {
		this.locationUri = locationUri;
	}

	public JsonNode getHeaders() {
		return headers;
	}

	public void setHeaders(JsonNode headers) {
		this.headers = headers;
	}

	public Body getBody() {
		return body;
	}

	public void setBody(Body body) {
		this.body = body;
	}
}