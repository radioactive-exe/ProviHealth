Larger update to provide bug fixes and new data-driven functionality.

## Additions
- Added data-driven customisation.
  - Resource packs can now be used to change the hud type, HUD health bars, portrait, and more for individual entity types and also tags.
  - Includes data generators so dependent mods can easily add their own customisations.

## Bugfixes
- Adjusted health bar rendering to use vanilla's "text" rendering instead of the prior custom implementation.
  - Resolves the incompatibility with Iris shaders.
  - Should resolve other rendering incompatibilities too.