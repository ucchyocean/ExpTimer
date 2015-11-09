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
    private String name;

    /** タイマー時間（秒） */
    private int seconds;

    /** 開始前タイマー時間（秒） */
    private int readySeconds;

    /** タイマースタート時に実行するコマンド */
    private List<String> commandsOnStart;

    /** 設定した時間に実行するコマンド */
    private List<String> commandsOnMid;

    /** タイマー終了時に実行するコマンド */
    private List<String> commandsOnEnd;

    /** タイマースタート時にコンソールで実行するコマンド */
    private List<String> consoleCommandsOnStart;

    /** 設定した時間にコンソールで実行するコマンド */
    private List<String> consoleCommandsOnMid;

    /** タイマー終了時にコンソールで実行するコマンド */
    private List<String> consoleCommandsOnEnd;

    /** タイマースタート前のカウントダウン時間（秒） */
    private int countdownOnStart;

    /** タイマー終了前のカウントダウン時間（秒） */
    private int countdownOnEnd;

    /** タイマー途中でコマンドを実行する時間 */
    private List<Integer> runCommandsOnMidSeconds;

    /** 残り時間アラートの時間（秒） */
    private List<Integer> restAlertSeconds;

    /** カウントダウン中に音を出すかどうか */
    private boolean playSound;

    /** カウントダウン時の音の種類（隠しオプション） */
    private String playSoundCountdown;

    /** スタート時・終了時の音の種類（隠しオプション） */
    private String playSoundStartEnd;

    /** 経験値バーをタイマー表示として使用するかどうか */
    private boolean useExpBar;

    /** サイドバーをタイマー表示として使用するかどうか */
    private boolean useSideBar;

    /** ボスMOBの体力バーをタイマー表示として使用するかどうか */
    private boolean useBossBar;

    /** タイトル部分をタイマー表示として使用するかどうか */
    private boolean useTitle;

    /** メッセージファイルのファイル名指定 */
    private String messageFileName;

    /** 次に自動で実行するタスクのコンフィグ名 */
    private String nextConfig;

    /** ColorTeamingのチームが全滅したら、タイマーを終了するかどうか */
    private boolean endWithCTTeamDefeat;

    /** ColorTeamingのリーダーが全滅したら、タイマーを終了するかどうか */
    private boolean endWithCTLeaderDefeat;

    /** ColorTeamingのKillTrophyが達成されたら、タイマーを終了するかどうか */
    private boolean endWithCTKillTrophy;

    /** ColorTeamingのチームポイントが、基準値を下回ったら、タイマーを終了する */
    private int endWithTeamPointUnder;

    /** ColorTeamingのチームポイントが、基準値を上回ったら、タイマーを終了する */
    private int endWithTeamPointOver;

    /** ColorTeamingで設定されたチームメンバーのみに、アナウンスメッセージを流すかどうか */
    private boolean announceToOnlyTeamMembers;

    /** メッセージファイル */
    private ExpTimerMessages messages;

    /**
     * コンストラクタ（外部からのアクセス不可）
     */
    private ExpTimerConfigData() {

        // 初期値の挿入
        seconds = 600;
        readySeconds = 10;
        commandsOnStart = new ArrayList<String>();
        commandsOnMid = new ArrayList<String>();
        commandsOnEnd = new ArrayList<String>();
        consoleCommandsOnStart = new ArrayList<String>();
        consoleCommandsOnMid = new ArrayList<String>();
        consoleCommandsOnEnd = new ArrayList<String>();
        countdownOnStart = 3;
        countdownOnEnd = 5;
        runCommandsOnMidSeconds = new ArrayList<Integer>();
        restAlertSeconds = new ArrayList<Integer>();
        restAlertSeconds.add(60);
        restAlertSeconds.add(180);
        restAlertSeconds.add(300);
        playSound = true;
        playSoundCountdown = "NOTE_STICKS";
        playSoundStartEnd = "NOTE_PLING";
        useExpBar = true;
        useSideBar = false;
        useBossBar = false;
        useTitle = false;
        endWithCTTeamDefeat = false;
        endWithCTLeaderDefeat = false;
        endWithCTKillTrophy = false;
        endWithTeamPointUnder = -99999;
        endWithTeamPointOver = 99999;
        announceToOnlyTeamMembers = false;
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

        if ( section != null && defaults == null ) {
            data.seconds = section.getInt("seconds", data.seconds);
            data.readySeconds = section.getInt("readySeconds", data.readySeconds);
            data.commandsOnStart = section.getStringList("commandsOnStart");
            data.commandsOnMid = section.getStringList("commandsOnMid");
            data.commandsOnEnd = section.getStringList("commandsOnEnd");
            data.consoleCommandsOnStart = section.getStringList("consoleCommandsOnStart");
            data.consoleCommandsOnMid = section.getStringList("consoleCommandsOnMid");
            data.consoleCommandsOnEnd = section.getStringList("consoleCommandsOnEnd");
            data.countdownOnStart = section.getInt("countdownOnStart", data.countdownOnStart);
            data.countdownOnEnd = section.getInt("countdownOnEnd", data.countdownOnEnd);
            data.runCommandsOnMidSeconds = section.getIntegerList("runCommandsOnMidSeconds");
            if ( section.contains("restAlertSeconds") ) {
                data.restAlertSeconds = section.getIntegerList("restAlertSeconds");
            }
            data.playSound = section.getBoolean("playSound", data.playSound);
            data.playSoundCountdown = section.getString("playSoundCountdown", data.playSoundCountdown);
            data.playSoundStartEnd = section.getString("playSoundStartEnd", data.playSoundStartEnd);
            data.useExpBar = section.getBoolean("useExpBar", data.useExpBar);
            data.useSideBar = section.getBoolean("useSideBar", data.useSideBar);
            data.useBossBar = section.getBoolean("useBossBar", data.useBossBar);
            data.useTitle = section.getBoolean("useTitle", data.useTitle);
            data.messageFileName = section.getString("messageFileName");
            data.nextConfig = section.getString("nextConfig");
            data.endWithCTTeamDefeat =
                    section.getBoolean("endWithCTTeamDefeat", data.endWithCTTeamDefeat);
            data.endWithCTLeaderDefeat =
                    section.getBoolean("endWithCTLeaderDefeat", data.endWithCTLeaderDefeat);
            data.endWithCTKillTrophy =
                    section.getBoolean("endWithCTKillTrophy", data.endWithCTKillTrophy);
            data.endWithTeamPointUnder =
                    section.getInt("endWithTeamPointUnder", data.endWithTeamPointUnder);
            data.endWithTeamPointOver =
                    section.getInt("endWithTeamPointOver", data.endWithTeamPointOver);
            data.announceToOnlyTeamMembers =
                    section.getBoolean("announceToOnlyTeamMembers", data.announceToOnlyTeamMembers);

        } else if ( section != null ) {

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
            data.useBossBar = section.getBoolean("useBossBar", defaults.useBossBar);
            data.useTitle = section.getBoolean("useTitle", defaults.useTitle);
            data.endWithCTTeamDefeat =
                section.getBoolean("endWithCTTeamDefeat", defaults.endWithCTTeamDefeat);
            data.endWithCTLeaderDefeat =
                section.getBoolean("endWithCTLeaderDefeat", defaults.endWithCTLeaderDefeat);
            data.endWithCTKillTrophy =
                section.getBoolean("endWithCTKillTrophy", defaults.endWithCTKillTrophy);
            data.endWithTeamPointUnder =
                section.getInt("endWithTeamPointUnder", defaults.endWithTeamPointUnder);
            data.endWithTeamPointOver =
                section.getInt("endWithTeamPointOver", defaults.endWithTeamPointOver);
            data.announceToOnlyTeamMembers =
                section.getBoolean(
                    "announceToOnlyTeamMembers", defaults.announceToOnlyTeamMembers);

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
    public ExpTimerConfigData clone() {

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
        data.useBossBar = this.useBossBar;
        data.useTitle = this.useTitle;
        data.messageFileName = this.messageFileName;
        data.nextConfig = this.nextConfig;
        data.endWithCTTeamDefeat = this.endWithCTTeamDefeat;
        data.endWithCTLeaderDefeat = this.endWithCTLeaderDefeat;
        data.endWithCTKillTrophy = this.endWithCTKillTrophy;
        data.endWithTeamPointUnder = this.endWithTeamPointUnder;
        data.endWithTeamPointOver = this.endWithTeamPointOver;
        data.announceToOnlyTeamMembers = this.announceToOnlyTeamMembers;
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

    // ===== 以下、自動生成されたgetterとsetter

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public int getReadySeconds() {
        return readySeconds;
    }

    public void setReadySeconds(int readySeconds) {
        this.readySeconds = readySeconds;
    }

    public List<String> getCommandsOnStart() {
        return commandsOnStart;
    }

    public void setCommandsOnStart(List<String> commandsOnStart) {
        this.commandsOnStart = commandsOnStart;
    }

    public List<String> getCommandsOnMid() {
        return commandsOnMid;
    }

    public void setCommandsOnMid(List<String> commandsOnMid) {
        this.commandsOnMid = commandsOnMid;
    }

    public List<String> getCommandsOnEnd() {
        return commandsOnEnd;
    }

    public void setCommandsOnEnd(List<String> commandsOnEnd) {
        this.commandsOnEnd = commandsOnEnd;
    }

    public List<String> getConsoleCommandsOnStart() {
        return consoleCommandsOnStart;
    }

    public void setConsoleCommandsOnStart(List<String> consoleCommandsOnStart) {
        this.consoleCommandsOnStart = consoleCommandsOnStart;
    }

    public List<String> getConsoleCommandsOnMid() {
        return consoleCommandsOnMid;
    }

    public void setConsoleCommandsOnMid(List<String> consoleCommandsOnMid) {
        this.consoleCommandsOnMid = consoleCommandsOnMid;
    }

    public List<String> getConsoleCommandsOnEnd() {
        return consoleCommandsOnEnd;
    }

    public void setConsoleCommandsOnEnd(List<String> consoleCommandsOnEnd) {
        this.consoleCommandsOnEnd = consoleCommandsOnEnd;
    }

    public int getCountdownOnStart() {
        return countdownOnStart;
    }

    public void setCountdownOnStart(int countdownOnStart) {
        this.countdownOnStart = countdownOnStart;
    }

    public int getCountdownOnEnd() {
        return countdownOnEnd;
    }

    public void setCountdownOnEnd(int countdownOnEnd) {
        this.countdownOnEnd = countdownOnEnd;
    }

    public List<Integer> getRunCommandsOnMidSeconds() {
        return runCommandsOnMidSeconds;
    }

    public void setRunCommandsOnMidSeconds(List<Integer> runCommandsOnMidSeconds) {
        this.runCommandsOnMidSeconds = runCommandsOnMidSeconds;
    }

    public List<Integer> getRestAlertSeconds() {
        return restAlertSeconds;
    }

    public void setRestAlertSeconds(List<Integer> restAlertSeconds) {
        this.restAlertSeconds = restAlertSeconds;
    }

    public boolean isPlaySound() {
        return playSound;
    }

    public void setPlaySound(boolean playSound) {
        this.playSound = playSound;
    }

    public String getPlaySoundCountdown() {
        return playSoundCountdown;
    }

    public void setPlaySoundCountdown(String playSoundCountdown) {
        this.playSoundCountdown = playSoundCountdown;
    }

    public String getPlaySoundStartEnd() {
        return playSoundStartEnd;
    }

    public void setPlaySoundStartEnd(String playSoundStartEnd) {
        this.playSoundStartEnd = playSoundStartEnd;
    }

    public boolean isUseExpBar() {
        return useExpBar;
    }

    public void setUseExpBar(boolean useExpBar) {
        this.useExpBar = useExpBar;
    }

    public boolean isUseSideBar() {
        return useSideBar;
    }

    public void setUseSideBar(boolean useSideBar) {
        this.useSideBar = useSideBar;
    }

    public boolean isUseBossBar() {
        return useBossBar;
    }

    public void setUseBossBar(boolean useBossBar) {
        this.useBossBar = useBossBar;
    }

    public boolean isUseTitle() {
        return useTitle;
    }

    public void setUseTitle(boolean useTitle) {
        this.useTitle = useTitle;
    }

    public String getMessageFileName() {
        return messageFileName;
    }

    public void setMessageFileName(String messageFileName) {
        this.messageFileName = messageFileName;
    }

    public String getNextConfig() {
        return nextConfig;
    }

    public void setNextConfig(String nextConfig) {
        this.nextConfig = nextConfig;
    }

    public boolean isEndWithCTTeamDefeat() {
        return endWithCTTeamDefeat;
    }

    public void setEndWithCTTeamDefeat(boolean endWithCTTeamDefeat) {
        this.endWithCTTeamDefeat = endWithCTTeamDefeat;
    }

    public boolean isEndWithCTLeaderDefeat() {
        return endWithCTLeaderDefeat;
    }

    public void setEndWithCTLeaderDefeat(boolean endWithCTLeaderDefeat) {
        this.endWithCTLeaderDefeat = endWithCTLeaderDefeat;
    }

    public boolean isEndWithCTKillTrophy() {
        return endWithCTKillTrophy;
    }

    public void setEndWithCTKillTrophy(boolean endWithCTKillTrophy) {
        this.endWithCTKillTrophy = endWithCTKillTrophy;
    }

    public int getEndWithTeamPointUnder() {
        return endWithTeamPointUnder;
    }

    public void setEndWithTeamPointUnder(int endWithTeamPointUnder) {
        this.endWithTeamPointUnder = endWithTeamPointUnder;
    }

    public int getEndWithTeamPointOver() {
        return endWithTeamPointOver;
    }

    public void setEndWithTeamPointOver(int endWithTeamPointOver) {
        this.endWithTeamPointOver = endWithTeamPointOver;
    }

    public boolean isAnnounceToOnlyTeamMembers() {
        return announceToOnlyTeamMembers;
    }

    public void setAnnounceToOnlyTeamMembers(boolean announceToOnlyTeamMembers) {
        this.announceToOnlyTeamMembers = announceToOnlyTeamMembers;
    }

    public ExpTimerMessages getMessages() {
        return messages;
    }

    public void setMessages(ExpTimerMessages messages) {
        this.messages = messages;
    }
}
