/*
 * Copyright (C) 2015 Jack Jiang(cngeeker.com) The DroidUIBuilder Project. 
 * All rights reserved.
 * Project URL:https://github.com/JackJiang2011/DroidUIBuilder
 * Version 1.0
 * 
 * Jack Jiang PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * AbstractDataPanel.java at 2015-2-6 16:12:03, original version by Jack Jiang.
 * You can contact author with jb2011@163.com.
 */
package org.droiddraw.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

public abstract class AbstractDataPanel extends JPanel
{
	private static final long serialVersionUID = 1L;

	private class DataTableModel extends AbstractTableModel
	{
		private static final long serialVersionUID = 1L;
		private Class<?>[] classes;

		public DataTableModel(Class<?>[] classes)
		{
			this.classes = classes;
		}

		public int getColumnCount()
		{
			return classes.length;
		}

		@Override
		public boolean isCellEditable(int row, int col)
		{
			return true;
		}

		@Override
		public Class<?> getColumnClass(int col)
		{
			return classes[col];
		}

		@Override
		public String getColumnName(int col)
		{
			if (col == 0)
			{
				return "Name";
			}
			else
			{
				return "Value";
			}
		}

		public int getRowCount()
		{
			return parentRowCount();
		}

		public Object getValueAt(int row, int col)
		{
			return parentValueAt(row, col);
		}

		@Override
		public void setValueAt(Object value, int rowIndex, int columnIndex)
		{
			parentSetValueAt(value, rowIndex, columnIndex);
			this.fireTableCellUpdated(rowIndex, columnIndex);
		}

		public void deleteRow(int row)
		{
			parentDeleteRow(row);
			this.fireTableDataChanged();
		}
	}

	Dimension d;

	protected JTable dataTable;
	JButton save;
	JButton create;
	JButton delete;
	DataTableModel model;

	public AbstractDataPanel(Class<?>[] classes)
	{
		d = new Dimension(500, 300);

		model = new DataTableModel(classes);

		dataTable = new JTable(model);
		dataTable.setShowHorizontalLines(true);
		// dataTable.setShowGrid(true);

		setLayout(new BorderLayout());
		add(new JScrollPane(dataTable), BorderLayout.CENTER);
		// add(dataTable.getTableHeader(), BorderLayout.NORTH);

		JPanel jp = new JPanel();
		jp.setLayout(new FlowLayout());
		JButton save;
		JButton create;
		JButton delete;

		save = new JButton("Save");

		save.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				doSave();
			}
		});

		create = new JButton("New");
		create.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				String name = JOptionPane.showInputDialog(dataTable,
						"Enter a name for this new value:");
				if (name != null)
				{
					addValue(name);
					model.fireTableDataChanged();
				}
			}
		});

		delete = new JButton("Delete");

		delete.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				int row = dataTable.getSelectedRow();
				model.deleteRow(row);
			}
		});

		jp.add(save);
		jp.add(create);
		jp.add(delete);
		add(jp, BorderLayout.SOUTH);
	}

	@Override
	public Dimension getPreferredSize()
	{
		return d;
	}

	@Override
	public Dimension getMinimumSize()
	{
		return d;
	}

	protected abstract int parentRowCount();

	protected abstract Object parentValueAt(int row, int col);

	protected abstract void parentSetValueAt(Object value, int rowIndex,
			int columnIndex);

	protected abstract void parentDeleteRow(int row);

	protected abstract void doSave();

	protected abstract void addValue(String name);
}
