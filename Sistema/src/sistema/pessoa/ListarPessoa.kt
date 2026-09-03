package sistema.pessoa

import enumeradores.Pessoas
import repositorio.JPAPessoa

fun listarPessoa() {
    val jpa = JPAPessoa()

    println("Escolha o tipo de cadastros que deseja visualizar: ")
    Pessoas.entries.forEach { tipo ->
        println("${tipo.ordinal}  - ${tipo.name}")
    }
    println("Numero do tipo: ")
    val tipo = readln().toInt()
    val tipoEscolhido = Pessoas.entries[tipo]

    jpa.listarPessoas(tipoEscolhido)
}