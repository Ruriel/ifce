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
		setPriority(ac.getPrioridade());
	}
	
	public void parar()
	{
		running = false;
	}
	
	@Override
	public void run()
	{
		long timeThen;
		int contador = ac.getContador();
		int prioridade = getPriority();
		while(running)
		{
			timeThen = System.currentTimeMillis();
			while(System.currentTimeMillis() - timeThen < 1000){}
			ac.setContador(contador+1);
			contador = ac.getContador();
			jp.refresh(contador);
			if(prioridade != ac.getPrioridade())
			{
				setPriority(ac.getPrioridade());
				prioridade = getPriority();
			}
		}
	}
}
