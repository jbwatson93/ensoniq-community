# Classic EPS capture, playback and live audio-through

**Finding:** a custom disk OS can capture and replay external audio, capture new
input while playing a stored buffer, and pass live input through a ring buffer
on an original EPS. **Hardware status through 2026-10-03:** live audio passes,
but intermittent glitches remain unresolved. Sustained glitch-free operation
has not been established.

## Hardware boundary

The classic EPS uses ES5504 DOC II and signed 13-bit left-justified samples
(`FFF8` mask). The EPS-16+ uses a different audio architecture; its ES5505/ES5510
paths and 16-bit sample assumptions must not be substituted here.

## Proven configuration used by EPSFX

| Setting | Value/role |
| --- | --- |
| Active-voice register ACT | `24` decimal: 25 voice slots; nominal 25 kHz scan rate |
| Voice 0 | Retains ADC triggering; capture CR=`0006` |
| ADC register | `7FFF` retained in the working configuration |
| Voice 1 frequency control | `0200`: half sample per scan, nominal 12.5 kHz playback |
| Voice 1 output | Volume `FFF0`, CA byte `F3`, full filters, CR=`08` loop |
| DUART capture timer | Reload 200 decimal; nominal 12.5 kHz input service |
| THRU sample RAM candidates | CPU `428000` and `628000`, both written |

These settings work as a combined configuration. They are not proof of each
register's behavior in isolation. The two RAM candidates are mirrored by the
firmware because physical bank selection/aliasing is not fully resolved.

ROM initialization is retained. The project clock interpretation is a shared
10 MHz master, a 5 MHz DUART timer source, and ROM ACR=`60` timer mode. Reload
200 corresponds to the nominal 80 microsecond service interval in this mode.
Independent hardware measurements would strengthen the timing evidence.

## Streaming design

The CPU polls the timer in a bounded foreground loop and writes a 2048-sample
ring. The DOC consumer runs independently. Playback begins after 1024 samples
of priming: approximately 81.92 ms of buffering at the nominal rate, excluding
analog/filter and scan-phase latency.

Lead is checked every 32 samples using a coherent high-low-high accumulator
snapshot. Accepted lead is 256-1792 samples. The first two samples are mirrored
at the loop guard to support boundary interpolation. No rate correction or
DMA streaming is implemented. No ROM scheduler traps or global DOC
initialization execute inside the streaming loop.

Input DC is estimated with a Q8 running mean updated by 1/256 per sample. Half
the pre-update input-minus-mean difference is quantized to 13 bits. This fixed
half gain fits the arithmetic range and is not AGC. Separate recorded buffers
use DC removal and peak normalization, so their level differs from live THRU.

## Hardware observations

- Analog monitoring, capture and normalized replay work.
- Stored playback during capture of a different input works; the new recording
  can subsequently be replayed.
- THRU v1 passed live audio but lost buffer lead and faulted `0003` after roughly
  one to two minutes.
- THRU v2 passes live audio. The reported lead display stays at “4000,” whereas
  the expected initial hexadecimal display is `0400`; exact digits are unresolved.
- V2 intermittently sounds glitchy. Cable/source comparison, five-minute
  sustained acceptance and v2 mute behavior have not been explicitly confirmed.

The cause of the glitches is unknown. Compare analog monitoring and digital
THRU with the same source/cable, then repeat with a better cable. If stable,
record exact lead and pressure values at start, one minute and five minutes.

## Diagnostics

`L` is hexadecimal producer/consumer lead. `P` counts timer events already
pending at the first poll after processing: it measures service pressure, not
missed samples. It saturates at `FFFF` without triggering a fault.

| Fault | Check that stopped streaming |
| --- | --- |
| `0001` | Timer event not observed within bounded wait |
| `0002` | Incoherent/out-of-range consumer position |
| `0003` | Lead outside 256-1792 samples |
| `0004` | UART error/queue overflow counter changed |
| `0005` | Initial sound-RAM clear/readback failed |

Faults mute playback. Their codes identify failed checks, not a proven physical
root cause. RIGHT re-primes; during running THRU RIGHT toggles digital mute,
STOP/CANCEL exits. Other audio commands and DATA ENTRY are inactive during THRU.

## Emulator validation

The v2 harness passed 315 simulated seconds with lead/min/max 1024. Capture/
playback coexistence regression and EpsLin round-trip checks also passed.
These results do not prove sustained physical audio quality. See
[EPSFX emulator notes](../emulation/epsfx/README.md) for measurements,
harness assumptions and setup limitations.

The analyzed v2 image contains 4466 code bytes; disk SHA-256:
`b0a7ab3c152caa0e7a4f91c862fba8099376eb56b7db88665d3a4d2ba410fe64`.
This identifies the tested artifact; no binary is included here.

## Sources and open work

Source project: `firmware/epsfx_thru.s`, `firmware/THRU-TEST.md`,
`firmware/README.md`, `tools/build_epsfx_thru.py`,
`work/mame_thru_v2/long.log`, `work/mame_thru_v2/regression.log`.

Next research: isolate physical glitches; verify timer/consumer timing and
sample-bank decoding; investigate safe slider/audio mux sharing. Adjustable
bit depth, sample rate and lo-fi effects are planned, not implemented results.
