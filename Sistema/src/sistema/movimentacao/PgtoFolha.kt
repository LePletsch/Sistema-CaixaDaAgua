package sistema.movimentacao

import enumeradores.ObsMovimentacao
import enumeradores.Pessoas
import movimentacao.FolhaFun
import movimentacao.Movimentacao
import repositorio.JPAMovimentacao
import repositorio.JPAPessoa

fun pagarFuncionario(){
    val jpa = JPAPessoa()
    val conexao = JPAMovimentacao()
    var condicao1 = true
    var condicao2 = true
    var condicao3 = true
    var funcionario = 0
    var pagador = 0
    var responsavel = 0
    val saldo = conexao.Saldo().toBigDecimal()

    println("Saldo disponivel: $saldo")

    while (condicao1) {
        val listaId = jpa.listarPessoas(Pessoas.FUNCIONARIO)

        println("Digite o id do funcionario que vai pagar o salario: ")
        funcionario = readln().toInt()

        if (funcionario in listaId){
            condicao1 = false
        } else {
            println("ID não encontrado!")
        }
    }

    val salario = conexao.Salario(funcionario).toBigDecimal()

    if (saldo < salario) {
        println("Saldo insuficiente!")
        println("Saldo atual: $saldo")
        return
    }

    while (condicao2) {
        val listaId = jpa.listarPessoas(Pessoas.FUNCIONARIO)

        println("Digite o id do pagador: ")
        pagador = readln().toInt()

        if (pagador in listaId){
            condicao2 = false
        } else {
            println("ID não encontrado!")
        }
    }

    while (condicao3) {
        val listaId = jpa.listarPessoas(Pessoas.FUNCIONARIO)

        println("Digite o id do responsavel pela compra: ")
        responsavel = readln().toInt()

        if (responsavel in listaId){
            condicao3 = false
        } else {
            println("ID não encontrado!")
        }
    }

    conexao.PgtoFolha(
        FolhaFun(funcionario = funcionario, responsavel = responsavel),
        Movimentacao(pagador = pagador, recebedor = funcionario, ObsMovimentacao.FOLHA),
        salario,
        saldo
    )
}