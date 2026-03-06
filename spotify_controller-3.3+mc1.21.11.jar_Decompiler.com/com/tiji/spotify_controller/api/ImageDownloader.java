package com.tiji.spotify_controller.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.tiji.spotify_controller.Main;
import com.tiji.spotify_controller.util.ImageWithColor;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;
import javax.imageio.ImageIO;
import net.minecraft.class_1011;
import net.minecraft.class_1043;
import net.minecraft.class_1060;
import net.minecraft.class_2960;
import net.minecraft.class_310;

public class ImageDownloader {
   private static final ArrayList<class_2960> loadedCover = new ArrayList();
   private static final ArrayBlockingQueue<JsonObject> queue = new ArrayBlockingQueue(200);
   private static final HashMap<JsonObject, ArrayList<Consumer<ImageWithColor>>> onComplete = new HashMap();
   public static final class_310 client = class_310.method_1551();

   private static ImageWithColor getAlbumCover(JsonObject trackObj) {
      try {
         class_2960 id = class_2960.method_60655("spotify_controller", SongDataExtractor.getId(trackObj).toLowerCase());
         int wantedSize = 100 * (Integer)client.field_1690.method_42474().method_41753();
         if (wantedSize == 0) {
            wantedSize = Integer.MAX_VALUE;
         }

         int closest = Integer.MAX_VALUE;
         JsonArray ImageList = trackObj.getAsJsonObject("album").getAsJsonArray("images");
         String closestUrl = ImageList.get(0).getAsJsonObject().get("url").getAsString();

         for(int i = 0; i < ImageList.size(); ++i) {
            int size = ImageList.get(i).getAsJsonObject().get("height").getAsInt();
            if (closest > size && size >= wantedSize) {
               closest = size;
               closestUrl = ImageList.get(i).getAsJsonObject().get("url").getAsString();
            }
         }

         InputStream albumCoverUrl = (new URI(closestUrl)).toURL().openStream();
         BufferedImage jpegImage = ImageIO.read(albumCoverUrl);
         ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
         ImageIO.write(jpegImage, "png", outputStream);
         ByteArrayInputStream imageStream = new ByteArrayInputStream(outputStream.toByteArray());
         CountDownLatch latch = new CountDownLatch(1);
         class_1011 image = class_1011.method_4309(imageStream);
         client.execute(() -> {
            class_1060 var10000 = client.method_1531();
            Objects.requireNonNull(id);
            var10000.method_4616(id, new class_1043(id::method_12832, image));
            latch.countDown();
         });
         latch.await();
         loadedCover.add(id);
         return new ImageWithColor(image, id);
      } catch (IOException var12) {
         Main.LOGGER.error("Failed to download album cover for {}: {}", SongDataExtractor.getId(trackObj), var12);
         Main.LOGGER.error(trackObj.toString());
      } catch (NullPointerException var13) {
         Main.LOGGER.error("Unexpected response from Spotify: {}\n{}", trackObj, var13.getLocalizedMessage());
      } catch (InterruptedException var14) {
         Thread.currentThread().interrupt();
      } catch (URISyntaxException var15) {
         throw new RuntimeException(var15);
      }

      return new ImageWithColor(-1, class_2960.method_60655("spotify_controller", "ui/nothing.png"));
   }

   public static void addDownloadTask(JsonObject data, Consumer<ImageWithColor> callback) {
      if (loadedCover.contains(class_2960.method_60655("spotify_controller", SongDataExtractor.getId(data).toLowerCase()))) {
         Main.LOGGER.debug("Cache hit for {}", SongDataExtractor.getId(data));
         CompletableFuture.runAsync(() -> {
            callback.accept(new ImageWithColor(class_2960.method_60655("spotify_controller", SongDataExtractor.getId(data).toLowerCase())));
         });
      } else {
         Main.LOGGER.debug("Adding download task lister for {}", SongDataExtractor.getId(data));
         if (onComplete.containsKey(data)) {
            ((ArrayList)onComplete.get(data)).add(callback);
         } else {
            ArrayList<Consumer<ImageWithColor>> callbacks = new ArrayList();
            callbacks.add(callback);
            onComplete.put(data, callbacks);
            queue.add(data);
            Main.LOGGER.debug("Added download task for {} - Queue size: {}", SongDataExtractor.getId(data), queue.size());
         }
      }
   }

   public static void startThreads() {
      for(int i = 0; i < Main.CONFIG.imageIoThreadCount(); ++i) {
         Thread thread = new Thread((ThreadGroup)null, ImageDownloader::threadWorker, "Image-IO-" + i);
         thread.start();
      }

   }

   private static void threadWorker() {
      while(!Thread.interrupted()) {
         try {
            JsonObject task = (JsonObject)queue.take();
            ImageWithColor coverId = getAlbumCover(task);
            Iterator var8 = ((ArrayList)onComplete.remove(task)).iterator();

            while(var8.hasNext()) {
               Consumer<ImageWithColor> callback = (Consumer)var8.next();
               callback.accept(coverId);
            }

            Main.LOGGER.debug("Finished downloading cover for {}", SongDataExtractor.getId(task));
         } catch (Exception var6) {
            StringBuilder sb = new StringBuilder();
            StackTraceElement[] var2 = var6.getStackTrace();
            int var3 = var2.length;

            for(int var4 = 0; var4 < var3; ++var4) {
               StackTraceElement element = var2[var4];
               sb.append("at ");
               sb.append(element.toString()).append("\n");
            }

            Main.LOGGER.error("Error in Image-IO thread: {}\n{}", var6.getLocalizedMessage(), sb);
         }
      }

   }
}
