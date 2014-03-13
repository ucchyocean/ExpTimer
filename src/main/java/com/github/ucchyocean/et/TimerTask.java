/*
 * @author     ucchy
 * @license    LGPLv3
 * @copyright  Copyright ucchy 2013
 */
package com.github.ucchyocean.et;

import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

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

    private ArrayList<Integer> restAlertSeconds;

    private boolean flagStart;
    private boolean[] alertFlags;
    private boolean flagEnd;

    // このタイマーが一時停止されているかどうか
    private boolean isPaused;

    private ExpTimer plugin;
    
    // スコアボードのオブジェクティブ
    private Objective objective;
    
    // 現在表示中のサイドバーアイテム
    private OfflinePlayer sidebarItem;

    /**
     * コンストラクタ
     * @param secondsReady タイマー開始までの設定時間
     * @param secondsGame タイマーの設定時間
     */
    public TimerTask(ExpTimer plugin) {

        this.plugin = plugin;
        ExpTimerConfig config = ExpTimer.config;

        secondsReadyRest = config.readySeconds;
        secondsGameRest = config.seconds;
        secondsGameMax = config.seconds;
        isPaused = false;

        tickReadyBase = System.currentTimeMillis() +
                config.readySeconds * 1000 + OFFSET;
        tickGameBase = tickReadyBase + config.seconds * 1000;

        flagStart = false;
        flagEnd = false;

        restAlertSeconds = new ArrayList<Integer>();
        for ( Integer i : config.restAlertSeconds ) {
            restAlertSeconds.add(i);
        }
        Collections.sort(restAlertSeconds);
        Collections.reverse(restAlertSeconds);

        alertFlags = new boolean[restAlertSeconds.size()];
        for ( int i=0; i<alertFlags.length; i++ ) {
            alertFlags[i] = ( secondsGameMax <= restAlertSeconds.get(i) );
        }

        // objectiveの取得
        if ( config.useSideBar ) {
            Scoreboard sb = Bukkit.getScoreboardManager().getMainScoreboard();
            
            // 既にオブジェクティブがあるなら、いったんクリアする
            objective = sb.getObjective("exptimer");
            if ( objective != null ) {
                objective.unregister();
            }
            
            // 新しいオブジェクティブを作成する
            objective = sb.registerNewObjective("exptimer", "dummy");
            objective.setDisplayName("残り時間");
            sidebarItem = null;
            
            // サイドバーに表示
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        }
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
                    playCountdownSound();
                }
            } else {
                // スタート
                flagStart = true;
                broadcastMessage("start");
                if ( ExpTimer.config.playSound ) {
                    playStartEndSound();
                }
                // コマンドの実行
                plugin.dispatchCommandsBySender(
                        ExpTimer.config.commandsOnStart);
                plugin.dispatchCommandsByConsole(
                        ExpTimer.config.consoleCommandsOnStart);
            }
            
        } else if ( !flagEnd ) {
            
            // 残りｎ分の告知処理
            for ( int index=0; index<alertFlags.length; index++ ) {
                if ( !alertFlags[index] 
                        && secondsGameRest <= restAlertSeconds.get(index) ) {
                    alertFlags[index] = true;
                    String key = "rest" + restAlertSeconds.get(index) + "sec";
                    broadcastMessage(key);
                    break;
                } else if ( secondsGameRest > restAlertSeconds.get(index) ) {
                    break;
                }
            }
            
            if ( 0 < secondsGameRest &&
                    secondsGameRest <= ExpTimer.config.countdownOnEnd ) {
                // 終了前のカウントダウン
                broadcastMessage("preEndSec", secondsGameRest);
                if ( ExpTimer.config.playSound ) {
                    playCountdownSound();
                }
            } else if ( secondsGameRest <= 0 ) {
                // 終了
                flagEnd = true;
                broadcastMessage("end");
                if ( ExpTimer.config.playSound ) {
                    playStartEndSound();
                }
                // コマンドの実行
                plugin.dispatchCommandsBySender(
                        ExpTimer.config.commandsOnEnd);
                plugin.dispatchCommandsByConsole(
                        ExpTimer.config.consoleCommandsOnEnd);
            }
        }

        // 経験値バーの表示更新
        if ( ExpTimer.config.useExpBar ) {
            ExpTimer.setExpLevel(secondsGameRest, secondsGameMax);
        }
        
        // サイドバーの表示更新
        if ( ExpTimer.config.useSideBar ) {
            refreshSidebar();
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
     * サイドバー表示を更新する
     */
    private void refreshSidebar() {
        
        int hour = secondsGameRest / 3600;
        int minute = (secondsGameRest - hour * 3600) / 60;
        int second = secondsGameRest - hour * 3600 - minute * 60;
        
        String name = String.format(ChatColor.RED + "%02d:%02d:", hour, minute);
        
        if ( sidebarItem == null ) {
            sidebarItem = Bukkit.getOfflinePlayer(name);
        } else if ( !name.equals(sidebarItem.getName()) ) {
            objective.getScore(sidebarItem).setScore(0);
            Scoreboard sb = Bukkit.getScoreboardManager().getMainScoreboard();
            sb.resetScores(sidebarItem);
            sidebarItem = Bukkit.getOfflinePlayer(name);
        }

        objective.getScore(sidebarItem).setScore(second);
    }
    
    /**
     * サイドバーを非表示にする
     */
    protected void removeSidebar() {
        
        Scoreboard sb = Bukkit.getScoreboardManager().getMainScoreboard();
        
        if ( sidebarItem != null ) {
            objective.getScore(sidebarItem).setScore(0);
            sb.resetScores(sidebarItem);
        }
        sidebarItem = null;
        
        Objective sideObj = sb.getObjective(DisplaySlot.SIDEBAR);
        if ( sideObj != null && sideObj.getName().equals("exptimer") ) {
            sb.clearSlot(DisplaySlot.SIDEBAR);
            sideObj.unregister();
        }
        objective = null;
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
    private void playCountdownSound() {
        
        String name = ExpTimer.config.playSoundCountdown;
        Sound sound;
        if ( isValidSoundName(name) ) {
            sound = Sound.valueOf(name);
        } else {
            sound = Sound.NOTE_STICKS;
        }
        for ( Player player : Bukkit.getOnlinePlayers() ) {
            player.playSound(player.getEyeLocation(), sound, 1.0F, 1.0F);
        }
    }
    
    /**
     * 開始音・終了音を出す
     */
    private void playStartEndSound() {
        
        String name = ExpTimer.config.playSoundStartEnd;
        Sound sound;
        if ( isValidSoundName(name) ) {
            sound = Sound.valueOf(name);
        } else {
            sound = Sound.NOTE_PLING;
        }
        for ( Player player : Bukkit.getOnlinePlayers() ) {
            player.playSound(player.getEyeLocation(), sound, 1.0F, 1.0F);
        }
    }
    
    /**
     * 指定された名前は、Soundクラスに含まれているか、確認する
     * @param name サウンド名
     * @return Soundクラスに含まれているかどうか
     */
    private boolean isValidSoundName(String name) {

        for ( Sound s : Sound.values() ) {
            if ( s.name().equals(name) ) {
                return true;
            }
        }
        return false;
    }
}
