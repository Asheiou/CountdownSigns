package cymru.asheiou.countdownSigns

import kotlinx.serialization.Serializable

@Serializable
data class SignMeta(
  val expiry: Long = 0L,
  val line: Int = 1
)