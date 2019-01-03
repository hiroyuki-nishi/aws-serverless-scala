package s3

import spray.json._

case class ProfileJson(id: String,
                       name: String)

object ProfileJsonProtocol extends DefaultJsonProtocol {
  implicit val profileJosonFormat =
    jsonFormat(ProfileJson,
      "id",
      "name"
    )
}

