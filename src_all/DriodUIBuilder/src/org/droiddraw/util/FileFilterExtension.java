/*
 * Copyright (C) 2015 Jack Jiang(cngeeker.com) The DroidUIBuilder Project. 
 * All rights reserved.
 * Project URL:https://github.com/JackJiang2011/DroidUIBuilder
 * Version 1.0
 * 
 * Jack Jiang PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * FileFilterExtension.java at 2015-2-6 16:11:59, original version by Jack Jiang.
 * You can contact author with jb2011@163.com.
 */
package org.droiddraw.util;

/**
 * 
 * @author SubOptimal
 */
import java.io.File;

import javax.swing.filechooser.FileFilter;

final public class FileFilterExtension extends FileFilter
{

	private String fileExtension;
	private String filterDescription;

	public FileFilterExtension(String extension, String description)
	{
		this.fileExtension = extension;
		this.filterDescription = description;
	}

	public boolean accept(File file)
	{
		return file.isDirectory() || file.getName().endsWith(fileExtension);
	}

	public String getDescription()
	{
		return filterDescription;
	}

	public String getExtension()
	{
		return fileExtension;
	}
}
