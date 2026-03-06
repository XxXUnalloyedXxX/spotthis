package com.tiji.spotify_controller.api;

import com.tiji.spotify_controller.util.ImageWithColor;
import java.net.URI;
import net.minecraft.class_2561;
import net.minecraft.class_2960;

public class SongData {
   public class_2561 title;
   public String artist;
   public ImageWithColor coverImage = new ImageWithColor(class_2960.method_60655("spotify_controller", "ui/nothing.png"));
   public String durationLabel;
   public Integer duration;
   public String Id = "";
   public URI songURI;

   public String toString() {
      String var10000 = String.valueOf(this.title);
      return "songData{title='" + var10000 + "', artist='" + this.artist + "', coverImage=" + String.valueOf(this.coverImage) + ", duration_label='" + this.durationLabel + "', Id='" + this.Id + "'\", songURI=" + String.valueOf(this.songURI) + "}";
   }

   public static SongData emptyData() {
      SongData songData = new SongData();
      songData.title = class_2561.method_43471("ui.spotify_controller.nothing_playing");
      songData.artist = class_2561.method_43471("ui.spotify_controller.unknown_artist").toString();
      songData.durationLabel = "00:00";
      songData.duration = 0;
      songData.Id = "";
      songData.songURI = null;
      return songData;
   }
}
