package core;

import javax.swing.ImageIcon;

import core.tilefactory.AbstractTileSetFactory;

public interface IGameStrategy 
{
	public void execute();
	
	public String getName();
	public ImageIcon getIcon();
	
	public void setEditor( IGameEditor editor );
	public void setEditorTileset( AbstractTileSetFactory editorTileset );
	public void setGame( IGameStrategy game );
	
	public void openEditor();
	public void closeEditor();
}
