package models;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import models.conta.Conta;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Banco {

	private String nome;
	private List<Conta> contas;
	private List<Operacao> operacoes;

	public Banco(String nome) {
		this.nome = nome;
		this.contas = new ArrayList<Conta>();
		this.operacoes = new ArrayList<Operacao>();
	}

	public List<Cliente> getAllClientes() {
		return this.contas.stream().map(conta -> conta.getCliente()).distinct().toList();
	}

	public int proximoIdCliente() {
		return this.getAllClientes().size();
	}

	public void addOperacao(Operacao operacao) {
		this.operacoes.add(operacao);
	}

}
