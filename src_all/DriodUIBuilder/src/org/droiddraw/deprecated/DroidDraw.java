package org.droiddraw.deprecated;
//package org.droiddraw;
//
//import java.awt.BorderLayout;
//import java.awt.Image;
//import java.awt.MediaTracker;
//import java.net.MalformedURLException;
//import java.net.URL;
//
//import javax.swing.JApplet;
//import javax.swing.UIManager;
//
//import org.droiddraw.gui.DroidDrawPanel;
//import org.droiddraw.gui.ImageResources;
//import org.droiddraw.gui.LayoutPainter;
//import org.droiddraw.gui.ScrollViewPainter;
//import org.droiddraw.gui.WidgetRegistry;
//import org.droiddraw.widget.AbstractLayout;
//import org.droiddraw.widget.Layout;
//import org.droiddraw.widget.ScrollView;
//
///**
// * DroidDraw的Applet主类.
// * 
// * 此类以后可以考虑删除，因为在Applet上运行本身就不实用，华而不实的东西完全可以无视之.
// */
//public class DroidDraw extends JApplet implements URLOpener,
//		ImageLoader.ImageLoaderInterface
//{
//	private static final long serialVersionUID = 1L;
//
//	protected static final void switchToLookAndFeel(String clazz)
//	{
//		try
//		{
//			UIManager.setLookAndFeel(clazz);
//		}
//		catch (Exception ex)
//		{
//			AndroidEditor.instance().error(ex);
//		}
//	}
//
//	public void openURL(String url)
//	{
//		try
//		{
//			getAppletContext().showDocument(new URL(url), "_blank");
//		}
//		catch (MalformedURLException ex)
//		{
//			AndroidEditor.instance().error(ex);
//		}
//	}
//
//	protected static final void setupRootLayout(Layout l)
//	{
//		l.setPosition(AndroidEditor.OFFSET_X, AndroidEditor.OFFSET_Y);
//		l.setPropertyByAttName("android:layout_width", "fill_parent");
//		l.setPropertyByAttName("android:layout_height", "fill_parent");
//		l.apply();
//	}
//
//	MediaTracker md;
//	int ix;
//
//	public void loadImage(String name)
//	{
//		Image img = getImage(getCodeBase(), "ui/" + name + ".png");
//		md.addImage(img, ix++);
//		ImageResources.instance().addImage(img, name);
//
//	}
//
//	@Override
//	public void init()
//	{
//		super.init();
//		AndroidEditor.instance().setURLOpener(this);
//		AndroidEditor.instance().getPropertiesPanel().setApplet(true);
//
//		// This is so that I can test out the Google examples...
//		// START
//		/*
//		 * if ("true".equals(this.getParameter("load_strings"))) { try { URL url
//		 * = new URL(getCodeBase(), "strings.xml");
//		 * AndroidEditor.instance().setStrings
//		 * (StringHandler.load(url.openStream())); } catch (Exception ex) {
//		 * AndroidEditor.instance().error(ex); } }
//		 */
//		// END
//
//		String screen = this.getParameter("Screen");
//		if (screen == null)
//		{
//			screen = "hvgap";
//		}
//		md = new MediaTracker(this);
//		ix = 0;
//
//		ImageLoader.loadImages(this);
//
//		for (int i = 0; i < ix; i++)
//		{
//			try
//			{
//				md.waitForID(i);
//			}
//			catch (InterruptedException ex)
//			{
//			}
//		}
//
//		WidgetRegistry.registerPainter(AbstractLayout.class,
//				new LayoutPainter());
//		WidgetRegistry.registerPainter(ScrollView.class,
//				new ScrollViewPainter());
//
//		setLayout(new BorderLayout());
//		setSize(1024, 650);
//		add(new DroidDrawPanel(screen, true), BorderLayout.CENTER);
//	}
//}
