package com.jb2011.drioduibuilder.swingw;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Stroke;

import javax.swing.JComponent;
import javax.swing.border.Border;

import com.jb2011.drioduibuilder.util.Utils;

/**
 * <p>
 * 一个简洁效果的可设定文本标题的组件.
 * 它是个Swing组件，目的是起占位和美化作用.
 * <p>
 * 
 * @author js, 2012-01-04
 * @version 1.1
 */
public class ETitledLine extends JComponent
{
    /** 标题内容 */
    private String title = null;
    /** 标题颜色（默认深灰色new Color(102,102,102)） */
    private Color titleColor = new Color(102,102,102);
    /** 标题字体（默认new Font("宋体", Font.BOLD, 12)） */
    private Font titleFont = new Font("宋体", Font.BOLD, 12); 
    
    /** 底线1的宽度 */
    private int bottomLine1Width = -1;
    /** 底线1的高度，默认4 */
    private int bottomLine1Height = 4;
    /** 底线1的颜色，默认 嫩绿色new Color(144,201,0) */
    private Color bottomLine1Color = new Color(144,201,0);
    
    /** 底线2的虚线实体长度 */
    private int bottomLine2EntityOfStroke = 1;
    /** 底线2的虚线空白长度 */
    private int bottomLine2SpacingOfStroke = 1;
    /** 底线2的虚线颜色（默认浅灰色new Color(180,180,180)） */
    private Color bottomLine2Color = new Color(180,180,180);
    
    public ETitledLine()
    {
    }
    
    public ETitledLine(String title)
    {
    	this.title = title;
    	
    	refreshBottomLine1Width();
    }
    
    private void refreshBottomLine1Width()
    {
    	bottomLine1Width = needDrawTitle()?Utils.getStrPixWidth(titleFont, getTitle()):0;
    }
    
    @Override
    public Dimension getPreferredSize() {
    	Border b = this.getBorder();
    	Insets its = null;
    	if(b != null)
    	{
    		its = b.getBorderInsets(this);
    	}
    	return new Dimension(100+(its != null?its.left+its.right:0)
    			,(needDrawTitle()?Utils.getStrPixHeight(titleFont):0) + bottomLine1Height+(its != null?its.top+its.bottom:0));
    }

    public void paint(Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g;
//        Paint oldPaint = g2.getPaint();
        
        Border b = this.getBorder();
    	Insets its = null;
    	if(b != null)
    	{
    		its = b.getBorderInsets(this);
    	}
    	
        int y = (its!=null?its.top:0);
        int x = (its!=null?its.left:0);
        
        //先计算出阴影线要绘制的开始坐标
        int titleWidth = needDrawTitle()?g2d.getFontMetrics().stringWidth(title):0;
        int startX = titleWidth==0?0:titleWidth+5;
        
        //绘制标题
        if(needDrawTitle())
        {
        	g2d.setFont(titleFont);
        	//先以白底绘制标题（以使之产生立体感）
        	g2d.setColor(new Color(255,255,255));
        	g2d.drawString(title, x, (y + g2d.getFontMetrics().getAscent()) - 1);
        	
        	//再以标题颜色绘制真正的标题内容
        	g2d.setColor(titleColor);
        	g2d.drawString(title, x, (y + g2d.getFontMetrics().getAscent()));
        }
        
        y += (needDrawTitle()?Utils.getStrPixHeight(titleFont):0);
        //------------------------------------- 底线绘制开始
        //** 绘制底线1（短实线区）
        g2d.setColor(bottomLine1Color);
        g2d.fillRoundRect(x, y, bottomLine1Width, bottomLine1Height, 0, 0);//填充的宽度是字段宽度
        
        y += bottomLine1Height - 1;//这里-1是为了使虚线Y开始绘制的坐标与底线1的底刚好在同一水平线
        //** 绘制底线2（长虚线区）
        Stroke oldStroke = g2d.getStroke();
		Stroke sroke = new BasicStroke(1, BasicStroke.CAP_BUTT,
				BasicStroke.JOIN_BEVEL, 0, new float[]
				{bottomLine2EntityOfStroke, bottomLine2SpacingOfStroke}, 0);//实线，空白
		g2d.setStroke(sroke);
		g2d.setColor(bottomLine2Color);
		g2d.drawLine(x+bottomLine1Width+1,y, getWidth(),y);//从底线1长度+1处开始绘度（+1是为了好看一点而已）
		g2d.setStroke(oldStroke);
        
        //------------------------------------- 底线绘制END
    	
    }
    
    public boolean needDrawTitle()
    {
    	return title!=null;
    }
    public String getTitle()
	{
		return title;
	}
	public ETitledLine setTitle(String title)
	{
		this.title = title;
		refreshBottomLine1Width();
		return this;
	}

	public Color getTitleColor()
	{
		return titleColor;
	}
	public ETitledLine setTitleColor(Color titleColor)
	{
		this.titleColor = titleColor;
		return this;
	}

	public Font getTitleFont()
	{
		return titleFont;
	}
	public ETitledLine setTitleFont(Font titleFont)
	{
		this.titleFont = titleFont;
		return this;
	}

	public int getBottomLine1Width()
	{
		return bottomLine1Width;
	}
	public ETitledLine setBottomLine1Width(int bottomLine1Width)
	{
		this.bottomLine1Width = bottomLine1Width;
		return this;
	}

	public int getBottomLine1Height()
	{
		return bottomLine1Height;
	}
	public ETitledLine setBottomLine1Height(int bottomLine1Height)
	{
		this.bottomLine1Height = bottomLine1Height;
		return this;
	}

	public Color getBottomLine1Color()
	{
		return bottomLine1Color;
	}
	public ETitledLine setBottomLine1Color(Color bottomLine1Color)
	{
		this.bottomLine1Color = bottomLine1Color;
		return this;
	}

	public int getBottomLine2EntityOfStroke()
	{
		return bottomLine2EntityOfStroke;
	}
	public ETitledLine setBottomLine2EntityOfStroke(int bottomLine2EntityOfStroke)
	{
		this.bottomLine2EntityOfStroke = bottomLine2EntityOfStroke;
		return this;
	}

	public int getBottomLine2SpacingOfStroke()
	{
		return bottomLine2SpacingOfStroke;
	}
	public ETitledLine setBottomLine2SpacingOfStroke(int bottomLine2SpacingOfStroke)
	{
		this.bottomLine2SpacingOfStroke = bottomLine2SpacingOfStroke;
		return this;
	}

	public Color getBottomLine2Color()
	{
		return bottomLine2Color;
	}
	public ETitledLine setBottomLine2Color(Color bottomLine2Color)
	{
		this.bottomLine2Color = bottomLine2Color;
		return this;
	}
}
