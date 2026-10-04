# Classic EPS ROM kernel and custom-OS boot

**Finding:** the classic boot ROM contains scheduler and message services that
a replacement disk OS can use. A custom OS using them boots on physical EPS
hardware. **Software:** analyzed classic ROM and disk OS 2.49.

## Kernel interface recovered from disassembly

The ROM kernel entry is `C0729C`. Interface words contain pointers; values at
or above `8000` are sign-extended into high RAM.

| Low-RAM words | Meaning |
| --- | --- |
| `014A/014C` | Region to clear |
| `014E/0150` | Task-structure table bounds; records are `1A` bytes |
| `0152/0154` | Task-registration list bounds; pairs of word pointers |
| `0156` | First initialization hook |
| `0158` | Second initialization hook |

Task registration installs a user-stack pointer and task entry. OS 2.49 registers
four tasks. ROM context switching at `C07342` saves/restores USP and registers
`d2-d7/a2-a6`; the scheduling loop is at `C07396`.

| Service | Observed role | ROM handler |
| --- | --- | --- |
| `trap #6` | Wait for a message | `C074DE` |
| `trap #7` | Wait for countdown completion in current firmware | `C07510` |
| `trap #8` | Set a delay countdown; does not itself block | `C07536` |
| `trap #9` | Enqueue a message | `C07544` |

The message service uses `a1` for the destination task structure and `a5` for
the message. Recovered task fields include queue pointers at `+0E/+10`, waiting
state at `+2` bit 7 and a waiting slot at `+0A`. This is a partial recovered API,
not a complete specification. Current task code delays with `trap #8` followed
by `trap #7`; historical descriptions calling #8 a blocking sleep are incomplete.

## Hardware-confirmed boot fix: ERROR 129

Early custom builds placed executable hooks around `22xx`. Supervisor instruction
fetches selected the low ROM overlay instead of that RAM code. Moving executable
hooks, task code and interrupt stubs to `FFFF8000+` fixed the boot failure.
The diagnostic displayed `EPSFX HIGH RAM BOOT OK` on physical hardware on
2026-10-01. The analyzed address-error entry `C076A8` sets error code `81`
(decimal 129); the original physical exception frame was not captured.

Two additional deterministic layout bugs were found in earlier builders:

- A task list at `0170` overlapped kernel bookkeeping. Initialization clears
  bytes `0170-0172`, corrupting the intended stack and entry words.
- A task list at `0160` overlapped the DUART dispatch pointer at `0162`.

These were real build defects, but they do not independently establish the
precise fault instruction for every historical hardware failure.

## Contracts for a replacement OS

- Reserve kernel variables and interrupt dispatch pointers before allocating data.
- Supply actual initialized table records. Equal table bounds do not skip the
  first record in the recovered post-tested loops.
- Use a valid supervisor stack and a separate user stack.
- Initialization hooks called by JSR return with RTS; interrupt stubs use RTE.
- Coordinate ownership of DUART receive data, including display acknowledgements.

Current builder choices include task registration at `FFBE00`, an RTS second
hook at `FFBE04`, a dummy task-table record at `FFCBC0`, and user stack pointer
`FFFFCF00`. These are example allocations from this build, not mandatory addresses.

## Interrupt identities

`C0937C` services the sound chip at `200000`; it is not the DUART handler.
`C0789A` dispatches DUART service through pointers `0162`, `0166`, `016A` and
the timer path. `C076FA` is the ROM timer service. Earlier recon logs contain
superseded interrupt labels; use these corrected identities.

## Sources

Source project: `codex-findings.md`, `RECON.md`, `firmware/epsfx_thru.s`,
`tools/build_epsfx_thru.py`; extracted `work/bootrom.bin` and OS 2.49 disassembly.
Do not use these routine addresses in an EPS-16+ ROM without checking its binary.
