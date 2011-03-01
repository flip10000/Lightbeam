package lightbeam.solution.strategies;

import java.util.ArrayList;

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
		// TODO Auto-generated method stub
		
	}

	public TileArray getMap() 			{ return this.map; 		}
	public boolean getResult() 			{ return this.result; 	}

	public void setMap( TileArray map )	{ this.map = map;		}
}
