package core.tilestate;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.Serializable;

public class Tile implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 303946875953233384L;
	private String type							= null;
	private int strength						= 0;
	private int strength_used					= 0;
	private int axis							= 0;
	private Tile parent							= null;
	private int row								= 0;
	private int col								= 0;
	private Color color							= new Color( 255, 255, 255, 0 );
	private transient BufferedImage image		= null;
	private transient boolean focused			= false;
	private boolean hidden						= false;
	private boolean prebeam						= false;
	
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
	public void strength( int strength ) 			{ this.strength 	= strength;		}
	public void setDirection( int axis )			{ this.axis			= axis;			}
	public void setBeamMaster( Tile beamsource )
	{
		this.parent		= beamsource;
	}
	
	public void setTileState( ITileState tileState )	
	{ 
		this.type		= tileState.type();
		this.image		= tileState.image();
		this.hidden		= tileState.hidden();
	}
	
	public void hidden( boolean hidden )		{ this.hidden = hidden;					}
	public void focus( boolean blFocus )		{ this.focused = blFocus;				}
	public void color( Color color )			{ this.color = color;					}
	public void image( BufferedImage image )	{ this.image = image;					}
	public void isPrebeam( boolean prebeam )	{ this.prebeam = prebeam;				}			
	public void type( String type )				{ this.type = type;						}
	public void usedStrength( int strength)		{ this.strength_used = strength;		}
	
	// Geta - Methoden
	public int direction()						{ return this.axis;						}
	public Tile beamsource()					{ return this.parent; 					}
	public Tile parent()						{ return this.parent; 					}
	public String type()						{ return this.type;						}
	public int row()							{ return this.row;						}
	public int col()							{ return this.col;						}
	public BufferedImage image()				{ return this.image;					}
	public int strength()						{ return this.strength;					}
	public boolean focused()					{ return this.focused;					}
	public boolean hidden()						{ return this.hidden;					}
	public Color color()						{ return this.color;					}
	public boolean isPrebeam()					{ return this.prebeam;					}
	public int usedStrength()					{ return this.strength_used;			}
}