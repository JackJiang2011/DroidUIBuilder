/*
 * Copyright (C) 2015 Jack Jiang(cngeeker.com) The DroidUIBuilder Project. 
 * All rights reserved.
 * Project URL:https://github.com/JackJiang2011/DroidUIBuilder
 * Version 1.0
 * 
 * Jack Jiang PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * MakeColorsPane.java at 2015-2-6 16:12:01, original version by Jack Jiang.
 * You can contact author with jb2011@163.com.
 */
package org.droiddraw;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;

import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import org.droiddraw.gui.AbstractDataPanel;
import org.droiddraw.util.ColorHandler;

public class MakeColorsPane extends AbstractDataPanel
{
	private static final long serialVersionUID = 1L;

	public static class ColorRenderer implements TableCellRenderer
	{
		Border selectedBorder;
		Border unselectedBorder;

		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column)
		{
			Color color = (Color) value;
			JLabel lb = new JLabel("#" + Integer.toString(color.getRed(), 16)
					+ Integer.toString(color.getGreen(), 16)
					+ Integer.toString(color.getBlue(), 16)
					+ Integer.toString(color.getAlpha(), 16));
			lb.setOpaque(true);
			lb.setBackground(color);

			if (isSelected)
			{
				if (selectedBorder == null)
				{
					selectedBorder = BorderFactory.createMatteBorder(1, 1, 1,
							1, table.getSelectionBackground());
				}
				lb.setBorder(selectedBorder);
			}
			else
			{
				if (unselectedBorder == null)
				{
					unselectedBorder = BorderFactory.createMatteBorder(1, 1, 1,
							1, table.getBackground());
				}
				lb.setBorder(unselectedBorder);
			}

			lb.setToolTipText("Red: " + color.getRed() + ", Green: "
					+ color.getGreen() + ", Blue: " + color.getBlue());

			return lb;
		}

	}

	public class ColorEditor extends AbstractCellEditor implements
			TableCellEditor, ActionListener
	{
		private static final long serialVersionUID = 1L;
		Color current;
		JButton b;
		JColorChooser cc;
		JDialog dl;

		public ColorEditor()
		{
			b = new JButton("Edit");
			b.addActionListener(this);

			cc = new JColorChooser();
			dl = JColorChooser.createDialog(b, "Choose a Color", true, cc,
					this, null);
		}

		public void actionPerformed(ActionEvent e)
		{
			if ("Edit".equals(e.getActionCommand()))
			{
				b.setBackground(current);
				cc.setColor(current);
				dl.setVisible(true);
				fireEditingStopped();
			}
			else
			{
				current = cc.getColor();
			}
		}

		public Object getCellEditorValue()
		{
			return current;
		}

		public Component getTableCellEditorComponent(JTable table,
				Object value, boolean isSelected, int row, int column)
		{
			current = (Color) value;
			return b;
		}
	}

	public MakeColorsPane()
	{
		super(new Class<?>[] { String.class, Color.class });
		dataTable.setDefaultRenderer(Color.class, new ColorRenderer());
		dataTable.setDefaultEditor(Color.class, new ColorEditor());
	}

	@Override
	protected int parentRowCount()
	{
		Hashtable<String, Color> colors = AndroidEditor.instance().getColors();
		return colors.size();
	}

	@Override
	protected Object parentValueAt(int row, int col)
	{
		Hashtable<String, Color> colors = AndroidEditor.instance().getColors();
		ArrayList<String> sorted = Collections.list(colors.keys());
		Collections.sort(sorted);

		if (col == 0)
		{
			return sorted.get(row);
		}
		else if (col == 1)
		{
			return colors.get(sorted.get(row));
		}
		return null;
	}

	@Override
	protected void parentSetValueAt(Object value, int rowIndex, int columnIndex)
	{
		String key = (String) parentValueAt(rowIndex, 0);
		Hashtable<String, Color> colors = AndroidEditor.instance().getColors();
		if (columnIndex == 1)
		{
			colors.put(key, (Color) value);
		}
		else
		{
			Color val = colors.get(key);
			colors.remove(key);
			colors.put((String) value, val);
		}
	}

	@Override
	protected void parentDeleteRow(int row)
	{
		String key = (String) parentValueAt(row, 0);
		Hashtable<String, Color> colors = AndroidEditor.instance().getColors();
		colors.remove(key);
	}

	@Override
	protected void doSave()
	{
		File out = AndroidEditor.instance().getColorFile();
		if (out == null)
		{
			out = Launch.doSaveBasic();
		}
		if (out != null)
		{
			try
			{
				ColorHandler.dump(new FileWriter(out), AndroidEditor.instance()
						.getColors());
			}
			catch (IOException ex)
			{
				MainPane.getTipCom().error(ex);
			}
		}
	}

	@Override
	protected void addValue(String name)
	{
		AndroidEditor.instance().getColors()
				.put(name, new Color(128, 128, 128));
	}
}
