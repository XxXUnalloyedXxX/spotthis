package com.tiji.spotify_controller.widgets;

import com.tiji.spotify_controller.util.SafeDrawer;
import java.util.Objects;
import java.util.function.Consumer;
import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_327;
import net.minecraft.class_332;
import net.minecraft.class_342;

public class StringInputWidget extends class_342 implements ValueHolder {
   private final class_2561 icon;
   private static final class_310 client = class_310.method_1551();
   private long time;
   private static final long CURSOR_BLINK_DURATION = 1000L;
   private static final int MAX_TYPING_PAUSE = 500;
   private final Consumer<String> action;
   private boolean didRunAction;

   public StringInputWidget(class_327 textRenderer, int x, int y, int width, int height, class_2561 text, class_2561 icon, Consumer<String> action) {
      super(textRenderer, x, y, width, height, text);
      this.time = System.currentTimeMillis();
      this.didRunAction = false;
      this.method_1880(Integer.MAX_VALUE);
      this.icon = icon;
      this.action = action;
   }

   public StringInputWidget(int x, int y, int width, int height) {
      this(client.field_1772, x, y, width, height, class_2561.method_43470(""), class_2561.method_43470(""), (s) -> {
      });
   }

   public void method_48579(class_332 context, int mouseX, int mouseY, float delta) {
      SafeDrawer.drawOutline(context, this.method_46426(), this.method_46427(), this.field_22758, this.field_22759, -1);
      int var10000 = this.field_22759;
      Objects.requireNonNull(client.field_1772);
      int y = (var10000 - 9) / 2 + this.method_46427();
      context.method_51439(client.field_1772, this.icon, this.field_22758 - 18 + this.method_46426(), y + 2, -1, false);
      context.method_44379(this.method_46426(), this.method_46427(), this.method_46426() + this.field_22758 - 18, this.method_46427() + this.field_22759);
      context.method_51433(client.field_1772, this.method_1882(), this.method_46426() + 4, y + 1, -1, false);
      context.method_44380();
      long timePast = System.currentTimeMillis() - this.time;
      boolean shouldBlink = timePast % 1000L < 500L;
      if (shouldBlink && this.method_25370()) {
         context.method_51742(client.field_1772.method_1727(this.method_1882()) + this.method_46426() + 4, this.method_46427() + 3, this.method_46427() + this.field_22759 - 5, -1);
      }

      if (!this.didRunAction && timePast > 500L) {
         this.didRunAction = true;
         this.action.accept(this.method_1882());
      }

   }

   public void method_1867(String text) {
      class_327 var10000 = client.field_1772;
      String var10001 = String.valueOf(this.getValue_());
      if (var10000.method_1727(var10001 + text) <= this.field_22758 - 22) {
         this.time = System.currentTimeMillis();
         this.didRunAction = false;
         super.method_1867(text);
      }
   }

   public Object getValue_() {
      return this.method_1882();
   }

   public void setValue(Object value) {
      this.method_1852(value.toString());
   }
}
