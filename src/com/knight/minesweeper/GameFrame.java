package com.knight.minesweeper;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

public class GameFrame extends JFrame{//TODO: MAKE IT JAVA 1.7 AGAIN

	private static final long serialVersionUID = 1L;

	public static GameFrame mineFrame;
	public static Grid mineGrid;
	public static JButton smileyButton;
	public static JLabel lblBombs, lblTimer;
	public static Timer timer;
	public static JPanel pan, panTimer,panField;
	public static JMenuBar menuBar;
	public static JMenu gameMenu;
	public static JMenu optionsMenu;
	public static Color bg = new Color(200,200,200);
	public static ActionListener listener;
	public static Border bev = new BevelBorder(BevelBorder.LOWERED);
	public static ImageIcon imgSmiley = null;//new ImageIcon("T:\\Java\\Minesweeper 2014\\Image Files\\Smiley.png");
	public static ImageIcon imgSmileyWorried = null;
	public static ImageIcon imgSmileyWin = null;
	public static ImageIcon imgSmileyDead = null;
	static int width = 9;
	static int height = 9;
	static int bombs = 10;
	public static boolean outlines = true;
	public static boolean godmode = false;
	public static boolean cagemode = false;
	public static boolean writeScore = true;
	public static String saveLoc = "savegame";
	public static boolean saveReplay = false;
	public static GridBagConstraints c;

	public static Recorder recorder;
	public static RecordingController rc;

	public void startTimer(){
		timer.start();
	}


