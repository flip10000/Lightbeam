package lightbeam.solution;

import core.tilestate.Tile;
import core.tilestate.TileArray;

public class LogicMethods
{
	private TileArray map			= null;
	private boolean result			= false;
	
	public void setMap( TileArray map )			{ this.map = map;		}
	protected void setResult( boolean result )	{ this.result = result;	}
	
	public TileArray getMap()					{ return this.map; 		}	
	public boolean getResult() 					{ return this.result;	}	
	
	// Letztm�gliches "Feld" links von der Beamsource zur�ckgeben:
	protected int getTillLastLeftField( Tile source )
	{
		int strength					= source.strength();
		int sCol						= source.col();
		int leftLastPos					= -1;
		
		// Pr�fen, ob links von Beamsource weiteres Beamsource liegt:
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
		if( leftLastPos > -1 && leftLastPos != source.col() &&
			this.crossedBySource( source.row(), leftLastPos, source ) == false				
		) {
			return leftLastPos;
		}
		
		// "Nicht gefunden" zur�ckgeben:
		return -1;
	}
	
	// Letztm�gliches "Feld" oben von der Beamsource zur�ckgeben:
	protected int getTillLastTopField( Tile source )
	{
		int strength					= source.strength();
		int sRow						= source.row();
		int topLastPos					= -1;
		
		// Pr�fen, ob links von Beamsource weiteres Beamsource liegt:
		Tile topBlock	= this.map.getTopBlocking( source, "beamsource" );
		
		// Wenn nein, dann:
		if( topBlock == null )
		{
			topLastPos	= ( sRow - strength > -1 )? sRow - strength : 0; 	
		} 
		// Ansonsten:
		else 
		{
			topLastPos	= ( sRow - strength > topBlock.row() )? ( sRow - strength ) : topBlock.row() + 1;
		}
		
		// Wenn gefunden, dann Tile-Row-Positions speichern:
		if( topLastPos > -1 && topLastPos != source.row() &&
			this.crossedBySource( topLastPos, source.col(), source ) == false				
		) { 
			return topLastPos;
		}
		
		// "Nicht gefunden" zur�ckgeben:
		return -1;
	}
	
	// Letztm�gliches "Feld" rechts von der Beamsource zur�ckgeben:
	protected int getTillLastRightField( Tile source )
	{
		int strength					= source.strength();
		int sCol						= source.col();
		int cols						= this.map.cols();
		int rightLastPos				= -1;
		
		// Pr�fen, ob links von Beamsource weiteres Beamsource liegt:
		Tile rightBlock		= this.map.getRightBlocking( source, "beamsource" );

		// Wenn nein, dann:
		if( rightBlock == null )
		{
			rightLastPos	= ( sCol + strength < cols )? sCol + strength : cols - 1;
		} 
		// Ansonsten:
		else 
		{
			rightLastPos	= ( sCol + strength < rightBlock.col() )? ( sCol + strength ) : rightBlock.col() - 1;
		}
		
		// Wenn gefunden, dann Tile-Col-Posiiton zur�ckgeben:
		if( rightLastPos > -1 && rightLastPos != source.col() && 
			this.crossedBySource( source.row(), rightLastPos, source ) == false				
		) {
			return rightLastPos;
		}
		
		// "Nicht gefunden" zur�ckgeben:
		return -1;
	}
	
	// Letztm�gliches "Feld" unten von der Beamsource zur�ckgeben:
	protected int getTillLastBottomField( Tile source )
	{
		int strength					= source.strength();
		int sRow						= source.row();
		int rows						= this.map.rows();
		int bottomLastPos				= -1;
		
		// Pr�fen, ob unterhalb vom Beamsource weiteres Beamsource liegt:
		Tile bottomBlock	= this.map.getBottomBlocking( source, "beamsource" );
		
		// Wenn nein, dann:
		if( bottomBlock == null )
		{
			bottomLastPos	= ( sRow + strength < rows )? sRow + strength : rows - 1;
		} 
		// Ansonsten:
		else 
		{
			bottomLastPos	= ( sRow + strength < bottomBlock.row() )? ( sRow + strength ) : bottomBlock.row() - 1;
		}
		
		// Wenn gefunden, dann Tile-Row-Position speichern:
		if( bottomLastPos > -1 && bottomLastPos != source.row() &&  
			this.crossedBySource( bottomLastPos, source.col(), source ) == false				
		) {
			return bottomLastPos;
		}
		
		// "Nicht gefunden" zur�ckgeben:
		return -1;
	}
	
