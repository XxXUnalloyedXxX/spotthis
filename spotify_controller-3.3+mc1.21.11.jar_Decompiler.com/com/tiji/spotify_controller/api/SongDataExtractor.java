package com.tiji.spotify_controller.api;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tiji.spotify_controller.Main;
import com.tiji.spotify_controller.ui.Icons;
import com.tiji.spotify_controller.util.ImageWithColor;
import java.net.URI;
import java.util.Iterator;
import net.minecraft.class_2561;
import net.minecraft.class_2960;
import org.jetbrains.annotations.Nullable;

public class SongDataExtractor {
   public static String getName(JsonObject trackObj) {
      return trackObj.get("name").getAsString();
   }

   public static String getArtist(JsonObject trackObj) {
      StringBuilder artists = new StringBuilder();
      Iterator var2 = trackObj.getAsJsonArray("artists").iterator();

      while(var2.hasNext()) {
         JsonElement artist = (JsonElement)var2.next();
         artists.append(artist.getAsJsonObject().get("name").getAsString()).append(", ");
      }

      artists.setLength(artists.length() - 2);
      return artists.toString();
   }

   public static String getId(JsonObject trackObj) {
      return trackObj.get("id").getAsString();
   }

   public static URI getSpotifyLink(JsonObject trackObj) {
      return URI.create(trackObj.getAsJsonObject("external_urls").get("spotify").getAsString());
   }

   public static double getDuration(JsonObject trackObj) {
      return trackObj.get("progress_ms").getAsDouble() / trackObj.getAsJsonObject("item").get("duration_ms").getAsDouble();
   }

   public static String getDurationLabel(JsonObject trackObj) {
      int duration = trackObj.get("duration_ms").getAsInt();
      duration /= 1000;
      Integer minutes_duration = duration / 60;
      Integer seconds_duration = duration % 60;
      return String.format("%02d:%02d", minutes_duration, seconds_duration);
   }

   public static String getProgressLabel(JsonObject trackObj) {
      int progress = trackObj.get("progress_ms").getAsInt();
      progress /= 1000;
      Integer minutes_progress = progress / 60;
      Integer seconds_progress = progress % 60;
      return String.format("%02d:%02d", minutes_progress, seconds_progress);
   }

   public static boolean isPlaying(JsonObject trackObj) {
      return trackObj.get("is_playing").getAsBoolean();
   }

   public static int getMaxDuration(JsonObject trackObj) {
      return trackObj.get("duration_ms").getAsInt();
   }

   public static boolean isExplicit(JsonObject trackObj) {
      return trackObj.get("explicit").getAsBoolean();
   }

   public static boolean getShuffleState(JsonObject trackObj) {
      return trackObj.get("shuffle_state").getAsBoolean();
   }

   public static String getRepeatState(JsonObject trackObj) {
      return trackObj.get("repeat_state").getAsString();
   }

   public static void reloadData(boolean forceFullReload, Runnable onNoUpdate, Runnable onDataUpdate, Runnable onImageLoad) {
      ApiCalls.getNowPlayingTrack((data) -> {
         if (data == null) {
            Main.currentlyPlaying = SongData.emptyData();
            Main.playbackState.canGoBack = false;
            Main.playbackState.canRepeat = false;
            Main.playbackState.canSeek = false;
            Main.playbackState.canSkip = false;
            Main.playbackState.canShuffle = false;
         } else {
            boolean isSongDifferent = !getId(data.getAsJsonObject("item")).equals(Main.currentlyPlaying.Id);
            Main.playbackState.progressLabel = getProgressLabel(data);
            Main.playbackState.isPlaying = isPlaying(data);
            Main.playbackState.progressValue = getDuration(data);
            Main.playbackState.repeat = getRepeatState(data);
            Main.playbackState.shuffle = getShuffleState(data);
            JsonObject disallows = data.getAsJsonObject("actions").getAsJsonObject("disallows");
            Main.playbackState.canShuffle = !disallows.has("toggling_shuffle");
            Main.playbackState.canRepeat = !disallows.has("toggling_repeat_context") && !disallows.has("toggling_repeat_track");
            Main.playbackState.canSkip = !disallows.has("skipping_next");
            Main.playbackState.canGoBack = !disallows.has("skipping_prev");
            Main.playbackState.canSeek = !disallows.has("seeking");
            if (isSongDifferent || forceFullReload) {
               Main.currentlyPlaying = getDataFor(data.getAsJsonObject("item"), onImageLoad);
            }

            ApiCalls.isSongLiked(Main.currentlyPlaying.Id, (isLiked) -> {
               Main.playbackState.isLiked = isLiked;
            });
            if (!isSongDifferent && !forceFullReload) {
               onNoUpdate.run();
            } else {
               onDataUpdate.run();
            }

         }
      });
   }

   public static SongData getDataFor(JsonObject data, @Nullable Runnable onImageLoad) {
      SongData song = new SongData();
      song.title = class_2561.method_43473().method_10852(isExplicit(data) ? Icons.EXPLICT : class_2561.method_43470("")).method_10852(class_2561.method_43470(getName(data)));
      song.artist = getArtist(data);
      song.durationLabel = getDurationLabel(data);
      song.Id = getId(data);
      song.duration = getMaxDuration(data);
      song.songURI = getSpotifyLink(data);
      if (!song.coverImage.image.method_12832().equals("ui/nothing.png")) {
         song.coverImage = new ImageWithColor(-1, class_2960.method_60655("spotify_controller", "ui/nothing.png"));
      }

      ImageDownloader.addDownloadTask(data, (image) -> {
         song.coverImage = image;
         if (onImageLoad != null) {
            onImageLoad.run();
         }

      });
      return song;
   }
}
