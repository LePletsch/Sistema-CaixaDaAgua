package sistema.caixa_da_agua

import enumeradores.Cor
import enumeradores.Formato
import enumeradores.Marcas
import enumeradores.Material
import enumeradores.Modelo
import produto.CaixaDaAgua
import repositorio.JPA

fun editarCaixa() {
    val jpa = JPA()
    jpa.listarCaixa()
    //Aqui exemplo de um item
    //Depois fazer os outros
    println("Digite uma caixa que deseja alterar: ")
    val id = readln().toInt()

    println("Escolha a marca ")
    Marcas.entries.forEach { marca ->
        marca.ordinal
        println("${marca.ordinal} - ${marca.name.replace("_", " ")}")
    }
    println("Numero da nova marca: ")
    val marca = readln().toInt()

    println("Escolha o modelo ")
    Modelo.entries.forEach { modelo ->
        modelo.ordinal
        println("${modelo.ordinal} - ${modelo.name.replace("_", " ")}")
    }
    println("Numero do novo modelo: ")
    val modelo = readln().toInt()

    println("Escolha o formato ")
    Formato.entries.forEach { formato ->
        formato.ordinal
        println("${formato.ordinal} - ${formato.name.replace("_", " ")}")
    }
    println("Numero do novo formato: ")
    val formato = readln().toInt()

    println("Digite a nova dimensão: ")
    println("Digite a nova largura: ")
    val largura = readln().toDouble()
    println("Digite a nova profundidade: ")
    val profundidade = readln().toDouble()
    println("Digite a nova altura: ")
    val altura = readln().toDouble()
    val dimensao = mutableListOf(largura, altura, profundidade)

    println("Escolha a cor ")

    Cor.entries.forEach { cor ->
        cor.ordinal
        println("${cor.ordinal} - ${cor.name.replace("_", " ")}")
    }
    println("Numero da cor NOVA: ")
    val cor = readln().toInt()

    println("Escolha o material ")
    Material.entries.forEach { material ->
        println("${material.ordinal} - ${material.name.replace("_", " ")}")
    }
    println("Numero do material NOVO: ")
    val material = readln().toInt()

    println("Digite o novo preço ")
    val preco = readln().toBigDecimal()

    println("Digite a quantidade em estoque ajustada: ")
    val estoque = readln().toInt()

    jpa.editarCaixa(
        CaixaDaAgua(
            marca = Marcas.entries[marca],
            modelo = Modelo.entries[modelo],
            formato = Formato.entries[formato],
            dimensao = dimensao,
            preco = preco,
            cor = Cor.entries[cor],
            material = Material.entries[material],
            estoque = estoque
        ),
        id
    )
}