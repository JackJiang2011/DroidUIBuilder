package org.droiddraw.widget;

public class Ticker extends FrameLayout
{
	public static final String TAG_NAME = "Ticker";

	public Ticker()
	{
		this.setTagName(TAG_NAME);
		apply();
	}

	@Override
	public void addWidget(Widget w)
	{
		if (widgets.size() == 0)
			super.addWidget(w);
	}
}
