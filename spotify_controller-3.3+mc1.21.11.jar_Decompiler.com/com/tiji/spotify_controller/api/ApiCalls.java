package com.tiji.spotify_controller.api;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.tiji.spotify_controller.Main;
import com.tiji.spotify_controller.WebGuideServer;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpRequest.Builder;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Base64;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_370;
import net.minecraft.class_370.class_9037;

public class ApiCalls {
   private static final HttpClient client = HttpClient.newHttpClient();
   private static final List<String> REQUIRED_SCOPES = List.of("user-read-playback-state", "user-modify-playback-state", "user-read-currently-playing", "user-read-private", "user-library-read", "user-library-modify");
   private static boolean cachedLikeStatus;
   private static String cachedSongId;
   private static long cachedLikeStatusTime;
   private static final int LIKE_CACHE_LIFETIME = 15000;
   private static final HashMap<String, Long> rateLimited = new HashMap();
   private static final List<String> handledErrors = List.of("NO_ACTIVE_DEVICE", "PREMIUM_REQUIRED");

   public static void convertAccessToken(String accessToken) {
      call("https://accounts.spotify.com/api/token?grant_type=authorization_code&redirect_uri=http://127.0.0.1:25566/callback&code=" + accessToken, getAuthorizationHeader(), "application/x-www-form-urlencoded", (body) -> {
         JsonObject data = (JsonObject)(new Gson()).fromJson((String)body.body(), JsonObject.class);
         verifyToken(data);
         Main.CONFIG.authToken(data.get("access_token").getAsString());
         Main.CONFIG.refreshToken(data.get("refresh_token").getAsString());
         Main.CONFIG.lastRefresh(System.currentTimeMillis());
         getSubscription();
         SongDataExtractor.reloadData(true, () -> {
         }, () -> {
         }, () -> {
         });
      }, "POST");
   }

   private static void verifyToken(JsonObject data) {
      boolean valid = true;
      String scopes = data.get("scope").getAsString();

      String scope;
      for(Iterator var3 = REQUIRED_SCOPES.iterator(); var3.hasNext(); valid &= scopes.contains(scope)) {
         scope = (String)var3.next();
      }

      if (!valid) {
         throw new RuntimeException("Invalid token! (probably modified auth)");
      }
   }

   public static void refreshAccessToken() {
      call("https://accounts.spotify.com/api/token?grant_type=refresh_token&refresh_token=" + Main.CONFIG.refreshToken(), getAuthorizationHeader(), "application/x-www-form-urlencoded", (body) -> {
         JsonObject data = (JsonObject)(new Gson()).fromJson((String)body.body(), JsonObject.class);
         if (data.has("error")) {
            Main.LOGGER.warn("Failed to refresh access token; Normally caused when developer app is deleted. {}: {}", data.get("error"), data.get("error_description"));
            Main.CONFIG.resetConnection();
         } else {
            verifyToken(data);
            Main.CONFIG.authToken(data.get("access_token").getAsString());
            if (data.has("refresh_token")) {
               Main.CONFIG.refreshToken(data.get("refresh_token").getAsString());
            }

            Main.CONFIG.lastRefresh(System.currentTimeMillis());
            getSubscription();
         }
      }, "POST");
   }

   public static void getNowPlayingTrack(Consumer<JsonObject> callback) {
      call("https://api.spotify.com/v1/me/player", getAuthorizationCode(), (String)null, (body) -> {
         callback.accept((JsonObject)(new Gson()).fromJson((String)body.body(), JsonObject.class));
      }, "GET");
   }

   public static void setPlaybackLoc(int position_ms) {
      call("https://api.spotify.com/v1/me/player/seek?position_ms=" + position_ms, getAuthorizationCode(), (String)null, (body) -> {
      }, "PUT");
   }

   public static void playPause(boolean state) {
      String uri;
      if (state) {
         uri = "https://api.spotify.com/v1/me/player/play";
      } else {
         uri = "https://api.spotify.com/v1/me/player/pause";
      }

      call(uri, getAuthorizationCode(), (String)null, (body) -> {
      }, "PUT");
   }

   public static void nextTrack() {
      if (!Main.playbackState.canSkip) {
         Main.showNotAllowedToast();
      } else {
         call("https://api.spotify.com/v1/me/player/next", getAuthorizationCode(), (String)null, (body) -> {
         }, "POST");
      }
   }

   public static void previousTrack() {
      if (!Main.playbackState.canGoBack) {
         Main.showNotAllowedToast();
      } else {
         call("https://api.spotify.com/v1/me/player/previous", getAuthorizationCode(), (String)null, (body) -> {
         }, "POST");
      }
   }

   public static void getUserName(Consumer<String> consumer) {
      call("https://api.spotify.com/v1/me", getAuthorizationCode(), (String)null, (body) -> {
         String name = ((JsonObject)(new Gson()).fromJson((String)body.body(), JsonObject.class)).get("display_name").getAsString();
         consumer.accept(name);
      }, "GET");
   }

   public static void getSubscription() {
      call("https://api.spotify.com/v1/me", getAuthorizationCode(), (String)null, (body) -> {
         String name = ((JsonObject)(new Gson()).fromJson((String)body.body(), JsonObject.class)).get("product").getAsString();
         Main.isPremium = name.equals("premium");
      }, "GET");
   }

   public static void setShuffle(boolean state) {
      if (!Main.playbackState.canShuffle) {
         Main.showNotAllowedToast();
      } else {
         call("https://api.spotify.com/v1/me/player/shuffle?state=" + (state ? "true" : "false"), getAuthorizationCode(), (String)null, (body) -> {
         }, "PUT");
      }
   }

