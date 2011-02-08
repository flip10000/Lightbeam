package lightbeam.playground;

import java.io.IOException;

import lightbeam.tiles.TileBeam;
import lightbeam.tiles.TileField;
import lightbeam.tiles.TileBeamsource;

import core.tilefactory.AbstractTileSetFactory;

public class Tileset extends AbstractTileSetFactory
{
	public Tileset() throws IOException {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void createTiles() throws IOException 
	{
		this.tileset.add( new TileBeamsource() );
		this.tileset.add( new TileField() );
		this.tileset.add( new TileBeam() );
	}
}
