package com.jb2011.drioduibuilder.swingw;

import org.jb2011.lnf.beautyeye.utils.NinePatchHelper;
import org.jb2011.lnf.beautyeye.utils.RawCache;
import org.jb2011.ninepatch4j.NinePatch;

/**
 * Object factory of NinePatch pictures(*.9.png).
 * 
 * @author Jack Jiang
 * @version 1.0
 */
public class NPIconFactory extends RawCache<NinePatch>
{
	/** root path(relative this NPIconFactory.class). */
	public final static String IMGS_ROOT="imgs/np";

	/** The instance. */
	private static NPIconFactory instance = null;

	/**
	 * Gets the single instance of __Icon9Factory__.
	 *
	 * @return single instance of __Icon9Factory__
	 */
	public static NPIconFactory getInstance()
	{
		if(instance==null)
			instance = new NPIconFactory();
		return instance;
	}
	
	/* (non-Javadoc)
	 * @see org.jb2011.lnf.beautyeye.utils.RawCache#getResource(java.lang.String, java.lang.Class)
	 */
	@Override
	protected NinePatch getResource(String relativePath, Class baseClass)
	{
		return NinePatchHelper.createNinePatch(baseClass.getResource(relativePath), false);
	}

	/**
	 * Gets the raw.
	 *
	 * @param relativePath the relative path
	 * @return the raw
	 */
	public NinePatch getRaw(String relativePath)
	{
		return  getRaw(relativePath,this.getClass());
	}

	public NinePatch getSwitchable_btn_nornal()
	{
		return getRaw(IMGS_ROOT+"/switchable_btn_nornal.9.png");
	}
	public NinePatch getSwitchable_btn_pressed()
	{
		return getRaw(IMGS_ROOT+"/switchable_btn_pressed.9.png");
	}
	public NinePatch getSwitchable_next_normal()
	{
		return getRaw(IMGS_ROOT+"/switchable_next_normal.9.png");
	}
	public NinePatch getSwitchable_previous_normal()
	{
		return getRaw(IMGS_ROOT+"/switchable_previous_normal.9.png");
	}
	
	public NinePatch getSwitchable_next_pressed()
	{
		return getRaw(IMGS_ROOT+"/switchable_next_pressed.9.png");
	}
	public NinePatch getSwitchable_previous_pressed()
	{
		return getRaw(IMGS_ROOT+"/switchable_previous_pressed.9.png");
	}
}