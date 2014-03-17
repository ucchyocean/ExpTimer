/*
 * @author     ucchy
 * @license    LGPLv3
 * @copyright  Copyright ucchy 2014
 */
package com.github.ucchyocean.et;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

/**
 * @author ucchy
 */
public class ExpTimerCommand implements TabExecutor {

    private static final String PERMISSION_PREFIX = "exptimer.";

    private ExpTimer plugin;

    /**
     * コンストラクタ
     * @param plugin
     */
    public ExpTimerCommand(ExpTimer plugin) {
        this.plugin = plugin;
    }

    /**
     * @see org.bukkit.command.TabCompleter#onTabComplete(org.bukkit.command.CommandSender, org.bukkit.command.Command, java.lang.String, java.lang.String[])
     */
    @Override
    public List<String> onTabComplete(
            CommandSender sender, Command command, String label, String[] args) {

        return null;
    }

    /**
     * コマンドが実行されたときに呼び出されるメソッド
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

            String node = PERMISSION_PREFIX + "start";
            if ( !sender.hasPermission(node) ) {
                sender.sendMessage(ChatColor.RED + String.format(
                        "パーミッション \"%s\" がないため、コマンドを実行できません。", node));
                return true;
            }

            TimerTask timer = plugin.getTask();

            if ( timer == null ) { // 現在、タイマーが動いていない

                ExpTimerConfigData config = null;

                if ( args.length >= 2 ) {
                    if ( args[1].matches("^[0-9]+$") ) {
                        config = plugin.getConfigData().clone();
                        config.seconds = Integer.parseInt(args[1]);
                        if ( args.length >= 3 && args[2].matches("^[0-9]+$")) {
                            config.readySeconds = Integer.parseInt(args[2]);
                        }
                    } else {
                        if ( !plugin.configs.containsKey(args[1]) ) {
                            sender.sendMessage(ChatColor.RED +
                                    String.format("設定 %s が見つかりません！", args[1]));
                            return true;
                        }
                        config = plugin.configs.get(args[1]).clone();
                    }
                }

                plugin.startNewTask(config, sender);
                sender.sendMessage(ChatColor.GRAY + "タイマーを新規に開始しました。");
                return true;

            } else {
                if ( timer.isPaused() ) {
                    timer.startFromPause();
                    sender.sendMessage(ChatColor.GRAY + "タイマーを再開しました。");
                    return true;
                } else {
                    sender.sendMessage(ChatColor.RED + "タイマーが既に開始されています！");
                    return true;
                }
            }

        } else if ( args[0].equalsIgnoreCase("pause") ) {
            // タイマーを一時停止する

            String node = PERMISSION_PREFIX + "pause";
            if ( !sender.hasPermission(node) ) {
                sender.sendMessage(ChatColor.RED + String.format(
                        "パーミッション \"%s\" がないため、コマンドを実行できません。", node));
                return true;
            }

            TimerTask timer = plugin.getTask();

            if ( timer == null ) {
                sender.sendMessage(ChatColor.RED + "タイマーが開始されていません！");
                return true;
            }

            if ( !timer.isPaused() ) {
                timer.pause();
                sender.sendMessage(ChatColor.GRAY + "タイマーを一時停止しました。");
                return true;
            } else {
                sender.sendMessage(ChatColor.RED + "タイマーは既に一時停止状態です！");
                return true;

            }

        } else if ( args[0].equalsIgnoreCase("end") ) {
            // タイマーを強制終了する

            String node = PERMISSION_PREFIX + "end";
            if ( !sender.hasPermission(node) ) {
                sender.sendMessage(ChatColor.RED + String.format(
                        "パーミッション \"%s\" がないため、コマンドを実行できません。", node));
                return true;
            }

            TimerTask timer = plugin.getTask();

            if ( timer == null ) {
                sender.sendMessage(ChatColor.RED + "タイマーが開始されていません！");
                return true;
            }

            plugin.endTask();
            sender.sendMessage(ChatColor.GRAY + "タイマーを停止しました。");

            return true;

        } else if ( args[0].equalsIgnoreCase("cancel") ) {
            // タイマーを強制終了する

            String node = PERMISSION_PREFIX + "cancel";
            if ( !sender.hasPermission(node) ) {
                sender.sendMessage(ChatColor.RED + String.format(
                        "パーミッション \"%s\" がないため、コマンドを実行できません。", node));
                return true;
            }

            TimerTask timer = plugin.getTask();

            if ( timer == null ) {
                sender.sendMessage(ChatColor.RED + "タイマーが開始されていません！");
                return true;
            }

            plugin.cancelTask();
            sender.sendMessage(ChatColor.GRAY + "タイマーを強制停止しました。");
            return true;

        } else if ( args[0].equalsIgnoreCase("status") ) {
            // ステータスを参照する

            String node = PERMISSION_PREFIX + "status";
            if ( !sender.hasPermission(node) ) {
                sender.sendMessage(ChatColor.RED + String.format(
                        "パーミッション \"%s\" がないため、コマンドを実行できません。", node));
                return true;
            }

            TimerTask timer = plugin.getTask();
            ExpTimerConfigData config = plugin.getConfigData();
            String stat;
            if ( timer == null ) {
                stat = "タイマー停止中";
            } else {
                stat = timer.getStatusDescription();
            }

            sender.sendMessage(ChatColor.GRAY + "----- ExpTimer information -----");
            sender.sendMessage(ChatColor.GRAY + "現在の状況：" +
                    ChatColor.WHITE + stat);
            sender.sendMessage(ChatColor.GRAY + "現在の設定名：" + config.name);
            sender.sendMessage(ChatColor.GRAY + "開始時に実行するコマンド：");
            for ( String com : config.commandsOnStart ) {
                sender.sendMessage(ChatColor.WHITE + "  " + com);
            }
            for ( String com : config.consoleCommandsOnStart ) {
                sender.sendMessage(ChatColor.AQUA + "  " + com);
            }
            sender.sendMessage(ChatColor.GRAY + "終了時に実行するコマンド：");
            for ( String com : config.commandsOnEnd ) {
                sender.sendMessage(ChatColor.WHITE + "  " + com);
            }
            for ( String com : config.consoleCommandsOnEnd ) {
                sender.sendMessage(ChatColor.AQUA + "  " + com);
            }
            sender.sendMessage(ChatColor.GRAY + "--------------------------------");

            return true;

        } else if ( args[0].equalsIgnoreCase("list") ) {
            // コンフィグ一覧を表示する

            String node = PERMISSION_PREFIX + "list";
            if ( !sender.hasPermission(node) ) {
                sender.sendMessage(ChatColor.RED + String.format(
                        "パーミッション \"%s\" がないため、コマンドを実行できません。", node));
                return true;
            }

            String format = ChatColor.GOLD + "%s " + ChatColor.GRAY + ": " +
                ChatColor.WHITE + "%d " + ChatColor.GRAY + "+ %d seconds";

            sender.sendMessage(ChatColor.GRAY + "----- ExpTimer config list -----");
            for ( String key : plugin.configs.keySet() ) {
                int sec = plugin.configs.get(key).seconds;
                int ready = plugin.configs.get(key).readySeconds;
                String message = String.format(format, key, sec, ready);
                sender.sendMessage(message);
            }
            sender.sendMessage(ChatColor.GRAY + "--------------------------------");

            return true;

        } else if ( args[0].equalsIgnoreCase("reload") ) {
            // config.yml をリロードする

            String node = PERMISSION_PREFIX + "reload";
            if ( !sender.hasPermission(node) ) {
                sender.sendMessage(ChatColor.RED + String.format(
                        "パーミッション \"%s\" がないため、コマンドを実行できません。", node));
                return true;
            }

            TimerTask timer = plugin.getTask();

            if ( timer != null ) {
                sender.sendMessage(ChatColor.RED +
                        "実行中のタイマーがあるため、リロードできません！");
                sender.sendMessage(ChatColor.RED +
                        "先に /" + label + " cancel を実行して、タイマーを終了してください。");
                return true;
            }

            plugin.reloadConfigData();
            sender.sendMessage(ChatColor.GRAY + "config.yml をリロードしました。");
            return true;
        }

        return false;
    }
}
