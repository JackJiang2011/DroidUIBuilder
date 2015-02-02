package org.droiddraw.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import org.jb2011.lnf.beautyeye.utils.BEUtils;

public abstract class Desing$CodeSwitcherPane extends JPanel
{
	private JToggleButton btnLeft = null;
	private JToggleButton btnRight = null;
	private MouseListener btnCursorListener = null;
	private BtnPane btnPane = null;
	
	public Desing$CodeSwitcherPane()
	{
		initGUI();
		initListeners();
		initDefaultselected();
	}
	
	@Override
	public void paintComponent(Graphics g) 
	{
		super.paintComponent(g);
		
		// ==== 说明：以下装饰的高度与this.getBorder().getInsets().top的值是一致的
		// ==== 哦（目前是5），也就是说border的top高度至少应为5，否则装 饰将画不完全哦
		
		//------------------------------------------------------------------------------ START
		// 水平虚线
		Stroke oldStroke = ((Graphics2D)g).getStroke();
		Stroke sroke = new BasicStroke(1, BasicStroke.CAP_BUTT,
				BasicStroke.JOIN_BEVEL, 0, new float[]{2, 2}, 0);//实线，空白
		((Graphics2D)g).setStroke(sroke);
		Color oldColor = g.getColor();
		g.setColor(new Color(162,162,162));//new Color(235,235,235));////239,239,239));
		g.drawLine(0, 4, this.getWidth(), 4);
		g.setColor(oldColor);
		((Graphics2D)g).setStroke(oldStroke);
		
		//--------------------------------------------------------------------
		Color c = new Color(237,84,16);
		g.setColor(c);
		int w = this.getWidth();
		
		// 先填充一个装饰3角形（用于在UI上提示用户它被选中了）
		//       x2,y2
		//
		//x1,y1          x3,y3
		final int tW = 7,tH = 3; 
		final int DECORATED_UNDERLINE_HEIGHT = 2;
		int x1 = w/2 - tW/2;
		int y1 = DECORATED_UNDERLINE_HEIGHT+1;
		int x2 = w/2;
		int y2 = DECORATED_UNDERLINE_HEIGHT+1 - tH;
		int x3 = w /2 + tW/2;
		int y3 = DECORATED_UNDERLINE_HEIGHT+1;
		//反走样
		BEUtils.setAntiAliasing((Graphics2D)g, true);
		BEUtils.fillTriangle(g, x1, y1, x2, y2, x3, y3, c);
		BEUtils.setAntiAliasing((Graphics2D)g, false);
		
		// 再填充一个底线（用于装饰）
		final int BTTOM_LINE_WIDTH = 72;
		g.fillRect(w/2-BTTOM_LINE_WIDTH/2 - 1,DECORATED_UNDERLINE_HEIGHT+1
				, BTTOM_LINE_WIDTH, DECORATED_UNDERLINE_HEIGHT);//h);
//		g.fillRect(0,DECORATED_UNDERLINE_HEIGHT+1
//				, w, DECORATED_UNDERLINE_HEIGHT);//h);
		//------------------------------------------------------------------------------ END
	}
	
	private void initGUI()
	{
		// 实例化按钮
		ButtonGroup bg = new ButtonGroup();
		btnLeft = new JToggleButton("");
		btnRight = new JToggleButton("");
		btnLeft.setContentAreaFilled(false);
		btnLeft.setFocusable(false);
		btnLeft.setBorder(null);
		btnLeft.setPreferredSize(new Dimension(64,25));
		btnRight.setContentAreaFilled(false);
		btnRight.setFocusable(false);
		btnRight.setBorder(null);
		btnRight.setPreferredSize(new Dimension(64,25));
		
		// 加入到ButtonGroup(实现RadioButton的形为)
		bg.add(btnLeft);
		bg.add(btnRight);
		
		// 主面板布局
		FlowLayout mainLayout = new FlowLayout(FlowLayout.CENTER);
		mainLayout.setHgap(0);
		mainLayout.setVgap(2);
		this.setLayout(mainLayout);
		// 此处的border的设置是为了背景上部的装饰而做哦
		this.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
		
		// 实例化按钮面板
		btnPane = new BtnPane();
		btnPane.add(btnLeft);
		btnPane.add(btnRight);
		
		// 添加到总体布局中
		this.add(btnPane);
	}
	
