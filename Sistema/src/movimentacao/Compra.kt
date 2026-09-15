package movimentacao

import java.math.BigDecimal

class Compra(
    val id_produto: Int,
    val qtde: Int,
    val vlr_unitario: BigDecimal,
    val id_fornecedor: Int,
    val id_responsavel: Int
) {
}