package com.tiji.spotify_controller.widgets;

import com.tiji.spotify_controller.util.SafeDrawer;
import net.minecraft.class_2561;
import net.minecraft.class_332;

public class BorderedButtonWidget extends BorderlessButtonWidget {
   private static final int PADDING = 2;
   private final int width;
   private final boolean needsCentering;
   private final int labelWidth;

   public BorderedButtonWidget(class_2561 innerText, int x, int y, Runnable action, boolean isIcon) {
      super(innerText, x, y, action, isIcon);
      if (isIcon) {
         this.width = 20;
      } else {
         this.width = client.field_1772.method_27525(innerText) + 4;
      }

      this.needsCentering = false;
      this.labelWidth = -1;
   }

   public BorderedButtonWidget(class_2561 innerText, int x, int y, Runnable action, boolean isIcon, int width) {
      super(innerText, x, y, action, isIcon);
      this.width = width;
      this.labelWidth = client.field_1772.method_27525(innerText);
      this.needsCentering = true;
   }

   public void safeRender(class_332 context, int mouseX, int mouseY, float delta) {
      SafeDrawer.drawOutline(context, this.method_46426(), this.method_46427(), this.width, 20, this.isHovered(mouseX, mouseY) ? -5592406 : -1);
      int x = this.method_46426() + 2;
      if (this.needsCentering) {
         x += (this.width - this.labelWidth) / 2;
      }

      context.method_51439(client.field_1772, this.label, x, this.method_46427() + 4 + 2, this.isHovered(mouseX, mouseY) ? -5592406 : -1, false);
   }

   private boolean isHovered(int mouseX, int mouseY) {
      return mouseX >= this.method_46426() && mouseX <= this.method_46426() + this.width && mouseY >= this.method_46427() && mouseY <= this.method_46427() + 16 + 4;
   }

   public void onPress() {
      super.onPress();
   }
}
