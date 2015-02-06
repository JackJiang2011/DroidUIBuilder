/*
 * Copyright (C) 2015 Jack Jiang(cngeeker.com) The DroidUIBuilder Project. 
 * All rights reserved.
 * Project URL:https://github.com/JackJiang2011/DroidUIBuilder
 * Version 1.0
 * 
 * Jack Jiang PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * Preferences.java at 2015-2-6 16:12:02, original version by Jack Jiang.
 * You can contact author with jb2011@163.com.
 */
package org.droiddraw.gui;

import java.util.prefs.BackingStoreException;

import org.droiddraw.MainPane;
import org.droiddraw.AndroidEditor.ScreenMode;

public class Preferences
{
	public static enum Layout
	{
		ABSOLUTE, LINEAR, RELATIVE
	}

	public static enum Update
	{
		YES, ASK, NO
	}

	private static final String SNAP = "snap";
	private static final String SCREEN = "screen";
	private static final String LAYOUT = "layout";
	private static final String UPDATE = "update";
	private static final String SCREEN_UNIT = "screen_unit";
	private static final String DEFAULT_DIR = "default_directory";

	protected boolean snap;
	protected ScreenMode screen;
	protected Layout layout;
	protected Update updateCheck;
	private String screenUnit;
	private String defaultDirectory;

	public Preferences()
	{
		this.snap = false;
		this.screen = ScreenMode.HVGA_PORTRAIT;
		this.layout = Layout.ABSOLUTE;
		this.updateCheck = Update.ASK;
		this.screenUnit = "dp";
		this.defaultDirectory = "";
	}

	public void load()
	{
		java.util.prefs.Preferences prefs = java.util.prefs.Preferences
				.userNodeForPackage(Preferences.class);
		this.snap = prefs.getBoolean(SNAP, false);

		int screenPref = prefs.getInt(SCREEN, 3);
		if (screenPref < 0 || screenPref >= ScreenMode.values().length)
		{
			screenPref = 3;
		}
		if (3 < ScreenMode.values().length)
		{
			screen = ScreenMode.values()[3];
		}
		else
		{
			screen = ScreenMode.QVGA_PORTRAIT;
		}
		int layoutPref = prefs.getInt(LAYOUT, 0);
		if (layoutPref < 0 || layoutPref >= Layout.values().length)
		{
			layoutPref = 0;
		}
		layout = Layout.values()[layoutPref];
		int updatePref = prefs.getInt(UPDATE, 1);
		if (updatePref < 0 || updatePref >= Update.values().length)
		{
			updatePref = 1;
		}
		updateCheck = Update.values()[updatePref];
		screenUnit = prefs.get(SCREEN_UNIT, "dp");
		defaultDirectory = prefs.get(DEFAULT_DIR, "");
	}

	public void save()
	{
		java.util.prefs.Preferences prefs = java.util.prefs.Preferences
				.userNodeForPackage(Preferences.class);

		prefs.putBoolean(SNAP, snap);
		prefs.putInt(SCREEN, screen.ordinal());
		prefs.putInt(LAYOUT, layout.ordinal());
		prefs.putInt(UPDATE, updateCheck.ordinal());
		prefs.put(SCREEN_UNIT, screenUnit);
		prefs.put(DEFAULT_DIR, defaultDirectory);
		try
		{
			prefs.sync();
		}
		catch (BackingStoreException ex)
		{
			MainPane.getTipCom().error(ex);
		}
	}

	public void setSnap(boolean value)
	{
		this.snap = value;
	}

	public boolean getSnap()
	{
		return snap;
	}

	public void setScreenMode(ScreenMode screen)
	{
		this.screen = screen;
	}

	public ScreenMode getScreenMode()
	{
		return screen;
	}

	public void setDefaultLayout(Layout l)
	{
		this.layout = l;
	}

	public Layout getDefaultLayout()
	{
		return layout;
	}

	public Update getUpdateCheck()
	{
		return updateCheck;
	}

	public void setUpdateCheck(Update u)
	{
		this.updateCheck = u;
	}

	public String getScreenUnit()
	{
		return screenUnit;
	}

	public void setScreenUnit(String unit)
	{
		if (!unit.equals("dp") && !unit.equals("dip") && !unit.equals("px"))
		{
			throw new IllegalArgumentException("Unknown unit: " + unit);
		}
		this.screenUnit = unit;
	}

	public void setDefaultDirectory(String dir)
	{
		this.defaultDirectory = dir;
	}

	public String getDefaultDirectory()
	{
		return defaultDirectory;
	}
}
