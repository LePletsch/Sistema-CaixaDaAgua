package sistema.caixa_da_agua

import enumeradores.Cor
import enumeradores.Formato
import enumeradores.Marcas
import enumeradores.Material
import enumeradores.Modelo
import produto.CaixaDaAgua
import repositorio.JPA

fun cadastrarNovaCaixa(){
    val jpa = JPA()
    var condicao1 = true
    var condicao2 = true
    var condicao3 = true
    var condicao4 = true
    var condicao5 = true
    var marca = 0
    var modelo = 0
    var cor = 0
    var material = 0
    var formato = 0

    while (condicao1) {
        println("Escolha a marca: ")
        Marcas.entries.forEach { marca ->
            println("${marca.ordinal}  - ${marca.name}")
        }
        println("Numero da marca: ")
        marca = readln().toInt()

        if (marca in Marcas.entries.indices) {
            condicao1 = false
        } else {
            println("Numero da marca invalido")
        }
    }

    while (condicao2) {
        println("Escolha o modelo: ")
        Modelo.entries.forEach { modelo ->
            println("${modelo.ordinal}  - ${modelo.name}")
        }
        println("Numero do modelo: ")
        modelo = readln().toInt()

        if (modelo in Modelo.entries.indices) {
            condicao2 = false
        } else {
            println("Numero do modelo invalido")
        }
    }

    val largura = jpa.regex("Digite a largura: ", jpa.regexValor, "Valor inválido. Use o formato 0.00")

    val altura = jpa.regex("Digite a altura: ", jpa.regexValor, "Valor inválido. Use o formato 0.00")

    val profundidade = jpa.regex("Digite a profundidade: ", jpa.regexValor, "Valor inválido. Use o formato 0.00")

    val dimensao = mutableListOf<Double>(largura.toDouble(), altura.toDouble(), profundidade.toDouble())

    while (condicao3) {
        println("Escolha a cor: ")
        Cor.entries.forEach { cor ->
            println("${cor.ordinal}  - ${cor.name}")
        }
        println("Numero da cor: ")
        cor = readln().toInt()

        if (cor in Cor.entries.indices) {
            condicao3 = false
        } else {
            println("Numero da cor invalido")
        }
    }

    while (condicao4) {
        println("Escolha o material: ")
        Material.entries.forEach { material ->
            println("${material.ordinal}  - ${material.name}")
        }
        println("Numero do material: ")
        material = readln().toInt()

        if (material in Material.entries.indices) {
            condicao4 = false
        } else {
            println("Numero do material invalido")
        }
    }

    while (condicao5) {
        println("Escolha o formato: ")
        Formato.entries.forEach { formato ->
            println("${formato.ordinal}  - ${formato.name}")
        }
        println("Numero do formato: ")
        formato = readln().toInt()

        if (formato in Formato.entries.indices) {
            condicao5 = false
        } else {
            println("Numero do formato invalido")
        }
    }

    val preco = jpa.regex("Digite o preco: ", jpa.regexValor, "Valor inválido. Use o formato 0000.00")

    val conexao = JPA()
    conexao.salvarCaixa(
        CaixaDaAgua(
            marca = Marcas.entries[marca],
            modelo = Modelo.entries[modelo],
            dimensao = dimensao,
            cor = Cor.entries[cor],
            material = Material.entries[material],
            formato = Formato.entries[formato],
            preco = preco.toBigDecimal()
        )
    )

}