	private void initListeners()
	{
		// 移动光标到组件上时更改光标为“手形”，移出时还原默认
		btnCursorListener = new MouseAdapter(){
			public void mouseEntered(MouseEvent e)
			{
				((JComponent)e.getSource()).setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			}
			public void mouseExited(MouseEvent e)
			{
				((JComponent)e.getSource()).setCursor(Cursor.getDefaultCursor());
			}
		};
		
		btnLeft.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				// 切换父面板的背景按钮
				btnPane.switchToLeft();
				
				// 处理额外事情
				fireLeftSelected();
				
				// 按钮即使处于ButtonGroup里（形成RadioButton的形为），它在处于被选中状态
				// 后，再次点击它依然像正常状态一样触发事件，所以以下代码目的就是要解决：当
				// 该按已经处理选中状态后，就不要让它能再次被点击了，否则重复这样的事件岂不浪费必能吗
				// 说明：单纯的通过判断isSelected()是无效的，因为达到此处代码时，它已经是selected==true了
				btnLeft.setEnabled(false);
				btnRight.setEnabled(true);

				// 手形光标的事件处理
				btnLeft.removeMouseListener(btnCursorListener);
				btnRight.addMouseListener(btnCursorListener);
			}
		});
		btnRight.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				// 切换父面板的背景按钮
				btnPane.switchToRight();
				
				// 处理额外事情
				fireRightSelected();
				
				// 按钮即使处于ButtonGroup里（形成RadioButton的形为），它在处于被选中状态
				// 后，再次点击它依然像正常状态一样触发事件，所以以下代码目的就是要解决：当
				// 该按已经处理选中状态后，就不要让它能再次被点击了，否则重复这样的事件岂不浪费必能吗
				// 说明：单纯的通过判断isSelected()是无效的，因为达到此处代码时，它已经是selected==true了
				btnLeft.setEnabled(true);
				btnRight.setEnabled(false);

				// 手形光标的事件处理
				btnRight.removeMouseListener(btnCursorListener);
				btnLeft.addMouseListener(btnCursorListener);
			}
		});
	}
	
	/**
	 * 本方法将指明本面板在首次实例化时默认设置哪个按钮为选中状态（即点击状态）.
	 * 
	 * <p>本类中默认选中的是左按钮.
	 * 
	 * @see AbstractButton#doClick()
	 * 
	 */
	protected void initDefaultselected()
	{
		btnLeft.doClick();
	}
	
	/**
	 * 当切换到左按钮时本方法将自动被调用.本方法默认什么也不做.
	 * 
	 * <p>子类应实现本方法，以便处理自定义的业务需求——比如切换到一个新面板.
	 */
	protected abstract void fireLeftSelected();
	
	/**
	 * 当切换到右按钮时本方法将自动被调用..本方法默认什么也不做.
	 * 
	 * <p>子类应实现本方法，以便处理自定义的业务需求——比如切换到一个新面板.
	 */
	protected abstract void fireRightSelected();
	
	//------------------------------------------------------------------------------------ 按钮面板实现类
	private enum BtnPaneStatus
	{
		left,
		right
	}
	
	private class BtnPane extends JPanel
	{
		private BtnPaneStatus status = BtnPaneStatus.left;
		
		public BtnPane()
		{
			FlowLayout btnPaneLayout = new FlowLayout(FlowLayout.CENTER);
			btnPaneLayout.setHgap(0);
			btnPaneLayout.setVgap(0);
			this.setLayout(btnPaneLayout);
			this.setPreferredSize(new Dimension(129,25));
		}
		
		/**
		 * 根据左右状态来决定面板的背景图片.
		 * 
		 * @return
		 */
		private ImageIcon getBgImage()
		{
			if(status == BtnPaneStatus.left)
				return org.droiddraw.resource.IconFactory.getInstance().getDAndCPane_Icon_left();
			else 
				return org.droiddraw.resource.IconFactory.getInstance().getDAndCPane_Icon_right();
		}
		
		@Override
		public void paintComponent(Graphics g) 
		{
			super.paintComponent(g);
			
			ImageIcon bgIcon = getBgImage();
			g.drawImage(bgIcon.getImage(), 0, 0, bgIcon.getIconWidth(),bgIcon.getIconHeight(), null);
		}
		
		public void switchToLeft()
		{
			this.status = BtnPaneStatus.left;
			this.repaint();
		}
		public void switchToRight()
		{
			this.status = BtnPaneStatus.right;
			this.repaint();
		}
	}
	
//	public static void main(String[] args)
//	{
//		JFrame frame = new JFrame("TTTTTTTTTTT");
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		
//		frame.getContentPane().add(new Desing$CodeSwitcherPane(){
//			@Override
//			protected void fireLeftSelected()
//			{
//				System.out.println("切换到左边了哦！");
//			}
//
//			@Override
//			protected void fireRightSelected()
//			{
//				System.out.println("切换到右边了哦！");
//			}
//		}, BorderLayout.SOUTH);
//		frame.setSize(1000, 715);
//		frame.setLocationRelativeTo(null);
//		frame.setVisible(true);
//	}
}
