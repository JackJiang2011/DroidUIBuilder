package org.droiddraw.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Stack;
import java.util.Vector;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.lang.StringEscapeUtils;
import org.droiddraw.AndroidEditor;
import org.droiddraw.widget.AbsoluteLayout;
import org.droiddraw.widget.AnalogClock;
import org.droiddraw.widget.AutoCompleteTextView;
import org.droiddraw.widget.Button;
import org.droiddraw.widget.CheckBox;
import org.droiddraw.widget.DatePicker;
import org.droiddraw.widget.DigitalClock;
import org.droiddraw.widget.EditView;
import org.droiddraw.widget.FrameLayout;
import org.droiddraw.widget.Gallery;
import org.droiddraw.widget.GridView;
import org.droiddraw.widget.ImageButton;
import org.droiddraw.widget.ImageView;
import org.droiddraw.widget.Layout;
import org.droiddraw.widget.LinearLayout;
import org.droiddraw.widget.ListView;
import org.droiddraw.widget.ProgressBar;
import org.droiddraw.widget.RadioButton;
import org.droiddraw.widget.RadioGroup;
import org.droiddraw.widget.RatingBar;
import org.droiddraw.widget.RelativeLayout;
import org.droiddraw.widget.ScrollView;
import org.droiddraw.widget.Spinner;
import org.droiddraw.widget.TableLayout;
import org.droiddraw.widget.TableRow;
import org.droiddraw.widget.TextView;
import org.droiddraw.widget.Ticker;
import org.droiddraw.widget.TimePicker;
import org.droiddraw.widget.ToggleButton;
import org.droiddraw.widget.View;
import org.droiddraw.widget.Widget;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class DroidDrawHandler extends DefaultHandler
{
	Vector<String> all_props;

	Stack<Vector<String>> layout_props;
	Stack<Layout> layoutStack;

	Layout root;
	Widget widget;

	boolean requireLayout;

	public DroidDrawHandler(boolean requireLayout)
	{
		all_props = new Vector<String>();
		all_props.add("android:layout_width");
		all_props.add("android:layout_height");
		all_props.add("android:background");
		all_props.add("android:padding");
		all_props.add("android:id");
		all_props.add("android:visibility");
		all_props.add("android:layout_marginTop");
		all_props.add("android:layout_marginBottom");
		all_props.add("android:layout_marginLeft");
		all_props.add("android:layout_marginRight");

		layout_props = new Stack<Vector<String>>();
		layoutStack = new Stack<Layout>();
		root = null;
		this.requireLayout = requireLayout;
	}

	protected boolean isLayout(String name)
	{
		return name.endsWith("Layout") || name.equals("RadioGroup")
				|| name.equals("Ticker") || name.equals("TableRow")
				|| name.equals("ScrollView");
	}

	@Override
	public void startElement(String ignore_uri, String ignore_lname,
			String qName, Attributes atts)
		throws SAXException
	{
		if (isLayout(qName))
		{
			Layout l = null;
			Vector<String> l_props = new Vector<String>();
			if (qName.equals("AbsoluteLayout"))
				l = new AbsoluteLayout();
			else if (qName.equals("LinearLayout")
					|| (qName.equals("RadioGroup")))
			{
				if (qName.equals("LinearLayout"))
				{
					l = new LinearLayout();
				}
				else if (qName.equals("RadioGroup"))
				{
					l = new RadioGroup();
					l.setPropertyByAttName("android:checkedButton", getValue(
							atts, "android:checkedButton"));
				}
				l.setPropertyByAttName("android:gravity", getValue(atts,
						"android:gravity"));
				if (getValue(atts, "android:orientation") == null)
				{
					l.setPropertyByAttName("android:orientation", "horizontal");
				}
				else
				{
					l.setPropertyByAttName("android:orientation", getValue(
							atts, "android:orientation"));
				}
				if (getValue(atts, "android:weightSum") != null)
				{
					l.setPropertyByAttName("android:weightSum", getValue(atts,
							"android:weightSum"));
				}
				l_props.add("android:layout_gravity");
				l_props.add("android:layout_weight");
			}
			else if (qName.equals("RelativeLayout"))
			{
				l = new RelativeLayout();
				for (int i = 0; i < RelativeLayout.propNames.length; i++)
				{
					l_props.add(RelativeLayout.propNames[i]);
				}
			}
			else if (qName.equals("FrameLayout"))
			{
				l = new FrameLayout();
			}
			else if (qName.equals("TableLayout"))
			{
				l = new TableLayout();
				l.setPropertyByAttName("android:stretchColumns", getValue(atts,
						"android:stretchColumns"));
			}
			else if (qName.equals("TableRow"))
			{
				l = new TableRow();
				l_props.add("android:layout_column");
				l_props.add("android:layout_span");
			}
			else if (qName.equals("Ticker"))
			{
				l = new Ticker();
			}
			else if (qName.equals("ScrollView"))
			{
				l = new ScrollView();
			}
			if (layoutStack.size() == 0)
			{
				l.setPosition(AndroidEditor.OFFSET_X, AndroidEditor.OFFSET_Y);
				for (String prop : all_props)
				{
					if (getValue(atts, prop) != null)
					{
						l.setPropertyByAttName(prop, getValue(atts, prop));
					}
				}
				l.apply();
				root = l;
			}
			else
			{
				addWidget(l, atts);
			}
			layoutStack.push(l);
			layout_props.push(l_props);
		}
		else if (layoutStack.size() == 0 && requireLayout)
		{
			throw new SAXException("Error, no Layout!");
		}
		else
		{
			Widget w = null;
			if (qName.equals("Button"))
			{
				String txt = getValue(atts, "android:text");
				Button b = new Button(txt);
				w = b;
			}
			else if (qName.equals("TextView"))
			{
				String txt = getValue(atts, "android:text");
				w = new TextView(txt);
				if (getValue(atts, "android:textAlign") != null)
				{
					w.setPropertyByAttName("android:textAlign", getValue(atts,
							"android:textAlign"));
				}
			}
			else if (qName.equals("EditText"))
			{
				String txt = getValue(atts, "android:text");
				EditView et = new EditView(txt);
				String hint = getValue(atts, "android:hint");
				if (hint != null)
				{
					et.setPropertyByAttName("android:hint", hint);
				}
				for (int i = 0; i < EditView.propertyNames.length; i++)
				{
					et.setPropertyByAttName(EditView.propertyNames[i],
							getValue(atts, EditView.propertyNames[i]));
				}
				w = et;
			}
			else if (qName.equals("CheckBox") || qName.equals("RadioButton"))
			{
				String txt = getValue(atts, "android:text");
				if (qName.equals("CheckBox"))
					w = new CheckBox(txt);
				else if (qName.equals("RadioButton"))
					w = new RadioButton(txt);
				w.setPropertyByAttName("android:checked", getValue(atts,
						"android:checked"));
			}
			else if (qName.equals("DigitalClock"))
			{
				w = new DigitalClock();
			}
			else if (qName.equals("AnalogClock"))
			{
				w = new AnalogClock();
			}
			else if (qName.equals("TimePicker"))
			{
				w = new TimePicker();
			}
			else if (qName.equals("ListView"))
			{
				w = new ListView();
			}
			else if (qName.equals("Spinner"))
			{
				w = new Spinner();
			}
			else if (qName.equals("AutoCompleteTextView"))
			{
				w = new AutoCompleteTextView("AutoComplete");
			}
			else if (qName.equals("ImageButton"))
			{
				w = new ImageButton();
				w.setPropertyByAttName("android:src", getValue(atts,
						"android:src"));
			}
			else if (qName.equals("ImageView"))
			{
				w = new ImageView();
				w.setPropertyByAttName("android:src", getValue(atts,
						"android:src"));
			}
			else if (qName.equals("ProgressBar") || qName.equals("RatingBar"))
			{
				if (qName.equals("ProgressBar"))
				{
					w = new ProgressBar();
				}
				else
				{
					w = new RatingBar();
					for (int i = 0; i < RatingBar.propertyNames.length; i++)
					{
						w.setPropertyByAttName(RatingBar.propertyNames[i],
								getValue(atts, RatingBar.propertyNames[i]));
					}
				}
				for (int i = 0; i < ProgressBar.propertyNames.length; i++)
				{
					w.setPropertyByAttName(ProgressBar.propertyNames[i],
							getValue(atts, ProgressBar.propertyNames[i]));
				}
			}
			else if (qName.equals("View"))
			{
				w = new View();
			}
			else if (qName.equals("GridView"))
			{
				w = new GridView();
				for (int i = 0; i < GridView.propertyNames.length; i++)
				{
					w.setPropertyByAttName(GridView.propertyNames[i], getValue(
							atts, GridView.propertyNames[i]));
				}
			}
			else if (qName.equals("Gallery"))
			{
				w = new Gallery();
				for (int i = 0; i < Gallery.propertyNames.length; i++)
				{
					w.setPropertyByAttName(Gallery.propertyNames[i], getValue(
							atts, Gallery.propertyNames[i]));
				}
			}
			else if (qName.equals("DatePicker"))
			{
				w = new DatePicker();
			}
			else if (qName.equals("ToggleButton"))
			{
				w = new ToggleButton("", "");
				w.setPropertyByAttName("android:textOn", getValue(atts,
						"android:textOn"));
				w.setPropertyByAttName("android:textOff", getValue(atts,
						"android:textOff"));
			}
			if (w != null)
			{
				widget = w;
				addWidget(w, atts);
			}
		}
	}

	@Override
	public void endElement(String ns, String lName, String qName)
	{
		if (isLayout(qName))
		{
			layout_props.pop();
			layoutStack.pop();
		}
	}

	protected void addWidget(Widget w, Attributes atts)
	{
		if (w instanceof TextView)
		{
			for (int i = 0; i < TextView.propertyNames.length; i++)
			{
				w.setPropertyByAttName(TextView.propertyNames[i], getValue(
						atts, TextView.propertyNames[i]));
			}
		}

		for (String prop : all_props)
		{
			if (getValue(atts, prop) != null)
			{
				w.setPropertyByAttName(prop, getValue(atts, prop));
			}
		}
		if (layout_props.size() == 0 || layoutStack.size() == 0)
		{
			return;
		}
		for (String prop : layout_props.peek())
		{
			if (getValue(atts, prop) != null)
			{
				w.setPropertyByAttName(prop, getValue(atts, prop));
			}
		}
		Layout layout = layoutStack.peek();
		w.apply();
		if (layout instanceof LinearLayout)
		{
			w.setPosition(layout.getWidth(), layout.getHeight());
		}
		layout.addWidget(w);
		if (layout instanceof AbsoluteLayout)
		{
			int x = DisplayMetrics.readSize(getValue(atts, "android:layout_x"));
			int y = DisplayMetrics.readSize(getValue(atts, "android:layout_y"));
			w.setPropertyByAttName("android:layout_x", getValue(atts,
					"android:layout_x"));
			w.setPropertyByAttName("android:layout_y", getValue(atts,
					"android:layout_y"));
			w.setPosition(x, y);
		}
	}

	private String getValue(Attributes atts, String name)
	{
		return StringEscapeUtils.unescapeXml(atts.getValue(name));
	}

	public static void loadFromString(String content)
		throws SAXException, ParserConfigurationException, IOException
	{
		load(new InputSource(new StringReader(content)));
	}

	public static void load(File f)
		throws SAXException, ParserConfigurationException, IOException,
		FileNotFoundException
	{
		load(new FileReader(f));
	}

	public static void load(Reader r)
		throws SAXException, ParserConfigurationException, IOException
	{
		load(new InputSource(r));
	}

	public static void load(InputSource in)
		throws SAXException, ParserConfigurationException, IOException
	{
		DroidDrawHandler ddh = new DroidDrawHandler(true);
		parseInternal(ddh, in);
		AndroidEditor.instance().setLayout(ddh.root, false);
		AndroidEditor.instance().getRootLayout().repositionAllWidgets();
	}

	public static void parseInternal(DroidDrawHandler ddh, InputSource in)
		throws SAXException, ParserConfigurationException, IOException
	{

		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser = factory.newSAXParser();
		parser.parse(in, ddh);
	}

	public static Widget parseFromString(String content)
		throws SAXException, ParserConfigurationException, IOException
	{
		return parse(new InputSource(new StringReader(content)));
	}

	public static Widget parse(Reader r)
		throws SAXException, ParserConfigurationException, IOException
	{
		return parse(new InputSource(r));
	}

	public static Widget parse(InputSource in)
		throws SAXException, ParserConfigurationException, IOException
	{
		DroidDrawHandler ddh = new DroidDrawHandler(false);
		parseInternal(ddh, in);
		if (ddh.root != null)
		{
			return ddh.root;
		}
		else
		{
			return ddh.widget;
		}
	}
}