	protected void assignFieldsToSource( int fRow, int fCol, Tile source )
	{
		int sRow	= source.row();
		int sCol	= source.col();
		
		// Pr�fen, ob Felder auf horizontaler Ebene zugeordnet werden sollen:
		if( sRow == fRow )
		{
			// Pr�fen, ob Felder links von der Beamsource zugeordnet werden sollen:
			if( fCol < sCol )
			{
				for( int col = fCol; col < sCol; col++ )
				{
					this.map.tile( sRow, col ).parent( source );
					this.map.tile( sRow, col).type( "beam" );
					this.map.tile( sRow, col ).setDirection( Tile.HORIZONTAL );
				}
				
				// Letzes Beam-Feld als Endst�ck markieren:
				this.map.tile( sRow, fCol ).isEnd( true );
				
				// Beamstrength f�r den n�chsten Durchlauf vermindern:
				int calcStrength	= this.map.tile( sRow, sCol ).strength() - ( sCol - fCol );
				int newStrength		= ( calcStrength > 0 )? calcStrength : 0;

				this.map.tile( sRow, sCol ).strength( newStrength );
			}
			// Nein, Felder sollen rechts von der Beamsource zugeordnet werden:
			else
			{
				for( int col = fCol; col > sCol; col-- )
				{
					this.map.tile( sRow, col ).parent( source );
					this.map.tile( sRow, col).type( "beam" );
					this.map.tile( sRow, col ).setDirection( Tile.HORIZONTAL );
				}
				
				// Letzes Beam-Feld als Endst�ck markieren:
				this.map.tile( sRow, fCol ).isEnd( true );

				// Beamstrength f�r den n�chsten Durchlauf vermindern:
				int calcStrength	= this.map.tile( sRow, sCol ).strength() - ( fCol - sCol );
				int newStrength		= ( calcStrength > 0 )? calcStrength : 0;
				
				this.map.tile( sRow, sCol ).strength( newStrength );
			}
		}
		// Nein, vertikale Zuordnung:
		else
		{
			// Pr�fen, ob Felder oberhalb von der Beamsource zugeordnet werden sollen:
			if( fRow < sRow )
			{
				for( int row = fRow; row < sRow; row++ )
				{
					this.map.tile( row, sCol ).parent( source );
					this.map.tile( row, sCol).type( "beam" );
					this.map.tile( row, sCol ).setDirection( Tile.VERTICAL );
				}
				
				// Letzes Beam-Feld als Endst�ck markieren:
				this.map.tile( fRow, sCol ).isEnd( true );
				
				// Beamstrength f�r den n�chsten Durchlauf vermindern:
				int calcStrength	= this.map.tile( sRow, sCol ).strength() - ( sRow - fRow );
				int newStrength		= ( calcStrength > 0 )? calcStrength : 0;
				
				this.map.tile( sRow, sCol ).strength( newStrength );
			}
			// Nein, Felder sollen unterhalb von der Beamsource zugeordnet werden:
			else
			{
				for( int row = fRow; row > sRow; row-- )
				{
					this.map.tile( row, sCol ).parent( source );
					this.map.tile( row, sCol).type( "beam" );
					this.map.tile( row, sCol ).setDirection( Tile.VERTICAL );
				}
				
				// Letzes Beam-Feld als Endst�ck markieren:
				this.map.tile( fRow, sCol ).isEnd( true );
				
				// Beamstrength f�r den n�chsten Durchlauf vermindern:	
				int calcStrength	= this.map.tile( sRow, sCol ).strength() - ( fRow  - sRow );
				int newStrength		= ( calcStrength > 0 )? calcStrength : 0;
				
				this.map.tile( sRow, sCol ).strength( newStrength );
			}
		}
	}
	
	protected boolean isField( int row, int col )
	{
		return ( this.map.tile( row, col ).type().equals( "field" ) )? true : false;
	}
	
	protected boolean crossedBySource( int row, int col, Tile exceptSource )
	{
		/* Beamsource von links */ 
		// Beamsource holen:
		Tile lSource	= this.map.getLeftBlocking( this.map.tile( row, col ), "beamsource" );
		// St�rke holen, falls vorhanden:
		int lStrength	= ( lSource != null )? lSource.strength() : 0;
		// Pr�fen, ob Feld kreuzt, falls vorhanden und nicht dieselbe wie "exceptSource" und die St�rke nicht ausreicht um zu kreuzen:
		boolean lCross	= ( lSource != null && !lSource.equals( exceptSource ) && ( lSource.col() + lStrength ) >= col )? true : false;
		/* Beamsource von links - ENDE */
		
		// Rechts vorhandene Beamsource:
		Tile rSource	= this.map.getRightBlocking( this.map.tile( row, col ), "beamsource" );
		int rStrength	= ( rSource != null )? rSource.strength() : 0;
		boolean rCross	= ( rSource != null && !rSource.equals( exceptSource ) && ( rSource.col() - rStrength ) <= col )? true : false;

		// Oberhalb vorhandene Beamsource:
		Tile tSource	= this.map.getTopBlocking( this.map.tile( row, col ), "beamsource" );
		int tStrength	= ( tSource != null )? tSource.strength() : 0;
		boolean tCross	= ( tSource != null && !tSource.equals( exceptSource ) && ( tSource.row() + tStrength ) >= row )? true : false;
		
		// Unterhalb vorhandene Beamsource:
		Tile bSource	= this.map.getBottomBlocking( this.map.tile( row, col ), "beamsource" );
		int bStrength	= ( bSource != null )? bSource.strength() : 0;
		boolean bCross	= ( bSource != null && !bSource.equals( exceptSource ) && ( bSource.row() - bStrength ) <= row )? true : false;
		
		return !( !lCross && !tCross && !rCross && !bCross );
	}
}
