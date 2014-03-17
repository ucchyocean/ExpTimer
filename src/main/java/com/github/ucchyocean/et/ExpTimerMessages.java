/*
 * @author     ucchy
 * @license    LGPLv3
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
public class ExpTimerMessages {

    private static final String FILE_NAME = "messages.yml";

    private YamlConfiguration resources;

    /**
     * コンストラクタ
     */
    public ExpTimerMessages() {
        initialize(null);
    }

    /**
     * コンストラクタ
     * @param filename メッセージファイル
     */
    public ExpTimerMessages(String filename) {
        initialize(filename);
    }

    /**
     * 初期化する
     * @param filename ファイル名、nullや存在しないファイルが指定されたならデフォルトになる
     */
    private void initialize(String filename) {

        // メッセージファイルをロード
        if ( filename == null ) {
            filename = FILE_NAME;
        }
        File file = new File(ExpTimer.getConfigFolder(), filename);
        if ( !file.exists() ) {
            Utility.copyFileFromJar(
                    ExpTimer.getPluginJarFile(), file, filename);
        }
        resources = YamlConfiguration.loadConfiguration(file);

        // デフォルトメッセージをロード、デフォルトデータとして足す。
        YamlConfiguration defaultMessages = loadDefaultMessages();
        for ( String key : defaultMessages.getKeys(true) ) {
            resources.addDefault(key, defaultMessages.get(key));
        }
    }

    /**
     * リソースを取得する
     * @param key リソースキー
     * @return リソース
     */
    public String get(String key) {
        return resources.getString(key);
    }

    /**
     * 指定したキーがリソース定義されているかどうかを確認する
     * @param key リソースキー
     * @return 定義されているかどうか
     */
    public boolean containsKey(String key) {
        return resources.contains(key);
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
