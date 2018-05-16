import java.awt.Color;
import java.awt.Rectangle;
import java.awt.geom.Line2D;


public class Block 
{
	private double x = 0;
	private double y = 0;
	private Rectangle bounds 			= new Rectangle();
	private Color col 					= null;
	private int size 					=  10;
	private boolean marked 				= false;
	private boolean spawn_point	 		= false;
	private boolean rigid				= true;
	private boolean collided			= false;
	private double weight				= 0;
	private long id						= 0;
	
	public Block(int x,int y,int size,Color col,long id)
	{
		bounds.setBounds(x,y,size,size);
		
		this.id = id;
		this.size = size;
		this.col = col;
		this.x = x;
		this.y = y;
	}
	
	public void setID(long id)
	{
		this.id = id;
	}
	
	public long getID()
	{
		return this.id;
	}
	
	public void setWeight(double w)
	{
		this.weight = w;
	}
	
	public double getWeight()
	{
		return this.weight;
	}
	
	public void setCollided(boolean c)
	{
		this.collided = c;
	}
	
	public boolean isCollided()
	{
		return this.collided;
	}
	
	public void setRigid(boolean r)
	{
		this.rigid = r;
	}
	
	public boolean isRigid()
	{
		return this.rigid;
	}
	
	public void setSpawnPoint(boolean s)
	{
		this.spawn_point = s;
	}
	
	public boolean isSpawnPoint()
	{
		return this.spawn_point;
	}
	
	public void setColour(Color col)
	{
		this.col = col;
	}
	
	public void setMarked(boolean m)
	{
		this.marked = m;
	}
	
	public boolean isMarked()
	{
		return marked;
	}
	
	public int getSize()
	{
		return size;
	}
	
	public double getX()
	{
		return x;
	}
	
	public double getY()
	{
		return y;
	}
	
	public void setX(double d)
	{
		bounds.x = (int) d;
		this.x = d;
	}
	
	public void setY(double y)
	{
		bounds.y = (int) y;
		this.y = y;
	}
	
	public Rectangle getRectangle()
	{
		return bounds;
	}
	
	public Color getColour()
	{
		return col;
	}

	public boolean collidesActor(Actor a)
	{
		a.setLeftColl(a.getLeftBounds().intersects(this.bounds));
		a.setRightColl(a.getRightBounds().intersects(this.bounds));
		//if this block collides with either of the sides
		if(a.getLeftColl() || a.getRightColl())
		{
			return true;
		}
		return false;
	}
}
