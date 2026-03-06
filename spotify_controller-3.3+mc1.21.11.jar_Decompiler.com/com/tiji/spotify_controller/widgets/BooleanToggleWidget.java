package com.tiji.spotify_controller.widgets;

import java.util.Objects;
import net.minecraft.class_124;
import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_327;
import net.minecraft.class_332;
import net.minecraft.class_6382;

public class BooleanToggleWidget extends SafeAbstractWidget implements ValueHolder {
   private boolean state;
   private static final class_2561 OFF_TEXT;
   private static final class_2561 ON_TEXT;

   public BooleanToggleWidget(int x, int y, int width, int height) {
      super(x, y, width, height, class_2561.method_43470(""));
   }

   public Object getValue_() {
      return this.state;
   }

   public void setValue(Object value) {
      if (value instanceof Boolean) {
         this.state = (Boolean)value;
      } else {
         throw new IllegalArgumentException("Value must be of type Boolean");
      }
   }

   public void safeRender(class_332 context, int mouseX, int mouseY, float delta) {
      class_327 textRenderer = class_310.method_1551().field_1772;
      int var10000 = this.method_25364();
      Objects.requireNonNull(textRenderer);
      int yOffset = (var10000 - 9) / 2;
      class_2561 text = this.state ? ON_TEXT : OFF_TEXT;
      if (this.isHovered((double)mouseX, (double)mouseY)) {
         text = ((class_2561)text).method_27661().method_27692(class_124.field_1073);
      }

      context.method_51439(textRenderer, (class_2561)text, this.method_46426(), this.method_46427() + yOffset, -1, false);
   }

   protected void method_47399(class_6382 builder) {
   }

   public void onClick(double mouseX, double mouseY) {
      if (this.isHovered(mouseX, mouseY)) {
         this.state = !this.state;
      }

   }

   private boolean isHovered(double mouseX, double mouseY) {
      return mouseX >= (double)this.method_46426() && mouseY >= (double)this.method_46427() && mouseY <= (double)(this.method_46427() + this.method_25364());
   }

   static {
      OFF_TEXT = class_2561.method_43471("ui.spotify_controller.toggle.off").method_27692(class_124.field_1061);
      ON_TEXT = class_2561.method_43471("ui.spotify_controller.toggle.on").method_27692(class_124.field_1060);
   }
}
