package org.droiddraw;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Hashtable;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.tree.TreeModel;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEdit;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.StringEscapeUtils;
import org.droiddraw.gui.AndroidEditorViewer;
import org.droiddraw.gui.Preferences;
import org.droiddraw.gui.PropertiesPanel;
import org.droiddraw.property.Property;
import org.droiddraw.property.StringProperty;
import org.droiddraw.util.ArrayHandler;
import org.droiddraw.util.ColorHandler;
import org.droiddraw.util.StringHandler;
import org.droiddraw.widget.AbstractWidget;
import org.droiddraw.widget.Button;
import org.droiddraw.widget.CheckBox;
import org.droiddraw.widget.Layout;
import org.droiddraw.widget.Widget;
import org.droiddraw.widget.WidgetTreeModel;
import org.xml.sax.SAXException;

public class AndroidEditor
{
	public static enum ScreenMode
	{
		QVGA_LANDSCAPE, QVGA_PORTRAIT, HVGA_LANDSCAPE, HVGA_PORTRAIT, WVGA_LANDSCAPE, WVGA_PORTRAIT
	}

//	public static int MAJOR_VERSION = 0;
//	public static int MINOR_VERSION = 23;

	Layout rootLayout;
	Widget selected;
	AndroidEditorViewer viewer;
	ScreenMode screen;
	int sx, sy;
	PropertiesPanel pp;
	File stringFile = null;
	Hashtable<String, String> strings;
	File colorFile = null;
	Hashtable<String, Color> colors;
	File arrayFile = null;
	Hashtable<String, Vector<String>> arrays;
	Preferences prefs;

	UndoManager undo;

	Vector<ChangeListener> changeListeners;

	boolean changed;

	File drawable_dir;
//	URLOpener opener;

	String theme;
	WidgetTreeModel treeModel;

	public static int OFFSET_X = 0;
	public static int OFFSET_Y = 48;

	private static AndroidEditor inst;

	private AndroidEditor()
	{
		this(ScreenMode.HVGA_PORTRAIT);
	}

	public TreeModel getLayoutTreeModel()
	{
		return treeModel;
	}

//	public boolean isLatestVersion()
//	{
//		try
//		{
//			URL u = new URL("http://www.droiddraw.org/version.txt");
//			BufferedReader in = new BufferedReader(new InputStreamReader(u
//					.openStream()));
//			String line = in.readLine();
//			StringTokenizer tok = new StringTokenizer(line);
//			int major = Integer.parseInt(tok.nextToken());
//			int minor = Integer.parseInt(tok.nextToken());
//			return MAJOR_VERSION > major
//					|| (MAJOR_VERSION == major && MINOR_VERSION >= minor);
//		}
//		catch (MalformedURLException ex)
//		{
//			return true;
//		}
//		catch (IOException ex)
//		{
//			return true;
//		}
//		catch (Exception ex)
//		{
//			return true;
//		}
//	}

	private AndroidEditor(ScreenMode mode)
	{
		setScreenMode(mode);
		this.pp = new PropertiesPanel(
//				false
				);
		this.colors = new Hashtable<String, Color>();
		this.strings = new Hashtable<String, String>();
		this.arrays = new Hashtable<String, Vector<String>>();
		this.undo = new UndoManager();
		this.changeListeners = new Vector<ChangeListener>();
		this.treeModel = new WidgetTreeModel();

		colors.put("black", Color.black);
		colors.put("darkgray", Color.darkGray);
		colors.put("gray", Color.gray);
		colors.put("lightgray", Color.lightGray);
		colors.put("red", Color.red);
		colors.put("green", Color.green);
		colors.put("blue", Color.blue);
		colors.put("yellow", Color.yellow);
		colors.put("cyan", Color.cyan);
		colors.put("magenta", Color.magenta);
		colors.put("white", Color.white);

		this.changed = false;
	}

	public void queueUndoRecord(UndoableEdit edit)
	{
		this.undo.addEdit(edit);
		setChanged(true);
	}

	public void undo()
	{
		if (undo.canUndo())
		{
			undo.undo();
			setChanged(true);
		}
	}

	public void redo()
	{
		if (undo.canRedo())
		{
			undo.redo();
			setChanged(true);
		}
	}

	public boolean isChanged()
	{
		return changed;
	}

	public void setChanged(boolean changed)
	{
		this.changed = changed;
		if (changed)
		{
			for (ChangeListener cl : changeListeners)
			{
				cl.stateChanged(new ChangeEvent(rootLayout));
			}
		}
	}

	public void addChangeListener(ChangeListener cl)
	{
		this.changeListeners.add(cl);
	}

