@echo off
cd /d %~dp0
javac -sourcepath src -d bin src/pdfeditor/PDFEditorMain.java
jar cvfm PDFEditor.jar META-INF/MANIFEST.MF -C bin .
pause