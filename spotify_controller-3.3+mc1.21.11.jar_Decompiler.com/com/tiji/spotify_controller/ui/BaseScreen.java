package com.tiji.spotify_controller.ui;

import com.tiji.spotify_controller.Main;
import com.tiji.spotify_controller.util.ImageWithColor;
import com.tiji.spotify_controller.util.SafeDrawer;
import java.util.Iterator;
import net.minecraft.class_2561;
import net.minecraft.class_2960;
import net.minecraft.class_332;
import net.minecraft.class_364;
import net.minecraft.class_437;
import net.minecraft.class_8021;

public class BaseScreen extends class_437 {
   protected float totalTime = 0.0F;
   protected int widgetsOffset = -100;
   protected static final int ANIMATION_AMOUNT = 100;
   private static final float animationTime = 0.5F;

   public BaseScreen(boolean animate) {
      super(class_2561.method_30163(""));
      if (!animate) {
         this.totalTime += 0.5F;
         this.widgetsOffset = 0;
      }

   }

   public void method_25394(class_332 context, int mouseX, int mouseY, float delta) {
      this.totalTime += delta / 10.0F;
      float normalized = Math.min(this.totalTime, 0.5F) / 0.5F;
      int previousOffset = this.widgetsOffset;
      ImageWithColor cover = Main.currentlyPlaying.coverImage;
      int color = cover.color;
      SafeDrawer.drawImage(context, class_2960.method_60655("spotify_controller", "ui/gradient.png"), this.widgetsOffset, 0, 0.0F, 0.0F, 255, this.field_22790, 255, 1, 255, 1, color);
      this.widgetsOffset = (int)(-100.0F + this.easeInOut(normalized) * 100.0F);
      Iterator var9 = this.method_25396().iterator();

      while(var9.hasNext()) {
         class_364 child = (class_364)var9.next();
         if (child instanceof class_8021) {
            class_8021 widget = (class_8021)child;
            widget.method_46421(widget.method_46426() + (this.widgetsOffset - previousOffset));
         }
      }

      this.field_33816.forEach((it) -> {
         it.method_25394(context, mouseX, mouseY, delta);
      });
   }

   private float easeInOut(float t) {
      return t * t * (3.0F - 2.0F * t);
   }
}
