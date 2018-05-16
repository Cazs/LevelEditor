import java.awt.Color;

public class Values 
{
	public static boolean canvas_focused		= true;
	public static int STEP 						= 10;
	public static int SCR_WIDTH 				= 1000;
	public static int SCR_HEIGHT 				= 700;
	public static int x 						= 10;
	public static int y 						= 10;
	public static boolean erase			 		= false;
	public static int CURSOR_SIZE				= 10;
	public static int SCALE						= 1;
	public static int GAP						= 0;
	public static int GRAVITY					= 4;//the higher the gravity the slower the object will fall
	public static boolean colour				= false;
	public static int TOP_H						= 100;
	public static int LEFT_W					= 200;
	public static Color block_colour			= Color.DARK_GRAY.brighter();
	public static Color bg_colour				= Color.BLACK;
	public static Color hl_colour				= Color.MAGENTA;
	public static Color curs_colour				= Color.GREEN;
	public static int pan_multiplier			= 10;
	public static boolean physics				= false;
	public static int PROP_W					= 200;
	public static int ACTOR_SPEED				= 10;
	public static boolean run					= false;
	public static String direction				= "RIGHT";
	public static final double MAX_SPEED		= 7;
	public static int canvas_width				= 0;
	public static int canvas_height				= 0;
	public static int ACTOR_SIZE				= 15;
	public static final double MAX_JUMP			= 70;  
	public static boolean jump					= false;
	public static double JUMP_INC				= 3.0;
	public static boolean show_coll_bounds		= false;
}
