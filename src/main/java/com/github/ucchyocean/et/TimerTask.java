/*
 * @author     ucchy
 * @license    GPLv3
 * @copyright  Copyright ucchy 2013
 */
package com.github.ucchyocean.et;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author ucchy
 *
 */
public class TimerTask extends BukkitRunnable {

    private static String prefix = Messages.get("prefix");

    private int secondsReadyRest;
    private int secondsGameRest;
    private int secondsGameMax;

    protected boolean isPaused;

    /**
     * コンストラクタ
     * @param secondsReady タイマー開始までの設定時間
     * @param secondsGame タイマーの設定時間
     */
    public TimerTask(int secondsReady, int secondsGame) {
        secondsReadyRest = secondsReady;
        secondsGameRest = secondsGame;
        secondsGameMax = secondsGame;
        isPaused = false;
    }

    /**
     * 1秒ごとに呼び出しされるメソッド
     */
    @Override
    public void run() {

        // 一時停止中は何もしない
        if ( isPaused ) {
            return;
        }

        if ( secondsReadyRest > 0 ) {
            secondsReadyRest--;

            if ( secondsReadyRest > 0 && secondsReadyRest <= 5 ) {
                String message = getMessage("preStartSec", secondsReadyRest);
                if ( !message.equals("") )
                    Bukkit.broadcastMessage(message);
            } else if ( secondsReadyRest == 0 ) {
                String message = getMessage("start");
                if ( !message.equals("") )
                    Bukkit.broadcastMessage(message);
                // コマンドの実行
                for ( String command : ExpTimer.instance.commandsOnStart ) {
                    Bukkit.dispatchCommand(
                            Bukkit.getConsoleSender(),
                            command);
                }
            }

        } else if ( secondsReadyRest <= 0 && secondsGameRest > 0 ) {
            secondsGameRest--;

            if ( secondsGameRest == 300 ) {
                String message = getMessage("rest300sec");
                if ( !message.equals("") )
                    Bukkit.broadcastMessage(message);
            } else if ( secondsGameRest == 180 ) {
                String message = getMessage("rest180sec");
                if ( !message.equals("") )
                    Bukkit.broadcastMessage(message);
            } else if ( secondsGameRest == 60 ) {
                String message = getMessage("rest60sec");
                if ( !message.equals("") )
                    Bukkit.broadcastMessage(message);
            } else if ( secondsGameRest > 0 && secondsGameRest <= 10 ) {
                String message = getMessage("preEndSec", secondsGameRest);
                if ( !message.equals("") )
                    Bukkit.broadcastMessage(message);
            } else if ( secondsGameRest == 0 ) {
                String message = getMessage("end");
                if ( !message.equals("") )
                    Bukkit.broadcastMessage(message);
                // コマンドの実行
                for ( String command : ExpTimer.instance.commandsOnEnd ) {
                    Bukkit.dispatchCommand(
                            Bukkit.getConsoleSender(),
                            command);
                }
            }

        } else if ( secondsReadyRest <= 0 && secondsGameRest <= 0 ) {

            Bukkit.getScheduler().cancelTask(ExpTimer.task.getTaskId());
            ExpTimer.runnable = null;
        }

        // 経験値バーの表示更新
        ExpTimer.setExpLevel(secondsGameRest, secondsGameMax);
    }

    private String getMessage(String key, Object... args) {
        return Utility.replaceColorCode(prefix + Messages.get(key, args));
    }

    protected String getStatus() {

        if ( isPaused ) {
            return "一時停止中 残りあと" + secondsGameRest + "秒";
        }
        if ( secondsReadyRest > 0 ) {
            return "開始準備中 開始まであと" + secondsReadyRest + "秒";
        }
        if ( secondsGameRest > 0 ) {
            return "開始中 終了まであと" + secondsGameRest + "秒";
        }
        return "終了状態";
    }
}
