package com.tiji.spotify_controller.ui;

import com.tiji.spotify_controller.Main;
import com.tiji.spotify_controller.util.ImageWithColor;
import com.tiji.spotify_controller.util.SafeDrawer;
import java.util.ArrayList;
import net.minecraft.class_332;
import net.minecraft.class_364;
import net.minecraft.class_4068;
import net.minecraft.class_6379;

public class SecondaryBaseScreen extends BaseScreen {
   private static final int IMAGE_SIZE = 30;
   private static final int MARGIN = 10;
   private static final int TITLE_Y = 24;
   private static final int ARTIST_Y = 9;
   protected static final int INFO_HEIGHT = 50;
   private final ArrayList<class_4068> drawables = new ArrayList();

   public SecondaryBaseScreen() {
      super(false);
   }

   public void method_25394(class_332 context, int mouseX, int mouseY, float delta) {
      super.method_25394(context, mouseX, mouseY, delta);
      ImageWithColor cover = Main.currentlyPlaying.coverImage;
      SafeDrawer.drawImage(context, cover.image, 10, this.field_22790 - 30 - 10, 0.0F, 0.0F, 30, 30);
      int nextX = (int)(15.0D + (double)this.widgetsOffset + 30.0D + 3.0D);
      context.method_51439(this.field_22793, Main.currentlyPlaying.title, nextX, this.field_22790 - 34, -1, false);
      context.method_51433(this.field_22793, Main.currentlyPlaying.artist, nextX, this.field_22790 - 19, -1, false);
      context.method_44379(0, 0, this.field_22789, this.field_22790 - 50);
      this.drawables.forEach((drawable) -> {
         drawable.method_25394(context, mouseX, mouseY, delta);
      });
      context.method_44380();
   }

   public void method_25419() {
      assert this.field_22787 != null;

      this.field_22787.method_1507(Main.nowPlayingScreen);
   }

   protected <T extends class_364 & class_4068 & class_6379> T method_37063(T drawableElement) {
      return this.drawables.add((class_4068)drawableElement) ? drawableElement : null;
   }
}
