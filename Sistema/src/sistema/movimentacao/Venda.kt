package sistema.movimentacao

import enumeradores.ObsMovimentacao
import enumeradores.Pessoas
import movimentacao.Movimentacao
import movimentacao.Venda
import repositorio.JPA
import repositorio.JPAMovimentacao
import repositorio.JPAPessoa

fun venderCaixa(){
    val jpa = JPA()
    val jpaPess = JPAPessoa()
    val conexao = JPAMovimentacao()
    var condicao = true
    var condicao1 = true
    var condicao2 = true
    var produto = 0
    var recebedor = 0
    var cliente = 0
    var responsavel = 0

    while (condicao) {
        val listaId = jpa.listarCaixa()
        println("Digite o id da caixa que foi vendida: ")
        produto = readln().toInt()

        if (produto in listaId){
            condicao = false
        } else {
            println("ID não encontrado!")
        }
    }

    val estoque = conexao.Estoque(produto)
    println("Quantidade disponivel em estoque: $estoque")

    println("Digite a quantidade de caixas: ")
    val quantidade = readln().toInt()

    if (quantidade > estoque){
        println("Quantidade insuficiente no estoque!")
        println("Quantidade disponivel em estoque: $estoque")
        return
    }

    while (condicao1) {
        val listaId = jpaPess.listarPessoas(Pessoas.CLIENTE)

        println("Digite o id do cliente: ")
        cliente = readln().toInt()

        if (cliente in listaId){
            condicao1 = false
        } else {
            println("ID não encontrado!")
        }
    }

    while (condicao2) {
        val listaIdFun = jpaPess.listarPessoas(Pessoas.FUNCIONARIO)

        println("Digite o id do recebedor: ")
        recebedor = readln().toInt()

        if (recebedor in listaIdFun){
            while (condicao2) {
                println("Digite o id do responsavel pela venda: ")
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

    val saldo = conexao.Saldo().toBigDecimal()

    conexao.MovVenda(
        Movimentacao(pagador = cliente, recebedor = recebedor, descricao = ObsMovimentacao.VENDA),
        produto,
        Venda(id_produto = produto, qtde = quantidade, id_cliente = cliente, id_responsavel = responsavel),
        saldo,
        estoque
    )

}