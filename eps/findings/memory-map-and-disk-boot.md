# Classic EPS memory map and disk boot

**Model:** original EPS. **Software:** classic boot ROM (64 KiB interleaved),
disk OS 2.49. **Evidence:** ROM/OS disassembly; custom OS boot verified on hardware.
Exact ROM version label and board revision remain undocumented.

## Processor-visible map

| Address | Role | Evidence/qualification |
| --- | --- | --- |
| `000000-007FFF` | Lower 32 KiB system RAM; supervisor instruction fetch selects ROM overlay | Disassembly/emulator investigation; high-RAM fix boots on hardware |
| `FF0000-FF7FFF` | Unconditional lower-RAM alias | Emulator injector writes/readback verified |
| `200000-20001F` | ES5504 DOC II | Classic hardware identification; working audio register programming |
| `240000-2400FF` | 68450 DMA controller region | Project research map; DMA streaming not implemented |
| `280000-28001F` | MC68681 DUART | ROM/OS and working panel/timer implementation |
| `2C0000-2C0007` | WD1772 floppy registers, odd byte lane | ROM loader analysis |
| `300000+` | WD33C93 SCSI region | Research map; SCSI behavior not validated by this contribution |
| `400000-5FFFFF` | Internal sound RAM window | Capacity/aliasing dependent |
| `600000-7FFFFF` | External sound RAM window | Expansion/capacity dependent |
| `C00000-C0FFFF` | Boot ROM | Extracted 64 KiB image and disassembly |
| `FF8000-FFFFFF` | Upper 32 KiB system RAM | Hardware-confirmed custom executable placement |

68000 word-sized absolute addresses at or above `8000` sign-extend, so software
often writes `FFFF8000` where the 24-bit bus sees `FF8000`.

## Boot and OS scatter loading

The analyzed reset vectors are SP=`00001548`, PC=`00C0BE38`. ROM initializes
hardware, finds an OS entry in the disk directory and loads its scattered
segments before entering the ROM kernel at `C0729C`.

Offsets below refer to the OS payload after the 512-byte EFE header:

| Disk blocks | Payload offset | Destination | Length |
| --- | --- | --- | --- |
| `0F` | `00000` | `000000` | `0200` |
| `10-3E` | `00200` | `002200` | `5E00` |
| `3F-7E` | `06000` | `FF8000` | `8000` |
| `7F-AE` | `0E000` | Not loaded by this boot path | `6000` |
| `AF-B4` | `14000` | `001600` | `0C00` |
| `B5` | `14C00` | `000732` directory buffer | `0200` |

The role of the non-boot-loaded region needs further investigation. The final
block is processed by `C0AA82`; early notes speculated about integrity checks.
Do not treat that speculation as a documented checksum contract. Custom builders
preserve the stock final block and ROM-checked version fields.

## Emulator setup

See [EPSFX emulator notes](../emulation/epsfx/README.md) for boot
injection and floppy-emulation limitations.

## Sources and model boundary

Source project: `RECON.md` memory map/loader analysis, `tools/osmap.py`,
`tools/mame_inject.py`, `tools/build_epsfx_thru.py`.
EPS-16+ memory layout, ROM capacity and audio devices differ. In particular, do
not replace this classic sound-RAM map with the 16+'s map based on family name.
