import io.ktor.client.*
import io.ktor.client.engine.cio.*
import net.dv8tion.jda.api.JDABuilder

fun main(args: Array<String>) {
    println("Hello World!")
    println("I am a discord proxy, translating clients queries to backend calls")

    //Here the Bot part
    val messageListener = MessageListener()
    val discord_bot = discordBotBuilder(System.getenv("DISCORD_TOKEN"), messageListener)
}