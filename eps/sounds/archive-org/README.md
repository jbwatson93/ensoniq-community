# Classic Ensoniq EPS sounds from Archive.org

Downloaded October 2, 2026. These are sound disks for the original EPS, not the EPS-16 Plus or ASR-10.

**This folder is an index only.** The disk images, EFE files and the uploaders' text files are not hosted in this repository. Download them from the source links below; `SHA256SUMS.txt` lets you confirm you have the same files, and `disk-contents.txt` lists what each disk holds. The folder names under Contents describe the local layout the downloads were unpacked into.

## Contents

- `who-am-i-1990/`: Who Am I 1990 sound disk in IMG and HFE formats, plus individual EFE files. Contains ECHO BASS, ORCH HITS, HARPSITINE, ERGO DRUMS, an EPS song, and a sound bank.
  Source: https://archive.org/details/EPS-who-am-i-1990
- `carless-whisper/`: Carless Whisper sound disk in IMG and HFE formats, plus individual EFE files. Contains ERGO DRUMS, SUPABAS-2 DX, JJ LA, and a sound bank. The spelling follows the archive item.
  Source: https://archive.org/details/EPS-carless-whisper
- `extra-demos/`: 16 additional IMG sound disks and the uploader's description.
  Source: https://archive.org/details/eps-extra-demo

## Testing on the EPS

Boot the stock EPS operating system before loading these sounds.

Start with `who-am-i-1990/WhoAmI1990.img`: it includes a stored song, which is useful for checking digital playback independently of keyboard input. Load its sound bank and song using the stock OS. The included TXT file gives the original disk contents.

IMG files are complete 800 KiB raw floppy images; HFE files are alternate floppy images. EFE files are individual exported instruments, banks, or songs, not complete bootable disks.

## Verification

All 33 downloaded files matched the sizes and SHA-1 hashes supplied by Archive.org. `SHA256SUMS.txt` records local SHA-256 hashes. All 18 IMG files were successfully read by EpsLin Neo and contain instrument entries. Their listings are saved in `disk-contents.txt`.

The original Archive.org metadata is retained in the three `*.metadata.json` files. Hardware update, 2026-10-03: the Who Am I sequence was loaded and played on an original EPS from a Gotek. The other 17 disks have not been individually confirmed on hardware. Availability in an archive does not establish a redistribution license; refer to the source items for any supplied rights information.
