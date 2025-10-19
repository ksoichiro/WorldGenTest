#!/usr/bin/env python3
"""
Crystal Dimension Texture Generator
Generates custom dimension block textures from vanilla Minecraft textures
by applying purple/crystal color transformation.
"""

import os
import sys
from pathlib import Path
from PIL import Image, ImageEnhance

def apply_crystal_dimension_transformation(image, color_shift=(30, 0, 50)):
    """
    Apply crystal dimension transformation with purple tint.

    Args:
        image: PIL Image object
        color_shift: RGB shift to apply (default: purple tint)

    Returns:
        PIL Image object with transformation applied
    """
    # Step 1: Increase brightness (+20%)
    brightness_enhancer = ImageEnhance.Brightness(image)
    brightened = brightness_enhancer.enhance(1.2)

    # Step 2: Apply purple color shift
    pixels = brightened.load()
    width, height = brightened.size

    for x in range(width):
        for y in range(height):
            r, g, b, a = pixels[x, y] if brightened.mode == 'RGBA' else (*pixels[x, y], 255)

            # Apply color shift with clamping
            r = max(0, min(255, r + color_shift[0]))
            g = max(0, min(255, g + color_shift[1]))
            b = max(0, min(255, b + color_shift[2]))

            pixels[x, y] = (r, g, b, a)

    # Step 3: Slightly increase saturation for purple effect
    color_enhancer = ImageEnhance.Color(brightened)
    saturated = color_enhancer.enhance(1.1)

    return saturated

def find_minecraft_jar():
    """
    Try to find Minecraft jar file in common locations.

    Returns:
        Path to minecraft jar or None if not found
    """
    common_paths = [
        "~/.minecraft/versions/1.21.1/1.21.1.jar",
        "~/.minecraft/versions/1.21/1.21.jar",
        "/Applications/Minecraft.app/Contents/Java/versions/1.21.1/1.21.1.jar"
    ]

    for path in common_paths:
        expanded_path = Path(path).expanduser()
        if expanded_path.exists():
            return expanded_path

    return None

def extract_vanilla_textures(minecraft_jar_path, output_dir):
    """
    Extract vanilla textures from Minecraft jar.

    Args:
        minecraft_jar_path: Path to minecraft jar file
        output_dir: Directory to save extracted textures
    """
    import zipfile

    textures_to_extract = {
        "assets/minecraft/textures/block/grass_block_top.png": "vanilla_grass_block_top.png",
        "assets/minecraft/textures/block/grass_block_side.png": "vanilla_grass_block_side.png",
        "assets/minecraft/textures/block/dirt.png": "vanilla_dirt.png",
        "assets/minecraft/textures/block/oak_log.png": "vanilla_oak_log.png",
        "assets/minecraft/textures/block/oak_log_top.png": "vanilla_oak_log_top.png",
        "assets/minecraft/textures/block/sand.png": "vanilla_sand.png"
    }

    with zipfile.ZipFile(minecraft_jar_path, 'r') as jar:
        for jar_path, output_name in textures_to_extract.items():
            try:
                with jar.open(jar_path) as texture_file:
                    texture_data = texture_file.read()
                    output_path = output_dir / output_name
                    with open(output_path, 'wb') as f:
                        f.write(texture_data)
                    print(f"Extracted {jar_path} -> {output_path}")
            except KeyError:
                print(f"Warning: Could not find {jar_path} in jar file")

def generate_dimension_textures(input_dir, output_dir):
    """
    Generate crystal dimension textures from vanilla textures.

    Args:
        input_dir: Directory containing vanilla textures
        output_dir: Directory to save dimension textures
    """
    texture_mappings = {
        "vanilla_grass_block_top.png": ("crystal_grass_block_top.png", (20, 0, 40)),
        "vanilla_grass_block_side.png": ("crystal_grass_block_side.png", (25, 0, 45)),
        "vanilla_dirt.png": ("crystal_dirt.png", (30, 5, 50)),
        "vanilla_oak_log.png": ("crystal_log.png", (35, -10, 60)),
        "vanilla_oak_log_top.png": ("crystal_log_top.png", (35, -10, 60)),
        "vanilla_sand.png": ("crystal_sand.png", (40, 10, 70))
    }

    for vanilla_name, (crystal_name, color_shift) in texture_mappings.items():
        vanilla_path = input_dir / vanilla_name
        crystal_path = output_dir / crystal_name

        if not vanilla_path.exists():
            print(f"Warning: {vanilla_path} not found, skipping {crystal_name}")
            continue

        try:
            # Load vanilla texture
            vanilla_image = Image.open(vanilla_path).convert('RGBA')

            # Apply crystal transformation
            crystal_image = apply_crystal_dimension_transformation(vanilla_image, color_shift)

            # Save crystal texture
            crystal_image.save(crystal_path, "PNG")
            print(f"Generated {crystal_name} from {vanilla_name}")

        except Exception as e:
            print(f"Error processing {vanilla_name}: {e}")

