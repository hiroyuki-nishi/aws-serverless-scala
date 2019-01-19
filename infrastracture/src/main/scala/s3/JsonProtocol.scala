package s3

import profile.Profile
import spray.json._


object ProfileJsonProtocol extends DefaultJsonProtocol {
  implicit val profileJosonFormat =
    jsonFormat(Profile,
      "company_id",
      "id",
      "identifer",
      "updated"
    )
}

