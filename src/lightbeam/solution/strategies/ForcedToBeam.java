package lightbeam.solution.strategies;

import java.util.ArrayList;

import core.tilestate.Tile;
import core.tilestate.TileArray;
import lightbeam.solution.ILogicStrategy;

public class ForcedToBeam implements ILogicStrategy 
{
	private TileArray map						= null;
	private boolean result						= false;
	private ArrayList<int[][]> fieldToSource	= null;
	private int[][] fields						= null;

	public void execute() 
	{
		this.result					= false;
		
		// Alle Beamsources in der map holen:
		ArrayList<Tile> beamsources	= this.map.getTilesInArea( "beamsource" );
		
		// Wenn vorhanden:
		if( beamsources != null )
		{
			int sSources			= beamsources.size();
			
			this.fieldToSource		= new ArrayList<int[][]>();
			this.fields				= new int[this.map.rows()][this.map.cols()];
			
			// Alle Beamsources durchlaufen:
			for( int i = 0; i < sSources; i++ )
			{
				Tile source		= beamsources.get(i);
				int strenght	= source.strength();
				int sRow		= source.row();
				int sCol		= source.col();
				
				// Wenn Beamsource-Stärke > 0:
				if( strenght > 0 )
				{
					int pos[][]		= new int[4][2];
					
					// Alle möglichen Feld-Positionen (nur Spalten) von links der Source
					// speichern:
					pos[0][0]		= sRow;
					pos[0][1]		= this.getTillLastLeftField( source );
					
					System.out.println(pos[0][1]);
				} else
				{
					this.fieldToSource.add( i, null );
				}
			}
		}
	}

	public TileArray getMap() 			{ return this.map; 		}
	public boolean getResult() 			{ return this.result; 	}

	public void setMap( TileArray map )	{ this.map = map;		}
	
	// Letztmögliches "Feld" links von der Beamsource zurückgeben:
	private int getTillLastLeftField( Tile source )
	{
		int strength					= source.strength();
		int sCol						= source.col();
		int leftLastPos					= -1;
		
		// Prüfen, ob links von Beamsource weiteres Beamsource liegt:
		Tile leftBlock	= this.map.getLeftBlocking( source, "beamsource" );
		
		// Wenn nein, dann:
		if( leftBlock == null )
		{
			leftLastPos	= ( sCol - strength > -1 )? sCol - strength : 0; 	
		} 
		// Ansonsten:
		else 
		{
			leftLastPos	= ( sCol - strength > leftBlock.col() )? ( sCol - strength ) : leftBlock.col() + 1;
		}
		
		// Wenn gefunden, dann Tile-Col-Positions speichern:
		if( leftLastPos > -1 && leftLastPos != source.col() ) 
		{
			return leftLastPos;
		}
		
		// "Nicht gefunden" zurückgeben:
		return -1;
	}
}
