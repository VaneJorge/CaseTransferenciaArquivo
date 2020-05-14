package br.fileTransfer.server.rest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.media.multipart.FormDataParam;
import org.jets3t.service.S3ServiceException;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import br.fileTransfer.server.util.WritingLog;

@Path("/fileTransferService")
public class FileTransferService {

	private static List<DownloadService> downloads;
	private final String awsAccessKey = "AKIAIOVUPNZSILNLJ26Q";
	private final String awsSecretKey = "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";
	private final BasicAWSCredentials awsCreds = new BasicAWSCredentials(awsAccessKey, awsSecretKey);
	private final AmazonS3 s3 = AmazonS3ClientBuilder.standard()
			.withCredentials(new AWSStaticCredentialsProvider(awsCreds)).withRegion(Regions.SA_EAST_1).build();

	@PostConstruct
	private void init() {
	}

	// Feito
	@POST
	@Path("/upload")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.MULTIPART_FORM_DATA)
	public String uploadFile(@FormDataParam("fileInputStream") InputStream fileInputStream,
			@FormDataParam("fileName") String fileName) throws NoSuchAlgorithmException {

		if (fileInputStream == null || fileName == null)
			return "Arquivo não encontrado";

		File file = new File(fileName);

		try (FileOutputStream outputStream = new FileOutputStream(fileName)) {
			int read;
			byte[] bytes = new byte[1024];

			while ((read = fileInputStream.read(bytes)) != -1) {
				outputStream.write(bytes, 0, read);
			}

			s3.putObject("case-api-file-transfer", fileName, file);

			outputStream.close();

		} catch (IOException e) {
			WritingLog.gravarLog(e.getMessage());
		}

		file.delete();

		return "Upload feito com sucesso.";
	}

	@GET
	@Path("/carregarArquivos")
	@Produces(MediaType.APPLICATION_XML)
	public List<DownloadService> carregarArquivos() throws S3ServiceException, IOException {
		downloads = new ArrayList<DownloadService>();

		ListObjectsV2Result result = s3.listObjectsV2("case-api-file-transfer");
		List<S3ObjectSummary> objects = result.getObjectSummaries();

		for (S3ObjectSummary object : objects) {

			DownloadService down = new DownloadService();
			down.setNomeArquivo(object.getKey());

			downloads.add(down);
		}

		return downloads;
	}

	@POST
	@Path("/download")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.MULTIPART_FORM_DATA)
	public String uploadFile(@FormDataParam("fileName") String fileName) throws NoSuchAlgorithmException, IOException {

		try (FileOutputStream outputStream = new FileOutputStream(fileName)) {
			S3Object o = s3.getObject("case-api-file-transfer", fileName);
			S3ObjectInputStream s3is = o.getObjectContent();

			int read;
			byte[] bytes = new byte[1024];

			while ((read = s3is.read(bytes)) != -1) {
				outputStream.write(bytes, 0, read);
			}

			outputStream.close();
			s3is.close();

		} catch (AmazonServiceException e) {
			WritingLog.gravarLog(e.getMessage());
		} catch (FileNotFoundException e) {
			WritingLog.gravarLog(e.getMessage());
		} catch (IOException e) {
			WritingLog.gravarLog(e.getMessage());
		}

		return "Arquivo baixado com sucesso!";
	}
}