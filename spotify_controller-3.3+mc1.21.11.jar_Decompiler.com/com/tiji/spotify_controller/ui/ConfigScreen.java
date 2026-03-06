package com.tiji.spotify_controller.ui;

import com.tiji.spotify_controller.Main;
import com.tiji.spotify_controller.SpotifyControllerConfig;
import com.tiji.spotify_controller.WebGuideServer;
import com.tiji.spotify_controller.api.ApiCalls;
import com.tiji.spotify_controller.widgets.BorderedButtonWidget;
import com.tiji.spotify_controller.widgets.LabelWidget;
import com.tiji.spotify_controller.widgets.ValueHolder;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;
import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_364;
import net.minecraft.class_4068;
import net.minecraft.class_437;
import net.minecraft.class_5250;
import net.minecraft.class_6379;

public class ConfigScreen extends BaseScreen {
   private final class_437 parent;
   private LabelWidget userNameWidget;
   private BorderedButtonWidget resetButton;
   private ConfigScreen.ResetConfirmStatus resetConfirmStatus;
   private String userName;
   private static final int WIDTH = 200;
   private static final int MARGIN = 10;
   private static final int FIELD_HEIGHT = 20;
   private final HashMap<Field, ValueHolder> map;

   public ConfigScreen(class_437 parent) {
      super(true);
      this.resetConfirmStatus = ConfigScreen.ResetConfirmStatus.IDLE;
      this.map = new HashMap();
      this.parent = parent;
      if (!Main.isNotSetup()) {
         ApiCalls.getUserName((name) -> {
            this.userName = name;
            if (this.userNameWidget != null) {
               this.userNameWidget.setText(class_2561.method_43469("ui.spotify_controller.status.setup", new Object[]{this.userName}));
            }

         });
      }

   }

   public void method_25426() {
      super.method_25426();
      int y = 10;
      class_5250 statusText;
      if (Main.isNotSetup()) {
         statusText = class_2561.method_43471("ui.spotify_controller.status.not_setup");
      } else if (this.userName == null) {
         statusText = class_2561.method_43471("ui.spotify_controller.loading");
      } else {
         statusText = class_2561.method_43469("ui.spotify_controller.status.setup", new Object[]{this.userName});
      }

      this.userNameWidget = new LabelWidget(10 + this.widgetsOffset, y, statusText);
      this.method_37063(this.userNameWidget);
      Objects.requireNonNull(this.field_22793);
      int y = y + 9 + 10;
      this.resetButton = new BorderedButtonWidget(this.resetConfirmStatus.text, 10 + this.widgetsOffset, y, this::onResetButtonPress, false, 200);
      this.method_37063(this.resetButton);
      y += this.resetButton.method_25364() + 30;
      Field[] var3 = SpotifyControllerConfig.class.getDeclaredFields();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Field field = var3[var5];
         SpotifyControllerConfig.EditableField metadata = (SpotifyControllerConfig.EditableField)field.getAnnotation(SpotifyControllerConfig.EditableField.class);
         if (metadata != null) {
            try {
               Constructor var10000 = metadata.widget().getConstructor(Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE);
               Object[] var10001 = new Object[]{10 + this.widgetsOffset, null, null, null};
               Objects.requireNonNull(this.field_22793);
               var10001[1] = y + 9;
               var10001[2] = 200;
               var10001[3] = 20;
               ValueHolder widget = (ValueHolder)var10000.newInstance(var10001);
               field.setAccessible(true);
               widget.setValue(field.get(Main.CONFIG));
               this.method_37063((class_364)((class_6379)((class_4068)widget)));
               this.method_37063(new LabelWidget(10 + this.widgetsOffset, y, class_2561.method_43471(metadata.translationKey())));
               this.map.put(field, widget);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException var9) {
               throw new RuntimeException(var9);
            }

            Objects.requireNonNull(this.field_22793);
            y += 30 + 9;
         }
      }

   }

   private void onResetButtonPress() {
      if (!Main.isNotSetup()) {
         if (this.resetConfirmStatus == ConfigScreen.ResetConfirmStatus.IDLE) {
            this.resetConfirmStatus = ConfigScreen.ResetConfirmStatus.CONFIRM;
         } else if (this.resetConfirmStatus == ConfigScreen.ResetConfirmStatus.CONFIRM) {
            this.resetConfirmStatus = ConfigScreen.ResetConfirmStatus.CONFIRMED;
            Main.CONFIG.resetConnection();
            WebGuideServer.start();
         }

         this.resetButton.setLabel(this.resetConfirmStatus.text);
      }
   }

   public void method_25419() {
      Iterator var1 = this.map.keySet().iterator();

      while(var1.hasNext()) {
         Field field = (Field)var1.next();
         field.setAccessible(true);

         try {
            field.set(Main.CONFIG, ((ValueHolder)this.map.get(field)).getValue_());
         } catch (IllegalAccessException var4) {
            throw new RuntimeException(var4);
         }
      }

      Main.CONFIG.writeToFile();
      class_310.method_1551().method_1507(this.parent);
   }

   private static enum ResetConfirmStatus {
      IDLE(class_2561.method_43471("ui.spotify_controller.reset_config")),
      CONFIRM(class_2561.method_43471("ui.spotify_controller.reset_config_confirm")),
      CONFIRMED(class_2561.method_43471("ui.spotify_controller.reset_config_success"));

      public final class_2561 text;

      private ResetConfirmStatus(class_2561 text) {
         this.text = text;
      }

      // $FF: synthetic method
      private static ConfigScreen.ResetConfirmStatus[] $values() {
         return new ConfigScreen.ResetConfirmStatus[]{IDLE, CONFIRM, CONFIRMED};
      }
   }
}
