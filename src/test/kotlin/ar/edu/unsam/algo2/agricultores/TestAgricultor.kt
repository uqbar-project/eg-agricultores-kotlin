package ar.edu.unsam.algo2.agricultores

import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class TestAgricultor : DescribeSpec({
    isolationMode = IsolationMode.InstancePerTest

    describe("cultivos de un agricultor") {
        val agricultor = Agricultor()
        it("si no hay cultivos devuelve una lista vacía") {
            agricultor.cultivos().size shouldBe 0
        }
        it("se pueden obtener los cultivos sin repetidos") {
            agricultor.apply {
                agregarParcela(Parcela().apply { cultivo = Soja() })
                agregarParcela(Parcela().apply { cultivo = Trigo() })
                agregarParcela(Parcela().apply { cultivo = Trigo() })
                agregarParcela(Parcela().apply { cultivo = Soja() })
            }
            agricultor.cultivos().size shouldBe 2
        }
    }

    describe("parcelas subutilizadas") {
        val agricultor = Agricultor()
        it("si no hay parcelas indica que ninguna está subutilizada") {
            agricultor.algunaParcelaSubutilizada() shouldBe false
        }
        it("si no hay parcelas subutilizadas devuelve falso") {
            agricultor.apply {
                agregarParcela(Parcela().apply { cantidadCultivada = 40; tamanio = 60})
                agregarParcela(Parcela().apply { cantidadCultivada = 30; tamanio = 40})
                agregarParcela(Parcela().apply { cantidadCultivada = 90; tamanio = 90})
            }
            agricultor.algunaParcelaSubutilizada() shouldBe false
        }
        it("si hay parcelas subutilizadas devuelve verdadero") {
            agricultor.apply {
                agregarParcela(Parcela().apply { cantidadCultivada = 40; tamanio = 60})
                agregarParcela(Parcela().apply { cantidadCultivada = 10; tamanio = 40})
                agregarParcela(Parcela().apply { cantidadCultivada = 90; tamanio = 90})
            }
            agricultor.algunaParcelaSubutilizada() shouldBe true
        }
    }
})