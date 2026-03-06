package com.tiji.spotify_controller.ui;

import com.tiji.spotify_controller.util.TextUtils;
import net.minecraft.class_2561;
import net.minecraft.class_2583;
import net.minecraft.class_2960;
import net.minecraft.class_5250;
import net.minecraft.class_11719.class_11721;

public class Icons {
   public static final class_2960 ICON_ID = class_2960.method_60655("spotify_controller", "icon");
   private static final class_2583 ICONS;
   private static final class_2561 RESETTER;
   public static final class_5250 NEXT;
   public static final class_5250 PREVIOUS;
   public static final class_5250 PAUSE;
   public static final class_5250 RESUME;
   public static final class_5250 SHUFFLE;
   public static final class_5250 SHUFFLE_ON;
   public static final class_5250 REPEAT;
   public static final class_5250 REPEAT_ON;
   public static final class_5250 REPEAT_SINGLE;
   public static final class_5250 EXPLICT;
   public static final class_5250 ADD_TO_FAV;
   public static final class_5250 REMOVE_FROM_FAV;
   public static final class_5250 SEARCH;
   public static final class_5250 ADD;
   public static final class_5250 PLAY;
   public static final class_5250 ADD_TO_QUEUE;
   public static final class_5250 POPUP_OPEN;

   static {
      ICONS = class_2583.field_24360.method_27704(new class_11721(ICON_ID));
      RESETTER = class_2561.method_43470("").method_10862(class_2583.field_24360.method_27704(TextUtils.DEFAULT));
      NEXT = class_2561.method_43470("1").method_10862(ICONS).method_10852(RESETTER);
      PREVIOUS = class_2561.method_43470("0").method_10862(ICONS).method_10852(RESETTER);
      PAUSE = class_2561.method_43470("2").method_10862(ICONS).method_10852(RESETTER);
      RESUME = class_2561.method_43470("3").method_10862(ICONS).method_10852(RESETTER);
      SHUFFLE = class_2561.method_43470("4").method_10862(ICONS).method_10852(RESETTER);
      SHUFFLE_ON = class_2561.method_43470("5").method_10862(ICONS).method_10852(RESETTER);
      REPEAT = class_2561.method_43470("6").method_10862(ICONS).method_10852(RESETTER);
      REPEAT_ON = class_2561.method_43470("7").method_10862(ICONS).method_10852(RESETTER);
      REPEAT_SINGLE = class_2561.method_43470("8").method_10862(ICONS).method_10852(RESETTER);
      EXPLICT = class_2561.method_43470("9").method_10862(ICONS).method_10852(RESETTER).method_27693(" ");
      ADD_TO_FAV = class_2561.method_43470("a").method_10862(ICONS).method_10852(RESETTER);
      REMOVE_FROM_FAV = class_2561.method_43470("b").method_10862(ICONS).method_10852(RESETTER);
      SEARCH = class_2561.method_43470("c").method_10862(ICONS).method_10852(RESETTER);
      ADD = class_2561.method_43470("d").method_10862(ICONS).method_10852(RESETTER);
      PLAY = class_2561.method_43470("e").method_10862(ICONS).method_10852(RESETTER);
      ADD_TO_QUEUE = class_2561.method_43470("f").method_10862(ICONS).method_10852(RESETTER);
      POPUP_OPEN = class_2561.method_43470("g").method_10862(ICONS).method_10852(RESETTER).method_27693(" ");
   }
}
