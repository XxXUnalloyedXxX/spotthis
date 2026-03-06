package com.tiji.spotify_controller.ui;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tiji.spotify_controller.api.ApiCalls;
import com.tiji.spotify_controller.widgets.SongListItem;
import com.tiji.spotify_controller.widgets.StringInputWidget;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.class_11909;
import net.minecraft.class_2561;
import net.minecraft.class_332;
import net.minecraft.class_364;
import net.minecraft.class_4068;

public class SearchScreen extends SecondaryBaseScreen {
   private static final int WIDTH = 300;
   private static final int MARGIN = 10;
   private static final int SCROLLBAR_WIDTH = 4;
   private final ArrayList<class_4068> searchResults = new ArrayList(20);
   private float scrollBarPos;
   private int offset;

   protected void method_25426() {
      super.method_25426();
      StringInputWidget searchField = new StringInputWidget(this.field_22793, 10, 10, 280, 20, class_2561.method_43473(), Icons.SEARCH, this::search);
      this.method_37063(searchField);
      this.method_25395(searchField);
   }

   public void method_25394(class_332 context, int mouseX, int mouseY, float delta) {
      super.method_25394(context, mouseX, mouseY, delta);
      int resultSpace = this.field_22790 - 20 - 20 - 50;
      int contentHeight = Math.max(0, this.searchResults.size() * 60 - 10);
      int scrollBarSize = (int)((float)resultSpace / (float)contentHeight * (float)resultSpace);
      int scrollBarPos = (int)((float)(resultSpace - scrollBarSize) * this.scrollBarPos);
      this.offset = 0;
      if (resultSpace < contentHeight) {
         context.method_25294(286, 40, 290, this.field_22790 - 50, -1426063361);
         context.method_25294(286, 40 + scrollBarPos, 290, 40 + scrollBarPos + scrollBarSize, -1);
         this.offset = -((int)((float)(contentHeight - resultSpace) * this.scrollBarPos));
      }

      context.method_44379(0, 40, 286, this.field_22790 - 50);
      context.method_51448().pushMatrix();
      context.method_51448().translate(0.0F, (float)this.offset);
      synchronized(this.searchResults) {
         Iterator var10 = this.searchResults.iterator();

         while(true) {
            if (!var10.hasNext()) {
               break;
            }

            class_4068 searchResult = (class_4068)var10.next();
            searchResult.method_25394(context, mouseX, mouseY - this.offset, delta);
         }
      }

      context.method_51448().popMatrix();
      context.method_44380();
   }

   private void search(String query) {
      if (!query.isEmpty()) {
         Iterator var2 = this.searchResults.iterator();

         while(var2.hasNext()) {
            class_4068 searchResult = (class_4068)var2.next();
            this.method_37066((class_364)searchResult);
         }

         synchronized(this.searchResults) {
            this.searchResults.clear();
            this.scrollBarPos = 0.0F;
         }

         ApiCalls.getSearch(query, (results) -> {
            synchronized(this.searchResults) {
               int y = 40;

               for(Iterator var4 = results.iterator(); var4.hasNext(); y += 60) {
                  JsonElement result = (JsonElement)var4.next();
                  JsonObject jsonObject = result.getAsJsonObject();
                  SongListItem item = new SongListItem(jsonObject, 10, y);
                  this.searchResults.add(item);
               }

            }
         });
      }
   }

   public boolean method_25401(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
      this.scrollBarPos = (float)Math.clamp((double)this.scrollBarPos - verticalAmount * 0.029999999329447746D, 0.0D, 1.0D);
      return true;
   }

   public boolean method_25402(class_11909 mouseButtonEvent, boolean doubleClicked) {
      if (!super.method_25402(mouseButtonEvent, doubleClicked)) {
         Iterator var3 = this.searchResults.iterator();

         while(var3.hasNext()) {
            class_4068 searchResult = (class_4068)var3.next();
            class_364 selectable = (class_364)searchResult;
            if (selectable.method_25402(mouseButtonEvent, doubleClicked)) {
               return true;
            }
         }
      }

      return false;
   }
}
