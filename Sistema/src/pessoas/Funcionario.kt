package pessoas

import enumeradores.Pessoas
import enumeradores.Setor
import enumeradores.Turno
import java.math.BigDecimal

class Funcionario (

    val cpf: String,
    val setor: Setor,
    val salario : BigDecimal,
    val turno : Turno,
    nome: String,
    email: String,
    telefone: String,
    tipo : Pessoas

) : Pessoa (nome, email, telefone, tipo)

