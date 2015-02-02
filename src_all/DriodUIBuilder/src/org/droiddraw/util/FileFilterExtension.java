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
