/*
 * Copyright (C) 2015 Jack Jiang(cngeeker.com) The DroidUIBuilder Project. 
 * All rights reserved.
 * Project URL:https://github.com/JackJiang2011/DroidUIBuilder
 * Version 1.0
 * 
 * Jack Jiang PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * ColorPanel.java at 2015-2-6 16:12:02, original version by Jack Jiang.
 * You can contact author with jb2011@163.com.
 */
package org.droiddraw.gui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.Border;

import org.droiddraw.AndroidEditor;
import org.droiddraw.property.ColorProperty;

public class ColorPanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	JButton jb;
	JAutoComboBox jac;
	BufferedImage img;
	Color c;

	public ColorPanel(ColorProperty clr)
	{
		FlowLayout flayout = new FlowLayout(FlowLayout.LEFT);
		flayout.setHgap(0);
		flayout.setVgap(0);
		this.setLayout(flayout);
		this.c = clr.getColorValue();
		img = new BufferedImage(17, 17, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics g = img.getGraphics();
		g.setColor(c);
		g.fillRect(0, 0, img.getWidth(), img.getHeight());
		jb = new JButton(new ImageIcon(img));
//		Border jbDefaultBorder = jb.getBorder();
		jb.setMargin(new Insets(3,3,3,3));

		String colorValue = clr.getStringValue();

		Vector<String> colors = new Vector<String>();
		colors.add("");
		for (String key : AndroidEditor.instance().getColors().keySet())
			colors.add("@color/" + key);
		jac = new JAutoComboBox(colors);
		jac.setEditable(true);
		jac.setStrict(false);
		jac.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				String colorDescriptor = jac.getSelectedItem().toString();
				Color nc = ColorProperty.parseColor(colorDescriptor);
				if (nc != null)
				{
					c = nc;
					repaintImage(c);
				}
				else
					repaintImage(Color.white);
			}
		});
		if (colorValue != null && colorValue.length() > 0)
			jac.setSelectedItem(colorValue);

		jb.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				Color nc = chooseColor();
				if (nc != null)
				{
					String colorString = ColorProperty.makeColor(nc);
					jac.addItem(colorString);
					jac.setSelectedItem(colorString);
				}
			}
		});
		add(jac);
		add(Box.createHorizontalStrut(2));
		add(jb);
	}

	private void repaintImage(Color nc)
	{
		Graphics g = img.getGraphics();
		g.setColor(nc);
		g.fillRect(0, 0, img.getWidth(), img.getHeight());
		jb.repaint();
	}

	public String getString()
	{
		return jac.getSelectedItem().toString();
	}

	// TODO(brendan) : This is f-ugly!
	static JDialog jd;
	static JColorChooser jcc;
	static Color sc;

	public static Color chooseColor()
	{
		sc = null;
		if (jcc == null)
		{
			jcc = new JColorChooser();
			jd = JColorChooser.createDialog(null, "Choose a color", true, jcc,
					new ActionListener()
					{
						public void actionPerformed(ActionEvent e)
						{
							sc = jcc.getColor();
						}
					}, null);
		}
		jd.setVisible(true);
		return sc;
	}
}
