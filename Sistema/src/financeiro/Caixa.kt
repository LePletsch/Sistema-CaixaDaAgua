package financeiro

import java.math.BigDecimal

class Caixa (
    var saldo : BigDecimal //não posso mexer via codigo, apenas no banco
){
    fun receita(valor: BigDecimal) : BigDecimal {
        return valor
    }

    fun despesa(valor: BigDecimal): BigDecimal {
        return valor.multiply("-1".toBigDecimal())
    }
}