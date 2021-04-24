
package net.blueheart.hdebug.features.special

import net.blueheart.hdebug.event.EventTarget
import net.blueheart.hdebug.event.Listenable
import net.blueheart.hdebug.event.SessionEvent
import net.blueheart.hdebug.ui.client.altmanager.sub.GuiDonatorCape
import net.blueheart.hdebug.utils.ClientUtils
import net.blueheart.hdebug.utils.MinecraftInstance
import net.blueheart.hdebug.utils.login.UserUtils
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import org.apache.http.HttpHeaders
import org.apache.http.HttpStatus
import org.apache.http.client.methods.HttpPatch
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.HttpClients
import org.apache.http.message.BasicHeader
import org.json.JSONObject
import kotlin.concurrent.thread

@SideOnly(Side.CLIENT)
class DonatorCape : Listenable, MinecraftInstance() {

    @EventTarget
    fun onSession(event: SessionEvent) {
        if (!GuiDonatorCape.capeEnabled || GuiDonatorCape.transferCode.isEmpty() ||
                !UserUtils.isValidTokenOffline(mc.session.token))
            return

        thread {
            val uuid = mc.session.playerID
            val username = mc.session.username

            val httpClient = HttpClients.createDefault()
            val headers = arrayOf(
                    BasicHeader(HttpHeaders.CONTENT_TYPE, "application/json"),
                    BasicHeader(HttpHeaders.AUTHORIZATION, GuiDonatorCape.transferCode)
            )
            val request = HttpPatch("http://capes.liquidbounce.net/api/v1/cape/self")
            request.setHeaders(headers)

            val body = JSONObject()
            body.put("uuid", uuid)
            request.entity = StringEntity(body.toString())

            val response = httpClient.execute(request)
            val statusCode = response.statusLine.statusCode

            ClientUtils.getLogger().info(
                    if(statusCode == HttpStatus.SC_NO_CONTENT)
                        "[Donator Cape] Successfully transferred cape to $uuid ($username)"
                    else
                        "[Donator Cape] Failed to transfer cape ($statusCode)"
            )
        }
    }

    override fun handleEvents() = true
}