import java.awt.Rectangle;
import java.util.ArrayList;


public class Actor 
{
	private double x						= 0;
	private double y						= 0;
	private Rectangle bounds_left 			= null;
	private Rectangle bounds_right 			= null;
	private double weight					= 5;
	private boolean fall					= false;
	private boolean l_coll					= false;
	private boolean r_coll					= false;
	
	public Actor(double x,double y)
	{
		this.x = x;
		this.y = y;
		bounds_left = new Rectangle();
		bounds_right = new Rectangle();

		bounds_left.setBounds((int)x, (int)y, 2, Values.ACTOR_SIZE-4);
		bounds_right.setBounds((int)x+Values.ACTOR_SIZE, (int)y, 2, Values.ACTOR_SIZE-4);
	}
	
	public Rectangle getLeftBounds()
	{
		return bounds_left;
	}
	
	public Rectangle getRightBounds()
	{
		return bounds_right;
	}
	
	public void setLeftColl(boolean l)
	{
		this.l_coll = l;
	}
	
	public void setRightColl(boolean r)
	{
		this.r_coll = r;
	}
	
	public boolean getRightColl()
	{
		return r_coll;
	}
	
	public boolean getLeftColl()
	{
		return l_coll;
	}
	
	public void setX(double x)
	{
		this.x = x;
		bounds_left.x = (int)x-2;
		bounds_right.x = (int)x+Values.ACTOR_SIZE;
	}
	
	public double getX()
	{
		return this.x;
	}
	
	public void setY(double y)
	{
		this.y = y;
		bounds_left.y = (int)y+2;
		bounds_right.y = (int)y+2;
	}
	
	public double getY()
	{
		return this.y;
	}
	
	public double getWeight()
	{
		return weight;
	}
	
	public void setCollisionBottom(boolean fall)
	{
		this.fall = fall;
	}
	
	public boolean collisionBottom()
	{
		return this.fall;
	}
	
	public boolean leftCollision(ArrayList<Block> blocks)
	{
		for(Block b:blocks)
		{
			if(this.bounds_left.intersects(b.getRectangle()))
			{
				return true;
			}
		}
		return false;
	}
	
	public boolean rightCollision(ArrayList<Block> blocks)
	{
		for(Block b:blocks)
		{
			if(b.getRectangle().intersects(this.bounds_right))
				return true;
		}
		return false;
	}

	public void setWeight(double w)
	{
		this.weight = w;
	}
}
