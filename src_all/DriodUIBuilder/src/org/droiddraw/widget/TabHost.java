package org.droiddraw.widget;

import org.droiddraw.property.StringProperty;

/**
 * This class will provide TabHost functionality by containing a TabWidget and
 * its corresponding FrameLayout.
 * 
 * @author rey malahay <a href="mailto:reymalahay@gmail.com">Rey Malahay</a>
 * 
 */
public class TabHost extends FrameLayout
{

	public static final String TAG_NAME = "TabHost";
	/**
	 * This is the id that is displayed in the final xml file.
	 */
	private static final String ANDROID_ID = "@android:id/tabhost"; 

	/**
	 * Default constructor
	 */
	public TabHost()
	{
		this.setTagName(TAG_NAME);
		((StringProperty) this.getProperties().get(
				this.getProperties().indexOf(
						this.getPropertyByAttName("android:id"))))
				.setStringValue(ANDROID_ID);
	}
}