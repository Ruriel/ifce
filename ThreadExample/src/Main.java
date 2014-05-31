import javax.swing.JFrame;

import Controller.ControllerFrame;
import View.MainFrame;

/**
 * Classe que contém o método main.
 * @author Ruriel
 *
 */
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