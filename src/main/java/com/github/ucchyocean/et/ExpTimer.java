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
import org.bukkit.ChatColor;
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
import org.bukkit.scheduler.BukkitTask;

/**
 * 経験値タイマー
 * @author ucchy
 */
public class ExpTimer extends JavaPlugin implements Listener {

    private static ExpTimer instance;
    private TimerTask runnable;
    private BukkitTask task;

    protected static ETConfigData config;
    protected static HashMap<String, ETConfigData> configs;
    private String currentConfigName;
    private CommandSender currentCommandSender;

    /**
     * @see org.bukkit.plugin.java.JavaPlugin#onEnable()
     */
    @Override
    public void onEnable() {

        instance = this;

        // コンフィグのロード
        reloadConfigData();

        // メッセージの初期化
        Messages.initialize(null);

        // イベントの登録
        getServer().getPluginManager().registerEvents(this, this);

        // ColorTeaming のロード
        if ( getServer().getPluginManager().isPluginEnabled("ColorTeaming") ) {
            Plugin temp = getServer().getPluginManager().getPlugin("ColorTeaming");
            String ctversion = temp.getDescription().getVersion();
            if ( Utility.isUpperVersion(ctversion, "2.2.0") ) {
                getLogger().info("ColorTeaming がロードされました。連携機能を有効にします。");
                getServer().getPluginManager().registerEvents(
                        new ColorTeamingListener(this), this);
            } else {
                getLogger().warning("ColorTeaming のバージョンが古いため、連携機能は無効になりました。");
                getLogger().warning("連携機能を使用するには、ColorTeaming v2.2.0 以上が必要です。");
            }
        }
    }

    /**
     * @see org.bukkit.plugin.java.JavaPlugin#onDisable()
     */
    @Override
    public void onDisable() {

        // タスクが残ったままなら、強制終了しておく。
        if ( runnable != null ) {
            getLogger().warning("タイマーが残ったままです。強制終了します。");
            getServer().getScheduler().cancelTask(task.getTaskId());
            runnable = null;
        }
    }

    /**
     * @see org.bukkit.plugin.java.JavaPlugin#onCommand(org.bukkit.command.CommandSender, org.bukkit.command.Command, java.lang.String, java.lang.String[])
     */
    @Override
    public boolean onCommand(
            CommandSender sender, Command command, String label, String[] args) {

        // 引数がない場合は実行しない
        if ( args.length <= 0 ) {
            return false;
        }

        if ( args[0].equalsIgnoreCase("start") ) {
            // タイマーをスタートする

            if ( runnable == null ) {

                if ( args.length == 1 ) {
                    config = configs.get("default").clone();
                } else if ( args.length >= 2 ) {
                    if ( args[1].matches("^[0-9]+$") ) {
                        config = configs.get("default").clone();
                        config.seconds = Integer.parseInt(args[1]);
                        if ( args.length >= 3 && args[2].matches("^[0-9]+$")) {
                            config.readySeconds = Integer.parseInt(args[2]);
                        }
                    } else {
                        if ( !configs.containsKey(args[1]) ) {
                            sender.sendMessage(ChatColor.RED +
                                    String.format("設定 %s が見つかりません！", args[1]));
                            return true;
                        }
                        config = configs.get(args[1]).clone();
                    }
                }

                // configからメッセージのリロード
                Messages.initialize(config.messageFileName);

                startNewTask(null, sender);
                sender.sendMessage(ChatColor.GRAY + "タイマーを新規に開始しました。");
                return true;

            } else {
                if ( runnable.isPaused() ) {
                    runnable.startFromPause();
                    sender.sendMessage(ChatColor.GRAY + "タイマーを再開しました。");
                    return true;
                } else {
                    sender.sendMessage(ChatColor.RED + "タイマーが既に開始されています！");
                    return true;
                }
            }

        } else if ( args[0].equalsIgnoreCase("pause") ) {
            // タイマーを一時停止する

            if ( runnable == null ) {
                sender.sendMessage(ChatColor.RED + "タイマーが開始されていません！");
                return true;
            }

            if ( !runnable.isPaused() ) {
                runnable.pause();
                sender.sendMessage(ChatColor.GRAY + "タイマーを一時停止しました。");
                return true;
            } else {
                sender.sendMessage(ChatColor.RED + "タイマーは既に一時停止状態です！");
                return true;

            }


        } else if ( args[0].equalsIgnoreCase("end") ) {
            // タイマーを強制終了する

            if ( runnable == null ) {
                sender.sendMessage(ChatColor.RED + "タイマーが開始されていません！");
                return true;
            }

            endTask();
            if ( config.useExpBar )
                setExpLevel(0, 1);
            sender.sendMessage(ChatColor.GRAY + "タイマーを停止しました。");

            return true;

        } else if ( args[0].equalsIgnoreCase("cancel") ) {
            // タイマーを強制終了する

            if ( runnable == null ) {
                sender.sendMessage(ChatColor.RED + "タイマーが開始されていません！");
                return true;
            }

            cancelTask();
            if ( config.useExpBar )
                setExpLevel(0, 1);
            sender.sendMessage(ChatColor.GRAY + "タイマーを強制停止しました。");
            return true;

        } else if ( args[0].equalsIgnoreCase("status") ) {
            // ステータスを参照する

            String stat;
            if ( runnable == null ) {
                stat = "タイマー停止中";
            } else {
                stat = runnable.getStatusDescription();
            }

            sender.sendMessage(ChatColor.GRAY + "----- ExpTimer information -----");
            sender.sendMessage(ChatColor.GRAY + "現在の状況：" +
                    ChatColor.WHITE + stat);
            sender.sendMessage(ChatColor.GRAY + "現在の設定名：" + currentConfigName);
            sender.sendMessage(ChatColor.GRAY + "開始時に実行するコマンド：");
            for ( String com : config.commandsOnStart ) {
                sender.sendMessage(ChatColor.WHITE + "  " + com);
            }
            sender.sendMessage(ChatColor.GRAY + "終了時に実行するコマンド：");
            for ( String com : config.commandsOnEnd ) {
                sender.sendMessage(ChatColor.WHITE + "  " + com);
            }
            sender.sendMessage(ChatColor.GRAY + "--------------------------------");

            return true;

        } else if ( args[0].equalsIgnoreCase("list") ) {
            // コンフィグ一覧を表示する

            String format = ChatColor.GOLD + "%s " + ChatColor.GRAY + ": " +
                ChatColor.WHITE + "%d " + ChatColor.GRAY + "+ %d seconds";
            
            sender.sendMessage(ChatColor.GRAY + "----- ExpTimer config list -----");
            for ( String key : configs.keySet() ) {
                int sec = configs.get(key).seconds;
                int ready = configs.get(key).readySeconds;
                String message = String.format(format, key, sec, ready);
                sender.sendMessage(message);
            }
            sender.sendMessage(ChatColor.GRAY + "--------------------------------");

            return true;

        } else if ( args[0].equalsIgnoreCase("reload") ) {
            // config.yml をリロードする

            if ( runnable != null ) {
                sender.sendMessage(ChatColor.RED + 
                        "実行中のタイマーがあるため、リロードできません！");
                sender.sendMessage(ChatColor.RED + 
                        "先に /" + label + " cancel を実行して、タイマーを終了してください。");
                return true;
            }
            
            reloadConfigData();
            sender.sendMessage(ChatColor.GRAY + "config.yml をリロードしました。");
            return true;
        }

        return false;
    }

