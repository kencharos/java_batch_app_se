jBatch on Java SE
-----------------

## 概要

jBatch の参照実装をカスタマイズして、Java EE 版と同じjBatchのソースをJava SE 環境で動かすサンプル。

以下の内容を実現する。

- Maven の dependency のみで依存性を管理する
- JPA を使えるようにする
- CDI を使用できるようにする
- CDI の @Namedの値を job xml の ref属性に書けるようにする 
- Batchlet における @Transactional によるトランザクション制御を行う
- Chunk におけるトランザクション制御を行う

## 参考資料

http://yoshio3.com/2014/02/18/jbatchjsr-352-on-java-se-%E7%92%B0%E5%A2%83/

## 作成物
+ EMHolder - スレッドローカルでEntitymanger を保持するもの。Dependentスコープのジョブクラスやインターセプターで同一のEM を取得するために必要
+ EMProducer - EntityManger のインジェクションを提供するクラス。 EMHolder と連携する。
+ SeTransactionInterceptor - JTAを使っていないので、@Transactional アノテーションをみてトランザクション制御するインターセプタを自作

## 拡張点

META-INF/services にて、jBatch RI への拡張点を指定。
このあたりは、 jBatch RI に限定した実装なので、他のjBatch 実装では一切役に立たない。

具体的には、以下
+ CONTAINER_ARTIFACT_FACTORY_SERVICE - CDIでジョブステップの実装クラスを取得するクラスなのだが、
　RI の実装では、実装クラスを1回取得するたびにCDIコンテナを初期化していて、そのせいか 2度目のバッチ実行ができないため、
  独自実装を追加。
+ TRANSACTION_SERVICE - Non EE 環境では、チャンクのトランザクション制御は行わない仕様だったので、
  JPA のトランザクションと連携するような仕組みを追加。

## その他

jBatch 実行環境は、 Executor で実行されているらしく、 mainメソッドが終了しても、プログラムは終了しない。
Executor を止める API は無いっぽいので、 Ctrl+C で止めるしかない。
ただし、 1ジョブ実行ごとに JVM を起動しなくてもよいということなので、Java SE 版でも効率面では良いかも。
適当に ジョブ実行を行うインターフェースを用意すれば、アプリケーションサーバ不用でそこそこ実用的な、
バッチサーバが作れそうではある。

が、そこまでやるなら、素直に paraya micro か、 spring batch on Spring Boot でいいんじゃないですかね。