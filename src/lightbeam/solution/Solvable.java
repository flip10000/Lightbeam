package lightbeam.solution;

import core.tilestate.Tile;
import core.tilestate.TileArray;

public abstract class Solvable 
{
	private Solvable() {}
	
	public static boolean check( TileArray origin )
	{
		int rows	= origin.rows();
		int cols	= origin.cols();
		
		for( int row = 0; row < rows; row++ )
		{
			for( int col = 0; col < cols; col++ )
			{
				Tile tile	= origin.tile( row, col );
				
				if( tile.type().equals( "field" ) || ( tile.type().equals( "beamsource" ) && tile.strength() == 0 )
				) {
					return false;
				}
			}
		}
		
		return true;
	}
}
