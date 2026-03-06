package com.tiji.spotify_controller.widgets;

import net.minecraft.class_11909;
import net.minecraft.class_2561;
import net.minecraft.class_332;
import net.minecraft.class_339;

public abstract class SafeAbstractWidget extends class_339 {
   public SafeAbstractWidget(int i, int j, int k, int l, class_2561 component) {
      super(i, j, k, l, component);
   }

   public void onClick(double x, double y) {
   }

   public void onRelease(double x, double y) {
   }

   public boolean mouseClicked(double mouseX, double mouseY, int button) {
      return false;
   }

   public void method_25348(class_11909 mouseButtonEvent, boolean bl) {
      super.method_25348(mouseButtonEvent, bl);
      this.mouseClicked(mouseButtonEvent.comp_4798(), mouseButtonEvent.comp_4799(), mouseButtonEvent.method_74245());
      if (mouseButtonEvent.method_74245() == 0) {
         this.onClick(mouseButtonEvent.comp_4798(), mouseButtonEvent.comp_4799());
      }

   }

   public void method_25357(class_11909 mouseButtonEvent) {
      super.method_25357(mouseButtonEvent);
      if (mouseButtonEvent.method_74245() == 0) {
         this.onRelease(mouseButtonEvent.comp_4798(), mouseButtonEvent.comp_4799());
      }

   }

   public abstract void safeRender(class_332 var1, int var2, int var3, float var4);

   protected void method_48579(class_332 guiGraphics, int i, int j, float f) {
      this.safeRender(guiGraphics, i, j, f);
   }
}
