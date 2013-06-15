/*
 * @author     ucchy
 * @license    GPLv3
 * @copyright  Copyright ucchy 2013
 */
package com.github.ucchyocean.et;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import org.bukkit.configuration.file.YamlConfiguration;

/**
 * プラグインのメッセージリソース管理クラス
 * @author ucchy
 */
public class Messages {

    private static final String FILE_NAME = "messages.yml";

    private static YamlConfiguration defaultMessages;
    private static YamlConfiguration resources;

    /**
     * 初期化する
     * @param filename ファイル名、nullや存在しないファイルが指定されたならデフォルトになる
     */
    protected static void initialize(String filename) {

        // デフォルトメッセージをロード
        if ( defaultMessages == null ) {
            defaultMessages = loadDefaultMessages();
        }

        // メッセージファイルをロード
        if ( filename == null ) {
            filename = FILE_NAME;
        }
        File file = new File(ExpTimer.getConfigFolder(), filename);
        if ( !file.exists() ) {
            Utility.copyFileFromJar(ExpTimer.getPluginJarFile(),
                    file, filename, false);
        }
        resources = YamlConfiguration.loadConfiguration(file);
    }

    /**
     * リソースを取得する
     * @param key リソースキー
     * @return リソース
     */
    protected static String get(String key) {

        if ( resources == null ) {
            initialize(FILE_NAME);
        }
        String def = defaultMessages.getString(key, "");
        return resources.getString(key, def);
    }

    /**
     * リソースを取得する
     * @param key リソースキー
     * @param args 引数
     * @return リソース
     */
    protected static String get(String key, Object... args) {

        return String.format(get(key), args);
    }

    /**
     * Jarファイル内から直接 messages.yml を読み込み、YamlConfigurationにして返すメソッド
     * @return
     */
    private static YamlConfiguration loadDefaultMessages() {

        YamlConfiguration messages = new YamlConfiguration();
        JarFile jarFile = null;
        try {
            jarFile = new JarFile(ExpTimer.getPluginJarFile());
            ZipEntry zipEntry = jarFile.getEntry(FILE_NAME);
            InputStream inputStream = jarFile.getInputStream(zipEntry);
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            String line;
            while ( (line = reader.readLine()) != null ) {
                if ( line.contains(":") && !line.startsWith("#") ) {
                    String key = line.substring(0, line.indexOf(":")).trim();
                    String value = line.substring(line.indexOf(":") + 1).trim();
                    if ( value.startsWith("'") && value.endsWith("'") )
                        value = value.substring(1, value.length()-1);
                    messages.set(key, value);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if ( jarFile != null ) {
                try {
                    jarFile.close();
                } catch (IOException e) {
                    // do nothing.
                }
            }
        }

        return messages;
    }
}