	public PropertiesPanel getPropertiesPanel()
	{
		return pp;
	}

	public void setDrawableDirectory(File dir)
	{
		if (!dir.isDirectory())
		{
			throw new IllegalArgumentException(
					"Can not set drawable directory, given file is not a directory!");
		}
		this.drawable_dir = dir;
	}

	public File getDrawableDirectory()
	{
		return this.drawable_dir;
	}

	public BufferedImage findDrawable(String src)
	{
		if (this.getDrawableDirectory() == null)
		{
			return null;
		}
		int ix = src.indexOf("/");
		String file = src.substring(ix + 1);
		// System.out.println("Looking for: "+file);
		File f = new File(this.getDrawableDirectory(), file + ".png");
		if (!f.exists())
		{
			// try {
			// System.out.println(f.getCanonicalPath()+" doesn't exist!");
			// } catch (IOException ex) {
			// ex.printStackTrace();
			// }
			f = new File(this.getDrawableDirectory(), file + ".bmp");
		}
		if (!f.exists())
		{
			f = new File(this.getDrawableDirectory(), file + ".jpg");
		}
		if (!f.exists())
		{
			return null;
		}
		try
		{
			// System.out.println("Reading in: "+f.getCanonicalPath());
			return ImageIO.read(f);
		}
		catch (IOException ex)
		{
			MainPane.getTipCom().error(ex);
		}
		return null;
	}

	public Hashtable<String, String> getStrings()
	{
		return strings;
	}

	public void setStrings(Hashtable<String, String> strings)
	{
		this.strings = strings;
	}

	public File getStringFile()
	{
		return stringFile;
	}

	public void setStrings(File f)
	{
		try
		{
			setStrings(StringHandler.load(new FileInputStream(f)));
			stringFile = f;
		}
		catch (IOException ex)
		{
			MainPane.getTipCom().error(ex);
		}
		catch (SAXException ex)
		{
			MainPane.getTipCom().error(ex);
		}
		catch (ParserConfigurationException ex)
		{
			MainPane.getTipCom().error(ex);
		}
	}

	public File getColorFile()
	{
		return colorFile;
	}

	public void setColors(File f)
	{
		try
		{
			setColors(ColorHandler.load(new FileInputStream(f)));
			colorFile = f;
		}
		catch (IOException ex)
		{
			MainPane.getTipCom().error(ex);
		}
		catch (SAXException ex)
		{
			MainPane.getTipCom().error(ex);
		}
		catch (ParserConfigurationException ex)
		{
			MainPane.getTipCom().error(ex);
		}
	}

	public Hashtable<String, Color> getColors()
	{
		return colors;
	}

//	public void error(String message)
//	{
//		JOptionPane.showMessageDialog(viewer, message, "Error",
//				JOptionPane.WARNING_MESSAGE);
//	}
//
//	public void error(Exception ex)
//	{
//		error(ex.getMessage());
//		ex.printStackTrace();
//	}

//	public void message(String title, String message)
//	{
//		JOptionPane.showMessageDialog(viewer, message, title,
//				JOptionPane.INFORMATION_MESSAGE);
//	}

	public void setColors(Hashtable<String, Color> colors)
	{
		for (String key : colors.keySet())
		{
			colors.put(key, colors.get(key));
		}
	}

	public static AndroidEditor instance()
	{
		if (inst == null)
			inst = new AndroidEditor();
		return inst;
	}

	public ScreenMode getScreenMode()
	{
		return screen;
	}

	public void setScreenMode(ScreenMode mode)
	{
		this.screen = mode;
		if (screen == ScreenMode.QVGA_LANDSCAPE)
		{
			sx = 320;
			sy = 240;
		}
		else if (screen == ScreenMode.QVGA_PORTRAIT)
		{
			sx = 240;
			sy = 320;
		}
		else if (screen == ScreenMode.HVGA_LANDSCAPE)
		{
			sx = 480;
			sy = 320;
		}
		else if (screen == ScreenMode.HVGA_PORTRAIT)
		{
			sx = 320;
			sy = 480;
		}
		else if (screen == ScreenMode.WVGA_LANDSCAPE)
		{
			sx = 854;
			sy = 480;
		}
		else if (screen == ScreenMode.WVGA_PORTRAIT)
		{
			sx = 480;
			sy = 854;
		}
		else if (screen == ScreenMode.WVGA_LANDSCAPE)
		{
			sx = 854;
			sy = 480;
		}
		else if (screen == ScreenMode.WVGA_PORTRAIT)
		{
			sx = 480;
			sy = 854;
		}
		if (this.getRootLayout() != null)
		{
			this.getRootLayout().apply();
			for (Widget w : this.getRootLayout().getWidgets())
			{
				w.apply();
			}
			this.getRootLayout().repositionAllWidgets();
		}
	}

