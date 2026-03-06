package com.tiji.spotify_controller.api;

import com.tiji.spotify_controller.Main;
import java.util.HashMap;
import java.util.Iterator;
import net.minecraft.class_1011;

public class ImageColorExtractor {
   public static final float MAX_COLOR_DIST = 0.2F;
   public static final float MAX_COLOR_DIST_SQR = 0.040000003F;

   public static int getDominantColor(class_1011 image) {
      HashMap<Integer, Integer> colorCount = getColorFrequency(image, Main.CONFIG.sampleSize());
      int highestScoredColor = 0;
      double highestScore = -1.7976931348623157E308D;
      Main.LOGGER.debug("Color frequencies (Found: {}): {}", colorCount.size(), colorCount);
      Iterator var5 = colorCount.keySet().iterator();

      while(var5.hasNext()) {
         Integer color = (Integer)var5.next();
         double score = calcWeightByArea((Integer)colorCount.getOrDefault(color, 0), Main.CONFIG.sampleSize()) * (double)calcScore(color, (float)Main.CONFIG.brightnessFactor(), (float)Main.CONFIG.saturationFactor(), Main.CONFIG.targetBrightness());
         Main.LOGGER.debug("Score for color {}: {}", color, score);
         if (score > highestScore) {
            highestScore = score;
            highestScoredColor = color;
         }
      }

      Main.LOGGER.debug("Dominant color: {}", highestScoredColor);
      return fixContrast(highestScoredColor);
   }

   private static int fixContrast(int color) {
      int r = color >> 16 & 255;
      int g = color >> 8 & 255;
      int b = color & 255;
      double brightness = (0.2126D * (double)r + 0.7152D * (double)g + 0.0722D * (double)b) / 255.0D;
      if (brightness > 0.9D) {
         r = (int)((double)r * 0.7D);
         g = (int)((double)g * 0.7D);
         b = (int)((double)b * 0.7D);
      } else if (brightness < 0.1D) {
         r = (int)((double)r * 1.7D);
         g = (int)((double)g * 1.7D);
         b = (int)((double)b * 1.7D);
      }

      return r << 16 | g << 8 | b | -16777216;
   }

   public static boolean shouldUseDarkMode(int dominantColor) {
      int r = dominantColor >> 16 & 255;
      int g = dominantColor >> 8 & 255;
      int b = dominantColor & 255;
      double brightness = (0.2126D * (double)r + 0.7152D * (double)g + 0.0722D * (double)b) / 255.0D;
      return brightness < 0.5D;
   }

   private static HashMap<Integer, Integer> getColorFrequency(class_1011 image, int sampleSize) {
      HashMap<Integer, Integer> colorCount = new HashMap();
      float multiplier_width = (float)image.method_4307() / (float)sampleSize;
      float multiplier_height = (float)image.method_4323() / (float)sampleSize;
      int multiplier_width_half = image.method_4307() / sampleSize / 2;
      int multiplier_height_half = image.method_4323() / sampleSize / 2;

      for(int x = 0; x < sampleSize; ++x) {
         for(int y = 0; y < sampleSize; ++y) {
            int color = image.method_61940((int)((float)x * multiplier_width) + multiplier_width_half, (int)((float)y * multiplier_height) + multiplier_height_half);
            int r = color >> 16 & 255;
            int g = color >> 8 & 255;
            int b = color & 255;
            boolean found = false;
            Iterator var14 = colorCount.keySet().iterator();

            while(var14.hasNext()) {
               Integer index = (Integer)var14.next();
               int r2 = index >> 16 & 255;
               int g2 = index >> 8 & 255;
               int b2 = index & 255;
               double lr = convertToLinear((double)r / 255.0D);
               double lg = convertToLinear((double)g / 255.0D);
               double lb = convertToLinear((double)b / 255.0D);
               double lr2 = convertToLinear((double)r2 / 255.0D);
               double lg2 = convertToLinear((double)g2 / 255.0D);
               double lb2 = convertToLinear((double)b2 / 255.0D);
               double brightness = (double)((0.299F * (float)r2 + 0.587F * (float)g2 + 0.114F * (float)b2) / 255.0F);
               double dist_cap = (brightness + 0.3D) * 0.20000000298023224D;
               double dist_sq = (lr - lr2) * (lr - lr2) + (lg - lg2) * (lg - lg2) + (lb - lb2) * (lb - lb2);
               if (dist_sq < dist_cap) {
                  colorCount.put(index, (Integer)colorCount.get(index) + 1);
                  found = true;
                  break;
               }
            }

            if (!found) {
               colorCount.put(color, 1);
               Main.LOGGER.debug("New color: {}", color);
            }
         }
      }

      return colorCount;
   }

   private static float calcScore(int color, float brightnessWeight, float saturationWeight, int target_brightness) {
      float TARGET_BRIGHTNESS = (float)target_brightness / 100.0F;
      int b = color >> 24 & 255;
      int g = color >> 16 & 255;
      int r = color >> 8 & 255;
      float r_norm = (float)r / 255.0F;
      float g_norm = (float)g / 255.0F;
      float b_norm = (float)b / 255.0F;
      float brightness = 0.299F * r_norm + 0.587F * g_norm + 0.114F * b_norm;
      float minc = Math.min(Math.min(r_norm, g_norm), b_norm);
      float maxc = Math.max(Math.max(r_norm, g_norm), b_norm);
      float delta = maxc - minc;
      float saturation;
      if (maxc == 0.0F) {
         saturation = 0.0F;
      } else {
         saturation = delta / maxc;
      }

      saturation *= 1.0F - Math.abs(brightness * 2.0F - 1.0F);
      float score = saturationWeight * saturation + (1.0F - Math.abs(brightness - TARGET_BRIGHTNESS)) * brightnessWeight;
      Main.LOGGER.debug("color: {}, saturation: {}, brightness: {}, score: {}", new Object[]{color, saturation, brightness, score});
      return score;
   }

   private static double calcWeightByArea(int x, int sampleSize) {
      return (double)x / (double)(sampleSize * sampleSize) < 0.2D ? 0.0D : 1.0D;
   }

   private static double convertToLinear(double color) {
      return color <= 0.04045D ? color / 12.92D : Math.pow((color + 0.055D) / 1.055D, 2.4D);
   }
}
