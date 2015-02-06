/*
 * Copyright (C) 2015 Jack Jiang(cngeeker.com) The DroidUIBuilder Project. 
 * All rights reserved.
 * Project URL:https://github.com/JackJiang2011/DroidUIBuilder
 * Version 1.0
 * 
 * Jack Jiang PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * LayoutUploader.java at 2015-2-6 16:12:02, original version by Jack Jiang.
 * You can contact author with jb2011@163.com.
 */
package org.droiddraw.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class LayoutUploader
{

	public static boolean upload(String host, int port, String layout)
		throws IOException, UnknownHostException
	{
		return upload(host, port, new ByteArrayInputStream(layout.getBytes()));
	}

	public static boolean upload(String host, int port, File f)
		throws IOException, UnknownHostException
	{
		return upload(host, port, new FileInputStream(f));
	}

	public static boolean upload(String host, int port, InputStream layout)
		throws IOException, UnknownHostException
	{
		InetAddress addr = InetAddress.getByName(host);
		return upload(addr, port, layout);
	}

	public static boolean upload(InetAddress addr, int port, InputStream layout)
		throws IOException
	{
		Socket s = new Socket(addr, port);
		OutputStream os = s.getOutputStream();
		BufferedReader in = new BufferedReader(new InputStreamReader(s
				.getInputStream()));
		String line = in.readLine();
		if (line == null || line.startsWith("Denied"))
		{
			return false;
		}

		byte[] buff = new byte[1024];
		int read = layout.read(buff);
		while (read != -1)
		{
			os.write(buff, 0, read);
			read = layout.read(buff);
		}
		os.flush();
		s.close();
		return true;
	}

	public static void main(String[] args)
		throws IOException, UnknownHostException
	{
		upload(args[0], Integer.parseInt(args[1]), new File(args[2]));
	}
}
