#!/usr/bin/env python3
"""
Crystal Texture Generator
Generates crystal stone textures from vanilla Minecraft stone textures
by applying white/crystal color transformation.
"""

import os
import sys
from pathlib import Path
from PIL import Image, ImageEnhance

def apply_crystal_transformation(image):
    """
    Apply crystal transformation to make textures white and crystal-like.

    Args:
        image: PIL Image object

    Returns:
        PIL Image object with crystal transformation applied
    """
    # Step 1: Increase brightness (+30%)
    brightness_enhancer = ImageEnhance.Brightness(image)
    brightened = brightness_enhancer.enhance(1.3)

    # Step 2: Decrease saturation (-40%) to make it more white/grey
    color_enhancer = ImageEnhance.Color(brightened)
    desaturated = color_enhancer.enhance(0.6)

    # Step 3: Slightly increase contrast to maintain detail
    contrast_enhancer = ImageEnhance.Contrast(desaturated)
    final_image = contrast_enhancer.enhance(1.1)

    return final_image

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
    Extract vanilla stone textures from Minecraft jar.

    Args:
        minecraft_jar_path: Path to minecraft jar file
        output_dir: Directory to save extracted textures
    """
    import zipfile

    textures_to_extract = {
        "assets/minecraft/textures/block/stone.png": "vanilla_stone.png",
        "assets/minecraft/textures/block/cobblestone.png": "vanilla_cobblestone.png",
        "assets/minecraft/textures/block/stone_bricks.png": "vanilla_stone_bricks.png"
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

def generate_crystal_textures(input_dir, output_dir):
    """
    Generate crystal textures from vanilla textures.

    Args:
        input_dir: Directory containing vanilla textures
        output_dir: Directory to save crystal textures
    """
    texture_mappings = {
        "vanilla_stone.png": "crystal_stone.png",
        "vanilla_cobblestone.png": "crystal_cobblestone.png",
        "vanilla_stone_bricks.png": "crystal_bricks.png"
    }

    for vanilla_name, crystal_name in texture_mappings.items():
        vanilla_path = input_dir / vanilla_name
        crystal_path = output_dir / crystal_name

        if not vanilla_path.exists():
            print(f"Warning: {vanilla_path} not found, skipping {crystal_name}")
            continue

        try:
            # Load vanilla texture
            vanilla_image = Image.open(vanilla_path)

            # Apply crystal transformation
            crystal_image = apply_crystal_transformation(vanilla_image)

            # Save crystal texture
            crystal_image.save(crystal_path, "PNG")
            print(f"Generated {crystal_name} from {vanilla_name}")

        except Exception as e:
            print(f"Error processing {vanilla_name}: {e}")

def create_fallback_textures(output_dir):
    """
    Create distinct fallback textures if vanilla extraction fails.

    Args:
        output_dir: Directory to save fallback textures
    """
    print("Creating distinct fallback textures...")

    # Create distinct textures for each block type
    texture_configs = {
        "crystal_stone.png": {
            "base_color": (240, 240, 250),  # Very light blue-white
            "pattern": "smooth",
            "brightness_variance": 10
        },
        "crystal_cobblestone.png": {
            "base_color": (230, 230, 240),  # Slightly darker, more textured
            "pattern": "cobbled",
            "brightness_variance": 20
        },
        "crystal_bricks.png": {
            "base_color": (245, 245, 255),  # Brightest, cleanest
            "pattern": "bricks",
            "brightness_variance": 5
        }
    }

    for texture_name, config in texture_configs.items():
        # Create 16x16 image with distinct pattern
        image = Image.new("RGBA", (16, 16), (*config["base_color"], 255))
        pixels = image.load()

        base_r, base_g, base_b = config["base_color"]
        variance = config["brightness_variance"]

        for x in range(16):
            for y in range(16):
                if config["pattern"] == "smooth":
                    # Smooth stone pattern - gentle variation
                    variation = ((x + y) % 4) * 3
                    color_shift = variance - variation
                elif config["pattern"] == "cobbled":
                    # Cobblestone pattern - random-looking variation
                    variation = ((x * 3 + y * 7) % 8) * 4
                    color_shift = variance - variation
                elif config["pattern"] == "bricks":
                    # Brick pattern - linear brick-like pattern
                    brick_x = x // 4
                    brick_y = y // 4
                    if (brick_x + brick_y) % 2 == 0:
                        color_shift = variance // 2
                    else:
                        color_shift = -variance // 2

                # Apply color variation
                r = max(0, min(255, base_r + color_shift))
                g = max(0, min(255, base_g + color_shift))
                b = max(0, min(255, base_b + color_shift))

                pixels[x, y] = (r, g, b, 255)

        output_path = output_dir / texture_name
        image.save(output_path, "PNG")
        print(f"Created distinct fallback texture: {texture_name} ({config['pattern']} pattern)")

def main():
    """Main execution function."""
    # Setup paths
    script_dir = Path(__file__).parent
    project_root = script_dir.parent
    temp_dir = script_dir / "temp_textures"
    output_dir = project_root / "common/src/main/resources/assets/worldgentest/textures/block"

    # Create necessary directories
    temp_dir.mkdir(exist_ok=True)
    output_dir.mkdir(parents=True, exist_ok=True)

    print("Crystal Texture Generator")
    print("=" * 50)

    # Try to find and extract vanilla textures
    minecraft_jar = find_minecraft_jar()

    if minecraft_jar:
        print(f"Found Minecraft jar: {minecraft_jar}")
        extract_vanilla_textures(minecraft_jar, temp_dir)

        # Generate crystal textures from extracted vanilla textures
        generate_crystal_textures(temp_dir, output_dir)

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
        generated_files = list(output_dir.glob("crystal_*.png"))
        if generated_files:
            print("\nGenerated textures:")
            for file in generated_files:
                print(f"  - {file.name}")
        else:
            print("Warning: No crystal textures were generated")

if __name__ == "__main__":
    try:
        main()
    except KeyboardInterrupt:
        print("\nTexture generation cancelled by user")
        sys.exit(1)
    except Exception as e:
        print(f"Error: {e}")
        sys.exit(1)