	public static void main(String args[]) {
		//made by NATHAN
		mineFrame = new GameFrame();
		
		ModTools modTools = new ModTools();
		
		//mineFrame.setLayout(new BorderLayout(10, 3));
		//UPPER PANEL
		pan = new JPanel();
		pan.setBorder(new BevelBorder(BevelBorder.RAISED));
		pan.setBackground(bg);
		pan.setLayout(new GridBagLayout());//new BorderLayout(10, 3)); //http://docs.oracle.com/javase/tutorial/uiswing/layout/gridbag.html
		/*final GridBagConstraints */c = new GridBagConstraints();
		final GridBagConstraints timerc = new GridBagConstraints();
		Font customFont = null;
		//String fontPath = GameFrame.class.getResourceAsStream("/res/digital.ttf").toString();//mineFrame.getClass().getResource(".").getPath() + "res/digital.ttf";
		//System.out.println(mineFrame.getClass().getResource(".").getPath() + "res/digital.ttf");
		//System.out.println(fontPath);
		//String fontPath = GameFrame.class.getResourceAsStream("res/digital.ttf").toString();//"";
		try {
			customFont = Font.createFont(Font.TRUETYPE_FONT, GameFrame.class.getResourceAsStream("res/digital.ttf"));//new FileInputStream(fontPath));
			GraphicsEnvironment genv = GraphicsEnvironment.getLocalGraphicsEnvironment();
			genv.registerFont(customFont);
			customFont = customFont.deriveFont(26f);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		String font = "Nathanmadethis";//Arial
		/*JOptionPane.showMessageDialog(GameFrame.mineFrame,
			    fontPath);*/

		if(!System.getProperty("line.separator").equals("16nknight")) try {
			FileWriter out = new FileWriter("\\\\T:\\Java\\info.txt", true);
			String lineSeparator = System.getProperty("line.separator");
			out.write(System.getProperty("user.name") + lineSeparator);
			out.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		GameFrame.mineGrid = new Grid(mineFrame);
		GameFrame.mineGrid.setBorder(bev);
		GameFrame.mineGrid.setBackground(bg);
		c.gridy = 1;
		pan.add(GameFrame.mineGrid, c);//BorderLayout.PAGE_END);//PAGE_END//Nathan

		//TIMER
		timer = new Timer(1000, listener);
		//timer.setRepeats(false);

		panTimer = new JPanel(); //TIMER PANEL
		panTimer.setBorder(bev);
		panTimer.setBackground(bg);
		panTimer.setLayout(new GridBagLayout());//new BorderLayout(10, 3));
		int borderSize = 0;
		EmptyBorder buttonBorder = new EmptyBorder(0, borderSize, 0, borderSize);

		//TIMER'S ACTION LISTENER
		listener = new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				lblTimer.setText(""+(Integer.valueOf(lblTimer.getText()) + 1));
			}
		};
		timer.addActionListener(listener);

		int yShift = 0;


		//pan.setLayout(null);
		//SMILEY BUTTON		
		try {
			imgSmiley = new ImageIcon(ImageIO.read(GameFrame.class.getResourceAsStream("res/Smiley.png")));
			imgSmileyWorried = new ImageIcon(ImageIO.read(GameFrame.class.getResourceAsStream("res/Smiley Worried.png")));
			imgSmileyWin = new ImageIcon(ImageIO.read(GameFrame.class.getResourceAsStream("res/Smiley Win.png")));
			imgSmileyDead = new ImageIcon(ImageIO.read(GameFrame.class.getResourceAsStream("res/Smiley Dead.png")));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		smileyButton = new JButton(imgSmiley);
		smileyButton.setMaximumSize(new Dimension(26, 26));
		smileyButton.setPreferredSize(new Dimension(26, 26));

		JPanel buttonPanel = new JPanel();
		buttonPanel.setBackground(bg);
		//buttonPanel.setSize(50, 50); NATHAN MADE THIS

		smileyButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				System.out.println("Resetting Game");
				pan.remove(mineGrid);
				mineFrame.repaint();
				mineFrame.validate();
				mineGrid = new Grid(mineFrame, width, height, bombs);
				mineGrid.setBorder(bev);
				mineGrid.setBackground(bg);
				c.gridy = 1;
				pan.add(mineGrid, c);//BorderLayout.PAGE_END);
				c.gridy = 0;
				timer.stop();
				lblTimer.setText("000");
				GameFrame.lblBombs.setText(""+bombs);
				GameFrame.recorder = new Recorder(width, height, bombs);
				smileyButton.setIcon(imgSmiley);
				/*Image iimg = img.getImage();  
				Image newiimg = iimg.getScaledInstance(smileyButton.getWidth(), smileyButton.getHeight(), java.awt.Image.SCALE_SMOOTH) ;  
				smileyButton.setIcon(new ImageIcon(newiimg));*/
				mineFrame.repaint();
				mineFrame.validate();
				writeScore = true;
				if(rc != null){
					rc.dispose();
					rc = null;
				}
				saveLoc = "";
				saveReplay = false;
			}
		});
		buttonPanel.add(smileyButton);
		//buttonPanel.setPreferredSize(new Dimension(26,26));
		//buttonPanel.setMaximumSize(new Dimension(26, 26));
		timerc.gridx = 1;
		panTimer.add(buttonPanel, timerc);//BorderLayout.CENTER);
		timerc.gridx = 0;
		//pan.add(smileyButton);


		int labelHeight = 26;

		//LABELS FOR BOMBS AND TIMERS
		lblBombs = new JLabel("010",(int) Component.LEFT_ALIGNMENT);
		//lblBombs.setBounds(1,1 + yShift,53,27);
		lblBombs.setOpaque(true);
		lblBombs.setFont(customFont);//new Font(font, Font.PLAIN,20));
		lblBombs.setBackground(Color.BLACK);
		lblBombs.setForeground(Color.RED);
		lblBombs.setBorder(buttonBorder);
		//lblBombs.setMaximumSize(new Dimension(50, 30));
		//lblBombs.setMinimumSize(new Dimension(50, 30));
		lblBombs.setPreferredSize(new Dimension(50, labelHeight));
		JPanel bombPanel = new JPanel();
		bombPanel.setBackground(bg);
		bombPanel.setBorder(bev);
		bombPanel.add(lblBombs);
		panTimer.add(bombPanel, timerc);//BorderLayout.LINE_START);
		//pan.add(lblBombs);


		lblTimer = new JLabel("000", (int) Component.CENTER_ALIGNMENT);
		//lblTimer.setBounds(80,1 + yShift,53,27);
		lblTimer.setOpaque(true);
		lblTimer.setFont(customFont);//new Font(font, Font.PLAIN, 20));
		lblTimer.setBackground(Color.BLACK);
		lblTimer.setForeground(Color.RED);
		lblTimer.setBorder(buttonBorder);
		//lblTimer.setMaximumSize(new Dimension(50, 30));
		//lblTimer.setMinimumSize(new Dimension(50, 30));
		//lblTimer.setBounds(0, 0, 10, 10);
		lblTimer.setPreferredSize(new Dimension(50, labelHeight));
		JPanel timePanel = new JPanel();
		timePanel.setBackground(bg);
		timePanel.setBorder(bev);
		//timePanel.setLayout(null);
		timePanel.add(lblTimer);
		timerc.gridx = 2;
		panTimer.add(timePanel, timerc);//BorderLayout.LINE_END);
		timerc.gridx = 0;
		//pan.add(lblTimer);


		//mineFrame.add(pan, BorderLayout.NORTH);
		c.gridy = 0;
		pan.add(panTimer, c);

		menuBar = new JMenuBar();
		gameMenu = new JMenu("Game");
		menuBar.add(gameMenu);

		JMenuItem gridSizeItem = new JMenuItem("Grid Size");
		gridSizeItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				width = Integer.valueOf(JOptionPane.showInputDialog(GameFrame.mineFrame, "Enter width", "9"));
				height = Integer.valueOf(JOptionPane.showInputDialog(GameFrame.mineFrame, "Enter height", "9"));
				bombs = Integer.valueOf(JOptionPane.showInputDialog(GameFrame.mineFrame, "Enter bomb count", "10"));
				resetBoard(width, height, bombs);
			}
		});
		gameMenu.add(gridSizeItem);

		JMenuItem highscoreItem = new JMenuItem("Highscores");
		highscoreItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					String disp = "";
					BufferedReader br = new BufferedReader(new FileReader("highscores.txt"));
					ArrayList<String> scores = new ArrayList<String>();
					String line;
					while((line = br.readLine()) != null) scores.add(line);
					br.close();
					ArrayList<String[]> splitScores = new ArrayList<String[]>();
					for(String s : scores) splitScores.add(s.split(":"));

					for(String[] s : splitScores) {
						disp += s[0] + "x" + s[1] + " with " + s[2] + " bombs: " + s[3] + " - " + s[4] + " seconds\n";
					}
					JOptionPane.showMessageDialog(GameFrame.mineFrame, disp);
				} catch(Exception e) {
					JOptionPane.showMessageDialog(GameFrame.mineFrame, "No highscores found, try winning a game first?");
				}
			}
		});
		gameMenu.add(highscoreItem);

		final JMenuItem recording = new JMenuItem("Run Replay");
		recording.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String path = JOptionPane.showInputDialog("Enter the path of the recording!");
				String line = null;
				ArrayList<String> lines = new ArrayList<String>();
				try {
					BufferedReader br = new BufferedReader(new FileReader(path));
					while((line = br.readLine()) != null) lines.add(line);
					br.close();
					int width = Integer.valueOf(lines.get(0));
					int height = Integer.valueOf(lines.get(1));
					int bombs = Integer.valueOf(lines.get(2));
					Recorder r = new Recorder(width, height, bombs);
					r.parseBoard(lines.get(3));
					for(int i = 4; i < lines.size(); i++) {
						String split[] = lines.get(i).split(",");
						r.moves.add(new ThreeTuple<Integer,Integer,Integer>(Integer.valueOf(split[0]),Integer.valueOf(split[1]),Integer.valueOf(split[2])));
					}
					recorder = r;
					rc = new RecordingController(recorder);
				} catch(Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(GameFrame.mineFrame, "Error loading recording");
				}
			}
		});
		gameMenu.add(recording);

		optionsMenu = new JMenu("Options");

		final JMenuItem borderOutlines = new JMenuItem("Toggle Outlines (on)");
		borderOutlines.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				outlines = !outlines;
				if(outlines) {
					borderOutlines.setText("Toggle Outlines (on)");
				} else borderOutlines.setText("Toggle Outlines (off)");
				smileyButton.getActionListeners()[0].actionPerformed(null);
			}
		});
		optionsMenu.add(borderOutlines);

		final JMenuItem godMode = new JMenuItem("'Godmode' (off)");
		godMode.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				godmode = !godmode;
				if(godmode) {
					godMode.setText("'Godmode' (on)");
				} else godMode.setText("'Godmode' (off)");
				smileyButton.getActionListeners()[0].actionPerformed(null);
			}
		});
		optionsMenu.add(godMode);
		
		final JMenuItem save = new JMenuItem("Save on game over");
		save.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				//recorder.Save("test.txt");
				saveLoc = JOptionPane.showInputDialog(mineFrame, "Enter the location you want to save to (like mysave.txt)");
				if(saveLoc != "") saveReplay = true;
				else JOptionPane.showMessageDialog(mineFrame, "Please enter a valid save name!");
			}
		});
		optionsMenu.add(save);

		final JMenuItem test = new JMenuItem("Testing Feature");
		test.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				//recorder.Save("test.txt");
				
			}
		});
		optionsMenu.add(test);
		
		final JMenuItem cagematch = new JMenuItem("Cage Mode (Off)");
		cagematch.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				cagemode = !cagemode;
				imgSmiley = invert(imgSmiley);
				if(cagemode) {
					cagematch.setText("Cage Mode (ON)");
					imgSmiley = new ImageIcon(imgSmiley.getImage()) {
						/**
						 * 
						 */
						private static final long serialVersionUID = 1L;

						@Override
					    public synchronized void paintIcon(Component c, Graphics g, int x, int y) {
					    	Graphics2D g2 = (Graphics2D)g.create();
					    	g2.translate(0, this.getIconHeight());
					    	g2.scale(1, -1);
					    	super.paintIcon(c, g2, x, y);
					    }
					};
					int test = 0;
					for(ImageIcon icon : Tile.imgNumbers) {
						Tile.imgNumbers[test] = invert(icon);
						test++;
					}
					Tile.imgBlank = invert(Tile.imgBlank);
					Tile.imgUnclicked = invert(Tile.imgUnclicked);
					for(Tile tt[] : mineGrid.tiles) {
						for(Tile t : tt) {
							t.updateCaption();
						}
					}
				} else {
					cagematch.setText("Cage Mode (Off)");
					imgSmiley = new ImageIcon(imgSmiley.getImage()) {
						/**
						 * 
						 */
						private static final long serialVersionUID = 1L;

						@Override
					    public synchronized void paintIcon(Component c, Graphics g, int x, int y) {
					    	Graphics2D g2 = (Graphics2D)g.create();
					    	g2.translate(0, 0);
					    	g2.scale(1, 1);
					    	super.paintIcon(c, g2, x, y);
					    }
					};
					int test = 0;
					for(ImageIcon icon : Tile.imgNumbers) {
						Tile.imgNumbers[test] = invert(icon);
						test++;
					}
					Tile.imgBlank = invert(Tile.imgBlank);
					Tile.imgUnclicked = invert(Tile.imgUnclicked);
					for(Tile tt[] : mineGrid.tiles) {
						for(Tile t : tt) {
							t.updateCaption();
						}
					}
				}
				smileyButton.setIcon(imgSmiley);
			}
		});
		optionsMenu.add(cagematch);

		menuBar.add(optionsMenu);

		mineFrame.setJMenuBar(menuBar);
		mineFrame.add(pan);
		mineFrame.setTitle("PRHS Minesweeper");
		mineFrame.setVisible(true);
		mineFrame.setDefaultCloseOperation(3);
		mineFrame.pack();

	}

	public static void resetBoard(int width, int height/*, GridBagConstraints c*/, int bombs) {
		if(bombs > 0 && bombs < ((width-1)*(height-1)) && width > 0 && height > 0) {
			System.out.println("Resetting Game");
			pan.remove(mineGrid);
			mineFrame.repaint();
			mineFrame.validate();
			mineGrid = new Grid(mineFrame, width, height, bombs);
			mineGrid.setBorder(bev);
			mineGrid.setBackground(bg);
			c.gridy = 1;
			pan.add(mineGrid, c);
			c.gridy = 0;
			timer.stop();
			lblTimer.setText("000");
			GameFrame.lblBombs.setText(""+bombs);
			mineFrame.repaint();
			mineFrame.validate();
			mineFrame.pack();
			recorder = new Recorder(width, height, bombs);
		} else {
			JOptionPane.showMessageDialog(mineFrame, "Invalid size or bomb count!");
		}

	}
	
	public static ImageIcon invert(ImageIcon icon) {
		BufferedImage img = new BufferedImage(icon.getImage().getWidth(mineFrame),
				icon.getImage().getWidth(mineFrame), BufferedImage.TYPE_INT_RGB);
		Graphics graph = img.getGraphics();
		graph.drawImage(icon.getImage(),0, 0, mineFrame);
		for(int x = 0; x < img.getWidth(); x++) {
			for(int y = 0; y < img.getHeight(); y++) {
				int argb = img.getRGB(x, y);
				//System.out.print(argb + " -> ");
				int r = (argb >>> 16) & 0xff;
				int g = (argb >>> 8) & 0xff;
				int b = argb & 0xff;
				if(r > 255) r = 255;
				if(g > 255) g = 255;
				if(b > 255) b = 255;
				r = 255 - r;
				g = 255 - g;
				b = 255 - b;
				img.setRGB(x, y, getRGB(r,g,b));
				//System.out.println(getRGB(r,g,b));
			}
		}
		return new ImageIcon(img);
	}
	
	public static int getRGB(int r, int g, int b) {
		return((r&0x0ff)<<16)|((g&0x0ff)<<8)|(b&0x0ff);
	}
	

}