package example.micronaut

import io.kotlintest.shouldBe
import io.kotlintest.specs.BehaviorSpec
import io.micronaut.context.ApplicationContext
import io.micronaut.test.annotation.MicronautTest
import java.util.function.Consumer

@MicronautTest
class TeamConfigurationBuilderTest : BehaviorSpec({
    given("an application context started with configuration") {
        val names = listOf("Nirav Assar", "Lionel Messi")
        val items = mapOf("team.name" to "evolution",
                "team.color" to "green",
                "team.team-admin.manager" to "Jerry Jones", // <1>
                "team.team-admin.coach" to "Tommy O'Neill",
                "team.team-admin.president" to "Mark Scanell",
                "team.player-names" to names)
        val ctx = ApplicationContext.run(ApplicationContext::class.java, items) // <1>
        `when`("TeamConfiguration is retrieved from the context") {
            val teamConfiguration = ctx.getBean(TeamConfiguration::class.java)
            then("configuration properties are populated") {
                teamConfiguration.name shouldBe "evolution"
                teamConfiguration.color shouldBe "green"
                teamConfiguration.playerNames!!.size shouldBe names.size
                names.forEach(Consumer { name: String? -> teamConfiguration.playerNames!!.contains(name) shouldBe true })
                teamConfiguration.builder.manager shouldBe "Jerry Jones"
                teamConfiguration.builder.coach shouldBe "Tommy O'Neill"
                teamConfiguration.builder.president shouldBe "Mark Scanell"

                val teamAdmin = teamConfiguration.builder.build() // <2>
                teamAdmin.manager shouldBe "Jerry Jones" // <3>
                teamAdmin.coach shouldBe "Tommy O'Neill"
                teamAdmin.president shouldBe "Mark Scanell"
            }
        }
        ctx.close()
    }
})