/*
 * @author     ucchy
 * @license    GPLv3
 * @copyright  Copyright ucchy 2013
 */
package com.github.ucchyocean.et;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author ucchy
 * タイマータスク
 */
public class TimerTask extends BukkitRunnable {

    private static String prefix = Messages.get("prefix");

    // 実行開始までにかかる処理時間を考慮して、
    // 少し待つためのオフセット（ミリ秒）
    private static final long OFFSET = 500;

    private int secondsReadyRest;
    private int secondsGameRest;
    private int secondsGameMax;

    private long tickReadyBase;
    private long tickGameBase;

    private boolean flagStart;
    private boolean flagRest300sec;
    private boolean flagRest180sec;
    private boolean flagRest60sec;
    private boolean flagEnd;

    // このタイマーが一時停止されているかどうか
    private boolean isPaused;

    private ExpTimer plugin;

    /**
     * コンストラクタ
     * @param secondsReady タイマー開始までの設定時間
     * @param secondsGame タイマーの設定時間
     */
    public TimerTask(ExpTimer plugin, int secondsReady, int secondsGame) {

        this.plugin = plugin;
        secondsReadyRest = secondsReady;
        secondsGameRest = secondsGame;
        secondsGameMax = secondsGame;
        isPaused = false;

        tickReadyBase = System.currentTimeMillis() +
            secondsReady * 1000 + OFFSET;
        tickGameBase = tickReadyBase + secondsGame * 1000;

        flagStart = false;
        flagRest300sec = ( secondsGameMax <= 300 );
        flagRest180sec = ( secondsGameMax <= 180 );
        flagRest60sec = ( secondsGameMax <= 60 );
        flagEnd = false;
    }

    /**
     * 1秒ごとに呼び出しされるメソッド
     */
    @Override
    public void run() {

        // 更新実行
        refreshRests();

        // 条件に応じてアナウンス
        if ( !flagStart &&
                secondsReadyRest <= plugin.countdownOnStart ) {
            if ( secondsReadyRest > 0 ) {
                // スタート前のカウントダウン
                broadcastMessage("preStartSec", secondsReadyRest);
            } else {
                // スタート
                flagStart = true;
                broadcastMessage("start");
                // コマンドの実行
                for ( String command : plugin.commandsOnStart ) {
                    Bukkit.dispatchCommand(
                            Bukkit.getConsoleSender(),
                            command);
                }
            }
        } else if ( !flagEnd ) {
            if ( !flagRest300sec && secondsGameRest <= 300 ) {
                // 残り5分
                flagRest300sec = true;
                broadcastMessage("rest300sec");
            } else if ( !flagRest180sec && secondsGameRest <= 180 ) {
                // 残り3分
                flagRest180sec = true;
                broadcastMessage("rest180sec");
            } else if ( !flagRest60sec && secondsGameRest <= 60 ) {
                // 残り1分
                flagRest60sec = true;
                broadcastMessage("rest60sec");
            } else if ( 0 < secondsGameRest &&
                    secondsGameRest <= plugin.countdownOnEnd ) {
                // 終了前のカウントダウン
                broadcastMessage("preEndSec", secondsGameRest);
            } else if ( secondsGameRest <= 0 ) {
                // 終了
                flagEnd = true;
                broadcastMessage("end");
                // コマンドの実行
                for ( String command : plugin.commandsOnEnd ) {
                    Bukkit.dispatchCommand(
                            Bukkit.getConsoleSender(),
                            command);
                }
            }
        }

        // 終了条件を満たす場合は、スケジュール解除
        if ( flagEnd ) {
            plugin.endTask();
        }

        // 経験値バーの表示更新
        if ( plugin.useExpBar ) {
            ExpTimer.setExpLevel(secondsGameRest, secondsGameMax);
        }
    }


    /**
     * secondsReadyRest, secondsGameRest を更新する。
     */
    private void refreshRests() {

        // 一時停止中は、更新を行わない
        if ( isPaused ) {
            return;
        }

        // restの更新
        long currentReady = -1, currentGame = -1;
        if ( !isPaused ) {
            long current = System.currentTimeMillis();
            currentReady = tickReadyBase - current;
            secondsReadyRest = (int)(currentReady/1000);
            currentGame = tickGameBase - current;
            secondsGameRest = (int)(currentGame/1000);
        }

        // デバッグ出力
        if ( plugin.getLogger().isLoggable(Level.FINEST) ) {
            String msg;
            if ( secondsReadyRest > 0 ) {
                msg = String.format(
                        "ready rest %dsec, millisec %d",
                        secondsReadyRest, currentReady);
            } else {
                float percent = (float)secondsGameRest / (float)secondsGameMax;
                msg = String.format(
                        "rest %dsec (%.2f%%), millisec %d",
                        secondsGameRest, percent, currentGame);
            }
            plugin.getLogger().finest(msg);
        }
    }

    /**
     * タイマーを一時停止する
     */
    protected void pause() {
        isPaused = true;
    }

    /**
     * タイマーを一時停止状態から再開する
     */
    protected void startFromPause() {
        if ( isPaused ) {
            isPaused = false;
            long current = System.currentTimeMillis();
            tickReadyBase = current + secondsReadyRest * 1000 + OFFSET;
            tickGameBase = current + secondsGameRest * 1000 + OFFSET;
        }
    }

    /**
     * メッセージリソースを取得し、ブロードキャストする
     * @param key メッセージキー
     * @param args メッセージの引数
     * @return メッセージ
     */
    private void broadcastMessage(String key, Object... args) {
        String msg = Messages.get(key, args);
        if ( msg.equals("") ) {
            return;
        }
        Bukkit.broadcastMessage(Utility.replaceColorCode(prefix + msg));
    }

    /**
     * このタイマーの現在状況を返す
     * @return タイマーの現在状況
     */
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
