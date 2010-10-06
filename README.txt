[UTF-8 Japanese]

                              T o g a G e m
                                  Readme

                                              Copyright(c) 2010 olyutorskii


=== TogaGemとは ===

TogaGemは、3D動画制作ツール、MikuMikuDance(MMD)で用いられる
各種データファイルを読み書きするためのJavaライブラリです。
TogaGemは、MikuTogaプロジェクトの派生ライブラリとして、
TogaParserライブラリの1.103.2版を引き継ぐ形で誕生しました。

MikuTogaプロジェクトは、MMDによる3Dアニメーション制作を支援するプログラムの
整備のために発足した、オープンソースプロジェクトです。

現時点では、PMDモデルファイル(*.pmd)の読み書きが可能です。

※ MMD開発者の樋口M氏は、MikuTogaの開発活動に一切関与していません。
　 TogaGemに関する問い合わせをを樋口M氏へ投げかけないように！約束だよ！


=== 実行環境 ===

 - TogaGemはJava言語(JLS3)で記述されたプログラムです。
 - TogaGemはJRE1.6に準拠したJava実行環境で利用できるように作られています。
   原則として、JRE1.6に準拠した実行系であれば、プラットフォームを選びません。


=== アーカイブ管理体制 ===

  このアーカイブは、UTF-8による開発環境を前提として構成されています。
  このアーカイブの原本となる開発資産は、
      http://hg.sourceforge.jp/view/mikutoga/TogaGem
  を上位に持つMercurialリポジトリで管理されています。
  アーカイブの代わりにMercurialを通じての開発資産へのアクセスが可能です。
  # hg clone http://hg.sourceforge.jp/view/mikutoga/TogaGem


=== 開発プロジェクト運営元 ===

  http://sourceforge.jp/projects/mikutoga/ まで。


=== ディレクトリ内訳構成 ===

基本的にはMaven2のmaven-archetype-quickstart構成に準じます。

./README.txt
    あなたが今見てるこれ。

./CHANGELOG.txt
    変更履歴。

./LICENSE.txt
    ライセンスに関して。

./pom.xml
    Maven2用プロジェクト構成定義ファイル。

./src/main/java/
    Javaのソースコード。

./src/test/java/
    JUnit 4.* 用のユニットテストコード。

./src/test/java/sample/
    パーサ利用のサンプルプログラム。

./src/main/config/
    各種ビルド・構成管理に必要なファイル群。

./src/main/config/checks.xml
    Checkstyle用configファイル。

./src/main/config/pmdrules.xml
    PMD用ルール定義ファイル。


=== 謝辞 ===

このライブラリを制作するにあたって、以下の情報を参考にしました。


通りすがりの記憶 (T.Tetosuki氏ブログ)
「MMD のモデルデータ(PMD)形式　めも　(まとめ)」
http://blog.goo.ne.jp/torisu_tetosuki/e/209ad341d3ece2b1b4df24abf619d6e4


PMDエディタver0.0.6.3(極北P氏制作)
同梱readme.txt


--- EOF ---
