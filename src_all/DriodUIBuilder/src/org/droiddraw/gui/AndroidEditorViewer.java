/*
 * Copyright (C) 2015 Jack Jiang(cngeeker.com) The DroidUIBuilder Project. 
 * All rights reserved.
 * Project URL:https://github.com/JackJiang2011/DroidUIBuilder
 * Version 1.0
 * 
 * Jack Jiang PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * AndroidEditorViewer.java at 2015-2-6 16:12:02, original version by Jack Jiang.
 * You can contact author with jb2011@163.com.
 */
package org.droiddraw.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.TexturePaint;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;

import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.droiddraw.AndroidEditor;
import org.droiddraw.MainPane;
import org.droiddraw.resource.FontObjectFactory;
import org.droiddraw.widget.Layout;
import org.droiddraw.widget.Widget;

import com.jb2011.drioduibuilder.util.Utils;

public class AndroidEditorViewer extends JPanel implements DropTargetListener,
		ChangeListener
{
//	Dimension d;
	AndroidEditor app;
	ViewerListener vl;
	Image img;
	Image back;

	DropTarget dt;

	public AndroidEditorViewer(AndroidEditor app, MainPane ddp, Image img)
	{
		this.app = app;
		// this.d = new Dimension(app.getScreenX(),app.getScreenY());
		vl = new ViewerListener(app, ddp, this);
		addMouseListener(vl);
		addMouseMotionListener(vl);
		addKeyListener(vl);
		app.addChangeListener(this);
		this.img = img;
//		this.d = new Dimension(480, 480);
		dt = new DropTarget(this, DnDConstants.ACTION_MOVE, this, true);
	}

	public void resetScreen(Image img)
	{
		// this.d = new Dimension(app.getScreenX(),app.getScreenY());
		this.img = img;
	}

	ViewerListener getListener()
	{
		return vl;
	}

	@Override
	public Dimension getPreferredSize()
	{
		return new Dimension(app.getScreenX(),app.getScreenY());//new Dimension(400,0);
	}

	@Override
	public Dimension getMinimumSize()
	{
		return getPreferredSize();
	}

	public int getOffX()
	{
		return (getWidth() - app.getScreenX()) / 2;
	}

	public int getOffY()
	{
		return 0;
	}

	/**
	 * 绘制ui设计器里顶级layout及其子子孙孙（它们的paint方法
	 * 是递归实现的，所以可以保证把所有子孙绘制出来）.
	 * 
	 * @param g
	 * @param l
	 */
	public void paint(Graphics g, Layout l)
	{
		l.clearRendering();
		l.resizeForRendering();
		
		//** @@deprecated
		//** 2012-11-20：由Jack Jiang标记为过时——目前为止不需要
		//** painter机制，它是droiddraw原作者的实现，目前的评估是没
		//** 有多大必要，相反还使得代码复杂性增加，以后以下所有代码可
		//** 能会被删除！ 详见类：org.droiddraw.gui.painter.WidgetPaintersRegistry
//		WidgetPainter wp = WidgetPaintersRegistry.getPainter(l.getClass());
//		if (wp != null)
//			// 用指定的painter绘制布局相关的背景等，它会进行递归绘制
//			// 该布局及其子子孙孙布局都将逐级进行绘制
//			wp.paint(l, g);
//		else
//		l.paint(g);
		
		// 绘制布局及其组件（子子孙孙的绘制是通过递归实现的）
		l.paint(g);
	}

	/**
	 * 绘制ui设计器预览界面本身.
	 */
	@Override
	public void paint(Graphics g)
	{
		Graphics2D g2d = (Graphics2D) g;
		
		// 填充主编辑区外的背景底色（纯色填充）
		g.setColor(new Color(212,213,215));//213,232,234));//Color.lightGray);
		g.fillRect(0, 0, getWidth(), getHeight());
		// 填充主编辑区外的半透明背景（用栅格化的图片平铺实现）
		g2d.setPaint(Utils.createTexturePaint(
				ImageResources.instance().getImage("editor_content_bg")));
		g2d.fillRect(0, 0, getWidth(), getHeight());
		
		int dx = getOffX();
		int dy = getOffY();
		g2d.transform(AffineTransform.getTranslateInstance(dx, dy));

		g.setColor(Color.white);
		g.fillRect(0, 0, app.getScreenX(), app.getScreenY());
		String theme = AndroidEditor.instance().getTheme();
		if (theme == null || theme.equals("default"))
		{
			Image back = null;
//			Image stat = null;
			int wx = AndroidEditor.instance().getScreenX();

			AndroidEditor.ScreenMode sc = AndroidEditor.instance()
					.getScreenMode();
			if (sc.equals(AndroidEditor.ScreenMode.HVGA_PORTRAIT)
					|| sc.equals(AndroidEditor.ScreenMode.QVGA_PORTRAIT)
					|| sc.equals(AndroidEditor.ScreenMode.WVGA_PORTRAIT))
			{
				back = ImageResources.instance().getImage("background_01p");
//				stat = ImageResources.instance().getImage(
//						"statusbar_background_p");
			}
			else if (sc.equals(AndroidEditor.ScreenMode.HVGA_LANDSCAPE)
					|| sc.equals(AndroidEditor.ScreenMode.QVGA_LANDSCAPE)
					|| sc.equals(AndroidEditor.ScreenMode.WVGA_LANDSCAPE))
			{
				back = ImageResources.instance().getImage("background_01p");//background_01l");
//				stat = ImageResources.instance().getImage(
//						"statusbar_background_l");
			}
			if (back != null)
				g.drawImage(back, 0, 0, AndroidEditor.instance().getScreenX(),
						AndroidEditor.instance().getScreenY(), this);
			// 绘制状态栏背景
			final int STATUS_BAR_HEIGHT = 25;//stat.getHeight(null)
//			if (stat != null)
//				g.drawImage(stat, 0, 0, AndroidEditor.instance().getScreenX(),
//						stat.getHeight(null), this);
			g.setColor(Color.black);
			g.fillRect(0, 0, AndroidEditor.instance().getScreenX(), STATUS_BAR_HEIGHT);
			

			Image dat = ImageResources.instance().getImage(
					"stat_sys_data_connected");
			g.drawImage(dat, wx - 140, 1, this);

			Image sig = ImageResources.instance().getImage("stat_sys_signal_3");
			g.drawImage(sig, wx - 113, 1, this);

			Image bat = ImageResources.instance().getImage(
					"stat_sys_battery_charge_100");
			g.drawImage(bat, wx - 90, 1, this);

			// 开启字体绘制反走样
			Utils.setTextAntiAliasing((Graphics2D)g, true);
			
			// * 绘制status栏的时间（图片填充方法）——为获得好的视觉效果标题文本改用图片填充
			Image statusTimeImg = ImageResources
					.instance().getImage("stat_sys_time_img");
			g.drawImage(statusTimeImg, wx - 65, 7, null);
			// * 绘制status栏的时间（文本填充方法）
//			Font f = g.getFont();
//			Font f2 = FontObjectFactory.getDroidSans();//f.deriveFont(Font.BOLD);
//			f2 = f2.deriveFont(Font.BOLD, 14f);
//			g.setFont(f2);
//			g.setColor(Color.white);
//			g.drawString(
////					DateFormat.getTimeInstance(DateFormat.SHORT).format(
////					new Date())
//					new java.text.SimpleDateFormat("hh:mm").
//					// 因为使用的是DroidSans字体，而经此form后pm会被解析成“下午”等
//					// 为了不出现乱码（DroidSans字体是英文字体），干脆就固定加个尾巴
//					// 吧（虽不正确但ui设计应该是够用了）
//	                format(new java.util.Date())+"PM"
//					, wx - 65, 18);

			// 标题栏背景填充
			Image title = ImageResources.instance().getImage("title_bar.9");
			NineWayImage nwt = new NineWayImage(title, 0, 0);
			nwt.paint(g, 0, STATUS_BAR_HEIGHT, wx, STATUS_BAR_HEIGHT);
			
			
			// * 绘制标题（图片填充方法）——为获得好的视觉效果标题文本改用图片填充
			Image titleTextImg = ImageResources
					.instance().getImage("stat_sys_title_text_img");
			g.drawImage(titleTextImg, 2, STATUS_BAR_HEIGHT 
					+ (title.getHeight(null)  - titleTextImg.getHeight(null))/2, null);
//			// * 绘制标题（文本填充方法）
//			// 标题：阴影效果
//			g.setColor(new Color(80,80,80));
//			g.drawString("DroidUIBuilder", 5, STATUS_BAR_HEIGHT + 16);
//			// 标题：真正的标题
//			g.setColor(Color.white);//Color.lightGray);
//			g.drawString("DroidUIBuilder", 5, STATUS_BAR_HEIGHT + 17);
//			
//			// 绘制完成后并闭字体绘制反走样
//			Utils.setTextAntiAliasing((Graphics2D)g, false);
//			g.setFont(f);
		}
		else
		{
			if (img != null)
				g.drawImage(img, 0, 0, this);
		}
		
		// 此方法中绘制布局相关的背景等
		g.setColor(Color.LIGHT_GRAY);
		paint(g, app.getRootLayout());

//		// 绘制选中widget的边框（告诉用户该组件被选中了）
//		Widget w = app.getSelected();
//		if (w != null)
//		{
//			int off_x = 0;
//			int off_y = 0;
//			if (w.getParent() != null)
//			{
//				off_x = w.getParent().getScreenX();
//				off_y = w.getParent().getScreenY();
//			}
//
//			// 绘制选中组件的边框
//			g.setColor(Color.yellow);
//			g.drawRect(w.getX() + off_x, w.getY() + off_y, w.getWidth(), w.getHeight());
//		}
	}

	public void dragEnter(DropTargetDragEvent arg0)
	{
	}

	public void dragExit(DropTargetEvent arg0)
	{
	}

	public void dragOver(DropTargetDragEvent arg0)
	{
	}

	public void drop(DropTargetDropEvent e)
	{
		e.acceptDrop(DnDConstants.ACTION_COPY);
		Transferable t = e.getTransferable();
		try
		{
			Object data = t.getTransferData(t.getTransferDataFlavors()[0]);
			Point l = e.getLocation();
			vl.addWidget(ViewerListener.createWidget((String) data), l.x
					- getOffX(), l.y - getOffY());
			AndroidEditor.instance().setChanged(true);
			e.dropComplete(true);
		}
		catch (IOException ex)
		{
			MainPane.getTipCom().error(ex);
		}
		catch (UnsupportedFlavorException ex)
		{
			MainPane.getTipCom().error(ex);
		}
	}

	public void dropActionChanged(DropTargetDragEvent arg0)
	{
	}

	public void stateChanged(ChangeEvent ev)
	{
		if (ev.getSource() instanceof Layout)
		{
			repaint();
		}
	}

	public void addWidget(Widget w, Layout layout, int x, int y)
	{
		vl.addWidget(w, layout, x, y);
	}
}