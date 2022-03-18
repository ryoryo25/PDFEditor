package pdfeditor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

import pdfeditor.merge.Merge;
import pdfeditor.rotate.Rotate;

public class PDFEditorMain extends JFrame {

	public static final String VERSION = "1.0";

	public PDFEditorMain() {
		this.setBackground(Color.GRAY);
		this.setTitle("PDF Editor v" + VERSION);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int w = screenSize.width;
		int h = screenSize.height;
		this.setLocation(w / 7, h / 5);

		JPanel menu = new JPanel();
		menu.setLayout(new GridLayout(0, 1, 5, 5));
		menu.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		OperationButton rotateButton = new OperationButton("Rotate", e -> new Rotate(this));
		OperationButton mergeButton = new OperationButton("Merge", e -> new Merge(this));
		menu.add(rotateButton);
		menu.add(mergeButton);

		this.add(menu);
		this.pack();
		this.setVisible(true);
	}

	public static void main(String[] args) throws Exception {
		new PDFEditorMain();
	}
}