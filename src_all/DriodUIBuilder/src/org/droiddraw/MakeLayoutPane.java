package org.droiddraw;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;

import org.droiddraw.gui.AndroidEditorViewer;
import org.droiddraw.gui.Desing$CodeSwitcherPane;
import org.droiddraw.gui.ImageResources;
import org.droiddraw.gui.WidgetPanel;
import org.droiddraw.property.ColorProperty;
import org.droiddraw.util.DroidDrawHandler;
import org.droiddraw.widget.AbsoluteLayout;
import org.droiddraw.widget.AbstractLayout;
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
import org.droiddraw.widget.ImageSwitcher;
import org.droiddraw.widget.ImageView;
import org.droiddraw.widget.Layout;
import org.droiddraw.widget.LinearLayout;
import org.droiddraw.widget.ListView;
import org.droiddraw.widget.MapView;
import org.droiddraw.widget.ProgressBar;
import org.droiddraw.widget.RadioButton;
import org.droiddraw.widget.RadioGroup;
import org.droiddraw.widget.RatingBar;
import org.droiddraw.widget.RelativeLayout;
import org.droiddraw.widget.ScrollView;
import org.droiddraw.widget.Spinner;
import org.droiddraw.widget.TabHost;
import org.droiddraw.widget.TabWidget;
import org.droiddraw.widget.TableLayout;
import org.droiddraw.widget.TableRow;
import org.droiddraw.widget.TextView;
import org.droiddraw.widget.Ticker;
import org.droiddraw.widget.TimePicker;
import org.droiddraw.widget.ToggleButton;
import org.droiddraw.widget.Widget;

import com.jb2011.drioduibuilder.swingw.HardLayoutPane;
import com.jb2011.drioduibuilder.swingw.SwitchablePane;
import com.jb2011.drioduibuilder.util.Platform;

public class MakeLayoutPane extends JPanel
{
	private JTextArea txtOutput;
	private JTabbedPane tabsWidgetsAndSoOn = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.WRAP_TAB_LAYOUT);
	private MainPane droidDrawPane = null;
	
	public MakeLayoutPane(MainPane droidDrawPane, String screen)
	{
		super(new BorderLayout());
		this.droidDrawPane = droidDrawPane;
		initGUI(screen);
	}
	
	private void initGUI(String screen)
	{
		//-------------------------------------------------------- must first init
		final JPanel paneOutput = initOutputUI();
		//
		final AndroidEditorViewer viewer = initAndroidEditorUI(screen);
		
		//-------------------------------------------------------- layout of toolbar
		// ************************************** 工具条主面板
		FlowLayout paneScreenToolsLayout = new FlowLayout(FlowLayout.LEFT);
		paneScreenToolsLayout.setVgap(0);
		JPanel paneScreenTools = new JPanel(paneScreenToolsLayout)
		{
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				// 在底部画一线灰线，视觉上好看一点
				Color oldColor = g.getColor();
				g.setColor(new Color(235,235,235));//239,239,239));
				// 左起5个像素与左边的彩带保持一致
				g.drawLine(5, this.getHeight()-1, this.getWidth(), this.getHeight()-1);
				g.setColor(oldColor);
				
				// 在左边画一个“Tools"字样的彩带背景，作为装饰
				ImageIcon hotIcon = org.droiddraw.resource.IconFactory
						.getInstance().getMakeLayoutHeadFlag_Icon();
				g.drawImage(hotIcon.getImage()
						, 2 // 左起2个像素
						, 4 // 下偏4个像素，对齐好看一些
						, hotIcon.getIconWidth(), hotIcon.getIconHeight(), null);
			}
		};
		// border设置的目的是为了给背景底边水平灰线留出绘制空间
		paneScreenTools.setBorder(BorderFactory.createEmptyBorder(0, 0, 3, 0));
		// 加入占位组件是为了给左头那的彩带背景留出绘制空间
		paneScreenTools.add(Box.createHorizontalStrut(68));
		
		// ************************************** 加入工具条组件1系列
		Object[] aeToolbar0Coms = initAndroidEditorToolbar0(paneScreenTools, viewer);
		// ************************************** 工具条主面板组件2系列
		initAndroidEditorToolbar1(paneScreenTools, viewer
				, (JComboBox)aeToolbar0Coms[0], (ActionListener)aeToolbar0Coms[1]);
		// ************************************** 工具条主面板组件3系列
		initAndroidEditorToolbar2(paneScreenTools, viewer);
		
		//-------------------------------------------------------- layout of screen area
		JPanel paneScreenArea = new JPanel();
		paneScreenArea.setLayout(new BorderLayout());
		paneScreenArea.add(viewer, BorderLayout.CENTER);
		
		//-------------------------------------------------------- layout of widgets tab
		JPanel paneWidgetsMain = initWidgetsPane();
		JPanel paneWidgetsMainParent = new JPanel(new FlowLayout(FlowLayout.LEFT));
		paneWidgetsMainParent.add(paneWidgetsMain);
		JPanel paneLayoutsMain = initLayoutsPane();
		// widget 复合组件面板
		SwitchablePane widgetsPane = new SwitchablePane(new Object[][]{
				new Object[]{new JScrollPane(paneWidgetsMainParent),"Widgets"}
				, new Object[]{paneLayoutsMain,"Layouts"}});
		
		tabsWidgetsAndSoOn.addTab("Widgets", widgetsPane.getMainPane());
		tabsWidgetsAndSoOn.addTab("Properties", AndroidEditor.instance().getPropertiesPanel());
		tabsWidgetsAndSoOn.addTab("！LayoutExplore", new JScrollPane(initTreeLayoutExplore()));// TODO TreeLayoutExplore稍后考虑放在一个更合理的地方！
