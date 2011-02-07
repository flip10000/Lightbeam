package core.tilefactory;

import java.io.IOException;
import java.util.ArrayList;

import core.tilestate.ITileState;


public abstract class AbstractTileSetFactory 
{
	protected ArrayList<AbstractTileSet> tileset	= new ArrayList<AbstractTileSet>();
	private ITileState tilestate					= null;
	
	public AbstractTileSetFactory() throws IOException	{ this.createTiles(); }
	
	public ITileState tile( int i ) 
	{
		return tileset.get( i ); 	
	}
	
	public int length()				{ return tileset.size();	}
	
	// TileState-Methods
	public void setSelected( ITileState tileState )		
	{ 
		this.tilestate = tileState;	
	}
	
	public ITileState getSelected() 					{ return this.tilestate;		}

	protected abstract void createTiles() throws IOException;	
}