package ar.edu.unsam.algo2.agricultores

interface Cultivo {
    fun costo(parcela: Parcela): Double
    fun precioVenta(parcela: Parcela): Double
}

open class Soja : Cultivo {
    override fun costo(parcela: Parcela) = 10.0
    override fun precioVenta(parcela: Parcela) = 10 * costo(parcela) * retencion(parcela)
    private fun retencion(parcela: Parcela) = if (parcela.esGrande()) 0.9 else 1.0
}

class SojaTransgenica : Soja() {
    var puedeSufrirMutaciones: Boolean = true
    override fun precioVenta(parcela: Parcela) = ajustePorMutacion() * super.precioVenta(parcela)
    private fun ajustePorMutacion() = if (puedeSufrirMutaciones) 0.5 else 1.0
}

class Trigo : Cultivo {
    val conservantes = mutableListOf<Conservante>()

    override fun costo(parcela: Parcela) = minOf(parcela.tamanio * 5.0, 500.0)
    fun agregarConservante(conservante: Conservante) {
        conservantes.add(conservante)
    }

    override fun precioVenta(parcela: Parcela) = 20 - costoConservantes()
    private fun costoConservantes() = conservantes.sumOf { it.costo }
}

data class Conservante(val costo: Double = 1.0)

class Sorgo : Cultivo {

    override fun costo(parcela: Parcela) = parcela.tamanio * costoPorHectarea(parcela)
    private fun costoPorHectarea(parcela: Parcela) = if (parcela.esChica()) 3.0 else 2.0

    override fun precioVenta(parcela: Parcela) = 20.0
}

class Parcela {
    var cultivo: Cultivo = Soja()
    var tamanio = 500
    var cantidadCultivada = 100

    fun esGrande() = tamanio > 1000
    fun esChica() = tamanio < 50
    fun subutilizada() = tamanio > cantidadCultivada * 2
}

