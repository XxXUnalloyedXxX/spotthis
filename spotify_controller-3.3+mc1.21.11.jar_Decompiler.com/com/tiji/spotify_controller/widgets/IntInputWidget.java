package com.tiji.spotify_controller.widgets;

import java.util.function.Consumer;
import net.minecraft.class_2561;
import net.minecraft.class_327;

public class IntInputWidget extends StringInputWidget {
   public IntInputWidget(class_327 textRenderer, int x, int y, int width, int height, class_2561 text, class_2561 icon, Consumer<String> action) {
      super(textRenderer, x, y, width, height, text, icon, action);
   }

   public IntInputWidget(int x, int y, int width, int height) {
      super(x, y, width, height);
   }

   public void method_1867(String text) {
      if (text.length() == 1) {
         char c = text.charAt(0);
         if (c >= '0' && c <= '9') {
            super.method_1867(text);
         }

      }
   }

   public void setValue(Object value) {
      if (value instanceof Integer) {
         super.setValue(value);
      } else {
         throw new IllegalArgumentException("Value must be an instance of Integer");
      }
   }

   public Object getValue_() {
      return Integer.parseInt(this.method_1882());
   }
}
