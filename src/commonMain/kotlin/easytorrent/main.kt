package easytorrent

import com.github.ajalt.mordant.table.table
import com.github.ajalt.mordant.terminal.Terminal
import drewcarlson.qbittorrent.QBittorrentClient
import io.ktor.client.*
import io.ktor.client.engine.curl.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kt.mobius.First.Companion.first
import kt.mobius.flow.FlowMobius
import platform.posix.system

fun main() = runBlocking {
    val http = HttpClient(Curl)
    //val ts = TorrentSearch(httpClient = http)
    val qbt = QBittorrentClient(
        baseUrl = "http://localhost:9090",
        httpClient = http,
    )
    val term = Terminal()
    term.cursor.hide()

    val loop = FlowMobius.loop(AppUpdate, createAppHandler(qbt))
        .init { model -> first(model, AppEffect.LoadTorrents()) }
        .startFrom(AppModel())

    loop.observe { model ->
        if (Platform.osFamily == OsFamily.WINDOWS) system("cls") else system("clear")
        term.println(table {
            header {
                row("Name", "Size", "Status", "Seeds", "Peers", "Down Speed", "Up Speed", "Ratio")
            }
            body {
                model.torrents.forEach { torrent ->
                    row {
                        cell("${torrent.stateIcon} ${torrent.name}")
                        cell(torrent.totalSizeString)
                        cell(torrent.state.name.lowercase().replaceFirstChar(Char::uppercaseChar))
                        cell("${torrent.connectedSeeds} (${torrent.seedsInSwarm})")
                        cell("${torrent.connectedLeechers} (${torrent.leechersInSwarm})")
                        cell(torrent.dlspeedString)
                        cell(torrent.uploadSpeedString)
                        cell(torrent.ratio)
                    }
                }
            }
        })
    }
    while (true) {
        term.info.updateTerminalSize()
        delay(1000)
    }
}
