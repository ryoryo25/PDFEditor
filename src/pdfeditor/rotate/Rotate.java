package pdfeditor.rotate;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.pdfbox.pdmodel.PDDocument;

import pdfeditor.filehandler.FileUtil;

public class Rotate extends JDialog implements ActionListener, ChangeListener, FocusListener {

	private static final String[] ROTATION_NAMES = { "90 deg. CW", "180 deg. CW", "90 deg. CCW" };
	private static final int[] ROTATION_ANGLES = { 90, 180, 270 };

	private static final String CUSTOM_PAGE_PLACEHOLDER = "Ex: 1-3 8 11-13 (Space separated)";

	private JLabel labelSelectedFile;
	private JButton selectButton;
	private File selectedFile;
	private PDDocument selectedPDF;

	private JComboBox<String> rotationAngleButton;

	private JRadioButton allPages;
	private JRadioButton customPages;
	private JTextField customPageInput;

	private JLabel labelDestFile;
	private JButton destSelectButton;
	private File destFile;

	private JButton rotateButton;
	private JButton cancelButton;

	public Rotate(JFrame owner) {
		super(owner, true); // modal
		this.setTitle("Rotate Pages");
		this.setLocationRelativeTo(owner);
		this.setResizable(false);

		JPanel menu = new JPanel();
		menu.setLayout(new GridLayout(0, 2, 5, 5));
		menu.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		// File selection
		JPanel labelSelectFile = new JPanel();
		labelSelectFile.setLayout(new GridLayout(1, 0, 5, 5));
		this.labelSelectedFile = new JLabel("None");
		labelSelectFile.add(new JLabel("Selected PDF:"));
		labelSelectFile.add(this.labelSelectedFile);
		menu.add(labelSelectFile);
		this.selectButton = new JButton("Select PDF file...");
		this.selectButton.addActionListener(this);
		menu.add(this.selectButton);

		// Rotation angle selection
		JLabel labelRotationAngleButton = new JLabel("Rotation Angle");
		menu.add(labelRotationAngleButton);
		this.rotationAngleButton = new JComboBox<>(ROTATION_NAMES);
		this.rotationAngleButton.setEnabled(false);
		menu.add(this.rotationAngleButton);

		// Page selection
		JLabel labelSelectPages = new JLabel("Page");
		menu.add(labelSelectPages);
		this.allPages = new JRadioButton("All pages", true);
		this.customPages = new JRadioButton("Custom");
		this.allPages.setEnabled(false);
		this.customPages.setEnabled(false);
		this.allPages.addChangeListener(this);
		this.customPages.addChangeListener(this);
		menu.add(this.allPages);
		menu.add(new JLabel()); // blank
		menu.add(this.customPages);
		menu.add(new JLabel()); // blank

		ButtonGroup pageSelectGroup = new ButtonGroup();
		pageSelectGroup.add(this.allPages);
		pageSelectGroup.add(this.customPages);

		// Custom page selection text input
		this.customPageInput = new JTextField(CUSTOM_PAGE_PLACEHOLDER);
		this.customPageInput.setEnabled(false);
		this.customPageInput.setForeground(Color.GRAY);
		this.customPageInput.addFocusListener(this);
		menu.add(this.customPageInput);

		// dest file selection
		JPanel labelDestFileSelect = new JPanel();
		labelDestFileSelect.setLayout(new GridLayout(1, 0, 5, 5));
		this.labelDestFile = new JLabel("None");
		labelDestFileSelect.add(new JLabel("Destination PDF:"));
		labelDestFileSelect.add(this.labelDestFile);
		menu.add(labelDestFileSelect);
		this.destSelectButton = new JButton("Select PDF file...");
		this.destSelectButton.addActionListener(this);
		menu.add(this.destSelectButton);

		// Run rotation button
		this.rotateButton = new JButton("Rotate!");
		this.rotateButton.setEnabled(false);
		this.rotateButton.addActionListener(this);
		menu.add(this.rotateButton);

		// Cancel button
		this.cancelButton = new JButton("Cancel");
		this.cancelButton.setPreferredSize(new Dimension(200, this.cancelButton.getPreferredSize().height));
		this.cancelButton.addActionListener(this);
		menu.add(this.cancelButton);

		this.add(menu);
		this.pack();
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.selectButton) {
			int state = FileUtil.FILE_CHOOSER.showOpenDialog(this);

			if (state == JFileChooser.APPROVE_OPTION) {
				this.selectedFile = FileUtil.FILE_CHOOSER.getSelectedFile();
				System.out.println("Selected: " + this.selectedFile.getAbsolutePath());

				this.labelSelectedFile.setText(this.selectedFile == null ? "None" : this.selectedFile.getName());

				if (this.selectedFile == null) {
					this.rotationAngleButton.setEnabled(false);
					this.allPages.setEnabled(false);
					this.customPages.setEnabled(false);
					this.customPageInput.setEnabled(false);
					this.rotateButton.setEnabled(false);
				} else {
					this.rotationAngleButton.setEnabled(true);
					this.allPages.setEnabled(true);
					this.customPages.setEnabled(true);
					if (this.customPages.isSelected()) {
						this.customPageInput.setEnabled(true);
					}
					if (this.destFile != null) {
						this.rotateButton.setEnabled(true);
					}

					try {
						this.selectedPDF = PDDocument.load(this.selectedFile);
					} catch (IOException e1) {
						System.out.println("Error: loading PDF file");
						this.dispose();
					}
				}
			}

			return;
		}

