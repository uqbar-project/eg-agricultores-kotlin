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
})