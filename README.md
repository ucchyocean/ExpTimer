ExpTimer
========

Build Status : [![Build Status](https://travis-ci.org/ucchyocean/ExpTimer.svg?branch=master)](https://travis-ci.org/ucchyocean/ExpTimer)

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
commandsOnMid - タイマー実行時に指定した時間で実行するコマンド<br />
commandsOnEnd - タイマー終了時に実行するコマンド<br />
　　例）commandsOnEnd: ['say 終了！！お疲れ様でした！', 'tpa 0 65 0']<br />
consoleCommandsOnStart - タイマー開始時にコンソールから実行するコマンド<br />
consoleCommandsOnMid - タイマー実行時に指定した時間でコンソールから実行するコマンド<br />
consoleCommandsOnEnd - タイマー終了時にコンソールから実行するコマンド<br />
countdownOnStart - スタート前のカウントダウンを開始する秒数<br />
　　例えば、「countdownOnStart: 15」とした場合、15秒前からカウントダウンを実行する。<br />
countdownOnEnd - 終了前のカウントダウンを開始する秒数<br />
runCommandsOnMidSeconds - commandsOnMid や consoleCommandsOnMid を実行する時間<br />
　　例）runCommandsOnMidSeconds: [10,20] # タイマーを開始してから10秒後と20秒後に、commandsOnMid と consoleCommandsOnMid を実行します。<br />
restAlertSeconds - 残り時間アラートを行う時間<br />
　　例）restAlertSeconds: [15,25] # タイマーを開始してから15秒後と25秒後に、「残り ？秒です」のアナウンスを実行します。<br />
playSound - カウントダウン中に音を出すかどうか<br />
useExpBar - 経験値バーおよび経験値レベルをタイマーとして使うかどうか<br />
useSideBar - サイドバーをタイマーとして使うかどうか<br />
useBossBar - ボスMOBの体力バーをタイマーとして使うかどうか（同時にBarAPIが導入されている必要があります。）<br />
endWithCTTeamDefeat - ColorTeamingのチームが全滅したら、タイマーを終了するかどうか<br />
　　※ ColorTeaming v2.3.2 以上が同時にロードされている必要があります。<br />
endWithCTLeaderDefeat - ColorTeamingのリーダーが全滅したら、タイマーを終了するかどうか<br />
　　※ ColorTeaming v2.3.2 以上が同時にロードされている必要があります。<br />
endWithCTKillTrophy - ColorTeamingのKillTrophyが達成されたら、タイマーを終了するかどうか<br />
　　※ ColorTeaming v2.3.2 以上が同時にロードされている必要があります。<br />
endWithTeamPointUnder - ColorTeamingのチームポイントが、指定された数値を下回ったら、タイマーを終了します。<br />
　　例えば、endWithTeamPointUnder: 0 を設定して、赤チームのチームポイントが 2 から 0 になったとすると、「赤チームの勝利です！」と表示してタイマーを終了します。<br />
　　※ ColorTeaming v2.3.2 以上が同時にロードされている必要があります。<br />
endWithTeamPointOver - ColorTeamingのチームポイントが、指定された数値を下回ったら、タイマーを終了します。<br />
　　例えば、endWithTeamPointOver: 30 を設定して、青チームのチームポイントが 26 から 30 になったとすると、「青チームの勝利です！」と表示してタイマーを終了します。<br />
　　※ ColorTeaming v2.3.2 以上が同時にロードされている必要があります。<br />
announceToOnlyTeamMembers - trueを設定すると、残り時間のカウントメッセージや、その音を、ColorTeamingでチーム設定されているプレイヤーにのみ表示するようになります。<br />
　　※ ColorTeaming v2.3.2 以上が同時にロードされている必要があります。<br />
nextConfig - タイマーが終了した後に、次に自動で実行するタスクのコンフィグ名<br />
　　自分自身のコンフィグ名を指定することで、そのコンフィグを永遠にループさせることが可能です。<br />
messageFileName - タイマーのメッセージファイル名。指定例） messageFileName: 'messages_test.yml'<br />
　　未指定の場合は、デフォルトの「messages.yml」が使用されます。<br />
</p>
