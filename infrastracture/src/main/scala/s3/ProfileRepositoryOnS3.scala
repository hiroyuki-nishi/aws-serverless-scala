package s3

import java.nio.charset.StandardCharsets
import spray.json._

import domain.RepositoryError
import profile.Profile

import scala.util.Try

trait ProfileRepositoryOnS3 extends S3Wrapper {
  override protected lazy val bucketName = "sbt-serverless"
  override protected lazy val regionName = "ap-northeast-1"

  private def generateKey(companyId: String, id: String) =
    s"${companyId}/${id}.json"

  // TODO Company
  def findBy(companyName: String): Either[RepositoryError, Option[Profile]] =
    (for {
      maybeBytes <- getObjectContent("1.json")
//      maybeBytes <- getObjectContent(companyName)
      company <- Try(maybeBytes.map { bytes =>
        import ProfileJsonProtocol._
        val jsObject = JsonParser(bytes).asJsObject
        val json = jsObject.convertTo[Profile]
        Profile(json.companyId, json.id, json.identifer, json.updated)
      })
    } yield company).fold(
      t => Left(RepositoryError(t)),
      Right(_)
    )

  def put(profile: Profile): Either[RepositoryError, Unit] =
    Try {
      import ProfileJsonProtocol._
      val bytes = Profile(
        profile.companyId,
        profile.id,
        profile.identifer,
        profile.updated
      ).toJson.compactPrint.getBytes(StandardCharsets.UTF_8)

      putObject(
        key = generateKey(profile.companyId, profile.id),
        bytes = bytes
      )
    }.fold(
      t => Left(RepositoryError(t)),
      _ => Right()
    )
}

