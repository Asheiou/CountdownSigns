package cymru.asheiou.countdownsigns

import cymru.asheiou.configmanager.ConfigManager
import org.bukkit.plugin.java.JavaPlugin

class CountdownSigns : JavaPlugin() {
  var configManager = ConfigManager(this, false)
  lateinit var sh: SignHandler

  override fun onEnable() {
    logger.info("Enabling...")
    val cmReturn = configManager.loadConfig()
    if (cmReturn[0] == -1)
      logger.info("Config was missing or unreadable, generating a new one...")
    else
      logger.info("Config loaded. ${cmReturn[0]} lines added, ${cmReturn[1]} lines removed.")
    sh = SignHandler(this)
    sh.init()
    sh.runTaskTimer(this, 100, 100)
    getCommand("signadd")?.setExecutor(SignAddCommandExecutor(this))
    getCommand("signremove")?.setExecutor(SignRemoveCommandExecutor(this))
    getCommand("signreload")?.setExecutor(SignReloadCommandExecutor(this))
    logger.info("Done!")
  }

  override fun onDisable() {
    sh.saveAll()
  }
}
