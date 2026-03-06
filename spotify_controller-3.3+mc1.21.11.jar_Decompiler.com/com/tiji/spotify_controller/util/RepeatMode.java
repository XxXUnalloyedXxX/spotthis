package com.tiji.spotify_controller.util;

import com.tiji.spotify_controller.ui.Icons;
import net.minecraft.class_2561;
import net.minecraft.class_5250;

public class RepeatMode {
   public static final String OFF = "off";
   public static final String CONTEXT = "context";
   public static final String TRACK = "track";

   public static class_2561 getAsText(String mode) {
      byte var2 = -1;
      switch(mode.hashCode()) {
      case 110621003:
         if (mode.equals("track")) {
            var2 = 1;
         }
         break;
      case 951530927:
         if (mode.equals("context")) {
            var2 = 0;
         }
      }

      class_5250 var10000;
      switch(var2) {
      case 0:
         var10000 = Icons.REPEAT_ON;
         break;
      case 1:
         var10000 = Icons.REPEAT_SINGLE;
         break;
      default:
         var10000 = Icons.REPEAT;
      }

      return var10000;
   }

   public static String getNextMode(String currentMode) {
      byte var2 = -1;
      switch(currentMode.hashCode()) {
      case 109935:
         if (currentMode.equals("off")) {
            var2 = 0;
         }
         break;
      case 951530927:
         if (currentMode.equals("context")) {
            var2 = 1;
         }
      }

      String var10000;
      switch(var2) {
      case 0:
         var10000 = "context";
         break;
      case 1:
         var10000 = "track";
         break;
      default:
         var10000 = "off";
      }

      return var10000;
   }
}
