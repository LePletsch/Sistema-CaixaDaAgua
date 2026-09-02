package pessoas

import enumeradores.Pessoas

class Fornecedor (

    val cnpj: String,
    nome: String,
    email: String,
    telefone: String,
    tipo : Pessoas

) : Pessoa (nome, email, telefone, tipo)

