package org.droiddraw;

import java.awt.BorderLayout;
import java.awt.FileDialog;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileFilter;
import javax.xml.parsers.ParserConfigurationException;

import org.droiddraw.gui.DonatePanel;
import org.droiddraw.gui.ImageResources;
import org.droiddraw.gui.Preferences;
import org.droiddraw.gui.PreferencesPanel;
import org.droiddraw.gui.WidgetDeleteRecord;
import org.droiddraw.util.DroidDrawHandler;
import org.droiddraw.util.FileFilterExtension;
import org.droiddraw.util.LayoutUploader;
import org.droiddraw.widget.Layout;
import org.droiddraw.widget.Widget;
import org.droiddraw.widget.WidgetTransferable;
import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;
import org.simplericity.macify.eawt.Application;
import org.simplericity.macify.eawt.ApplicationEvent;
import org.simplericity.macify.eawt.ApplicationListener;
import org.simplericity.macify.eawt.DefaultApplication;
import org.xml.sax.SAXException;

import com.jb2011.drioduibuilder.util.FileHelper;
import com.jb2011.drioduibuilder.util.Platform;
import com.jb2011.drioduibuilder.util.Utils;

public class Launch implements ApplicationListener//, URLOpener
{
	private final static String ANT_EXEC;

	private static File saveFile = null;
	private static JFrame frame;
	private static MainPane droidDrawPanel;
	private static JFileChooser fileChooser = null;
	private static FileDialog fileDialog = null;
//	private static public boolean osx;
	private static FileFilterExtension xmlFilter = null;
	private static FileFilter dirFilter = null;
	private static FileFilter imgFilter = null;

	static
	{
//		if (System.getProperty("os.name").toLowerCase().indexOf("windows") != -1)
		if(Platform.isWindows())
			ANT_EXEC = "ant.bat";
		else
			ANT_EXEC = "ant";
	}

	protected static void doMacOSXIntegration()
	{
		Application a = new DefaultApplication();
		a.addApplicationListener(new Launch());
		a.addPreferencesMenuItem();
		a.addAboutMenuItem();
	}

	protected static void open(String file)
	{
		open(new File(file));
	}

	protected static void open(File f)
	{
		droidDrawPanel.open(f);
		saveFile = f;
	}

	protected static boolean quit()
	{
		if (AndroidEditor.instance().isChanged())
		{
			int opt = JOptionPane.showConfirmDialog(droidDrawPanel,
					"Do you wish to save changes to your layout?",
					"Unsaved Changes", JOptionPane.YES_NO_CANCEL_OPTION);
			switch (opt)
			{
				case JOptionPane.CANCEL_OPTION:
					return false;
				case JOptionPane.YES_OPTION:
					if (doSave())
						break;
					else
						return false;
				case JOptionPane.NO_OPTION:
					break;
				default:
					// I guess this happens if you close the dialog?
					return false;
			}
		}
		System.exit(0);
		return true;
	}

	protected static void showPreferencesFrame()
	{
		JFrame jf = new JFrame();
		jf.getContentPane().add(
				new PreferencesPanel(AndroidEditor.instance().getPreferences(),jf));
		jf.pack();
		jf.setResizable(false);
		jf.setVisible(true);
	}

	protected static void showAboutFrame()
	{
		final JDialog jd = new JDialog(JOptionPane.getRootFrame(), "About DroidUIBuilder");
		jd.getContentPane().setLayout(new BorderLayout());
		jd.getContentPane().add(new JLabel(new ImageIcon(ImageResources.instance().getImage(
						"droiddraw_small"))), BorderLayout.CENTER);
		jd.pack();
		jd.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		jd.setResizable(false);
		jd.setLocationRelativeTo(null);
		jd.setVisible(true);
	}
	
	protected static void showSurportFrame()
	{
		DonatePanel dp = new DonatePanel();
		dp.setBorder(BorderFactory.createEmptyBorder(100,100,100,300));
		final JDialog jd = new JDialog(JOptionPane.getRootFrame(), "Support us");
		jd.getContentPane().setLayout(new BorderLayout());
		jd.getContentPane().add(dp, BorderLayout.CENTER);
		jd.pack();
		jd.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		jd.setResizable(false);
		jd.setLocationRelativeTo(null);
		jd.setVisible(true);
	}

