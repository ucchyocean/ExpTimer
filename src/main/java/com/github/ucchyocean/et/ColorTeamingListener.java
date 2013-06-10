/*
 * @author     ucchy
 * @license    GPLv3
 * @copyright  Copyright ucchy 2013
 */
package com.github.ucchyocean.et;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import com.github.ucchyocean.ct.ColorTeaming;
import com.github.ucchyocean.ct.ColorTeamingAPI;
import com.github.ucchyocean.ct.event.ColorTeamingLeaderDefeatedEvent;
import com.github.ucchyocean.ct.event.ColorTeamingTeamDefeatedEvent;
import com.github.ucchyocean.ct.event.ColorTeamingTrophyKillEvent;

/**
 * ColorTeaming連携機能リスナークラス
 * @author ucchy
 */
public class ColorTeamingListener implements Listener {

    private ExpTimer plugin;
    private ColorTeaming ctplugin;

    protected ColorTeamingListener(ExpTimer plugin, Plugin ctplugin) {
        this.plugin = plugin;
        this.ctplugin = (ColorTeaming)ctplugin;
    }

    @EventHandler
    public void onTeamDefeat(ColorTeamingTeamDefeatedEvent event) {

        // 設定オフなら、ここで終了する
        if ( !ExpTimer.config.endWithCTTeamDefeat ) {
            return;
        }

        // タイマーが開始していないなら、ここで終了する。
        TimerTask task = plugin.getTask();
        if ( task == null ) {
            return;
        }

        // 残り1チームになったのかどうかを確認する
        ColorTeamingAPI api = ctplugin.getAPI();
        // TODO: getAllTeamMembers だと重いので、getAllTeamNames に変更したい
        // TODO: もしくは、ColorTeaming側に、残り1チームになったというイベントを作る
        HashMap<String, ArrayList<Player>> members = api.getAllTeamMembers();

        ArrayList<String> noneMemberTeam = new ArrayList<String>();
        for ( String teamName : members.keySet() ) {
            if ( members.get(teamName).size() <= 0 ) {
                noneMemberTeam.add(teamName);
            }
        }
        for ( String t : noneMemberTeam ) {
            members.remove(t);
        }

        if ( members.size() == 1 ) {
            // TODO: メッセージを流す

            // タスク終了
            plugin.endTask();
        }
    }

    @EventHandler
    public void onLeaderDefeat(ColorTeamingLeaderDefeatedEvent event) {
        // TODO:
    }

    @EventHandler
    public void onKillTrophy(ColorTeamingTrophyKillEvent event) {
        // TODO:
    }
}
