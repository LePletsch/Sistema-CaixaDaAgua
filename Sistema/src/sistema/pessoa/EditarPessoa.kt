package sistema.pessoa

import enumeradores.Pessoas
import enumeradores.Setor
import enumeradores.Turno
import pessoas.Cliente
import pessoas.Fornecedor
import pessoas.Funcionario
import pessoas.Pessoa
import repositorio.JPAPessoa

fun EditarPessoa(){
    val jpa = JPAPessoa()
    listarPessoa()

    println("Digite o id do cadastro que deseja alterar: ")
    val id = readln().toInt()

    println("Digite o nome: ")
    val nome = readln()

    println("Digite o email: ")
    val email = readln()

    println("Digite o telefone: ")
    val telefone = readln()

    val tipo = jpa.tipoPessoa(id)

    val pessoa: Pessoa = when (tipo) {
        "CLIENTE" -> {
            println("Digite o cpf: ")
            Cliente(cpf = readln(), nome = nome, email = email, telefone = telefone, tipo = Pessoas.CLIENTE)
        }
        "FUNCIONARIO" -> {
            println("Digite o cpf: ")
            val cpf = readln()

            println("Escolha o setor: ")
            Setor.entries.forEach { setor ->
                println("${setor.ordinal}  - ${setor.name}")
            }
            println("Numero do setor: ")
            val setor = readln().toInt()

            println("Digite o salário: ")
            val salario = readln().toBigDecimal()

            println("Escolha o turno: ")
            Turno.entries.forEach { turno ->
                println("${turno.ordinal}  - ${turno.name}")
            }
            println("Numero do setor: ")
            val turno = readln().toInt()

            Funcionario(cpf = cpf, setor = Setor.entries[setor], salario = salario, turno = Turno.entries[turno], nome = nome, email = email, telefone = telefone, tipo = Pessoas.FUNCIONARIO)
        }
        "FORNECEDOR" -> {
            println("Digite o cnpj: ")
            Fornecedor(cnpj = readln(), nome = nome, email = email, telefone = telefone, tipo = Pessoas.FORNECEDOR)
        }
        else -> {
            println("Tipo invalido!")
            return
        }
    }

    jpa.editarPessoas(pessoa,id)

}