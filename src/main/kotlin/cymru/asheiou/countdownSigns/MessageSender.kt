package cymru.asheiou.countdownSigns

import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage

object MessageSender {
  val miniMessage = MiniMessage.miniMessage()
  private const val PREFIX = "<aqua>[CS]<reset> "

  @Suppress("unused")
  fun sendMessage(audience: Audience, message: Component, prefix: Boolean = true) {
    sendMessage(audience, miniMessage.serialize(message), prefix)
  }

  fun sendMessage(audience: Audience, message: String, prefix: Boolean = true) {
    val toSend = if (prefix) (PREFIX + message) else message
    audience.sendMessage(miniMessage.deserialize(toSend))
  }
}