package org.droiddraw.widget;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.droiddraw.AndroidEditor;

public class WidgetTransferable implements Transferable
{
	private Widget w;

	public WidgetTransferable(Widget w)
	{
		this.w = w;
	}

	public Object getTransferData(DataFlavor flavor)
		throws UnsupportedFlavorException, IOException
	{
		if (flavor.equals(DataFlavor.stringFlavor)
				|| flavor.getRepresentationClass().equals(Widget.class))
		{
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			AndroidEditor.instance().generateWidget(w, pw);
			pw.flush();
			return sw.toString();
		}
		else
			throw new UnsupportedFlavorException(flavor);
	}

	public DataFlavor[] getTransferDataFlavors()
	{
		DataFlavor[] result = new DataFlavor[] { DataFlavor.stringFlavor,
				new DataFlavor(Widget.class, "DroidDraw Widget") };
		return result;
	}

	public boolean isDataFlavorSupported(DataFlavor flavor)
	{
		return flavor.equals(DataFlavor.stringFlavor)
				|| flavor.getRepresentationClass().equals(Widget.class);
	}

}
