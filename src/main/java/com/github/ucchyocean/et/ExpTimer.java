/*
 * @author     ucchy
 * @license    LGPLv3
 * @copyright  Copyright ucchy 2013
 */
package com.github.ucchyocean.et;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * 経験値タイマー
 * @author ucchy
 */
public class ExpTimer extends JavaPlugin implements Listener {

    private static ExpTimer instance;
    private TimerTask timer;

    protected ExpTimerConfigData configData;
    protected HashMap<String, ExpTimerConfigData> configs;
    private CommandSender currentCommandSender;

    /**
     * @see org.bukkit.plugin.java.JavaPlugin#onEnable()
     */
    @Override
    public void onEnable() {

        instance = this;

        // コンフィグのロード
        reloadConfigData();

        // コマンドの登録
        ExpTimerCommand executor = new ExpTimerCommand(this);
        getCommand("exptimer").setExecutor(executor);
        getCommand("exptimer").setTabCompleter(executor);

        // イベントの登録
        getServer().getPluginManager().registerEvents(this, this);

        // ColorTeaming のロード
        if ( getServer().getPluginManager().isPluginEnabled("ColorTeaming") ) {
            Plugin temp = getServer().getPluginManager().getPlugin("ColorTeaming");
            String ctversion = temp.getDescription().getVersion();
            if ( Utility.isUpperVersion(ctversion, "2.3.0") ) {
                getLogger().info("ColorTeaming was loaded. "
                        + getDescription().getName() + " is in cooperation with ColorTeaming.");
                ColorTeamingListener listener = new ColorTeamingListener(this);
                getServer().getPluginManager().registerEvents(listener, this);
            } else {
                getLogger().warning("ColorTeaming was too old. The cooperation feature will be disabled.");
                getLogger().warning("NOTE: Please use ColorTeaming v2.3.0 or later version.");
            }
        }
    }

    /**
     * @see org.bukkit.plugin.java.JavaPlugin#onDisable()
     */
    @Override
    public void onDisable() {

        // タスクが残ったままなら、強制終了しておく。
        if ( timer != null ) {
            getLogger().warning("タイマーが残ったままです。強制終了します。");
            cancelTask();
        }
    }

    /**
     * config.yml を再読み込みする
     */
    protected void reloadConfigData() {

        File file = new File(ExpTimer.getConfigFolder(), "config.yml");
        if ( !file.exists() ) {
            Utility.copyFileFromJar(
                    ExpTimer.getPluginJarFile(), file, "config_ja.yml");
        }

        reloadConfig();
        FileConfiguration config = getConfig();
        configs = new HashMap<String, ExpTimerConfigData>();

        // デフォルトのデータ読み込み
        ConfigurationSection section = config.getConfigurationSection("default");
        ExpTimerConfigData defaultData =
                ExpTimerConfigData.loadFromSection("default", section, null);
        configData = defaultData;
        configs.put("default", defaultData);

        // デフォルト以外のデータ読み込み
        for ( String key : config.getKeys(false) ) {
            if ( key.equals("default") ) {
                continue;
            }
            section = config.getConfigurationSection(key);
            ExpTimerConfigData data = ExpTimerConfigData.loadFromSection(key, section, defaultData);
            configs.put(key, data);
        }
    }

    /**
     * 全プレイヤーの経験値レベルを設定する
     * @param level 設定するレベル
     */
    protected static void setExpLevel(int level, int max) {

        float progress = (float)level / (float)max;
        if ( progress > 1.0f ) {
            progress = 1.0f;
        } else if ( progress < 0.0f ) {
            progress = 0.0f;
        }
        if ( level > 24000 ) {
            level = 24000;
        } else if ( level < 0 ) {
            level = 0;
        }

        Player[] players = instance.getServer().getOnlinePlayers();
        for ( Player player : players ) {
            player.setLevel(level);
            player.setExp(progress);
        }
    }

    /**
     * 新しいタスクを開始する
     * @param config コンフィグ。nullならそのままconfigを変更せずにタスクを開始する
     * @param sender コマンド実行者。nullならcurrentCommandSenderがそのまま引き継がれる
     */
    public void startNewTask(ExpTimerConfigData config, CommandSender sender) {

        if ( config != null ) {
            configData = config;
        }
        if ( sender != null ) {
            currentCommandSender = sender;
        }
        timer = new TimerTask(this, configData);
        timer.startTimer();
    }

    /**
     * 新しいタスクを開始する
     * @param configName コンフィグ名。nullならそのままconfigを変更せずにタスクを開始する
     * @param sender コマンド実行者。nullならcurrentCommandSenderがそのまま引き継がれる
     */
    public void startNewTask(String configName, CommandSender sender) {

        if ( configName == null || !configs.containsKey(configName) ) {
            startNewTask((ExpTimerConfigData)null, sender);
        }
        startNewTask(configs.get(configName).clone(), sender);
    }

