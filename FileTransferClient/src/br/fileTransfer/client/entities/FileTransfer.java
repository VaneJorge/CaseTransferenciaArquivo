package br.fileTransfer.client.entities;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.json.JSONObject;
import org.primefaces.model.UploadedFile;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

import br.fileTransfer.client.rest.DownloadService;
import br.fileTransfer.client.util.Constant;
import br.fileTransfer.client.util.WritingLog;

@ManagedBean
@RequestScoped
public class FileTransfer implements Serializable {

	private static final long serialVersionUID = 1L;
	private List<DownloadService> filesDownloadService;
	private static String CLIENT_ID = "286472118492-p8ilurhtbovi82ghua2f74h44hl57bmk.apps.googleusercontent.com";
	private static String CLIENT_SECRET = "c6KBrJaRYwIrPBlwEisGb-GF";
	private static String REDIRECT_URI = "http://localhost:8080/FileTransferClient/Transferencia.xhtml";
	private String email;
	private boolean logado;

	public FileTransfer() throws IOException {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		if (request.getParameter("code") != null)
			Constant.setCode(request.getParameter("code"));

		if (Constant.getCode() != null) {
			if (Constant.getEmail() == null && Constant.getId() == null)
				autorizacao();

			this.logado = true;
			carregarArquivos();
		} else {
			this.logado = false;
		}
	}

	public void autorizacao() throws IOException {
		try {
			GoogleTokenResponse response = new GoogleAuthorizationCodeTokenRequest(new NetHttpTransport(),
					new JacksonFactory(), CLIENT_ID, CLIENT_SECRET, Constant.getCode(), REDIRECT_URI).execute();

			Constant.setAccessToken(response.getAccessToken());

			Client client = ClientBuilder.newClient();
			WebTarget webTarget = client
					.target("https://www.googleapis.com/oauth2/v1/userinfo?access_token=" + response.getAccessToken());
			Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_XML);
			Response responseEmail = invocationBuilder.get();
			String json = responseEmail.readEntity(String.class);
			JSONObject jsonObj = new JSONObject(json);
			Constant.setId(jsonObj.getString("id"));
			Constant.setEmail(jsonObj.getString("email"));
		} catch (IOException e) {
			WritingLog.gravarLog(e.getMessage());
		}
	}

	public void carregarArquivos() {
		try {
			filesDownloadService = new ArrayList<DownloadService>();

			Client client = ClientBuilder.newClient();
			WebTarget webTarget = client
					.target("http://localhost:8080/FileTransferServer/rest/fileTransferService/carregarArquivos");
			Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_XML);
			Response response = invocationBuilder.get();

			FacesContext context = FacesContext.getCurrentInstance();
			if (response.getStatus() != Response.Status.OK.getStatusCode())
				context.addMessage(null, new FacesMessage("Erro", "Erro na conexão."));

			filesDownloadService = response.readEntity(new GenericType<List<DownloadService>>() {
			});

		} catch (Exception e) {
			WritingLog.gravarLog(e.getMessage());
		}
	}

	public void baixarArquivo(String nomeArquivo) {
		try {
			Client client = ClientBuilder.newBuilder().register(MultiPartFeature.class).build();

			FormDataBodyPart fileName = new FormDataBodyPart("fileName", nomeArquivo,
					MediaType.APPLICATION_OCTET_STREAM_TYPE);

			FormDataMultiPart formDataMultiPart = new FormDataMultiPart();
			FormDataMultiPart multipart = (FormDataMultiPart) formDataMultiPart.field("foo", "bar").bodyPart(fileName);

			WebTarget target = client
					.target("http://localhost:8080/FileTransferServer/rest/fileTransferService/download");
			Response response = target.request().post(Entity.entity(multipart, multipart.getMediaType()));

			FacesContext context = FacesContext.getCurrentInstance();
			if (response.getStatus() == Response.Status.OK.getStatusCode()) {
				context.addMessage(null, new FacesMessage("Sucesso", "O download foi concluído."));
			} else {
				context.addMessage(null, new FacesMessage("Erro", "Algo deu errado, tente novamente."));
			}

			formDataMultiPart.close();
			multipart.close();
		} catch (IOException e) {
			WritingLog.gravarLog(e.getMessage());
		}
	}

	// Feito
	public void upload(FileUploadEvent event) {
		try {
			UploadedFile uploadedFile = event.getFile();

			Client client = ClientBuilder.newBuilder().register(MultiPartFeature.class).build();

			FormDataBodyPart fileName = new FormDataBodyPart("fileName", uploadedFile.getFileName(),
					MediaType.APPLICATION_OCTET_STREAM_TYPE);
			FormDataBodyPart fileInputStream = new FormDataBodyPart("fileInputStream", uploadedFile.getInputstream(),
					MediaType.APPLICATION_OCTET_STREAM_TYPE);
			FormDataMultiPart formDataMultiPart = new FormDataMultiPart();
			FormDataMultiPart multipart = (FormDataMultiPart) formDataMultiPart.field("foo", "bar").bodyPart(fileName)
					.bodyPart(fileInputStream);

			WebTarget target = client
					.target("http://localhost:8080/FileTransferServer/rest/fileTransferService/upload");
			Response response = target.request().post(Entity.entity(multipart, multipart.getMediaType()));

			FacesContext context = FacesContext.getCurrentInstance();
			if (response.getStatus() == Response.Status.OK.getStatusCode()) {
				context.addMessage(null, new FacesMessage("Sucesso", "O upload foi feito com sucesso."));
			} else {
				context.addMessage(null, new FacesMessage("Erro", "Algo deu errado, tente novamente."));
			}

			formDataMultiPart.close();
			multipart.close();
		} catch (IOException e) {
			WritingLog.gravarLog(e.getMessage());
		}
	}

	public void logout() throws IOException {
		Constant.setEmail(null);
		Constant.setId(null);
		Constant.setCode(null);
		Constant.setAccessToken(null);
		this.logado = false;
		FacesContext.getCurrentInstance().getExternalContext().redirect("/FileTransferClient/");
	}

	public List<DownloadService> getFilesDownloadService() {
		return filesDownloadService;
	}

	public void setFilesDownloadService(List<DownloadService> filesDownloadService) {
		this.filesDownloadService = filesDownloadService;
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

	public String getEmail() {
		return email;
	}

	public boolean isLogado() {
		return logado;
	}

	public void setLogado(boolean logado) {
		this.logado = logado;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}