package core;

import javax.swing.ImageIcon;

import core.tilefactory.AbstractTileSetFactory;



public abstract class Game implements IGameStrategy
{
	protected static AbstractTileSetFactory editorTileset	= null;
	protected static IGameStrategy game						= null;
	protected IGameEditor editor							= null;
	
	
	public Game() 				{				}
	public void execute() 		{				}
	public ImageIcon getIcon() 	{ return null; 	}
	public String getName() 	{ return null; 	}

	public void setGame( IGameStrategy game  )
	{
		this.game	= game; 
	}
	
	public void setEditorTileset( AbstractTileSetFactory editorTileset )
	{
		Game.editorTileset = editorTileset;
	}

	public void setEditor( IGameEditor editor )	
	{ 
		this.editor = editor;
	}
	
	public void openEditor()
	{
		this.editor.showEditor();
	}
	
	public void closeEditor() {}

	public static IGameStrategy getGame() { return Game.game; }
	
	public static AbstractTileSetFactory getEditorTileset() { return Game.editorTileset; }
}
