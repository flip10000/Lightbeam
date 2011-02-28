package lightbeam.solution;

import core.tilestate.TileArray;

public interface ILogicStrategy
{
	public void execute();
	
	public void setMap( TileArray map );
	public TileArray getMap();
	
	public boolean getResult();
}
