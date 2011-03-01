package lightbeam.solution;

import core.GameObjects;
import core.tilestate.Tile;
import core.tilestate.TileArray;
import lightbeam.solution.strategies.ConclusiveReachable;

public class LogicClient extends GameObjects
{
	private boolean result	= false; 
	private TileArray map	= null;
	
	public LogicClient() {}
	
	public void check( TileArray map, ILogicResponse callee )
	{
		this.map	= map.createClone();
		this.result	= false;
		
		this.simulateMode( this.map );		

		LogicContext lContext	= new LogicContext( this.map );
		
		lContext.setLogic( new ConclusiveReachable() );
		
		while( lContext.getResult() == true )
		{		
			lContext.executeLogic();
			this.map	= lContext.getMap();
			this.markTiles();
			callee.logicResponse( this.map );

			if( lContext.getResult() == true )
			{
				this.result = true;
			} else
			{
				// ToDo: Nächste Strategie auswählen!!
			}

		}
	}
	
	public boolean getResult() 	{ return this.result; 	}
	public TileArray getMap() 	{ return this.map; 		}
	
	private void simulateMode( TileArray map )
	{
		int rows	= map.rows();
		int cols	= map.cols();
		
		for( int row = 0; row < rows; row++ )
		{
			for( int col = 0; col < cols; col++ )
			{
				Tile tile	= map.tile( row, col );
				
				if( tile.type().equals( "beam" ) ) 			{ tile.type( "field" );		}
				if( !tile.type().equals( "beamsource" ) )	{ tile.color( Tile.CRED );	}
			}
		}
	}
	
	private void markTiles()
	{
		int rows	= this.map.rows();
		int cols	= this.map.cols();
		
		for( int row = 0; row < rows; row++ )
		{
			for( int col = 0; col < cols; col++ )
			{
				Tile tile	= this.map.tile( row, col );
				
				if( tile.type().equals( "beam" ) && !tile.color().equals( Tile.CGREEN ) )
				{
					this.map.tile( row, col ).color( Tile.CGREEN );
				}
			}
		}
	}
}
