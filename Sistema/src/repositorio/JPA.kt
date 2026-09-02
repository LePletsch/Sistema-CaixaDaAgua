package repositorio

import produto.CaixaDaAgua
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

//porta: 5432
//usuario:postgres
//senha:postgress
//banco:caixaDaAgua

class JPA (
    val user : String = "postgres",
    val senha : String = "postgress",
    val url : String = "jdbc:postgresql://localhost:5432/caixaDaAgua",
    var c : Connection? = null
)
{
    fun conectar() {
        try {
            //Carregar o Driver
            Class.forName("org.postgresql.Driver")

            //Estabelecer Conexão
            c = DriverManager.getConnection(url, user, senha)
            println("Conectado")

        } catch (e: SQLException) {

        }

    }

    fun salvarCaixa(a: CaixaDaAgua) {
        println("Salvando...")
        try {

            conectar() //abre a conexão com o banco
            val sql = "INSERT INTO caixa_da_agua " +
            "(marca, modelo,dimensao, cor, material, formato, preco, estoque) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?)"

            val stnt = c!!.prepareStatement(sql)

            //Preparar Lista para Double Precision
            val doublePrecision = c!!.createArrayOf("float8", a.dimensao.toTypedArray())
            //toTypeArray() converte um array para um tipo de dado legivel para o Postgres

            //Preparar as variaveis para o banco
            stnt.setString(1, a.marca.name)
            stnt.setString(2, a.modelo.name)
            stnt.setArray(3, doublePrecision)
            stnt.setString(4, a.cor.name)
            stnt.setString(5, a.material.name)
            stnt.setString(6, a.formato.name)
            stnt.setBigDecimal(7, a.preco)
            stnt.setInt(8, a.estoque)

            stnt.executeUpdate()

            stnt.close()
            c!!.close() //encerra a conexão com o banco
        }catch (e: SQLException){
            println("Não salvou: ${e.printStackTrace()}")
        }
    }

    fun listarCaixa() {
        try {
            conectar()
            val stmt = c!!.createStatement()

            val sql = "SELECT * from caixa_da_agua"
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
            c!!.close()

        } catch (e: SQLException) {
            println(e.printStackTrace())
        }

    }//Fim listar

    fun editarCaixa(caixa: CaixaDaAgua, id: Int) {
        try {
            conectar()
            val sql = "UPDATE caixa_da_agua SET preco = ?, marca = ?, modelo = ?, formato = ?, cor = ?, material = ?, dimensao = ?, estoque = ? WHERE id = ?"
            //Continuar a logica para os outros itens

            val stmt = c!!.prepareStatement(sql)

            val doublePrecision = c!!.createArrayOf("float8", caixa.dimensao.toTypedArray())

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
            c!!.close()

        } catch (e: SQLException) {
            println(e.printStackTrace())
        }

    }

    fun excluirCaixa(id: Int) {
        try {
            conectar()
            val sql = "DELETE FROM caixa_da_agua WHERE id = ?"
            val stmt = c!!.prepareStatement(sql)
            stmt.setInt(1, id)
            stmt.executeUpdate()

            c!!.close()


        } catch (e: SQLException) {
            println(e.printStackTrace())
        }

    }
}
