/*
 * @author     ucchy
 * @license    LGPLv3
 * @copyright  Copyright ucchy 2013
 */
package com.github.ucchyocean.et;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * タイマータスク
 * @author ucchy
 */
public class TimerTask extends BukkitRunnable {

    /** タイマータスクのステータス列挙体 */
    public enum Status {
        
        /** 一時停止中 */
        PAUSED,
        
        /** 開始準備中 */
        READY,
        
        /** 開始中 */
        RUN,
        
        /** 終了状態 */
        END;
    }
    
    // 実行開始までにかかる処理時間を考慮して、
    // 少し待つためのオフセット（ミリ秒）
    private static final long OFFSET = 150;

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
                secondsReadyRest <= ExpTimer.config.countdownOnStart ) {
            if ( secondsReadyRest > 0 ) {
                // スタート前のカウントダウン
                broadcastMessage("preStartSec", secondsReadyRest);
                if ( ExpTimer.config.playSound ) {
                    playPing();
                }
            } else {
                // スタート
                flagStart = true;
                broadcastMessage("start");
                if ( ExpTimer.config.playSound ) {
                    playPong();
                }
                // コマンドの実行
                plugin.dispatchCommandsBySender(ExpTimer.config.commandsOnStart);
                plugin.dispatchCommandsByConsole(ExpTimer.config.consoleCommandsOnStart);
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
                    secondsGameRest <= ExpTimer.config.countdownOnEnd ) {
                // 終了前のカウントダウン
                broadcastMessage("preEndSec", secondsGameRest);
                if ( ExpTimer.config.playSound ) {
                    playPing();
                }
            } else if ( secondsGameRest <= 0 ) {
                // 終了
                flagEnd = true;
                broadcastMessage("end");
                if ( ExpTimer.config.playSound ) {
                    playPong();
                }
                // コマンドの実行
                plugin.dispatchCommandsBySender(ExpTimer.config.commandsOnEnd);
                plugin.dispatchCommandsByConsole(ExpTimer.config.consoleCommandsOnEnd);
            }
        }

        // 経験値バーの表示更新
        if ( ExpTimer.config.useExpBar ) {
            ExpTimer.setExpLevel(secondsGameRest, secondsGameMax);
        }

        // 終了条件を満たす場合は、スケジュール解除
        if ( flagEnd ) {
            plugin.cancelTask();

            // リピート設定なら、新しいタスクを再スケジュール
            if ( ExpTimer.config.nextConfig != null &&
                    ExpTimer.configs.containsKey(ExpTimer.config.nextConfig) ) {
                plugin.startNewTask(ExpTimer.config.nextConfig, null);
            }
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
    public void pause() {
        isPaused = true;
    }

    /**
     * タイマーが一時停止しているかどうかを返す
     * @return タイマーが一時停止しているかどうか
     */
    public boolean isPaused() {
        return isPaused;
    }

    /**
     * タイマーを一時停止状態から再開する
     */
    public void startFromPause() {
        if ( isPaused ) {
            isPaused = false;
            long current = System.currentTimeMillis();
            tickReadyBase = current + secondsReadyRest * 1000 + OFFSET;
            tickGameBase = current + secondsGameRest * 1000 + OFFSET;
        }
    }
    
    /**
     * 現在のステータスを返す
     * @return ステータス
     */
    public Status getStatus() {
        
        if ( isPaused ) {
            return Status.PAUSED;
        }
        if ( secondsReadyRest > 0 ) {
            return Status.READY;
        }
        if ( secondsGameRest > 0 ) {
            return Status.RUN;
        }
        return Status.END;
    }

    /**
     * このタイマーの現在状況を返す
     * @return タイマーの現在状況
     */
    public String getStatusDescription() {

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

    /**
     * 残りの準備秒数を返す
     * @return 残りの準備秒数
     */
    public int getSecondsReadyRest() {
        return secondsReadyRest;
    }
    
    /**
     * 残りのゲーム秒数を返す
     * @return 残りのゲーム秒数
     */
    public int getSecondsGameRest() {
        return secondsGameRest;
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
        String prefix = Messages.get("prefix");
        Bukkit.broadcastMessage(Utility.replaceColorCode(prefix + msg));
    }
    
    /**
     * カウントダウンの音を出す
     */
    private void playPing() {
        
        for ( Player player : Bukkit.getOnlinePlayers() ) {
            player.playSound(player.getEyeLocation(), Sound.NOTE_STICKS, 1.0F, 1.0F);
        }
    }
    
    /**
     * カウントダウンからの開始音を出す
     */
    private void playPong() {
        
        for ( Player player : Bukkit.getOnlinePlayers() ) {
            player.playSound(player.getEyeLocation(), Sound.NOTE_PLING, 1.0F, 1.0F);
        }
    }
}