//		tabsWidgetsAndSoOn.addTab("！Output", paneOutput);// TODO 此output面板稍后考虑放在一个更合理的地方！
		
		//-------------------------------------------------------- layout of （widgets的Tab和屏幕编辑面板）
		JSplitPane sp1 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT
				, new JScrollPane(paneScreenArea), tabsWidgetsAndSoOn);
		sp1.setBorder(BorderFactory.createEmptyBorder(1, 3, 0, 3));
		sp1.setDividerLocation(698);
		
		//-------------------------------------------------------- layout of 工具条+（widgets的Tab和屏幕编辑面板）
		final JPanel main = new JPanel(new BorderLayout());
		main.add(paneScreenTools, BorderLayout.NORTH);
		main.add(sp1, BorderLayout.CENTER);
		
		//======================================================== 总体布局：Desing主面板和Code主面板 
		final JPanel tabsSceenCenter = new JPanel(new BorderLayout());
		Desing$CodeSwitcherPane dcSwitcherPane = new Desing$CodeSwitcherPane(){
			@Override
			protected void fireLeftSelected(){
				setContentToCenter(main);
			}
			@Override
			protected void fireRightSelected(){
				setContentToCenter(paneOutput);
			}
			/**
			 * 将指定组件独占性地放入中央区.
			 * 
			 * @param com
			 */
			public void setContentToCenter(JComponent com)
			{
				// 正常放入组件
				tabsSceenCenter.removeAll();
				tabsSceenCenter.add(com, BorderLayout.CENTER);
				tabsSceenCenter.revalidate();
				tabsSceenCenter.repaint();
			}
		};
		this.add(tabsSceenCenter, BorderLayout.CENTER);// 主体内容组件
		this.add(dcSwitcherPane, BorderLayout.SOUTH);  // 下部的左右切换组件
	}
	
	private AndroidEditorViewer initAndroidEditorUI(String screen)
	{
		AndroidEditor ae = AndroidEditor.instance();
		AbsoluteLayout al = new AbsoluteLayout();
		MainPane.setupRootLayout(al);
		ae.setLayout(al);
		
		Image img = null;
		if ("qvgap".equals(screen))
		{
			ae.setScreenMode(AndroidEditor.ScreenMode.QVGA_PORTRAIT);
			img = ImageResources.instance().getImage("emu2");
		}
		else if ("hvgal".equals(screen))
		{
			ae.setScreenMode(AndroidEditor.ScreenMode.HVGA_LANDSCAPE);
			img = ImageResources.instance().getImage("emu3");
		}
		else if ("hvgap".equals(screen))
		{
			ae.setScreenMode(AndroidEditor.ScreenMode.HVGA_PORTRAIT);
			img = ImageResources.instance().getImage("emu4");
		}
		else if ("qvgal".equals(screen))
		{
			ae.setScreenMode(AndroidEditor.ScreenMode.QVGA_LANDSCAPE);
			img = ImageResources.instance().getImage("emu1");
		}
		else if ("wvgap".equals(screen))
		{
			ae.setScreenMode(AndroidEditor.ScreenMode.WVGA_PORTRAIT);
			img = ImageResources.instance().getImage("emu4");
		}
		else if ("wvgal".equals(screen))
		{
			ae.setScreenMode(AndroidEditor.ScreenMode.WVGA_LANDSCAPE);
			img = ImageResources.instance().getImage("emu1");
		}
		else if ("wvgap".equals(screen))
		{
			ae.setScreenMode(AndroidEditor.ScreenMode.HVGA_PORTRAIT);
			img = ImageResources.instance().getImage("emu4");
		}
		else if ("wvgal".equals(screen))
		{
			img = ImageResources.instance().getImage("emu1");
		}
		
		final AndroidEditorViewer viewer = new AndroidEditorViewer(ae, droidDrawPane, img);
		ae.setViewer(viewer);
		return viewer;
	}
	
	private Object[] initAndroidEditorToolbar0(JPanel paneScreenToolsfinal, final AndroidEditorViewer viewer)
	{
		JLabel lbl = new JLabel("Root Layout:");
		final JComboBox layout = new JComboBox(new String[] {
				AbsoluteLayout.TAG_NAME, LinearLayout.TAG_NAME,
				RelativeLayout.TAG_NAME, ScrollView.TAG_NAME,
				TableLayout.TAG_NAME, TabHost.TAG_NAME });
//		if (!System.getProperty("os.name").toLowerCase().contains("mac os x"))
		if(!Platform.isMac())
			layout.setLightWeightPopupEnabled(false);
		final ActionListener layoutActionListener = new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if (e.getActionCommand().equals("comboBoxChanged"))
				{
					String select = (String) ((JComboBox) e.getSource()).getSelectedItem();
					Layout l = null;
					if (select.equals(AbsoluteLayout.TAG_NAME))
						l = new AbsoluteLayout();
					else if (select.equals(LinearLayout.TAG_NAME))
						l = new LinearLayout();
					else if (select.equals(RelativeLayout.TAG_NAME))
						l = new RelativeLayout();
					else if (select.equals(ScrollView.TAG_NAME))
						l = new ScrollView();
					else if (select.equals(TableLayout.TAG_NAME))
						l = new TableLayout();
					else if (select.equals(TabHost.TAG_NAME))
						l = new TabHost();
					viewer.repaint();
					MainPane.setupRootLayout(l);
					AndroidEditor.instance().setLayout(l);
				}
			}
		};
		layout.addActionListener(layoutActionListener);
