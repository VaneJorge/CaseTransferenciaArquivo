package br.fileTransfer.client.entities;

import java.io.File;

import org.primefaces.model.StreamedContent;

public class Download {
	private String nomeArquivo;
	private File arquivo;
	private StreamedContent arquivoStream;

	public String getNomeArquivo() {
		return nomeArquivo;
	}

	public void setNomeArquivo(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}

	public File getArquivo() {
		return arquivo;
	}

	public void setArquivo(File arquivo) {
		this.arquivo = arquivo;
	}

	public StreamedContent getArquivoStream() {
		return arquivoStream;
	}

	public void setArquivoStream(StreamedContent arquivoStream) {
		this.arquivoStream = arquivoStream;
	}
}