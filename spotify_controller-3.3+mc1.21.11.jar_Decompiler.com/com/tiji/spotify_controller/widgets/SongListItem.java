package com.tiji.spotify_controller.widgets;

import com.google.gson.JsonObject;
import com.tiji.spotify_controller.api.ApiCalls;
import com.tiji.spotify_controller.api.SongData;
import com.tiji.spotify_controller.api.SongDataExtractor;
import com.tiji.spotify_controller.ui.Icons;
import com.tiji.spotify_controller.util.SafeDrawer;
import net.minecraft.class_1109;
import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_3417;
import net.minecraft.class_6382;

public class SongListItem extends SafeAbstractWidget {
   SongData song;
   private final int x;
   private final int y;
   public static final int WIDTH = 300;
   public static final int HEIGHT = 50;
   private static final int IMAGE_SIZE = 50;
   private static final int MARGIN = 10;
   private static final float FADE_TIME = 0.2F;
   private static final int IMAGE_FADE_OUT = 80;
   private float fadePos = 0.0F;
   private static final class_310 client = class_310.method_1551();

   public SongListItem(JsonObject data, int x, int y) {
      super(x, y, 300, 50, class_2561.method_43473());
      this.song = SongDataExtractor.getDataFor(data, () -> {
      });
      this.x = x;
      this.y = y;
   }

   protected void method_47399(class_6382 narrationElementOutput) {
   }

   public void safeRender(class_332 context, int mouseX, int mouseY, float delta) {
      boolean isHovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + 300 && mouseY < this.y + 50;
      float change = delta / 10.0F / 0.2F;
      if (isHovered) {
         this.fadePos += change;
      } else {
         this.fadePos -= change;
      }

      this.fadePos = Math.clamp(this.fadePos, 0.078431375F, 1.0F);
      int color = (int)(this.fadePos * 255.0F) << 24 | 16777215;
      int imageColor = (int)(255.0F - this.fadePos * 80.0F) * 65793 | -16777216;
      SafeDrawer.drawImage(context, this.song.coverImage.image, this.x, this.y, 0.0F, 0.0F, 50, 50, imageColor);
      context.method_51439(client.field_1772, this.song.title, this.x + 50 + 10, this.y + 10, -1, false);
      context.method_51433(client.field_1772, this.song.artist, this.x + 50 + 10, this.y + 10 + 15, -1, false);
      context.method_51439(client.field_1772, Icons.ADD_TO_QUEUE, this.x + 50 - 8 - 10, this.y + 50 - 5 - 10, color, false);
   }

   public boolean mouseClicked(double mouseX, double mouseY, int button) {
      boolean isHovered = mouseX >= (double)this.x && mouseY >= (double)this.y && mouseX < (double)(this.x + 300) && mouseY < (double)(this.y + 50);
      if (isHovered && button == 0) {
         ApiCalls.addSongToQueue(this.song.Id);
         client.method_1483().method_4873(class_1109.method_47978(class_3417.field_15015, 1.0F));
         return true;
      } else {
         return false;
      }
   }
}