	public int getScreenX()
	{
		return sx;
	}

	public int getScreenY()
	{
		return sy;
	}

	public void setViewer(AndroidEditorViewer v)
	{
		this.viewer = v;
		this.pp.setViewer(v);
	}

	public void setIdsFromLabels()
	{
		setIdsFromLabels(rootLayout);
	}

	public void setIdsFromLabels(Layout l)
	{
		for (Widget w : l.getWidgets())
		{
			if (w instanceof Layout)
			{
				setIdsFromLabels((Layout) w);
			}
			else
			{
				Property p = w.getPropertyByAttName("android:text");
				if (p != null)
				{
					((AbstractWidget) w).setId("@+id/"
							+ ((StringProperty) p).getStringValue());
				}
			}
		}
	}

	public void setLayout(Layout l)
	{
		setLayout(l, true);
	}

	public void setLayout(Layout l, boolean fill)
	{
		if (fill)
		{
			l.setPropertyByAttName("android:layout_width", "fill_parent");
			l.setPropertyByAttName("android:layout_height", "fill_parent");
		}

		if (this.rootLayout != null)
		{
			Vector<Widget> widgets = rootLayout.getWidgets();
			for (Widget w : widgets)
			{
				l.addWidget(w);
			}
			this.rootLayout.removeAllWidgets();
		}
		this.rootLayout = l;
		if (l.getPropertyByAttName("xmlns:android") == null)
		{
			l.addProperty(new StringProperty("xmlns", "xmlns:android",
					"http://schemas.android.com/apk/res/android", false));

		}
		if (selected == null)
		{
			pp.setProperties(l.getProperties(), l);
		}
		treeModel.setRoot(this.rootLayout);
	}

	public boolean canSelect()
	{
		return (rootLayout != null && rootLayout.getWidgets().size() > 0);
	}

	public Layout getRootLayout()
	{
		return rootLayout;
	}
	
	/**
	 * 指定的组件实例是否就是rootLayout.
	 * 
	 * @param l
	 * @return true表示是rootLayout，否则不是
	 */
	public boolean isRootLayout(Widget l)
	{
		return l == rootLayout;
	}

	public Widget getSelected()
	{
		return selected;
	}

	public void select(Widget w)
	{
		// 如果之前有组件被设中，则首选置去掉该组件的选中状态
		if(selected != null && selected != w)
			selected.setSelect(false);
		
		// 原作的实现逻辑是rootLayout就不用设置它的选中状
		// 态了，这样比较符合使用习惯，仅此而已
		if (w == rootLayout)
		{
			selected = null;
			pp.setProperties(rootLayout.getProperties(), w);
		}
		else
		{
			// 保存选中组件的引用
			selected = w;
			
			// 同时通知该widget它已经被“选中"了
			if(selected != null)
				selected.setSelect(true);
		}
		
		if (w != null)
			pp.setProperties(w.getProperties(), w);
		pp.validate();
		pp.repaint();

		viewer.requestFocus();
		viewer.repaint();
	}

	public Vector<Layout> findLayouts(int x, int y)
	{
		return findLayout(rootLayout, x, y);
	}

	protected Vector<Layout> findLayout(Layout l, int x, int y)
	{
		Vector<Layout> res = new Vector<Layout>();
		if (l.clickedOn(x, y))
		{
			for (Widget w : l.getWidgets())
			{
				if (w instanceof Layout)
				{
					Vector<Layout> tmp = findLayout((Layout) w, x, y);
					for (Layout lt : tmp)
					{
						res.add(lt);
					}
				}
			}
			res.add(l);
		}
		return res;
	}

	public Vector<Widget> findWidgets(int x, int y)
	{
		return findWidgets(rootLayout, x, y);
	}

	public Vector<Widget> findWidgets(Layout l, int x, int y)
	{
		Vector<Widget> res = new Vector<Widget>();
		for (Widget w : l.getWidgets())
		{
			if (w.clickedOn(x, y))
			{
				if (w instanceof Layout)
				{
					Vector<Widget> tmp = findWidgets((Layout) w, x, y);
					for (Widget wt : tmp)
						res.add(wt);
				}
				res.add(w);
			}
		}
		return res;
	}

	public void generateSource(PrintWriter pw, String pkg)
	{
		pw.println("package " + pkg + ";");
		pw.println("public class DroidDrawSetup {");
		pw.println("public static void setup(Context c) {");
		generateSource(rootLayout, pw, pkg);
		pw.println("}");
		pw.println("}");
	}

