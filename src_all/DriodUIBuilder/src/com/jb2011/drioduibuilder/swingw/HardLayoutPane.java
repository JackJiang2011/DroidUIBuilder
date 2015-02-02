package com.jb2011.drioduibuilder.swingw;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * 一个使用硬（固定）布局的面板.<br>
 * 实际上所有需要硬（固定）布局的组件是放置在一个GridBagLayout布局的子面板上的，它上面的组件
 * 一旦放上去之后位置就不会随着父面板的变化而改变，直接固定在上面.
 * 
 * @author Jack Jiang, 2010-12-25
 * @version 1.0
 */
public class HardLayoutPane extends JPanel
{
	/** 真正放置组件的面板才是该子面板，布局是GridBagLayout */
	private JPanel fixedSubPane = null;
	/**与GridBagLayout配套使用的配置类*/
	private GridBagConstraints gbc = new GridBagConstraints();
	/** 
	 * 添加的组件时的默认内衬，本字段是静态变量全局有效。
	 * 可以通过 {@link #setComponentInsets(Insets)}实例方法单独设置每一个面板实例 */
	public static Insets defaultComponentInsets = new Insets(4,5,1,5);
	
	/** 是否在添加的组件上添加“回车即移动焦点到下一个组件上”事件 */
	public static boolean addEnterKeyTrasfer  = true;//javax.swing.UIManager.get("zcsoftEnterDefault") != null;

	public HardLayoutPane()
	{
		parkedFixedSubPane();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 0;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		this.setComponentInsets(defaultComponentInsets);
	}
	
	/**
	 * 把子固定布局面板放入本面板中（放入之前会先设置本面板的布局）.
	 */
	protected void parkedFixedSubPane()
	{
		super.setLayout(new FlowLayout(FlowLayout.LEFT));
		fixedSubPane = createFixedSubPane();
		super.add(fixedSubPane);
	}
	
	/**
	 * 获得固定布局子面板.
	 * @return
	 */
	protected JPanel createFixedSubPane()
	{
		return new JPanel(new GridBagLayout());
	}
	
	/**
	 * 添加一个组件到固定面板上.
	 *
	 * @param willBeAddTo 要加入的组件
	 * @param comWidth 组件自定义高度
	 * @param comWidth 组件自定义宽度
	 * @param gridX 水平方向上网格跨度
	 * @param gridY 垂直方向上网格跨度
	 * @param gridWidth 水平方向上占据的网格跨度
	 * @param gridHeight 水平方向上占据的网格跨度
	 *
	 * 组件填充方式的确定：<br>
	 * 如果组件的宽度小于0且高度也小于0，填充方式为双向，否则<br>
	 * 如果组件的宽度小于0，填充方式为水平方向;如果等于零，则不进行填充，否则<br>
	 * 如果组件的高度小于0，填充方式为垂直方向。
	 * @see GridBagConstraints
	 */
	public void addTo(JComponent willBeAddTo,int comWidth,
		int comHeight,int gridX,int gridY,
		int gridWidth,int gridHeight)
	{
		gbc.gridx = gridX;
		gbc.gridy = gridY;
		gbc.gridwidth = gridWidth;
		gbc.gridheight = gridHeight;
		gbc.weightx = 0.0;
		gbc.weighty = 0.0;

		if ( comWidth <0 && comHeight <0 )
		{
			gbc.fill = GridBagConstraints.BOTH;
			gbc.weightx = 1.0;
			gbc.weighty = 1.0;
		}
		else if (comWidth <=0)
		{
			if ( comWidth == 0 )
				gbc.fill = GridBagConstraints.NONE;
			else
			{
				gbc.weightx = 1.0;
				gbc.fill = GridBagConstraints.HORIZONTAL;
			}
		}
		else if (comHeight <0 )
		{
			gbc.weighty = 1.0;
			gbc.fill = GridBagConstraints.VERTICAL;
		}
		else
		{
			gbc.fill = GridBagConstraints.NONE;
			willBeAddTo.setPreferredSize(new Dimension(comWidth,comHeight));
		}
		
		//需要增加“回车移动焦点到下一组件上”监听事件
		if (addEnterKeyTrasfer)
			//给其它组件增加（文本组件已经UI里实现了
			addEnterKeyTransferFocusImplExceptText(willBeAddTo);
		this.fixedSubPane.add(willBeAddTo,gbc);
	}

	/**
	 * 在最近添加的组件右旁添加组件，不进行填充。
	 * 水平和垂直方向上占据的网格个数为都为：1
	 *
	 * @param willBeAddTo 要添加的组件
	 * @see GridBagConstraints
	 */
	public Component addTo(Component willBeAddTo)
	{
		this.addTo(willBeAddTo, 1, false);
		return willBeAddTo;
	}

