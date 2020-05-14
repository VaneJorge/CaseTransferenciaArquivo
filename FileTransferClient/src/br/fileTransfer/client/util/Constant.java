package br.fileTransfer.client.util;

public class Constant {

	private static String id;
	private static String email;
	private static String code;
	private static String accessToken;

	public static String getId() {
		return id;
	}

	public static void setId(String id) {
		Constant.id = id;
	}

	public static String getEmail() {
		return email;
	}

	public static void setEmail(String email) {
		Constant.email = email;
	}

	public static String getCode() {
		return code;
	}

	public static void setCode(String code) {
		Constant.code = code;
	}

	public static String getAccessToken() {
		return accessToken;
	}

	public static void setAccessToken(String accessToken) {
		Constant.accessToken = accessToken;
	}
}