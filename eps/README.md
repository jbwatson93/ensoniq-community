# Ensoniq EPS

Research and reference index for the original EPS (and EPS-M), not the EPS-16
Plus. The OS image and the main manuals are hosted in this repository; the
boot ROM and sound disks are linked only. Every row gives the place the file
came from. Links were checked on 2026-10-04.

"Verified" means the download was compared, by checksum, with the copy used
for the [findings](findings/README.md). "Reachable" means only that the link
responded.

## Boot ROM and operating system

| What | Source | Status |
| --- | --- | --- |
| Boot ROM, `eps-h.bin` + `eps-l.bin` | [`eps.zip` on Rainer Buchty's Ensoniq page](http://www.buchty.net/ensoniq/files/roms/eps.zip) (listed there as "EPS16 OS", under [buchty.net/ensoniq](http://www.buchty.net/ensoniq/)) | Verified; linked only, see [roms](roms/README.md) |
| OS 2.49 disk image | [Archive.org: ENSONIQ EPS OS V2.49 Image Disk](https://archive.org/details/eps-os-v-249) | Verified; hosted in [os/2.49](os/2.49/README.md) |

## Manuals, datasheets and schematics

| What | Source | Status |
| --- | --- | --- |
| EPS Technical Manual | [Google Drive](https://drive.google.com/file/d/1v7JlYQHTrD3Hf7Tw7OLxh1jksgm066g2/view), via the R-Massive index | Verified; hosted in [docs](docs/README.md) |
| ES5700 EPS gate array specification | [Google Drive](https://drive.google.com/file/d/1njL-XUtfzWzqOeS0kxKHYGy12JLFpmsD/view), via the R-Massive index | Verified; hosted in [docs](docs/README.md) |
| ES5504 DOC II datasheet (ICS1399 rebadge) | [R-Massive](https://audiopro.r-massive.com/Ensoniq/Schematics/ICS1399%20%28rebadged%205504%20DOC%20II%29.pdf) | Verified; hosted in [docs](docs/README.md) |
| Ensoniq floppy diskette formats, Gary Giebler | [deepsonic.ch](https://www.deepsonic.ch/deep/docs_manuals/ensoniq_floppy_diskette_formats.pdf) | Verified; hosted in [docs](docs/README.md) |
| EPS schematics | [buchty.net](http://www.buchty.net/ensoniq/files/schematics/eps-sch.pdf) | Reachable |
| ES5700 gate array, VHDL description | [buchty.net](http://www.buchty.net/ensoniq/files/es5700.vhd) | Reachable |
| EPS / EPS-M service manual | [audioservicemanuals.com](https://www.audioservicemanuals.com/e/ensoniq/6128869-ensoniq-eps-epsm-service-manual) | Not checked; the site blocks automated requests |
| EPS musician's manual, remastered | [vartemyev88/ensoniq-eps-classic-remaster](https://github.com/vartemyev88/ensoniq-eps-classic-remaster) | Reachable |
| More EPS manuals | [yalaorg EPS manuals page](https://sites.google.com/site/yalaorg/1-synthesizers-music-audio/ensoniq-eps-manuals) | Reachable |
| Index of Ensoniq technical documents | [R-Massive: Ensoniq technical documents and schematics](https://zine.r-massive.com/ensoniq-technical-documents-and-schematics/) | Reachable |

Checksums for the hosted documents are in [docs](docs/README.md).

## Sound disks

| What | Source | Status |
| --- | --- | --- |
| Who Am I 1990 | [Archive.org](https://archive.org/details/EPS-who-am-i-1990) | Verified; loaded and played on hardware |
| Carless Whisper | [Archive.org](https://archive.org/details/EPS-carless-whisper) | Verified; read with EpsLin Neo only |
| 16 extra demo disks | [Archive.org](https://archive.org/details/eps-extra-demo) | Verified; read with EpsLin Neo only |

The disks are linked only; checksums and per-disk contents are in [sounds/archive-org](sounds/archive-org/README.md).

## Tools

| What | Source |
| --- | --- |
| EpsLin Neo: read and write EPS/EPS-16/ASR disk images and EFE files | [handsthatstrike/EpsLinNeo](https://github.com/handsthatstrike/EpsLinNeo) |
| epstool: EPS-family filesystem and EFE tools | [mikewolak/epstool](https://github.com/mikewolak/epstool) |
| ensoniqfs: filesystem implementation | [thoralt/ensoniqfs](https://github.com/thoralt/ensoniqfs) |
| vgsound_emu: ES5504 emulation source | [cam900/vgsound_emu](https://gitlab.com/cam900/vgsound_emu) |
| FlashFloppy Ensoniq host notes | [Host Platforms wiki](https://github.com/keirf/flashfloppy/wiki/Host-Platforms) |

Only EpsLin Neo and FlashFloppy were used for the findings here; the others are
listed as references and have not been evaluated.

## In this folder

- [findings/](findings/README.md): memory map, ROM kernel, panel protocol, audio capture, Gotek setup, and recon tools.
- [emulation/](emulation/epsfx/README.md): MAME notes and a boot injector.
- [os/2.49/](os/2.49/README.md) and [docs/](docs/README.md): the hosted OS image and manuals, with checksums and sources.
- [roms/](roms/README.md) and [sounds/](sounds/archive-org/README.md): identification, checksums and listings for files that are linked only.
