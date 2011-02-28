package lightbeam.solution;

import core.tilestate.TileArray;

public class LogicContext 
{
	private ILogicStrategy logicStrategy	= null;
	private TileArray map					= null;
	
	public LogicContext( TileArray map ) { this.map = map; }
	
	public void setLogic( ILogicStrategy logicStrategy )	
	{ 
		this.logicStrategy = logicStrategy;
		this.logicStrategy.setMap( this.map );
	}
	
	public void setMap( TileArray map ) 
	{ 
		this.map	= map;
		this.logicStrategy.setMap( map );
	}
	
	public ILogicStrategy getLogic()			{ return this.logicStrategy;	}
	
	public void executeLogic()	{ this.logicStrategy.execute();	}
	
	public boolean getResult() 	{ return this.logicStrategy.getResult();	}
	public TileArray getMap() 	{ return this.logicStrategy.getMap();		}
}
