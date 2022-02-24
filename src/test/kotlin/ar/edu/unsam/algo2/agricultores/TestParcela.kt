package ar.edu.unsam.algo2.agricultores

import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class TestCultivos : DescribeSpec({
    isolationMode = IsolationMode.InstancePerTest

    describe("tests de soja") {
        val soja = Soja()
        it("el costo de soja es fijo") {
            val parcela = Parcela()
            soja.costo(parcela) shouldBe 10
        }
        it("el precio de venta para una parcela chica está en base al costo por hectárea") {
            val parcelaChica = Parcela()
            soja.precioVenta(parcelaChica) shouldBe 100
        }
        it("el precio de venta para una parcela grande tiene una retención") {
            val parcelaChica = Parcela().apply {
                tamanio = 1050
            }
            soja.precioVenta(parcelaChica) shouldBe 90
        }
    }

    describe("tests de soja transgénica") {
        val sojaTransgenica = SojaTransgenica()
        it("el costo de soja es fijo") {
            val parcela = Parcela()
            sojaTransgenica.costo(parcela) shouldBe 10
        }
        it("el precio de venta para una parcela es la mitad si la soja sufre mutaciones") {
            val parcela = Parcela()
            sojaTransgenica.precioVenta(parcela) shouldBe 50
        }
        it("el precio de venta para una parcela es la misma que la soja si no sufre mutaciones") {
            val parcelaChica = Parcela()
            sojaTransgenica.puedeSufrirMutaciones = false
            sojaTransgenica.precioVenta(parcelaChica) shouldBe 100
        }
    }
})