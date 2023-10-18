# TogaGem #

[![Java CI with Maven](https://github.com/olyutorskii/TogaGem/actions/workflows/maven.yml/badge.svg)](https://github.com/olyutorskii/TogaGem/actions/workflows/maven.yml)
[![CodeQL](https://github.com/olyutorskii/TogaGem/actions/workflows/codeql-analysis.yml/badge.svg)](https://github.com/olyutorskii/TogaGem/actions/workflows/codeql-analysis.yml)
-----------------------------------------------------------------------


## What is TogaGem ? ##

* **TogaGem**は、3D動画制作ツール、MikuMikuDance(MMD)で用いられる
各種データファイルを読み書きするためのJavaライブラリです。

* MikuMikuDance was used by a community of Japanese speakers,
so much of TogaGem's documentation and comments contain Japanese word.
We plan to provide information in English at some point.

* TogaGemは、MikuTogaプロジェクトの派生ライブラリとして、
TogaParserライブラリの1.103.2版を引き継ぐ形で誕生しました。

* 現時点で読み込みが可能なファイルは、
  - PMDモデルファイル(*.pmd)
  - VMDモーションファイル(*.vmd)
の二種類です。PMXモデルファイルは読み込めません。

* TogaGemは2023年10月頃まで [OSDN][OSDN](旧称 SourceForge.jp)
でホスティングされていました。
OSDNの可用性に関する問題が長期化しているため、GitHubへと移転してきました。

* MikuTogaプロジェクトは、MMDによる3Dアニメーション制作を支援するプログラムの
整備のために発足した、オープンソースプロジェクトです。


## ビルド方法 ##

* TogaGemはビルドに際して [Maven 3.3.9+](https://maven.apache.org/)
と JDK 1.8+ を要求します。

* TogaGemは他のライブラリに依存していません。

* Mavenを使わずとも `src/main/java/` 配下のソースツリーをコンパイルすることで
ライブラリを構成することが可能です。


## ライセンス ##

* TogaGem独自のソフトウェア資産には [The MIT License][MIT] が適用されます.


## プロジェクト創設者 ##

* 2010年に [Olyutorskii](https://github.com/olyutorskii) によってプロジェクトが発足しました。


## 謝辞 ##

このライブラリを制作するにあたって、以下の情報を参考にしました。


- 通りすがりの記憶 (T.Tetosuki氏ブログ)
  - 「MMD のモデルデータ(PMD)形式　めも　(まとめ)」
  http://blog.goo.ne.jp/torisu_tetosuki/e/209ad341d3ece2b1b4df24abf619d6e4
  - 「MMDのモーションデータ(VMD)形式　めも」
  http://blog.goo.ne.jp/torisu_tetosuki/e/bc9f1c4d597341b394bd02b64597499d


- 針金のブログ VMDメモ
  http://harigane.at.webry.info/201103/article_1.html


- PMDエディタver0.0.6.3(極北P制作)
  同梱readme.txt


[OSDN]: https://ja.osdn.net
[MIT]: https://opensource.org/licenses/MIT


--- EOF ---
