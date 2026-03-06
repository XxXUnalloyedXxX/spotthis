package com.tiji.spotify_controller.ui;

import com.tiji.spotify_controller.util.TextUtils;
import com.tiji.spotify_controller.widgets.BorderlessButtonWidget;
import java.util.Objects;
import net.minecraft.class_1074;
import net.minecraft.class_156;
import net.minecraft.class_2561;
import net.minecraft.class_2583;
import net.minecraft.class_332;
import net.minecraft.class_5250;

public class SetupScreen extends BaseScreen {
   private static final int MARGIN = 10;
   private static final class_2583 LINK;

   public SetupScreen() {
      super(true);
   }

   protected void method_25426() {
      super.method_25426();
      class_5250 var10003 = Icons.POPUP_OPEN.method_27661().method_10852(class_2561.method_43470("http://127.0.0.1:25566").method_10862(LINK));
      int var10004 = 10 + this.widgetsOffset;
      Objects.requireNonNull(this.field_22793);
      this.method_37063(new BorderlessButtonWidget(var10003, var10004, 30 + 9 * 3, () -> {
         class_156.method_668().method_670("http://127.0.0.1:25566");
      }, false));
   }

   public void method_25394(class_332 context, int mouseX, int mouseY, float delta) {
      super.method_25394(context, mouseX, mouseY, delta);
      context.method_51439(this.field_22793, class_2561.method_43471("ui.spotify_controller.welcome"), 10 + this.widgetsOffset, 10, -1, false);
      String rawText = class_1074.method_4662("ui.spotify_controller.welcome.subtext", new Object[0]);
      String[] warpedText = TextUtils.warpText(rawText, 200);
      Objects.requireNonNull(this.field_22793);
      int y = 20 + 9;
      String[] var8 = warpedText;
      int var9 = warpedText.length;

      for(int var10 = 0; var10 < var9; ++var10) {
         String line = var8[var10];
         context.method_51433(this.field_22793, line, 10 + this.widgetsOffset, y, -1, false);
         Objects.requireNonNull(this.field_22793);
         y += 9;
      }

   }

   static {
      LINK = class_2583.field_24360.method_27704(TextUtils.DEFAULT).method_30938(true);
   }
}
