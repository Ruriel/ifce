package Controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Model.Acumulador;
import View.JPanelAcumulador;

public class ControllerPanel
{
	private JPanelAcumulador jp;
	private Update up = null;
	private Acumulador ac;
	
	public ControllerPanel(JPanelAcumulador panel)
	{
		jp = panel;
		jp.addAumentarListener(new UpListener());
		jp.addDiminuirListener(new DownListener());
		jp.addResetListener(new ResetListener());
		ac = new Acumulador();
	}
	
	public void iniciar()
	{
		if(up == null)
			up = new Update(jp, ac);
		up.start();
	}
	
	public void parar()
	{
		up.parar();
		up = null;
	}
	
	public class UpListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e)
		{
			jp.aumentarPrioridade(ac.getPrioridade()+1);
			ac.setPrioridade(ac.getPrioridade()+1);
		}
	}
	
	public class DownListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e) 
		{
			jp.diminuirPrioridade(ac.getPrioridade()-1);
			ac.setPrioridade(ac.getPrioridade()-1);
		}
		
	}
	
	public class ResetListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e) 
		{
			ac.setPrioridade(Thread.NORM_PRIORITY);
			ac.setContador(0);
		}
		
	}
}
