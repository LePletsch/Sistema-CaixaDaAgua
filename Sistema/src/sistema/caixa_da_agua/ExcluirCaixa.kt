package sistema.caixa_da_agua

import repositorio.JPA

fun excluirCaixa(){
    val jpa = JPA()
    jpa.listarCaixa()
    println("Digite o ID que deseja excluir: ")
    val id = readln().toInt()

    jpa.excluirCaixa(id)
}