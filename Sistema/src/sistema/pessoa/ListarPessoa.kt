package sistema.pessoa

import enumeradores.Pessoas
import enumeradores.Turno
import repositorio.JPAPessoa

fun listarPessoa(): List<Int> {
    val jpa = JPAPessoa()
    var condicao = true
    var tipo = 0

    while (condicao) {
        println("Escolha o tipo de cadastros que deseja visualizar: ")
        Pessoas.entries.forEach { tipo ->
            println("${tipo.ordinal}  - ${tipo.name}")
        }
        println("Numero do tipo: ")
        tipo = readln().toInt()

        if (tipo in Pessoas.entries.indices) {
            condicao = false
        } else {
            println("Tipo inválido!")
        }
    }

    val tipoEscolhido = Pessoas.entries[tipo]

    try {
       val id = jpa.listarPessoas(tipoEscolhido)
        return id
    } catch (e: Exception) {
        println("Erro ao listar cadastros: ${e.message}")
    }
    return listOf()
}