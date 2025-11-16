package cymru.asheiou.countdownsigns

import kotlinx.serialization.Serializable
import org.bukkit.block.Block

@Serializable
data class SignData(
  val x: Int,
  val y: Int,
  val z: Int,
  val world: String,
  val meta: SignMeta
) {

  companion object {
    fun fromBlock(sign: Block, meta: SignMeta): SignData {
      return SignData(sign.x, sign.y, sign.z, sign.world.name, meta)
    }
  }
}