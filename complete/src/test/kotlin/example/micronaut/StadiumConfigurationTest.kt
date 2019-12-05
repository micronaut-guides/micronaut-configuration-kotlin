package example.micronaut

import io.kotlintest.shouldBe
import io.kotlintest.specs.BehaviorSpec
import io.micronaut.context.ApplicationContext
import io.micronaut.inject.qualifiers.Qualifiers
import io.micronaut.test.annotation.MicronautTest

@MicronautTest
class StadiumConfigurationTest : BehaviorSpec({

    given("an application context started with configuration") {
        val items = mapOf("stadium.fenway.city" to "Boston", // <1>
                "stadium.fenway.size" to  60000,
                "stadium.wrigley.city" to  "Chicago", // <1>
                "stadium.wrigley.size" to 45000
        )
        val ctx = ApplicationContext.run(ApplicationContext::class.java, items)

        `when`("StadiumConfiguration are retrieved with bean qualifier") {
            val fenwayConfiguration = ctx.getBean(StadiumConfiguration::class.java, Qualifiers.byName("fenway"))  // <2>
            val wrigleyConfiguration = ctx.getBean(StadiumConfiguration::class.java, Qualifiers.byName("wrigley")) // <2>

            then("configuration properties are populated") {
                fenwayConfiguration.name shouldBe "fenway"
                fenwayConfiguration.size shouldBe 60000
                wrigleyConfiguration.name shouldBe "wrigley"
                wrigleyConfiguration.size shouldBe 45000
            }
        }

        ctx.close()
    }
})
