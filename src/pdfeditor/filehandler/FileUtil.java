package pdfeditor.filehandler;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class FileUtil {

	public static final JFileChooser FILE_CHOOSER;

	static {
		FILE_CHOOSER = new AdvancedFileChooser();
		FILE_CHOOSER.setFileFilter(new FileNameExtensionFilter("PDF File (*.pdf)", "pdf"));
	}

	public static String getExtension(File f) {
		String extension = null;
		String fileName = f.getName();
		int dotIndex = fileName.lastIndexOf('.');

		if (0 < dotIndex && dotIndex < fileName.length() - 1) {
			extension = fileName.substring(dotIndex + 1).toLowerCase();
		}

		return extension;
	}

	public static boolean hasExtension(File f, String... preferExtensions) {
		String extension = getExtension(f);

		if (extension == null) {
			return false;
		}

		boolean flag = false;
		for (int i = 0; i < preferExtensions.length; i ++) {
			flag = flag || extension.equals(preferExtensions[i]);
		}

		return flag;
	}

	public static String removeExtension(String path) {
		int dotIndex = path.lastIndexOf('.');
		if (0 < dotIndex && dotIndex < path.length() - 1) {
			return path.substring(0, path.lastIndexOf('.'));
		}
		return path;
	}

	public static String findUnusedName(String pathNoExtension, String extension) {
		if (!Files.exists(Paths.get(pathNoExtension + "." + extension))) {
			return pathNoExtension + "." + extension;

		}

		int i = 1;
		while (Files.exists(Paths.get(pathNoExtension + " (" + i + ")." + extension))) {
			i ++;
		}

		return pathNoExtension + " (" + i + ")." + extension;
	}
}
