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
    if (args.size != 1) return invalidExecution(sender)
    var arg = args[0]
    if (arg.length == 10) arg += "000"
    if (arg.length != 13) return invalidExecution(sender)
    val expiry = arg.toLongOrNull() ?: run {
      return invalidExecution(sender)
    }

    val block = sender.getTargetBlock(null, 10)
    if (block.state !is Sign) {
      MessageSender.sendMessage(sender, "You're not looking at a sign.")
      return true
    }
    if (cs.sh.signs.keys.contains(block)) {
      MessageSender.sendMessage(
        sender,
        "That sign already exists. Please delete and re-add it if you wish to change the expiry."
      )
      return true
    }

    cs.sh.signs.put(block, expiry)
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