#!/usr/bin/env python3
"""
Create a 16x16 white-blue crystal block texture for Minecraft
"""

from PIL import Image, ImageDraw
import random

def create_crystal_texture():
    # Create a 16x16 image
    img = Image.new('RGBA', (16, 16), (255, 255, 255, 0))

    # Define color palette for white-blue crystal
    colors = [
        (240, 248, 255, 255),  # Alice blue
        (220, 235, 250, 255),  # Light blue
        (200, 225, 245, 255),  # Medium blue
        (180, 210, 240, 255),  # Darker blue
        (255, 255, 255, 255),  # Pure white
        (250, 250, 255, 255),  # Slightly off-white
        (245, 248, 255, 255),  # Very light blue-white
    ]

    # Create crystal-like pattern
    pixels = []

    for y in range(16):
        row = []
        for x in range(16):
            # Create some randomness for crystal effect
            noise = random.random()

            # Create diagonal patterns like crystal facets
            diagonal1 = (x + y) % 3
            diagonal2 = (x - y) % 4

            # Edges should be darker/more blue
            edge_factor = min(x, y, 15-x, 15-y)

            # Choose color based on position and patterns
            if edge_factor <= 1:
                # Edges - more blue
                color_idx = min(3, int(noise * 4))
            elif diagonal1 == 0 or diagonal2 == 0:
                # Crystal facet lines - mix of colors
                color_idx = int(noise * len(colors))
            else:
                # Interior - more white
                color_idx = max(4, int(noise * len(colors)))

            row.append(colors[color_idx])
        pixels.append(row)

    # Set pixels
    for y in range(16):
        for x in range(16):
            img.putpixel((x, y), pixels[y][x])

    return img

if __name__ == "__main__":
    # Create the texture
    texture = create_crystal_texture()

    # Save to the correct location
    output_path = "common/src/main/resources/assets/worldgentest/textures/block/crystal_block.png"
    texture.save(output_path)
    print(f"Created crystal texture: {output_path}")