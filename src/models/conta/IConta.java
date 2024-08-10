package models.conta;

public interface IConta {
	
	void sacar(double valor);
	
	void depositar(double valor);
	
	void transferir(double valor, IConta contaDestino);

	boolean fundos(double valor);
	
	void imprimirExtrato();
}
