package lightbeam.solution.strategies;

import java.util.ArrayList;
import lightbeam.solution.ILogicStrategy;
import lightbeam.solution.LogicMethods;
import core.tilestate.Tile;
import core.tilestate.TileArray;

public class ConclusiveReachable extends LogicMethods implements ILogicStrategy
{
	private TileArray map						= null;
	private ArrayList<int[][]> fieldToSource	= null;
	private int[][] fields						= null;
	
	public ConclusiveReachable() { super.setResult( true ); }

	public void execute() 
	{
		super.setResult( false );
		this.map	= super.getMap();
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
					
					// Alle möglichen Feld-Positionen (nur Zeilen) oberhalb der Source
					// speichern:
					pos[1][0]		= this.getTillLastTopField( source );
					pos[1][1]		= sCol;
					
					// Alle möglichen Feld-Positionen (nur Spalten) rechts der Source
					// speichern:
					pos[2][0]		= sRow;
					pos[2][1]		= this.getTillLastRightField( source );
					
					// Alle möglichen Feld-Positionen (nur Zeilen) unterhalb der Source
					// speichern:
					pos[3][0]		= this.getTillLastBottomField( source );
					pos[3][1]		= sCol;
					
					// Mögliche eindeutige Felder der Beamsource zuordnen:
					this.fieldToSource.add( i, pos );

					// Häufigkeit der Feldbenutzung bzgl. der möglichen Beamsources hochzählen, falls
					// letzmögliche Felder noch nicht zugeordnet:
					if( pos[0][1] > -1 && this.isField( sRow, pos[0][1] ) ) { this.fields[sRow][pos[0][1]]++; }
					if( pos[1][0] > -1 && this.isField( pos[1][0], sCol ) ) { this.fields[pos[1][0]][sCol]++; }
					if( pos[2][1] > -1 && this.isField( sRow, pos[2][1] ) ) { this.fields[sRow][pos[2][1]]++; }
					if( pos[3][0] > -1 && this.isField( pos[3][0], sCol ) ) { this.fields[pos[3][0]][sCol]++; }
				} else
				{
					this.fieldToSource.add( i, null );
				}
			}
			
			// Alle Beamsources durchlaufen:
			for( int i = 0; i < sSources; i++ )
			{
				// Beamsource holen:
				Tile source			= beamsources.get(i);
				
				if( source.strength() > 0 )
				{
					// Alle letztmöglichen Felder einer Beamsource holen:
					int[][] fieldsPos	= this.fieldToSource.get( i );
					
					if( fieldsPos != null )
					{
						// Prüfen, ob letztmögliches Feld von links der Beamsource eindeutig
						// zuordenbar ist. Dabei ist:
						// [0][0] = row,
						// [0][1] = col
						if( fieldsPos[0][0] > -1 && fieldsPos[0][1] > -1 
							&& this.fields[fieldsPos[0][0]][fieldsPos[0][1]] == 1 
						) {
							this.assignFieldsToSource( fieldsPos[0][0], fieldsPos[0][1], source );
		
							if( super.getResult() == false ) { super.setResult( true ); }
						}
						
						// Prüfen, ob letztmögliches Feld oberhalb der Beamsource eindeutig
						// zuordenbar ist. Dabei ist:
						// [1][0] = row,
						// [1][1] = col
						if( fieldsPos[1][0] > -1 && fieldsPos[1][1] > -1 && 
							this.fields[fieldsPos[1][0]][fieldsPos[1][1]] == 1 
						) {
							this.assignFieldsToSource( fieldsPos[1][0], fieldsPos[1][1], source );
							
							if( super.getResult() == false ) { super.setResult( true ); }
						}
						
						// Prüfen, ob letztmögliches Feld rechts von der Beamsource eindeutig
						// zuordenbar ist. Dabei ist:
						// [2][0] = row,
						// [2][1] = col
						if( fieldsPos[2][0] > -1 && fieldsPos[2][1] > -1 &&
							this.fields[fieldsPos[2][0]][fieldsPos[2][1]] == 1 
						) {
							this.assignFieldsToSource( fieldsPos[2][0], fieldsPos[2][1], source );
							
							if( super.getResult() == false ) { super.setResult( true ); }
						}
						
						// Prüfen, ob letztmögliches Feld unterhalb der Beamsource eindeutig
						// zuordenbar ist. Dabei ist:
						// [3][0] = row,
						// [3][1] = col
						if( fieldsPos[3][0] > -1 && fieldsPos[3][1] > -1 &&
							this.fields[fieldsPos[3][0]][fieldsPos[3][1]] == 1 
						) {
							this.assignFieldsToSource( fieldsPos[3][0], fieldsPos[3][1], source );
							
							if( super.getResult() == false ) { super.setResult( true ); }
						}
					}
				}
			}
			
			super.setMap( this.map );
		} 
	}
}
