package sistema.caixa_da_agua

import repositorio.JPA

fun excluirCaixa(){
    val jpa = JPA()
    var condicao1 = true
    var id = 0

    while (condicao1) {
        val listaId = jpa.listarCaixa()
        println("Digite o ID que deseja excluir: ")
        id = readln().toInt()

        if (id in listaId){
            condicao1 = false
        } else {
            println("ID não encontrado!")
        }
    }

    jpa.excluirCaixa(id)
}