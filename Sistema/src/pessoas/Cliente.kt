package pessoas

import enumeradores.Pessoas

class Cliente (

    val cpf: String,
    nome: String,
    email: String,
    telefone: String,
    tipo : Pessoas

) : Pessoa (nome, email, telefone, tipo)
