easy-torrent
===

A toy program to find and manage torrents with [qBittorrent](https://github.com/qbittorrent/qBittorrent/).

### Libs

This project exists only for testing the following Kotlin libraries with the new Kotlin/Native [memory manager](https://github.com/JetBrains/kotlin/blob/master/kotlin-native/NEW_MM.md).

- [kotlinx.coroutines](https://github.com/Kotlin/kotlinx.coroutines)
- [ktor](https://github.com/ktorio/ktor)
- [mobius.kt](https://github.com/DrewCarlson/mobius.kt)

### Notes

Windows setup:
- Install [Msys2](https://www.msys2.org/#installation)
- Install curl: `pacman -S mingw-w64-x86_64-curl`
- (Optional) Add `C:\msys64\mingw64` to your `PATH` system variable