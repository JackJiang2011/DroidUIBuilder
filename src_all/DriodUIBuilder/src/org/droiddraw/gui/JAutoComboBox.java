/*
 * Copyright (C) 2015 Jack Jiang(cngeeker.com) The DroidUIBuilder Project. 
 * All rights reserved.
 * Project URL:https://github.com/JackJiang2011/DroidUIBuilder
 * Version 1.0
 * 
 * Jack Jiang PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * JAutoComboBox.java at 2015-2-6 16:11:59, original version by Jack Jiang.
 * You can contact author with jb2011@163.com.
 */
package org.droiddraw.gui;

/* From http://java.sun.com/docs/books/tutorial/index.html */

/*
 * Copyright (c) 2006 Sun Microsystems, Inc. All Rights Reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * -Redistribution of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 * 
 * -Redistribution in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * 
 * Neither the name of Sun Microsystems, Inc. or the names of contributors may
 * be used to endorse or promote products derived from this software without
 * specific prior written permission.
 * 
 * This software is provided "AS IS," without a warranty of any kind. ALL
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING ANY
 * IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR
 * NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN MIDROSYSTEMS, INC. ("SUN") AND ITS
 * LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A
 * RESULT OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 * IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT
 * OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR
 * PUNITIVE DAMAGES, HOWEVER CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY,
 * ARISING OUT OF THE USE OF OR INABILITY TO USE THIS SOFTWARE, EVEN IF SUN HAS
 * BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 * 
 * You acknowledge that this software is not designed, licensed or intended for
 * use in the design, construction, operation or maintenance of any nuclear
 * facility.
 */

import java.awt.event.ItemEvent;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.plaf.basic.BasicComboBoxEditor;

public class JAutoComboBox extends JComboBox
{
	private AutoTextFieldEditor autoTextFieldEditor;
	private boolean isFired;
	
	private class AutoTextFieldEditor extends BasicComboBoxEditor
	{
		private JAutoTextField getAutoTextFieldEditor()
		{
			return (JAutoTextField) editor;
		}

		AutoTextFieldEditor(java.util.List<String> list)
		{
			editor = new JAutoTextField(list, JAutoComboBox.this);
			editor.setOpaque(false);// add by Jack Jiang
			editor.setBorder(null);// add by Jack Jiang
		}
	}

	@SuppressWarnings("serial")
	public JAutoComboBox(java.util.List<String> list)
	{
		isFired = false;
		autoTextFieldEditor = new AutoTextFieldEditor(list);
		setEditable(true);
		setModel(new DefaultComboBoxModel(list.toArray())
		{

			@Override
			protected void fireContentsChanged(Object obj, int i, int j)
			{
				if (!isFired)
					super.fireContentsChanged(obj, i, j);
			}

		});
		setEditor(autoTextFieldEditor);
	}

	public boolean isCaseSensitive()
	{
		return autoTextFieldEditor.getAutoTextFieldEditor().isCaseSensitive();
	}

	public void setCaseSensitive(boolean flag)
	{
		autoTextFieldEditor.getAutoTextFieldEditor().setCaseSensitive(flag);
	}

	public boolean isStrict()
	{
		return autoTextFieldEditor.getAutoTextFieldEditor().isStrict();
	}

	public void setStrict(boolean flag)
	{
		autoTextFieldEditor.getAutoTextFieldEditor().setStrict(flag);
	}

	public java.util.List<String> getDataList()
	{
		return autoTextFieldEditor.getAutoTextFieldEditor().getDataList();
	}

	public void setDataList(java.util.List<String> list)
	{
		autoTextFieldEditor.getAutoTextFieldEditor().setDataList(list);
		setModel(new DefaultComboBoxModel(list.toArray()));
	}

	void setSelectedValue(Object obj)
	{
		if (isFired)
		{
			return;
		}
		else
		{
			isFired = true;
			setSelectedItem(obj);
			fireItemStateChanged(new ItemEvent(this, 701, selectedItemReminder,1));
			isFired = false;
			return;
		}
	}

	@Override
	protected void fireActionEvent()
	{
		if (!isFired)
			super.fireActionEvent();
	}
}
