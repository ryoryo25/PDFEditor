package pdfeditor.merge;

import java.io.File;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class FileListElement {

	public static int elementCount = 0;

	private File selectedFile;

	public FileListElement(JDialog frame, JPanel parent, File selectedFile) {
		this.selectedFile = selectedFile;

		parent.add(new JLabel("PDF " + (elementCount + 1) + ":"), 2 * elementCount);
		parent.add(new JLabel(this.selectedFile.getName()), 2 * elementCount + 1);
		elementCount ++;

		// re-render window
		parent.revalidate();
		int w = frame.getSize().width;
		frame.pack();
		frame.setSize(w, frame.getSize().height);
	}

	public File getSelectedFile() {
		return selectedFile;
	}
}
