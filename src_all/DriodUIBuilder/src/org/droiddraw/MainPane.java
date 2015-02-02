package org.droiddraw;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

import org.droiddraw.resource.IconFactory;
import org.droiddraw.util.DroidDrawHandler;
import org.droiddraw.widget.Layout;
import org.droiddraw.widget.Widget;

import com.jb2011.drioduibuilder.swingw.LaconicTip;

/**
 * DroidDraw的全局主面板.
 * 
 * @author refactoried by Jack Jiang, 2012-11-16
 */
public class MainPane extends JPanel
{
	/** 辅助组件：信息提示组件 */
	private static LaconicTip comInfoTip = new LaconicTip(false);
	/** 核心组件：ui布局主面板 */
	private MakeLayoutPane paneMakeLayout = null;
	/** 核心组件：主界面的中央工作区（各种Make面板都将分别独立放置其上的） */
	private JPanel paneWorkbench = new JPanel(new BorderLayout());

	public MainPane(String screen)
	{
		setLayout(new BorderLayout());
		initGUI(screen);
	}
	
	/**
	 * GUI core method.
	 *  
	 * @param screen
	 */
	protected void initGUI(String screen)
	{
		// 初始化主面板的导航工具条
		JToolBar toolbarMain = initNativeToolBar(screen);
		
		// TODO 以下2行代码是用来测试信息提示组件的，以后可以删除！
		comInfoTip.error("Hi, this is just used for test tips.");
		comInfoTip.showIt();
		
		// 总体布局
		add(toolbarMain, BorderLayout.NORTH);   // 导航工具条
		add(paneWorkbench, BorderLayout.CENTER);// 中央工作台
		add(comInfoTip, BorderLayout.SOUTH);    // 底部信息提示复合组件
	}
	
	/**
	 * 初始化化主界面的导航工具条.
	 * 
	 * @return
	 */
	private JToolBar initNativeToolBar(String screen)
	{
		JToolBar toolbarMain = new JToolBar(){
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				
				// 加一个灰色的android小图标作为装饰^_^
				ImageIcon decoratedAndroidLogo
					= IconFactory.getInstance().getNavigateTool_decoratedAndroidLogo_Icon();
				g.drawImage(decoratedAndroidLogo.getImage()
						, this.getWidth() - decoratedAndroidLogo.getIconWidth() - 8
						, (this.getHeight() - decoratedAndroidLogo.getIconHeight())/2, null);
			}
		};
		ButtonGroup toolbarGroup = new ButtonGroup();
		// 加入Make layout面板的导航按钮
		paneMakeLayout = new MakeLayoutPane(this, screen);// 本组件对引用要单独保存到全局变量以备后用
		addNativeComTo(toolbarGroup, toolbarMain, "Make layout", getPaneMakeLayout(), true)
			// 默认选中该JToggleButton
			.doClick();;
		// 加入Make Strings面板的导航按钮
		addNativeComTo(toolbarGroup, toolbarMain, "Make Strings", new MakeStringsPane(), false);
		// 加入Make Colors面板的导航按钮
		addNativeComTo(toolbarGroup, toolbarMain, "Make Colors", new MakeColorsPane(), false);
		// 加入Make Arrays面板的导航按钮
		addNativeComTo(toolbarGroup, toolbarMain, "Make Arrays", new MakeArraysPane(), false);
		
