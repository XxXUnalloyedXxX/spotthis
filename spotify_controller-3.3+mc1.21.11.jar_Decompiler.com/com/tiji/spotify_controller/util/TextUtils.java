package com.tiji.spotify_controller.util;

import java.util.Optional;
import net.minecraft.class_11719;
import net.minecraft.class_2561;
import net.minecraft.class_2583;
import net.minecraft.class_310;
import net.minecraft.class_327;
import net.minecraft.class_5250;
import org.jetbrains.annotations.Contract;

public class TextUtils {
   private static final class_327 textRenderer;
   public static final class_11719 DEFAULT;

   @Contract("null, _ -> fail; _, _ -> new")
   public static class_2561 getTrantedText(class_2561 title, int maxWidth) {
      if (textRenderer.method_27525(title) <= maxWidth) {
         return title;
      } else {
         int ellipsisSize = textRenderer.method_1727("...");
         int remainingWidth = maxWidth - ellipsisSize;
         class_5250 result = class_2561.method_43470("");
         title.method_27658((style, asString) -> {
            class_2561 textToAppend = class_2561.method_43470(asString).method_10862(style);
            int width = textRenderer.method_27525(textToAppend);
            if (width <= remainingWidth) {
               result.method_10852(textToAppend);
               return Optional.empty();
            } else {
               result.method_10852(trimToWidth(asString, style, remainingWidth));
               return Optional.of(new Object());
            }
         }, class_2583.field_24360);
         return result.method_27693("...");
      }
   }

   private static class_2561 trimToWidth(String text, class_2583 style, int maxWidth) {
      class_2561 styledText = class_2561.method_43470(text).method_10862(style);
      int width = textRenderer.method_27525(styledText);
      if (width <= maxWidth) {
         return styledText;
      } else {
         int cutoff = (int)Math.ceil((double)((float)text.length() / 2.0F));
         String halfString = text.substring(0, cutoff);
         class_5250 halfText = class_2561.method_43470(halfString).method_10862(style);
         int halfWidth = textRenderer.method_27525(halfText);
         if (halfWidth >= maxWidth) {
            return (class_2561)(text.equals(halfString) ? class_2561.method_43470("") : trimToWidth(halfString, style, maxWidth));
         } else {
            class_2561 otherHalf = trimToWidth(text.substring(cutoff), style, maxWidth - halfWidth);
            return halfText.method_10852(otherHalf);
         }
      }
   }

   public static String[] warpText(String text, int maxWidth) {
      StringBuilder sb = new StringBuilder();
      StringBuilder textSoFar = new StringBuilder();
      StringBuilder currentWord = new StringBuilder();

      for(int i = 0; i < text.length(); ++i) {
         char c = text.charAt(i);
         currentWord.append(c);
         if (Character.isWhitespace(c)) {
            class_327 var10000 = textRenderer;
            String var10001 = String.valueOf(textSoFar);
            if (var10000.method_1727(var10001 + currentWord.toString()) > maxWidth) {
               sb.append(textSoFar).append("\n");
               textSoFar.setLength(0);
            }

            textSoFar.append(currentWord);
            currentWord.setLength(0);
         }
      }

      sb.append(textSoFar);
      return sb.toString().split("\n");
   }

   static {
      textRenderer = class_310.method_1551().field_1772;
      DEFAULT = class_11719.field_61972;
   }
}
