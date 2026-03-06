package com.tiji.spotify_controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.tiji.spotify_controller.api.ApiCalls;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.file.Path;
import java.util.concurrent.Executor;
import net.minecraft.class_310;
import net.minecraft.class_437;

public class WebGuideServer {
   public static HttpServer server;

   static String getMIMEType(String filename) {
      String extension = filename.substring(filename.lastIndexOf(46) + 1);
      byte var3 = -1;
      switch(extension.hashCode()) {
      case 98819:
         if (extension.equals("css")) {
            var3 = 1;
         }
         break;
      case 111145:
         if (extension.equals("png")) {
            var3 = 2;
         }
         break;
      case 3213227:
         if (extension.equals("html")) {
            var3 = 0;
         }
      }

      String var10000;
      switch(var3) {
      case 0:
         var10000 = "text/html";
         break;
      case 1:
         var10000 = "text/css";
         break;
      case 2:
         var10000 = "image/png";
         break;
      default:
         var10000 = "application/octet-stream";
      }

      return var10000;
   }

   public static void start() {
      try {
         server = HttpServer.create(new InetSocketAddress(25566), 0);
      } catch (IOException var1) {
         throw new RuntimeException(var1);
      }

      server.createContext("/callback", new WebGuideServer.callbackHandler());
      server.createContext("/data", new WebGuideServer.dataHandler());
      server.createContext("/", new WebGuideServer.rootHandler());
      server.setExecutor((Executor)null);
      server.start();
   }

   public static void stop() {
      server.stop(0);
   }

   private static class callbackHandler implements HttpHandler {
      public void handle(HttpExchange exchange) throws IOException {
         String Code = exchange.getRequestURI().getQuery().split("=")[1];
         Main.LOGGER.info("Callback Received: {}", Code);
         String var4 = class_310.method_1551().method_1526().method_4669();
         byte var5 = -1;
         switch(var4.hashCode()) {
         case 102218274:
            if (var4.equals("ko_kr")) {
               var5 = 0;
            }
         default:
            String var10000;
            switch(var5) {
            case 0:
               var10000 = "/allset/ko_kr.html";
               break;
            default:
               var10000 = "/allset/en_us.html";
            }

            String filepath = var10000;
            InputStream in = WebGuideServer.class.getResourceAsStream(filepath);

            int length;
            String response;
            try {
               if (in == null) {
                  throw new RuntimeException("Guide file is not found!");
               }

               byte[] file = in.readAllBytes();
               length = file.length;
               response = new String(file);
            } catch (Throwable var10) {
               if (in != null) {
                  try {
                     in.close();
                  } catch (Throwable var9) {
                     var10.addSuppressed(var9);
                  }
               }

               throw var10;
            }

            if (in != null) {
               in.close();
            }

            exchange.getResponseHeaders().set("Content-Type", "text/html");
            exchange.sendResponseHeaders(200, (long)length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
            ApiCalls.convertAccessToken(Code);
            class_310.method_1551().execute(() -> {
               class_310.method_1551().method_1507((class_437)null);
            });
            Main.LOGGER.info("Stopping Guide Server...");
            WebGuideServer.stop();
         }
      }
   }

   private static class dataHandler implements HttpHandler {
      public void handle(HttpExchange exchange) throws IOException {
         Main.CONFIG.clientSecret(exchange.getRequestHeaders().getFirst("Secret"));
         Main.CONFIG.clientId(exchange.getRequestHeaders().getFirst("Client-Id"));
         Main.LOGGER.info("Client Information Received");
         String response = "Received";
         exchange.sendResponseHeaders(200, (long)response.length());
         OutputStream os = exchange.getResponseBody();
         os.write(response.getBytes());
         os.close();
      }
   }

   private static class rootHandler implements HttpHandler {
      public void handle(HttpExchange exchange) throws IOException {
         URI path = exchange.getRequestURI();
         String filepath;
         if (path.getPath().equals("/")) {
            String var4 = class_310.method_1551().method_1526().method_4669();
            byte var5 = -1;
            switch(var4.hashCode()) {
            case 102218274:
               if (var4.equals("ko_kr")) {
                  var5 = 0;
               }
            default:
               String var10000;
               switch(var5) {
               case 0:
                  var10000 = "/guide/ko_kr.html";
                  break;
               default:
                  var10000 = "/guide/en_us.html";
               }

               filepath = var10000;
            }
         } else {
            filepath = String.valueOf(Path.of("/guide/", new String[]{path.getPath()}));
            filepath = filepath.replaceAll("\\\\", "/");
         }

         InputStream in = WebGuideServer.class.getResourceAsStream(filepath);

         byte[] data;
         try {
            if (in == null) {
               throw new RuntimeException("Guide file is not found!");
            }

            data = in.readAllBytes();
         } catch (Throwable var9) {
            if (in != null) {
               try {
                  in.close();
               } catch (Throwable var8) {
                  var9.addSuppressed(var8);
               }
            }

            throw var9;
         }

         if (in != null) {
            in.close();
         }

         exchange.getResponseHeaders().set("Content-Type", WebGuideServer.getMIMEType(filepath));
         exchange.sendResponseHeaders(200, (long)data.length);
         OutputStream os = exchange.getResponseBody();
         os.write(data);
         os.close();
      }
   }
}
