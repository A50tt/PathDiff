
package com.a50tt.main;

import java.io.File;
import javax.swing.JLabel;

public class DiffHandler {

	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_BLACK = "\u001B[30m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_YELLOW = "\u001B[33m";

	public static File mainDir = null;
	public static File comparingDir = null;
	public static String currentLastDirectory = "";
	public static String otherLastDirectory = "";
	public static JLabel textLog;

	public static StringBuilder str;

	public static void start(File _mainDir, File _comparingDir, JLabel _textLog) {
		textLog = _textLog;
		mainDir = _mainDir;
		comparingDir = _comparingDir;
		str = new StringBuilder(textLog.getText());
		currentLastDirectory = mainDir.toString().substring(mainDir.toString().lastIndexOf("\\") + 1);
		otherLastDirectory = comparingDir.toString().substring(comparingDir.toString().lastIndexOf("\\") + 1);
		DiffHandler.pathsDiff(_mainDir, _comparingDir);
	}

	public static void pathsDiff(File dir1, File dir2) {
		
		String mainPathDir1 = dir1.toString();
		String mainPathDir2 = dir2.toString();
		for (File f : dir1.listFiles()) {
			if (f.isFile()) {
				File f2 = new File(mainPathDir2.toString() + f.toString().replace(mainPathDir1, ""));
				if (!f2.exists()) {
					str.delete(str.indexOf("</html>"), str.length());
					str.append("<font color='red'>FILE - only exists in " + currentLastDirectory + " --> </font>" + f.toString() + "</font><br></html>");
					textLog.setText(str.toString());
				} else if (f.length() != f2.length()) {
					str.delete(str.indexOf("</html>"), str.length());
					str.append("<font color='orange'>FILE - size is different." + " --> </font>" + currentLastDirectory + " = " + f.length() + " B &nbsp;&nbsp;||&nbsp;&nbsp; " + otherLastDirectory + " = " + f2.length() + " B<br></html>");
					textLog.setText(str.toString());
				}
			} else {
				File d2 = new File(mainPathDir2.toString() + f.toString().replace(mainPathDir1, ""));
				if (!d2.exists()) {
					str.delete(str.indexOf("</html>"), str.length());
					str.append("<font color='red'>DIR  - only exists in " + currentLastDirectory + " --> </font>" + f.toString() + "<br></html>");
					textLog.setText(str.toString());
				} else {
					pathsDiff(f, d2);
				}
			}
		}
	}
}
