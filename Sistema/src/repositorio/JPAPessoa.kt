package repositorio

import pessoas.Cliente
import pessoas.Fornecedor
import pessoas.Funcionario
import pessoas.Pessoa
import produto.CaixaDaAgua
import java.sql.SQLException
import java.sql.Statement

class JPAPessoa() {
    val jpa = JPA()

    fun salvarPessoa(p: Pessoa) {

        println("Salvando...")
        try {
            jpa.conectar()
            //abre a conexão com o banco
            val sql = "INSERT INTO pessoa " +
                    "(nome, email, telefone, tipo) " +
                    "VALUES (?, ?, ?, ?)"

            val stmt = jpa.c!!.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)

            //Preparar as variaveis para o banco
            stmt.setString(1, p.nome)
            stmt.setString(2, p.email)
            stmt.setString(3, p.telefone)
            stmt.setString(4, p.tipo.name)

            stmt.executeUpdate()

            //Retorna id em um objeto ResultSet, que funciona como uma tabela
            val resultado = stmt.generatedKeys
            var idPessoa: Int = 0
            if (resultado.next()) { //ResultSet inicia antes do primeiro dado, next() move para a resposta
                idPessoa = resultado.getInt(1) //pega a primeira coluna do ResultSet
            }

            stmt.close()
            when (p){
                 is Cliente -> {
                     val sql = "INSERT INTO cliente (id_pessoa, cpf) VALUES (?, ?)"

                     val stmt = jpa.c!!.prepareStatement(sql)

                     stmt.setInt(1, idPessoa)
                     stmt.setString(2, p.cpf)
                     stmt.executeUpdate()
                     stmt.close()
                 }

                is Fornecedor -> {
                    val sql = "INSERT INTO fornecedor (id_pessoa, cnpj) VALUES (?, ?)"

                    val stmt = jpa.c!!.prepareStatement(sql)

                    stmt.setInt(1, idPessoa)
                    stmt.setString(2, p.cnpj)
                    stmt.executeUpdate()
                    stmt.close()
                }

                is Funcionario -> {
                    val sql = "INSERT INTO funcionarios (id_pessoa, cpf, setor, salario, turno) VALUES (?, ?, ?, ?, ?)"

                    val stmt = jpa.c!!.prepareStatement(sql)

                    stmt.setInt(1, idPessoa)
                    stmt.setString(2, p.cpf)
                    stmt.setString(3, p.setor.name)
                    stmt.setBigDecimal(4, p.salario)
                    stmt.setString(5, p.turno.name)
                    stmt.executeUpdate()
                    stmt.close()
                }
            }

            jpa.c!!.close() //encerra a conexão com o banco
        } catch (e: SQLException) {
            println("Não salvou: ${e.printStackTrace()}")
        }
    }

    fun listarPessoas() {
        try {
            jpa.conectar()
            val stmt = jpa.c!!.createStatement()

            val sql = "SELECT * from pessoa"
            //metadados vem em forma de lista, ResultSet
            val metadados = stmt.executeQuery(sql)

            val resultado = metadados.metaData // Metadados
            val tamanhoTabela = resultado.columnCount//Tamanho da tabela em colunas

            while (metadados.next()) {
                for (i in 1..tamanhoTabela) {
                    //nome da coluna
                    val nomeColuna = resultado.getColumnName(i)
                    //dado que esta na coluna
                    val valorColuna = metadados.getObject(i)
                    println("$nomeColuna -> $valorColuna")
                } // fim for
                println("---------------------------------------------------")
            }//fim while


            stmt.close()
            jpa.c!!.close()

        } catch (e: SQLException) {
            println(e.printStackTrace())
        }

    }//Fim listar

    fun editarCaixa(caixa: CaixaDaAgua, id: Int) {
        try {
            jpa.conectar()
            val sql =
                "UPDATE caixa_da_agua SET preco = ?, marca = ?, modelo = ?, formato = ?, cor = ?, material = ?, dimensao = ?, estoque = ? WHERE id = ?"
            //Continuar a logica para os outros itens

            val stmt = jpa.c!!.prepareStatement(sql)

            val doublePrecision = jpa.c!!.createArrayOf("float8", caixa.dimensao.toTypedArray())

            stmt.setBigDecimal(1, caixa.preco)
            stmt.setString(2, caixa.marca.name)
            stmt.setString(3, caixa.modelo.name)
            stmt.setString(4, caixa.formato.name)
            stmt.setString(5, caixa.cor.name)
            stmt.setString(6, caixa.material.name)
            stmt.setArray(7, doublePrecision)
            stmt.setInt(8, caixa.estoque)
            stmt.setInt(9, id)

            stmt.executeUpdate()//Faz as alterações e manda pro banco

            stmt.close()
            jpa.c!!.close()

        } catch (e: SQLException) {
            println(e.printStackTrace())
        }

    }

    fun excluirCaixa(id: Int) {
        try {
            jpa.conectar()
            val sql = "DELETE FROM caixa_da_agua WHERE id = ?"
            val stmt = jpa.c!!.prepareStatement(sql)
            stmt.setInt(1, id)
            stmt.executeUpdate()

            jpa.c!!.close()


        } catch (e: SQLException) {
            println(e.printStackTrace())
        }

    }
}