    /**
     * config.yml を再読み込みする
     */
    protected void reloadConfigData() {

        saveDefaultConfig();
        reloadConfig();
        FileConfiguration config = getConfig();
        ExpTimer.configs = new HashMap<String, ETConfigData>();

        // デフォルトのデータ読み込み
        ConfigurationSection section = config.getConfigurationSection("default");
        ETConfigData defaultData = ETConfigData.loadFromSection(section, null);
        ExpTimer.config = defaultData;
        ExpTimer.configs.put("default", defaultData);
        currentConfigName = "default";

        // デフォルト以外のデータ読み込み
        for ( String key : config.getKeys(false) ) {
            if ( key.equals("default") ) {
                continue;
            }
            section = config.getConfigurationSection(key);
            ETConfigData data = ETConfigData.loadFromSection(section, defaultData);
            ExpTimer.configs.put(key, data);
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
     * @param configName コンフィグ名。nullならそのままconfigを変更せずにタスクを開始する
     * @param sender コマンド実行者。nullならcurrentCommandSenderがそのまま引き継がれる
     */
    public void startNewTask(String configName, CommandSender sender) {

        if ( configName != null && configs.containsKey(configName) ) {
            config = configs.get(configName).clone();
            Messages.initialize(config.messageFileName);
            currentConfigName = configName;
        }
        if ( sender != null ) {
            currentCommandSender = sender;
        }
        runnable = new TimerTask(this, config.readySeconds, config.seconds);
        task = getServer().getScheduler().runTaskTimer(this, runnable, 20, 20);
    }

    /**
     * 現在実行中のタスクを中断する。終了時のコマンドは実行されない。
     */
    public void cancelTask() {

        if ( runnable != null ) {
            // タスクを終了する
            getServer().getScheduler().cancelTask(task.getTaskId());
            runnable = null;
            task = null;
        }
        
        if ( ExpTimer.config.useExpBar ) {
            ExpTimer.setExpLevel(0, 1);
        }
    }

    /**
     * 現在実行中のタスクを終了コマンドを実行してから終了する
     */
    public void endTask() {

        if ( runnable != null ) {
            // 終了コマンドを実行してタスクを終了する
            dispatchCommands(config.commandsOnEnd);
            getServer().getScheduler().cancelTask(task.getTaskId());
            runnable = null;
            task = null;
        }
        
        if ( ExpTimer.config.useExpBar ) {
            ExpTimer.setExpLevel(0, 1);
        }
    }

    /**
     * 現在のタイマータスクを取得する
     * @return タスク
     */
    public TimerTask getTask() {
        return runnable;
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
     * 指定されたコマンドをまとめて実行する。
     * @param commands コマンド
     */
    protected void dispatchCommands(List<String> commands) {
        
        // CommandSender を取得する
        CommandSender sender = Bukkit.getConsoleSender();
        if ( ExpTimer.config != null && 
                !ExpTimer.config.forceEmulateConsoleCommand &&
                currentCommandSender != null ) {
            sender = currentCommandSender;
        }
        
        // コマンド実行
        for ( String command : commands ) {
            if ( command.startsWith("/") ) {
                command = command.substring(1); // スラッシュ削除
            }
            
            // @aがコマンドに含まれている場合は、展開して実行する。
            // 含まれていないならそのまま実行する。
            if ( command.contains("@a") ) {
                Player[] players = Bukkit.getOnlinePlayers();
                for ( Player p : players ) {
                    Bukkit.dispatchCommand(sender, 
                            command.replaceAll("@a", p.getName()));
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
        if ( runnable != null )
            event.setDroppedExp(0);
    }
}
