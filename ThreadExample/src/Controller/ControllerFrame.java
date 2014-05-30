package Controller;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import View.MainFrame;


public class ControllerFrame
{
	private MainFrame view;
	public ControllerFrame(MainFrame view)
	{
		this.view = view;
		view.addIniciarListener(new StartListener());
		view.addPararListener(new StopListener());
	}
	
	
	public class StartListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e) 
		{
			view.startPanels();
			view.enableParar();
			view.disableIniciar();
		}
		
	}
	
	public class StopListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent arg0) 
		{
			view.stopPanels();
			view.disableParar();
			view.enableIniciar();
		}
		
	}
}
