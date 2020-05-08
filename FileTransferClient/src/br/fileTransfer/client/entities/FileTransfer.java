package br.fileTransfer.client.entities;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
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
import org.primefaces.model.UploadedFile;

import br.fileTransfer.client.rest.DownloadService;
import br.fileTransfer.client.util.WritingLog;

@ManagedBean
@RequestScoped
public class FileTransfer implements Serializable {

	private static final long serialVersionUID = 1L;
	private List<DownloadService> filesDownloadService;

	public FileTransfer() throws IOException {
		carregarArquivos();
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

	public void baixarArquivo(String nomeArquivo) throws IOException {

		Client client = ClientBuilder.newBuilder().register(MultiPartFeature.class).build();

		FormDataBodyPart fileName = new FormDataBodyPart("fileName", nomeArquivo,
				MediaType.APPLICATION_OCTET_STREAM_TYPE);

		FormDataMultiPart formDataMultiPart = new FormDataMultiPart();
		FormDataMultiPart multipart = (FormDataMultiPart) formDataMultiPart.field("foo", "bar").bodyPart(fileName);

		WebTarget target = client.target("http://localhost:8080/FileTransferServer/rest/fileTransferService/download");
		Response response = target.request().post(Entity.entity(multipart, multipart.getMediaType()));

		FacesContext context = FacesContext.getCurrentInstance();
		if (response.getStatus() == Response.Status.OK.getStatusCode()) {
			context.addMessage(null, new FacesMessage("Sucesso", "O download foi concluído."));
		} else {
			context.addMessage(null, new FacesMessage("Erro", "Algo deu errado, tente novamente."));
		}

		formDataMultiPart.close();
		multipart.close();
	}

	// Feito
	public void upload(FileUploadEvent event) throws IOException {
		UploadedFile uploadedFile = event.getFile();

		Client client = ClientBuilder.newBuilder().register(MultiPartFeature.class).build();

		FormDataBodyPart fileName = new FormDataBodyPart("fileName", uploadedFile.getFileName(),
				MediaType.APPLICATION_OCTET_STREAM_TYPE);
		FormDataBodyPart fileInputStream = new FormDataBodyPart("fileInputStream", uploadedFile.getInputstream(),
				MediaType.APPLICATION_OCTET_STREAM_TYPE);
		FormDataMultiPart formDataMultiPart = new FormDataMultiPart();
		FormDataMultiPart multipart = (FormDataMultiPart) formDataMultiPart.field("foo", "bar").bodyPart(fileName)
				.bodyPart(fileInputStream);

		WebTarget target = client.target("http://localhost:8080/FileTransferServer/rest/fileTransferService/upload");
		Response response = target.request().post(Entity.entity(multipart, multipart.getMediaType()));

		FacesContext context = FacesContext.getCurrentInstance();
		if (response.getStatus() == Response.Status.OK.getStatusCode()) {
			context.addMessage(null, new FacesMessage("Sucesso", "O upload foi feito com sucesso."));
		} else {
			context.addMessage(null, new FacesMessage("Erro", "Algo deu errado, tente novamente."));
		}

		formDataMultiPart.close();
		multipart.close();
	}

	public List<DownloadService> getFilesDownloadService() {
		return filesDownloadService;
	}

	public void setFilesDownloadService(List<DownloadService> filesDownloadService) {
		this.filesDownloadService = filesDownloadService;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}