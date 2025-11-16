package cymru.asheiou.countdownsigns

import org.bukkit.block.Sign
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class SignRemoveCommandExecutor(private val cs: CountdownSigns) : CommandExecutor {
  override fun onCommand(
    sender: CommandSender,
    command: Command,
    label: String,
    args: Array<out String>
  ): Boolean {
    if (sender !is Player) return true
    val block = sender.getTargetBlock(null, 10)
    if (block.state !is Sign) {
      MessageSender.sendMessage(sender, "You're not looking at a sign.")
      return true
    }
    if (!cs.sh.signs.keys.contains(block.location)) {
      MessageSender.sendMessage(sender, "This sign is not being tracked by the plugin.")
      return true
    }
    cs.sh.signs.remove(block.location)
    cs.sh.saveAll()
    MessageSender.sendMessage(sender, "Sign removed.")
    return true
  }
}