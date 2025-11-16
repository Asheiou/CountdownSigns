package cymru.asheiou.countdownsigns

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class SignReloadCommandExecutor(private val cs: CountdownSigns) : CommandExecutor {
  override fun onCommand(
    sender: CommandSender,
    command: Command,
    label: String,
    args: Array<out String>
  ): Boolean {
    cs.sh.init()
    cs.configManager.loadConfig()
    MessageSender.sendMessage(sender, "Reload complete.")
    return true
  }

}