/*
 * Copyright (C) 2015 Jack Jiang(cngeeker.com) The DroidUIBuilder Project. 
 * All rights reserved.
 * Project URL:https://github.com/JackJiang2011/DroidUIBuilder
 * Version 1.0
 * 
 * Jack Jiang PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * PropertiesPanel.java at 2015-2-6 16:12:03, original version by Jack Jiang.
 * You can contact author with jb2011@163.com.
 */
package org.droiddraw.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import org.droiddraw.AndroidEditor;
import org.droiddraw.Launch;
import org.droiddraw.MainPane;
import org.droiddraw.property.BooleanProperty;
import org.droiddraw.property.ColorProperty;
import org.droiddraw.property.ImageProperty;
import org.droiddraw.property.IntProperty;
import org.droiddraw.property.Property;
import org.droiddraw.property.SelectProperty;
import org.droiddraw.property.StringProperty;
import org.droiddraw.util.FileCopier;
import org.droiddraw.widget.Layout;
import org.droiddraw.widget.Widget;

import com.jb2011.drioduibuilder.swingw.HardLayoutPane;

public class PropertiesPanel extends JPanel implements ActionListener,
		PropertyChangeListener, KeyListener
{
	private static final long serialVersionUID = 1L;

	Vector<Property> properties;
	Hashtable<Property, JComponent> components;
	Hashtable<ColorProperty, Color> colorTable;
	AndroidEditorViewer viewer;
	Widget w;
	HardLayoutPane items;
	Dimension d;
//	boolean applet;

	public PropertiesPanel(
//			boolean applet
			)
	{
		this(new Vector<Property>(), null
//				, applet
				);
	}

	public PropertiesPanel(Vector<Property> properties, Widget w
//			, boolean applet
			)
	{
		this.components = new Hashtable<Property, JComponent>();
		this.colorTable = new Hashtable<ColorProperty, Color>();
		this.w = w;
//		this.applet = applet;

		setProperties(properties, w);
		this.d = new Dimension(200, 400);
	}

//	public void setApplet(boolean applet)
//	{
//		this.applet = applet;
//	}

	public void setProperties(Vector<Property> properties, Widget w)
	{
		this.properties = properties;
		this.removeAll();
		this.w = w;
		if (w == null)
			return;
		w.setPropertyChangeListener(this);
		items = new HardLayoutPane();
		items.setComponentInsets(new Insets(3,1,3,1));
//		items.setLayout(new GridLayout(0, 2));
		// items.setBorder(BorderFactory.createTitledBorder("Properties"));
		components.clear();

		if (properties.size() > 0)
		{
			items.addTo(new JLabel("FOR: ")//"Properties for: ")
			,true);
			items.addTo(new JLabel(w.getTagName()),true);
			items.nextLine();
		}
//		java.awt.FlowLayout fl = new FlowLayout();
//		fl.setAlignment(FlowLayout.RIGHT);
		for (Property prop : properties)
		{
			if (prop.getEditable())
			{
				if (prop instanceof BooleanProperty)
				{
//					JPanel p = new JPanel();// TODO this panel will drop
//					p.setBorder(BorderFactory.createLineBorder(Color.red));
					items.addTo(new JLabel(""),true);// just space holder
					JCheckBox jcb = new JCheckBox(prop.getEnglishName());
					jcb.setSelected(((BooleanProperty) prop).getBooleanValue());
					components.put(prop, jcb);
					items.addTo(jcb,true);
					items.nextLine();
				}
				else if (prop instanceof IntProperty)
				{
					items.addTo(new JLabel(prop.getEnglishName()),true);
					JTextField jf = new JTextField(
							prop.getValue() != null ? prop.getValue().toString() : "", 5);
//					JPanel jp = new JPanel();
//					jp.setLayout(fl);
//					jp.add(jf);
					components.put(prop, jf);
					items.addTo(jf,true);//jp,true);
					items.nextLine();
				}
				else if (prop instanceof ImageProperty)
				{
					items.addTo(new JLabel(prop.getEnglishName()),true);
					final JTextField jf = new JTextField(
							prop.getValue() != null ? prop.getValue().toString() : "", 10);
					JPanel jp = new JPanel(new FlowLayout(FlowLayout.LEFT));
//					jp.setLayout(fl);
					jp.add(jf);
//					if (!applet)
					{
						JButton jb = new JButton("Browse");
						jb.addActionListener(new ActionListener()
						{
							public void actionPerformed(ActionEvent e)
							{
								if (AndroidEditor.instance().getDrawableDirectory() == null)
								{
									JOptionPane.showMessageDialog(
													PropertiesPanel.this,
													"You must select a drawables directory.\n If you select an image which is not in this directory,\n it will be copied into it.",
													"Select Drawable Dir.",
													JOptionPane.INFORMATION_MESSAGE);
									File dir = Launch.doOpenDir();
									if (dir != null)
										AndroidEditor.instance().setDrawableDirectory(dir);
								}
								File dir = AndroidEditor.instance().getDrawableDirectory();
								if (dir != null)
								{
									File img = Launch.doOpenImage(dir);
									if (img != null)
									{
										if (!img.getParentFile().equals(dir))
										{
											try{
												FileCopier.copy(img, dir, true);
											}
											catch (IOException ex){
												MainPane.getTipCom().error(ex);
											}
										}

										String name = img.getName();
										int ix = name.indexOf(".");
										if (ix != -1)
										{
											name = name.substring(0, ix);
										}
										jf.setText("@drawable/" + name);
										jf.requestFocus();
									}
								}
							}
						});
						jp.add(jb);
					}
					components.put(prop, jf);
					items.addTo(jp,true);
					items.nextLine();

				}
				else if (prop instanceof StringProperty)
				{
					items.addTo(new JLabel(prop.getEnglishName()),true);
					JComponent jc;
					if (prop instanceof SelectProperty)
					{
						JComboBox jcb = new JComboBox(((SelectProperty) prop).getOptions());
						jcb.setSelectedIndex(((SelectProperty) prop).getSelectedIndex());
						jc = jcb;
					}
					else if (prop instanceof ColorProperty)
						jc = new ColorPanel((ColorProperty) prop);
					else
					{
						if (prop.getAtttributeName().equals(
								"android:layout_width")
								|| prop.getAtttributeName().equals(
										"android:layout_height"))
						{
							Vector<String> v = new Vector<String>();
							v.add("");
							v.add("match_parent");
							v.add("wrap_content");
							v.add("fill_parent");
							// JAutoComboBox jat = new JAutoComboBox(v);
							JAutoTextField jat = new JAutoTextField(v);
							jat.setStrict(false);
							jat.setColumns(10);
							if (prop.getValue() != null)
								jat.setText(prop.getValue().toString());
							jc = jat;
						}
						else
						{
							jc = new JTextField(prop.getValue() != null ? prop
									.getValue().toString() : "", 10);
						}
					}
					prop.addPropertyChangeListener(new PropertyChangeListener()
					{
						public void propertyChange(PropertyChangeEvent evt)
						{
							Property p = (Property) evt.getSource();
							JComponent jc = components.get(p);
							if (p instanceof BooleanProperty)
								((JCheckBox) jc).setSelected(((Boolean) evt
										.getNewValue()).booleanValue());
							else if (p instanceof ColorProperty)
							{
								// TODO
							}
							else if (p instanceof SelectProperty)
							{
								// TODO
							}
							else if (p instanceof StringProperty)
							{
								if (jc != null)
									((JTextField) jc).setText(evt.getNewValue().toString());
							}
						}
					});
					components.put(prop, jc);
//					JPanel p = new JPanel();
//					p.setLayout(fl);
//					p.add(jc);
					items.addTo(jc,true);//p,true);
					items.nextLine();
				}
			}
		}
		
		this.setLayout(new BorderLayout());
		FlowLayout ffl = new FlowLayout(FlowLayout.LEFT);
		ffl.setVgap(0);
		ffl.setHgap(0);
		JPanel p = new JPanel(ffl);
		p.add(items);
		this.add(new JScrollPane(p), BorderLayout.CENTER);
		if (properties.size() > 0)
		{
			JButton apply = new JButton("Apply");
			apply.addActionListener(this);
			p = new JPanel();
			p.add(apply);
			this.add(p, BorderLayout.SOUTH);
		}
		this.repaint();
		this.invalidate();
	}

	public void setViewer(AndroidEditorViewer v)
	{
		this.viewer = v;
	}

	public void actionPerformed(ActionEvent arg0)
	{
		apply();
	}

	protected void apply()
	{
		for (Property prop : properties)
		{
			if (prop.getEditable())
			{
				if (prop instanceof BooleanProperty)
				{
					JCheckBox jcb = (JCheckBox) components.get(prop);
					((BooleanProperty) prop).setBooleanValue(jcb.isSelected());
				}
				else if (prop instanceof IntProperty)
				{
					JTextField jf = (JTextField) components.get(prop);
					try
					{
						((IntProperty) prop).setIntValue(Integer.parseInt(jf
								.getText()));
					}
					catch (NumberFormatException ex)
					{
						MainPane.getTipCom().warn(ex);
					}
				}
				else if (prop instanceof StringProperty)
				{
					if (prop instanceof SelectProperty)
					{
						JComboBox jcb = (JComboBox) components.get(prop);
						if (jcb == null)
						{
							MainPane.getTipCom().warn(
									"Couldn't find select for: "
											+ prop.getAtttributeName());
						}
						else
							((SelectProperty) prop).setSelectedIndex(jcb
									.getSelectedIndex());
					}
					else if (prop instanceof ColorProperty)
					{
						ColorPanel cp = (ColorPanel) components.get(prop);
						((ColorProperty) prop).setStringValue(cp.getString());
					}
					else
					{
						JTextField jtf = (JTextField) components.get(prop);
						if (jtf == null)
							MainPane.getTipCom().warn(
									"Couldn't find text for: "+ prop.getAtttributeName());
						else if (prop.getAtttributeName().equals(
								"android:layout_width")
								|| prop.getAtttributeName().equals(
										"android:layout_height"))
						{
							if (!isValidWidthMeasurement(jtf.getText())
									&& !isAbstractWidth(jtf.getText()))
							{
								MainPane.getTipCom()
										.warn("Incorrect Syntax for: "+ prop.getEnglishName()
														+ "\n\"(px or dp)\" is required after a width or height entry");
								((StringProperty) prop).setStringValue(jtf
										.getText()+ AndroidEditor.instance().getScreenUnit());
							}
							else
								((StringProperty) prop).setStringValue(jtf.getText());
						}
						else
							((StringProperty) prop).setStringValue(jtf.getText());
					}
				}
			}
		}
		w.apply();
		if (w instanceof Layout)
		{
			((Layout) w).repositionAllWidgets();
			w.apply();
		}
		if (w.getParent() != null)
			w.getParent().positionWidget(w);

		if (viewer != null)
			viewer.repaint();
	}

	private boolean isValidWidthMeasurement(String text)
	{
		return text.endsWith("px") || text.endsWith("dp");
	}

	private boolean isAbstractWidth(String text)
	{
		return text.equals("wrap_content") || text.equals("fill_parent")
				|| text.equals("match_parent");
	}

	@SuppressWarnings("unchecked")
	public void propertyChange(PropertyChangeEvent evt)
	{
		if (w.equals(evt.getSource()))
		{
			setProperties((Vector<Property>) evt.getNewValue(), w);
		}
	}

	public void keyPressed(KeyEvent e)
	{
	}

	public void keyReleased(KeyEvent e)
	{
	}

	public void keyTyped(KeyEvent e)
	{
		if (e.getKeyCode() == KeyEvent.VK_ENTER)
			apply();
	}
}
