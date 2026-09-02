//package produto
//
//import enumeradores.Habilidade
//import enumeradores.Turno
//import pessoas.Cliente
//import pessoas.Instalador
//import java.math.BigDecimal
//import java.sql.Date
//
//class Servico {
//    var instalador : Instalador = Instalador(
//        nome = "",
//        cpf = "",
//        idade = 0,
//        salario = BigDecimal.ZERO,
//        turno = Turno.NOTURNO,
//        habilidade = Habilidade.INSTALACAO
//    )
//    var cliente : Cliente = Cliente(
//        nomeCliente = "",
//        cpfCliente = "",
//        idadeCliente = 0,
//        dividasAbertas = false,
//        parcelasAPagar = mutableListOf()
//    )
//    var preco : BigDecimal = BigDecimal.valueOf(0.0)
//    var solicitante: String = "nome do solicitante"
//    var tipoServico : String = "tipo de serviço"
//    var prazoMax : Date = Date.valueOf("05/08/2026")
//    var dataSolicitacao : Date = Date.valueOf("")
//
//}