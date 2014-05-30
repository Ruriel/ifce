import javax.swing.JFrame;

import Controller.ControllerFrame;
import View.MainFrame;


public class Main 
{

	public static void main(String[] args) 
	{
		MainFrame view = new MainFrame();
		@SuppressWarnings("unused")
		ControllerFrame controlFrame = new ControllerFrame(view);
		view.setVisible(true);
		view.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}