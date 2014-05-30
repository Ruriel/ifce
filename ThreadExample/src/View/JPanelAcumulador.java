package View;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;

@SuppressWarnings("serial")
public class JPanelAcumulador extends JPanel{

	private JLabel lblPrioridade;
	private JLabel lblContador;
	private JTextField jtfContador;
	private JTextField jtfPrioridade;
	private JButton btnReset;
	private JButton btnAumentar;
    private JButton btnDiminuir;
	
	public JTextField getJtfContador() {
		return jtfContador;
	}


	public JTextField getJtfPrioridade() {
		return jtfPrioridade;
	}

	public void addResetListener(ActionListener reset)
	{
		btnReset.addActionListener(reset);
	}

	public void addAumentarListener(ActionListener up)
	{
		btnAumentar.addActionListener(up);
	}

	public void addDiminuirListener(ActionListener down)
	{
		btnDiminuir.addActionListener(down);
	}
	
	public void setTextContador(int x)
    {
    	jtfContador.setText(""+x);
    }
	
	public void setTextPrioridade(int x)
	{
		jtfPrioridade.setText(""+x);
	}
    
	public void aumentarPrioridade(int prioridade)
	{
		jtfPrioridade.setText(prioridade+"");
		if(prioridade == Thread.MAX_PRIORITY)
			btnAumentar.setEnabled(false);
		if(!btnDiminuir.isEnabled())
			btnDiminuir.setEnabled(true);
	}
	
	public void diminuirPrioridade(int prioridade)
	{
		jtfPrioridade.setText(prioridade+"");
		if(prioridade == Thread.MIN_PRIORITY)
			btnDiminuir.setEnabled(false);
		if(!btnAumentar.isEnabled())
			btnAumentar.setEnabled(true);
	}
	
	public void enableButtons()
	{
		btnDiminuir.setEnabled(true);
		btnAumentar.setEnabled(true);
		btnReset.setEnabled(true);
	}
	
	public void disableButtons()
	{
		btnDiminuir.setEnabled(false);
		btnAumentar.setEnabled(false);
		btnReset.setEnabled(false);
	}
    

	public void refresh(int contador)
	{
		jtfContador.setText(contador+"");
	}
	
	/**
	 * Create the panel.
	 */
	public JPanelAcumulador() {
		setLayout(null);
		
		jtfContador = new JTextField();
		jtfContador.setBounds(40, 32, 70, 25);
		jtfContador.setEditable(false);
		add(jtfContador);
		
		lblContador = new JLabel("Contador");
		lblContador.setBounds(40, 11, 70, 15);
		add(lblContador);
		
		jtfPrioridade = new JTextField();
		jtfPrioridade.setBounds(40, 80, 70, 25);
		jtfPrioridade.setEditable(false);
		jtfPrioridade.setText(Thread.NORM_PRIORITY+"");
		add(jtfPrioridade);
		
		lblPrioridade = new JLabel("Prioridade");
		lblPrioridade.setBounds(40, 57, 87, 15);
		add(lblPrioridade);
		
		btnReset = new JButton("Reset");
		btnReset.setBounds(25, 125, 117, 25);
		btnReset.setEnabled(false);
		add(btnReset);
		
		btnAumentar = new JButton("+");
		btnAumentar.setBounds(116, 80, 44, 12);
		btnAumentar.setEnabled(false);
		add(btnAumentar);

		btnDiminuir = new JButton("-");
		btnDiminuir.setBounds(116, 92, 44, 12);
		btnDiminuir.setEnabled(false);
		add(btnDiminuir);
		
	}
}