//		tb.add(layout);
		// This is 1.6.x specific *sigh*
		// sl.putConstraint(SpringLayout.BASELINE, lbl, 0,
		// SpringLayout.BASELINE, layout);
		// sl.putConstraint(SpringLayout.NORTH, tb, 5, SpringLayout.SOUTH,
		// layout);
		// p.setSize(200, 300);
		// p.validate();

		JComboBox screen_size = new JComboBox(new String[] { "QVGA Landscape",
				"QVGA Portrait", "HVGA Landscape", "HVGA Portrait",
				"WVGA Landscape", "WVGA Portrait" });
		screen_size.setSelectedIndex(3);
		
		screen_size.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				JComboBox jcb = (JComboBox) e.getSource();
				int ix = jcb.getSelectedIndex();
				AndroidEditor ae = AndroidEditor.instance();
				switch (ix)
				{
					case 0:
						ae.setScreenMode(AndroidEditor.ScreenMode.QVGA_LANDSCAPE);
						viewer.resetScreen(ImageResources.instance().getImage("emu1"));
						break;
					case 1:
						ae.setScreenMode(AndroidEditor.ScreenMode.QVGA_PORTRAIT);
						viewer.resetScreen(ImageResources.instance().getImage("emu2"));
						break;
					case 2:
						ae.setScreenMode(AndroidEditor.ScreenMode.HVGA_LANDSCAPE);
						viewer.resetScreen(ImageResources.instance().getImage("emu3"));
						break;
					case 3:
						ae.setScreenMode(AndroidEditor.ScreenMode.HVGA_PORTRAIT);
						viewer.resetScreen(ImageResources.instance().getImage("emu4"));
						break;
					case 4:
						ae.setScreenMode(AndroidEditor.ScreenMode.WVGA_LANDSCAPE);
						viewer.resetScreen(ImageResources.instance().getImage("emu5"));
						setSize(1000, 750);
						break;
					case 5:
						ae.setScreenMode(AndroidEditor.ScreenMode.WVGA_PORTRAIT);
						viewer.resetScreen(ImageResources.instance().getImage("emu6"));
						setSize(1000, 750);
						break;
				}
