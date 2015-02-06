/*
 * Copyright (C) 2015 Jack Jiang(cngeeker.com) The DroidUIBuilder Project. 
 * All rights reserved.
 * Project URL:https://github.com/JackJiang2011/DroidUIBuilder
 * Version 1.0
 * 
 * Jack Jiang PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * FileCopier.java at 2015-2-6 16:12:03, original version by Jack Jiang.
 * You can contact author with jb2011@163.com.
 */
package org.droiddraw.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileCopier
{
	public static void copy(File file, File dir, boolean overwrite)
		throws IOException
	{
		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(
				file));
		File out = new File(dir, file.getName());
		if (out.exists() && !overwrite)
		{
			throw new IOException("File: " + out + " already exists.");
		}
		FileOutputStream fos = new FileOutputStream(out, false);

		byte[] block = new byte[4096];
		int read = bis.read(block);
		while (read != -1)
		{
			fos.write(block, 0, read);
			read = bis.read(block);
		}
	}
}
