package sistema

import produto.CaixaDaAgua
import repositorio.JPA
import sistema.caixa_da_agua.cadastrarNovaCaixa
import sistema.caixa_da_agua.editarCaixa
import sistema.caixa_da_agua.excluirCaixa
import sistema.caixa_da_agua.listarCaixa
import sistema.pessoa.EditarPessoa
import sistema.pessoa.cadastrarNovaPessoa
import sistema.pessoa.listarPessoa

fun menuInicial() {
    do {
        println("-------------- MENU INICIAL --------------")
        println("0 - Sair")
        println("1 - Cadastro Caixa de Água")
        println("2 - Cadastro Pessoas")
        println("3 - Movimentações Financeiras")
        val regex = Regex("\\d")
        val validaEmail = Regex("""^[a-zA-Z0-9]+.@[a-z]+(.com.br)$""")
        val op = readln()
        validaEmail.find(op)

        //Se for um digito será verdadeiro
        if (regex.matches(op)) {

            when (op) {
                "1" -> {
                    do {
                        println("-------------- CAIXA DE ÁGUA --------------")
                        println("0 - Voltar")
                        println("1 - Cadastrar Caixa de Água")
                        println("2 - Editar Caixa de Água")
                        println("3 - Listar Caixa de Água")
                        println("4 - Excluir Caixa de Água")
                        println("5 - Sair")
                        val regex = Regex("\\d")
                        val op = readln()

                        //Se for um digito será verdadeiro
                        if (regex.matches(op)) {

                            when (op) {
                                "0" -> break
                                "1" -> cadastrarNovaCaixa()
                                "2" -> editarCaixa()
                                "3" -> listarCaixa()
                                "4" -> excluirCaixa()
                                "5" -> return
                                else -> println("Ação Inválida!")
                            }
                        }
                    } while (true)
                }

                "2" -> {
                    do {
                        println("-------------- PESSOAS --------------")
                        println("0 - Voltar")
                        println("1 - Cadastrar Pessoas")
                        println("2 - Editar Pessoas cadastradas")
                        println("3 - Listar Pessoas cadastradas")
                        println("4 - Excluir Pessoas cadastradas")
                        println("5 - Sair")
                        val regex = Regex("\\d")
                        val op = readln()

                        //Se for um digito será verdadeiro
                        if (regex.matches(op)) {

                            when (op) {
                                "0" -> break
                                "1" -> cadastrarNovaPessoa()
                                "2" -> EditarPessoa()
                                "3" -> listarPessoa()
                                "4" -> excluirCaixa()
                                "5" -> return
                                else -> println("Ação Inválida!")
                            }
                        }
                    } while (true)
                }

                "3" -> {
                    do {
                        println("-------------- FINANCEIRO --------------")
                        println("0 - Voltar")
                        println("1 - Compra")
                        println("2 - Venda")
                        println("3 - Folha Funcionário")
                        println("4 - Sair")
                        val regex = Regex("\\d")
                        val op = readln()

                        //Se for um digito será verdadeiro
                        if (regex.matches(op)) {

                            when (op) {
                                "0" -> break
                                "1" -> cadastrarNovaPessoa()
                                "2" -> editarCaixa()
                                "3" -> listarCaixa()
                                "4" -> return
                                else -> println("Ação Inválida!")
                            }
                        }
                    } while (true)
                }
                "0" -> return
                else -> println("Ação Inválida!")
            }
        }
    } while (true)

}