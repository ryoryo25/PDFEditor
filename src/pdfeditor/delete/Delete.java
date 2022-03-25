package pdfeditor.delete;

import javax.swing.JDialog;
import javax.swing.JFrame;

public class Delete extends JDialog {

	public Delete(JFrame owner) {
		super(owner, true);
		this.setTitle("Delete Pages");
		this.setLocationRelativeTo(owner);
		this.setResizable(false);

		this.setVisible(true);
	}
}
