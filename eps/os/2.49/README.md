# EPS OS 2.49 identification

**Applies to:** original EPS. This folder holds the OS 2.49 disk image
analysed in the [EPS findings](../../findings/README.md), as a raw image and
as an HFE for Gotek use.

## Source

[ENSONIQ EPS OS V2.49 Image Disk](https://archive.org/details/eps-os-v-249) on
Archive.org. Its `EPS_OS_V249.img` has SHA-1
`8f2d050e7c7c2ddd89949fc830ae01b7d98fbf8d` in the item's metadata, which
matches the image here (checked 2026-10-04). `EPS_OS_V249.hfe` is from the
same item and matches its SHA-1 `e2249c01efab1e63f98f68b2f73c9dfad57f084c`.
The item's second image, `EPS_OS_V249_Patches.img`, is not included; see
below.

## Files

| File | Size | SHA-256 | What it is |
| --- | --- | --- | --- |
| [`EPS_OS_V249.img`](EPS_OS_V249.img) | 819200 | `d21757266df3d5a2a3677d94ff7dbed63f61a7119e242bc0358213a60fdaed74` | Raw 800 KiB floppy image |
| [`EPS_OS_V249.hfe`](EPS_OS_V249.hfe) | 2008064 | `46643c688a89cfd9bd5088341657598c450295ab594fb5a4b8f6ef993f004dae` | The same disk as an HFE image |
| `[00][EPS-OS ] EPS-1 O.S.  .efe` | 86016 | `a69ef1f6751b89ed80c1a4d964586b90db418665c9ea99f59675bc3ba71eb7b9` | The OS file exported as an EFE (512-byte header, then the OS) |
| `os249.bin` | 85504 | `01911d8f30e900892fdd1ddbc10bbc047836fb4785de6b87c8b69674edcb79e3` | The EFE with its 512-byte header removed |

The OS is 167 disk blocks of 512 bytes. The boot ROM does not load it
contiguously; the scatter map is in
[memory map and disk boot](../../findings/memory-map-and-disk-boot.md), and
[`osmap.py`](../../findings/tools/osmap.py) converts between RAM addresses and
file offsets.

Stock OS 2.49 boots and loads sounds on the test EPS from a Gotek (see
[Gotek and FlashFloppy setup](../../findings/gotek-flashfloppy-setup.md)).
The EFE and `os249.bin` are not included: extract the EFE from the disk image
with EpsLin Neo, and drop its first 512 bytes to get `os249.bin`.

## Version marker

The OS version is stored as two bytes, major then minor in binary: `02 31` is
2.49 (hex 31 = 49). It appears at offset `1A8` of the OS file (`os249.bin`,
header removed) and at byte 4 of disk block 2, after the four-byte free-block
count. This is an observation from comparing two images, not from the
disassembly.

## The "Patches" image in the same Archive.org item

`EPS_OS_V249_Patches.img` (819200 bytes, SHA-1
`bc3af9d72ee7a8bdc3c4fff521965684f4e45572`) is a sound disk, not a patched
OS. EpsLin Neo lists 28 instruments (FM SLAP BASS, ELEC PIANO, PAN FLOOT and
others, ending with BLANK INST and BLANK MIDI16) and an OS file, with 5 blocks
free.

Despite the file name, its OS is not 2.49: both version markers read `02 28`,
which is 2.40, and 55,236 of the OS file's 85,504 bytes differ from the 2.49
file. It has not been booted on hardware here.
