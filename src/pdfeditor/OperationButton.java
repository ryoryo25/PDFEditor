package pdfeditor;

import java.awt.Dimension;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class OperationButton extends JButton {

	public OperationButton(String title, ActionListener action) {
		super(title);
		this.setPreferredSize(new Dimension(220, 30));
		this.addActionListener(action);
	}
}
