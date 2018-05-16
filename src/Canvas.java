import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;


public class Canvas extends JPanel
{
	private ArrayList<Block> blocks 		= new ArrayList<Block>();
	private long ms							= 0;
	private boolean pause					= false;
	private JButton btn = new JButton("Click me");
	private Graphics2D g2d					= null;
	private Image actor_img					= null;
	private Image actor_right				= null;
	private Image actor_left				= null;
	private Actor actor						= null;
	private double curr_jump				= 0;
	
	public Canvas()
	{
		actor = new Actor(10,10);
		actor_img = getToolkit().getDefaultToolkit().getImage("./sphere.png");
		actor_img = actor_img.getScaledInstance(Values.ACTOR_SIZE, Values.ACTOR_SIZE, Image.SCALE_DEFAULT);
		actor_right = getToolkit().getDefaultToolkit().getImage("./actor_final.png");
		actor_left = getToolkit().getDefaultToolkit().getImage("./actor_final_l.png");
		
		javax.swing.Timer t = new javax.swing.Timer(1,new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				ms += 1;
			}
		}); 
		t.start();
	}
	
	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		g2d = (Graphics2D) g;
		g2d.setColor(Values.bg_colour);
		g2d.fillRect(0, 0, Values.canvas_width, Values.canvas_height);
		
		//Draw cursor
		if(ms%80 >= 0 && ms%80<=5)
		
		{}else
		{
			g2d.setColor(Color.GREEN);
			g2d.fillRect(Values.x,Values.y, Values.CURSOR_SIZE,Values.CURSOR_SIZE);
		}
		
		//Draw All Blocks
		for(Block b:blocks)
		{
			//Draw actual block
			g2d.setColor(b.getColour());
			g2d.fillRect((int)(b.getX())+(Values.GAP),(int)(b.getY())+(Values.GAP), b.getSize(),b.getSize());//*Values.CURSOR_SIZE*Values.SCALE,Values.CURSOR_SIZE*Values.SCALE);
			//Magenta square around block if it's marked
			if(b.isMarked())
			{
				g2d.setColor(Color.MAGENTA.darker());
				g2d.drawRect((int)(b.getX())-1,(int)(b.getY())-1, b.getSize()+1,b.getSize()+1);
			}
			//Draw white circle if spawn point
			if(b.isSpawnPoint())
			{
				g2d.setColor(Color.WHITE.brighter());
				g2d.drawOval((int)b.getX(), (int)b.getY(), b.getSize(), b.getSize());
				//g2d.drawLine(b.getX()+b.getSize()/2, b.getY(), b.getX()+b.getSize()/2, b.getY()+b.getSize());
			}
			
			//Draw diagonal cross if not rigid
			if(!b.isRigid())
			{
				g2d.setColor(Color.WHITE.brighter());
				g2d.drawLine((int)b.getX(), (int)b.getY(), (int) (b.getX()+b.getSize()), (int)(b.getY()+b.getSize()));
				g2d.drawLine((int)(b.getX()+b.getSize()), (int)b.getY(), (int)b.getX(),(int)( b.getY()+b.getSize()));
			}
			
			//Simulate physics
			if(Values.physics && !b.isCollided())
			{
				//Temp rect for collision detection
				Rectangle temp = b.getRectangle();
				temp.y = (int) (b.getY() + b.getWeight()/Values.GRAVITY);
				boolean move = true;
				
				for(Block b2:blocks)
				{	
					if(temp.intersects(b2.getRectangle()) && b2.getID() != b.getID() && b.isRigid() && b2.isRigid())
						move = false;
				}
				
				if(move)
					b.setY(b.getY() + b.getWeight()/Values.GRAVITY);
			}
			g2d.setColor(Color.RED);
			g2d.drawRect(b.getRectangle().x, b.getRectangle().y, b.getRectangle().width, b.getRectangle().height);
			
		}
		
		//Make actor jump when player presses the spacebar
		if(Values.physics)
		{
			if(Values.jump)
			{
				if(curr_jump + Values.JUMP_INC <= Values.MAX_JUMP)
				{
					actor.setY(actor.getY()-Values.JUMP_INC);
					curr_jump += Values.JUMP_INC;
				}
				else
				{
					Values.jump = false;
					curr_jump = 0;
				}
			}
		}
		
		if(Values.physics)
		{
			int al = (int) actor.getX();
			int ab = (int) actor.getY() + Values.ACTOR_SIZE;
			Rectangle r = new Rectangle();
			r.x = al;
			r.y = ab;
			r.width = Values.ACTOR_SIZE;
			r.height = 2;
		
			//Check for bottom collision
			actor.setCollisionBottom(false);
			for(Block b:blocks)
			{
				if(b.getRectangle().intersects(r))
					actor.setCollisionBottom(true);
			}
			
			g.setColor(Color.ORANGE);
			if(!actor.collisionBottom())
			{
				g.drawRect((int)r.getX(),(int)r.getY(),(int)r.getWidth(),(int)r.getHeight());//Draw actor
				float i = (float) (actor.getWeight()/Values.GRAVITY);//Drop speed
				if(actor.getWeight()<0)
					actor.setY(actor.getY() - Values.JUMP_INC);//Float upwards
				else
					actor.setY(actor.getY() + i);//Fall down
			}
			else
			{
				if(Values.show_coll_bounds)
					g.fillRect((int)r.getX(),(int)r.getY(),(int)r.getWidth(),(int)r.getHeight());//bottom bounds
			}
			if(Values.show_coll_bounds)
			{
				if(actor.getLeftColl())
					g.fillRect((int)actor.getLeftBounds().getX(), (int)actor.getLeftBounds().getY(), actor.getLeftBounds().width, actor.getLeftBounds().height);//Left bounds
				else
					g.drawRect((int)actor.getLeftBounds().getX(), (int)actor.getLeftBounds().getY(), actor.getLeftBounds().width, actor.getLeftBounds().height);//Left bounds
				
				if(actor.getLeftColl())
					g.fillRect((int)actor.getRightBounds().getX(), (int)actor.getRightBounds().getY(), actor.getRightBounds().width, actor.getRightBounds().height);//Right bounds
				else
					g.drawRect((int)actor.getRightBounds().getX(), (int)actor.getRightBounds().getY(), actor.getRightBounds().width, actor.getRightBounds().height);//Right bounds
			}
		}
		
		if(Values.canvas_focused)
		{
			g2d.setStroke(new BasicStroke(10));
			g2d.setColor(new Color(Color.GREEN.getRGB()).brighter());
			//g2d.drawRect(0, 0, Values.SCR_WIDTH - Values.LEFT_W - Values.PROP_W, Values.SCR_HEIGHT - Values.TOP_H - 30);
			g2d.drawRect(0, 0, Values.canvas_width-5,Values.canvas_height - 5);
		}
		else
		{
			g2d.setStroke(new BasicStroke(10));
			g2d.setColor(new Color(Color.RED.getRGB()).brighter());
			//g2d.drawRect(0, 0, Values.SCR_WIDTH - Values.LEFT_W - Values.PROP_W, Values.SCR_HEIGHT - Values.TOP_H - 30);
			g2d.drawRect(0, 0, Values.canvas_width-5,Values.canvas_height - 5);
		}
		
		//Draw actor
		//g2d.drawImage(actor_img, (int)actor.getX(),(int)actor.getY(), this);
		g.setColor(Color.ORANGE);
		g2d.fillOval((int)actor.getX(),(int)actor.getY(), Values.ACTOR_SIZE,Values.ACTOR_SIZE);
		/*if(Values.direction.equals("RIGHT"))
			g2d.drawImage(actor_right, actor.getX(),actor.getY(), this);
		else if(Values.direction.equals("LEFT"))
			g2d.drawImage(actor_left, actor.getX(),actor.getY(), this);*/
	}
	
	public Actor getActorInstance()
	{
		return this.actor;
	}
	
	public void addBlock(Block b)
	{
		blocks.add(b);
	}
	
	public ArrayList<Block> getBlocks()
	{
		return blocks;
	}
	
	public void setBlocks(ArrayList<Block> bk)
	{
		//this.blocks = null;
		//this.blocks = new ArrayList<Block>();
		int i = 0;
		if(bk!=null && !bk.isEmpty())
		{
			for(Block b:bk)
			{
				blocks.get(i).setX(b.getX());
				blocks.get(i).setY(b.getY());
				//blocks.add(new Block((int)b.getX(),(int)b.getY(),b.getSize(),b.getColour(),b.getID()));
				i++;
			}
		}
		else
		{
			System.err.println("Backup empty");
		}
	}
}
