/*
 * Copyright (C) 2015 Jack Jiang(cngeeker.com) The DroidUIBuilder Project. 
 * All rights reserved.
 * Project URL:https://github.com/JackJiang2011/DroidUIBuilder
 * Version 1.0
 * 
 * Jack Jiang PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * FileHelper.java at 2015-2-6 16:12:03, original version by Jack Jiang.
 * You can contact author with jb2011@163.com.
 */
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
