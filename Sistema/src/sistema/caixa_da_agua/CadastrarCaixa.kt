package sistema.caixa_da_agua

import enumeradores.Cor
import enumeradores.Formato
import enumeradores.Marcas
import enumeradores.Material
import enumeradores.Modelo
import produto.CaixaDaAgua
import repositorio.JPA

fun cadastrarNovaCaixa(){
    println("Escolha a marca: ")
    Marcas.entries.forEach { marca ->
        println("${marca.ordinal}  - ${marca.name}")
    }
    println("Numero da marca: ")
    val marca = readln().toInt()

    println("Escolha o modelo: ")
    Modelo.entries.forEach { modelo ->
        println("${modelo.ordinal}  - ${modelo.name}")
    }
    println("Numero do modelo: ")
    val modelo = readln().toInt()

    println("Digite a largura: ")
    val largura = readln().toDouble()

    println("Digite a altura: ")
    val altura = readln().toDouble()

    println("Digite a profundidade: ")
    val profundidade = readln().toDouble()

    val dimensao = mutableListOf<Double>(largura, altura, profundidade)

    println("Escolha a cor: ")
    Cor.entries.forEach { cor ->
        println("${cor.ordinal}  - ${cor.name}")
    }
    println("Numero da cor: ")
    val cor = readln().toInt()

    println("Escolha o material: ")
    Material.entries.forEach { material ->
        println("${material.ordinal}  - ${material.name}")
    }
    println("Numero do material: ")
    val material = readln().toInt()

    println("Escolha o formato: ")
    Formato.entries.forEach { formato ->
        println("${formato.ordinal}  - ${formato.name}")
    }
    println("Numero do formato: ")
    val formato = readln().toInt()

    println("Digite o preco: ")
    val preco = readln().toBigDecimal()

    println("Digite a quantidade em estoque: ")
    val estoque = readln().toInt()

    val conexao = JPA()
    conexao.salvarCaixa(
        CaixaDaAgua(
            marca = Marcas.entries[marca],
            modelo = Modelo.entries[modelo],
            dimensao = dimensao,
            cor = Cor.entries[cor],
            material = Material.entries[material],
            formato = Formato.entries[formato],
            preco = preco,
            estoque = estoque
        )
    )

}