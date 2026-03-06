package com.tiji.spotify_controller.util;

import net.minecraft.class_10799;
import net.minecraft.class_2960;
import net.minecraft.class_332;

public class SafeDrawer {
   public static void drawImage(class_332 context, class_2960 sprite, int x, int y, float u, float v, int width, int height, int regionWith, int regionHeight, int textureWidth, int textureHeight) {
      context.method_25302(class_10799.field_56883, sprite, x, y, u, v, width, height, regionWith, regionHeight, textureWidth, textureHeight);
   }

   public static void drawImage(class_332 context, class_2960 sprite, int x, int y, float u, float v, int width, int height, int regionWith, int regionHeight, int textureWidth, int textureHeight, int tint) {
      context.method_25293(class_10799.field_56883, sprite, x, y, u, v, width, height, regionWith, regionHeight, textureWidth, textureHeight, tint);
   }

   public static void drawImage(class_332 context, class_2960 sprite, int x, int y, float u, float v, int width, int height) {
      drawImage(context, sprite, x, y, u, v, width, height, 1, 1, 1, 1);
   }

   public static void drawImage(class_332 context, class_2960 sprite, int x, int y, float u, float v, int width, int height, int color) {
      drawImage(context, sprite, x, y, u, v, width, height, 1, 1, 1, 1, color);
   }

   public static void drawOutline(class_332 context, int x, int y, int w, int h, int color) {
      context.method_73198(x, y, w, h, color);
   }
}
