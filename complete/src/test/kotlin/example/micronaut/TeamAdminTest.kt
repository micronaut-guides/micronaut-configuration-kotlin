package example.micronaut;

import io.kotlintest.shouldBe
import io.kotlintest.specs.BehaviorSpec

class TeamAdminTest : BehaviorSpec({
    given("A Team Admin class") {
        `when`("A team admin is constructed with the Builder") {
            val teamAdmin = TeamAdmin.Builder().withManager("Nirav")
                    .withCoach("Tommy")
                    .withPresident("Mark").build()
            then("the team admin manager is populated") {
                teamAdmin.manager shouldBe "Nirav"
                teamAdmin.coach shouldBe "Tommy"
                teamAdmin.president shouldBe "Mark"
            }
        }
    }
})
