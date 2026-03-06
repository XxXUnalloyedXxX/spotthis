package com.tiji.spotify_controller;

import com.tiji.spotify_controller.api.ApiCalls;
import com.tiji.spotify_controller.api.ImageDownloader;
import com.tiji.spotify_controller.api.SongData;
import com.tiji.spotify_controller.api.SongDataExtractor;
import com.tiji.spotify_controller.ui.NowPlayingScreen;
import com.tiji.spotify_controller.ui.SetupScreen;
import java.util.Objects;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.class_2561;
import net.minecraft.class_2960;
import net.minecraft.class_304;
import net.minecraft.class_310;
import net.minecraft.class_370;
import net.minecraft.class_304.class_11900;
import net.minecraft.class_370.class_9037;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main implements ClientModInitializer {
   public static final String MOD_ID = "spotify_controller";
   public static final Logger LOGGER = LoggerFactory.getLogger("spotify_controller");
   public static SpotifyControllerConfig CONFIG = new SpotifyControllerConfig();
   private static final class_304 SETUP_KEY;
   public static int tickCount;
   public static NowPlayingScreen nowPlayingScreen;
   public static SongData currentlyPlaying;
   public static PlaybackState playbackState;
   public static boolean isPremium;
   public static boolean isStarted;

   public void onInitializeClient() {
      FabricLoader.getInstance().getModContainer("spotify_controller").ifPresent((modContainer) -> {
         if (!ResourceManagerHelper.registerBuiltinResourcePack(class_2960.method_60655("spotify_controller", "higher_res"), modContainer, ResourcePackActivationType.NORMAL)) {
            LOGGER.error("High Resolution RP failed load!");
         }

      });
      CONFIG = SpotifyControllerConfig.generate();
      KeyBindingHelper.registerKeyBinding(SETUP_KEY);
      ImageDownloader.startThreads();
      if (isNotSetup()) {
         WebGuideServer.start();
      } else {
         ApiCalls.refreshAccessToken();
      }

      ClientLifecycleEvents.CLIENT_STARTED.register((client) -> {
         isStarted = true;
         if (!isNotSetup()) {
            SongDataExtractor.reloadData(true, () -> {
            }, () -> {
            }, () -> {
            });
         }

      });
      ClientTickEvents.END_CLIENT_TICK.register((client) -> {
         while(SETUP_KEY.method_1436()) {
            if (isNotSetup()) {
               client.method_1507(new SetupScreen());
            } else {
               nowPlayingScreen = new NowPlayingScreen();
               nowPlayingScreen.updateCoverImage();
               nowPlayingScreen.updateNowPlaying();
               client.method_1507(nowPlayingScreen);
            }
         }

         if (!isNotSetup() && tickCount % 10 == 0) {
            if (nowPlayingScreen != null) {
               NowPlayingScreen var10001 = nowPlayingScreen;
               Objects.requireNonNull(var10001);
               Runnable var1 = var10001::updateStatus;
               NowPlayingScreen var10002 = nowPlayingScreen;
               Objects.requireNonNull(var10002);
               SongDataExtractor.reloadData(false, var1, var10002::updateNowPlaying, () -> {
                  nowPlayingScreen.updateCoverImage();
                  if (CONFIG.shouldShowToasts() && isStarted) {
                     showNewSongToast();
                  }

               });
            } else {
               SongDataExtractor.reloadData(false, () -> {
               }, () -> {
               }, () -> {
                  if (CONFIG.shouldShowToasts() && isStarted) {
                     showNewSongToast();
                  }

               });
            }

            if ((double)CONFIG.lastRefresh() + 1800000.0D < (double)System.currentTimeMillis()) {
               ApiCalls.refreshAccessToken();
            }
         }

         ++tickCount;
      });
   }

   private static void showNewSongToast() {
      (new SongToast(currentlyPlaying.coverImage, currentlyPlaying.artist, currentlyPlaying.title)).show(class_310.method_1551().method_1566());
   }

   public static boolean isNotSetup() {
      return CONFIG.clientId().isEmpty() || CONFIG.authToken().isEmpty() || CONFIG.refreshToken().isEmpty();
   }

   public static void showNotAllowedToast() {
      class_310.method_1551().method_1566().method_1999(new class_370(new class_9037(), class_2561.method_43471("ui.spotify_controller.not_allowed.title"), class_2561.method_43471("ui.spotify_controller.not_allowed.message")));
   }

   static {
      SETUP_KEY = new class_304("key.spotify_controller.general", 90, class_11900.field_62556);
      tickCount = 0;
      nowPlayingScreen = null;
      currentlyPlaying = SongData.emptyData();
      playbackState = new PlaybackState();
      isPremium = false;
      isStarted = false;
   }
}