   public static void setRepeat(String state) {
      if (!Main.playbackState.canRepeat) {
         Main.showNotAllowedToast();
      } else {
         call("https://api.spotify.com/v1/me/player/repeat?state=" + state, getAuthorizationCode(), (String)null, (body) -> {
         }, "PUT");
      }
   }

   public static void isSongLiked(String trackId, Consumer<Boolean> consumer) {
   }

   public static void toggleLikeSong(String trackId, boolean state) {
      call("https://api.spotify.com/v1/me/tracks?ids=" + trackId, getAuthorizationCode(), (String)null, (body) -> {
      }, state ? "PUT" : "DELETE");
   }

   public static void getSearch(String query, Consumer<JsonArray> consumer) {
      call("https://api.spotify.com/v1/search?q=" + URLEncoder.encode(query, StandardCharsets.UTF_8) + "&type=track", getAuthorizationCode(), (String)null, (body) -> {
         consumer.accept(((JsonObject)(new Gson()).fromJson((String)body.body(), JsonObject.class)).getAsJsonObject("tracks").getAsJsonArray("items"));
      }, "GET");
   }

   public static void setPlayingSong(String trackId) {
      call("https://api.spotify.com/v1/me/player/play", getAuthorizationCode(), (String)null, (body) -> {
      }, "PUT", "{\"uris\": [\"spotify:track:" + trackId + "\"]}");
   }

   public static void addSongToQueue(String trackId) {
      call("https://api.spotify.com/v1/me/player/queue?uri=spotify:track:" + trackId, getAuthorizationCode(), (String)null, (body) -> {
      }, "POST");
   }

   private static void call(String endpoint, String Authorization, String ContentType, Consumer<HttpResponse<String>> consumer, String method, String requestBody) {
      if (rateLimited.containsKey(endpoint)) {
         if ((Long)rateLimited.get(endpoint) > System.currentTimeMillis()) {
            return;
         }

         rateLimited.remove(endpoint);
      }

      Builder request = HttpRequest.newBuilder().uri(URI.create(endpoint)).timeout(Duration.ofSeconds(10L)).header("Authorization", Authorization);
      if (ContentType != null) {
         request.header("Content-Type", ContentType);
      }

      BodyPublisher publisher = BodyPublishers.ofString(requestBody);
      byte var9 = -1;
      switch(method.hashCode()) {
      case 70454:
         if (method.equals("GET")) {
            var9 = 0;
         }
         break;
      case 79599:
         if (method.equals("PUT")) {
            var9 = 2;
         }
         break;
      case 2461856:
         if (method.equals("POST")) {
            var9 = 1;
         }
         break;
      case 2012838315:
         if (method.equals("DELETE")) {
            var9 = 3;
         }
      }

      Builder var10000;
      switch(var9) {
      case 0:
         var10000 = request.GET();
         break;
      case 1:
         var10000 = request.POST(publisher);
         break;
      case 2:
         var10000 = request.PUT(publisher);
         break;
      case 3:
         var10000 = request.DELETE();
         break;
      default:
         var10000 = null;
      }

      request = var10000;
      if (request == null) {
         throw new RuntimeException("Invalid request method");
      } else {
         client.sendAsync(request.build(), BodyHandlers.ofString()).exceptionally((e) -> {
            Main.LOGGER.error("Failed to call API: {}", e.getMessage());
            return null;
         }).thenAccept((stringHttpResponse) -> {
            if (stringHttpResponse == null) {
               Main.LOGGER.warn("Empty response");
            } else {
               String responseBody = (String)stringHttpResponse.body();
               if (responseBody.isEmpty()) {
                  consumer.accept((Object)null);
               } else if (stringHttpResponse.statusCode() == 429) {
                  long retryAfter = stringHttpResponse.headers().firstValueAsLong("Retry-After").orElse(Long.MAX_VALUE);
                  Main.LOGGER.error("Rate limit hit for: endpoint: {}, retryAfter: {}", endpoint, retryAfter);
                  rateLimited.put(endpoint, System.currentTimeMillis() + retryAfter * 1000L);
               } else if (stringHttpResponse.statusCode() >= 400) {
                  JsonObject error = (JsonObject)(new Gson()).fromJson(responseBody, JsonObject.class);
                  if (!handleError(error)) {
                     Main.CONFIG.resetConnection();
                     WebGuideServer.start();
                  }

               } else {
                  consumer.accept(stringHttpResponse);
               }
            }
         });
      }
   }

   private static boolean handleError(JsonObject data) {
      if (!data.get("error").getAsJsonObject().has("reason")) {
         return false;
      } else {
         String reason = data.get("error").getAsJsonObject().get("reason").getAsString();
         if (handledErrors.contains(reason)) {
            class_310.method_1551().method_1566().method_1999(new class_370(new class_9037(), class_2561.method_43473(), class_2561.method_43471("api.spotify_controller.error." + reason)));
            return true;
         } else {
            Main.LOGGER.error("Unhandled error: {}", reason);
            return false;
         }
      }
   }

   private static void call(String endpoint, String Authorization, String ContentType, Consumer<HttpResponse<String>> consumer, String method) {
      call(endpoint, Authorization, ContentType, consumer, method, "");
   }

   private static String getAuthorizationHeader() {
      String clientId = Main.CONFIG.clientId();
      String clientSecret = Main.CONFIG.clientSecret();
      String encoded = Base64.getEncoder().encodeToString((clientId + ":" + clientSecret).getBytes());
      return "Basic " + encoded;
   }

   private static String getAuthorizationCode() {
      return "Bearer " + Main.CONFIG.authToken();
   }
}
