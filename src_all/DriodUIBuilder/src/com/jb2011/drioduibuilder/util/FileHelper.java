package com.jb2011.drioduibuilder.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileHelper
{
	public static void copy(File from, File to)
		throws IOException
	{
		FileInputStream fis = new FileInputStream(from);
		FileOutputStream fos = new FileOutputStream(to);

		byte[] buffer = new byte[4096];
		int rd = fis.read(buffer);
		while (rd != -1)
		{
			fos.write(buffer, 0, rd);
			rd = fis.read(buffer);
		}
		fos.flush();
		fos.close();
	}
}
