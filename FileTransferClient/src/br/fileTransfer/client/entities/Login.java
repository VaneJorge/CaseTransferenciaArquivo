package br.fileTransfer.client.entities;

import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;

import br.fileTransfer.client.util.WritingLog;

@ManagedBean
@RequestScoped
public class Login implements Serializable {

	private static final long serialVersionUID = 1L;
	private static String CLIENT_ID = "286472118492-p8ilurhtbovi82ghua2f74h44hl57bmk.apps.googleusercontent.com";
	private static String CLIENT_SECRET = "c6KBrJaRYwIrPBlwEisGb-GF";
	private static String REDIRECT_URI = "http://localhost:8080/FileTransferClient/Transferencia.xhtml";
	private static List<String> SCOPES = Arrays.asList("https://www.googleapis.com/auth/userinfo#email");

	public Login() {
	}

	public void logar() throws IOException {
		try {
			String authorizationUrl = new GoogleAuthorizationCodeRequestUrl(CLIENT_ID, REDIRECT_URI, SCOPES).build();
			FacesContext.getCurrentInstance().getExternalContext().redirect(authorizationUrl);
		} catch (IOException e) {
			WritingLog.gravarLog(e.getMessage());
		}
	}

	public static String getCLIENT_ID() {
		return CLIENT_ID;
	}

	public static void setCLIENT_ID(String cLIENT_ID) {
		CLIENT_ID = cLIENT_ID;
	}

	public static String getCLIENT_SECRET() {
		return CLIENT_SECRET;
	}

	public static void setCLIENT_SECRET(String cLIENT_SECRET) {
		CLIENT_SECRET = cLIENT_SECRET;
	}

	public static String getREDIRECT_URI() {
		return REDIRECT_URI;
	}

	public static void setREDIRECT_URI(String rEDIRECT_URI) {
		REDIRECT_URI = rEDIRECT_URI;
	}

	public static List<String> getSCOPES() {
		return SCOPES;
	}

	public static void setSCOPES(List<String> sCOPES) {
		SCOPES = sCOPES;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