		return toolbarMain;
	}
	
	/**
	 * 将指定组件进入到主界面上的导航工具条.
	 * 
	 * @param toolbarGroup
	 * @param toolbarMain
	 * @param nativeShowText
	 * @param willToWorkbench
	 * @param isHot true则将在按钮右上角画一个"hot"样式的小图标
	 * @return
	 */
	private JToggleButton addNativeComTo(ButtonGroup toolbarGroup
			, JToolBar toolbarMain, String nativeShowText
			, final JComponent willToWorkbench, final boolean isHot)
	{
		AbstractAction actionNavigate = new AbstractAction(nativeShowText){
			public void actionPerformed(ActionEvent e){
				// 点击按钮则把它对象的组件设置到工作台
				setContentToCenter(willToWorkbench);
			}
		};
		// 导航按钮实例化
		JToggleButton btnNavigate = new JToggleButton(actionNavigate){
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				// 画"hot"小图标
				if(isHot)
				{
					ImageIcon hotIcon = org.droiddraw.resource.IconFactory
							.getInstance().getNavigateTool_hot_Icon();
					g.drawImage(hotIcon.getImage()
							, this.getWidth() - hotIcon.getIconWidth(), 2
							, hotIcon.getIconWidth(), hotIcon.getIconHeight(), null);
				}
			}
		};
		// 加入到工具条
		toolbarGroup.add(btnNavigate);
		toolbarMain.add(btnNavigate);
		return btnNavigate;
	}
	
	/**
	 * 将指定组件独占性地放入中央工作区.
	 * 
	 * @param com
	 */
	public void setContentToCenter(JComponent com)
	{
		// 按钮即使处于ButtonGroup里（形成RadioButton的形为），它在处于被选中状态
		// 后，再次点击它依然像正常状态一样触发事件，所以以下代码目的就是要解决：该
		// 组件已经加入到paneWorkbench后，就不需要重新removeAll再重复放入一遍了，
		// 否则岂不是浪费性能吗，呵呵
		Component[] coms = paneWorkbench.getComponents();
		if(coms != null)
		{
			for(Component childC:coms)
			{
				if(childC == com)
					return;
			}
		}
		
		// 正常放入组件
		paneWorkbench.removeAll();
		paneWorkbench.add(com, BorderLayout.CENTER);
		paneWorkbench.revalidate();
		paneWorkbench.repaint();
	}
	
	public MakeLayoutPane getPaneMakeLayout()
	{
		return this.paneMakeLayout;
	}
	
	public void save(File f)
	{
		try
		{
			AndroidEditor.instance().generate(new PrintWriter(new FileWriter(f)));
			AndroidEditor.instance().setChanged(false);
		}
		catch (IOException ex)
		{
			MainPane.getTipCom().error(ex);
		}
	}

	public void open(File f)
	{
		try{
			this.open(new FileReader(f));
		}
		catch (FileNotFoundException ex){
			MainPane.getTipCom().error(ex);
		}
	}

	public void open(FileReader r)
	{
		try{
			StringBuffer buff = new StringBuffer();
			char[] data = new char[4098];
			int read = r.read(data);
			while (read != -1)
			{
				buff.append(data, 0, read);
				read = r.read(data);
			}
			AndroidEditor.instance().removeAllWidgets();
			DroidDrawHandler.loadFromString(buff.toString());
			this.getPaneMakeLayout().getTextOutput().setText(buff.toString());
			this.repaint();
			AndroidEditor.instance().setChanged(false);
		}
		catch (Exception ex){
			MainPane.getTipCom().error(ex);
		}
	}

	//--------------------------------------------------------- about clear
	public void fireClearScreen(boolean confirm)
	{
		int res = JOptionPane.YES_OPTION;
		if (confirm)
		{
			res = JOptionPane.showConfirmDialog(JOptionPane.getRootFrame(),
					"This will delete your entire GUI.  Proceed?",
					"Clear Screen?", JOptionPane.YES_NO_OPTION);
		}
		if (res == JOptionPane.YES_OPTION)
		{
			clear();
			repaint();
		}
	}
	private void clear()
	{
		AndroidEditor.instance().removeAllWidgets();
		AndroidEditor.instance().select(AndroidEditor.instance().getRootLayout());
		this.getPaneMakeLayout().getTextOutput().setText("");
	}
	
	//--------------------------------------------------------- other utilities
	public static final void setupRootLayout(Layout l)
	{
		l.setPosition(AndroidEditor.OFFSET_X + l.getPadding(Widget.LEFT)
				+ l.getMargin(Widget.LEFT), AndroidEditor.OFFSET_Y
				+ l.getPadding(Widget.TOP) + l.getMargin(Widget.TOP));
		l.setPropertyByAttName("android:layout_width", "fill_parent");
		l.setPropertyByAttName("android:layout_height", "fill_parent");
		l.apply();
	}
	
	public static LaconicTip getTipCom()
	{
		return comInfoTip;
	}
}
