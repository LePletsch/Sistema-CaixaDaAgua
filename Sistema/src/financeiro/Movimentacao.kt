package financeiro

import java.math.BigDecimal
import java.time.LocalDate

class Movimentacao (
    var valor: BigDecimal,
    val dataMovimentacao : LocalDate,
    //val pessoa : Pessoa //Precisa fazer depois
){

    fun movimentar(valor: BigDecimal, data: LocalDate){

    }
}