package produto

import enumeradores.Cor
import enumeradores.Formato
import enumeradores.Marcas
import enumeradores.Material
import enumeradores.Modelo
import java.math.BigDecimal

class CaixaDaAgua (

    val marca : Marcas,
    val modelo : Modelo,
    val dimensao : MutableList<Double>,
    val cor : Cor,
    val material : Material,
    val formato : Formato,
    val preco : BigDecimal,
    val estoque : Int

)