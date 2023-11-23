package com.example.backend.utils;

import java.awt.*;
import java.io.File;

public class FileUtil {
	public static void openFile(String filePath) {

		try {
			File file = new File(filePath);

			if (file.exists()) {

				if (Desktop.isDesktopSupported()) {
					Desktop.getDesktop().open(file);
				}
				else {
					System.out.println("AWT Desktop is not supported!");
				}
			}
			else {
				System.out.println("Output file is not exists!");
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void makeDirectory(String dirPath) {

		File directory = new File(dirPath);

		if (!directory.exists()) {

			System.out.println("Creating directory :: " + dirPath);

			directory.mkdirs();
		}
	}
}
