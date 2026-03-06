package com.tiji.spotify_controller.widgets;

import net.minecraft.class_11907;
import net.minecraft.class_2561;
import net.minecraft.class_332;
import net.minecraft.class_4264;

public abstract class SafeAbstractButton extends class_4264 {
   public SafeAbstractButton(int i, int j, int k, int l, class_2561 component) {
      super(i, j, k, l, component);
   }

   public abstract void onPress();

   public void method_25306(class_11907 inputWithModifiers) {
      this.onPress();
   }

   public abstract void safeRender(class_332 var1, int var2, int var3, float var4);

   protected void method_75752(class_332 guiGraphics, int i, int j, float f) {
      this.safeRender(guiGraphics, i, j, f);
   }
}
