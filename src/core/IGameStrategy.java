package core;

import javax.swing.ImageIcon;

import core.tilefactory.AbstractTileSetFactory;

public interface IGameStrategy 
{
	public void execute();
	
	public String getName();
	public ImageIcon getIcon();
	
	public void setEditor( IGameEditor editor );
	public void setPlayground( IGamePlayground playground );
	public void setEditorTileset( AbstractTileSetFactory editorTileset );
	public void setPGTileset( AbstractTileSetFactory playgroundTileset );
	public void setGame( IGameStrategy game );

	public void openGameGUI();	
	public void closeGameGUI();
	public void exitGameGUI();
	
	public void openEditor();
	public void closeEditor();
	
	public void openPlayground();
	public void closePlayground();
	
}
