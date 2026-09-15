package movimentacao

import enumeradores.ObsMovimentacao
import java.math.BigDecimal
import java.time.LocalDateTime

class Movimentacao (
    val pagador : Int,
    val recebedor : Int,
    val descricao : ObsMovimentacao
){
}