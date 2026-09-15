package repositorio

import movimentacao.Compra
import movimentacao.FolhaFun
import movimentacao.Movimentacao
import movimentacao.Venda
import java.math.BigDecimal
import java.sql.SQLException
import java.sql.Statement
import kotlin.plus

class JPAMovimentacao() {
    val jpa = JPA()

    fun Saldo(): Int {
        jpa.conectar()

        val sqlSaldo = "SELECT saldo FROM saldo ORDER BY id DESC LIMIT 1"
        val stntSaldo = jpa.c!!.prepareStatement(sqlSaldo)
        val result = stntSaldo.executeQuery()

        var saldo = 0
        if (result.next()) {
            saldo = result.getInt("saldo")
        }

        result.close()
        stntSaldo.close()
        jpa.c!!.close()
        return saldo
    }

    fun MovCompra(movimentacao: Movimentacao, id_produtos: Int, compra: Compra, saldoAtual : BigDecimal) {
        println("Salvando...")
        try {

            jpa.conectar() //abre a conexão com o banco

            val sqlSaldo = "INSERT INTO saldo (vlr_mov, saldo, data) VALUES (?, ?, now())"
            val stntSaldo = jpa.c!!.prepareStatement(sqlSaldo, Statement.RETURN_GENERATED_KEYS)

            val vlrTotal = compra.vlr_unitario * compra.qtde.toBigDecimal()
            val vlrMov = -vlrTotal
            val novoSaldo = saldoAtual - vlrTotal

            stntSaldo.setBigDecimal(1, vlrMov)
            stntSaldo.setBigDecimal(2, novoSaldo)
            stntSaldo.executeUpdate()

            val rsSaldo = stntSaldo.generatedKeys
            var idSaldo: Int = 0
            if (rsSaldo.next()) { //ResultSet inicia antes do primeiro dado, next() move para a resposta
                idSaldo = rsSaldo.getInt(1) //pega a primeira coluna do ResultSet
            }

            rsSaldo.close()
            stntSaldo.close()

            val sql = "INSERT INTO movimentacao (valor, id_pagador,id_recebedor, data_mov, descricao, id_responsavel, id_saldo) " +
                    "VALUES (?, ?, ?, now(), ?, ?, ?)"

            val stnt = jpa.c!!.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)

            //Preparar as variaveis para o banco
            stnt.setBigDecimal(1, vlrMov)
            stnt.setInt(2, movimentacao.pagador)
            stnt.setInt(3, compra.id_fornecedor)
            stnt.setString(4, movimentacao.descricao.descricao)
            stnt.setInt(5, compra.id_responsavel)
            stnt.setInt(6, idSaldo)

            stnt.executeUpdate()

            val resultado = stnt.generatedKeys
            var idMov: Int = 0
            if (resultado.next()) { //ResultSet inicia antes do primeiro dado, next() move para a resposta
                idMov = resultado.getInt(1) //pega a primeira coluna do ResultSet
            }
            stnt.close()

            val sqlCompra = "INSERT INTO compra " +
                    "(id_produto, qtde,vlr_unitario, vlr_total, id_fornecedor, id_responsavel, data, id_mov) " +
                    "VALUES (?, ?, ?, ?, ?, ?, now(), ?)"

            val stntCompra = jpa.c!!.prepareStatement(sqlCompra)

            //Preparar as variaveis para o banco
            stntCompra.setInt(1, compra.id_produto)
            stntCompra.setInt(2, compra.qtde)
            stntCompra.setBigDecimal(3, compra.vlr_unitario)
            stntCompra.setBigDecimal(4, vlrTotal)
            stntCompra.setInt(5, compra.id_fornecedor)
            stntCompra.setInt(6, compra.id_responsavel)
            stntCompra.setInt(7, idMov)

            stntCompra.executeUpdate()

            val sqlQtdeAtual = "SELECT qtde_atual FROM estoque WHERE id_produto = ? ORDER BY id DESC LIMIT 1"
            val stntProduto = jpa.c!!.prepareStatement(sqlQtdeAtual)
            stntProduto.setInt(1, id_produtos)
            val rs = stntProduto.executeQuery()

            var qtdAtual = 0
            if (rs.next()) {
                qtdAtual = rs.getInt("qtde_atual")
            }

            rs.close()
            stntProduto.close()
            qtdAtual += compra.qtde

            val sqlEstoque = "INSERT INTO estoque (movimento, qtde_atual,id_produto, data, id_mov) " +
                    "VALUES (?, ?, ?, now(), ?)"

            val stntEstoque = jpa.c!!.prepareStatement(sqlEstoque)

            //Preparar as variaveis para o banco
            stntEstoque.setInt(1, compra.qtde)
            stntEstoque.setInt(2, qtdAtual)
            stntEstoque.setInt(3, compra.id_produto)
            stntEstoque.setInt(4, idMov)

            stntEstoque.executeUpdate()
            stntEstoque.close()

            jpa.c!!.close() //encerra a conexão com o banco
        } catch (e: SQLException) {
            println("Não salvou: ${e.printStackTrace()}")
        }
    }

    fun Estoque (id: Int): Int{
        jpa.conectar()

        val sqlQtdeAtual = "SELECT qtde_atual FROM estoque WHERE id_produto = ? ORDER BY id DESC LIMIT 1"
        val stntProduto = jpa.c!!.prepareStatement(sqlQtdeAtual)
        stntProduto.setInt(1, id)
        val rs = stntProduto.executeQuery()

        var qtdEstoque = 0
        if (rs.next()) {
            qtdEstoque = rs.getInt("qtde_atual")
        }

        rs.close()
        stntProduto.close()
        jpa.c!!.close()
        return qtdEstoque
    }

    fun MovVenda(movimentacao: Movimentacao, id_produtos: Int, venda: Venda, saldoAtual : BigDecimal, qtdEstoque: Int) {
        println("Salvando...")
        try {

            jpa.conectar() //abre a conexão com o banco

            val sqlPreco = "SELECT preco FROM caixa_da_agua WHERE id = ? LIMIT 1"
            val stntPreco = jpa.c!!.prepareStatement(sqlPreco)
            stntPreco.setInt(1, id_produtos)
            val rsPreco = stntPreco.executeQuery()

            var preco = 0
            if (rsPreco.next()) {
                preco = rsPreco.getInt("preco")
            }

            rsPreco.close()
            stntPreco.close()

            val vlrTotal = preco.toBigDecimal() * venda.qtde.toBigDecimal()

            val sqlSaldo = "INSERT INTO saldo (vlr_mov, saldo, data) VALUES (?, ?, now())"
            val stntSaldo = jpa.c!!.prepareStatement(sqlSaldo, Statement.RETURN_GENERATED_KEYS)

            val novoSaldo = saldoAtual + vlrTotal
            stntSaldo.setBigDecimal(1, vlrTotal)
            stntSaldo.setBigDecimal(2, novoSaldo)
            stntSaldo.executeUpdate()

            val rsSaldo = stntSaldo.generatedKeys
            var idSaldo: Int = 0
            if (rsSaldo.next()) { //ResultSet inicia antes do primeiro dado, next() move para a resposta
                idSaldo = rsSaldo.getInt(1) //pega a primeira coluna do ResultSet
            }

            rsSaldo.close()
            stntSaldo.close()

            val sql = "INSERT INTO movimentacao (valor, id_pagador,id_recebedor, data_mov, descricao, id_responsavel, id_saldo) " +
                    "VALUES (?, ?, ?, now(), ?, ?, ?)"

            val stnt = jpa.c!!.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)


            //Preparar as variaveis para o banco
            stnt.setBigDecimal(1, vlrTotal)
            stnt.setInt(2, venda.id_cliente)
            stnt.setInt(3, movimentacao.recebedor)
            stnt.setString(4, movimentacao.descricao.descricao)
            stnt.setInt(5, venda.id_responsavel)
            stnt.setInt(6, idSaldo)

            stnt.executeUpdate()

            val resultado = stnt.generatedKeys
            var idMov: Int = 0
            if (resultado.next()) { //ResultSet inicia antes do primeiro dado, next() move para a resposta
                idMov = resultado.getInt(1) //pega a primeira coluna do ResultSet
            }
            stnt.close()

            val sqlVenda = "INSERT INTO venda " +
                    "(id_produto, qtde,vlr_unitario, vlr_total, id_cliente, id_responsavel, data, id_mov) " +
                    "VALUES (?, ?, ?, ?, ?, ?, now(), ?)"

            val stntVenda = jpa.c!!.prepareStatement(sqlVenda)

            //Preparar as variaveis para o banco
            stntVenda.setInt(1, venda.id_produto)
            stntVenda.setInt(2, venda.qtde)
            stntVenda.setBigDecimal(3, preco.toBigDecimal())
            stntVenda.setBigDecimal(4, vlrTotal)
            stntVenda.setInt(5, venda.id_cliente)
            stntVenda.setInt(6, venda.id_responsavel)
            stntVenda.setInt(7, idMov)

            stntVenda.executeUpdate()

            val qtdAtual = qtdEstoque - venda.qtde

            val sqlEstoque = "INSERT INTO estoque (movimento, qtde_atual,id_produto, data, id_mov) " +
                    "VALUES (?, ?, ?, now(), ?)"

            val stntEstoque = jpa.c!!.prepareStatement(sqlEstoque)

            //Preparar as variaveis para o banco
            stntEstoque.setInt(1, venda.qtde)
            stntEstoque.setInt(2, qtdAtual)
            stntEstoque.setInt(3, venda.id_produto)
            stntEstoque.setInt(4, idMov)

            stntEstoque.executeUpdate()
            stntEstoque.close()

            jpa.c!!.close() //encerra a conexão com o banco
        } catch (e: SQLException) {
            println("Não salvou: ${e.printStackTrace()}")
        }
    }

    fun Salario(id: Int) : Int {
        jpa.conectar() //abre a conexão com o banco

        val sqlSalario = "SELECT salario FROM funcionarios WHERE id_pessoa = ? LIMIT 1"
        val stntSalario = jpa.c!!.prepareStatement(sqlSalario)
        stntSalario.setInt(1, id)
        val rsSalario = stntSalario.executeQuery()

        var salario = 0
        if (rsSalario.next()) {
            salario = rsSalario.getInt("salario")
        }

        rsSalario.close()
        stntSalario.close()

        jpa.c!!.close()
        return salario
    }

    fun PgtoFolha(funcionario: FolhaFun, movimentacao: Movimentacao, salario: BigDecimal, saldoAtual: BigDecimal){
        println("Salvando...")
        try {
            jpa.conectar() //abre a conexão com o banco

            val vlrMov = -salario

            val sqlSaldo = "INSERT INTO saldo (vlr_mov, saldo, data) VALUES (?, ?, now())"
            val stntSaldo = jpa.c!!.prepareStatement(sqlSaldo, Statement.RETURN_GENERATED_KEYS)

            val novoSaldo = saldoAtual - salario
            stntSaldo.setBigDecimal(1, vlrMov)
            stntSaldo.setBigDecimal(2, novoSaldo)
            stntSaldo.executeUpdate()

            val rsSaldo = stntSaldo.generatedKeys
            var idSaldo: Int = 0
            if (rsSaldo.next()) { //ResultSet inicia antes do primeiro dado, next() move para a resposta
                idSaldo = rsSaldo.getInt(1) //pega a primeira coluna do ResultSet
            }

            rsSaldo.close()
            stntSaldo.close()

            val sql = "INSERT INTO movimentacao (valor, id_pagador,id_recebedor, data_mov, descricao, id_responsavel, id_saldo) " +
                    "VALUES (?, ?, ?, now(), ?, ?, ?)"

            val stnt = jpa.c!!.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)


            //Preparar as variaveis para o banco
            stnt.setBigDecimal(1, vlrMov)
            stnt.setInt(2, movimentacao.pagador)
            stnt.setInt(3, funcionario.funcionario)
            stnt.setString(4, movimentacao.descricao.descricao)
            stnt.setInt(5, funcionario.responsavel)
            stnt.setInt(6, idSaldo)

            stnt.executeUpdate()
            val resultado = stnt.generatedKeys
            var idMov: Int = 0
            if (resultado.next()) { //ResultSet inicia antes do primeiro dado, next() move para a resposta
                idMov = resultado.getInt(1) //pega a primeira coluna do ResultSet
            }
            stnt.close()

            val sqlFolha = "INSERT INTO folha_funcionario (id_funcionario, valor, id_responsavel, data, id_mov) " +
                    "VALUES (?, ?, ?, now(), ?)"

            val stntFolha = jpa.c!!.prepareStatement(sqlFolha)

            //Preparar as variaveis para o banco
            stntFolha.setInt(1, funcionario.funcionario)
            stntFolha.setBigDecimal(2, salario)
            stntFolha.setInt(3, funcionario.responsavel)
            stntFolha.setInt(4, idMov)

            stntFolha.executeUpdate()
            stntFolha.close()

            jpa.c!!.close() //encerra a conexão com o banco

        } catch (e: SQLException) {
            println("Não salvou: ${e.printStackTrace()}")
        }
    }

}