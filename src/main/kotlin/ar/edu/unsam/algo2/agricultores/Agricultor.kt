package ar.edu.unsam.algo2.agricultores

class Agricultor {
    val parcelas = mutableListOf<Parcela>()

    fun agregarParcela(parcela: Parcela) {
        parcelas.add(parcela)
    }

    fun cultivos(): List<String> {
        val cultivos = mutableListOf<String>()
        for (parcela in parcelas) {
            val nombreCultivo = parcela.cultivo.javaClass.name
            if (!cultivos.contains(nombreCultivo)) {
                cultivos.add(nombreCultivo)
            }
        }
        return cultivos
    }

    fun algunaParcelaSubutilizada(): Boolean {
        var subutilizada = false
        for (parcela in parcelas) {
            if (parcela.tamanio > parcela.cantidadCultivada * 2) {
                subutilizada = true
            }
        }
        return subutilizada
    }
}