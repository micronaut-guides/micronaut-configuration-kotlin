package example.micronaut

import io.kotlintest.shouldBe
import io.kotlintest.specs.BehaviorSpec
import io.micronaut.context.ApplicationContext
import io.micronaut.context.env.Environment
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.runtime.server.EmbeddedServer
import io.micronaut.test.annotation.MicronautTest
import java.util.function.Consumer

@MicronautTest
class MyControllerTest : BehaviorSpec() {
    init {
        given("the math service") {
            val embeddedServer = autoClose(ApplicationContext.run(EmbeddedServer::class.java, mapOf(), Environment.TEST))

            val client = autoClose(embeddedServer.applicationContext.createBean(HttpClient::class.java, embeddedServer.url))

            `when`("fetching the teams config") {
                val expectedPlayers = listOf("Mason Rudolph", "James Connor")
                val teamConfiguration = client.toBlocking().retrieve(HttpRequest.GET<Any>("/my/team"), TeamConfiguration::class.java)
                then("the config values from application.yml are used") {
                    teamConfiguration.name shouldBe "Steelers"
                    teamConfiguration.color shouldBe "Black"
                    expectedPlayers.size shouldBe teamConfiguration.playerNames!!.size
                    expectedPlayers.forEach(Consumer { name: String? -> teamConfiguration.playerNames!!.contains(name) shouldBe true })
                }
            }
            `when`("fetching the stadium config") {
                val conf = client.toBlocking().retrieve(HttpRequest.GET<Any>("/my/stadium"), StadiumConfiguration::class.java)
                then("the config values from application.yml are used") {
                    conf.city shouldBe "Pittsburgh"
                    conf.size shouldBe 35000
                }
            }
        }
    }
}