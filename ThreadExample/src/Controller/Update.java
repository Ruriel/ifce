package Controller;

import Model.Acumulador;
import View.JPanelAcumulador;

public class Update extends Thread
{
	private JPanelAcumulador jp;
	private Acumulador ac;
	private boolean running = true;
	
	public Update(JPanelAcumulador jp, Acumulador ac)
	{
		this.jp = jp;
		this.ac = ac;
	}
	
	public void parar()
	{
		running = false;
	}
	
	@Override
	public void run()
	{
		long timeThen;
		while(running)
		{
			timeThen = System.currentTimeMillis();
			while(System.currentTimeMillis() - timeThen < 1000){}
			ac.setContador(ac.getContador()+1);
			jp.refresh(ac.getContador());
		}
	}
}
