/*
 * @author     ucchy
 * @license    LGPLv3
 * @copyright  Copyright ucchy 2014
 */
package com.github.ucchyocean.et;

import me.confuser.barapi.BarAPI;

import org.bukkit.entity.Player;

/**
 * BarAPI連携クラス
 * @author ucchy
 */
public class BarAPIBridge {

    /**
     * BarAPIに、メッセージとゲージ量を設定する
     * @param player 対象プレイヤー
     * @param message メッセージ
     * @param percent ゲージ量（最大値が 100.0、最小値が 0.0）
     */
    public void setMessage(Player player, String message, float percent) {
        BarAPI.setMessage(player, message, percent);
    }

    /**
     * 指定したプレイヤーのBarを削除する。
     * @param player 対象プレイヤー
     */
    public void removeBar(Player player) {
        BarAPI.removeBar(player);
    }
}
