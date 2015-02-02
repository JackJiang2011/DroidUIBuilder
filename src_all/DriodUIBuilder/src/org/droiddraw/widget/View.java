package org.droiddraw.widget;

import java.awt.Graphics;

public class View extends AbstractWidget
{

	public static final String TAG_NAME = "View";

	public View()
	{
		super(TAG_NAME);
	}

	@Override
	protected int getContentHeight()
	{
		return 0;
	}

	@Override
	protected int getContentWidth()
	{
		return 0;
	}

	public void paint(Graphics g)
	{
		// 绘制背景
		super.paintBackground(g);
		
		g.setColor(background.getColorValue());
		g.fillRect(getX(), getY(), getWidth(), getHeight());
	}
}
