package service;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import models.Banco;
import models.Cliente;
import models.Operacao;
import models.TipoOperacao;
import models.conta.Conta;
import models.conta.ContaCorrente;
import models.conta.ContaPoupanca;

@Data
@AllArgsConstructor
public class BancoServico {

    private Banco banco;

    public BancoServico() {
        this.banco = new Banco("Banrisul");

        Cliente cliente1 = Cliente.builder().id(0).nome("Pedro").build();
        Cliente cliente2 = Cliente.builder().id(1).nome("João").build();

        this.banco.getContas().add(new ContaCorrente(cliente1));
        this.banco.getContas().add(new ContaPoupanca(cliente1));
        this.banco.getContas().add(new ContaCorrente(cliente2));
        this.banco.getContas().add(new ContaPoupanca(cliente2));
    }

    public Conta getConta(int id) {
        return this.banco.getContas().stream().filter(c -> c.getNumero() == id).toList().get(0);
    }

    public void addConta(Conta conta) {
        banco.getContas().add(conta);
    }

    public Cliente getCliente(int id) {
        return this.banco.getAllClientes().stream().filter(c -> c.getId() == id).toList().get(0);
    }

    public int nextClienteId() {
        return this.banco.proximoIdCliente();
    }

    public void registrarOperacao(TipoOperacao tipo, Conta conta, double valor) {
        this.banco.addOperacao(new Operacao(tipo, conta, valor));
    }

    public String getNomeBanco() {
        return this.banco.getNome();
    }

    public List<Cliente> allClinetes() {
        return this.banco.getAllClientes();
    }

    public List<Conta> allContas() {
        return this.banco.getContas();
    }

    public List<Operacao> allOperacaos() {
        return this.banco.getOperacoes();
    }

    public void tranferir(Conta origem, Conta destino, double valor) {
        if (origem.fundos(valor)) {
            origem.transferir(valor, destino);
            this.registrarOperacao(TipoOperacao.TRANFERENCIA, destino, valor);
            System.out.println("Transfência realizada com sucesso.");
        } else {
            System.out.println("A conta indicada não possui fundos para esta transfência.");
        }
    }

    public void saque(Conta origem, double valor) {
        if (origem.fundos(valor)) {
            origem.sacar(valor);
            this.registrarOperacao(TipoOperacao.SAQUE, origem, valor);
            System.out.println("Saque realizado com sucesso.");
        } else {
            System.out.println("A conta indicada não possui fundos para a operação.");
        }

    }

    public void depositar(Conta destino, double valor) {
        destino.depositar(valor);
        this.registrarOperacao(TipoOperacao.DEPOSITO, destino, valor);
        System.out.println("Depósito realizado com sucesso.");
    }

}
