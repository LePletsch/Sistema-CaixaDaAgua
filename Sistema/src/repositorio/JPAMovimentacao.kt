//package repositorio
//
//import java.math.BigDecimal
//import java.sql.SQLException
//import java.time.LocalDateTime
//
//fun salvarMovimentacao(contexto: String, valor: BigDecimal, data: LocalDateTime) {
//    val jpa = JPA()
//    jpa.conectar()
//
//    try {
//
//        jpa.conectar() //abre a conexão com o banco
//        val sql = "INSERT INTO movimentacao " +
//                "(valor, data_movimentacao, descricao) " + // descricao precisa ser em contexto, usuario não digita, apenas escolhe
//                "VALUES (?, ?, ?)"
//
//        val stnt = jpa.c!!.prepareStatement(sql)
//
//        //Preparar as variaveis para o banco
//        stnt.setString(1, valor.toString())
//        stnt.setDate(2, data) //data errada
//        stnt.setString(3, contexto)
//
//        stnt.executeUpdate()
//
//        stnt.close()
//        jpa.c!!.close() //encerra a conexão com o banco
//    }catch (e: SQLException){
//        println("Não salvou: ${e.printStackTrace()}")
//    }
//}