package lightbeam.editor;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class SettingsDialog 
{
	private JOptionPane pane			= null;
	private JPanel panel				= new JPanel();
	private JLabel lblPath				= new JLabel( "Speicherort:" );
	private JTextField inpPath			= new JTextField();
	private JButton btnPath				= new JButton( "..." );
	private JFileChooser fc				= new JFileChooser();
	
	public SettingsDialog()	
	{
		this.panel.setLayout( null );
		this.panel.setPreferredSize( new Dimension( 400, 200 ) );
		
		this.lblPath.setBounds( new Rectangle( 10, 10, 70, 20 ) );
		this.inpPath.setBounds( new Rectangle( 95, 10, 270, 20 ) );
		this.btnPath.setBounds( new Rectangle( 370, 10, 20, 20 ) );
		
		this.panel.add( this.lblPath );
		this.panel.add( this.inpPath );
		this.panel.add( this.btnPath );
		
		this.fc.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY );
        
        this.pane 		= new JOptionPane( this.panel, JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION );
        
		this.btnPath.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				SettingsDialog.this.fc.showOpenDialog( SettingsDialog.this.pane );
				
				String path	= SettingsDialog.this.fc.getSelectedFile() + "";

				SettingsDialog.this.inpPath.setText( ( !path.equals( "null"  ) )? path : "" );
			}
		});

	}
	
	public void showDialog()
	{
        this.pane.createDialog( null, "Titelmusik" ).setVisible( true );
	}
}
