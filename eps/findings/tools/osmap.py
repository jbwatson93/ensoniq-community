#!/usr/bin/env python3
"""Translate EPS RAM addresses <-> OS 2.49 file offsets, per the boot ROM's
scatter loader at $C0BFEE (disk blocks -> RAM):
  OS blk 0   (file 0x00000-0x001FF) -> RAM 0x000000-0x0001FF  (vectors + kernel iface)
  OS blks 1-47   (0x00200-0x05FFF)  -> RAM 0x002200-0x007FFF  (lower RAM)
  OS blks 48-111 (0x06000-0x0DFFF)  -> RAM 0xFF8000-0xFFFFFF  (upper RAM)
  OS blks 112-159(0x0E000-0x13FFF)  -> NOT loaded at boot (overlay, loaded on demand)
  OS blks 160-165(0x14000-0x14BFF)  -> RAM 0x001600-0x0021FF  (lower RAM hole)
  OS blk 166 (0x14C00-0x14DFF)      -> RAM 0x000732 (buffer; version/csum info?)
Word pointers used by the ROM kernel are sign-extended (0x8000-0xFFFF -> 0xFF8000+).
"""
import struct, sys

SEGS = [  # (ram_start, ram_end_excl, file_off)
    (0x000000, 0x000200, 0x00000),
    (0x002200, 0x008000, 0x00200),
    (0xFF8000, 0x1000000, 0x06000),
    (0x001600, 0x002200, 0x14000),
]

def ext(w):  # sign-extend a word pointer to the 24-bit bus address
    return (0xFF0000 | w) if w >= 0x8000 else w

def ram2file(addr):
    for s, e, f in SEGS:
        if s <= addr < e:
            return f + (addr - s)
    return None

def read(os_bin, addr, n):
    off = ram2file(addr)
    if off is None:
        raise ValueError(f"RAM {addr:#x} not boot-loaded (overlay or unmapped)")
    return os_bin[off:off + n]

def word(os_bin, addr):
    return struct.unpack(">H", read(os_bin, addr, 2))[0]

if __name__ == "__main__":
    data = open(sys.argv[1], "rb").read()
    for arg in sys.argv[2:]:
        a = ext(int(arg, 16))
        print(f"RAM {a:#08x} -> file {ram2file(a):#x}: "
              + read(data, a, 16).hex(" "))
