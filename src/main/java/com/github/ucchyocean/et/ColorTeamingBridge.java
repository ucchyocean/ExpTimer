/*
 * @author     ucchy
 * @license    LGPLv3
 * @copyright  Copyright ucchy 2013
 */
package com.github.ucchyocean.et;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import com.github.ucchyocean.ct.ColorTeaming;
import com.github.ucchyocean.ct.ColorTeamingAPI;
import com.github.ucchyocean.ct.config.TeamNameSetting;
import com.github.ucchyocean.ct.event.ColorTeamingTeamscoreChangeEvent;
import com.github.ucchyocean.ct.event.ColorTeamingTrophyKillEvent;
import com.github.ucchyocean.ct.event.ColorTeamingWonLeaderEvent;
import com.github.ucchyocean.ct.event.ColorTeamingWonTeamEvent;

/**
 * ColorTeaming連携クラス
 * @author ucchy
 */
public class ColorTeamingBridge implements Listener {

    private ExpTimer plugin;
    private ColorTeamingAPI ctapi;

    /**
     * コンストラクタ
     * @param plugin ExpTimerのインスタンス
     * @param colorteaming ColorTeamingのインスタンス
     */
    protected ColorTeamingBridge(ExpTimer plugin, Plugin colorteaming) {
        this.plugin = plugin;
        this.ctapi = ((ColorTeaming)colorteaming).getAPI();
    }

    /**
     * ColorTeaming チーム全滅イベント
     * @param event
     */
    @EventHandler
    public void onTeamWon(ColorTeamingWonTeamEvent event) {

        // 設定オフなら、ここで終了する
        if ( plugin.getConfigData() != null &&
                !plugin.getConfigData().isEndWithCTTeamDefeat() ) {
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
        if ( plugin.getConfigData() != null &&
                !plugin.getConfigData().isEndWithCTLeaderDefeat() ) {
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
        if ( plugin.getConfigData() != null &&
                !plugin.getConfigData().isEndWithCTKillTrophy() ) {
            return;
        }

        endTask(event.getTeam().getName());
    }

    /**
     * ColorTeaming チームポイント変動イベント
     * @param event
     */
    @EventHandler
    public void onTeamPointChange(ColorTeamingTeamscoreChangeEvent event) {

        int before = event.getPointBefore();
        int after = event.getPointAfter();

        // ポイントが基準値を下回ったかどうかを確認する。
        int threshold = plugin.getConfigData().getEndWithTeamPointUnder();
        if ( threshold < before && after <= threshold ) {
            endTask(event.getTeam());
            return;
        }

        // ポイントが基準値を上回ったかどうかを確認する。
        threshold = plugin.getConfigData().getEndWithTeamPointOver();
        if ( before < threshold && threshold <= after ) {
            endTask(event.getTeam());
            return;
        }
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
        plugin.endTask(true);
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

        ExpTimerConfigData configData = ExpTimer.getInstance().configData;
        if ( configData == null ) {
            return;
        }
        String msg = configData.getMessages().get(key);
        if ( msg == null || msg.equals("") ) {
            return;
        }
        msg = String.format(msg, args);
        String prefix = configData.getMessages().get("prefix");
        
        if ( configData.isAnnounceToOnlyTeamMembers() ) {
            HashMap<String, ArrayList<Player>> members = getTeamMembers();
            for ( ArrayList<Player> players : members.values() ) {
                if ( players != null ) {
                    for ( Player player : players ) {
                        player.sendMessage(Utility.replaceColorCode(prefix + msg));
                    }
                }
            }
        } else {
            Bukkit.broadcastMessage(Utility.replaceColorCode(prefix + msg));
        }
    }
    
    /**
     * カラーチーミングのチームに所属しているプレイヤーの一覧を返す
     * @return チームに所属しているプレイヤー
     */
    public HashMap<String, ArrayList<Player>> getTeamMembers() {
        return ctapi.getAllTeamMembers();
    }
}
