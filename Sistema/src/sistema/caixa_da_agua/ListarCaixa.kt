package sistema.caixa_da_agua
import repositorio.JPA


fun listarCaixa(){
    val conexao = JPA()
    conexao.listarCaixa()
}