	public static File doOpen()
	{
		return doOpen(null);
	}

	public static File doOpen(File file)
	{
		return doOpen(file, xmlFilter);
	}

	public static File doOpenImage(File file)
	{
		return doOpen(file, imgFilter);
	}

	private static File doOpen(File file, FileFilter filter)
	{
		if (!Platform.isMac())
		{
			fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			fileChooser.setFileFilter(filter);
			if (file != null)
			{
				if (file.isDirectory())
					fileChooser.setCurrentDirectory(file);
				else
					fileChooser.setCurrentDirectory(file.getParentFile());
			}
			else
				maybeSetCurrentFileDir();
			int res = fileChooser.showOpenDialog(droidDrawPanel);
			if (res == JFileChooser.APPROVE_OPTION)
				return fileChooser.getSelectedFile();
		}
		else
		{
			fileDialog.setMode(FileDialog.LOAD);
			if (file != null)
			{
				try
				{
					if (file.isDirectory())
						fileDialog.setDirectory(file.getCanonicalPath());
					else
						fileDialog.setDirectory(file.getParentFile()
								.getCanonicalPath());
				}
				catch (IOException ex)
				{
					MainPane.getTipCom().error(ex);
				}
			}
			else
				maybeSetCurrentFileDir();

			fileDialog.setVisible(true);
			if (fileDialog.getDirectory() != null
					&& fileDialog.getFile() != null)
				return new File(fileDialog.getDirectory() + "/" + fileDialog.getFile());
		}
		return null;
	}

	public static File doOpenDir()
	{
		// if (!osx) {
		fileChooser.setFileFilter(dirFilter);
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
//		int res = fileChooser.showOpenDialog(droidDrawPanel);
		if (fileChooser.showOpenDialog(droidDrawPanel) == JFileChooser.APPROVE_OPTION)
			return fileChooser.getSelectedFile();
		// }
		// else {
		// fd.setMode(FileDialog.LOAD);
		// fd.setFilenameFilter(new FilenameFilter() {
		// public boolean accept(File arg0, String arg1) {
		// return arg0.isDirectory();
		// }
		// });
		// fd.setVisible(true);
		// return new File(fd.getDirectory()+"/"+fd.getFile());
		// }
		return null;
	}

	public static void maybeSetCurrentFileDir()
	{
		String dir = AndroidEditor.instance().getPreferences()
				.getDefaultDirectory();
		if (dir.length() > 0)
		{
			File dirFile = new File(dir);
			if (dirFile.exists())
			{
				if (!Platform.isMac())
					fileChooser.setCurrentDirectory(dirFile);
				else
					fileDialog.setDirectory(dir);
			}
			else
				System.err.println(dir + " doesn't exist!");
		}
	}

	public static File doSaveBasic()
	{
		File f = null;
		maybeSetCurrentFileDir();

		if (!Platform.isMac())
		{
			int res = fileChooser.showSaveDialog(droidDrawPanel);
			if (res == JFileChooser.APPROVE_OPTION)
			{
				f = fileChooser.getSelectedFile();
				FileFilter ff = fileChooser.getFileFilter();
				if (FileFilterExtension.class.isInstance(ff))
				{
					String extension = ((FileFilterExtension) ff).getExtension();
					if (extension.length() > 0
							&& !f.getName().endsWith(extension))
					{
						f = new File(f.getAbsolutePath() + "." + extension);
					}
				}
			}
		}
		else
		{
			fileDialog.setMode(FileDialog.SAVE);
			fileDialog.setVisible(true);
			if (fileDialog.getFile() != null)
				f = new File(fileDialog.getDirectory() + "/" + fileDialog.getFile());
		}
		if (f != null && f.exists())
		{
			int res = JOptionPane.showConfirmDialog(droidDrawPanel, f.getName()
					+ " exists. Overwrite?", "Overwrite",
					JOptionPane.OK_CANCEL_OPTION);
			if (res == JOptionPane.CANCEL_OPTION)
				return null;
		}
		return f;
	}

