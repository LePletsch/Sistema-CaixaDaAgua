package repositorio

import enumeradores.Pessoas
import pessoas.Cliente
import pessoas.Fornecedor
import pessoas.Funcionario
import pessoas.Pessoa
import java.math.BigDecimal
import java.sql.SQLException
import java.sql.Statement

class JPAPessoa() {
    val jpa = JPA()

    // [\w.+-]+ -> uma ou mais letra, número, _, ., +, - | @ -> @ obrigatorio | \. -> um . obrigatorio | [a-zA-Z]{2,} -> 2 ou  mais letras maiuscula ou minuscula
    val regexEmail = Regex("^[\\w.+-]+@[\\w-]+\\.[a-zA-Z]{2,}$")

    // \(? -> () opcional | \d{2} -> 2 digitos obrigatorios (DDD) | [\s-]? -> espaço ou hífen opcional (separador) | 9? -> nº 9 opcional no inicio | \d{4} -> 4 digitos obrigatorios
    val regexTelefone = Regex("^\\(?\\d{2}\\)?[\\s-]?9?\\d{4}[\\s-]?\\d{4}$")

    // \d{3} -> 3 digitos obrigatorios | \.? -> ponto opcional | -? -> hífen opcional | \d{2} -> 2 digitos obrigatorios
    val regexCPF = Regex("^\\d{3}\\.?\\d{3}\\.?\\d{3}-?\\d{2}$")

    // \d{2} -> 2 digitos obrigatorios | \.? -> ponto opcional | \d{3} -> 3 digitos obrigatorios | /? -> barra opcional | \d{4} -> 4 digitos obrigatorios | -? -> hífen opcional | \d{2} -> 2 digitos obrigatorios
    val regexCNPJ = Regex("^\\d{2}\\.?\\d{3}\\.?\\d{3}/?\\d{4}-?\\d{2}$")

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

    fun listarPessoas(p: Pessoas): List<Int> {
        val ids = mutableListOf<Int>()

        try {
            jpa.conectar()
            val stmt = jpa.c!!.createStatement()

            val sql = when (p) {
                Pessoas.CLIENTE ->
                    "SELECT pessoa.id, pessoa.nome, pessoa.email, pessoa.telefone, pessoa.tipo, cliente.cpf " +
                            "FROM pessoa JOIN cliente ON pessoa.id = cliente.id_pessoa " +
                            "WHERE pessoa.tipo = 'CLIENTE'"
                Pessoas.FORNECEDOR ->
                    "SELECT pessoa.id, pessoa.nome, pessoa.email, pessoa.telefone, pessoa.tipo, fornecedor.cnpj " +
                            "FROM pessoa JOIN fornecedor ON pessoa.id = fornecedor.id_pessoa " +
                            "WHERE pessoa.tipo = 'FORNECEDOR'"
                Pessoas.FUNCIONARIO ->
                    "SELECT pessoa.id, pessoa.nome, pessoa.email, pessoa.telefone, pessoa.tipo, " +
                            "funcionarios.cpf, funcionarios.setor, funcionarios.salario, funcionarios.turno " +
                            "FROM pessoa JOIN funcionarios ON pessoa.id = funcionarios.id_pessoa " +
                            "WHERE pessoa.tipo = 'FUNCIONARIO'"
            }

            //metadados vem em forma de lista, ResultSet
            val metadados = stmt.executeQuery(sql)

            val resultado = metadados.metaData // Metadados
            val tamanhoTabela = resultado.columnCount//Tamanho da tabela em colunas

            while (metadados.next()) {
                // Captura o id desta linha (assumindo que "id" é sempre a 1ª coluna do SELECT)
                val idDaLinha = metadados.getInt("id")
                ids.add(idDaLinha)

                for (i in 1..tamanhoTabela) {
                    val nomeColuna = resultado.getColumnName(i)
                    val valorColuna = metadados.getObject(i)
                    println("$nomeColuna -> $valorColuna")
                }
                println("---------------------------------------------------")
            }

            stmt.close()
            jpa.c!!.close()

        } catch (e: SQLException) {
            println("Erro ao consultar o banco: ${e.message}")
            e.printStackTrace()
        }

        return ids

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

    fun excluirPessoa(tipo: String?, id: Int) {
        try {
            jpa.conectar()

            when(tipo){
                "CLIENTE" -> {
                    val sql = "DELETE FROM cliente WHERE id_pessoa = ?"
                    val stmt = jpa.c!!.prepareStatement(sql)
                    stmt.setInt(1, id)
                    stmt.executeUpdate()
                    stmt.close()
                }

                "FORNECEDOR" -> {
                    val sql = "DELETE FROM fornecedor WHERE id_pessoa = ?"
                    val stmt = jpa.c!!.prepareStatement(sql)
                    stmt.setInt(1, id)
                    stmt.executeUpdate()
                    stmt.close()
                }

                "FUNCIONARIO" -> {
                    val sql = "DELETE FROM funcionarios WHERE id_pessoa = ?"
                    val stmt = jpa.c!!.prepareStatement(sql)
                    stmt.setInt(1, id)
                    stmt.executeUpdate()
                    stmt.close()
                }
            }

            val sql = "DELETE FROM pessoa WHERE id = ?"
            val stmt = jpa.c!!.prepareStatement(sql)
            stmt.setInt(1, id)
            stmt.executeUpdate()

            jpa.c!!.close()


        } catch (e: SQLException) {
            println(e.printStackTrace())
        }

    }
}