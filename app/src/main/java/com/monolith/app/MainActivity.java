package com.monolith.app;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.appcompat.app.AppCompatActivity;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;

public class MainActivity extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // 1. Инициализация Python
        if (!Python.isStarted()) {
            Python.start(new AndroidPlatform(this));
        }

        // 2. Запуск твоего Flask сервера в отдельном потоке
        new Thread(new Runnable() {
            @Override
            public void run() {
                Python py = Python.getInstance();
                // Предполагаем, что твой файл называется main.py 
                // и в нем есть функция start_server()
                py.getModule("main").callAttr("start_server");
            }
        }).start();

        // 3. Настройка интерфейса (WebView)
        webView = new WebView(this);
        setContentView(webView);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true); // Включаем JS для кнопок
        webSettings.setDomStorageEnabled(true); // Для локального хранилища

        // Чтобы ссылки открывались внутри приложения, а не в браузере
        webView.setWebViewClient(new WebViewClient());

        // Загружаем локальный адрес Flask
        // Подождем секунду, пока сервер поднимется (в идеале делать проверку)
        webView.postDelayed(() -> webView.loadUrl("http://127.0.0.1:8080/view/"), 1000);
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack(); // Назад по истории папок
        } else {
            super.onBackPressed();
        }
    }
}
