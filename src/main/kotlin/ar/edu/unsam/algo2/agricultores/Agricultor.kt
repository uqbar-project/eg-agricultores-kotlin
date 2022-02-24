package ar.edu.unsam.algo2.agricultores

class Agricultor {
    val parcelas = mutableListOf<Parcela>()

    fun agregarParcela(parcela: Parcela) {
        parcelas.add(parcela)
    }

    fun cultivos() = parcelas.map { it.cultivo.javaClass.name }.distinct()

    fun algunaParcelaSubutilizada() = parcelas.any { it.subutilizada() }
}