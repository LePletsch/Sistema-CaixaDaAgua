//package pessoas
//
//import enumeradores.Habilidade
//import enumeradores.Turno
//import financeiro.Movimentacao
//import java.math.BigDecimal
//import java.time.LocalDate
//
//class Instalador (
//    nome : String,
//    cpf : String ,
//    idade : Int,
//    val salario : BigDecimal,
//    val turno : Turno,
//    val habilidade: Habilidade
//) : Pessoa (
//    nome, cpf, idade
//){
//    override fun receberConta(valor : BigDecimal, conta : Pessoa) : Movimentacao{
//        return Movimentacao(
//            dinheiro = valor,
//            pessoa = conta,
//            dataMovimentacao = LocalDate.now()
//        )
//    }
//
//
//
//
//
//
//}