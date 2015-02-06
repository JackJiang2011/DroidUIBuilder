/*
 * Copyright (C) 2015 Jack Jiang(cngeeker.com) The DroidUIBuilder Project. 
 * All rights reserved.
 * Project URL:https://github.com/JackJiang2011/DroidUIBuilder
 * Version 1.0
 * 
 * Jack Jiang PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * DonatePanel.java at 2015-2-6 16:12:02, original version by Jack Jiang.
 * You can contact author with jb2011@163.com.
 */
package org.droiddraw.gui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JPanel;


import com.jb2011.drioduibuilder.util.Utils;

public class DonatePanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	Image donate;
//	Dimension d;

	public DonatePanel()
	{
		donate = ImageResources.instance().getImage("paypal");

//		d = new Dimension(200, 200);

		this.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				String url = null;
				int y = e.getY();
				if (y >= 60 && y < 90)
					url = "https://www.paypal.com/us/cgi-bin/webscr?cmd=_xclick&business=brendan.d.burns@gmail.com&item_name=Support%20DroidDraw&currency_code=USD";
				else if (y >= 110 && y < 130)
					url = "http://www.droiddraw.org/tutorial.html";
				else if (y >= 160 && y < 180)
					url = "http://code.google.com/p/droiddraw/issues/list";
				if (url != null)
				{
					Utils.browseURL(url);
//					AndroidEditor.instance().getURLOpener().openURL(url);
				}
			}
		});

		this.addMouseMotionListener(new MouseMotionAdapter()
		{
			@Override
			public void mouseMoved(MouseEvent e)
			{
				if (e.getX() >= 20
						&& e.getX() < 80
						&& e.getY() >= 60
						&& e.getY() < 90
						|| e.getX() >= 20
						&& e.getX() < 300
						&& ((e.getY() >= 110 && e.getY() < 130) || (e.getY() >= 160 && e
								.getY() < 180)))
				{
					setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				}
				else
				{
					setCursor(Cursor.getDefaultCursor());
				}
			}
		});

	}

//	@Override
//	public Dimension getPreferredSize()
//	{
//		return d;
//	}
//
//	@Override
//	public Dimension getMinimumSize()
//	{
//		return d;
//	}

	@Override
	public void paint(Graphics g)
	{
		g.setColor(Color.white);
		g.fillRect(0, 0, getWidth(), getHeight());

		g.setColor(Color.black);
		g.drawString("DroidDraw is dependent on user donations to fund development.",
						20, 20);
		g.drawString(
				"If you find DroidDraw useful, please consider supporting us.",
				20, 40);
		g.drawImage(donate, 20, 60, null);

		g.drawString("Tutorials are also available at:", 20, 110);
		// Font f = g.getFont();
		// Map<TextAttribute, Object> map = new Hashtable<TextAttribute,
		// Object>();
		// map.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
		// Font u = f.deriveFont(map);
		// g.setFont(u);
		g.drawString("http://www.droiddraw.org/tutorial.html", 20, 130);

		g.drawString("Please submit bugs/feature requests to:", 20, 160);
		g.drawString("http://code.google.com/p/droiddraw/issues/list", 20, 180);

	}
}