	public void generateSource(Layout l, PrintWriter pw, String pkg)
	{
		for (Widget w : l.getWidgets())
		{
			generateSource(w, pw, pkg);
		}
	}

	public void generateSource(Widget w, PrintWriter pw, String pkg)
	{
		if (w instanceof Layout)
		{
			generateSource((Layout) w, pw, pkg);
		}
		else
		{
			if (w instanceof Button)
			{
				StringProperty onClick = (StringProperty) w
						.getPropertyByAttName("droiddraw:onClickListener");
				if (onClick != null && onClick.getStringValue() != null
						&& onClick.getStringValue().length() > 0)
				{
					String id = w.getId();
					int ix = id.indexOf("/");
					id = id.substring(ix + 1);
					pw.println("View b" + id + " = context.findViewById(R.ids."
							+ id + ";");
					pw.println("b" + id + ".setOnClickListener(new "
							+ onClick.getStringValue() + "());");
				}
			}
		}
	}

	public void generate(PrintWriter pw)
	{
		pw.println("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		generateWidget(rootLayout, pw);
		pw.flush();
	}

	@SuppressWarnings("unchecked")
	public void generateWidget(Widget w, PrintWriter pw)
	{
		pw.print("<" + w.getTagName());
		Vector<Property> props = (Vector<Property>) w.getProperties().clone();
		if (w != rootLayout)
			w.getParent().addOutputProperties(w, props);
		for (Property prop : props)
		{
			if (prop.getValue() != null
					&& prop.getValue().toString().length() > 0
					&& !prop.isDefault())
			{
				// Work around an android bug... *sigh*
				if (w instanceof CheckBox
						&& prop.getAtttributeName().equals("android:padding"))
					continue;
				String value;
				if (prop instanceof StringProperty)
				{
					value = StringEscapeUtils.escapeXml(((StringProperty) prop)
							.getRawStringValue());
				}
				else
				{
					value = prop.getValue().toString();
				}
				pw.println();
				pw
						.print("\t" + prop.getAtttributeName() + "=\"" + value
								+ "\"");
			}
		}
		if (w instanceof Layout)
		{
			pw.println(">");
			for (Widget wt : ((Layout) w).getWidgets())
			{
				generateWidget(wt, pw);
			}
			pw.println("</" + w.getTagName() + ">");
		}
		else
		{
			pw.println(" />");
		}
	}

//	public void setURLOpener(URLOpener open)
//	{
//		this.opener = open;
//	}
//
//	public URLOpener getURLOpener()
//	{
//		return opener;
//	}

	public File getArrayFile()
	{
		return arrayFile;
	}

	public void setArrayFile(File arrayFile)
	{
		this.arrayFile = arrayFile;
	}

	public Hashtable<String, Vector<String>> getArrays()
	{
		return arrays;
	}

	public void setArrays(Hashtable<String, Vector<String>> arrays)
	{
		this.arrays = arrays;
	}

	public void setArrays(File f)
	{
		setArrayFile(f);
		try
		{
			setArrays(ArrayHandler.load(new FileInputStream(f)));
		}
		catch (IOException ex)
		{
			MainPane.getTipCom().error(ex);
		}
		catch (SAXException ex)
		{
			MainPane.getTipCom().error(ex);
		}
		catch (ParserConfigurationException ex)
		{
			MainPane.getTipCom().error(ex);
		}
	}

	public String getTheme()
	{
		return theme;
	}

	public void setTheme(String theme)
	{
		this.theme = theme;
	}

	public Preferences getPreferences()
	{
		if (prefs == null)
		{
			prefs = new Preferences();
			try
			{
				prefs.load();
			}
			catch (SecurityException ex)
			{
			}
		}
		return prefs;
	}

	public String getScreenUnit()
	{
		Preferences prefs = getPreferences();
		if (prefs == null)
		{
			return "dp";
		}
		return prefs.getScreenUnit();
	}

	public void addWidget(Widget w, int x, int y)
	{
		viewer.addWidget(w, rootLayout, x, y);
	}

	public void removeWidget(Widget w)
	{
		if (w != null)
		{
			treeModel.removeWidget(w);
			w.getParent().removeWidget(w);
			if (selected == w)
			{
				selected = null;
			}
			changed = true;
		}
	}

	public void removeAllWidgets()
	{
		treeModel.removeAllWidgets(rootLayout);
		rootLayout.removeAllWidgets();
		selected = null;
		changed = true;
	}

	public WidgetTreeModel getTreeModel()
	{
		return treeModel;
	}
}
