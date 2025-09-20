from PIL import Image, ImageDraw

# 64x32のテクスチャを作成
img = Image.new('RGBA', (64, 32), (0, 0, 0, 0))
draw = ImageDraw.Draw(img)

# クリスタルゴーレムの色を定義
crystal_blue = (100, 150, 255, 255)
crystal_light = (150, 200, 255, 255)
crystal_dark = (50, 100, 200, 255)
face_color = (200, 220, 255, 255)
eye_color = (255, 255, 0, 255)

# 頭部 (Head) - 8x8x8のキューブ
# 右側面 (Right): (0,0) - (8,8)
draw.rectangle([0, 0, 7, 7], fill=crystal_blue, outline=crystal_dark)
draw.rectangle([2, 2, 5, 5], fill=crystal_light)

# 前面 (Front/Face): (8,0) - (16,8)
draw.rectangle([8, 0, 15, 7], fill=face_color, outline=crystal_dark)
# 目を描く
draw.rectangle([10, 2, 11, 3], fill=eye_color)  # 左目
draw.rectangle([13, 2, 14, 3], fill=eye_color)  # 右目
# 口を描く
draw.rectangle([11, 5, 13, 5], fill=crystal_dark)

# 左側面 (Left): (16,0) - (24,8)
draw.rectangle([16, 0, 23, 7], fill=crystal_blue, outline=crystal_dark)
draw.rectangle([18, 2, 21, 5], fill=crystal_light)

# 背面 (Back): (24,0) - (32,8)
draw.rectangle([24, 0, 31, 7], fill=crystal_blue, outline=crystal_dark)
draw.rectangle([26, 2, 29, 5], fill=crystal_light)

# 上面 (Top): (8,8) - (16,16)
draw.rectangle([8, 8, 15, 15], fill=crystal_light, outline=crystal_dark)
draw.rectangle([10, 10, 13, 13], fill=crystal_blue)

# 下面 (Bottom): (16,8) - (24,16)
draw.rectangle([16, 8, 23, 15], fill=crystal_dark, outline=crystal_blue)

# 体部 (Body) - 8x12x4のキューブ
# 右側面: (32,0) - (36,12)
draw.rectangle([32, 0, 35, 11], fill=crystal_blue, outline=crystal_dark)

# 前面: (36,0) - (44,12)
draw.rectangle([36, 0, 43, 11], fill=crystal_light, outline=crystal_dark)
draw.rectangle([38, 2, 41, 9], fill=crystal_blue)

# 左側面: (44,0) - (48,12)
draw.rectangle([44, 0, 47, 11], fill=crystal_blue, outline=crystal_dark)

# 背面: (48,0) - (56,12)
draw.rectangle([48, 0, 55, 11], fill=crystal_blue, outline=crystal_dark)

# 上面: (36,12) - (44,16)
draw.rectangle([36, 12, 43, 15], fill=crystal_light, outline=crystal_dark)

# 下面: (44,12) - (52,16)
draw.rectangle([44, 12, 51, 15], fill=crystal_dark, outline=crystal_blue)

# 腕部 (Arms) - 4x12x4のキューブ
# 右腕
draw.rectangle([56, 0, 59, 11], fill=crystal_blue, outline=crystal_dark)  # 右側面
draw.rectangle([60, 0, 63, 11], fill=crystal_light, outline=crystal_dark)  # 前面

# 左腕
draw.rectangle([0, 16, 3, 27], fill=crystal_blue, outline=crystal_dark)   # 右側面
draw.rectangle([4, 16, 7, 27], fill=crystal_light, outline=crystal_dark)  # 前面

# 脚部 (Legs) - 4x12x4のキューブ
# 右脚
draw.rectangle([8, 16, 11, 27], fill=crystal_blue, outline=crystal_dark)  # 右側面
draw.rectangle([12, 16, 15, 27], fill=crystal_light, outline=crystal_dark) # 前面

# 左脚
draw.rectangle([16, 16, 19, 27], fill=crystal_blue, outline=crystal_dark) # 右側面
draw.rectangle([20, 16, 23, 27], fill=crystal_light, outline=crystal_dark) # 前面

# テクスチャを保存
img.save('/Users/ksoichiro/src/github.com/ksoichiro/WorldGenTest/fabric/src/main/resources/assets/worldgentest/textures/entity/crystal_golem.png')
print("Crystal Golem texture created with correct UV mapping!")