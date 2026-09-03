package repositorio

import enumeradores.Pessoas
import pessoas.Cliente
import pessoas.Fornecedor
import pessoas.Funcionario
import pessoas.Pessoa
import produto.CaixaDaAgua
import java.sql.ResultSet
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

    fun listarPessoas(p: Pessoas) {
        try {
            jpa.conectar()
            val stmt = jpa.c!!.createStatement()
            var sql = ""

            when (p) {
                Pessoas.CLIENTE -> {
                    sql = "SELECT pessoa.id, pessoa.nome, pessoa.email, pessoa.telefone, pessoa.tipo, cliente.cpf " +
                            "FROM pessoa JOIN cliente ON pessoa.id = cliente.id_pessoa " +
                            "WHERE pessoa.tipo = 'CLIENTE'"
                }
                Pessoas.FORNECEDOR -> {
                    sql = "SELECT pessoa.id, pessoa.nome, pessoa.email, pessoa.telefone, pessoa.tipo, fornecedor.cnpj " +
                            "FROM pessoa JOIN fornecedor ON pessoa.id = fornecedor.id_pessoa " +
                            "WHERE pessoa.tipo = 'FORNECEDOR'"
                }
                Pessoas.FUNCIONARIO -> {
                    sql = "SELECT pessoa.id, pessoa.nome, pessoa.email, pessoa.telefone, pessoa.tipo, " +
                            "funcionarios.cpf, funcionarios.setor, funcionarios.salario, funcionarios.turno " +
                            "FROM pessoa JOIN funcionarios ON pessoa.id = funcionarios.id_pessoa " +
                            "WHERE pessoa.tipo = 'FUNCIONARIO'"
                }
            }
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

    fun tipoPessoa(id: Int): String? {
        var tipo: String? = null
        try {
            jpa.conectar()
            val sql = "SELECT tipo FROM pessoa WHERE id = ?"
            val stmt = jpa.c!!.prepareStatement(sql)
            stmt.setInt(1, id)
            val rs = stmt.executeQuery()

            if (rs.next()) {
                tipo = rs.getString("tipo")
            }

            rs.close()
            stmt.close()
            jpa.c!!.close()

        } catch (e: SQLException) {
            println(e.printStackTrace())
        }

        return tipo
    }

    fun editarPessoas(p : Pessoa, id: Int) {
        try {
            jpa.conectar()
            val sql = "UPDATE pessoa SET nome = ?, email = ?, telefone = ?, tipo = ? WHERE id = ?"
            //Continuar a logica para os outros itens

            val stmt = jpa.c!!.prepareStatement(sql)

            stmt.setString(1, p.nome)
            stmt.setString(2, p.email)
            stmt.setString(3, p.telefone)
            stmt.setString(4, p.tipo.name)
            stmt.setInt(5, id)

            stmt.executeUpdate()//Faz as alterações e manda pro banco

            stmt.close()

            when(p){
                is Cliente -> {
                    val sql = "UPDATE cliente SET cpf = ? WHERE id_pessoa = ?"
                    val stmt = jpa.c!!.prepareStatement(sql)

                    stmt.setString(1, p.cpf)
                    stmt.setInt(2, id)
                    stmt.executeUpdate()//Faz as alterações e manda pro banco
                    stmt.close()
                }

                is Fornecedor -> {
                    val sql = "UPDATE fornecedor SET cnpj = ? WHERE id_pessoa = ?"
                    val stmt = jpa.c!!.prepareStatement(sql)

                    stmt.setString(1, p.cnpj)
                    stmt.setInt(2, id)
                    stmt.executeUpdate()//Faz as alterações e manda pro banco
                    stmt.close()
                }

                is Funcionario -> {
                    val sql = "UPDATE funcionarios SET cpf = ?, setor = ?, salario = ?, turno = ? WHERE id_pessoa = ?"
                    val stmt = jpa.c!!.prepareStatement(sql)

                    stmt.setString(1, p.cpf)
                    stmt.setString(2, p.setor.name)
                    stmt.setBigDecimal(3, p.salario)
                    stmt.setString(4, p.turno.name)
                    stmt.setInt(5, id)
                    stmt.executeUpdate()//Faz as alterações e manda pro banco
                    stmt.close()
                }
            }

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