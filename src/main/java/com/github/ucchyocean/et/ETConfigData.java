/*
 * @author     ucchy
 * @license    GPLv3
 * @copyright  Copyright ucchy 2013
 */
package com.github.ucchyocean.et;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;

/**
 * コンフィグデータコンポーネント
 * @author ucchy
 */
public class ETConfigData {

    /** タイマー時間（秒） */
    protected int seconds;

    /** 開始前タイマー時間（秒） */
    protected int readySeconds;

    /** タイマースタート時に実行するコマンド */
    protected List<String> commandsOnStart;

    /** タイマー終了時に実行するコマンド */
    protected List<String> commandsOnEnd;

    /** タイマースタート前のカウントダウン時間（秒） */
    protected int countdownOnStart;

    /** タイマー終了前のカウントダウン時間（秒） */
    protected int countdownOnEnd;

    /** 経験値バーをタイマー表示として使用するかどうか */
    protected boolean useExpBar;

    /** ColorTeamingのチームが全滅したら、タイマーを終了するかどうか */
    protected boolean endWithCTTeamDefeat;

    /** ColorTeamingのリーダーが全滅したら、タイマーを終了するかどうか */
    protected boolean endWithCTLeaderDefeat;

    /** ColorTeamingのKillTrophyが達成されたら、タイマーを終了するかどうか */
    protected boolean endWithCTKillTrophy;

    /**
     * コンフィグのセクションから、ETConfigDataを作成して返す
     * @param section セクション
     * @param defaults デフォルトデータ
     * @return 作成されたETConfigData
     */
    protected static ETConfigData loadFromSection(ConfigurationSection section, ETConfigData defaults) {

        ETConfigData data = new ETConfigData();
        if ( defaults == null ) {
            data.seconds = section.getInt("seconds", 600);
            data.readySeconds = section.getInt("readySeconds", 10);
            data.commandsOnStart = section.getStringList("commandsOnStart");
            if ( data.commandsOnStart == null ) {
                data.commandsOnStart = new ArrayList<String>();
            }
            data.commandsOnEnd = section.getStringList("commandsOnEnd");
            if ( data.commandsOnEnd == null ) {
                data.commandsOnEnd = new ArrayList<String>();
            }
            data.countdownOnStart = section.getInt("countdownOnStart", 3);
            data.countdownOnEnd = section.getInt("countdownOnEnd", 5);
            data.useExpBar = section.getBoolean("useExpBar", true);
            data.endWithCTTeamDefeat = section.getBoolean("endWithCTTeamDefeat", false);
            data.endWithCTLeaderDefeat = section.getBoolean("endWithCTLeaderDefeat", false);
            data.endWithCTKillTrophy = section.getBoolean("endWithCTKillTrophy", false);
        } else {
            data.seconds = section.getInt("seconds", defaults.seconds);
            data.readySeconds = section.getInt("readySeconds", defaults.readySeconds);
            if ( section.contains("commandsOnStart") )
                data.commandsOnStart = section.getStringList("commandsOnStart");
            else
                data.commandsOnStart = copyList(defaults.commandsOnStart);
            if ( section.contains("commandsOnEnd") )
                data.commandsOnEnd = section.getStringList("commandsOnEnd");
            else
                data.commandsOnEnd = copyList(defaults.commandsOnEnd);
            data.countdownOnStart = section.getInt("countdownOnStart", defaults.countdownOnStart);
            data.countdownOnEnd = section.getInt("countdownOnEnd", defaults.countdownOnEnd);
            data.useExpBar = section.getBoolean("useExpBar", defaults.useExpBar);
            data.endWithCTTeamDefeat =
                    section.getBoolean("endWithCTTeamDefeat", defaults.endWithCTTeamDefeat);
            data.endWithCTLeaderDefeat =
                section.getBoolean("endWithCTLeaderDefeat", defaults.endWithCTLeaderDefeat);
            data.endWithCTKillTrophy =
                section.getBoolean("endWithCTKillTrophy", defaults.endWithCTKillTrophy);
        }

        return data;
    }

    /**
     * オブジェクトのディープコピーを行う
     * @param org オリジナル
     * @return コピー
     */
    protected ETConfigData clone() {

        ETConfigData data = new ETConfigData();
        data.seconds = this.seconds;
        data.readySeconds = this.readySeconds;
        data.commandsOnStart = new ArrayList<String>();
        for ( String c : this.commandsOnStart ) {
            data.commandsOnStart.add(c);
        }
        data.commandsOnEnd = new ArrayList<String>();
        for ( String c : this.commandsOnEnd ) {
            data.commandsOnEnd.add(c);
        }
        data.countdownOnStart = this.countdownOnStart;
        data.countdownOnEnd = this.countdownOnEnd;
        data.useExpBar = this.useExpBar;
        data.endWithCTTeamDefeat = this.endWithCTTeamDefeat;
        data.endWithCTLeaderDefeat = this.endWithCTLeaderDefeat;
        data.endWithCTKillTrophy = this.endWithCTKillTrophy;

        return data;
    }

    /**
     * List&gt;String&lt;のコピーを行う
     * @param org オリジナル
     * @return コピー
     */
    private static List<String> copyList(List<String> org) {

        ArrayList<String> data = new ArrayList<String>();
        for ( String d : org ) {
            data.add(d);
        }
        return data;
    }
}
