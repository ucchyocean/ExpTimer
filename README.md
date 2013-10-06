ExpTimer
========

<p>
<h2>コマンド</h2>
※ コマンドは "/exptimer"、省略形として、"/et"、"/timer" も使用可能です。<br />
※ パーミッションノードは "exptimer"、OPはデフォルトで使用可能です。<br /><br />
/timer start [seconds] [readySeconds] - 経験値バータイマーを開始する<br />
/timer pause - 経験値バータイマーを一旦停止する<br />
/timer cancel - 経験値バータイマーを終了する(終了時コマンドを実行しない)<br />
/timer end - 経験値バータイマーを終了する(終了時コマンドを実行する)<br />
/timer status - 現在の設定を表示する<br />
/timer list - コンフィグのリストを表示する<br />
/timer reload - config.yml をリロードする<br />
</p>

<p>
<h2>コンフィグ</h2>
seconds - 規定のタイマー秒数<br />
readySeconds - 規定のタイマー開始までの秒数<br />
commandsOnStart - タイマー開始時に実行するコマンド<br />
　　例）commandsOnStart: ['say スタート！！がんばってくださいね！', 'tpa 100 65 100']<br />
commandsOnEnd - タイマー終了時に実行するコマンド<br />
　　例）commandsOnEnd: ['say 終了！！お疲れ様でした！', 'tpa 0 65 0']<br />
countdownOnStart - スタート前のカウントダウンを開始する秒数<br />
　　例えば、「countdownOnStart: 15」とした場合、15秒前からカウントダウンを実行する。<br />
countdownOnEnd - 終了前のカウントダウンを開始する秒数<br />
playSound - カウントダウン中に音を出すかどうか<br />
useExpBar - 経験値バーおよび経験値レベルをタイマーとして使うかどうか<br />
endWithCTTeamDefeat - ColorTeamingのチームが全滅したら、タイマーを終了するかどうか<br />
　　※ ColorTeaming v2.2.4 以上が同時にロードされている必要があります。<br />
endWithCTLeaderDefeat - ColorTeamingのリーダーが全滅したら、タイマーを終了するかどうか<br />
　　※ ColorTeaming v2.2.4 以上が同時にロードされている必要があります。<br />
endWithCTKillTrophy - ColorTeamingのKillTrophyが達成されたら、タイマーを終了するかどうか<br />
　　※ ColorTeaming v2.2.4 以上が同時にロードされている必要があります。<br />
nextConfig - タイマーが終了した後に、次に自動で実行するタスクのコンフィグ名<br />
　　自分自身のコンフィグ名を指定することで、そのコンフィグを永遠にループさせることが可能です。<br />
messageFileName - タイマーのメッセージファイル名。指定例） messageFileName: 'messages_test.yml'<br />
　　未指定の場合は、デフォルトの「messages.yml」が使用されます。<br />
forceEmulateConsoleCommand - コンソールでの実行エミュレートを強制するかどうか<br />
　　未指定の場合は false で、/timer start を実行した人<br />
　　（ゲーム内から実行した場合はそのプレイヤー、コンソールから実行した場合はコンソール）で<br />
　　コマンドが実行エミュレートされます。<br />
　　この設定に true を設定すると、コマンドは全てコンソールから実行されます。<br />
</p>
