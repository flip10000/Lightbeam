package lightbeam.solution.strategies;

import java.util.ArrayList;

import core.tilestate.Tile;
import core.tilestate.TileArray;
import lightbeam.solution.ILogicStrategy;
import lightbeam.solution.LogicMethods;

public class ForcedToBeam extends LogicMethods implements ILogicStrategy 
{
	private TileArray map						= null;
	private ArrayList<int[][]> fieldToSource	= null;
	private int[][] fields						= null;

	public void execute() 
	{
		super.setResult( false );
		this.map	= super.getMap();
		
		// Alle Beamsources in der map holen:
		ArrayList<Tile> beamsources	= this.map.getTilesInArea( "beamsource" );
		
		// Container-Var f�r Position der blockierenden Beamsources:
		// blockings[0]	= Source von links,
		// blockings[1]	= Source von oben,
		// blockings[2]	= Source von rechts,
		// blockings[3]	= Source von unten,
		boolean[] blockings	= new boolean[4];

		// Container-Var f�r die zu verteilenden Beams:
		int[] beams			= null;
		
		// Wenn vorhanden:
		if( beamsources != null )
		{
			int sSources			= beamsources.size();
			beams					= new int[sSources];
			
			// Alle Beamsources durchlaufen:
			for( int i = 0; i < sSources; i++ )
			{
				Tile source		= beamsources.get(i);
				int strength	= source.strength();
				int sRow		= source.row();
				int sCol		= source.col();
				
				// Wenn Beamsource-St�rke > 0:
				if( strength > 0 )
				{
					int lDiff	= strength;
					int tDiff	= strength;
					int rDiff	= strength;
					int bDiff	= strength;
					
					blockings[0]	= false;
					blockings[1]	= false;
					blockings[2]	= false;
					blockings[3]	= false;
					
					Tile aBeam		= null;
					
					// Beamsource links von akuteller Beamsource finden 
					// und St�rke holen, falls vorhanden:
					Tile lSource	= this.map.getLeftBlocking( source, "beamsource" );
					aBeam			= this.map.getLeftBlocking( source, "beam" );
					if( aBeam != null && lSource != null && aBeam.col() > lSource.col() )
					{
						lSource	= aBeam;
					}
					
					// Beamsource oberhalb von akuteller Beamsource finden 
					// und St�rke holen, falls vorhanden:
					Tile tSource	= this.map.getTopBlocking( source, "beamsource" );
					aBeam			= this.map.getTopBlocking( source, "beam" );
					if( aBeam != null && tSource != null && aBeam.row() > tSource.row() )
					{
						tSource	= aBeam;
					}
					
					// Beamsource rechts von akuteller Beamsource finden 
					// und St�rke holen, falls vorhanden:
					Tile rSource	= this.map.getRightBlocking( source, "beamsource" );
					aBeam			= this.map.getRightBlocking( source, "beam" );
					if( aBeam != null && rSource != null && aBeam.col() < rSource.col() )
					{
						rSource	= aBeam;
					}

					// Beamsource unterhalb von akuteller Beamsource finden 
					// und St�rke holen, falls vorhanden:
					Tile bSource	= this.map.getBottomBlocking( source, "beamsource" );
					aBeam			= this.map.getBottomBlocking( source, "beam" );
					if( aBeam != null && bSource != null && aBeam.row() < bSource.col() )
					{
						bSource	= aBeam;
					}
					
					// Pr�fen, ob Beamsource in Umgebung existiert und
					// einen Beam den aktuellen Beamsource blockiert:
					if( lSource != null || tSource != null || rSource != null || bSource != null )
					{
						// Differenzen init:
						lDiff	= ( lSource != null )? source.col() - lSource.col() - 1: strength;
						tDiff	= ( tSource != null )? source.row() - tSource.row() - 1: strength;
						rDiff	= ( rSource != null )? rSource.col() - source.col() - 1: strength;
						bDiff	= ( bSource != null )? bSource.row() - source.row() - 1: strength;

						// Mit links anfangen, falls Beamsource existiert
						// und pr�fen, ob St�rke der akt. Beamsource gr��er als Differenz zw.
						// Beamsources:
						if( strength > lDiff && lSource != null )
						{
							// Links blockiert:
							blockings[0]	= true;
							
							// Zu verteilende Beams zuweisen:
							beams[i]		+= lDiff;
							
							// St�rke der akt. Beamsource runtersezten
							strength		-= ( lDiff == 0 )? 1 : lDiff; 
						}
	
						// Weiter mit oben, falls Beamsource existiert
						// und pr�fen, ob St�rke der akt. Beamsource gr��er als Differenz zw.
						// Beamsources:
						if( strength > tDiff && tSource != null )
						{
							// Oben blockiert:
							blockings[1]	= true;
							
							// Zu verteilende Beams zuweisen:
							beams[i]		+= tDiff;
							
							// St�rke der akt. Beamsource runtersezten
							strength		-= ( tDiff == 0 )? 1 : tDiff ;
						}

						// Weiter mit rechts, falls Beamsource existiert
						// und pr�fen, ob St�rke der akt. Beamsource gr��er als Differenz zw.
						// Beamsources:
						if( strength > rDiff && rSource != null )
						{
							// Rechts blockiert:
							blockings[2]	= true;
							
							// Zu verteilende Beams zuweisen:
							beams[i]		+= rDiff;
							
							// St�rke der akt. Beamsource runtersezten
							strength		-= ( rDiff == 0 )? 1 : rDiff; 
						}

						// Weiter mit unten, falls Beamsource existiert
						// und pr�fen, ob St�rke der akt. Beamsource gr��er als Differenz zw.
						// Beamsources:
						if( strength > bDiff && bSource != null )
						{
							// Unten blockiert:
							blockings[3]	= true;
							
							// Zu verteilende Beams zuweisen:
							beams[i]		+= bDiff;
							
							// St�rke der akt. Beamsource runtersezten
							strength		-= ( bDiff == 0 )? 1 : bDiff; 
						}
					}

					// Wenn zu verteilende Elemente, dann auf m�gliche Felder verteilen, falls noch nicht erfolgt:
					// Kriterium f�r zu verteilende Beams: gespeicherte St�rke > neu kalkulierte St�rke
					int chkStrength	= super.getMap().tile( sRow, sCol ).strength();

					while( chkStrength > strength )
					{
						this.map	= super.getMap();
						
						// Falls links vom Beamsource frei, Beams dort verteilen, soweit es geht:
						if( blockings[0] == false ) 
						{
							// Pr�fen, ob Beam von links blockiert:
							Tile lBeam	= this.map.getLeftBlocking( source, "beam" );
							
							int tmpDiff	= ( lBeam == null || ( lSource != null && lBeam.col() < lSource.col() ) )? lDiff : sCol - lBeam.col() - 1;
							if( tmpDiff > chkStrength ) { tmpDiff = chkStrength; }
							int tmpCol	= ( sCol - tmpDiff > 0 )? sCol - tmpDiff : 0;
							if( sRow == 0 && sCol == 0 )
							{
								System.out.println(tmpCol);
								System.out.println(tmpDiff);
								System.out.println(blockings[2]);
								System.out.println(blockings[3]);
							}
							
							this.assignFieldsToSource( sRow, tmpCol, source );
							chkStrength	-= tmpDiff;
						} 
						// Falls oberhalb vom Beamsource frei, Beams dort verteilen, soweit es geht:
						else if( blockings[1] == false )
						{
							// Pr�fen, ob Beam von oben blockiert:
							Tile tBeam	= this.map.getTopBlocking( source, "beam" );
							int tmpDiff	= ( tBeam == null || ( tSource != null && tBeam.row() < tSource.row() ) )? tDiff : sRow - tBeam.row() - 1;
							if( tmpDiff > chkStrength ) { tmpDiff = chkStrength; }
							int tmpRow	= ( sRow - tmpDiff > 0 )? sRow - tmpDiff : 0;
							
							this.assignFieldsToSource( tmpRow, sCol, source );
							chkStrength	-= tmpDiff;
						}
						// Falls rechts vom Beamsource frei, Beams dort verteilen, soweit es geht:
						else if( blockings[2] == false )
						{
							// Pr�fen, ob Beam von rechts blockiert:
							Tile rBeam	= this.map.getRightBlocking( source, "beam" );
							int tmpDiff	= ( rBeam == null || ( rSource != null && rBeam.col() > rSource.col() ) )? rDiff : rBeam.col() - sCol - 1;
							if( tmpDiff > chkStrength ) { tmpDiff = chkStrength; }
							int tmpCol	= ( tmpDiff + sCol < this.map.cols() )? tmpDiff + sCol : this.map.cols();
							
							this.assignFieldsToSource( sRow, tmpCol, source );
							chkStrength	-= tmpDiff;
						}
						// Falls unterhalb vom Beamsource frei, Beams dort verteilen, soweit es geht:
						else if( blockings[3] == false )
						{
							// Pr�fen, ob Beam von unten blockiert:
							Tile bBeam	= this.map.getBottomBlocking( source, "beam" );
							int tmpDiff	= ( bBeam == null || ( bSource != null && bBeam.row() > bSource.row() ) )? bDiff : bBeam.row() - sRow - 1;
							if( tmpDiff > chkStrength ) { tmpDiff = chkStrength; }
							int tmpRow	= ( tmpDiff + sRow < this.map.rows() )? tmpDiff + sRow : this.map.rows();
							
							this.assignFieldsToSource( tmpRow, sCol , source );
							chkStrength	-= tmpDiff;
						}
					}
					
//					this.map	= super.getMap();
//					this.map.tile( sRow, sCol ).strength( strength );
//					super.setMap( this.map );
				} 
			}
			
			super.setMap( this.map );
		}
	}
}
