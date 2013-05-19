ExpTimer 
========

<p>
<h2>コマンド</h2>
/et start [seconds] [readySeconds] - 経験値バータイマーを開始する<br />
/et pause - 経験値バータイマーを一旦停止する<br />
/et end - 経験値バータイマーを終了する<br />
/et status - 現在の設定を参照する<br />
/et reload - config.yml をリロードする<br />
</p>

<p>
<h2>コンフィグ</h2>
defaultSeconds - 規定のタイマー秒数<br />
defaultReadySeconds - 規定のタイマー開始までの秒数<br />
commandsOnStart - タイマー開始時に実行するコマンド（スラッシュ不要なので注意）<br />
　　例）commandsOnStart: ['say スタート！！がんばってくださいね！', 'tpa 100 65 100']<br />
commandsOnEnd - タイマー終了時に実行するコマンド（スラッシュ不要なので注意）<br />
　　例）commandsOnEnd: ['say 終了！！お疲れ様でした！', 'tpa 0 65 0']<br />
</p>

