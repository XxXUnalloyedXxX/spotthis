package com.tiji.spotify_controller;

import com.google.gson.Gson;
import com.tiji.spotify_controller.widgets.BooleanToggleWidget;
import com.tiji.spotify_controller.widgets.IntInputWidget;
import com.tiji.spotify_controller.widgets.ValueHolder;
import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import net.fabricmc.loader.api.FabricLoader;

public class SpotifyControllerConfig {
   private String clientId = "";
   private String clientSecret = "";
   private String authToken = "";
   private String refreshToken = "";
   private long lastRefresh = 0L;
   @SpotifyControllerConfig.EditableField(
      translationKey = "ui.spotify_controller.config.show_toast",
      widget = BooleanToggleWidget.class
   )
   private boolean shouldShowToasts = true;
   @SpotifyControllerConfig.EditableField(
      translationKey = "ui.spotify_controller.config.thread_image_io",
      widget = IntInputWidget.class
   )
   private int imageIoThreadCount = 4;
   @SpotifyControllerConfig.EditableField(
      translationKey = "ui.spotify_controller.config.brightness_weight",
      widget = IntInputWidget.class
   )
   private int brightnessFactor = 20;
   @SpotifyControllerConfig.EditableField(
      translationKey = "ui.spotify_controller.config.saturation_weight",
      widget = IntInputWidget.class
   )
   private int saturationFactor = 5;
   @SpotifyControllerConfig.EditableField(
      translationKey = "ui.spotify_controller.config.target_brightness",
      widget = IntInputWidget.class
   )
   private int targetBrightness = 70;
   @SpotifyControllerConfig.EditableField(
      translationKey = "ui.spotify_controller.config.sample_size",
      widget = IntInputWidget.class
   )
   private int sampleSize = 10;

   public String clientId() {
      return this.clientId;
   }

   public void clientId(String value) {
      this.clientId = value;
      this.writeToFile();
   }

   public String clientSecret() {
      return this.clientSecret;
   }

   public void clientSecret(String value) {
      this.clientSecret = value;
      this.writeToFile();
   }

   public String authToken() {
      return this.authToken;
   }

   public void authToken(String value) {
      this.authToken = value;
      this.writeToFile();
   }

   public String refreshToken() {
      return this.refreshToken;
   }

   public void refreshToken(String value) {
      this.refreshToken = value;
      this.writeToFile();
   }

   public long lastRefresh() {
      return this.lastRefresh;
   }

   public void lastRefresh(long value) {
      this.lastRefresh = value;
      this.writeToFile();
   }

   public boolean shouldShowToasts() {
      return this.shouldShowToasts;
   }

   public void shouldShowToasts(boolean value) {
      this.shouldShowToasts = value;
      this.writeToFile();
   }

   public int imageIoThreadCount() {
      return this.imageIoThreadCount;
   }

   public void imageIoThreadCount(byte value) {
      this.imageIoThreadCount = value;
      this.writeToFile();
   }

   public int brightnessFactor() {
      return this.brightnessFactor;
   }

   public void brightnessFactor(int value) {
      this.brightnessFactor = value;
      this.writeToFile();
   }

   public int saturationFactor() {
      return this.saturationFactor;
   }

   public void saturationFactor(int value) {
      this.saturationFactor = value;
      this.writeToFile();
   }

   public int targetBrightness() {
      return this.targetBrightness;
   }

   public void targetBrightness(int value) {
      this.targetBrightness = value;
      this.writeToFile();
   }

   public int sampleSize() {
      return this.sampleSize;
   }

   public void sampleSize(int value) {
      this.sampleSize = value;
      this.writeToFile();
   }

   public static SpotifyControllerConfig generate() {
      Path configDir = FabricLoader.getInstance().getConfigDir();
      Path configPath = configDir.resolve("spotify_controller.json");
      Path oldPath = configDir.resolve("media.json");

      try {
         if (Files.exists(configPath, new LinkOption[0])) {
            String json = Files.readString(configPath);
            return (SpotifyControllerConfig)(new Gson()).fromJson(json, SpotifyControllerConfig.class);
         } else if (Files.exists(oldPath, new LinkOption[0])) {
            Files.copy(oldPath, configPath, StandardCopyOption.REPLACE_EXISTING);
            return generate();
         } else {
            SpotifyControllerConfig newInstance = new SpotifyControllerConfig();
            newInstance.writeToFile(false);
            return newInstance;
         }
      } catch (IOException var4) {
         throw new RuntimeException(var4);
      }
   }

   public void writeToFile(boolean delete) {
      Path configPath = FabricLoader.getInstance().getConfigDir().resolve("spotify_controller.json");
      String config = (new Gson()).toJson(this);

      try {
         Files.writeString(configPath, config, new OpenOption[]{delete ? StandardOpenOption.TRUNCATE_EXISTING : StandardOpenOption.CREATE_NEW});
      } catch (IOException var5) {
         throw new RuntimeException(var5);
      }
   }

   public void writeToFile() {
      this.writeToFile(true);
   }

   public void resetConnection() {
      this.lastRefresh(0L);
      this.clientId("");
      this.clientSecret("");
      this.authToken("");
      this.refreshToken("");
   }

   @Retention(RetentionPolicy.RUNTIME)
   @Target({ElementType.FIELD})
   public @interface EditableField {
      String translationKey();

      Class<? extends ValueHolder> widget();
   }
}
