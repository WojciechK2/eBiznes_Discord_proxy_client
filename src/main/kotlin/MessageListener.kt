import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.coroutines.runBlocking
import models.Category
import models.Product
import net.dv8tion.jda.api.entities.ChannelType
import net.dv8tion.jda.api.entities.MessageChannel
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class MessageListener : ListenerAdapter(){

    private val helpMessage: String = """
                Hi!
                Asking for products: message me "products",
                Asking for categories: message me "categories"
                Asking for specific product: message me "products <number>"
                Asking for specific category: message me "categories <number>"
            """.trimIndent()

    private val notProperCommand: String = "Do not Understand: type help for help"

    private val privateChannelMessage: String = "Please don't call me this way"

    private val serverUrl: String = System.getenv("SERVER_URL")

    @Override
    override fun onMessageReceived(event: MessageReceivedEvent) {
        super.onMessageReceived(event)

        if(event.author.isBot) return

        if(!event.message.mentions.isMentioned(event.jda.selfUser)) return

        if(event.isFromType(ChannelType.PRIVATE)){
            sendMessage(event.channel,privateChannelMessage)
        }
        else {
            runBlocking {
                val resp = handleRequest(event.message.contentStripped)
                sendMessage(event.channel,resp)
            }
        }
    }

    private fun sendMessage(channel:MessageChannel, message: String){
        channel.sendMessage(message).queue()
    }

    private suspend fun handleRequest(message:String) : String{
        var returnMessage: String = ""

        if(message.contains("products")){
            if(message.any {it.isDigit()}){
                val id = message.filter { it.isDigit() }
                val response: Product = client.get(serverUrl + "products/" + id.toString()).body()
                returnMessage = "Product name: '${response.name}' \n '${response.description}' \n Price: '${response.price}'"
            } else {
                val response: List<Product> = client.get(serverUrl + "products").body()
                response.forEach{
                    returnMessage += it.id
                    returnMessage += " "
                    returnMessage += it.name
                    returnMessage += "\n"
                }
            //parse it nicely before sending
            }
        } else if(message.contains("categories")){
            if(message.any {it.isDigit()}){
                val id = message.filter { it.isDigit() }
                val categoryName: Category = client.get(serverUrl + "/categories/" + id.toString()).body()
                    returnMessage += categoryName.name
                    returnMessage += "\n"
                val response: List<Product> = client.get(serverUrl + "products/category/" + id.toString()).body()
                response.forEach{
                    returnMessage += it.id
                    returnMessage += " "
                    returnMessage += it.name
                    returnMessage += "\n"
                }
            } else {
                val response: List<Category> = client.get(serverUrl + "categories").body()
                response.forEach{
                    returnMessage += it.id
                    returnMessage += " "
                    returnMessage += it.name
                    returnMessage += "\n"
                }
                //parse it nicely before sending
            }
        }
        else if (message.contains("help")){
            returnMessage = helpMessage
        }
        else {
            returnMessage = notProperCommand
        }

        return returnMessage
    }
}