package proven.arnau.cat

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.bson.Document
import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.ServerApi
import com.mongodb.ServerApiVersion
import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kotlinx.coroutines.runBlocking

import compose_desktop.composeapp.generated.resources.Res
import compose_desktop.composeapp.generated.resources.compose_multiplatform
import kotlinx.coroutines.flow.toList
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class Dog(
    @BsonId
    val id: ObjectId,
    val name: String
)

@Composable
@Preview
fun App() {
    val database = remember { connectToMongoDB() }
    val collection = remember { database.getCollection<Dog>("dogs") }
    LaunchedEffect("d") {
        //collection.insertMany(listOf(Dog(ObjectId(), "Rex"), Dog(ObjectId(), "Lassie"), Dog(ObjectId(), "Pluto"), Dog(ObjectId(), "Goofy")))
        val dogs = collection.find().toList()
    }
    val dogs by produceState(initialValue = emptyList<Dog>(), key1 = collection) {
        value = collection.find().toList()
    }
    MaterialTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            dogs.forEach { dog ->
                Text(
                    text = dog.name
                )
            }
        }
    }
}

fun connectToMongoDB(): MongoDatabase {
    // Replace the placeholders with your credentials and hostname
    val connectionString = "mongodb+srv://arnaunl3:ar1732nu@accesadades.f1cvg.mongodb.net/?retryWrites=true&w=majority&appName=Accesadades"
    val serverApi = ServerApi.builder()
        .version(ServerApiVersion.V1)
        .build()
    val mongoClientSettings = MongoClientSettings.builder()
        .applyConnectionString(ConnectionString(connectionString))
        .serverApi(serverApi)
        .build()
    // Create a new client and connect to the server
    MongoClient.create(mongoClientSettings).use { mongoClient ->
        val mongoClient = MongoClient.create(mongoClientSettings)
        val database = mongoClient.getDatabase("accesadades")
        runBlocking {
            database.runCommand(Document("ping", 1))
        }
        println("Pinged your deployment. You successfully connected to MongoDB!")
        return database
    }
}