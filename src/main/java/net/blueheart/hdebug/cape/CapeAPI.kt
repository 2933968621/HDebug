
package net.blueheart.hdebug.cape

import com.google.gson.JsonParser
import net.blueheart.hdebug.HDebug
import net.blueheart.hdebug.utils.ClientUtils
import net.blueheart.hdebug.utils.MinecraftInstance
import net.blueheart.hdebug.utils.misc.HttpUtils
import net.minecraft.client.renderer.IImageBuffer
import net.minecraft.client.renderer.ThreadDownloadImageData
import net.minecraft.util.ResourceLocation
import java.awt.image.BufferedImage
import java.util.*
import kotlin.collections.HashMap

object CapeAPI : MinecraftInstance() {

    // Cape Service
    private var capeService: CapeService? = null

    /**
     * Register cape service
     */
    fun registerCapeService() {
        // Read cape infos from web
        val jsonObject = JsonParser()
                .parse(HttpUtils.get("${HDebug.CLIENT_CLOUD}/capes.json")).asJsonObject
        val serviceType = jsonObject.get("serviceType").asString

        // Setup service type
        when (serviceType.toLowerCase()) {
            "api" -> {
                val url = jsonObject.get("api").asJsonObject.get("url").asString

                capeService = ServiceAPI(url)
                ClientUtils.getLogger().info("Registered $url as '$serviceType' service type.")
            }
            "list" -> {
                val users = HashMap<String, String>()

                for ((key, value) in jsonObject.get("users").asJsonObject.entrySet()) {
                    users[key] = value.asString
                    ClientUtils.getLogger().info("Loaded user cape for '$key'.")
                }

                capeService = ServiceList(users)
                ClientUtils.getLogger().info("Registered '$serviceType' service type.")
            }
        }

        ClientUtils.getLogger().info("Loaded.")
    }

    /**
     * Load cape of user with uuid
     *
     * @param uuid
     * @return cape info
     */
    fun loadCape(uuid: UUID): CapeInfo? {
        // Get url of cape from cape service
        val url = (capeService ?: return null).getCape(uuid) ?: return null

        // Load cape
        val resourceLocation = ResourceLocation("capes/%s.png".format(uuid.toString()))
        val capeInfo = CapeInfo(resourceLocation)
        val threadDownloadImageData = ThreadDownloadImageData(null, url, null, object : IImageBuffer {

            override fun parseUserSkin(image: BufferedImage): BufferedImage {
                return image
            }

            override fun skinAvailable() {
                capeInfo.isCapeAvailable = true
            }

        })

        mc.textureManager.loadTexture(resourceLocation, threadDownloadImageData)

        return capeInfo
    }

    /**
     * Check if cape service is available
     *
     * @return capeservice status
     */
    fun hasCapeService() = capeService != null
}

data class CapeInfo(val resourceLocation: ResourceLocation, var isCapeAvailable: Boolean = false)