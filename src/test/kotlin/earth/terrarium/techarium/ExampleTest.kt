package earth.terrarium.techarium

import net.minecraft.network.chat.Component
import net.minecraft.server.MinecraftServer
import net.neoforged.testframework.junit.EphemeralTestServerProvider
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(EphemeralTestServerProvider::class)
class ExampleTest {

    @Test
    fun `example test`(server: MinecraftServer) {
        server.sendSystemMessage(Component.literal("Hello, world!"))
    }
}