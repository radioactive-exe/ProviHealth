1.21.4 introduces rendering changes that cause the original damage indicator particle implementation crash the game (Sodium users were seemingly unaffected).
This update fixes this issue by changing, once again, how the text is rendered by the particle.

## Changes
- The coloured rhombus and the text for damage particles have been split into two separate particle types.
  - `provihealth:text_particle` is no longer the image, now it is purely the text and uses the `CUSTOM` particle layer.
  - `provihealth:health_particle` uses all of the original parameters that the damage particles had originally, except now it only renders the image and not the text. This particle spawns the text as a separate particle when created.
- No longer uses its own built-in JSON builder. The mod now uses the JSON builder supplied by LilyLib.