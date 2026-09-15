package sistema.movimentacao

import enumeradores.ObsMovimentacao
import enumeradores.Pessoas
import movimentacao.Compra
import movimentacao.Movimentacao
import repositorio.JPA
import repositorio.JPAMovimentacao
import repositorio.JPAPessoa

fun comprarCaixa(){
    val jpa = JPA()
    val jpaPess = JPAPessoa()
    val conexao = JPAMovimentacao()
    var condicao = true
    var condicao1 = true
    var condicao2 = true
    var produto = 0
    var fornecedor = 0
    var pagador = 0
    var responsavel = 0
    val saldo = conexao.Saldo().toBigDecimal()

    println("Saldo disponivel: $saldo")

    while (condicao) {
        val listaId = jpa.listarCaixa()
        println("Digite o id da caixa que vai ser comprada: ")
        produto = readln().toInt()

        if (produto in listaId){
            condicao = false
        } else {
            println("ID não encontrado!")
        }
    }

    println("Digite a quantidade de caixas: ")
    val quantidade = readln().toInt()

    val valor = jpa.regex("Digite o valor unitario do produto: ", jpa.regexValor, "Valor inválido. Use o formato 0000.00")

    val total = valor.toBigDecimal() * quantidade.toBigDecimal()

    if (saldo < total){
        println("Saldo insuficiente!")
        println("Saldo atual: $saldo")
        return
    }

    while (condicao1) {
        val listaIdFor = jpaPess.listarPessoas(Pessoas.FORNECEDOR)

        println("Digite o id do fornecedor: ")
        fornecedor = readln().toInt()

        if (fornecedor in listaIdFor){
            condicao1 = false
        } else {
            println("ID não encontrado!")
        }
    }

    while (condicao2) {
        val listaIdFun = jpaPess.listarPessoas(Pessoas.FUNCIONARIO)

        println("Digite o id do pagador: ")
        pagador = readln().toInt()

        if (pagador in listaIdFun){
            while (condicao2) {
                println("Digite o id do responsavel pela compra: ")
                responsavel = readln().toInt()

                if (responsavel in listaIdFun) {
                    condicao2 = false
                } else {
                    println("ID não encontrado!")
                }
            }
        } else {
            println("ID não encontrado!")
        }

    }

    conexao.MovCompra(
        Movimentacao(pagador = pagador, recebedor = fornecedor, descricao = ObsMovimentacao.COMPRA),
        produto,
        Compra(id_produto = produto, qtde = quantidade, id_fornecedor = fornecedor, id_responsavel = responsavel, vlr_unitario = valor.toBigDecimal()),
        saldo
    )

}