		if (e.getSource() == this.destSelectButton) {
			int state = FileUtil.FILE_CHOOSER.showSaveDialog(this);

			if (state == JFileChooser.APPROVE_OPTION) {
				this.destFile = FileUtil.FILE_CHOOSER.getSelectedFile();
				if (this.destFile == null) {
					this.dispose();
					return;
				}

				if (!FileUtil.hasExtension(this.destFile, "pdf")) {
					this.destFile = new File(this.destFile + ".pdf");
				}

				System.out.println("Selected: " + this.destFile.getAbsolutePath());

				this.labelDestFile.setText(this.destFile.getName());

				if (!this.rotateButton.isEnabled() && this.selectedFile != null) {
					this.rotateButton.setEnabled(true);
				}
			}
		}

		if (e.getSource() == this.rotateButton) {
			if (this.selectedPDF == null) {
				return;
			}

			if (this.allPages.isSelected()) {
				this.selectedPDF.getPages().forEach(p -> p.setRotation(ROTATION_ANGLES[this.rotationAngleButton.getSelectedIndex()]));
			} else {
				Set<Integer> pages = parseCustomPages(this.customPageInput.getText());
				pages.forEach(p -> {
					if (p < this.selectedPDF.getNumberOfPages()) {
						this.selectedPDF.getPage(p).setRotation(ROTATION_ANGLES[this.rotationAngleButton.getSelectedIndex()]);
					}
				});
			}

			try {
				this.selectedPDF.save(this.destFile);
				this.selectedPDF.close();
			} catch (IOException e1) {
				System.out.println("Failed to save file!");
				this.dispose();
			}
			this.dispose();

			return;
		}

		if (e.getSource() == this.cancelButton) {
			if (this.selectedPDF != null) {
				try {
					this.selectedPDF.close();
				} catch (IOException e1) {}
			}
			this.dispose();
			return;
		}
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		if (e.getSource() == this.customPages) {
			if (this.customPages.isSelected()) {
				this.customPageInput.setEnabled(true);
			} else {
				this.customPageInput.setEnabled(false);
			}
		}
	}

	@Override
	public void focusGained(FocusEvent e) {
		if (e.getSource() == this.customPageInput && this.customPageInput.getText().equals(CUSTOM_PAGE_PLACEHOLDER)) {
			this.customPageInput.setText("");
			this.customPageInput.setForeground(Color.BLACK);
		}
	}

	@Override
	public void focusLost(FocusEvent e) {
		if (e.getSource() == this.customPageInput && this.customPageInput.getText().isEmpty()) {
			if (this.selectedPDF != null) {
				int p = this.selectedPDF.getNumberOfPages();
				this.customPageInput.setText(p == 1 ? "1" : "1-" + p);
			} else {
				this.customPageInput.setText(CUSTOM_PAGE_PLACEHOLDER);
				this.customPageInput.setForeground(Color.GRAY);
			}
		}
	}

	private static Set<Integer> parseCustomPages(String input) {
		Set<Integer> pages = new HashSet<>();

		if (!input.matches("((([0-9]+)-([0-9]+))|([0-9]+)| )+")) {
			System.out.println("Not matched pattern!");
			return pages;
		}

		String[] splitted = input.split(" ");

		for (String pp : splitted) {
			if (pp.matches("([0-9]+)-([0-9]+)")) {
				int start = Integer.parseInt(pp.substring(0, pp.indexOf('-')));
				int end = Integer.parseInt(pp.substring(pp.indexOf('-') + 1, pp.length()));

				if (start > end) {
					int tmp = start;
					start = end;
					end = tmp;
				}

				for (int p = start; p <= end; p ++) {
					pages.add(p - 1);
				}
			} else if (pp.matches("[0-9]+")) {
				pages.add(Integer.parseInt(pp) - 1);
			} else {
				System.out.println("Skipped");
			}
		}

		return pages;
	}
}