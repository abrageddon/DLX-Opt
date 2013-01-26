package tests;

import java.io.File;
import java.io.FilenameFilter;

public class TestUtils {

	public static String[] listFiles(String dir, final String ext) {

		String[] testFiles = new File(dir).list(
				new FilenameFilter() {
					public boolean accept(File dir, String name) {
						return name.toLowerCase().endsWith(ext);
					}
				});
		return testFiles;
	}
	
}
