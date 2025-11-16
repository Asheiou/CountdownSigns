package cymru.asheiou.countdownSigns

import cymru.asheiou.countdownSigns.SignHandler.Companion.formatDiff
import org.bukkit.block.Sign
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class SignAddCommandExecutor(private val cs: CountdownSigns) : CommandExecutor {
  override fun onCommand(
    sender: CommandSender,
    command: Command,
    label: String,
    args: Array<out String>
  ): Boolean {
    if (sender !is Player) return true
    if (args.size != 2) return invalidExecution(sender)
    var expiryUnprocessed = args[0]
    if (expiryUnprocessed.length == 10) expiryUnprocessed += "000"
    if (expiryUnprocessed.length != 13) return invalidExecution(sender)
    val expiry = expiryUnprocessed.toLongOrNull() ?: run {
      return invalidExecution(sender)
    }

    var line = args[1].toIntOrNull() ?: run {
      return invalidExecution(sender)
    }
    if (line < 1 || line > 4) return invalidExecution(sender)
    line -= 1

    val block = sender.getTargetBlock(null, 10)
    if (block.state !is Sign) {
      MessageSender.sendMessage(sender, "You're not looking at a sign.")
      return true
    }
    if (cs.sh.signs.keys.contains(block.location)) {
      MessageSender.sendMessage(
        sender,
        "That sign already exists. Please delete and re-add it if you wish to change the expiry."
      )
      return true
    }

    cs.sh.signs.put(block.location, SignMeta(expiry, line))
    cs.sh.saveAll()
    val format = cs.config.getString("format")!!
    val formatted = formatDiff(expiry, format)
    MessageSender.sendMessage(sender, "Sign added. It will expire in $formatted.")
    return true
  }

  fun invalidExecution(sender: Player): Boolean {
    MessageSender.sendMessage(sender, "Invalid usage. Usage:")
    return false
  }
}