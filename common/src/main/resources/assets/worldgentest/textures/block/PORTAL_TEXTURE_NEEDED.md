# Portal Texture Needed

**File**: `crystal_portal.png`

**Description**:
A purple-tinted animated portal texture, similar to Nether portal but with purple/violet colors instead of purple.

**Specifications**:
- Format: PNG
- Size: 16x16 pixels (or 32x32 for higher resolution)
- Animation: Optional (can use .mcmeta file for animation)
- Color scheme: Purple/violet tones (#8E44AD or similar)

**Creation Method**:
1. Use vanilla `nether_portal.png` as base
2. Apply color adjustment: shift hue towards purple/violet
3. Adjust brightness to match crystal block's light level (11)

**Temporary Solution**:
Until the texture is created, the game will use missing texture (pink/black checkerboard).
The portal will still function correctly, just without proper visuals.

**Python Script Example** (using PIL):
```python
from PIL import Image
import numpy as np

# Load vanilla nether portal texture
# Apply color transformation
# Save as crystal_portal.png
```
