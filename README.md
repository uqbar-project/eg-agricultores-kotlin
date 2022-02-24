
# Ejercicio de Diseño - Agricultores

[![build](https://github.com/uqbar-project/eg-agricultores-kotlin/actions/workflows/build.yml/badge.svg?branch=refactor)](https://github.com/uqbar-project/eg-microprocesador-kotlin/actions/workflows/build.yml) [![codecov](https://codecov.io/gh/uqbar-project/eg-agricultores-kotlin/branch/refactor/graph/badge.svg?token=Cwoprdysxs)](https://codecov.io/gh/uqbar-project/eg-agricultores-kotlin)

![agricultores](images/agricultores.png)


## Dominio

Se pide modelar una solución para un grupo de agricultores, que tienen parcelas. Se asume el siguiente circuito:

* El agricultor decide comprar una parcela para cultivar
* Cuando el agricultor siembra sube la cantidad de hectáreas cultivadas
* Cuando el agricultor cosecha disminuye la cantidad de hectáreas cultivadas y sube el silo (en kilos).
* Al vender, baja el silo en kilos (la cantidad cultivada no cambia)
* En algún momento, el agricultor puede decidir cambiar el cultivo de la parcela, para lo cual no debe quedar cantidad cultivada en la parcela ni en el silo.

## Parcelas

Cada parcela tiene

* un tamaño (medido en hectáreas),
* el cultivo que planta,
* la cantidad de hectáreas cultivadas y
* la cantidad (en kilos) de cultivo cosechado, que se guarda en un silo.

Tenemos diferentes tipos de cultivo:

* **Soja**
    * El costo de la soja es de $ 10 por hectárea
    * El precio de venta por kg se calcula como 10 veces el costo x hectárea menos
      una retención de 10% si el tamaño de la parcela supera las 1000 hectáreas.

* **Soja transgénica**: sabe si el que lo come puede sufrir mutaciones genéticas
    * El costo por hectárea se calcula igual que la soja
    * El precio de venta por kg se calcula igual que la soja o la mitad de ese precio si el que lo come puede sufrir mutaciones genéticas.

* **Trigo**
    * El costo es de $ 5 por hectárea hasta un máximo de $ 500 (en una parcela de 200 hectáreas el costo es $ 500, en una parcela de 50 hectáreas el costo es $ 250)
    * El precio de venta es de $ 20 por kg a los cuales hay que restarle los conservantes del silo (se conoce el costo por kg de cada conservante).

* **Sorgo**
    * El costo es de 3 $ por hectárea si la cantidad cultivada es menor a 50 hectáreas
      o $ 2 en caso contrario.
    * El precio de venta es de $ 20 por kg.
    

Se pide codificar:

1. El costo total del cultivo de una parcela determinada (costo x hectárea por cantidad cultivada en la parcela).

2. El precio de venta del cultivo de una parcela determinada (precio venta x kg).

3. Cuáles son los cultivos de un agricultor, sin repetidos.

4. Saber si un agricultor tiene alguna parcela subutilizada (cuando la cantidad cultivada no llega al 50% del tamaño de la parcela en hectáreas).

## Code smells

### Soja

```kt
    override fun precioVenta(parcela: Parcela): Double {
        var costo1 = 100.0 // 10 * el costo por hectárea que es 10
        if (parcela.tamanio > 1000) {
            costo1 *= 0.9
        }
        return costo1
    }
```

- el costo por hectárea deberíamos utilizarlo y **no hacer cuentas nosotros**. El código no es resiliente a cambios, si cambia el costo por hectárea es imposible rastrearlo porque está incorporado en el cálculo
- por otra parte, el nombre `costo1` tiene un nombre que no revela la intención: **intention revealing**
- la pregunta `parcela.tamanio > 1000` está tomando una responsabilidad que podría ser propia de Parcela
- y ya que estamos, la pregunta sobre el tamaño no reifica un concepto importante, que es la retención. Si escribimos un método, no solo le damos entidad a esa idea del negocio sino también podemos reutilizar la idea en otro momento.

```kt
override fun precioVenta(parcela: Parcela) = 10 * costo(parcela) * retencion(parcela)
private fun retencion(parcela: Parcela) = if (parcela.esGrande()) 0.9 else 1.0
```

### Soja Transgenica

- La Soja transgénica debería heredar de Soja y solo redefinir el precio de venta:

```kt
class SojaTransgenica : Soja() {
    var puedeSufrirMutaciones: Boolean = true
    override fun precioVenta(parcela: Parcela) = ajustePorMutacion() * super.precioVenta(parcela)
    private fun ajustePorMutacion() = if (puedeSufrirMutaciones) 0.5 else 1.0
}
```

Fíjense que nuevamente extraemos un método para reificar el ajuste por mutación.

### Trigo

Con respecto al trigo, fíjense esta solución:

```kt
class Trigo : Cultivo {
    val conservantes = mutableListOf<Conservante>()
    var costoConservantes = 0.0

    fun agregarConservante(conservante: Conservante) {
        conservantes.add(conservante)
        var aux = 0.0
        for (i in 0..conservantes.size - 1) {
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
```

- respecto al costo, hay un uso de variables locales innecesarias, el código es muy **imperativo** (tiene bastante control del flujo), fíjense esta otra variante

```kt
    override fun costo(parcela: Parcela) = minOf(parcela.tamanio * 5.0, 500.0)
```

es más directa y delega el control en la función `minOf` de Kotlin.

- por otra parte vemos el bad smell **temporary variable**: el costo de los conservantes es un valor calculable, no hay necesidad de pre-calcularlo, asumiendo que la colección de conservantes es acotada y no hay costo computacional en resolver el costo.
- el método `agregarConservante` tiene dos responsabilidades: agregar un conservante a la lista **y** recalcular el costo de los conservantes, por lo tanto tiene baja cohesión
- si tuviéramos un método `quitarConservante`, hay un acoplamiento que no es fácil de detectar con la referencia `costoConservantes`
- el refactor transforma la variable en un método de consulta, que además utiliza `sumOf`, una forma más **declarativa** que trabajar mediante un forEach + índice + una variable auxiliar

```kt
    fun agregarConservante(conservante: Conservante) {
        conservantes.add(conservante)
    }

    override fun precioVenta(parcela: Parcela) = 20 - costoConservantes()
    private fun costoConservantes() = conservantes.sumOf { it.costo }
```

Originalmente la implementación de la suma de conservantes había sido:

```kt
for (i in 1..conservantes.size) {
    ...
```

lo cual en el test tiraba un `Index out of bounds` (porque las listas comienzan a partir de 0). Esto no nos pasa nunca si enviamos el mensaje `sumOf` (está encapsulado).

### Sorgo

Al ver el código ya vamos comprendiendo ciertas formas que podríamos mejorar:

```kt
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
```

En particular la variable `costo` podemos extraerla en un método:

```kt
    override fun costo(parcela: Parcela) = parcela.tamanio * costoPorHectarea(parcela)

    private fun costoPorHectarea(parcela: Parcela) = if (parcela.esChica()) 3.0 else 2.0
```

### Cultivos de un agricultor

En el método que calcula los cultivos de un agricultor nuevamente tenemos código **imperativo**, que tiene reminiscencias de lenguajes como C o Pascal:

```kt
class Agricultor {
    val parcelas = mutableListOf<Parcela>()

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
}
```

En lugar de eso, vamos a obtener los cultivos de cada parcela, con duplicados, mediante el mensaje `map` y luego `distinct` para eliminar los duplicados:

```kt
fun cultivos() = parcelas.map { it.cultivo.javaClass.name }.distinct()
```

Escribimos menos porque nuestra escritura es más **declarativa**, menos propensa a errores: tenemos menos control sobre el algoritmo (no sabemos de qué manera va a filtrar los elementos ni cómo lo resuelve distinct, pero no nos importa).

### Parcela subutilizada

Pasemos al último punto, donde se repiten algunas cosas que hemos visto antes:

```kt
class Agricultor { ...
    fun algunaParcelaSubutilizada(): Boolean {
        var subutilizada = false
        for (parcela in parcelas) {
            if (parcela.tamanio > parcela.cantidadCultivada * 2) {
                subutilizada = true
            }
        }
        return subutilizada
    }
```

- nuevamente el código es **imperativo**, un for + if que además no está cortando (podemos tener 20 parcelas, si la primera está subutilizada se sigue recorriendo las otras 19)
- podríamos trabajar con un `return` para cortar el loop, pero antes que eso está el método `any` que es más **declarativo** (delegamos el control de flujo al método)
- por otra parte nos está faltando reificar el concepto de parcela subutilizada, que es responsabilidad de la parcela (es un smell de **Feature Envy** que envía mensajes al objeto, porque se piensa en la parcela como una estructura de datos o bien un  **Data class**, otro smell)

```kt
class Parcela { ...
    fun subutilizada() = tamanio > cantidadCultivada * 2
```

Ahora que tenemos esa responsabilidad en Parcela, en Agricultor solo debemos hacer:

```kt
fun algunaParcelaSubutilizada() = parcelas.any { it.subutilizada() }
```

El lector podrá apreciar que al escribir código **declarativo**, tenemos menos chances de cometer errores (podemos equivocarnos en el mensaje que enviamos a la parcela, pero es más fácil de identificar).
