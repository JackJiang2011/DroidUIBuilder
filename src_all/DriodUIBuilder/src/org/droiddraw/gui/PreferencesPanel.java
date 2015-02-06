/*
 * Copyright (C) 2015 Jack Jiang(cngeeker.com) The DroidUIBuilder Project. 
 * All rights reserved.
 * Project URL:https://github.com/JackJiang2011/DroidUIBuilder
 * Version 1.0
 * 
 * Jack Jiang PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * PreferencesPanel.java at 2015-2-6 16:12:03, original version by Jack Jiang.
 * You can contact author with jb2011@163.com.
 */
package org.droiddraw.gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import org.droiddraw.AndroidEditor;


public class PreferencesPanel extends JPanel
{
	private static final long serialVersionUID = 1L;

	protected JCheckBox grid;
	protected JComboBox screen;
	protected JComboBox defLayout;
	protected JComboBox update;
	protected JComboBox screenUnit;
	protected JTextField defaultDirectory;

	protected JButton ok;
	protected JButton cancel;
	protected Preferences prefs;
	protected JFrame frame;

	public PreferencesPanel(Preferences prefs, JFrame frame)
	{
		this.prefs = prefs;
		this.frame = frame;

		this.setLayout(new GridLayout(0, 2));
		this.grid = new JCheckBox("Snap to grid.");
		this.grid.setSelected(prefs.getSnap());

		this.screen = new JComboBox(new String[] { "QVGA Landscape",
				"QVGA Portrait", "HVGA Landscape", "HVGA Portrait",
				"WVGA Landscape", "WVGA Portrait" });
		this.screen.setSelectedIndex(prefs.getScreenMode().ordinal());

		this.defLayout = new JComboBox(new String[] { "Absolute Layout",
				"Linear Layout", "Relative Layout" });
		this.defLayout.setSelectedIndex(prefs.getDefaultLayout().ordinal());

		this.update = new JComboBox(
				new String[] { "Always", "Ask me", "Never" });
		this.update.setSelectedIndex(prefs.getUpdateCheck().ordinal());

		this.screenUnit = new JComboBox(new String[] { "dp", "px" });
		this.screenUnit.setSelectedIndex(0);

		this.defaultDirectory = new JTextField("");
		this.defaultDirectory.setText(prefs.getDefaultDirectory());

		this.ok = new JButton("Apply");
		this.ok.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				PreferencesPanel.this.prefs.setSnap(PreferencesPanel.this.grid
						.isSelected());
				PreferencesPanel.this.prefs
						.setScreenMode(AndroidEditor.ScreenMode.values()[PreferencesPanel.this.screen
								.getSelectedIndex()]);
				PreferencesPanel.this.prefs.setDefaultLayout(Preferences.Layout
						.values()[PreferencesPanel.this.defLayout
						.getSelectedIndex()]);
				PreferencesPanel.this.prefs.setUpdateCheck(Preferences.Update
						.values()[PreferencesPanel.this.update
						.getSelectedIndex()]);
				PreferencesPanel.this.prefs.setScreenUnit((String) screenUnit
						.getSelectedItem());
				PreferencesPanel.this.prefs
						.setDefaultDirectory(defaultDirectory.getText());
				PreferencesPanel.this.prefs.save();
				PreferencesPanel.this.frame.setVisible(false);
				PreferencesPanel.this.frame.dispose();
			}
		});

		this.cancel = new JButton("Cancel");
		this.cancel.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				PreferencesPanel.this.frame.setVisible(false);
				PreferencesPanel.this.frame.dispose();
			}
		});

		this.setBorder(new TitledBorder(new EtchedBorder(), "Preferences"));
		this.add(grid);
		this.add(new JLabel(""));
		this.add(new JLabel("Default Screen Size"));
		this.add(screen);
		this.add(new JLabel("Default Screen Layout"));
		this.add(defLayout);
		this.add(new JLabel("Check for updates at startup?"));
		this.add(update);
		this.add(new JLabel("Default screen unit."));
		this.add(screenUnit);
		this.add(new JLabel("Default location for saving files"));
		this.add(defaultDirectory);
		this.add(cancel);
		this.add(ok);
	}

}
