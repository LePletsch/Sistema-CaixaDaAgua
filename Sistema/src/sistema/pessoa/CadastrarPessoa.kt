package sistema.pessoa

import enumeradores.Pessoas
import enumeradores.Setor
import enumeradores.Turno
import pessoas.Cliente
import pessoas.Fornecedor
import pessoas.Funcionario
import pessoas.Pessoa
import repositorio.JPA
import repositorio.JPAPessoa

fun cadastrarNovaPessoa(){
    val jpa = JPA()
    val conexao = JPAPessoa()
    var condicao1 = true
    var condicao2 = true
    var condicao3 = true
    var tipo = 0

    println("Digite o nome: ")
    val nome = readln()

    val email = jpa.regex("Digite o email: ", conexao.regexEmail, "Email inválido. Exemplo: nome@dominio.com")

    val telefone = jpa.regex("Digite o telefone: ", conexao.regexTelefone, "Telefone inválido. Use um formato como (11) 91234-5678 ou 10 a 11 dígitos")

    while (condicao1) {
        println("Escolha o tipo de cadastro: ")
        Pessoas.entries.forEach { tipo ->
            println("${tipo.ordinal}  - ${tipo.name}")
        }
        println("Numero do tipo: ")
        tipo = readln().toInt()

        if (tipo in Pessoas.entries.indices) {
            condicao1 = false
        }else {
            println("Número do tipo inválido!")
        }
    }

    val pessoa: Pessoa = when (tipo) {
        0 -> {
            val cpf = jpa.regex("Digite o cpf: ", conexao.regexCPF, "CPF inválido. Use o formato 000.000.000-00 ou 11 dígitos")
            Cliente(cpf = cpf, nome = nome, email = email, telefone = telefone, tipo = Pessoas.entries[tipo])
        }
        1 -> {
            val cpf = jpa.regex("Digite o cpf: ", conexao.regexCPF, "CPF inválido. Use o formato 000.000.000-00 ou 11 dígitos")
            var setor = 0
            var turno = 0
            while (condicao2) {
                println("Escolha o setor: ")
                Setor.entries.forEach { setor ->
                    println("${setor.ordinal}  - ${setor.name}")
                }
                println("Numero do setor: ")
                setor = readln().toInt()

                if (setor in Setor.entries.indices) {
                    condicao2 = false
                } else {
                    println("Número de setor inválido!")
                }
            }

            val salario = jpa.regex("Digite o salário: ", jpa.regexValor, "Salário inválido. Use o formato 0000.00")

            while (condicao3) {
                println("Escolha o turno: ")
                Turno.entries.forEach { turno ->
                    println("${turno.ordinal}  - ${turno.name}")
                }
                println("Numero do turno: ")
                turno = readln().toInt()

                if (turno in Turno.entries.indices) {
                    condicao3 = false
                } else {
                    println("Número do turno inválido!")
                }
            }

            Funcionario(cpf = cpf, setor = Setor.entries[setor], salario = salario.toBigDecimal(), turno = Turno.entries[turno], nome = nome, email = email, telefone = telefone, tipo = Pessoas.entries[tipo])
        }
        2 -> {
            val cnpj = jpa.regex("Digite o cnpj: ", conexao.regexCNPJ, "CNPJ inválido. Use o formato 00.000.000/0000-00 ou 14 dígitos.")
            Fornecedor(cnpj = cnpj, nome = nome, email = email, telefone = telefone, tipo = Pessoas.entries[tipo])
        }
        else -> {
            println("Tipo invalido!")
            return
        }
    }

    try {
        conexao.salvarPessoa(pessoa)
        println("Pessoa cadastrada com sucesso!")
    } catch (e: Exception) {
        println("Erro ao salvar no banco de dados: ${e.message}")
    }
}