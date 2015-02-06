/*
 * Copyright (C) 2015 Jack Jiang(cngeeker.com) The DroidUIBuilder Project. 
 * All rights reserved.
 * Project URL:https://github.com/JackJiang2011/DroidUIBuilder
 * Version 1.0
 * 
 * Jack Jiang PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * Utils.java at 2015-2-6 16:12:00, original version by Jack Jiang.
 * You can contact author with jb2011@163.com.
 */
package com.jb2011.drioduibuilder.util;

import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.TexturePaint;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import org.droiddraw.MainPane;

public class Utils
{
	/**
	 * 图形绘制反走样设置.
	 *
	 * @param g2 the g2
	 * @param antiAliasing 是否反走样
	 */
	public static void setTextAntiAliasing(Graphics2D g2 ,boolean antiAliasing)
	{
		if(antiAliasing)
			g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING
					, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		else
			g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING
					, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
	}
	
	/**
	 * 图形绘制反走样设置.
	 *
	 * @param g2 the g2
	 * @param antiAliasing 是否反走样
	 */
	public static void setAntiAliasing(Graphics2D g2 ,boolean antiAliasing)
	{
		if(antiAliasing)
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING
					, RenderingHints.VALUE_ANTIALIAS_ON);
		else
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING
					, RenderingHints.VALUE_ANTIALIAS_OFF);
	}
	
	/**
	 * 获得指定高度内的指定字体，垂直居中绘制文本时的Y直点坐标.
	 * 
	 * @param f
	 * @param sumHeight 字体所有区域的总高度（居中就是相对于它而言的）
	 * @return
	 */
	public static int getFontCenterY(Font f, int sumHeight)
	{
		FontMetrics fontMetrics = Toolkit.getDefaultToolkit().getFontMetrics(f); 
		return sumHeight / 2 
			+ (fontMetrics.getAscent() - fontMetrics.getDescent()) / 2;
	}
	
	/**
	 * 获得一个可按指定图片进行纹理填充方式的对象，利用此对象可实现图片填充背景效果。
	 * 
	 * <pre>
	 * 示例如下（在这个例子里将实现一个以指定图片填充效果为背景的面板对象）：
	 * private FixedLayoutPane inputPane = new FixedLayoutPane() {
	 * 	//给它弄一个图片平铺的背景
	 * 	private TexturePaint paint = createTexturePaint(LaunchIconFactory.getInstance()
	 * 			.getImage("/login_background.png").getImage());
	 * 	//重写本方法实现图片平铺的背景 
	 * 	protected void paintComponent(Graphics g) {
	 * 		super.paintComponent(g);
	 * 		Graphics2D g2d = (Graphics2D) g;
	 * 		g2d.setPaint(paint);
	 * 		g2d.fillRect(0, 0, this.getWidth(), this.getHeight());
	 * 	}
	 * };
	 * </pre>
	 * @param image 填充图片，该图片一宽1像素高N像素（据这1像素宽进行重复填充即可达到目的）
	 * @return
	 */
    public static TexturePaint createTexturePaint(Image image) 
    {
        int imageWidth = image.getWidth(null);
        int imageHeight = image.getHeight(null);
        BufferedImage bi = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bi.createGraphics();
        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();
        return new TexturePaint(bi, new Rectangle(0, 0, imageWidth, imageHeight));
    }
    
	/**
	 * 获得字符串的像素宽度
	 * @param fm
	 * @param str
	 * @return
	 * @see FontMetrics#stringWidth(String)
	 */
	public static int getStrPixWidth(FontMetrics fm,String str)
    {
    	return fm.stringWidth(str+"");
    }
	/**
	 * 获得字符串的像素宽度
	 * @param f
	 * @param str
	 * @return
	 * @see #getStrPixWidth(FontMetrics, String)
	 */
	public static int getStrPixWidth(Font f,String str)
    {
    	return getStrPixWidth(Toolkit.getDefaultToolkit().getFontMetrics(f),str);
    	//*** 另一种比较麻烦的获得字符串的像素宽度的方法
    	/*if(str==null || str.length() < 1 )
			return 0;
		
		 * 不需要设置第三个参数值为true。有如下原因：
		 * 1、求字符串长度时，增加其计算时间
		 * 2、目前的Graphics对象在绘制文本时，也是一整数来度量。
		 
		FontRenderContext frc =  new FontRenderContext(font.getTransform(),
				false, false);
		return (int)(0.5F + new TextLayout(str, font, frc).getAdvance());*/
    }
    /**
     * 获得字符串的像素高度
     * @param fm
     * @return
     * @see FontMetrics#getHeight()
     */
	public static int getStrPixHeight(FontMetrics fm)
    {
    	return fm.getHeight();
    }
    /**
     * 获得字符串的像素高度
     * @param f
     * @return
     * @see #getStrPixHeight(Font)
     */
	public static int getStrPixHeight(Font f)
    {
    	return getStrPixHeight(Toolkit.getDefaultToolkit().getFontMetrics(f));
    }
	
	/**
	 * 获得屏幕除去工具栏外的显示区大小.<br>
	 * 它与 Toolkit#getDefaultToolkit()#getScreenSize()的区别是后者取的是整屏幕大小（不管什么工具栏什么的）.
	 * @see GraphicsEnvironment#getLocalGraphicsEnvironment()#getMaximumWindowBounds()
	 * @return
	 */
	public static Rectangle getScreenBounds()
	{
		return GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
	}
	/**
	 * 取整个屏幕尺寸.
	 * @see Toolkit#getDefaultToolkit()#getScreenSize()
	 */
	public static Dimension getScreenSize()
	{
		return Toolkit.getDefaultToolkit().getScreenSize();
	}
	/**
	 * 获取整个屏幕宽.
	 * @see Toolkit#getDefaultToolkit()#getScreenSize()#getWidth()
	 */
	public static int getScreenWidth()
	{
		return (int)getScreenSize().getWidth();
	}
	/**
	 * 获取整个屏幕高.
	 * @see Toolkit#getDefaultToolkit()#getScreenSize()#getHeight()
	 */
	public static int getScreenHeight()
	{
		return (int)getScreenSize().getHeight();
	}
	/**
	 * 对照屏幕大小，按比例获取尺寸
	 * @param xd 横向比例
	 * @param yd 纵向比例
	 * @return Dimenion  相对尺寸
	 * @see #getScreenSize()
	 */
	public static Dimension getSizeWithScreen(double xd,double yd)
	{
		Dimension d = getScreenSize();
		double width = d.getWidth()*xd;
		double height = d.getHeight()*yd;
		d = new Dimension((int)width,(int)height);
		return d;
	}
	
	public static void fullScreen(Window w)
	{
		GraphicsEnvironment env = GraphicsEnvironment
				.getLocalGraphicsEnvironment();
		for (GraphicsDevice device : env.getScreenDevices())
		{
			if (device.isFullScreenSupported())
			{
				device.setFullScreenWindow(w);
				return;
			}
		}
		MainPane.getTipCom().warn("No supported device for fullscreen mode.");
	}
	
	/**
	 * Run an command line.
	 * 
	 * @param cmd
	 * @param workingDirectory
	 * @return
	 * @throws IOException
	 */
	public static boolean run(String[] cmd, File workingDirectory)
		throws IOException
	{
		Process p = Runtime.getRuntime().exec(cmd, null, workingDirectory);
		try
		{
			int ret = p.waitFor();
			if (ret != 0)
			{
				InputStream input = p.getErrorStream();
				byte[] buff = new byte[4096];
				for (int i = input.read(buff); i != -1; i = input.read(buff))
				{
					System.err.write(buff, 0, i);
				}
				input = p.getInputStream();
				for (int i = input.read(buff); i != -1; i = input.read(buff))
				{
					System.out.write(buff, 0, i);
				}
				return false;
			}
		}
		catch (InterruptedException ex)
		{
		}
		return true;
	}
	
	/**
	 * 打开一个远程网页.
	 * 
	 * <p>
	 * 此java1.6里的方法完全可解决原作者使用的打开网页的方法（ 他使用了101KB的BrowserLauncher2-1_3.jar）。
	 * 
	 * @param wwwPageURL
	 *            要打开的网址
	 */
	public static void browseURL(String wwwPageURL)
	{
		try
		{
			Desktop.getDesktop().browse(new URI(wwwPageURL));
		}
		catch (Exception e2)
		{
			// e2.printStackTrace();
			MainPane.getTipCom().warn(e2);
		}
	}

}
