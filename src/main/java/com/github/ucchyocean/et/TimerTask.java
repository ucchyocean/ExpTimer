/*
 * @author     ucchy
 * @license    LGPLv3
 * @copyright  Copyright ucchy 2013
 */
package com.github.ucchyocean.et;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
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

    private ArrayList<Integer> runCommandsOnMidSeconds;
    private ArrayList<Integer> restAlertSeconds;

    private boolean flagStart;
    private boolean[] commandFlags;
    private boolean[] alertFlags;
    private boolean flagEnd;

    private BukkitTask task;

    // このタイマーが一時停止されているかどうか
    private boolean isPaused;

    // ExpTimerのインスタンス
    private ExpTimer plugin;

    // スコアボードのオブジェクティブ
    private Objective objective;

    // 現在表示中のサイドバーアイテム
    private String sidebarItem;

    // コンフィグデータ
    private ExpTimerConfigData configData;

    // ボスバーのタイトルテンプレート
    private String bossbarTitleTemplate;

    /**
     * コンストラクタ
     * @param secondsReady タイマー開始までの設定時間
     * @param secondsGame タイマーの設定時間
     */
    public TimerTask(ExpTimer plugin, ExpTimerConfigData configData) {

        this.plugin = plugin;
        this.configData = configData;

        // 各種変数の初期化
        secondsReadyRest = configData.getReadySeconds();
        secondsGameRest = configData.getSeconds();
        secondsGameMax = configData.getSeconds();
        isPaused = false;

        tickReadyBase = System.currentTimeMillis() +
                configData.getReadySeconds() * 1000 + OFFSET;
        tickGameBase = tickReadyBase + configData.getSeconds() * 1000;

        flagStart = false;
        flagEnd = false;

        // runCommandsOnMidSecondsの初期化
        runCommandsOnMidSeconds = new ArrayList<Integer>();
        for ( Integer i : configData.getRunCommandsOnMidSeconds() ) {
            runCommandsOnMidSeconds.add(i);
        }
        Collections.sort(runCommandsOnMidSeconds);
        Collections.reverse(runCommandsOnMidSeconds);

        commandFlags = new boolean[runCommandsOnMidSeconds.size()];
        for ( int i=0; i<commandFlags.length; i++ ) {
            commandFlags[i] = ( secondsGameMax <= runCommandsOnMidSeconds.get(i) );
        }

        // restAlertSecondsの初期化
        restAlertSeconds = new ArrayList<Integer>();
        for ( Integer i : configData.getRestAlertSeconds() ) {
            restAlertSeconds.add(i);
        }
        Collections.sort(restAlertSeconds);
        Collections.reverse(restAlertSeconds);

        alertFlags = new boolean[restAlertSeconds.size()];
        for ( int i=0; i<alertFlags.length; i++ ) {
            alertFlags[i] = ( secondsGameMax <= restAlertSeconds.get(i) );
        }

        // objectiveの取得
        if ( configData.isUseSideBar() ) {
            Scoreboard sb = Bukkit.getScoreboardManager().getMainScoreboard();

            // 既にオブジェクティブがあるなら、いったんクリアする
            objective = sb.getObjective("exptimer");
            if ( objective != null ) {
                objective.unregister();
            }

            // 新しいオブジェクティブを作成する
            objective = sb.registerNewObjective("exptimer", "dummy");
            String sidebarTitle = configData.getMessages().get("sidebarTitle");
            if ( sidebarTitle == null || sidebarTitle.equals("") ) {
                sidebarTitle = "残り時間";
            }
            objective.setDisplayName(Utility.replaceColorCode(sidebarTitle));
            sidebarItem = null;

            // サイドバーに表示
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        }

        // ボスバーのタイトルのテンプレートを取得
        if ( configData.isUseBossBar() ) {
            bossbarTitleTemplate = configData.getMessages().get("bossbarTitle");
            if ( bossbarTitleTemplate == null || bossbarTitleTemplate.equals("") ) {
                bossbarTitleTemplate = "&6残り時間 - %s";
            }
            bossbarTitleTemplate = Utility.replaceColorCode(bossbarTitleTemplate);
        }
    }

    /**
     * 1秒ごとに呼び出しされるメソッド
     */
    @Override
    public void run() {

        // 終了フラグが既に立っている場合は、スケジュール解除して何もしない
        if ( flagEnd ) {
            endTimer();
            return;
        }

        // 更新実行
        refreshRests();

        // 条件に応じてアナウンス
        if ( !flagStart &&
                secondsReadyRest <= configData.getCountdownOnStart() ) {

            if ( secondsReadyRest > 0 ) {
                // スタート前のカウントダウン
                broadcastMessage("preStartSec", secondsReadyRest);
                if ( configData.isPlaySound() ) {
                    playCountdownSound();
                }
            } else {
                // スタート
                flagStart = true;
                broadcastMessage("start");
                if ( configData.isPlaySound() ) {
                    playStartEndSound();
                }
                // コマンドの実行
                plugin.dispatchCommandsBySender(configData.getCommandsOnStart());
                plugin.dispatchCommandsByConsole(configData.getConsoleCommandsOnStart());
            }

        } else if ( !flagEnd ) {

            // 残りｎ分の告知処理
            for ( int index=0; index<alertFlags.length; index++ ) {
                if ( !alertFlags[index]
                        && secondsGameRest <= restAlertSeconds.get(index) ) {
                    alertFlags[index] = true;
                    broadcastAlertMessage(restAlertSeconds.get(index));
                    break;
                } else if ( secondsGameRest > restAlertSeconds.get(index) ) {
                    break;
                }
            }

            // 残りｎ分のコマンド実行
            for ( int index=0; index<commandFlags.length; index++ ) {
                if ( !commandFlags[index]
                        && secondsGameRest <= runCommandsOnMidSeconds.get(index) ) {
                    commandFlags[index] = true;
                    // コマンドの実行
                    plugin.dispatchCommandsBySender(configData.getCommandsOnMid());
                    plugin.dispatchCommandsByConsole(configData.getConsoleCommandsOnMid());
                    break;
                } else if ( secondsGameRest > runCommandsOnMidSeconds.get(index) ) {
                    break;
                }
            }

            if ( 0 < secondsGameRest &&
                    secondsGameRest <= configData.getCountdownOnEnd() ) {
                // 終了前のカウントダウン
                broadcastMessage("preEndSec", secondsGameRest);
                if ( configData.isPlaySound() ) {
                    playCountdownSound();
                }
            } else if ( secondsGameRest <= 0 ) {
                // 終了
                flagEnd = true;
                broadcastMessage("end");
                if ( configData.isPlaySound() ) {
                    playStartEndSound();
                }
                // タスクの終了を呼び出し
                plugin.endTask(true);
                // スケジュール解除
                endTimer();
            }
        }

        // 経験値バーの表示更新
        if ( configData.isUseExpBar() ) {
            refreshExpbar();
        }

        // サイドバーの表示更新
        if ( configData.isUseSideBar() ) {
            refreshSidebar();
        }

        // ボスバーの表示更新
        if ( configData.isUseBossBar() ) {
            refreshBossbar();
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
     * 経験値バーの表示を更新する
     */
    private void refreshExpbar() {

        float progress = (float)secondsGameRest / (float)secondsGameMax;
        if ( progress > 1.0f ) {
            progress = 1.0f;
        } else if ( progress < 0.0f ) {
            progress = 0.0f;
        }

        int level = secondsGameRest;
        if ( level > 24000 ) {
            level = 24000;
        } else if ( level < 0 ) {
            level = 0;
        }

        for ( Player player : getRefreshTargets() ) {
            player.setLevel(level);
            player.setExp(progress);
        }
    }

    /**
     * 経験値バーの表示を0にリセットする
     */
    protected void resetExpbar() {

        for ( Player player : getRefreshTargets() ) {
            player.setLevel(0);
            player.setExp(0);
        }
    }

    /**
     * サイドバー表示を更新する
     */
    private void refreshSidebar() {

        if ( objective == null ) {
            return;
        }

        int hour = secondsGameRest / 3600;
        int minute = (secondsGameRest - hour * 3600) / 60;
        int second = secondsGameRest - hour * 3600 - minute * 60;

        String name = String.format(ChatColor.RED + "%02d:%02d:", hour, minute);

        if ( sidebarItem == null ) {
            sidebarItem = name;
        } else if ( !name.equals(sidebarItem) ) {
            removeScore(objective, sidebarItem);
            sidebarItem = name;
        }

        getScore(objective, sidebarItem).setScore(second);
    }

    /**
     * サイドバーを非表示にする
     */
    protected void removeSidebar() {

        if ( sidebarItem != null ) {
            removeScore(objective, sidebarItem);
        }
        sidebarItem = null;

        Scoreboard sb = Bukkit.getScoreboardManager().getMainScoreboard();
        Objective sideObj = sb.getObjective(DisplaySlot.SIDEBAR);
        if ( sideObj != null && sideObj.getName().equals("exptimer") ) {
            sb.clearSlot(DisplaySlot.SIDEBAR);
            sideObj.unregister();
        }
        objective = null;
    }

    /**
     * ボスバー表示を更新する。
     */
    private void refreshBossbar() {

        if ( ExpTimer.getInstance().getBarAPI() == null ) {
            return;
        }

        BarAPIBridge barapi = ExpTimer.getInstance().getBarAPI();

        float progress = (float)secondsGameRest / (float)secondsGameMax;
        if ( secondsGameRest <= 0 || progress < 0.0f ) {
            // BarAPIを消去して終わる
            for ( Player player : getRefreshTargets() ) {
                barapi.removeBar(player);
            }
            return;
        }

        if ( progress > 1.0f ) {
            progress = 1.0f;
        }
        progress *= 100;

        int hour = secondsGameRest / 3600;
        int minute = (secondsGameRest - hour * 3600) / 60;
        int second = secondsGameRest - hour * 3600 - minute * 60;
        String timeMessage = String.format("%02d:%02d:%02d", hour, minute, second);
        String message = String.format(bossbarTitleTemplate, timeMessage);

        for ( Player player : getRefreshTargets() ) {
            barapi.setMessage(player, message, progress);
        }
    }

    /**
     * ボスバーを非表示にする。
     */
    protected void removeBossbar() {

        if ( ExpTimer.getInstance().getBarAPI() == null ) {
            return;
        }

        for ( Player player : getRefreshTargets() ) {
            ExpTimer.getInstance().getBarAPI().removeBar(player);
        }
    }

    /**
     * メッセージリソースを取得し、ブロードキャストする
     * @param key メッセージキー
     * @param args メッセージの引数
     */
    private void broadcastMessage(String key, Object... args) {

        String msg = configData.getMessages().get(key);
        if ( msg == null || msg.equals("") ) {
            return;
        }
        msg = String.format(msg, args);
        String prefix = configData.getMessages().get("prefix");

        for ( Player player : getRefreshTargets() ) {
            player.sendMessage(Utility.replaceColorCode(prefix + msg));
        }
    }

    /**
     * 残り時間の通知をブロードキャストする
     * @param seconds 残り時間
     */
    private void broadcastAlertMessage(int seconds) {

        String key = "rest" + seconds + "sec";
        String msg = configData.getMessages().get(key);
        if ( msg == null || msg.equals("") ) {
            msg = configData.getMessages().get("preEndSec");
            msg = String.format(msg, seconds);
        }
        String prefix = configData.getMessages().get("prefix");

        for ( Player player : getRefreshTargets() ) {
            player.sendMessage(Utility.replaceColorCode(prefix + msg));
        }
    }

    /**
     * カウントダウンの音を出す
     */
    private void playCountdownSound() {

        String name = configData.getPlaySoundCountdown();
        Sound sound;
        if ( isValidSoundName(name) ) {
            sound = Sound.valueOf(name);
        } else {
            sound = Sound.NOTE_STICKS;
        }

        for ( Player player : getRefreshTargets() ) {
            player.playSound(player.getEyeLocation(), sound, 1.0F, 1.0F);
        }
    }

    /**
     * 開始音・終了音を出す
     */
    private void playStartEndSound() {

        String name = configData.getPlaySoundStartEnd();
        Sound sound;
        if ( isValidSoundName(name) ) {
            sound = Sound.valueOf(name);
        } else {
            sound = Sound.NOTE_PLING;
        }

        for ( Player player : getRefreshTargets() ) {
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

    /**
     * タイマーのスケジュールを行う
     */
    protected void startTimer() {
        task = Bukkit.getScheduler().runTaskTimer(plugin, this, 20, 20);
    }

    /**
     * タイマーのスケジュール解除を行う
     */
    protected void endTimer() {
        if ( task != null ) {
            Bukkit.getScheduler().cancelTask(task.getTaskId());
        }
    }

    /**
     * コンフィグ内容に応じて、タイマー表示対象のプレイヤーリストを取得する
     * @return タイマー表示対象プレイヤー
     */
    private ArrayList<Player> getRefreshTargets() {

        ArrayList<Player> targets = new ArrayList<Player>();
        if ( configData.isAnnounceToOnlyTeamMembers() &&
                ExpTimer.getInstance().getColorTeaming() != null ) {
            HashMap<String, ArrayList<Player>> members =
                    ExpTimer.getInstance().getColorTeaming().getTeamMembers();
            for ( ArrayList<Player> players : members.values() ) {
                if ( players != null ) {
                    for ( Player player : players ) {
                        targets.add(player);
                    }
                }
            }
        } else {
            for ( Player player : Bukkit.getOnlinePlayers() ) {
                targets.add(player);
            }
        }

        return targets;
    }

    /**
     * スコア項目を取得する
     * @param objective
     * @param name
     * @return
     */
    private static Score getScore(Objective objective, String name) {

        if ( Utility.isCB178orLater() ) {
            return objective.getScore(name);
        } else {
            @SuppressWarnings("deprecation")
            Score score = objective.getScore(Bukkit.getOfflinePlayer(name));
            return score;
        }
    }

    /**
     * スコア項目を削除する
     * @param objective
     * @param name
     */
    @SuppressWarnings("deprecation")
    private static void removeScore(Objective objective, String name) {

        Scoreboard sb = Bukkit.getScoreboardManager().getMainScoreboard();

        if ( Utility.isCB178orLater() ) {
            objective.getScore(name).setScore(0);
            sb.resetScores(name);
        } else {
            OfflinePlayer item = Bukkit.getOfflinePlayer(name);
            Score score = objective.getScore(item);
            score.setScore(0);
            sb.resetScores(item);
        }
    }
}
