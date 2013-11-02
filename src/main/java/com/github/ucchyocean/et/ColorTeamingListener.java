/*
 * @author     ucchy
 * @license    LGPLv3
 * @copyright  Copyright ucchy 2013
 */
package com.github.ucchyocean.et;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.github.ucchyocean.ct.config.TeamNameSetting;
import com.github.ucchyocean.ct.event.ColorTeamingTrophyKillEvent;
import com.github.ucchyocean.ct.event.ColorTeamingWonLeaderEvent;
import com.github.ucchyocean.ct.event.ColorTeamingWonTeamEvent;

/**
 * ColorTeaming連携機能リスナークラス
 * @author ucchy
 */
public class ColorTeamingListener implements Listener {

    private ExpTimer plugin;

    protected ColorTeamingListener(ExpTimer plugin) {
        this.plugin = plugin;
    }

    /**
     * ColorTeaming チーム全滅イベント
     * @param event
     */
    @EventHandler
    public void onTeamWon(ColorTeamingWonTeamEvent event) {

        // 設定オフなら、ここで終了する
        if ( !ExpTimer.config.endWithCTTeamDefeat ) {
            return;
        }

        endTask(event.getWonTeamName());
    }

    /**
     * ColorTeaming 大将全滅イベント
     * @param event
     */
    @EventHandler
    public void onLeaderDefeat(ColorTeamingWonLeaderEvent event) {

        // 設定オフなら、ここで終了する
        if ( !ExpTimer.config.endWithCTLeaderDefeat ) {
            return;
        }

        endTask(event.getWonTeamName());
    }

    /**
     * ColorTeaming kill数達成イベント
     * @param event
     */
    @EventHandler
    public void onKillTrophy(ColorTeamingTrophyKillEvent event) {

        // 設定オフなら、ここで終了する
        if ( !ExpTimer.config.endWithCTLeaderDefeat ) {
            return;
        }

        endTask(event.getTeam().getName());
    }

    /**
     * タスクを終了する
     * @param wonTeamName 勝利したチームのチーム名
     */
    private void endTask(String wonTeamName) {

        // タイマーが開始していないなら、ここで終了する。
        TimerTask task = plugin.getTask();
        if ( task == null ) {
            return;
        }

        // メッセージを流す
        broadcastMessage("onTeamWon", wonTeamName);

        // タスク終了
        plugin.endTask();
        
        // リピート設定なら、新しいタスクを再スケジュール
        if ( ExpTimer.config.nextConfig != null &&
                ExpTimer.configs.containsKey(ExpTimer.config.nextConfig) ) {
            plugin.startNewTask(ExpTimer.config.nextConfig, null);
        }
    }

    /**
     * タスクを終了する
     * @param wonTeamName 勝利したチームのチーム名
     */
    private void endTask(TeamNameSetting wonTeamName) {

        endTask(wonTeamName.toString() + ChatColor.RESET);
    }

    /**
     * メッセージリソースを取得し、ブロードキャストする
     * @param key メッセージキー
     * @param args メッセージの引数
     * @return メッセージ
     */
    private void broadcastMessage(String key, Object... args) {

        String prefix = Messages.get("prefix");
        String msg = Messages.get(key, args);
        if ( msg.equals("") ) {
            return;
        }
        Bukkit.broadcastMessage(Utility.replaceColorCode(prefix + msg));
    }
}
