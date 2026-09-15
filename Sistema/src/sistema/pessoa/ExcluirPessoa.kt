package sistema.pessoa

import repositorio.JPAPessoa

fun ExcluirPessoa() {
    val jpa = JPAPessoa()
    var condicao = true
    var id = 0

    while(condicao){
        val lista = listarPessoa()
        println("Digite o id do cadastro que deseja excluir: ")
        id = readln().toInt()

        if (id in lista){
            condicao = false
        } else {
            println("ID nao encontrado!")
        }
    }

    val tipo = jpa.tipoPessoa(id)

    try {
        jpa.excluirPessoa(tipo,id)
    } catch (e: Exception) {
        println("Erro ao excluir cadastro: ${e.message}")
    }
}