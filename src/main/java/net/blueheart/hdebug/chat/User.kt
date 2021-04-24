
package net.blueheart.hdebug.chat

import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * A axochat user
 *
 * @param name of user
 * @param uuid of user
 */
data class User(
    @SerializedName("name")
    val name: String,

    @SerializedName("uuid")
    val uuid: UUID
)