	protected static boolean doSave()
	{
		File f = doSaveBasic();
		if (f != null)
		{
			frame.setTitle("DroidDraw: " + f.getName());
			droidDrawPanel.save(f);
			saveFile = f;
			return true; /*
						 * 2010/10/16: Added this line of code as a result of
						 * commenting out the code block at line 263.
						 */

			/*
			 * 2010/10/16: Disabled this code block, as per email conversation
			 * with Brendan Burns
			 */
			// File src = new File( f.getParentFile(), "Foo.java" );
			// try {
			// FileWriter fw = new FileWriter( src );
			// PrintWriter pw = new PrintWriter( fw );
			// AndroidEditor.instance().generateSource( pw, "foo.bar" );
			// pw.flush();
			// fw.flush();
			// pw.close();
			// fw.close();
			// return true;
			// }
			// catch ( IOException ex ) {
			// ex.printStackTrace();
			// return false;
			// }
		}
		else
			return false;
	}

	public static class MainImageLoader implements
			ImageLoader.ImageLoaderInterface
	{
		public void loadImage(String name)
		{
			URL u = this.getClass().getClassLoader().getResource("ui/" + name + ".png");
			if (u == null)
			{
				MainPane.getTipCom().error("Couldn't open image : " + name);
				return;
			}
			try
			{
				InputStream is = u.openStream();
				BufferedImage img = ImageIO.read(is);
				ImageResources.instance().addImage(img, name);
			}
			catch (IOException ex)
			{
				ex.printStackTrace();
			}
		}
	}

	public static final int BUFFER = 4096;

	protected static void makeAPK(File dir, boolean install)
		throws IOException
	{
		URL u = ClassLoader.getSystemClassLoader().getResource(
				"data/activity.zip");
		if (u == null)
		{
			MainPane.getTipCom().error("Couldn't open activity.zip");
			return;
		}
		InputStream is = u.openStream();

		ZipInputStream zis = new ZipInputStream(new BufferedInputStream(is));
		ZipEntry entry;
		while ((entry = zis.getNextEntry()) != null)
		{
			int count;
			byte data[] = new byte[BUFFER];
			// write the files to the disk
			if (entry.isDirectory())
			{
				File f = new File(dir, entry.getName());
				f.mkdir();
			}
			else
			{
				FileOutputStream fos = new FileOutputStream(new File(dir, entry.getName()));
				BufferedOutputStream dest = new BufferedOutputStream(fos, BUFFER);
				while ((count = zis.read(data, 0, BUFFER)) != -1)
				{
					dest.write(data, 0, count);
				}
				dest.flush();
				dest.close();
			}
		}
		zis.close();

		String[] cmd = install ? new String[] { ANT_EXEC, "install" } : new String[] { ANT_EXEC };
		File wd = new File(dir, "activity");
		Utils.run(cmd, wd);

		File res = new File(wd, "res");
		res = new File(res, "layout");
		res = new File(res, "main.xml");
		PrintWriter pw = new PrintWriter(new FileWriter(res));
		AndroidEditor.instance().generate(pw);
		pw.flush();
		pw.close();
	}
	
