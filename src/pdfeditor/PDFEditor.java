package pdfeditor;

import java.io.File;

import org.apache.pdfbox.pdmodel.PDDocument;

public class PDFEditor {

	public static void main(String[] args) throws Exception {
		PDDocument document = PDDocument.load(new File("./input.pdf"));

		document.getPages().forEach(p -> p.setRotation(90));

		document.save(new File("./output.pdf"));
		document.close();
	}
}