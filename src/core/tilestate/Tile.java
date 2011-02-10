package core.tilestate;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.Serializable;

public class Tile implements Serializable, Cloneable
{
	public final transient static Color CBLUE			= new Color( 3, 115, 210, 110 );
	public final transient static Color CRED			= new Color( 255, 0, 0, 100 );
	public final transient static Color CYELLOW			= new Color( 255, 212, 0, 100 );
	public final transient static Color CGREEN			= new Color( 76, 188, 64, 100 );
	public final transient static Color CTRANSPARENT	= new Color( 255, 255, 255, 0 );	
	
	private static final long serialVersionUID = 303946875953233384L;
	
	private String type							= null;
	private int strength						= 0;
	private int axis							= 0;
	private Tile parent							= null;
	private int row								= 0;
	private int col								= 0;
	private Color color							= new Color( 255, 255, 255, 0 );
	private transient BufferedImage image		= null;

	private transient boolean focused			= false;
	private boolean hidden						= false;
	
	public final static int HORIZONTAL		= 0;
	public final static int VERTICAL		= 1;
	
	public final static int LEFT			= 0;
	public final static int RIGHT			= 1;
	public final static int TOP				= 2;
	public final static int BOTTOM			= 3;
	
	public Tile( Tile beamsource, int row, int col )
	{
		this.parent		= beamsource;

		this.row		= row;
		this.col		= col;
	}
	
	// Seta - Methoden
	public void setTileState( ITileState tileState )	
	{ 
		this.type		= tileState.type();
		this.image		= tileState.image();
		this.hidden		= tileState.hidden();
	}
	
	public void setDirection( int axis )		{ this.axis = axis;			}
	public void strength( int strenght )		{ this.strength = strenght;	}
	public void parent( Tile parentTile )		{ this.parent = parentTile;	}
	public void color( Color color )			{ this.color = color;		}
	public void image( BufferedImage image )	{ this.image = image;		}
	public void type( String type )				{ this.type = type;			}
	public void hidden( boolean hidden )		{ this.hidden = hidden;		}
	public void focus( boolean blFocus )		{ this.focused = blFocus;	}
	
	// Geta - Methoden
	public Tile parent()						{ return this.parent; 		}
	public Color color()						{ return this.color;		}
	public BufferedImage image()				{ return this.image;		}
	public String type()						{ return this.type;			}
	public int direction()						{ return this.axis;			}
	public int row()							{ return this.row;			}
	public int col()							{ return this.col;			}
	public int strength()						{ return this.strength;		}
	public boolean focused()					{ return this.focused;		}
	public boolean hidden()						{ return this.hidden;		}
	
	protected Object clone()
	{
		try 									{ return (Tile)super.clone(); }	 
		catch (CloneNotSupportedException e) 	{ return null;					}
	}
}