package com.monolith.app;

import dev.rikka.shizuku.Shizuku;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ShizukuBridge {
    public static String exec(String command) {
        try {
            // Выполняем команду через оболочку Shizuku
            Process process = Shizuku.newProcess(new String[]{"sh", "-c", command}, null, null);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            process.waitFor();
            return output.toString();
        } catch (Exception e) {
            return "ERROR: " + e.getMessage();
        }
    }
}
