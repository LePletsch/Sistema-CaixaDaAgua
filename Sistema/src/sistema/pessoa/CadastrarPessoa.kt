package sistema.pessoa

import enumeradores.Pessoas
import enumeradores.Setor
import enumeradores.Turno
import pessoas.Cliente
import pessoas.Fornecedor
import pessoas.Funcionario
import pessoas.Pessoa
import repositorio.JPAPessoa

fun cadastrarNovaPessoa(){
    println("Digite o nome: ")
    val nome = readln()

    println("Digite o email: ")
    val email = readln()

    println("Digite o telefone: ")
    val telefone = readln()

    println("Escolha o tipo de cadastro: ")
    Pessoas.entries.forEach { tipo ->
        println("${tipo.ordinal}  - ${tipo.name}")
    }
    println("Numero do tipo: ")
    val tipo = readln().toInt()

    val pessoa: Pessoa = when (tipo) {
        0 -> {
            println("Digite o cpf: ")
            Cliente(cpf = readln(), nome = nome, email = email, telefone = telefone, tipo = Pessoas.entries[tipo])
        }
        1 -> {
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

            Funcionario(cpf = cpf, setor = Setor.entries[setor], salario = salario, turno = Turno.entries[turno], nome = nome, email = email, telefone = telefone, tipo = Pessoas.entries[tipo])
        }
        2 -> {
            println("Digite o cnpj: ")
            Fornecedor(cnpj = readln(), nome = nome, email = email, telefone = telefone, tipo = Pessoas.entries[tipo])
        }
        else -> {
            println("Tipo invalido!")
            return
        }
    }

    val conexao = JPAPessoa()
    conexao.salvarPessoa(pessoa)

}