package org.droiddraw.gui.deprecated;
//package org.droiddraw.gui.painter;
//
//import java.util.Hashtable;
//
//
//@Deprecated
////2012-11-20：由Jack Jiang标记为过时——目前为止不需要
////painter机制，它是droiddraw原作者的实现，目前的评估是没
////有多大必要，相反还使得代码复杂性增加，以后以下所有代码可
////能会被删除！
//public class WidgetPaintersRegistry
//{
//	static Hashtable<Class<?>, WidgetPainter> painters = new Hashtable<Class<?>, WidgetPainter>();
//
//	/**
//	 * 本方法在launch主类中被调用，用来设置指定类及其子类的Painter类.
//	 * 
//	 * @param c
//	 * @param wp
//	 */
//	public static void registerPainter(Class<?> c, WidgetPainter wp)
//	{
//		painters.put(c, wp);
//	}
//
//	public static WidgetPainter getPainter(Class<?> c)
//	{
//		Class<?> clazz = c;
//		while (clazz != null && !clazz.equals(Object.class))
//		{
//			if (painters.get(clazz) != null)
//				return painters.get(clazz);
//			clazz = clazz.getSuperclass();
//		}
//		return null;
//	}
//}
