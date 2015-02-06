/*
 * Copyright (C) 2015 Jack Jiang(cngeeker.com) The DroidUIBuilder Project. 
 * All rights reserved.
 * Project URL:https://github.com/JackJiang2011/DroidUIBuilder
 * Version 1.0
 * 
 * Jack Jiang PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * LayoutPainter.java at 2015-2-6 16:12:01, original version by Jack Jiang.
 * You can contact author with jb2011@163.com.
 */
package org.droiddraw.gui.deprecated;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;

import org.droiddraw.widget.Layout;
import org.droiddraw.widget.Widget;

@Deprecated
// 2012-11-20：由Jack Jiang标记为过时——目前为止不需要
// painter机制，它是droiddraw原作者的实现，目前的评估是没
// 有多大必要，相反还使得代码复杂性增加，以后以下所有代码可
// 能会被删除！
public class LayoutPainter extends AbstractWidgetPainter
{
	public void paint(Widget wx, Graphics g)
	{
		Layout l = (Layout) wx;
		
		// 画背景
		Graphics2D g2d = (Graphics2D) g;
		drawBackground(l, g);
		
		// 画TAG字符串（用于告之ui设计者这个组件的名字）
		g2d.translate(l.getX(), l.getY());
		g.setColor(Color.lightGray);
		if (l.getWidgets().size() == 0)
			g.drawString(l.getTagName(), 2, 15);
		
		// 画边框
		g.drawRect(0, 0, l.getWidth(), l.getHeight());
		
		// 递归画该布局的子组件
		for (Widget w : l.getWidgets())
		{
			Stroke oldStroke = g2d.getStroke();
			
			// 处于gone状态的widget就不用绘制了
			if(!w.isGone())
			{
				if (w.isVisible())
				{
					// do nothing
				}
				// 如果是invisible则用虚线进行绘制，从而方便告之用户，这些组件是invisible的
				else
				{
					// 虚线样式
					Stroke sroke = new BasicStroke(1, BasicStroke.CAP_BUTT,
							BasicStroke.JOIN_BEVEL, 0, new float[]{1, 2}, 0);//实线，空白
					g2d.setStroke(sroke);
				}

//				似乎没有必要单独搞什么painter，只需要给各widget一个isUsedForDesign不就行了？
//				起码目前想不出来用painter有什么好处？相反还要维护2处paint代码！！！！！！！！
				// TODO !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
				// TODO !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
				// TODO !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
				// TODO !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
				// TODO !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
				// TODO !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
				// TODO !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
				// TODO !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
				// TODO !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
				// TODO !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
				// TODO !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
				// TODO !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
				// 递归画该布局的子组件哦
//				WidgetPainter wp = WidgetPaintersRegistry.getPainter(w.getClass());
//				if (wp != null)
//					wp.paint(w, g);
//				else
					w.paint(g);

				// 撤销虚线设置
				if( !w.isVisible())
					g2d.setStroke(oldStroke);
			}
		}
		g2d.translate(-l.getX(), -l.getY());
	}
}
