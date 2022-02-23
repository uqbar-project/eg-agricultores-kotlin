package ar.edu.unsam.algo2.agricultores

class Agricultor {
    val parcelas = mutableListOf<Parcela>()

    fun cultivos(): List<Cultivo> {
        val cultivos = mutableListOf<Cultivo>()
        for (parcela in parcelas) {
           if (!cultivos.contains(parcela.cultivo)) {
               cultivos.add(parcela.cultivo)
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