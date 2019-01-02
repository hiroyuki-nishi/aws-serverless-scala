# build.sbtにdockerの定義を追加する

```.sbtshell
    maintainer in Docker := "k636362",
    packageName in Docker := "dockerised-akka-http",
    dockerBaseImage := "openjdk:8-jre-alpine",
    dockerExposedPorts := Seq(8080),
    mainClass in assembly := Some("person.Server")
```

# Dockerfileおよびコンテナに追加するファイルを生成します。
`sbt docker:stage`
下記ファイルが作成される
e.g.) application/person/target/docker/stage/Dockerfile

# 実行ファイルを作成する 

```
sbt person/compile
sbt person/assembly 
```

# Dockerイメージを生成します。
`sbt docker:publishLocal`

# Dockerイメージが作成されているか確認する
`docker images`

```text
dockerised-akka-http           0.1.0-SNAPSHOT      4437752ce2eb        9 seconds ago       124MB
```