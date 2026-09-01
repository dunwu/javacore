package io.github.dunwu.javacore.jdk11.http;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

/**
 * Java 11 标准 HttpClient 示例。
 * <p>
 * Java 9 以孵化 API 引入、Java 11 正式转正的 {@link HttpClient}，
 * 取代了功能有限的 {@code HttpURLConnection}，支持：
 * <ul>
 * <li>HTTP/1.1 和 HTTP/2 协议</li>
 * <li>同步（send）与异步（sendAsync）请求</li>
 * <li>基于 CompletableFuture 的响应式编程模型</li>
 * </ul>
 * 注意：本示例在无网络环境下会优雅降级，不会抛出未捕获异常。
 */
public class HttpClientDemo {

    private static final String URL = "https://www.example.com";

    /**
     * 创建 HttpClient（推荐使用 Builder 配置超时、重定向策略等）
     */
    private static HttpClient newClient() {
        return HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .connectTimeout(Duration.ofSeconds(5))
            .followRedirects(HttpClient.Redirect.NORMAL)
            .build();
    }

    public static void main(String[] args) {
        // 同步 GET 请求
        syncGet(newClient());

        // 异步 GET 请求
        asyncGet(newClient());

        // POST 请求
        post(newClient());
    }

    /**
     * 示例 1：同步 GET 请求
     */
    public static void syncGet() {
        syncGet(newClient());
    }

    private static void syncGet(HttpClient client) {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(URL))
            .GET()
            .timeout(Duration.ofSeconds(5))
            .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("同步 GET 状态码: " + response.statusCode());
            System.out.println("响应体长度: " + response.body().length());
        } catch (Exception e) {
            System.out.println("同步 GET 请求失败（可能是无网络环境）: " + e.getMessage());
        }
    }

    /**
     * 示例 2：异步 GET 请求（基于 CompletableFuture）
     */
    public static void asyncGet() {
        asyncGet(newClient());
    }

    private static void asyncGet(HttpClient client) {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(URL))
            .GET()
            .build();
        try {
            CompletableFuture<HttpResponse<String>> future = client
                .sendAsync(request, HttpResponse.BodyHandlers.ofString());
            HttpResponse<String> response = future.join();
            System.out.println("异步 GET 状态码: " + response.statusCode());
        } catch (Exception e) {
            System.out.println("异步 GET 请求失败（可能是无网络环境）: " + e.getMessage());
        }
    }

    /**
     * 示例 3：POST 请求携带 JSON 请求体
     */
    public static void post() {
        post(newClient());
    }

    private static void post(HttpClient client) {
        String jsonBody = "{\"name\": \"Java\", \"version\": 11}";
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(URL))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
            .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("POST 状态码: " + response.statusCode());
        } catch (Exception e) {
            System.out.println("POST 请求失败（可能是无网络环境）: " + e.getMessage());
        }
    }

}
