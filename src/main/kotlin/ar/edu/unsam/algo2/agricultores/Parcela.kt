package ar.edu.unsam.algo2.agricultores

interface Cultivo {
    fun costo(parcela: Parcela): Double
    fun precioVenta(parcela: Parcela): Double
}

open class Soja : Cultivo {
    override fun costo(parcela: Parcela) = 10.0
    override fun precioVenta(parcela: Parcela): Double {
        var costo1 = 100.0 // 10 * el costo por hectárea que es 10
        if (parcela.tamanio > 1000) {
            costo1 *= 0.9
        }
        return costo1
    }
}

class SojaTransgenica : Cultivo {
    var puedeSufrirMutaciones: Boolean = true
    override fun costo(parcela: Parcela) = 10.0 // igual que la Soja
    override fun precioVenta(parcela: Parcela): Double {
        var costo1 = 100.0 // 10 * el costo por hectárea que es 10
        if (parcela.tamanio > 1000) {
            costo1 *= 0.9
        }
        if (puedeSufrirMutaciones) {
            costo1 = costo1 / 2
        }
        return costo1
    }

}

class Trigo : Cultivo {
    val conservantes = mutableListOf<Conservante>()
    var costoConservantes = 0.0

    fun agregarConservante(conservante: Conservante) {
        conservantes.add(conservante)
        var aux = 0.0
        for (i in 1..conservantes.size) {
            aux += conservantes[i].costo
        }
        costoConservantes = aux
    }

    override fun costo(parcela: Parcela): Double {
        val costo = parcela.tamanio * 5.0
        if (costo > 500)
            return 500.0
        else
            return costo
    }

    override fun precioVenta(parcela: Parcela) = 20 - costoConservantes
}

data class Conservante(val costo: Double = 1.0)

class Sorgo : Cultivo {

    override fun costo(parcela: Parcela): Double {
        var costo = 3.0
        if (parcela.tamanio >= 50) {
            costo = 2.0
        }
        return costo * parcela.tamanio
    }


    override fun precioVenta(parcela: Parcela) = 20.0
}

class Parcela {
    var cultivo: Cultivo = Soja()
    var tamanio = 500
    var cantidadCultivada = 100
}