	private static void initUserInterface() {
		System.setProperty("apple.laf.useScreenMenuBar", "true");
		System.setProperty(
				"com.apple.mrj.application.apple.menu.about.name",
		"DriodUIBuilder");

		try {
			if(Platform.isWindows())
			{
				BeautyEyeLNFHelper.frameBorderStyle = 
					BeautyEyeLNFHelper.FrameBorderStyle.translucencySmallShadow;
//					BeautyEyeLNFHelper.FrameBorderStyle.osLookAndFeelDecorated;
				BeautyEyeLNFHelper.translucencyAtFrameInactive = false;
				BeautyEyeLNFHelper.launchBeautyEyeLNF();
				
				//此属性决定了JTabbedPane的tab标签的内衬
				UIManager.put("TabbedPane.tabInsets"
						, new javax.swing.plaf.InsetsUIResource(7,10,9,10));//BELNF default 7,15,9,15
				//改变InsetsUIResource参数的值即可实现
				UIManager.put("TabbedPane.tabAreaInsets"
				        , new javax.swing.plaf.InsetsUIResource(3,10,2,10));//BELNF default 3,10,2,10
				//此属性决定了tab与内容面板间的空白
				UIManager.put("TabbedPane.contentBorderInsets"
						,new javax.swing.plaf.InsetsUIResource(2,0,0,0));//BELNF default 2,0,3,0
				UIManager.put("RootPane.setupButtonVisible", false);
				
//				JFrame.setDefaultLookAndFeelDecorated(true);
//				JDialog.setDefaultLookAndFeelDecorated(true);
//				UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
			}
			else
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws IOException
	{
		// init LNF first
		initUserInterface();
		
		String file = null;
		if (args.length > 0)
		{
			if (args[0].endsWith("xml"))
				file = args[0];
		}
		// This is so that I can test out the Google examples...
		// START
		/*
		 * if (args.length > 0) { try {
		 * AndroidEditor.instance().setStrings(StringHandler.load(new
		 * FileInputStream("src/strings.xml"))); } catch (Exception ex) {
		 * AndroidEditor.instance().error(ex); } }
		 */
		// END

//		osx = (System.getProperty("os.name").toLowerCase().contains("mac os x"));
		if (Platform.isMac())// && !isJNLP)
		{
			doMacOSXIntegration();
		}

		final Preferences prefs = AndroidEditor.instance().getPreferences();
		ImageLoader.loadImages(new MainImageLoader());

		frame = new JFrame("DriodUIBuilder - 欢迎加入Swing交流群：259448663");
		JOptionPane.setRootFrame(frame);// save as rootPane, BY Jack Jiang
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				quit();
			}
		});
		
		//** @@deprecated
		//** 2012-11-20：以下4行代码由Jack Jiang标记为过时，详见
		//** 类：org.droiddraw.gui.painter.WidgetPaintersRegistry的说明
//		// 设置指定布局及其子类的绘制实现类，绘制实现类里实现了该布局
//		// 或其子布局（不一定是直接子哦）的背景等的绘制实现
//		WidgetPaintersRegistry.registerPainter(ScrollView.class, new ScrollViewPainter());
//		WidgetPaintersRegistry.registerPainter(AbstractLayout.class, new LayoutPainter());

		String screen = "hvgap";
		AndroidEditor.ScreenMode scr = prefs.getScreenMode();
		if (scr.equals(AndroidEditor.ScreenMode.HVGA_LANDSCAPE))
			screen = "hvgal";
		else if (scr.equals(AndroidEditor.ScreenMode.QVGA_LANDSCAPE))
			screen = "qvgal";
		else if (scr.equals(AndroidEditor.ScreenMode.QVGA_PORTRAIT))
			screen = "qvgap";
		else if (scr.equals(AndroidEditor.ScreenMode.WVGA_LANDSCAPE))
			screen = "wvgal";
		else if (scr.equals(AndroidEditor.ScreenMode.WVGA_PORTRAIT))
			screen = "wvgap";

		droidDrawPanel = new MainPane(screen);
		AndroidEditor.instance().setScreenMode(prefs.getScreenMode());
		fileDialog = new FileDialog(frame);
//		if (!isJNLP)
		{
			fileChooser = new JFileChooser();
			xmlFilter = new FileFilterExtension("xml", "Android Layout file (.xml)");

			dirFilter = new FileFilter()
			{
				@Override
				public boolean accept(File arg)
				{
					return arg.isDirectory();
				}

				@Override
				public String getDescription()
				{
					return "Directory";
				}
			};

			imgFilter = new FileFilter()
			{
				@Override
				public boolean accept(File f)
				{
					if (f.isDirectory())
						return true;
					return f.getName().endsWith(".png")
							|| f.getName().endsWith(".jpg")
							|| f.getName().endsWith(".jpeg")
							|| f.getName().endsWith(".gif");
				}

				@Override
				public String getDescription()
				{
					return "Image file (*.png, *.jpg, *.jpeg, *.gif)";
				}
			};

			fileChooser.setFileFilter(xmlFilter);
		}
		int ctl_key = InputEvent.CTRL_MASK;
		if (Platform.isMac())
			ctl_key = InputEvent.META_MASK;

//		if (!isJNLP)
		{
			JMenuBar mb = new JMenuBar();
			createMenus(ctl_key, mb);
			frame.setJMenuBar(mb);
		}