    /**
     * 現在実行中のタスクを中断する。終了時のコマンドは実行されない。
     */
    public void cancelTask() {

        if ( timer != null ) {

            // サイドバーのクリア
            if ( configData.useSideBar ) {
                timer.removeSidebar();
            }

            // タスクを終了する
            timer.endTimer();
            timer = null;

            // 経験値バーのクリア
            if ( configData.useExpBar ) {
                ExpTimer.setExpLevel(0, 1);
            }
        }
    }

    /**
     * 現在実行中のタスクを終了コマンドを実行してから終了する
     * @param invokeNextConfig nextConfigを起動するかどうか
     */
    public void endTask(boolean invokeNextConfig) {

        if ( timer != null ) {
            // 終了コマンドを実行してタスクを終了する

            dispatchCommandsBySender(configData.commandsOnEnd);
            dispatchCommandsByConsole(configData.consoleCommandsOnEnd);
            cancelTask();

            // リピート設定なら、新しいタスクを再スケジュール
            if ( invokeNextConfig &&
                    configData.nextConfig != null &&
                    configs.containsKey(configData.nextConfig) ) {
                startNewTask(configData.nextConfig, null);
            }
        }
    }

    /**
     * 現在のタイマータスクを取得する
     * @return タスク
     */
    public TimerTask getTask() {
        return timer;
    }

    /**
     * 現在のタイマーコンフィグデータを取得する
     * @return タイマーコンフィグデータ
     */
    public ExpTimerConfigData getConfigData() {
        return configData;
    }

    /**
     * 指定された名前のコンフィグを取得する。
     * @param name コンフィグ名
     * @return コンフィグ
     */
    public ExpTimerConfigData getConfigData(String name) {
        return configs.get(name);
    }

    /**
     * プラグインのデータフォルダを返す
     * @return プラグインのデータフォルダ
     */
    protected static File getConfigFolder() {
        return instance.getDataFolder();
    }

    /**
     * プラグインのJarファイル自体を示すFileオブジェクトを返す
     * @return プラグインのJarファイル
     */
    protected static File getPluginJarFile() {
        return instance.getFile();
    }

    /**
     * キャッシュされているCommandSenderで、指定されたコマンドをまとめて実行する。
     * @param commands コマンド
     */
    protected void dispatchCommandsBySender(List<String> commands) {

        // currentCommandSenderが既に無効なら、ConsoleSenderに置き換えておく
        if ( currentCommandSender == null ) {
            currentCommandSender = Bukkit.getConsoleSender();
        }

        // コマンド実行
        dispatchCommands(currentCommandSender, commands);
    }

    /**
     * コンソールで、指定されたコマンドをまとめて実行する。
     * @param commands コマンド
     */
    protected void dispatchCommandsByConsole(List<String> commands) {
        dispatchCommands(Bukkit.getConsoleSender(), commands);
    }

    /**
     * 指定されたコマンドをまとめて実行する。
     * @param sender コマンドを実行する人
     * @param commands コマンド
     */
    private void dispatchCommands(CommandSender sender, List<String> commands) {

        for ( String command : commands ) {

            if ( command.startsWith("/") ) {
                command = command.substring(1); // スラッシュ削除
            }

            // コマンドブロックパターンがコマンドに含まれている場合は、
            // 展開して実行する。含まれていないならそのまま実行する。
            String pattern = getCommandBlockPattern(command);
            if ( pattern != null ) {
                Location location = null;
                if ( sender instanceof Player ) {
                    location = ((Player)sender).getLocation().clone();
                } else if ( sender instanceof BlockCommandSender ) {
                    location = ((BlockCommandSender)sender).getBlock().getLocation().clone();
                }
                Player[] players = PlayerSelector.getPlayers(location, pattern);
                if ( players != null ) {
                    for ( Player p : players ) {
                        Bukkit.dispatchCommand(sender,
                                command.replaceAll(pattern, p.getName()));
                    }
                }
            } else {
                Bukkit.dispatchCommand(sender, command);
            }
        }
    }

    /**
     * プレイヤー死亡時に呼び出されるメソッド
     * @param event
     */
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {

        // タイマー起動中の死亡は、経験値を落とさない
        if ( timer != null )
            event.setDroppedExp(0);
    }

    /**
     * 指定されたコマンド文字列から、最初に現れるコマンドブロックパターンを
     * 抽出して返す。
     * @param command コマンド
     * @return パターン。見つからない場合はnullになる。
     */
    private String getCommandBlockPattern(String command) {

        String[] arguments = command.split(" ");

        for ( int i=1; i<arguments.length; i++ ) {
            if ( PlayerSelector.isPattern(arguments[i]) ) {
                return arguments[i];
            }
        }
        return null;
    }

    /**
     * ExpTimerのインスタンスを返す
     * @return ExpTimerのインスタンス
     */
    protected static ExpTimer getInstance() {
        return instance;
    }
}
