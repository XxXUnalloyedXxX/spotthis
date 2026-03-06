package com.tiji.spotify_controller.ui;

import com.tiji.spotify_controller.Main;
import com.tiji.spotify_controller.api.ApiCalls;
import com.tiji.spotify_controller.util.ImageWithColor;
import com.tiji.spotify_controller.util.RepeatMode;
import com.tiji.spotify_controller.util.SafeDrawer;
import com.tiji.spotify_controller.util.TextUtils;
import com.tiji.spotify_controller.widgets.BorderlessButtonWidget;
import com.tiji.spotify_controller.widgets.ProgressWidget;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;
import net.minecraft.class_2561;
import net.minecraft.class_2583;
import net.minecraft.class_310;
import net.minecraft.class_332;
import org.lwjgl.glfw.GLFW;

public class NowPlayingScreen extends BaseScreen {
   private static final int MARGIN = 10;
   private static final int IMAGE_SIZE = 70;
   private static final int PLAYBACK_CONTROL_Y = 50;
   private static final int TITLE_Y = 8;
   private static final int ARTIST_Y = 23;
   private static final int PLAYBACK_SIZE = 200;
   private static final int INFO_TEXT_SIZE = 110;
   private BorderlessButtonWidget playPauseButton;
   private ProgressWidget progressBar;
   private BorderlessButtonWidget repeatButton;
   private BorderlessButtonWidget shuffleButton;
   private boolean isFirstInit = true;
   private static final Map<class_2561, Class<? extends SecondaryBaseScreen>> SUBSCREENS;
   private static final int SUBSCREEN_BUTTONS_HEIGHT = 16;

   private static class_2561 subscreenText(class_2561 Icon, String description) {
      return Icon.method_27661().method_27693(" ").method_10852(class_2561.method_43471(description).method_10862(class_2583.field_24360.method_27704(TextUtils.DEFAULT)));
   }

   public NowPlayingScreen() {
      super(true);
   }

   protected void method_25426() {
      super.method_25426();
      class_310 client = class_310.method_1551();
      if (this.isFirstInit) {
         GLFW.glfwSetCursorPos(client.method_22683().method_4490(), 0.0D, 0.0D);
         this.isFirstInit = false;
      }

      int x = 20 + this.widgetsOffset + 70;
      int y = 60;
      this.shuffleButton = new BorderlessButtonWidget(Icons.SHUFFLE, x, y, () -> {
         ApiCalls.setShuffle(!Main.playbackState.shuffle);
      }, true);
      this.method_37063(this.shuffleButton);
      x += 17;
      this.method_37063(new BorderlessButtonWidget(Icons.PREVIOUS, x, y, ApiCalls::previousTrack, true));
      x += 17;
      this.playPauseButton = new BorderlessButtonWidget(Main.playbackState.isPlaying ? Icons.PAUSE : Icons.RESUME, x, y, () -> {
         ApiCalls.playPause(!Main.playbackState.isPlaying);
      }, true);
      this.method_37063(this.playPauseButton);
      x += 17;
      this.method_37063(new BorderlessButtonWidget(Icons.NEXT, x, y, ApiCalls::nextTrack, true));
      x += 17;
      this.repeatButton = new BorderlessButtonWidget(Icons.REPEAT, x, y, () -> {
         ApiCalls.setRepeat(RepeatMode.getNextMode(Main.playbackState.repeat));
      }, true);
      this.method_37063(this.repeatButton);
      this.progressBar = new ProgressWidget(10 + this.widgetsOffset, 85, 200, (float)Main.playbackState.progressValue, (v) -> {
         ApiCalls.setPlaybackLoc((int)((float)Main.currentlyPlaying.duration * v));
      });
      this.method_37063(this.progressBar);
      int y = this.field_22790 - 10 - 16;

      for(Iterator var4 = SUBSCREENS.entrySet().iterator(); var4.hasNext(); y -= 16) {
         Entry<class_2561, Class<? extends SecondaryBaseScreen>> entry = (Entry)var4.next();
         this.method_37063(new BorderlessButtonWidget((class_2561)entry.getKey(), 10 + this.widgetsOffset, y, () -> {
            try {
               SecondaryBaseScreen screen = (SecondaryBaseScreen)((Class)entry.getValue()).getDeclaredConstructor().newInstance();
               client.method_1507(screen);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException var3) {
            }

         }, false));
      }

   }

   public void method_25394(class_332 context, int mouseX, int mouseY, float delta) {
      super.method_25394(context, mouseX, mouseY, delta);
      ImageWithColor cover = Main.currentlyPlaying.coverImage;
      SafeDrawer.drawImage(context, cover.image, 10 + this.widgetsOffset, 10, 0.0F, 0.0F, 70, 70);
      int nextX = 20 + this.widgetsOffset + 70 + 3;
      class_2561 title = class_2561.method_54155(Main.currentlyPlaying.title);
      class_2561 artist = class_2561.method_30163(Main.currentlyPlaying.artist);
      title = TextUtils.getTrantedText(title, 110);
      artist = TextUtils.getTrantedText(artist, 110);
      context.method_44379(92 + this.widgetsOffset, 0, 200 + this.widgetsOffset, this.field_22790);
      context.method_51439(this.field_22793, title, nextX, 18, -1, false);
      context.method_51439(this.field_22793, artist, nextX, 33, -1, false);
      context.method_44380();
      context.method_51439(this.field_22793, class_2561.method_30163(Main.playbackState.progressLabel), 10 + this.widgetsOffset, 95, -1, false);
      context.method_51439(this.field_22793, class_2561.method_30163(Main.currentlyPlaying.durationLabel), 10 + this.widgetsOffset + 200 - this.field_22793.method_27525(class_2561.method_30163(Main.currentlyPlaying.durationLabel)) + 1, 95, -1, false);
      this.drawFullName(context, mouseX, mouseY, nextX, nextX);
   }

   public void method_25419() {
      super.method_25419();
      Main.nowPlayingScreen = null;
   }

   public void updateStatus() {
      this.playPauseButton.setLabel(Main.playbackState.isPlaying ? Icons.PAUSE : Icons.RESUME);
      this.progressBar.setValue((float)Main.playbackState.progressValue);
      this.repeatButton.setLabel(RepeatMode.getAsText(Main.playbackState.repeat));
      this.shuffleButton.setLabel(Main.playbackState.shuffle ? Icons.SHUFFLE_ON : Icons.SHUFFLE);
   }

   public void updateNowPlaying() {
   }

   public void nothingPlaying() {
   }

   public void updateCoverImage() {
   }

   private void drawFullName(class_332 context, int mouseX, int mouseY, int titleX, int artistX) {
      Objects.requireNonNull(this.field_22793);
      int textHeight = 9;
      int textWidth = 110;
      if (isInsideOf(mouseX, mouseY, titleX, 18, textWidth, textHeight)) {
         context.method_51438(this.field_22793, class_2561.method_54155(Main.currentlyPlaying.title), mouseX, mouseY);
      } else if (isInsideOf(mouseX, mouseY, artistX, 33, textWidth, textHeight)) {
         context.method_51438(this.field_22793, class_2561.method_30163(Main.currentlyPlaying.artist), mouseX, mouseY);
      }

   }

   private static boolean isInsideOf(int mouseX, int mouseY, int x, int y, int w, int h) {
      return mouseX >= x && mouseX <= x + w && mouseY >= y && mouseY <= y + h;
   }

   static {
      SUBSCREENS = Map.of(subscreenText(Icons.SEARCH, "ui.spotify_controller.subscreens.search"), SearchScreen.class);
   }
}
