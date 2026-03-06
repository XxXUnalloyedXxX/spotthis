package com.tiji.spotify_controller.util;

import com.tiji.spotify_controller.api.ImageColorExtractor;
import java.util.HashMap;
import net.minecraft.class_1011;
import net.minecraft.class_2960;

public class ImageWithColor {
   public class_2960 image;
   public final int color;
   private static final HashMap<class_2960, Integer> cachedColors = new HashMap();
   public boolean shouldUseDarkUI;

   public ImageWithColor(class_1011 image, class_2960 id) {
      this.image = id;
      if (cachedColors.containsKey(id)) {
         this.color = (Integer)cachedColors.get(id);
      } else {
         this.color = ImageColorExtractor.getDominantColor(image);
         this.shouldUseDarkUI = ImageColorExtractor.shouldUseDarkMode(this.color);
         cachedColors.put(id, this.color);
      }
   }

   public ImageWithColor(int color, class_2960 id) {
      this.image = id;
      this.color = color;
      this.shouldUseDarkUI = ImageColorExtractor.shouldUseDarkMode(this.color);
   }

   public ImageWithColor(class_2960 id) {
      this.image = id;
      this.color = (Integer)cachedColors.getOrDefault(id, -1055568);
      this.shouldUseDarkUI = ImageColorExtractor.shouldUseDarkMode(this.color);
   }

   public String toString() {
      String var10000 = String.valueOf(this.image);
      return "imageWithColor{image=" + var10000 + ", color=" + Integer.toHexString(this.color) + "}";
   }
}
