package com.tiji.spotify_controller.widgets;

import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_6382;

public class BorderlessButtonWidget extends SafeAbstractButton {
   protected class_2561 label;
   protected final Runnable action;
   private final int width;
   protected static final int HOVERED_COLOR = -5592406;
   protected static final int NORMAL_COLOR = -1;
   protected static final class_310 client = class_310.method_1551();
   public static final int BUTTON_SIZE = 16;
   protected static final int LABEL_OFFSET = 4;

   public BorderlessButtonWidget(class_2561 innerText, int x, int y, Runnable action, boolean isIcon) {
      super(x, y, isIcon ? 16 : client.field_1772.method_27525(innerText), 16, class_2561.method_43473());
      this.label = innerText;
      this.action = action;
      this.width = isIcon ? 16 : client.field_1772.method_27525(innerText);
   }

   public void onPress() {
      this.action.run();
   }

   public void safeRender(class_332 context, int mouseX, int mouseY, float delta) {
      context.method_51439(client.field_1772, this.label, this.method_46426(), this.method_46427() + 4, this.isHovered(mouseX, mouseY) ? -5592406 : -1, false);
   }

   protected void method_47399(class_6382 builder) {
   }

   private boolean isHovered(int mouseX, int mouseY) {
      return mouseX >= this.method_46426() && mouseX <= this.method_46426() + this.width && mouseY >= this.method_46427() && mouseY <= this.method_46427() + 16;
   }

   public void setLabel(class_2561 label) {
      this.label = label;
   }
}
