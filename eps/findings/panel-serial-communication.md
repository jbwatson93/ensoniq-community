# Classic EPS panel serial communication

**Evidence:** ROM/OS disassembly plus hardware-confirmed display updates,
button handling and DATA ENTRY direction in the EPSFX project. This is a partial
protocol description for the original EPS; the EPS-16+ panel has different keys.

## DUART connection

The panel/display uses MC68681 channel B. Channel A is the MIDI-side channel;
the current custom firmware disables MIDI interrupts and does not establish a
complete MIDI implementation.

Registers use odd byte addresses: `280000 + 2*n + 1`.

| Address | Use in the current firmware |
| --- | --- |
| `280013` | Channel B status; bit 0 RX ready, bit 2 TX ready |
| `280015` | Channel B command |
| `280017` | Channel B receive/transmit data |
| `28000B` | DUART interrupt mask when written |

## Receive parsing

- `FF` acknowledges transmitted data and marks the transmitter ready.
- `FC` introduces one auxiliary payload byte. Consume that payload before
  interpreting high-valued bytes as standalone control bytes.
- `F7` clears the parser's partial packet state in the current implementation.
- Ordinary event packets contain two bytes. A zero second byte identifies a
  panel event in this implementation; nonzero second bytes are ignored as
  keyboard traffic by the custom panel-control code.
- The first event byte's low seven bits are a raw matrix index; bit 7 indicates
  a press in the implemented parser. Release packets do not trigger audio actions.

Examples used by the streaming parser are `91 00` for RIGHT, `97 00` for STOP,
and `A1 00` for CANCEL. Raw indexes must be translated through the classic ROM
table at `C02032`, rather than confused with logical function codes. For example,
raw RIGHT index `11` translates to logical RIGHT code `24`.

This does not establish the meaning of every status byte or the full keyboard
protocol. A general parser should preserve unknown traffic for investigation.

## Receive ownership and bounded work

Outside streaming, a receive ISR produces bytes into a queue and foreground code
consumes them. Display transmission waits for acknowledgements without allowing
an unrelated receive handler to discard them. Changes are coalesced instead of
starting an entire display redraw for every event.

During live THRU, DUART interrupts are masked and the foreground handles at most
one RX byte per audio sample, draining queued data first. Display work is staged
at one character or TX/ACK service per 128 samples, about two frames per second.
No full-frame formatting or blocking acknowledgement wait occurs in the audio
loop. Normal mask `28` is restored on exit.

This scheduling reduced overhead; it has not established glitch-free sustained
hardware audio. The panel shares resources with sampling: DATA ENTRY is inactive
during THRU because it shares the input mux with audio.

## Sources

Source project: `firmware/epsfx_thru.s` (`consume`, `service_tx`, streaming parser
and UI), `tools/build_epsfx_thru.py` (raw/logical label translation), `RECON.md`.
Display/button operation is hardware verified; FIFO/timeout stress scenarios
were also exercised in an emulator harness. Complete protocol and cross-model
compatibility remain open research topics.
