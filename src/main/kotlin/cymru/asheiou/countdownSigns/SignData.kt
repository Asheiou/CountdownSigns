package cymru.asheiou.countdownSigns

import kotlinx.serialization.Serializable
import org.bukkit.block.Block
import org.bukkit.block.Sign

@Serializable
data class SignData(
  val x: Int,
  val y: Int,
  val z: Int,
  val world: String,
  val expiry: Long
) {
  companion object {
    fun fromBlock(sign: Block, expiry: Long): SignData {
      return SignData(sign.x, sign.y, sign.z, sign.world.name, expiry)
    }
  }
}