	/**
	 * 在最近添加的组件右旁添加组件，以无填充方式添加组件。
	 * 垂直方向上占据的网格个数为1
	 *
	 * @param willBeAddTo 要添加的组件
	 * @param gridWidth 水平方向上占据的网格个数
	 * @see GridBagConstraints
	 */
	public Component addTo(Component willBeAddTo, int gridWidth)
	{
		return this.addTo(willBeAddTo, gridWidth, false);
	}
	/**
	 * 在最近添加的组件右旁，以是否为水平填充方式添加组件。
	 * 水平和垂直方向上占据的网格个数为1
	 *
	 * @param willBeAddTo 要添加的组件
	 * @param horizonal 填充方式是否为水平
	 * @see GridBagConstraints
	 */
	public Component addTo(Component willBeAddTo, boolean horizonal)
	{
		return this.addTo(willBeAddTo, 1, horizonal);
	}

	/**
	 * 在最近添加的组件右旁，以是否为水平填充方式添加组件。
	 * 垂直方向上占据的网格个数为1
	 *
	 * @param willBeAddTo 要添加的组件
	 * @param gridWidth 水平方向上占据的网格个数
	 * @param horizonal 填充方式是否为水平
	 * @see GridBagConstraints
	 */
	public Component addTo(Component willBeAddTo, int gridWidth, boolean horizonal)
	{
		return this.addTo(willBeAddTo, gridWidth, 1, horizonal?GridBagConstraints.HORIZONTAL:GridBagConstraints.NONE);
	}

	/**
	 * 在最近添加的组件右旁，以指定的填充方式添加组件。
	 *
	 * @param willBeAddTo 要添加的组件
	 * @param gridWidth 水平方向上占据的网格个数
	 * @param gridHeight 垂直方向上占据的网格个数
	 * @param fill 填充方式
	 * @see GridBagConstraints
	 */
	public Component addTo(Component willBeAddTo, int gridWidth, int gridHeight, int fill)
	{
		return this.addTo(willBeAddTo, this.gbc.gridx + this.gbc.gridwidth
				, this.gbc.gridy, gridWidth, gridHeight, fill);
	}
	/**
	 * 按照指定的放置位置和占据的网格数添加组件，填充方式为水平。
	 *
	 * @param willBeAddTo 要添加的组件
	 * @param gridX 水平方向上网格位置
	 * @param gridY 垂直方向上网格位置
	 * @param gridWidth 水平方向上占据的网格个数
	 * @param gridHeight 垂直方向上占据的网格个数
	 * @see GridBagConstraints
	 */
	public Component addTo(Component willBeAddTo, int gridX, int gridY, int gridWidth, int gridHeight)
	{
		return this.addTo(willBeAddTo, gridX, gridY, gridWidth, gridHeight, GridBagConstraints.HORIZONTAL);
	}

	/**
	 * 按照指定的放置位置和占据的网格数，以指定的填充方式添加组件
	 * 
	 * @param willBeAddTo 要添加的组件
	 * @param gridX 水平方向上网格位置
	 * @param gridY 垂直方向上网格位置
	 * @param gridWidth 水平方向上占据的网格个数
	 * @param gridHeight 垂直方向上占据的网格个数
	 * @param fill 填充方式
	 * @see GridBagConstraints
	 */
	public Component addTo(Component willBeAddTo, int gridX, int gridY,
		int gridWidth, int gridHeight, int fill)
	{
		gbc.gridx = gridX;
		gbc.gridy = gridY;
		gbc.gridwidth = gridWidth;
		gbc.gridheight = gridHeight;
		if (willBeAddTo instanceof javax.swing.JLabel &&
			((JLabel)willBeAddTo).getHorizontalAlignment() == javax.swing.JLabel.RIGHT)
		{
			gbc.anchor = GridBagConstraints.NORTHEAST;
		}
		else
		{
			gbc.anchor = GridBagConstraints.NORTHWEST;
		}
		switch (fill)
		{
			case GridBagConstraints.NONE:
				gbc.weightx = 0.0;
				gbc.weighty = 0.0;
				break;
			case GridBagConstraints.HORIZONTAL:
				gbc.weightx = 1.0;
				gbc.weighty = 0.0;
				break;
			case GridBagConstraints.VERTICAL:
				gbc.weightx = 0.0;
				gbc.weighty = 1.0;
				break;
			default:
				gbc.weightx = 1.0;
				gbc.weighty = 1.0;
		}
		gbc.fill = fill;
		
		//需要增加“回车移动焦点到下一组件上”监听事件
		if (addEnterKeyTrasfer)
			//给其它组件增加（文本组件已经UI里实现了
			addEnterKeyTransferFocusImplExceptText(willBeAddTo);
		this.fixedSubPane.add(willBeAddTo, gbc);
		return willBeAddTo;
	}
	