def create_fallback_textures(output_dir):
    """
    Create fallback textures if vanilla extraction fails.

    Args:
        output_dir: Directory to save fallback textures
    """
    print("Creating fallback textures...")

    texture_configs = {
        "crystal_grass_block_top.png": {
            "base_color": (140, 120, 180),  # Purple-tinted green
            "pattern": "grass"
        },
        "crystal_grass_block_side.png": {
            "base_color": (120, 100, 150),  # Darker purple-brown
            "pattern": "grass_side"
        },
        "crystal_dirt.png": {
            "base_color": (130, 110, 160),  # Purple-brown
            "pattern": "dirt"
        },
        "crystal_log.png": {
            "base_color": (150, 100, 180),  # Purple wood
            "pattern": "log"
        },
        "crystal_log_top.png": {
            "base_color": (160, 110, 190),  # Lighter purple
            "pattern": "log_top"
        },
        "crystal_sand.png": {
            "base_color": (180, 160, 200),  # Light purple sand
            "pattern": "sand"
        }
    }

    for texture_name, config in texture_configs.items():
        image = Image.new("RGBA", (16, 16), (*config["base_color"], 255))
        pixels = image.load()

        base_r, base_g, base_b = config["base_color"]

        for x in range(16):
            for y in range(16):
                # Add simple noise pattern
                variation = ((x * 3 + y * 7) % 16) - 8

                r = max(0, min(255, base_r + variation))
                g = max(0, min(255, base_g + variation))
                b = max(0, min(255, base_b + variation))

                pixels[x, y] = (r, g, b, 255)

        output_path = output_dir / texture_name
        image.save(output_path, "PNG")
        print(f"Created fallback texture: {texture_name}")

def main():
    """Main execution function."""
    # Setup paths
    script_dir = Path(__file__).parent
    project_root = script_dir.parent
    temp_dir = script_dir / "temp_dimension_textures"
    output_dir = project_root / "common/src/main/resources/assets/worldgentest/textures/block"

    # Create necessary directories
    temp_dir.mkdir(exist_ok=True)
    output_dir.mkdir(parents=True, exist_ok=True)

    print("Crystal Dimension Texture Generator")
    print("=" * 50)

    # Try to find and extract vanilla textures
    minecraft_jar = find_minecraft_jar()

    if minecraft_jar:
        print(f"Found Minecraft jar: {minecraft_jar}")
        extract_vanilla_textures(minecraft_jar, temp_dir)

        # Generate dimension textures from extracted vanilla textures
        generate_dimension_textures(temp_dir, output_dir)

        # Clean up temp directory
        import shutil
        shutil.rmtree(temp_dir)

    else:
        print("Could not find Minecraft jar file.")
        print("Creating fallback textures instead...")
        create_fallback_textures(output_dir)

    print("\nTexture generation complete!")
    print(f"Output directory: {output_dir}")

    # List generated files
    if output_dir.exists():
        generated_files = [
            f for f in output_dir.glob("crystal_*.png")
            if any(name in f.name for name in ["grass_block", "dirt", "log", "sand"])
        ]
        if generated_files:
            print("\nGenerated dimension textures:")
            for file in sorted(generated_files):
                print(f"  - {file.name}")
        else:
            print("Warning: No dimension textures were generated")

if __name__ == "__main__":
    try:
        main()
    except KeyboardInterrupt:
        print("\nTexture generation cancelled by user")
        sys.exit(1)
    except Exception as e:
        print(f"Error: {e}")
        sys.exit(1)
