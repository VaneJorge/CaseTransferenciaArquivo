package br.fileTransfer.client.entities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.Arrays;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

@ManagedBean
@RequestScoped
public class Login implements Serializable {

	private static final long serialVersionUID = 1L;

	public void logar() throws IOException {
//		try {
//			FacesContext.getCurrentInstance().getExternalContext().redirect(
//					"https://accounts.google.com/o/oauth2/auth?client_id=654065539636-6gai0mt2q5qv65nnphvanp1hcg3vcpkk.apps.googleusercontent.com&redirect_uri=http://localhost:8080&scope=https://mail.google.com&response_type=code&access_type=offline");

//			Form form = new Form().param("client_id", "123456").param("username", "teste").param("password",
//					"teste123");
//			ResteasyClient client = new ResteasyClientBuilder().build();
//			ResteasyWebTarget target = client
//					.target("https://accounts.google.com/o/oauth2/auth");
//			Response response = target.request(MediaType.APPLICATION_JSON).post(Entity.form(form));
//			String json = response.readEntity(String.class);
//			response.close();
//			ObjectMapper mapper = new ObjectMapper();
//			Autorizacao autorizacao = mapper.readValue(json, Autorizacao.class);
//			System.out.println(autorizacao.getBody().getToken());
//			System.out.println(autorizacao.getBody().getExpires());
//		} catch (Exception e) {
//			System.out.println("Erro: " + e.getMessage());
//		}

//		GoogleAuthorizationCodeFlow authorizationFlow = new GoogleAuthorizationCodeFlow.Builder(
//			      new NetHttpTransport(),
//			      new JacksonFactory(),
//			      "286472118492-p8ilurhtbovi82ghua2f74h44hl57bmk.apps.googleusercontent.com",
//			      "c6KBrJaRYwIrPBlwEisGb-GF",
//			      Arrays.asList(DriveScope))
//			      .setDataStoreFactory(storeFactory)
//			      // Set the access type to offline so that the token can be refreshed.
//			      // By default, the library will automatically refresh tokens when it
//			      // can, but this can be turned off by setting
//			      // api.dfp.refreshOAuth2Token=false in your ads.properties file.
//			      .setAccessType("offline").build();
//
//			  String authorizeUrl =
//			      authorizationFlow.newAuthorizationUrl().setRedirectUri(CALLBACK_URL).build();
//			  System.out.printf("Paste this url in your browser:%n%s%n", authorizeUrl);
//
//			  // Wait for the authorization code.
//			  System.out.println("Type the code you received here: ");
//			  @SuppressWarnings("DefaultCharset") // Reading from stdin, so default charset is appropriate.
//			  String authorizationCode = new BufferedReader(new InputStreamReader(System.in)).readLine();
//
//			  // Authorize the OAuth2 token.
//			  GoogleAuthorizationCodeTokenRequest tokenRequest =
//			      authorizationFlow.newTokenRequest(authorizationCode);
//			  tokenRequest.setRedirectUri(CALLBACK_URL);
//			  GoogleTokenResponse tokenResponse = tokenRequest.execute();
//
//			  // Store the credential for the user.
//			  authorizationFlow.createAndStoreCredential(tokenResponse, userId);

	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