		frame.getContentPane().add(droidDrawPanel);
//		frame.setSize(Utils.getSizeWithScreen(1.0, 0.95));
//		Dimension dd = Utils.getSizeWithScreen(1.0, 0.90);
		frame.setSize(1000, 715);//(int)dd.getHeight());
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		if (file != null)
		{
			if (file.endsWith(".xml"))
				open(new File(file));
		}
	}

	private static void createMenus(int ctl_key, JMenuBar mb)
	{
		JMenu menu = new JMenu("File");
		JMenuItem it;
		it = new JMenuItem("New");
		it.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				droidDrawPanel.fireClearScreen(false);
			}
		});
		it.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ctl_key));
		menu.add(it);
		it = new JMenuItem("Open");
		it.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ev)
			{
				open(doOpen());
			}
		});
		it.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ctl_key));
		menu.add(it);
		menu.addSeparator();
		it = new JMenuItem("Save");
		it.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ev)
			{
				if (saveFile == null)
				{
					doSave();
				}
				else
				{
					droidDrawPanel.save(saveFile);
				}
			}
		});
		it.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ctl_key));
		menu.add(it);

		it = new JMenuItem("Save As...");
		it.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				doSave();
			}
		});
		menu.add(it);

		it = new JMenuItem("Export as .apk");
		it.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				String tmpFile = System.getProperty("java.io.tmpdir");
				File f = new File(tmpFile);

				if (f != null)
				{
					try
					{
						makeAPK(f, false);
						File save = doOpenDir();

						File apk = new File(f, "activity");
						apk = new File(apk, "bin");
						apk = new File(apk, "DroidDrawActivity.apk");

						save = new File(save, "DroidDrawActivity.apk");
						FileHelper.copy(apk, save);
						MainPane.getTipCom().info("Saved - "+
								"Layout saved as " + save.getCanonicalPath());
					}
					catch (IOException ex)
					{
						MainPane.getTipCom().error(ex);
					}
				}
			}
		});
		menu.add(it);

		it = new JMenuItem("Export to device");
		it.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				String tmpFile = System.getProperty("java.io.tmpdir");
				File f = new File(tmpFile);
				if (f.exists())
				{
					try
					{
						makeAPK(f, true);
						MainPane.getTipCom().info("Installed - Layout successfully installed.");
					}
					catch (IOException ex)
					{
						MainPane.getTipCom().error(ex);
					}
				}
				else
					MainPane.getTipCom().error("Error generating .apk");
			}
		});
		menu.add(it);

		if (!Platform.isMac())
		{
			it = new JMenuItem("Preferences");
			it.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					showPreferencesFrame();
				}
			});
			menu.add(it);

			menu.addSeparator();

			it = new JMenuItem("Quit");
			it.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent arg0)
				{
					quit();
				}
			});
			it.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ctl_key));
			menu.add(it);
		}
		mb.add(menu);

		menu = new JMenu("Edit");
		it = new JMenuItem("Undo");
		it.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				AndroidEditor.instance().undo();
			}
		});
		it.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, ctl_key));
		menu.add(it);

		it = new JMenuItem("Redo");
		it.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				AndroidEditor.instance().redo();
			}
		});
		it.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, ctl_key));
		menu.add(it);

		it = new JMenuItem("Cut");
		addCutAction(it);
		it.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ctl_key));
		menu.add(it);
		it = new JMenuItem("Copy");
		addCopyAction(it);
		it.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ctl_key));
		menu.add(it);
		it = new JMenuItem("Paste");
		addPasteAction(it);
		it.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ctl_key));
		menu.add(it);

		menu.addSeparator();
		it = new JMenuItem("Select All");
		it.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				droidDrawPanel.getPaneMakeLayout().selectAll();
			}
		});
		it.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ctl_key));
		// it.setShortcut(new MenuShortcut(KeyEvent.VK_A, false));
		menu.add(it);

		menu.addSeparator();

		it = new JMenuItem("Fullscreen");
		it.addActionListener(new ActionListener()
		{
			boolean isFullscreen = false;

			public void actionPerformed(ActionEvent arg0)
			{
				if (isFullscreen)
				{
					Utils.fullScreen(null);
					isFullscreen = false;
				}
				else
				{
					Utils.fullScreen(frame);
					isFullscreen = true;
				}
			}
		});
		it.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, ctl_key));
		menu.add(it);
		menu.addSeparator();

		it = new JMenuItem("Clear Screen");
		it.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
