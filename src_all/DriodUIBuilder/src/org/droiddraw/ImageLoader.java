/*
 * Copyright (C) 2015 Jack Jiang(cngeeker.com) The DroidUIBuilder Project. 
 * All rights reserved.
 * Project URL:https://github.com/JackJiang2011/DroidUIBuilder
 * Version 1.0
 * 
 * Jack Jiang PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * ImageLoader.java at 2015-2-6 16:12:00, original version by Jack Jiang.
 * You can contact author with jb2011@163.com.
 */
package org.droiddraw;

import org.droiddraw.widget.TabWidget;

public class ImageLoader
{
	public interface ImageLoaderInterface
	{
		public void loadImage(String path);
	}

	public static void loadImages(ImageLoaderInterface ld)
	{
		ld.loadImage("emu1");
		ld.loadImage("emu2");
		ld.loadImage("emu3");
		ld.loadImage("emu4");
		ld.loadImage("paint");
		ld.loadImage("droiddraw_small");
		ld.loadImage("paypal");
		ld.loadImage("gallery");

		ld.loadImage("background_01p");
//		ld.loadImage("background_01l");
		ld.loadImage("editor_content_bg");

		ld.loadImage("statusbar_background_p");
		ld.loadImage("statusbar_background_l");

		ld.loadImage("title_bar.9");
		ld.loadImage("stat_sys_data_connected");
		ld.loadImage("stat_sys_battery_charge_100");
		ld.loadImage("stat_sys_signal_3");
		ld.loadImage("stat_sys_time_img");// 为了更好的效果，状态栏上时间用此图片实现
		ld.loadImage("stat_sys_title_text_img");// 为了更好的效果，标题栏上标题用此图片实现

		ld.loadImage("scrollbar.9");
		ld.loadImage("scrollfield.9");

		ld.loadImage("mapview");
		ld.loadImage("listview_small");
		ld.loadImage("gridview_small");

		ld.loadImage("rate_star_big_on");
		ld.loadImage("rate_star_big_half");
		ld.loadImage("rate_star_big_off");
//		ld.loadImage("rate_star_med_on");
//		ld.loadImage("rate_star_small_on");
		
		ld.loadImage("date_picker");
		ld.loadImage("time_picker");
		ld.loadImage("date_picker_small");
		ld.loadImage("time_picker_small");

		ld.loadImage("light/checkbox_off_background");
		ld.loadImage("light/checkbox_on_background");
		ld.loadImage("light/clock_dial");
		ld.loadImage("light/clock_hand_hour");
		ld.loadImage("light/clock_hand_minute");
		ld.loadImage("light/radiobutton_off_background");
		ld.loadImage("light/radiobutton_on_background");
		ld.loadImage("light/button_background_normal.9");
		ld.loadImage("light/editbox_background_normal.9");
		ld.loadImage("light/progress_circular_background");
		ld.loadImage("light/progress_particle");
		ld.loadImage("light/progress_circular_indeterminate");
//		ld.loadImage("light/arrow_up_float");
//		ld.loadImage("light/arrow_down_float");
		ld.loadImage("light/spinnerbox_background_focus_yellow.9");
		ld.loadImage("light/spinnerbox_arrow_middle.9");

		ld.loadImage("def/btn_check_off");
		ld.loadImage("def/btn_check_on");

		ld.loadImage("def/btn_radio_off");
		ld.loadImage("def/btn_radio_on");

//		ld.loadImage("def/textfield.9");
//		ld.loadImage("def/btn_default_normal.9");
		ld.loadImage("def/progress_wheel_medium");

//		ld.loadImage("def/spinner_normal.9");
//		ld.loadImage("def/btn_dropdown_neither.9");
//		ld.loadImage(TabWidget.IMAGE_NAME);
//		ld.loadImage("mdpi/textfield_default.9");

		ld.loadImage("def/btn_toggle_off.9");
		ld.loadImage("def/btn_toggle_on.9");
		
		ld.loadImage("def/radio_group");

		/**
		 * applet list. loadImage("emu1"); loadImage("emu2"); loadImage("emu3");
		 * loadImage("emu4"); loadImage("emu5"); loadImage("emu6");
		 * loadImage("paint"); loadImage("droiddraw_small");
		 * loadImage("paypal");
		 * 
		 * loadImage("background_01p"); loadImage("background_01l");
		 * 
		 * loadImage("statusbar_background_p");
		 * loadImage("statusbar_background_l");
		 * 
		 * loadImage("rate_star_big_on"); loadImage("rate_star_med_on");
		 * loadImage("rate_star_small_on");
		 * 
		 * loadImage("title_bar.9"); loadImage("stat_sys_data_connected");
		 * loadImage("stat_sys_battery_charge_100");
		 * loadImage("stat_sys_signal_3");
		 * 
		 * loadImage("mapview");
		 * 
		 * loadImage("scrollbar.9"); loadImage("scrollfield.9");
		 * 
		 * loadImage("light/checkbox_off_background");
		 * loadImage("light/checkbox_on_background");
		 * loadImage("light/clock_dial"); loadImage("light/clock_hand_hour");
		 * loadImage("light/clock_hand_minute");
		 * loadImage("light/radiobutton_off_background");
		 * loadImage("light/radiobutton_on_background");
		 * loadImage("light/button_background_normal.9");
		 * loadImage("light/editbox_background_normal.9");
		 * loadImage("light/progress_circular_background");
		 * loadImage("light/progress_particle");
		 * loadImage("light/progress_circular_indeterminate");
		 * loadImage("light/arrow_up_float");
		 * loadImage("light/arrow_down_float");
		 * loadImage("light/spinnerbox_background_focus_yellow.9");
		 * loadImage("light/spinnerbox_arrow_middle.9");
		 * 
		 * loadImage("def/btn_check_off"); loadImage("def/btn_check_on");
		 * 
		 * loadImage("def/btn_radio_off"); loadImage("def/btn_radio_on");
		 * 
		 * loadImage("def/textfield.9"); loadImage("def/btn_default_normal.9");
		 * loadImage("def/progress_wheel_medium");
		 * 
		 * loadImage("def/spinner_normal.9"); loadImage("def/tab_selected.9");
		 * loadImage("def/btn_dropdown_neither.9");
		 * 
		 * loadImage( "mdpi/textfield_default.9");
		 * 
		 * loadImage("def/btn_toggle_off.9"); loadImage("def/btn_toggle_on.9");
		 **/
	}
}
