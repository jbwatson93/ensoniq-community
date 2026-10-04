# Original EPS boot ROM identification

**Applies to:** original EPS. The dump is not hosted here; this note
identifies the one used by the [EPS findings](../findings/README.md) and says
where to get it.

## Source

[`eps.zip`](http://www.buchty.net/ensoniq/files/roms/eps.zip) from
[Rainer Buchty's Ensoniq page](http://www.buchty.net/ensoniq/) contains
`eps-h.bin` and `eps-l.bin`. Downloaded on 2026-10-04, both files matched the
SHA-1 values below. The page lists the archive as "EPS16 OS (version unknown,
2x 27256)", but the contents are the files MAME uses for the original EPS.

## Files

| File | Size | CRC32 | SHA-1 | SHA-256 |
| --- | --- | --- | --- | --- |
| `eps-h.bin` | 32768 | `d8747420` | `460597751386eb5f08465699b61381c4acd78065` | `e19cebb5269fd53616e049743bba1d832714f0300ed84bc446dc74bc79dd336c` |
| `eps-l.bin` | 32768 | `382beac1` | `110e31edb03fcf7bbde3e17423b21929e5b32db2` | `5e0d21ce96434167067b1650c1db27a4dc056053e5a10689ad3b0b41de2af975` |
| `bootrom.bin` (interleaved) | 65536 | `64cc5548` | `7aa9ef21ca77b885c21ea2d71632fdf60eec50bc` | `d33f15551b178b77c491f4d89e1a63008a2017d0408658a8888b237f72af77d8` |

The two halves match the names, sizes, CRC32 and SHA-1 that MAME 0.289 lists for
its `eps` driver (`mame -listroms eps`).

## Interleaving

`bootrom.bin` is the 64 KiB image as the 68000 sees it at `C00000-C0FFFF`.
`eps-h.bin` supplies the even bytes (the high byte of each word) and
`eps-l.bin` the odd bytes:

```python
h = open("eps-h.bin", "rb").read()
l = open("eps-l.bin", "rb").read()
open("bootrom.bin", "wb").write(bytes(b for pair in zip(h, l) for b in pair))
```

The result starts with the reset vectors `00001548 00C0BE38` (initial SP, then
PC), which is a quick check that the halves are in the right order.

## Not recorded

The ROM version label and the chip positions on the board were not captured
in the project notes, and the source page gives the version as unknown. Nor do
the notes record whether the ROM in the physical EPS used for hardware tests
matches this image.
