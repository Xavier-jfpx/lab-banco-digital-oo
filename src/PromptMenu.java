import java.util.Scanner;

import lombok.AllArgsConstructor;
import lombok.Data;
import models.Cliente;
import models.conta.Conta;
import models.conta.ContaCorrente;
import models.conta.ContaPoupanca;
import service.BancoServico;

@Data
@AllArgsConstructor

public class PromptMenu {
    BancoServico bancoServico;

    public PromptMenu() {
        bancoServico = new BancoServico();
    }

    private boolean dialogo(Scanner scanner, String texto, String yes, String no) {
        String resposta = "";
        while (true) {
            System.out.print(String.format("%s (%s/%s)", texto, yes, no));
            resposta = scanner.next();
            if (resposta.equals(yes)) {
                return true;
            }
            if (resposta.equals(no)) {
                return false;
            }
        }

    }

    private Conta getConta(Scanner scanner) {
        listarContas();
        System.out.print("Selecione o número da conta: ");
        int idConta = scanner.nextInt();
        var conta = bancoServico.getConta(idConta);
        conta.imprimirExtrato();
        return conta;
    }

    public void show() {
        System.out.println("++++++++++++ " + bancoServico.getNomeBanco() + " ++++++++++++");
        System.out.println("Seja bem vindo.");

        Scanner scanner = new Scanner(System.in);
        int operacao = 0;
        boolean loop = true;
        while (loop) {
            this.showFuncoes();
            System.out.print("Qual operação você deseja:");
            operacao = scanner.nextInt();

            switch (operacao) {
                case 1:
                    cadastrarCliente(scanner);
                    break;
                case 2:
                    escolherClienteECadastrarConta(scanner);
                    break;
                case 3:
                    listarClientes();
                    break;
                case 4:
                    listarContas();
                    break;
                case 5:
                    extrato(scanner);
                    break;
                case 6:
                    depositar(scanner);
                    break;
                case 7:
                    saque(scanner);
                    break;
                case 8:
                    tranferencia(scanner);
                    break;
                case 9:
                    listarOperacoes();
                    break;
                case 0:
                    loop = false;
                    break;

                default:
                    break;
            }
        }
        scanner.close();
    }

    private void showFuncoes() {
        System.out.println("");
        System.out.println("========================= Operações ========================");
        System.out.println("1 - Cadastrar Cliente;");
        System.out.println("2 - Cadastrar Conta;");
        System.out.println("3 - Listar Clientes;");
        System.out.println("4 - Listar contas;");
        System.out.println("5 - Extrato");
        System.out.println("6 - Depósito");
        System.out.println("7 - Saque");
        System.out.println("8 - Tranferência");
        System.out.println("9 - Listar Operações");
        System.out.println("0 - Sair");
        System.out.println("=============================================================");
    }

    private void cadastrarCliente(Scanner scanner) {
        System.out.print("Informe o nome do novo cliente: ");
        String nome = scanner.next();
        cadastrarConta(scanner, new Cliente(bancoServico.nextClienteId(), nome));
    }

    private void escolherClienteECadastrarConta(Scanner scanner) {
        this.listarClientes();
        System.out.print("Informe o id do novo cliente: ");
        int id = scanner.nextInt();
        Cliente clienteEscolhido = bancoServico.getCliente(id);

        System.out.println("Cliente selecionado -> " + clienteEscolhido.getNome());
        cadastrarConta(scanner, clienteEscolhido);
    }

    private void cadastrarConta(Scanner scanner, Cliente cliente) {
        if (dialogo(scanner, "Conta corrente ou Poupança?", "c", "p")) {
            bancoServico.addConta(new ContaPoupanca(cliente));
        } else {
            bancoServico.addConta(new ContaCorrente(cliente));
        }

    }

    private void listarClientes() {
        System.out.println("");
        System.out.println("========================== Clientes =========================");
        for (Cliente cliente : bancoServico.allClinetes()) {
            System.out.println(cliente.getId() + " - " + cliente.getNome());
        }
        System.out.println("=============================================================");
    }

    private void listarContas() {
        System.out.println("");
        System.out.println("========================= Contas ============================");
        for (var conta : bancoServico.allContas()) {
            if (conta.getClass().equals(ContaCorrente.class)) {
                System.out.println(conta.getNumero() + " - " + conta.getCliente().getNome() + " - Conta Corrente - "
                        + conta.getSaldo());
            } else {
                System.out.println(
                        conta.getNumero() + " - " + conta.getCliente().getNome() + " - Poupança - " + conta.getSaldo());
            }
        }
    }

    private void listarOperacoes() {
        System.out.println("");
        System.out.println("======================= Operações ==========================");
        System.out.println(String.format("Data|Tipo|Conta|Valor"));
        for (var operacao : bancoServico.allOperacaos()) {
            System.out.println(String.format("%s, | %s | nº. %s - Cliente: %s | R$ %.2f", operacao.getData(),
                    operacao.getTipo(),
                    operacao.getConta().getNumero(), operacao.getConta().getCliente().getNome(), operacao.getValor()));
        }

        System.out.println("=============================================================");
    }

    private void extrato(Scanner scanner) {
        Conta conta = getConta(scanner);
        conta.imprimirExtrato();
    }

    private void depositar(Scanner scanner) {
        Conta conta = getConta(scanner);
        if (dialogo(scanner, "Confirma a seleção da conta acima para depósito?", "s", "n")) {
            System.out.print("Informe o valor do depósito: ");
            String valor = scanner.next();
            bancoServico.depositar(conta, Double.parseDouble(valor));
        }
    }

    private void saque(Scanner scanner) {

        System.out.print("OPERAÇÃO SAQUE: ");
        Conta conta = getConta(scanner);

        if (dialogo(scanner, "Confirma a seleção da conta acima para saque?", "s", "n")) {
            System.out.print("Informe o valor do saque: ");
            double valor = Double.parseDouble(scanner.next());
            bancoServico.saque(conta, valor);
        }

    }

    private void tranferencia(Scanner scanner) {
        System.out.println("OPERAÇÃO TRANSFERÊNCIA:");
        System.out
                .print("Selecione o número da conta para transferir com o operador '>' indicando a conta a receber: ");
        var comando = scanner.next().split(">");
        var conta = bancoServico.getConta(Integer.parseInt(comando[0]));
        var destino = bancoServico.getConta(Integer.parseInt(comando[1]));
        System.out.println("=============================================================");
        conta.imprimirExtrato();
        System.out.println("=============================================================");
        destino.imprimirExtrato();
        System.out.println("=============================================================");

        if (dialogo(scanner, "Confirma a seleção de contas para transferência?", "s", "n")) {
            System.out.print("Informe o valor da tranferência: ");
            double valor = Double.parseDouble(scanner.next());
            bancoServico.tranferir(conta, destino, valor);
        }

    }

}