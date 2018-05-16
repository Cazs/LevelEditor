import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class Main extends JFrame 
{	
	private static Key key 							= new Key();
	private JMenuBar bar 							= new JMenuBar();
	private JMenu menu 								= new JMenu();
	private JMenuItem colour 						= new JMenuItem("Colour:");
	private JPanel left 							= new JPanel();
	private JPanel top 								= new JPanel();
	private JPanel properties 						= new JPanel();
	private JColorChooser col 						= null;
	private boolean blk 							= false;
	private boolean bg								= false;
	private boolean m_int							= false;
	private int i									= 0;
	private boolean set_start						= false;
	private int x									= 0;
	private int y									= 0;
	public static long id							= 0;
	private static ArrayList<Block> backup			=  new ArrayList<Block>();
	private ImageIcon play							= null;
	private ImageIcon stop							= null;
	
	
	private void setFocusHere()
	{
		this.requestFocusInWindow();
	}
	
	public Main()
	{
		super("Platformer Level Editor");
		
		try 
		{
			UIManager.setLookAndFeel(javax.swing.plaf.nimbus.NimbusLookAndFeel.class.getName());
		} 
		catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e1) 
		{
			e1.printStackTrace();
		}
		
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setBounds(0,0,Values.SCR_WIDTH,Values.SCR_HEIGHT);
		this.setLocationRelativeTo(null);
		this.setLayout(new BorderLayout());
		
		final JFrame colour_frame = new JFrame();
		colour_frame.setBounds(0,0,480,320);
		colour_frame.setLocationRelativeTo(null);
		col = new JColorChooser();
		colour_frame.add(col);
		
		JButton block = new JButton("Choose block colour");
		JButton bkg = new JButton("Choose background colour");
		JButton spawn = new JButton("Set Spawn Point To Selected");
		JButton rigid = new JButton("Set Rigid Body To Selected");
		final JButton physics = new JButton();//"Simulate Physics"
		JButton wght = new JButton("Apply Weight To Selected");
		final JLabel notif	= new JLabel("Notifications");
		final JTextField weight	= new JTextField();
		
		this.setVisible(true);
		this.setFocusable(true);
		
		top.setBackground(Color.CYAN);
		top.setPreferredSize(new Dimension(400,Values.TOP_H));
		
		left.setPreferredSize(new Dimension(Values.LEFT_W,400));
		left.setBackground(Color.ORANGE);
		
		properties.setPreferredSize(new Dimension(Values.PROP_W,400));
		properties.setBackground(Color.YELLOW);
		
		block.setPreferredSize(new Dimension(Values.LEFT_W,50));
		bkg.setPreferredSize(new Dimension(Values.LEFT_W,50));
		spawn.setPreferredSize(new Dimension(Values.LEFT_W,50));
		rigid.setPreferredSize(new Dimension(Values.LEFT_W,50));
		physics.setPreferredSize(new Dimension(Values.LEFT_W,50));
		weight.setPreferredSize(new Dimension(Values.LEFT_W,40));
		wght.setPreferredSize(new Dimension(Values.LEFT_W,50));
		notif.setPreferredSize(new Dimension(200,50));
		
		
		left.add(block);
		left.add(bkg);
		left.add(spawn);
		left.add(rigid);
		left.add(physics);
		left.add(weight);
		left.add(wght);
		top.add(notif);
	
		JPanel propHolder = new JPanel();
		propHolder.setLayout(new GridLayout(4,1));
		
		final JCheckBox rig				= 	new JCheckBox("Rigid");
		final JCheckBox spwn			= 	new JCheckBox("Spawn Point");
		final JLabel w					= 	new JLabel("Weight: ");
		final JLabel lbl_size			=	new JLabel("Size: ");
		final JLabel lbl_colour			= 	new JLabel("Colour: ");
		final JLabel lbl_id				= 	new JLabel("ID: ");
		final JLabel lbl_x				= 	new JLabel("X: ");
		final JLabel lbl_y				= 	new JLabel("Y: ");
		
		try 
		{
			//Play button image
			play	= new ImageIcon(new URL("file:play.png"));
			Image image = play.getImage();
			image = image.getScaledInstance(image.getWidth(null)/2, image.getHeight(null)/2, Image.SCALE_SMOOTH);
			play.setImage(image);
			
			//Stop button image
			stop = new ImageIcon(new URL("file:stop.png"));
			image = stop.getImage();
			image = image.getScaledInstance(image.getWidth(null)/2, image.getHeight(null)/2, Image.SCALE_SMOOTH);
			stop.setImage(image);
		}
		catch (MalformedURLException e1) 
		{
			e1.printStackTrace();
		}
		physics.setIcon(play);
		
		propHolder.add(rig);
		propHolder.add(spwn);
		propHolder.add(w);
		propHolder.add(lbl_size);
		propHolder.add(lbl_colour);
		propHolder.add(lbl_id);
		propHolder.add(lbl_x);
		propHolder.add(lbl_y);
		
		properties.setLayout(new GridLayout(6,2));
		properties.add(propHolder);
		
		add(properties,BorderLayout.EAST);
		add(key.getCanvas(),BorderLayout.CENTER);
		add(top,BorderLayout.NORTH);
		add(left,BorderLayout.WEST);
		
		Thread t_canv = new Thread(new Runnable() 
		{
			
			@Override
			public void run() 
			{
				while(true)
				{
					try
					{
						Thread.sleep(5);
					}
					catch(InterruptedException e)
					{
						
					}
					key.getCanvas().repaint();
				}
			}
		});
		t_canv.start();
		
		key.getCanvas().addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e) 
			{
				super.mouseClicked(e);
				setFocusHere();
			}
		});
		
		javax.swing.Timer scr = new javax.swing.Timer(1,new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				//Refresh screen size
				Values.SCR_WIDTH = getContentPane().getWidth();//Toolkit.getDefaultToolkit().getScreenSize().width;
				Values.SCR_HEIGHT =getContentPane().getHeight();// Toolkit.getDefaultToolkit().getScreenSize().height;
				Values.canvas_width = Values.SCR_WIDTH - Values.PROP_W - Values.LEFT_W;
				Values.canvas_height = Values.SCR_HEIGHT - Values.TOP_H; 
			}
		});
		scr.start();
		
		this.addFocusListener(new FocusAdapter()
		{
			@Override
			public void focusLost(FocusEvent e) 
			{
				super.focusLost(e);
				Values.canvas_focused = false;
			}
			
			@Override
			public void focusGained(FocusEvent e) 
			{
				super.focusGained(e);
				Values.canvas_focused = true;
			}
		});
		
		/**Update properties pane**/
		javax.swing.Timer tprop_pane = new javax.swing.Timer(1, new ActionListener() 
		{
			
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				int numMarked = 0,i=0;
				for(Block b:key.getCanvas().getBlocks())
				{
					if(b.isMarked())
					{
						numMarked++;
					}
					i++;
				}
				
				if(numMarked==1)
				{
					for(Block b:key.getCanvas().getBlocks())
					{
						if(b.isMarked())
						{
							rig.setSelected(b.isRigid());
							spwn.setSelected(b.isSpawnPoint());
							w.setText("Weight: " + b.getWeight());
							lbl_size.setText("Size: "+b.getSize());
							lbl_colour.setText("Color: ["+b.getColour().getRed()+","+b.getColour().getGreen()+","+b.getColour().getBlue()+"]");
							lbl_id.setText("ID: "+b.getID());
							lbl_x.setText("X: "+b.getX());
							lbl_y.setText("Y: "+b.getY());
						}
					}
				}
				else
				{
					rig.setSelected(false);
					spwn.setSelected(false);
					w.setText("Weight: ");
					lbl_size.setText("Size: ");
					lbl_colour.setText("Color: ");
					lbl_id.setText("ID: ");
					lbl_x.setText("X: ");
					lbl_y.setText("Y: ");
				}
			}
		});
		tprop_pane.start();
		
		block.addActionListener(new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				colour_frame.setVisible(true);//show colour frame
				blk = true;
			}
		});
		
		colour_frame.addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e) 
			{
				super.windowClosing(e);
				if(blk)
				{
					Values.block_colour = col.getColor().brighter();
					blk = false;
				}
				
				if(bg)
				{
					Values.bg_colour = col.getColor().brighter();
					bg = false;
				}
			}
		});
		
		wght.addActionListener(new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				if(weight.getText().length()>0)
				{
					for(Block b: key.getCanvas().getBlocks())
						if(b.isMarked())
						{
							b.setWeight(Double.valueOf(weight.getText()));
							notif.setText(String.format("Weight of %s units applied to block: " + b,weight.getText()));
						}
				}
				else
				{
					JOptionPane.showMessageDialog(null, "Cannot have empty weight","Error",JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		rigid.addActionListener(new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				for(Block b: key.getCanvas().getBlocks())
					if(b.isMarked())
					{
						if(b.isRigid())
							b.setRigid(false);
						else
							b.setRigid(true);
					}
				
				notif.setText("Applied rigid body to selected blocks");
				JOptionPane.showMessageDialog(null, "Applied rigid body to selected blocks","Rigid application Success",JOptionPane.INFORMATION_MESSAGE);
			}
		});
		
		physics.addActionListener(new ActionListener() 
		{
			
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				if(Values.physics)
				{
					key.getCanvas().setBlocks(backup);
					Values.physics	= false;
					notif.setText("Physics simulation ended");
					//physics.setText("Simulate physics");
					physics.setIcon(play);
					key.getCanvas().getActorInstance().setX(10);
					key.getCanvas().getActorInstance().setY(10);
				}
				else
				{
					backup = null;
					backup = new ArrayList<Block>();
					for(Block b:key.getCanvas().getBlocks())
						backup.add(new Block((int)b.getX(),(int)b.getY(),b.getSize(),b.getColour(),b.getID()));
					
					Values.physics = true;
					notif.setText("Physics simulation started");
					//physics.setText("Stop physics simulation");
					physics.setIcon(stop);
				}
			}
		});
		
		spawn.addActionListener(new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				for(Block b: key.getCanvas().getBlocks())
					if(b.isMarked())
					{
						if(b.isSpawnPoint())
							b.setSpawnPoint(false);
						else
							b.setSpawnPoint(true);
					}
			}
		});
		
		key.getCanvas().addMouseMotionListener(new MouseAdapter()
		{
			@Override
			public void mouseDragged(MouseEvent e) 
			{
				super.mouseDragged(e);
				int drag_x = e.getX()-x;
				int drag_y = e.getY()-y;
				
				for(Block b:key.getCanvas().getBlocks())
				{
					if(drag_x>0)
						b.setX(b.getX()+Values.pan_multiplier);
					else
						b.setX(b.getX()-Values.pan_multiplier);
					
					if(drag_y>0)
						b.setY(b.getY()+Values.pan_multiplier);
					else
						b.setY(b.getY()-Values.pan_multiplier);
				}
			}
		});
	
		key.getCanvas().addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e) 
			{
				super.mouseClicked(e);
				for(Block b:key.getCanvas().getBlocks())
				{
					Rectangle r = new Rectangle();
					r.setBounds(e.getX(), e.getY(), 1, 1);
					
					if(r.intersects(b.getRectangle()))
					{
						if(b.isMarked())
							b.setMarked(false);
						else
							b.setMarked(true);
					}
				}
			}
			
			@Override
			public void mousePressed(MouseEvent e) 
			{
				super.mouseClicked(e);
				/**
				 * These are the coordinates of the location where the user started dragging from.
				 * Used to determine which side the user dragged to.
				 */
				x = e.getX();
				y = e.getY();
			}

		});
	
		col.getSelectionModel().addChangeListener(new ChangeListener() 
		{
			@Override
			public void stateChanged(ChangeEvent e) 
			{
				if(blk)//if changing the block colour
				{
					Values.block_colour = col.getColor().brighter();
					for(Block b:key.getCanvas().getBlocks())
						if(b.isMarked())
							b.setColour(Values.block_colour);
				}
				
				if(bg)//if changing the background colour
				{
					Values.bg_colour = col.getColor().brighter();
				}
			}
		});
	
		bkg.addActionListener(new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				bg = true;
				colour_frame.setVisible(true);//display colour frame
			}
		});
	}
	
	public static void main(String[] args)
	{
		Main m = new Main();
		m.setVisible(true);
		m.addKeyListener(key);
		Thread tKey	= new Thread(new Runnable()
		{
			
			@Override
			public void run() 
			{
				javax.swing.Timer t = new javax.swing.Timer(20,key);
				t.start();
			}
		});
		tKey.start();
	}

}
