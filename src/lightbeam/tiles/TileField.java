package lightbeam.tiles;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import core.tilefactory.AbstractTileSet;
import custom.objects.ImgResize;



public class TileField extends AbstractTileSet 
{
	public TileField() throws IOException	
	{
		super( "field", ImgResize.resize( ImageIO.read( new File( "./src/fx/Tiles/tile_field.png" ) ), 32, 32 ) );
	}
}
