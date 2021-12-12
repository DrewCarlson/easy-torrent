package easytorrent

import drewcarlson.qbittorrent.QBittorrentClient
import drewcarlson.qbittorrent.QBittorrentException
import drewcarlson.qbittorrent.models.Torrent
import kotlinx.coroutines.delay
import kt.mobius.Next
import kt.mobius.Update
import kt.mobius.flow.subtypeEffectHandler

data class AppModel(
    val torrents: List<Torrent> = emptyList(),
)

sealed class AppEvent {
    data class TorrentsUpdate(val torrents: List<Torrent>) : AppEvent()
}

sealed class AppEffect {
    data class LoadTorrents(val frequency: Long = 1000) : AppEffect()
}

val AppUpdate = Update<AppModel, AppEvent, AppEffect> { model, event ->
    when (event) {
        is AppEvent.TorrentsUpdate -> Next.next(model.copy(torrents = event.torrents))
    }
}

fun createAppHandler(
    qbt: QBittorrentClient
) = subtypeEffectHandler<AppEffect, AppEvent> {
    addLatestValueCollector<AppEffect.LoadTorrents> { effect ->
        while (true) {
            val torrents = try {
                qbt.getTorrents()
            } catch (e: QBittorrentException) {
                // e.printStackTrace()
                emptyList()
            }
            emit(AppEvent.TorrentsUpdate(torrents))
            delay(effect.frequency)
        }
    }
}