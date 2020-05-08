package br.fileTransfer.server.util;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WritingLog {

	public static void gravarLog(String mensagem) {

		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss");
		Date date = new Date();
		String data = sdf.format(date).toString();

		try (FileOutputStream outputStream = new FileOutputStream("Log-" + data + ".txt")) {

			mensagem = "\n" + mensagem;
			byte[] bytesData = data.getBytes();
			byte[] bytesMensagem = mensagem.getBytes();

			outputStream.write(bytesData);
			outputStream.write(bytesMensagem);
			outputStream.close();

		} catch (Exception e) {
			gravarLog(e.getMessage());
		}
	}
}