//				jsp.validate(); // TODO comment by Jck Jiang, 有用吗？
				viewer.repaint();
			}
		});
		validate();
		
		paneScreenToolsfinal.add(lbl);
		paneScreenToolsfinal.add(layout);
		paneScreenToolsfinal.add(new JLabel("Screen Size:"));
		paneScreenToolsfinal.add(screen_size);
		
		return new Object[]{layout,layoutActionListener};
	}
	
	private void initAndroidEditorToolbar1(JPanel paneScreenTools, final AndroidEditorViewer viewer
			, final JComboBox layout, final ActionListener layoutActionListener)
	{
		JButton gen = new JButton("Generate");
		gen.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0){
				// TODO 功能待进一步考虑！
				JOptionPane.showMessageDialog(viewer
						, "本功能因设计不合理，稍后将去掉！", "！该功能有待进一步评估！",
						JOptionPane.INFORMATION_MESSAGE);
				
				StringWriter sw = new StringWriter();
				AndroidEditor.instance().generate(new PrintWriter(sw));
				getTextOutput().setText(sw.getBuffer().toString());
			}
		});
		
		JButton undo = new JButton("Undo");
		undo.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				AndroidEditor.instance().undo();
			}
		});
		JButton redo = new JButton("Redo");
		redo.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				AndroidEditor.instance().redo();
			}
		});
		
		JButton load = new JButton("Load");
		load.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0){
				// TODO 功能待进一步考虑！
				JOptionPane.showMessageDialog(viewer
						, "本功能因设计不合理，稍后将去掉！", "！该功能有待进一步评估！",
						JOptionPane.INFORMATION_MESSAGE);
				try{
					AndroidEditor.instance().removeAllWidgets();
					String layoutXML = getTextOutput().getText();
					if (layoutXML.length() > 0)
						DroidDrawHandler.loadFromString(layoutXML);
					layout.removeActionListener(layoutActionListener);
					layout.setSelectedItem(AndroidEditor.instance().getRootLayout().toString());
					layout.addActionListener(layoutActionListener);
					viewer.repaint();
				}
				catch (Exception ex){
					MainPane.getTipCom().error(ex);
				}
			}
		});
		
		JSeparator sp = new JSeparator(JSeparator.VERTICAL);
		sp.setPreferredSize(new Dimension(2,22));
		paneScreenTools.add(sp);
		paneScreenTools.add(setToolbarBtnMargin(gen));
		paneScreenTools.add(setToolbarBtnMargin(load));
		paneScreenTools.add(setToolbarBtnMargin(undo));
		paneScreenTools.add(setToolbarBtnMargin(redo));
		paneScreenTools.add(setToolbarBtnMargin(
				new JButton(new ClearAction())));
	}
	
	private void initAndroidEditorToolbar2(JPanel paneScreenTools, final AndroidEditorViewer viewer)
	{
		JButton edit;
		edit = new JButton("Edit");
		edit.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				// TODO 功能待进一步考虑！
				JOptionPane.showMessageDialog(viewer
						, "本功能因设计不合理，稍后将去掉(放入Widget对象的右键菜单里更合理)！", "！该功能有待进一步评估！",
						JOptionPane.INFORMATION_MESSAGE);
				editSelected();
			}
		});
		JButton delete = new JButton("Delete");
		delete.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0){
				// TODO 功能待进一步考虑！
				JOptionPane.showMessageDialog(viewer
						, "本功能因设计不合理，稍后将去掉(放入Widget对象的右键菜单里更合理)！", "！该功能有待进一步评估！",
						JOptionPane.INFORMATION_MESSAGE);
				AndroidEditor.instance().removeWidget(AndroidEditor.instance().getSelected());
				viewer.repaint();
			}
		});

		paneScreenTools.add(setToolbarBtnMargin(edit));
		paneScreenTools.add(setToolbarBtnMargin(delete));
	}
	private AbstractButton setToolbarBtnMargin(AbstractButton btn)
	{
		btn.setMargin(new Insets(4,3,4,3));
		return btn;
	}
	
	private JPanel initOutputUI()
	{
		txtOutput = new JTextArea(10, 50);
		txtOutput.getDocument().addUndoableEditListener(new UndoableEditListener(){
			public void undoableEditHappened(UndoableEditEvent e){
				AndroidEditor.instance().queueUndoRecord(e.getEdit());
			}
		});
		
		// TODO 为output文本框增加复制、粘贴等的代码等BeautyEye LNF里
		//      增加这些功能后就可以去掉了！
		final JPopupMenu popup = new JPopupMenu();
		JMenuItem it = new JMenuItem("Cut");
		popup.add(it);
		it.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0){
				if (getSelectedText() != null && getSelectedText().length() != 0){
					Toolkit.getDefaultToolkit().getSystemClipboard()
							.setContents(new StringSelection(getSelectedText()),null);
					deleteSelectedText();
				}
			}
		});
		it = new JMenuItem("Copy");
		it.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0){
				if (getSelectedText() != null && getSelectedText().length() != 0){
					Toolkit.getDefaultToolkit().getSystemClipboard()
							.setContents(new StringSelection(getSelectedText()),null);
				}
			}
		});
		Launch.addCopyAction(it);
		popup.add(it);
		it = new JMenuItem("Paste");
		it.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e){
				Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
				try{
					String txt = (String) c.getData(DataFlavor.stringFlavor);
					if (txt != null)
						insertText(txt);
				}
				catch (Exception ex){
					ex.printStackTrace();
				}
			}
		});
		popup.add(it);

		txtOutput.addMouseListener(new MouseAdapter()
		{
			public void mousePressed(MouseEvent e){
//				if (e.isPopupTrigger())
				// right button
				if (e.getButton() == MouseEvent.BUTTON3)
					popup.show(txtOutput, e.getX() + 3, e.getY() + 3);
			}
		});
		
		JPanel out = new JPanel();
		out.setLayout(new BorderLayout());
		out.add(new JScrollPane(txtOutput),BorderLayout.CENTER);
