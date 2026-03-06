package com.tiji.spotify_controller.widgets;

import java.util.function.Consumer;
import net.minecraft.class_2561;
import net.minecraft.class_332;
import net.minecraft.class_6382;

public class ProgressWidget extends SafeAbstractWidget {
   private static final int h = 10;
   private static final int RAIL_HEIGHT = 2;
   private static final int RAIL_Y = 4;
   private static final int THUMB_SIZE = 4;
   private static final int COLOR = -1;
   private float value;
   private final Consumer<Float> action;
   private boolean dragging;

   public ProgressWidget(int x, int y, int w, float value, Consumer<Float> action) {
      super(x, y, w, 10, class_2561.method_43473());
      this.value = value;
      this.action = action;
   }

   public void onClick(double mouseX, double mouseY) {
      super.onClick(mouseX, mouseY);
      this.dragging = true;
   }

   public void onRelease(double mouseX, double mouseY) {
      super.onRelease(mouseX, mouseY);
      this.dragging = false;
      this.action.accept(this.value);
   }

   public void safeRender(class_332 context, int mouseX, int mouseY, float delta) {
      if (this.dragging) {
         this.value = this.getValue(mouseX - this.method_46426());
      }

      int thumbPosition = this.getThumbPosition();
      context.method_25294(this.method_46426(), this.method_46427() + 4, this.method_46426() + thumbPosition, this.method_46427() + 4 + 2, -1);
      context.method_25294(this.method_46426() + thumbPosition, this.method_46427() + 4, this.method_46426() + this.method_25368(), this.method_46427() + 4 + 2, 2013265919);
      context.method_25294(this.method_46426() + thumbPosition, this.method_46427() + 3, this.method_46426() + thumbPosition + 4, this.method_46427() + 7, -1);
   }

   protected int getThumbPosition() {
      return (int)(this.value * (float)(this.method_25368() - 4));
   }

   protected float getValue(int x) {
      return (float)x / (float)this.method_25368();
   }

   public void setValue(float value) {
      this.value = Math.max(0.0F, Math.min(1.0F, value));
   }

   protected void method_47399(class_6382 builder) {
   }
}
