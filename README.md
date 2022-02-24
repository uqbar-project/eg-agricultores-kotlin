
# Ejercicio de Diseño - Agricultores

[![build](https://github.com/uqbar-project/eg-agricultores-kotlin/actions/workflows/build.yml/badge.svg?branch=master)](https://github.com/uqbar-project/eg-microprocesador-kotlin/actions/workflows/build.yml) [![coverage](https://codecov.io/gh/uqbar-project/eg-agricultores-kotlin/graph/badge.svg)](https://codecov.io/gh/uqbar-project/eg-agricultores-kotlin/badge.svg)

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

## Buscando code smells

A partir de aquí te invitamos a recorrer la implementación y a descubrir code smells, así como la forma de resolverlos.