//				doClear(true);
				droidDrawPanel.fireClearScreen(true);
			}
		});
		menu.add(it);

		it = new JMenuItem("Set Ids from Labels");
		it.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				AndroidEditor.instance().setIdsFromLabels();
			}
		});
		menu.add(it);
		mb.add(menu);

		menu = new JMenu("Project");
		it = new JMenuItem("Load string resources");
		it.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				File f = doOpen();
				if (f != null)
				{
					AndroidEditor.instance().setStrings(f);
				}
			}
		});
		menu.add(it);

		it = new JMenuItem("Load color resources");
		it.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				File f = doOpen();
				if (f != null)
				{
					AndroidEditor.instance().setColors(f);
				}
			}
		});
		menu.add(it);

		it = new JMenuItem("Load array resources");
		it.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				File f = doOpen();
				if (f != null)
				{
					AndroidEditor.instance().setArrays(f);
				}
			}
		});
		menu.add(it);

		it = new JMenuItem("Set drawables directory");
		it.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				File f = doOpenDir();
				if (f != null)
				{
					AndroidEditor.instance().setDrawableDirectory(f);
				}
			}
		});
		menu.add(it);

		menu.addSeparator();

		it = new JMenuItem("Load resource directory");
		it.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				File f = doOpenDir();
				if (f != null)
				{
					File drawable = new File(f, "drawable");
					if (drawable.exists() && drawable.isDirectory())
					{
						AndroidEditor.instance().setDrawableDirectory(f);
					}

					f = new File(f, "values");
					if (f.exists() && f.isDirectory())
					{
						File strings = new File(f, "strings.xml");
						if (strings.exists())
						{
							AndroidEditor.instance().setStrings(strings);
						}
						File colors = new File(f, "colors.xml");
						if (colors.exists())
						{
							AndroidEditor.instance().setColors(colors);
						}
						File arrays = new File(f, "arrays.xml");
						if (arrays.exists())
						{
							AndroidEditor.instance().setArrays(arrays);
						}
					}
				}
			}
		});
		menu.add(it);
		menu.addSeparator();

		it = new JMenuItem("Send GUI to device");
		it.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				try
				{
					ByteArrayOutputStream ba = new ByteArrayOutputStream();
					PrintWriter pw = new PrintWriter(ba);
					AndroidEditor.instance().generate(pw);
					pw.flush();
					ba.flush();
					if (LayoutUploader.upload("127.0.0.1", 6100,
							new ByteArrayInputStream(ba.toByteArray())))
						JOptionPane.showMessageDialog(frame, "Upload succeeded");
					else
						JOptionPane.showMessageDialog(frame, "Upload failed.  Is AnDroidDraw running?");
				}
				catch (IOException ex)
				{
					MainPane.getTipCom().warn(ex);
				}
			}
		});
		it.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, ctl_key));
		menu.add(it);

		mb.add(menu);

		menu = new JMenu("Help");
		it = new JMenuItem("Tutorial");
		it.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, ctl_key));
		it.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				Utils.browseURL("http://www.droiddraw.org/tutorial.html");
			}
		});
		menu.add(it);

		it = new JMenuItem("Support");
		it.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				showSurportFrame();
			}
		});
		menu.add(it);
		
		menu.addSeparator();
