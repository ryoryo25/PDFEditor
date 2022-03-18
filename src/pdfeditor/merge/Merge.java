package pdfeditor.merge;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;

import pdfeditor.filehandler.FileUtil;

public class Merge extends JDialog implements ActionListener {

	private JPanel menuPanel;
	private JButton addButton;

	private List<FileListElement> fileList;

	private JLabel labelDestFile;
	private JButton destSelectButton;
	private File destFile;

	private JButton mergeButton;
	private JButton cancelButton;

	public Merge(JFrame owner) {
		super(owner, true);
		this.setTitle("Merge Files");
		this.setLocationRelativeTo(owner);
		this.setResizable(false);

		this.menuPanel = new JPanel();
		this.menuPanel.setLayout(new GridLayout(0, 2, 5, 5));
		this.menuPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		FileListElement.elementCount = 0;
		fileList = new ArrayList<>();

		this.menuPanel.add(new JLabel("Add PDF file"));
		this.addButton = new JButton("+");
		this.addButton.addActionListener(this);
		this.menuPanel.add(this.addButton);

		// dest file selection
		JPanel labelDestFileSelect = new JPanel();
		labelDestFileSelect.setLayout(new GridLayout(1, 0, 5, 5));
		this.labelDestFile = new JLabel("None");
		labelDestFileSelect.add(new JLabel("Destination PDF:"));
		labelDestFileSelect.add(this.labelDestFile);
		this.menuPanel.add(labelDestFileSelect);
		this.destSelectButton = new JButton("Select PDF file...");
		this.destSelectButton.addActionListener(this);
		this.menuPanel.add(this.destSelectButton);

		this.mergeButton = new JButton("Merge!");
		this.mergeButton.setEnabled(false);
		this.mergeButton.addActionListener(this);
		this.menuPanel.add(this.mergeButton);

		this.cancelButton = new JButton("Cancel");
		this.cancelButton.setPreferredSize(new Dimension(200, this.cancelButton.getPreferredSize().height));
		this.cancelButton.addActionListener(this);
		this.menuPanel.add(this.cancelButton);

		this.add(this.menuPanel);
		this.pack();
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.addButton) {
			int state = FileUtil.FILE_CHOOSER.showOpenDialog(this);

			if (state == JFileChooser.APPROVE_OPTION) {
				File selectedFile = FileUtil.FILE_CHOOSER.getSelectedFile();
				if (selectedFile == null) {
					this.dispose();
					return;
				}

				System.out.println("Selected: " + selectedFile.getAbsolutePath());

				fileList.add(new FileListElement(this, this.menuPanel, selectedFile));

				if (!this.mergeButton.isEnabled() && fileList.size() >= 2 && !this.labelDestFile.getText().equals("None")) {
					this.mergeButton.setEnabled(true);
				}
			}
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

				if (!this.mergeButton.isEnabled() && fileList.size() >= 2) {
					this.mergeButton.setEnabled(true);
				}
			}
		}

		if (e.getSource() == this.mergeButton) {

			PDFMergerUtility pdfMerger = new PDFMergerUtility();

			fileList.forEach(f -> {
				try {
					pdfMerger.addSource(f.getSelectedFile());
				} catch (FileNotFoundException e1) {
					System.out.println("Error: file not found");
					this.dispose();
				}
			});

			pdfMerger.setDestinationFileName(this.destFile.getAbsolutePath());

			try {
				pdfMerger.mergeDocuments(MemoryUsageSetting.setupMainMemoryOnly());
			} catch (IOException e1) {
				System.out.println("Failed to merge PDF files");
				this.dispose();
			}

			this.dispose();

			return;
		}

		if (e.getSource() == this.cancelButton) {
			this.dispose();
			return;
		}
	}
}