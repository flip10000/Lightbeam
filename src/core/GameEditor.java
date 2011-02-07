package core;

import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class GameEditor extends GameObjects implements IGameEditor 
{
	protected JFrame frame						= new JFrame();

	public GameEditor()
	{
		// Fenster schliessen
		GameEditor.this.frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
//		this.frame.addWindowListener(new java.awt.event.WindowAdapter() {
//		    public void windowClosing(WindowEvent winEvt) {}
//		});	
	}

	public void closeEditor()
	{		
		GameEditor.this.frame.setVisible( false );
		GameEditor.this.game.closeEditor();
	}
	
	public void showEditor() 
	{
		this.frame.setVisible( true );
	}
}
