package pdfeditor.filehandler;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

public class AdvancedFileChooser extends JFileChooser {

	@Override
	public void approveSelection() {
		if (this.getDialogType() != SAVE_DIALOG) {
			super.approveSelection();
			return;
		}

		File file = this.getSelectedFile();
		FileFilter filter = this.getFileFilter();

		boolean showAlert = file.exists();
		String existExtension = null;

		if (!showAlert && filter instanceof FileNameExtensionFilter) {
			FileNameExtensionFilter extensionFilter = (FileNameExtensionFilter) filter;
			for (String extension : extensionFilter.getExtensions()) {
				if ((new File(file + "." + extension)).exists()) {
					existExtension = extension;
					showAlert = true;
				}
			}
		}

		if (showAlert) {
			String msg = String.format("<html>%s already exists.<br>Do you want to replace it?", file.getAbsolutePath() + (existExtension == null ? "" : ("." + existExtension)));
			int comfirm = JOptionPane.showConfirmDialog(this, msg, "Save As", JOptionPane.YES_NO_OPTION);
			if (comfirm != JOptionPane.YES_OPTION) {
				return;
			}
		}

		super.approveSelection();
	}

}
