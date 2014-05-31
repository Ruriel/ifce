package View;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;

/**
 * Painel que ser� associado a um Acumulador.
 * @author Ruriel
 *
 */
@SuppressWarnings("serial")
public class JPanelAcumulador extends JPanel{

	private JLabel lblPrioridade;
	private JLabel lblContador;
	private JTextField jtfContador;
	private JTextField jtfPrioridade;
	private JButton btnReset;
	private JButton btnAumentar;
    private JButton btnDiminuir;
	
    /**
     * Adiciona a a��o ao evento do bot�o "Reset".
     * @param reset A��o.
     */
    public void addResetListener(ActionListener reset)
	{
		btnReset.addActionListener(reset);
	}

    /**
     * Adiciona a a��o ao evento do bot�o "+".
     * @param up A��o.
     */
	public void addAumentarListener(ActionListener up)
	{
		btnAumentar.addActionListener(up);
	}

	/**
     * Adiciona a a��o ao evento do bot�o "-".
     * @param down A��o.
     */
	public void addDiminuirListener(ActionListener down)
	{
		btnDiminuir.addActionListener(down);
	}
	
	/**
	 * Atualiza o campo do contador.
	 * @param x N�mero a ser colocado no campo.
	 */
	public void setTextContador(int x)
    {
    	jtfContador.setText(""+x);
    }
	
	/**
	 * Atualiza o campo da prioridade.
	 * @param x N�mero a ser colocado no campo.
	 */
	public void setTextPrioridade(int x)
	{
		jtfPrioridade.setText(""+x);
	}
    
	/**
	 * Seta a prioridade do campo. Se for igual a MAX_PRIORITY, desabilita o bot�o "+".
	 * Caso o bot�o "-" esteja desabilitado, habilita-o.
	 * @param prioridade A ser colocada no campo.
	 */
	public void aumentarPrioridade(int prioridade)
	{
		jtfPrioridade.setText(prioridade+"");
		if(prioridade == Thread.MAX_PRIORITY)
			btnAumentar.setEnabled(false);
		if(!btnDiminuir.isEnabled())
			btnDiminuir.setEnabled(true);
	}
	
	/**
	 * Seta a prioridade do campo. Se for igual a MIN_PRIORITY, desabilita o bot�o "-".
	 * Caso o bot�o "+" esteja desabilitado, habilita-o.
	 * @param prioridade A ser colocada no campo.
	 */
	public void diminuirPrioridade(int prioridade)
	{
		jtfPrioridade.setText(prioridade+"");
		if(prioridade == Thread.MIN_PRIORITY)
			btnDiminuir.setEnabled(false);
		if(!btnAumentar.isEnabled())
			btnAumentar.setEnabled(true);
	}
	
	/**
	 * Habilita todos os bot�es do painel.
	 */
	public void enableButtons()
	{
		btnDiminuir.setEnabled(true);
		btnAumentar.setEnabled(true);
		btnReset.setEnabled(true);
	}
	
	/**
	 * Desabilita todos os bot�es do painel.
	 */
	public void disableButtons()
	{
		btnDiminuir.setEnabled(false);
		btnAumentar.setEnabled(false);
		btnReset.setEnabled(false);
	}
	
	/**
	 * Atualiza o campo do contador.
	 * @param contador N�mero a ser colocado no campo.
	 */
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
