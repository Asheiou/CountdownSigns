package cymru.asheiou.countdownSigns

import kotlinx.serialization.json.Json
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.block.Block
import org.bukkit.block.Sign
import org.bukkit.block.sign.Side
import org.bukkit.scheduler.BukkitRunnable
import java.io.File
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes


class SignHandler(private val cs: CountdownSigns) : BukkitRunnable() {
  val signs: MutableMap<Location, Pair<Long, Int>> = mutableMapOf()
  val path = cs.dataFolder.toString() + File.separator + "signs.json"
  val file = File(path)

  fun init() {
    if (!file.exists()) {
      cs.saveResource("signs.json", true)
      return
    }
    signs.clear()
    val signsJson = Json.decodeFromString<MutableList<SignData>>(file.readText())
    signsJson.forEach {
      val world = Bukkit.getServer().getWorld(it.world) ?: return@forEach
      val block = world.getBlockAt(it.x, it.y, it.z)
      signs.put(block.location, it.expiry to it.line)
    }
  }

  override fun run() {
    val signsToRemove = mutableListOf<Location>()
    signs.forEach { location, data ->
      if (!location.chunk.isLoaded) return@forEach
      if (location.block.state !is Sign) {
        cs.logger.fine("Block at ${location.x}, ${location.y}, ${location.z} is not a sign!")
        return@forEach
      }
      val sign = location.block.state as Sign
      val format = cs.config.getString("format")!!
      val formatted = formatDiff(data.first, format)

      listOf(sign.getSide(Side.FRONT), sign.getSide(Side.BACK)).forEach { side ->
        side.line(data.second, MessageSender.miniMessage.deserialize(formatted))
      }

      sign.update()

      if (data.first < System.currentTimeMillis()) signsToRemove += location

    }
    signsToRemove.forEach {
      signs.remove(it)
    }
  }

  override fun cancel() {
    saveAll()
  }

  fun saveAll() {
    val signList = mutableListOf<SignData>()
    signs.forEach { (location, data) ->
      signList.add(SignData.fromBlock(location.block, data.first, data.second))
    }
    val signsJson = Json.encodeToString(signList)
    file.writeText(signsJson)
  }

  companion object {
    fun formatDiff(expiry: Long, format: String): String {
      val now = System.currentTimeMillis()
      var diff: Duration = (expiry - now).milliseconds


      if (diff.isNegative()) diff = Duration.ZERO

      var totalMinutes = diff.inWholeMinutes
      val leftover = diff - totalMinutes.minutes
      if (leftover.inWholeMilliseconds > 0) {
        totalMinutes += 1
      }

      val days = totalMinutes / (60 * 24)
      val hours = (totalMinutes % (60 * 24)) / 60
      val minutes = totalMinutes % 60

      return format
        .replace("{d}", days.toString())
        .replace("{h}", hours.toString())
        .replace("{m}", minutes.toString())
    }
  }
}