package com.tiji.spotify_controller;

import com.tiji.spotify_controller.util.ImageWithColor;
import com.tiji.spotify_controller.util.SafeDrawer;
import com.tiji.spotify_controller.util.TextUtils;
import net.minecraft.class_2561;
import net.minecraft.class_327;
import net.minecraft.class_332;
import net.minecraft.class_368;
import net.minecraft.class_374;
import net.minecraft.class_368.class_369;

public class SongToast implements class_368 {
   private static final int TITLE_Y = 6;
   private static final int ARTIST_Y = 18;
   private static final int TOAST_WIDTH = 160;
   private static final int TOAST_HEIGHT = 32;
   private static final int MARGIN = 5;
   private static final int IMAGE_WIDTH = 32;
   private static final long DISPLAY_DURATION_MS = 3000L;
   private static final int TEXT_WIDTH = 118;
   private final ImageWithColor cover;
   private final class_2561 artist;
   private final class_2561 title;
   private class_369 visibility;

   public SongToast(ImageWithColor cover, String artist, class_2561 title) {
      this.cover = cover;
      this.artist = TextUtils.getTrantedText(class_2561.method_30163(artist), 118);
      this.title = TextUtils.getTrantedText(title, 118);
      this.visibility = class_369.field_2209;
   }

   public void show(class_374 manager) {
      manager.method_1999(this);
   }

   public class_369 method_61988() {
      return this.visibility;
   }

   public void method_61989(class_374 manager, long time) {
      this.visibility = 3000.0D * manager.method_48221() <= (double)time ? class_369.field_2209 : class_369.field_2210;
   }

   public void method_1986(class_332 context, class_327 textRenderer, long startTime) {
      context.method_25294(0, 0, 160, 32, this.cover.color);
      int labelColor = this.cover.shouldUseDarkUI ? -1 : -16777216;
      context.method_51439(textRenderer, this.title, 37, 6, labelColor, false);
      context.method_51439(textRenderer, this.artist, 37, 18, labelColor, false);
      SafeDrawer.drawImage(context, this.cover.image, 0, 0, 0.0F, 0.0F, 32, 32);
   }
}
