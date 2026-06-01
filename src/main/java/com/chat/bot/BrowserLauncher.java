package com.chat.bot;

import org.springframework.boot.web.server.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class BrowserLauncher implements ApplicationListener<WebServerInitializedEvent> {

    @Override
    public void onApplicationEvent(WebServerInitializedEvent event) {

        try {

            Thread.sleep(3000);
            int port = event.getWebServer().getPort();
            String url = "http://localhost:" + port;
            System.out.println("Opening browser: " + url);
            Runtime.getRuntime().exec(
                    "rundll32 url.dll,FileProtocolHandler " + url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
