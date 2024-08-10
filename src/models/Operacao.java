package models;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import models.conta.Conta;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Operacao {
    Date data;
    TipoOperacao tipo;
    Conta conta;
    double valor;

    public Operacao(TipoOperacao tipo, Conta conta, double valor) {
        data = new Date();
        this.tipo = tipo;
        this.conta = conta;
        this.valor = valor;
    }

}
