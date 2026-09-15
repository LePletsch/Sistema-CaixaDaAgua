package enumeradores

enum class ObsMovimentacao(val descricao: String) {
    COMPRA("Pagamento de compra de produto."),
    VENDA("Recebimento de venda de produto."),
    FOLHA("Pagamento de folha de funcionario.")
}