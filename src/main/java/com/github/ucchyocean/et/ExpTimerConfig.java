/*
 * @author     ucchy
 * @license    LGPLv3
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
public class ExpTimerConfig {

    /** タイマー時間（秒） */
    protected int seconds;

    /** 開始前タイマー時間（秒） */
    protected int readySeconds;

    /** タイマースタート時に実行するコマンド */
    protected List<String> commandsOnStart;

    /** タイマー終了時に実行するコマンド */
    protected List<String> commandsOnEnd;

    /** タイマースタート時にコンソールで実行するコマンド */
    protected List<String> consoleCommandsOnStart;

    /** タイマー終了時にコンソールで実行するコマンド */
    protected List<String> consoleCommandsOnEnd;

    /** タイマースタート前のカウントダウン時間（秒） */
    protected int countdownOnStart;

    /** タイマー終了前のカウントダウン時間（秒） */
    protected int countdownOnEnd;

    /** 残り時間アラートの時間（秒） */
    protected List<Integer> restAlertSeconds;

    /** カウントダウン中に音を出すかどうか */
    protected boolean playSound;

    /** カウントダウン時の音の種類（隠しオプション） */
    protected String playSoundCountdown;

    /** スタート時・終了時の音の種類（隠しオプション） */
    protected String playSoundStartEnd;

    /** 経験値バーをタイマー表示として使用するかどうか */
    protected boolean useExpBar;
    
    /** サイドバーをタイマー表示として使用するかどうか */
    protected boolean useSideBar;

    /** メッセージファイルのファイル名指定 */
    protected String messageFileName;

    /** 次に自動で実行するタスクのコンフィグ名 */
    protected String nextConfig;

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
    protected static ExpTimerConfig loadFromSection(ConfigurationSection section, ExpTimerConfig defaults) {

        ExpTimerConfig data = new ExpTimerConfig();
        if ( defaults == null ) {
            data.seconds = section.getInt("seconds", 600);
            data.readySeconds = section.getInt("readySeconds", 10);
            data.commandsOnStart = section.getStringList("commandsOnStart");
            data.commandsOnEnd = section.getStringList("commandsOnEnd");
            data.consoleCommandsOnStart = section.getStringList("consoleCommandsOnStart");
            data.consoleCommandsOnEnd = section.getStringList("consoleCommandsOnEnd");
            data.countdownOnStart = section.getInt("countdownOnStart", 3);
            data.countdownOnEnd = section.getInt("countdownOnEnd", 5);
            if ( section.contains("restAlertSeconds") ) {
                data.restAlertSeconds = section.getIntegerList("restAlertSeconds");
            } else {
                data.restAlertSeconds = new ArrayList<Integer>();
                data.restAlertSeconds.add(60);
                data.restAlertSeconds.add(180);
                data.restAlertSeconds.add(300);
            }
            data.playSound = section.getBoolean("playSound", true);
            data.playSoundCountdown = section.getString("playSoundCountdown", "NOTE_STICKS");
            data.playSoundStartEnd = section.getString("playSoundStartEnd", "NOTE_PLING");
            data.useExpBar = section.getBoolean("useExpBar", true);
            data.useSideBar = section.getBoolean("useSideBar", false);
            data.messageFileName = section.getString("messageFileName");
            data.nextConfig = section.getString("nextConfig");
            data.endWithCTTeamDefeat = section.getBoolean("endWithCTTeamDefeat", false);
            data.endWithCTLeaderDefeat = section.getBoolean("endWithCTLeaderDefeat", false);
            data.endWithCTKillTrophy = section.getBoolean("endWithCTKillTrophy", false);
        } else {
            data.seconds = section.getInt("seconds", defaults.seconds);
            data.readySeconds = section.getInt("readySeconds", defaults.readySeconds);
            if ( section.contains("commandsOnStart") )
                data.commandsOnStart = section.getStringList("commandsOnStart");
            else
                data.commandsOnStart = copyStringList(defaults.commandsOnStart);
            if ( section.contains("commandsOnEnd") )
                data.commandsOnEnd = section.getStringList("commandsOnEnd");
            else
                data.commandsOnEnd = copyStringList(defaults.commandsOnEnd);
            if ( section.contains("consoleCommandsOnStart") )
                data.consoleCommandsOnStart = section.getStringList("consoleCommandsOnStart");
            else
                data.consoleCommandsOnStart = copyStringList(defaults.consoleCommandsOnStart);
            if ( section.contains("consoleCommandsOnEnd") )
                data.consoleCommandsOnEnd = section.getStringList("consoleCommandsOnEnd");
            else
                data.consoleCommandsOnEnd = copyStringList(defaults.consoleCommandsOnEnd);
            data.countdownOnStart = section.getInt("countdownOnStart", defaults.countdownOnStart);
            data.countdownOnEnd = section.getInt("countdownOnEnd", defaults.countdownOnEnd);
            if ( section.contains("restAlertSeconds") )
                data.restAlertSeconds = section.getIntegerList("restAlertSeconds");
            else
                data.restAlertSeconds = copyIntegerList(defaults.restAlertSeconds);
            data.playSound = section.getBoolean("playSound", defaults.playSound);
            data.playSoundCountdown = 
                section.getString("playSoundCountdown", defaults.playSoundCountdown);
            data.playSoundStartEnd = 
                section.getString("playSoundStartEnd", defaults.playSoundStartEnd);
            data.useExpBar = section.getBoolean("useExpBar", defaults.useExpBar);
            data.useSideBar = section.getBoolean("useSideBar", defaults.useSideBar);
            data.endWithCTTeamDefeat =
                section.getBoolean("endWithCTTeamDefeat", defaults.endWithCTTeamDefeat);
            data.endWithCTLeaderDefeat =
                section.getBoolean("endWithCTLeaderDefeat", defaults.endWithCTLeaderDefeat);
            data.endWithCTKillTrophy =
                section.getBoolean("endWithCTKillTrophy", defaults.endWithCTKillTrophy);

            // メッセージファイル設定、次タスク設定は、デフォルトを引き継がない。
            data.messageFileName = section.getString("messageFileName");
            data.nextConfig = section.getString("nextConfig");
        }

        return data;
    }

    /**
     * オブジェクトのディープコピーを行う
     * @param org オリジナル
     * @return コピー
     */
    protected ExpTimerConfig clone() {

        ExpTimerConfig data = new ExpTimerConfig();
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
        data.consoleCommandsOnStart = new ArrayList<String>();
        for ( String c : this.consoleCommandsOnStart ) {
            data.consoleCommandsOnStart.add(c);
        }
        data.consoleCommandsOnEnd = new ArrayList<String>();
        for ( String c : this.consoleCommandsOnEnd ) {
            data.consoleCommandsOnEnd.add(c);
        }
        data.countdownOnStart = this.countdownOnStart;
        data.countdownOnEnd = this.countdownOnEnd;
        data.restAlertSeconds = new ArrayList<Integer>();
        for ( Integer c : this.restAlertSeconds ) {
            data.restAlertSeconds.add(c);
        }
        data.playSound = this.playSound;
        data.playSoundCountdown = this.playSoundCountdown;
        data.playSoundStartEnd = this.playSoundStartEnd;
        data.useExpBar = this.useExpBar;
        data.useSideBar = this.useSideBar;
        data.messageFileName = this.messageFileName;
        data.nextConfig = this.nextConfig;
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
    private static List<String> copyStringList(List<String> org) {

        ArrayList<String> data = new ArrayList<String>();
        for ( String d : org ) {
            data.add(d);
        }
        return data;
    }

    /**
     * List&gt;Integer&lt;のコピーを行う
     * @param org オリジナル
     * @return コピー
     */
    private static List<Integer> copyIntegerList(List<Integer> org) {

        ArrayList<Integer> data = new ArrayList<Integer>();
        for ( Integer d : org ) {
            data.add(d);
        }
        return data;
    }
}
