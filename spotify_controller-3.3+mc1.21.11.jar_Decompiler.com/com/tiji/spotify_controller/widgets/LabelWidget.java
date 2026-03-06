package com.tiji.spotify_controller.widgets;

import java.util.function.Consumer;
import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_339;
import net.minecraft.class_364;
import net.minecraft.class_4068;
import net.minecraft.class_6379;
import net.minecraft.class_6382;
import net.minecraft.class_8021;
import net.minecraft.class_8030;
import net.minecraft.class_6379.class_6380;

public class LabelWidget implements class_4068, class_364, class_6379, class_8021 {
   private int x;
   private int y;
   private class_2561 text;

   public LabelWidget(int x, int y, class_2561 text) {
      this.x = x;
      this.y = y;
      this.text = text;
   }

   public void method_25394(class_332 context, int mouseX, int mouseY, float delta) {
      context.method_51439(class_310.method_1551().field_1772, this.text, this.x, this.y, -1, false);
   }

   public int method_46426() {
      return this.x;
   }

   public void method_46421(int x) {
      this.x = x;
   }

   public int method_46427() {
      return this.y;
   }

   public int method_25368() {
      return 0;
   }

   public int method_25364() {
      return 0;
   }

   public void method_48206(Consumer<class_339> consumer) {
   }

   public void method_25365(boolean focused) {
   }

   public void method_46419(int y) {
      this.y = y;
   }

   public class_2561 getText() {
      return this.text;
   }

   public void setText(class_2561 text) {
      this.text = text;
   }

   public boolean method_25370() {
      return false;
   }

   public class_8030 method_48202() {
      return super.method_48202();
   }

   public class_6380 method_37018() {
      return class_6380.field_33784;
   }

   public void method_37020(class_6382 builder) {
   }
}