//		if (!Platform.isMac())
		{
			it = new JMenuItem("About");
			it.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent arg0)
				{
					showAboutFrame();
				}
			});
			menu.add(it);
		}
		

		mb.add(menu);
	}

	public static void addPasteAction(JMenuItem it)
	{
		it.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
				if (droidDrawPanel != null && droidDrawPanel.hasFocus())
				{
					try
					{
						String txt = (String) c
								.getData(DataFlavor.stringFlavor);
						if (txt != null)
						{
							droidDrawPanel.getPaneMakeLayout().insertText(txt);
						}
					}
					catch (UnsupportedFlavorException ex)
					{
					}
					catch (IOException ex)
					{
					}
				}
				else
				{
					DataFlavor flavor = new DataFlavor(Widget.class, "DroidDraw Widget");
					if (c.isDataFlavorAvailable(flavor))
					{
						boolean ok = false;
						try
						{
							String s = (String) c.getData(flavor);
							Widget w = DroidDrawHandler.parseFromString(s);
							String id = w.getId() + "_copy";
							w.setPropertyByAttName("android:id", id);
							if (w != null)
							{
								AndroidEditor.instance().addWidget(w, 50, 50);
							}
							ok = true;
						}
						catch (IOException ex)
						{
							ex.printStackTrace();
						}
						catch (UnsupportedFlavorException ex)
						{
							ex.printStackTrace();
						}
						catch (SAXException ex)
						{
							ex.printStackTrace();
						}
						catch (ParserConfigurationException ex)
						{
							ex.printStackTrace();
						}
						if (!ok)
						{
							MainPane.getTipCom().warn("Paste failed.");
						}
					}
				}
			}
		});
	}

	public static void addCopyAction(JMenuItem it)
	{
		it.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				if (droidDrawPanel.hasFocus())
				{
					if (droidDrawPanel.getPaneMakeLayout().getSelectedText() != null
							&& droidDrawPanel.getPaneMakeLayout().getSelectedText().length() != 0)
					{
						Toolkit.getDefaultToolkit().getSystemClipboard()
								.setContents(new StringSelection(droidDrawPanel.getPaneMakeLayout().getSelectedText()), null);
					}
				}
				else
				{
					Widget w = AndroidEditor.instance().getSelected();
					if (w != null)
					{
						Toolkit.getDefaultToolkit().getSystemClipboard()
								.setContents(new WidgetTransferable(w), null);
					}
				}
			}
		});
	}

	public static void addCutAction(JMenuItem it)
	{
		it.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				if (droidDrawPanel.hasFocus())
				{
					String txt = droidDrawPanel.getPaneMakeLayout().getSelectedText();
					droidDrawPanel.getPaneMakeLayout().deleteSelectedText();
					Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
					c.setContents(new StringSelection(txt), null);
				}
				else
				{
					Widget w = AndroidEditor.instance().getSelected();
					if (w != null)
					{
						Toolkit.getDefaultToolkit().getSystemClipboard()
								.setContents(new WidgetTransferable(w), null);
						AndroidEditor.instance().removeWidget(w);
						Layout l = w.getParent();
						AndroidEditor.instance().queueUndoRecord(new WidgetDeleteRecord(l, w));
						AndroidEditor.instance().viewer.repaint();
					}
				}
			}
		});
	}

	public void handleAbout(ApplicationEvent ev)
	{
		showAboutFrame();
		ev.setHandled(true);
	}

	public void handleOpenApplication(ApplicationEvent arg0)
	{
	}

	public void handleOpenFile(ApplicationEvent ev)
	{
		String f = ev.getFilename();
		if (f.endsWith(".xml"))
		{
			open(ev.getFilename());
			ev.setHandled(true);
		}
	}

	public void handlePreferences(ApplicationEvent arg0)
	{
		showPreferencesFrame();
	}

	public void handlePrintFile(ApplicationEvent arg0)
	{
	}

	public void handleQuit(ApplicationEvent arg0)
	{
		quit();
	}

	public void handleReopenApplication(ApplicationEvent arg0)
	{
	}

//	public void openURL(String url)
//	{
//		Utils.browseURL(url);
//	}
}