	/**
	 * 给除文本组件外的其它加入本面板的组件添加“回车转移焦点到下一组件”的监听器.<br>
	 * 注:文本组件的相似监听器已经利用其全局action map在LNF UI中调用实现了.
	 * 
	 * @param willBeAddTo 要给其添加“回车转移焦点到下一组件”的监听器的组件
	 * @see LaunchRoot#spesificTextField()
	 * @see TextFocusAction#installTextActionMap()
	 */
	private void addEnterKeyTransferFocusImplExceptText(Component willBeAddTo)
	{
		//文本组件不需要添加了
		if(!(willBeAddTo instanceof javax.swing.text.JTextComponent))
			willBeAddTo.addKeyListener(TransferFocusImpl.SINGLETON);
	}

	/**
	 * 水平换行.<br>
	 * 本方法调用后，下一组件将在第gridX网格处开始添加.
	 *
	 * @param gridX 网络水平方向换行处
	 * @return 设置完毕后的网格布局控制实例
	 * @see GridBagConstraints
	 */
	public GridBagConstraints nextLine(int gridX)
	{
		this.gbc.gridx = gridX;
		this.gbc.gridwidth = 0;//水平方向上的前面没有添加过组件，故记录了最近添加的组件的水平宽度为零
		this.gbc.gridy ++;
		return gbc;
	}
	/**
	 * 水平换行.<br>
	 * 本方法调用后，下一组件将在第0网格处开始添加.
	 *
	 * @see #nextLine(int)
	 * @see GridBagConstraints
	 */
	public GridBagConstraints nextLine()
	{
		return this.nextLine(0);
	}
	
	/**
	 * 添加一个不带标题的分隔线组件.
	 * @return
	 */
	public JComponent addTitledLineSeparator()
	{
		return addTitledLineSeparator(null);
	}
	/**
	 * 添加一个带标题的分隔线组件.
	 * @param title 本参数为空表示不带标题
	 * @return
	 */
	public JComponent addTitledLineSeparator(String title)
	{
		return addTitledLineSeparator(title, null);//EBorderFactory.createCoolShadowLineGRAY(title));
	}
	public JComponent addTitledLineSeparator(String title,Color bottomLine1Color)
	{
		return addTitledLineSeparator(title,bottomLine1Color,null);
	}
	/**
	 * @param title
	 * @param bottomLine1Color
	 * @param bottomLine2Color
	 * @return
	 */
	public JComponent addTitledLineSeparator(String title,Color bottomLine1Color,Color bottomLine2Color)
	{
		ETitledLine el = new ETitledLine(title).setBottomLine1Height(2);
		if(bottomLine1Color != null)
			el.setBottomLine1Color(bottomLine1Color);
		if(bottomLine2Color != null)
			el.setBottomLine2Color(bottomLine2Color);
		return addLineSeparatorComponent(el);//EBorderFactory.createCoolShadowLineGRAY(title,titleColor,bgColor));
	}
	/**
	 * 添加一个分隔线组件，该组件可以是任意swing组件，以实现添加自定义分隔组件功能
	 * @param seperatorCom
	 * @return
	 */
	public JComponent addLineSeparatorComponent(JComponent seperatorCom)
	{
		this.nextLine();
		this.addTo(seperatorCom,99,true);
		this.nextLine();
		return seperatorCom;
	}
	
	/**
	 *  获取待添加的组件的边界
	 */
	public void setComponentInsets(java.awt.Insets newComponentInsets)
	{
		gbc.insets = newComponentInsets;
	}
	/**
	 * 设置待添加的组件的边界
	 */
	public java.awt.Insets getComponentInsets()
	{
		return gbc.insets;
	}

	/**
	 * 获取该面板实例的唯一子组件（面板容器）
	 * 通过方法addComToModel()添加组件时，组件实际添加到
	 * 该面板实例的唯一子组件上。
	 *
	 * @return 子组件,为面板容器。面板的布局器为GridGagLayout;
	 */
	public JPanel getFixedSubPane()
	{
		return this.fixedSubPane;
	}
	
	/**
	 * 按回车键则将当前组件的焦点移向下一个组件的监听器实现.
	 * 
	 * @author Jack Jiang, 2011-01-05
	 * @version 1.0
	 */
	public final static class TransferFocusImpl extends KeyAdapter
	{
		/** 唯一实例 */
		public static final KeyAdapter SINGLETON = new TransferFocusImpl();
		
	    public void keyPressed(KeyEvent e)
	    {
	    	//按回车键则将当前组件的焦点移向下一个组件
	        if(e.getKeyCode() == KeyEvent.VK_ENTER && e.getModifiers() == 0)
	            ((Component)e.getSource()).transferFocus();
	    }
	}
}