//		TitledBorder border = BorderFactory.createTitledBorder("Output");
//		out.setBorder(border);
		return out;
	}
	
	private JPanel initWidgetsPane()
	{
		final int BIG_WIDGET_DEFAULT_WIDTH = 55;
		final int BIG_WIDGET_DEFAULT_HEIGHT = 78;
		
		//-------------------------------------------------------
		JPanel paneWindgetMain = new JPanel();
		paneWindgetMain.setLayout(new BoxLayout(paneWindgetMain, BoxLayout.Y_AXIS));
		
		JPanel paneWidgetRow1 = new JPanel(getFlowLayout());
//		paneWidgetRow1.setBackground(new Color(66,66,66));
//		paneWidgetRow1.setBorder(BorderFactory.createLineBorder(Color.red));
		Button widgetButton = new Button(Button.TAG_NAME);
		((ColorProperty) widgetButton.getPropertyByAttName("android:textColor"))
				.setColorValue(Color.black);
		paneWidgetRow1.add(new WidgetPanel(widgetButton));
		
		ToggleButton togg = new ToggleButton("Toggle", "Toggle Off");
//		togg.setHeight(50);
		togg.setWidth(65);
		paneWidgetRow1.add(new WidgetPanel(togg));
		
		paneWidgetRow1.add(new WidgetPanel(new ImageButton()));
		
		TextView tvInTools = new TextView(TextView.TAG_NAME);
		tvInTools.setSize(80, Button.DEFAULT_CONTENT_HEIGHT);
//		((ColorProperty) tvInTools.getPropertyByAttName("android:textColor"))
//				.setColorValue(Color.black);
		paneWidgetRow1.add(new WidgetPanel(tvInTools));

		paneWindgetMain.add(paneWidgetRow1);

		//---------------------------- 2
		JPanel paneWidgetRow2 = new JPanel(getFlowLayout());
		ImageView ivInTools = new ImageView();
		ivInTools.setWidth(60);
		ivInTools.setHeight(Button.DEFAULT_CONTENT_HEIGHT);
		paneWidgetRow2.add(new WidgetPanel(ivInTools));
		
		EditView ev = new EditView(EditView.TAG_NAME);
		((ColorProperty) ev.getPropertyByAttName("android:textColor"))
				.setColorValue(Color.black);
		paneWidgetRow2.add(new WidgetPanel(ev));

		AutoCompleteTextView actv = new AutoCompleteTextView("AutoComplete");
		((ColorProperty) actv.getPropertyByAttName("android:textColor"))
				.setColorValue(Color.black);
		paneWidgetRow2.add(new WidgetPanel(actv));

		paneWindgetMain.add(paneWidgetRow2);
		
		//------------------------------- 3
		JPanel paneWidgetRow3 = new JPanel(getFlowLayout());
		CheckBox widgetCheckBox = new CheckBox(CheckBox.TAG_NAME);
		((ColorProperty) widgetCheckBox.getPropertyByAttName("android:textColor"))
				.setColorValue(Color.black);
		paneWidgetRow3.add(new WidgetPanel(widgetCheckBox));

		RadioButton widgetRadioButton = new RadioButton(RadioButton.TAG_NAME);
		((ColorProperty) widgetRadioButton.getPropertyByAttName("android:textColor"))
				.setColorValue(Color.black);
		paneWidgetRow3.add(new WidgetPanel(widgetRadioButton));
		
		RadioGroup rgInTools = new RadioGroup();
		rgInTools.setSize(46,Button.DEFAULT_CONTENT_HEIGHT);
		paneWidgetRow3.add(new WidgetPanel(rgInTools));
		
		paneWindgetMain.add(paneWidgetRow3);
		
		//------------------------------- 4
		JPanel paneWidgetRow4 = new JPanel(getFlowLayout());
		paneWidgetRow4.add(new WidgetPanel(new TabWidget()));
		
		RatingBar rbInTools = new RatingBar();
		rbInTools.setWidth(RatingBar.DEFAULT_START_WIDTH*2+2);// 在tools面板中默认只绘2颗星哦
		paneWidgetRow4.add(new WidgetPanel(rbInTools));
		
		Gallery g = new Gallery();
		g.setWidth(60);
		g.setHeight(Button.DEFAULT_CONTENT_HEIGHT);
		paneWidgetRow4.add(new WidgetPanel(g));
		
		paneWindgetMain.add(paneWidgetRow4);
		
		//------------------------------- 41
		JPanel paneWidgetRow41 = new JPanel(getFlowLayout());
		paneWidgetRow41.add(new WidgetPanel(new Spinner()));
		
		DigitalClock dcb = new DigitalClock();
		dcb.setSize(105, Button.DEFAULT_CONTENT_HEIGHT);
//		((ColorProperty) dcb.getPropertyByAttName("android:textColor"))
//				.setColorValue(Color.black);
		paneWidgetRow41.add(new WidgetPanel(dcb));
		
		AnalogClock ac = new AnalogClock();
		ac.setSize(50, 50);
		paneWidgetRow41.add(new WidgetPanel(ac));
		
		paneWindgetMain.add(paneWidgetRow41);
		
		//------------------------------- 5
		JPanel paneWidgetRow5 = new JPanel(getFlowLayout());
		DatePicker dp = new DatePicker();
		dp.setSize(DatePicker.getBgImg_small().getWidth(null)
				, DatePicker.getBgImg_small().getHeight(null));
		paneWidgetRow5.add(new WidgetPanel(dp));
		
		paneWidgetRow5.add(Box.createHorizontalStrut(7));

		TimePicker tp = new TimePicker();
		tp.setSize(TimePicker.getBgImg_small().getWidth(null)
				, TimePicker.getBgImg_small().getHeight(null));
		paneWidgetRow5.add(new WidgetPanel(tp));

		paneWindgetMain.add(paneWidgetRow5);
		
		//------------------------------- 6
		JPanel paneWidgetRow6 = new JPanel(getFlowLayout());
		paneWidgetRow6.add(new WidgetPanel(new ProgressBar()));
		
		ListView lvInTools = new ListView();
		lvInTools.setHeight(BIG_WIDGET_DEFAULT_HEIGHT);//!
		lvInTools.setWidth(BIG_WIDGET_DEFAULT_WIDTH);//!
		paneWidgetRow6.add(new WidgetPanel(lvInTools));
		
		GridView gvInTools = new GridView();
		gvInTools.setHeight(BIG_WIDGET_DEFAULT_HEIGHT);//!
		gvInTools.setWidth(BIG_WIDGET_DEFAULT_WIDTH);//!
		paneWidgetRow6.add(new WidgetPanel(gvInTools));
		
		MapView mapView = new MapView();
		mapView.setHeight(BIG_WIDGET_DEFAULT_HEIGHT);//!
		mapView.setWidth(BIG_WIDGET_DEFAULT_WIDTH);//!
		paneWidgetRow6.add(new WidgetPanel(mapView));
		
		paneWindgetMain.add(paneWidgetRow6);
		return paneWindgetMain;
	}
	private FlowLayout getFlowLayout()
	{
		return getFlowLayout(0,3);
	}
	private FlowLayout getFlowLayout(int hGap, int vGap)
	{
		FlowLayout fl = new FlowLayout(FlowLayout.LEFT);
		fl.setHgap(hGap);
		fl.setVgap(vGap);
		return fl;
	}
	
	private JPanel initLayoutsPane()
	{
		HardLayoutPane paneLayoutsMain = new HardLayoutPane();
		paneLayoutsMain.addTitledLineSeparator("Title1:");
		paneLayoutsMain.addTo(new WidgetPanel(new AbsoluteLayout()));
		paneLayoutsMain.addTo(new WidgetPanel(new FrameLayout()));
		paneLayoutsMain.nextLine();
		paneLayoutsMain.addTo(new WidgetPanel(new LinearLayout()));
		paneLayoutsMain.addTo(new WidgetPanel(new ScrollView()));

		paneLayoutsMain.addTitledLineSeparator("Title2:");
		paneLayoutsMain.addTo(new WidgetPanel(new RelativeLayout()));
		TableRow tr = new TableRow();
		// 为什么要它么强制设制呢？因为代码里默认width是fill_parent的
		// 而当前位于工具面板时就不需要这样了，否则很难看
		tr.setSize(AbstractLayout.DEFAULT_WRAP_CONTENT_WIDTH
				, tr.getHeight());
		paneLayoutsMain.addTo(new WidgetPanel(tr));
		paneLayoutsMain.nextLine();
		paneLayoutsMain.addTo(new WidgetPanel(new TableLayout()));
		paneLayoutsMain.addTo(new WidgetPanel(new Ticker()));
		
		paneLayoutsMain.addTitledLineSeparator("Title3:");
		paneLayoutsMain.addTo(new WidgetPanel(new ImageSwitcher()));
		
		return paneLayoutsMain;
	}
	
	private JTree initTreeLayoutExplore()
	{
		final JTree treeLayoutExplore = new JTree(AndroidEditor.instance().getLayoutTreeModel());
		treeLayoutExplore.setShowsRootHandles(true);
		treeLayoutExplore.addTreeSelectionListener(new TreeSelectionListener(){
			public void valueChanged(TreeSelectionEvent arg0){
				Widget w = (Widget) treeLayoutExplore.getLastSelectedPathComponent();
				AndroidEditor.instance().select(w);
			}
		});
		treeLayoutExplore.setBorder(BorderFactory.createTitledBorder("Layout Explorer"));
		treeLayoutExplore.setMinimumSize(new Dimension(200, 400));
		return treeLayoutExplore;
	}
	
	//--------------------------------------------------------- about etc..
	public String getSelectedText()
	{
		return txtOutput.getSelectedText();
	}

	public void deleteSelectedText()
	{
		String txt = txtOutput.getText();
		int start = txtOutput.getSelectionStart();
		int end = txtOutput.getSelectionEnd();
		if (end < txt.length())
			txtOutput.setText(txt.substring(0, start) + txt.substring(end + 1));
		else
			txtOutput.setText(txt.substring(0, start));
	}

	public void insertText(String txt)
	{
		int start = txtOutput.getSelectionStart();
		deleteSelectedText();
		txtOutput.insert(txt, start);
	}

//	@Override
//	public Dimension getMinimumSize()
//	{
//		return PREFFER_SIZE;
//	}

	public void selectAll()
	{
		txtOutput.selectAll();
	}
	
	public JTextArea getTextOutput()
	{
		return this.txtOutput;
	}

//	@Override
//	public Dimension getPreferredSize()
//	{
//		return PREFFER_SIZE;
//	}

	public void editSelected()
	{
		tabsWidgetsAndSoOn.setSelectedIndex(2);
	}

	
	//--------------------------------------------------------- about clear
	private class ClearAction extends AbstractAction
	{
		public ClearAction(){
			super("Clear");
		}

		public void actionPerformed(ActionEvent e){
			droidDrawPane.fireClearScreen(true);
		}
	}
}
