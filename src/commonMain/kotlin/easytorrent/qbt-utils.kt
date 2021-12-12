package easytorrent

import drewcarlson.qbittorrent.models.Torrent
import kotlin.math.*

private const val KB = 1024.0
private const val RF = 100.0

private fun Long.toHumanReadableSize(): String {
    if (this == 0L) return "0 B"
    val e = floor(ln(this.toDouble()) / ln(KB))
    val sizeString = ((this / KB.pow(e)) * RF).roundToInt() / RF
    return "$sizeString ${" KMGTP"[e.toInt()]}B"
}

val Torrent.totalSizeString: String
    get() = totalSize.toHumanReadableSize()

val Torrent.dlspeedString: String
    get() = "${dlspeed.toHumanReadableSize()}/s"

val Torrent.uploadSpeedString: String
    get() = "${uploadSpeed.toHumanReadableSize()}/s"

val Torrent.stateIcon: String
    get() = when (state) {
        Torrent.State.UPLOADING,
        Torrent.State.MOVING,
        Torrent.State.DOWNLOADING,
        Torrent.State.STALLED_UP,
        Torrent.State.CHECKING_UP,
        Torrent.State.FORCED_UP,
        Torrent.State.ALLOCATING,
        Torrent.State.CHECKING_DL,
        Torrent.State.FORCED_DL,
        Torrent.State.CHECKING_RESUME_DATA -> "▶"

        Torrent.State.ERROR,
        Torrent.State.MISSING_FILES,
        Torrent.State.PAUSED_UP,
        Torrent.State.QUEUED_UP,
        Torrent.State.META_DL,
        Torrent.State.PAUSED_DL,
        Torrent.State.STALLED_DL,
        Torrent.State.UNKNOWN -> "⏸"
    }