import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JOptionPane;
import javax.swing.text.html.HTMLDocument.Iterator;

public class Key extends KeyAdapter implements ActionListener
{
	private boolean a_down 			= false;
	private boolean d_down 			= false;
	private boolean w_down 			= false;
	private boolean s_down 			= false;
	private boolean jump			= false;
	private double speed   			= 0;
	private double acceleration	   	= 0.3;
	private double deceleration	   	= 0.7;
	private double curr_jump       = 0;
	
	private final Canvas canvas = new Canvas();
	
	public Key()
	{
		canvas.setLayout(null);
	}
	
	@Override
	public void keyPressed(KeyEvent e) 
	{
		super.keyPressed(e);
		switch(e.getKeyCode())
		{
			case KeyEvent.VK_A:
				a_down = true;
				Values.direction="LEFT";
				if(!Values.physics)
				{
					//Move cursor...in edit mode
					if(Values.x-Values.STEP <= 300)
						for(Block b:canvas.getBlocks())
							b.setX(b.getX()+Values.STEP);
					else
						Values.x-=Values.STEP;
				}
				break;
			case KeyEvent.VK_D:
				d_down = true;
				Values.direction="RIGHT";
				if(!Values.physics)
				{
					//Move cursor...in edit mode
					if(Values.x+Values.STEP >= Values.canvas_width-300)
						for(Block b:canvas.getBlocks())
							b.setX(b.getX()-Values.STEP);
					else
						Values.x+=Values.STEP;
				}
				break;
			case KeyEvent.VK_W:
				w_down = true;
				if(!Values.physics)
				{
					if(Values.y-Values.STEP <= 300)
						for(Block b:canvas.getBlocks())
							b.setY(b.getY()+Values.STEP);
					else
						Values.y-=Values.STEP;
				}
				break;
			case KeyEvent.VK_S:
				s_down = true;
				if(!Values.physics)
				{
					if(Values.y+Values.STEP >= Values.canvas_height-300)
						for(Block b:canvas.getBlocks())
							b.setY(b.getY()-Values.STEP);
					else
						Values.y+=Values.STEP;
				}
				break;
			case KeyEvent.VK_SPACE:
				jump = true;
				Values.jump = true;
				break;
			//Add to map
			case KeyEvent.VK_ENTER:
				boolean addable = true;
				java.util.Iterator<Block> it = canvas.getBlocks().iterator();
				if(Values.erase)
				{
					int i = 0;
					//for(Block b:it)
					while(it.hasNext())
					{
						Block b = it.next();
						
						if(b.getX() == Values.x && b.getY() == Values.y)
						{
							it.remove();
						}
						i++;
					}
				}
				else
				{
					for(Block b:canvas.getBlocks())
						if(b.getX() == Values.x && b.getY() == Values.y)
						{
							addable = false;
							System.err.println("A block already exists here");
						}
					
					if(addable)
					{
						canvas.addBlock(new Block(Values.x,Values.y,Values.CURSOR_SIZE,Values.block_colour,Main.id));
						Main.id++;
					}
				}
				break;
			//Erase
			case KeyEvent.VK_E:
				if(Values.erase)
					Values.erase = false; 
				else 
					Values.erase = true;
				break;
			case KeyEvent.VK_R:
				if(Values.CURSOR_SIZE < 100)
					Values.CURSOR_SIZE += 10;
				break;
			case KeyEvent.VK_T:
				if(Values.CURSOR_SIZE > 10)
					Values.CURSOR_SIZE -= 10;
				break;
			case KeyEvent.VK_X:
				if(Values.colour) Values.colour = false; else Values.colour=true;
				break;
			
			/*case KeyEvent.VK_Z:
				Values.SCALE ++;
				
				int i = 0;
				for(Block b:canvas.getBlocks())
				{
					b.setX(b.getX()+(i*8*Values.SCALE));
					b.setY(b.getY()+(10*Values.SCALE));
					i++;
				}
				break;*/
		}
	}

	@Override
	public void keyReleased(KeyEvent e) 
	{
		super.keyReleased(e);
		switch(e.getKeyCode())
		{
			case KeyEvent.VK_A:
				a_down = false;
				break;
			case KeyEvent.VK_D:
				d_down = false;
				break;
			case KeyEvent.VK_W:
				w_down = false;
				break;
			case KeyEvent.VK_S:
				s_down = false;
				break;
		}
	}
	
	public Canvas getCanvas()
	{
		return canvas;
	}
	
	private void listen()
	{
		Actor a = canvas.getActorInstance();
		
		if(!a_down && !d_down)
		{
			//decrease speed
			if(speed > 0)
				speed -= deceleration;
			//Move world while decelerating
			if(Values.direction.equals("RIGHT") && speed > 0)
			{
				if(a!=null)
				{
					if(a.getX() + speed > Values.canvas_width - 50)
					{
						for(Block b:canvas.getBlocks())
							b.setX(b.getX() - speed);
					}
					else
					{
						//Move actor if no collision
						if(!a.rightCollision(canvas.getBlocks()))
							a.setX(a.getX() + speed);
						else
							speed = 0;
					}
				}
			}
			if(Values.direction.equals("LEFT") && speed > 0)
			{
				if(a!=null)
				{
					if(a.getX() - speed < 50)
					{
						for(Block b:canvas.getBlocks())
							b.setX(b.getX() + speed);
					}
					else
					{
						//Move actor if no collision
						if(!a.leftCollision(canvas.getBlocks()))
							a.setX(a.getX()- speed);
						else
							speed = 0;
					}
				}
			}
		}
		
		if(a_down && Values.physics)
		{
			//Increase speed
			if(speed < Values.MAX_SPEED)
			{
				speed += acceleration;
			}
			
			Values.run = true;
			if(a!=null)
			{
				//Move world
				if(a.getX()- speed < 300)
				{
					for(Block b:canvas.getBlocks())
						b.setX(b.getX() + speed);
				}
				else
				{
					//Move actor if no collision
					if(!a.leftCollision(canvas.getBlocks()))
						a.setX(a.getX()- speed);
					else
						speed = 0;
				}
			}
		}
		
		if(d_down && Values.physics)
		{
			//Increase speed
			if(speed < Values.MAX_SPEED)
			{
				speed += acceleration;
			}
			
			Values.run = true;
			if(a!=null)
			{
				//Move world
				if(a.getX() + speed > Values.canvas_width - 300)
				{
					for(Block b:canvas.getBlocks())
						b.setX(b.getX() - speed);
				}
				else
				{
					//Move actor if no collision
					if(!a.rightCollision(canvas.getBlocks()))
						a.setX(a.getX() + speed);
					else
						speed = 0;
				}
			}
		}
		
		if(w_down && Values.physics)
		{
			//Move actor
		}
		
		if(s_down && Values.physics)
		{
			//Move actor 
		}
	}

	private void shiftScreen()
	{
		Actor a = canvas.getActorInstance();
		if(a!=null)
		{
			if(a.getX() > Values.SCR_WIDTH-100)
			{
				for(Block b:canvas.getBlocks())
					if(b!=null)
						b.setX(b.getX()-1);
			}
			
			if(a.getX() < 100)
			{
				for(Block b:canvas.getBlocks())
					b.setX(b.getX()+1);
			}
			
			if(a.getY() > Values.SCR_HEIGHT-100)
			{
				for(Block b:canvas.getBlocks())
					b.setY(b.getY()-1);
			}
			
			if(a.getY() < 100)
			{
				for(Block b:canvas.getBlocks())
					b.setY(b.getY()+1);
			}
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		listen();
		//shiftScreen();
		//System.out.println(Values.physics);
	}
}