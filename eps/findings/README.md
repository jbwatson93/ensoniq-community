# Original Ensoniq EPS findings

Research on the classic EPS, contributed from the EPSFX custom-OS project.
These notes describe the original EPS, not the EPS-16 Plus. Snapshot prepared
2026-10-04; hardware results are recorded through 2026-10-03.

## Findings

- [Memory map and disk boot](memory-map-and-disk-boot.md)
- [ROM kernel and custom-OS boot](rom-kernel-and-custom-os-boot.md)
- [Panel serial communication](panel-serial-communication.md)
- [Audio capture, playback and live streaming](audio-capture-and-live-streaming.md)
- [Gotek and FlashFloppy setup](gotek-flashfloppy-setup.md)

## Evidence and scope

The research uses a 64 KiB interleaved classic EPS boot ROM, stock disk OS 2.49,
68000 disassembly, custom assembly firmware, MAME harnesses and tests on an
original EPS with a Gotek/FlashFloppy drive. Exact physical board revision,
installed expansions and ROM version label were not captured in the supplied
project notes. Do not assume those details when reproducing results.

Each finding separates hardware observations, software/disassembly evidence,
emulator results and unresolved questions. Register sequences that worked in
this configuration are not universal device specifications.

No third-party ROMs, stock OS images, sound libraries or the independently
authored EPS-16+ bible are included in this contribution. The bible's author
has not supplied publication permission in this project.

## Source provenance

Source project: `eps-os-mod` / EPSFX. Source paths below identify local research
artifacts; they are not links to files in this repository:

- `RECON.md`: chronological ROM/OS research, with some superseded assumptions.
- `codex-findings.md`: ERROR 129 investigation and high-RAM boot fix.
- `firmware/epsfx_thru.s`: current implementation.
- `tools/build_epsfx_thru.py`: custom image layout and round-trip validation.
- `firmware/README.md`, `firmware/THRU-TEST.md`, `PLAN.md`: current evidence/status.
- `tools/mame_inject.py`, `tools/mame_thru_test.lua`,
  `tools/mame_coexist_test.lua`: emulator injection and test harnesses.

The notes here curate the current conclusions instead of copying the entire
chronological log. Emulator setup and harness limitations are documented in
[the EPSFX emulator notes](../emulation/epsfx/README.md).
The ROM and stock OS used are identified by checksum, with download sources,
in [eps/roms](../roms/README.md) and [eps/os/2.49](../os/2.49/README.md); the
[EPS index](../README.md) lists every source link.
The EPSFX source, build scripts and firmware images are not published here.

## Recon tools

Small helpers used for the disassembly work, in [tools/](tools/):

- [`osmap.py`](tools/osmap.py) converts between RAM addresses and OS 2.49 file
  offsets using the boot ROM's scatter-load map.
- [`disasm.py`](tools/disasm.py) is a 68000 disassembly helper for the boot ROM
  and OS (needs the Python `capstone` package).
- [`EpsSetup.java`](tools/EpsSetup.java) is a Ghidra headless script that maps
  the OS 2.49 segments over the boot ROM, seeds entry points and exports a
  function list and decompilation. Ghidra 12.1.4 was used.

All three need a ROM or OS image that you supply.
