package ar.edu.unsam.algo2.agricultores

import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class TestCultivos : DescribeSpec({
    isolationMode = IsolationMode.InstancePerTest

    describe("tests de soja") {
        val soja = Soja()
        it("su costo es fijo") {
            val parcela = Parcela()
            soja.costo(parcela) shouldBe 10
        }
        it("su precio de venta para una parcela chica está en base al costo por hectárea") {
            val parcelaChica = Parcela()
            soja.precioVenta(parcelaChica) shouldBe 100
        }
        it("su precio de venta para una parcela grande tiene una retención") {
            val parcelaChica = Parcela().apply {
                tamanio = 1050
            }
            soja.precioVenta(parcelaChica) shouldBe 90
        }
    }

    describe("tests de soja transgénica") {
        val sojaTransgenica = SojaTransgenica()
        it("su costo es fijo") {
            val parcela = Parcela()
            sojaTransgenica.costo(parcela) shouldBe 10
        }
        it("su precio de venta para una parcela es la mitad si la soja sufre mutaciones") {
            val parcela = Parcela()
            sojaTransgenica.precioVenta(parcela) shouldBe 50
        }
        it("su precio de venta para una parcela es la misma que la soja si no sufre mutaciones") {
            val parcela = Parcela().apply {
                tamanio = 2000
            }
            sojaTransgenica.puedeSufrirMutaciones = false
            sojaTransgenica.precioVenta(parcela) shouldBe 90
        }
    }

    describe("tests de trigo") {
        val trigo = Trigo()
        it("su costo si la parcela es chica depende del tamaño") {
            val parcela = Parcela().apply {
                tamanio = 20
            }
            trigo.costo(parcela) shouldBe 100
        }
        it("su costo si la parcela es grande tiene un tope") {
            val parcela = Parcela()
            trigo.costo(parcela) shouldBe 500
        }
        it("su precio de venta para una parcela sin conservantes es un valor fijo") {
            val parcela = Parcela()
            trigo.precioVenta(parcela) shouldBe 20
        }
        it("su precio de venta para una parcela es afectado por sus conservantes") {
            val parcela = Parcela()
            trigo.agregarConservante(Conservante(4.0))
            trigo.agregarConservante(Conservante(3.0))
            trigo.precioVenta(parcela) shouldBe 13
        }
    }

    describe("tests de sorgo") {
        val trigo = Sorgo()
        it("su costo si la parcela es chica multiplica por un valor mayor") {
            val parcela = Parcela().apply {
                tamanio = 20
            }
            trigo.costo(parcela) shouldBe 60
        }
        it("su costo si la parcela es grande multiplica por un valor menor") {
            val parcela = Parcela()
            trigo.costo(parcela) shouldBe 1000
        }
        it("su precio de venta es fijo") {
            val parcela = Parcela()
            trigo.precioVenta(parcela) shouldBe 20
        }
    }
})