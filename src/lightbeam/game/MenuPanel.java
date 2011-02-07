package lightbeam.game;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

public class MenuPanel extends JPanel 
{
	private static final long serialVersionUID 	= 4L;
	
	private PlayGameWindow window	= null;
	private JButton btnOpen			= new JButton();
	
	public MenuPanel( PlayGameWindow kWindow )
	{
		this.window	= kWindow;
		this.setLayout( null );
		
		this.btnOpen.setIcon( new ImageIcon( "./src/fx/Lightbeam/gamepanel/btnOpen.png" ) );
		this.btnOpen.setCursor( new Cursor( Cursor.HAND_CURSOR ) );
		this.btnOpen.setBounds( 10, 10, 50, 50 );
		
		this.add( this.btnOpen );
		
		this.btnOpen.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				MenuPanel.this.window.openGame();
			}
		});

	}	
}
