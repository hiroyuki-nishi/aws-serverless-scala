# Dockerfileおよびコンテナに追加するファイルを生成します。
sbt docker:stage

# Dockerイメージを生成します。
sbt docker:publishLocal