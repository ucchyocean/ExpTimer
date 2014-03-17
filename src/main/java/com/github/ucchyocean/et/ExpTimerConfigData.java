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
public class ExpTimerConfigData {

    /** コンフィグ名 */
    protected String name;

    /** タイマー時間（秒） */
    protected int seconds;

    /** 開始前タイマー時間（秒） */
    protected int readySeconds;

    /** タイマースタート時に実行するコマンド */
    protected List<String> commandsOnStart;

    /** 設定した時間に実行するコマンド */
    protected List<String> commandsOnMid;

    /** タイマー終了時に実行するコマンド */
    protected List<String> commandsOnEnd;

    /** タイマースタート時にコンソールで実行するコマンド */
    protected List<String> consoleCommandsOnStart;

    /** 設定した時間にコンソールで実行するコマンド */
    protected List<String> consoleCommandsOnMid;

    /** タイマー終了時にコンソールで実行するコマンド */
    protected List<String> consoleCommandsOnEnd;

    /** タイマースタート前のカウントダウン時間（秒） */
    protected int countdownOnStart;

    /** タイマー終了前のカウントダウン時間（秒） */
    protected int countdownOnEnd;

    /** タイマー途中でコマンドを実行する時間 */
    protected List<Integer> runCommandsOnMidSeconds;

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

    /** メッセージファイル */
    protected ExpTimerMessages messages;

    /**
     * コンストラクタ（外部からのアクセス不可）
     */
    private ExpTimerConfigData() {
    }

    /**
     * コンフィグのセクションから、ETConfigDataを作成して返す
     * @param name コンフィグ名
     * @param section セクション
     * @param defaults デフォルトデータ
     * @return 作成されたETConfigData
     */
    protected static ExpTimerConfigData loadFromSection(
            String name, ConfigurationSection section, ExpTimerConfigData defaults) {

        ExpTimerConfigData data = new ExpTimerConfigData();

        data.name = name;

        if ( defaults == null ) {
            data.seconds = section.getInt("seconds", 600);
            data.readySeconds = section.getInt("readySeconds", 10);
            data.commandsOnStart = section.getStringList("commandsOnStart");
            data.commandsOnMid = section.getStringList("commandsOnMid");
            data.commandsOnEnd = section.getStringList("commandsOnEnd");
            data.consoleCommandsOnStart = section.getStringList("consoleCommandsOnStart");
            data.consoleCommandsOnMid = section.getStringList("consoleCommandsOnMid");
            data.consoleCommandsOnEnd = section.getStringList("consoleCommandsOnEnd");
            data.countdownOnStart = section.getInt("countdownOnStart", 3);
            data.countdownOnEnd = section.getInt("countdownOnEnd", 5);
            data.runCommandsOnMidSeconds = section.getIntegerList("runCommandsOnMidSeconds");
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
            if ( section.contains("commandsOnMid") )
                data.commandsOnMid = section.getStringList("commandsOnMid");
            else
                data.commandsOnMid = copyStringList(defaults.commandsOnMid);
            if ( section.contains("commandsOnEnd") )
                data.commandsOnEnd = section.getStringList("commandsOnEnd");
            else
                data.commandsOnEnd = copyStringList(defaults.commandsOnEnd);
            if ( section.contains("consoleCommandsOnStart") )
                data.consoleCommandsOnStart = section.getStringList("consoleCommandsOnStart");
            else
                data.consoleCommandsOnStart = copyStringList(defaults.consoleCommandsOnStart);
            if ( section.contains("consoleCommandsOnMid") )
                data.consoleCommandsOnMid = section.getStringList("consoleCommandsOnMid");
            else
                data.consoleCommandsOnMid = copyStringList(defaults.consoleCommandsOnMid);
            if ( section.contains("consoleCommandsOnEnd") )
                data.consoleCommandsOnEnd = section.getStringList("consoleCommandsOnEnd");
            else
                data.consoleCommandsOnEnd = copyStringList(defaults.consoleCommandsOnEnd);
            data.countdownOnStart = section.getInt("countdownOnStart", defaults.countdownOnStart);
            data.countdownOnEnd = section.getInt("countdownOnEnd", defaults.countdownOnEnd);
            if ( section.contains("runCommandsOnMidSeconds") )
                data.runCommandsOnMidSeconds = section.getIntegerList("runCommandsOnMidSeconds");
            else
                data.runCommandsOnMidSeconds = copyIntegerList(defaults.runCommandsOnMidSeconds);
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

        data.messages = new ExpTimerMessages(data.messageFileName);

        return data;
    }

    /**
     * オブジェクトのディープコピーを行う
     * @param org オリジナル
     * @return コピー
     */
    protected ExpTimerConfigData clone() {

        ExpTimerConfigData data = new ExpTimerConfigData();
        data.name = this.name;
        data.seconds = this.seconds;
        data.readySeconds = this.readySeconds;
        data.commandsOnStart = new ArrayList<String>();
        for ( String c : this.commandsOnStart ) {
            data.commandsOnStart.add(c);
        }
        data.commandsOnMid = new ArrayList<String>();
        for ( String c : this.commandsOnMid ) {
            data.commandsOnMid.add(c);
        }
        data.commandsOnEnd = new ArrayList<String>();
        for ( String c : this.commandsOnEnd ) {
            data.commandsOnEnd.add(c);
        }
        data.consoleCommandsOnStart = new ArrayList<String>();
        for ( String c : this.consoleCommandsOnStart ) {
            data.consoleCommandsOnStart.add(c);
        }
        data.consoleCommandsOnMid = new ArrayList<String>();
        for ( String c : this.consoleCommandsOnMid ) {
            data.consoleCommandsOnMid.add(c);
        }
        data.consoleCommandsOnEnd = new ArrayList<String>();
        for ( String c : this.consoleCommandsOnEnd ) {
            data.consoleCommandsOnEnd.add(c);
        }
        data.countdownOnStart = this.countdownOnStart;
        data.countdownOnEnd = this.countdownOnEnd;
        data.runCommandsOnMidSeconds = new ArrayList<Integer>();
        for ( Integer c : this.runCommandsOnMidSeconds ) {
            data.runCommandsOnMidSeconds.add(c);
        }
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
        data.messages = this.messages;

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
