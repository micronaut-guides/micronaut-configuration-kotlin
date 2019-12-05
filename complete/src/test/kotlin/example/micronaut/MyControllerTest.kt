package example.micronaut

import io.kotlintest.shouldBe
import io.kotlintest.specs.BehaviorSpec
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.runtime.server.EmbeddedServer
import io.micronaut.test.annotation.MicronautTest
import java.util.function.Consumer

@MicronautTest
class MyControllerTest(private val server: EmbeddedServer,
                       @Client("/") var client: HttpClient): BehaviorSpec({
    given("the math service") {
        `when`("the fetching october headlines is called the first time") {
            val expectedPlayers = listOf("Mason Rudolph", "James Connor")
            val teamConfiguration = client.toBlocking().retrieve(HttpRequest.GET<Any>("/my/team"), TeamConfiguration::class.java)
            then("the cache is used") {
                teamConfiguration.name shouldBe "Steelers"
                teamConfiguration.color shouldBe "Black"
                expectedPlayers.size shouldBe teamConfiguration.playerNames!!.size
                expectedPlayers.forEach(Consumer { name: String? -> teamConfiguration.playerNames!!.contains(name) shouldBe true })
            }
        }
        `when`("the fetching october headlines is called the first time") {
            val conf = client.toBlocking().retrieve(HttpRequest.GET<Any>("/my/stadium"), StadiumConfiguration::class.java)
            then("the cache is used") {
                conf.name shouldBe "Pittsburgh"
                conf.size shouldBe "35000"
            }
        }
    }
})