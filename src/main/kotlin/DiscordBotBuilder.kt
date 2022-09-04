import net.dv8tion.jda.api.JDABuilder

fun discordBotBuilder(token: String, messageListener: MessageListener) : JDABuilder{
    val aJDABuilder = JDABuilder.createDefault(token)

    //Listener for user commands
    aJDABuilder.addEventListeners(messageListener)
    aJDABuilder.build()
    return aJDABuilder
}