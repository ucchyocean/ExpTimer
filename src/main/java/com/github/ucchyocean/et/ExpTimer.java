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
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

/**
 * 経験値タイマー
 * @author ucchy
 */
public class ExpTimer extends JavaPlugin implements Listener {

    private static ExpTimer instance;
    private TimerTask timer;
    private ExpTimerCommand executor;

    protected ExpTimerConfigData configData;
    protected HashMap<String, ExpTimerConfigData> configs;
    private CommandSender currentCommandSender;
    private ColorTeamingBridge ctbridge;
    private BossBarManager bbmanager;

    // 同期用のスコアボード
    private Objective obj;

    /**
     * @see org.bukkit.plugin.java.JavaPlugin#onEnable()
     */
    @Override
    public void onEnable() {

        instance = this;

        // コンフィグのロード
        reloadConfigData();

        // コマンドの登録
        executor = new ExpTimerCommand(this);

        // イベントの登録
        getServer().getPluginManager().registerEvents(this, this);

        // ColorTeaming のロード
        if ( getServer().getPluginManager().isPluginEnabled("ColorTeaming") ) {
            Plugin temp = getServer().getPluginManager().getPlugin("ColorTeaming");
            String ctversion = temp.getDescription().getVersion();
            if ( Utility.isUpperVersion(ctversion, "2.3.2") ) {
                getLogger().info("ColorTeaming was loaded. "
                        + getDescription().getName() + " is in cooperation with ColorTeaming.");
                ctbridge = new ColorTeamingBridge(this, temp);
                getServer().getPluginManager().registerEvents(ctbridge, this);
            } else {
                getLogger().warning("ColorTeaming was too old. The cooperation feature will be disabled.");
                getLogger().warning("NOTE: Please use ColorTeaming v2.3.2 or later version.");
            }
        }

        // 同期表示用のスコアボードを準備
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        obj = scoreboard.getObjective("exptimer_time");
        if ( obj == null ) {
            obj = scoreboard.registerNewObjective("exptimer_time", "dummy");
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
     * @see org.bukkit.plugin.java.JavaPlugin#onCommand(org.bukkit.command.CommandSender, org.bukkit.command.Command, java.lang.String, java.lang.String[])
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return executor.onCommand(sender, command, label, args);
    }

    /**
     * @see org.bukkit.plugin.java.JavaPlugin#onTabComplete(org.bukkit.command.CommandSender, org.bukkit.command.Command, java.lang.String, java.lang.String[])
     */
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return executor.onTabComplete(sender, command, alias, args);
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
            if ( configData.isUseSideBar() ) {
                timer.removeSidebar();
            }

            // 経験値バーのクリア
            if ( configData.isUseExpBar() ) {
                timer.resetExpbar();
            }

            // ボスバーのクリア
            if ( configData.isUseBossBar() ) {
                timer.removeBossbar();
            }

            // 同期用スコアボードをリセットする
            timer.resetScoreboard();

            // タスクを終了する
            timer.endTimer();
            timer = null;
        }
    }

    /**
     * 現在実行中のタスクを終了コマンドを実行してから終了する
     * @param invokeNextConfig nextConfigを起動するかどうか
     */
    public void endTask(boolean invokeNextConfig) {
        endTask(invokeNextConfig, null);
    }

    /**
     * 現在実行中のタスクを終了コマンドを実行してから終了する
     * @param invokeNextConfig nextConfigを起動するかどうか
     * @param winTeam 勝利チーム（不要ならnull指定）
     */
    public void endTask(boolean invokeNextConfig, String winTeam) {

        if ( timer != null ) {
            // 終了コマンドを実行してタスクを終了する

            dispatchCommandsBySender(configData.getCommandsOnEnd(), winTeam);
            dispatchCommandsByConsole(configData.getConsoleCommandsOnEnd(), winTeam);

            // タスク終了
            cancelTask();

            // リピート設定なら、新しいタスクを再スケジュール
            if ( invokeNextConfig &&
                    configData.getNextConfig() != null &&
                    configs.containsKey(configData.getNextConfig()) ) {
                startNewTask(configData.getNextConfig(), null);
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
        dispatchCommands(currentCommandSender, commands, null);
    }

    /**
     * キャッシュされているCommandSenderで、指定されたコマンドをまとめて実行する。
     * @param commands コマンド
     * @param winTeam 勝利チーム（不要ならnull指定）
     */
    protected void dispatchCommandsBySender(List<String> commands, String winTeam) {

        // currentCommandSenderが既に無効なら、ConsoleSenderに置き換えておく
        if ( currentCommandSender == null ) {
            currentCommandSender = Bukkit.getConsoleSender();
        }

        // コマンド実行
        dispatchCommands(currentCommandSender, commands, winTeam);
    }

    /**
     * コンソールで、指定されたコマンドをまとめて実行する。
     * @param commands コマンド
     */
    protected void dispatchCommandsByConsole(List<String> commands) {
        dispatchCommands(Bukkit.getConsoleSender(), commands, null);
    }

    /**
     * コンソールで、指定されたコマンドをまとめて実行する。
     * @param commands コマンド
     * @param winTeam 勝利チーム（不要ならnull指定）
     */
    protected void dispatchCommandsByConsole(List<String> commands, String winTeam) {
        dispatchCommands(Bukkit.getConsoleSender(), commands, winTeam);
    }

    /**
     * 指定されたコマンドをまとめて実行する。
     * @param sender コマンドを実行する人
     * @param commands コマンド
     * @param winTeam 勝利チーム（不要ならnull指定）
     */
    private void dispatchCommands(
            CommandSender sender, List<String> commands, String winTeam) {

        for ( String command : commands ) {

            if ( command.startsWith("/") ) {
                command = command.substring(1); // スラッシュ削除
            }

            if ( command.contains("%winteam") ) {
                // 勝利チームキーワードが含まれる場合

                if ( winTeam == null ) {
                    continue; // コマンドは実行せずに、次へ進む。
                }

                // キーワードを置き換えておく
                command = command.replace("%winteam", winTeam);
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
                        String com = command.replace(pattern, p.getName());
                        Bukkit.dispatchCommand(sender, com);
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

    /**
     * ColorTeaming連携時に、ColorTeamingBridgeを返す
     * @return ColorTeamingBridge、非連携時にはnullになることに注意
     */
    protected ColorTeamingBridge getColorTeaming() {
        return ctbridge;
    }

    /**
     * 新しいBossBarManagerを作成する
     * @param players 表示対象プレイヤー
     * @return BossBarManager
     */
    protected BossBarManager newBossBarManager(List<Player> players) {
        bbmanager = new BossBarManager(players);
        return bbmanager;
    }

    /**
     * BossBarManagerを返す
     * @return BossBarManager
     */
    protected BossBarManager getBossBarManager() {
        return bbmanager;
    }

    /**
     * 同期用スコアボードのオブジェクティブを取得する
     */
    protected Objective getObjForSyncSB() {
        return obj;
    }
}
