/*
 * @author     ucchy
 * @license    LGPLv3
 * @copyright  Copyright ucchy 2019
 */
package com.github.ucchyocean.et;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

/**
 * BossBarの表示管理を行うクラス
 * @author ucchy
 */
public class BossBarManager {

    private BossBar bar;
    private boolean isEnd;

    public BossBarManager(List<Player> players) {
        bar = Bukkit.createBossBar("", BarColor.RED, BarStyle.SEGMENTED_10);
        for ( Player player : players ) {
            bar.addPlayer(player);
        }
        isEnd = false;
    }

    /**
     * BossBarに、メッセージとゲージ量を設定する
     * @param message メッセージ
     * @param percent ゲージ量（最大値が 100.0、最小値が 0.0）
     */
    public void setMessage(String message, float percent) {
        float value = percent / 100;
        if ( value > 1 ) value = 1;
        if ( value < 0 ) value = 0;
        bar.setTitle(message);
        bar.setProgress(value);
    }

    /**
     * BossBarを削除する。
     * @param player 対象プレイヤー
     */
    public void removeBar() {
        bar.removeAll();
        isEnd = true;
    }

    /**
     * このBossBarが終了状態かどうかを返す
     * @return
     */
    public boolean isEnd() {
        return isEnd;
    }
}
