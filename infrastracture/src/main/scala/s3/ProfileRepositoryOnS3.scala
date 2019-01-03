package s3

import java.nio.charset.StandardCharsets
import spray.json._

import domain.RepositoryError
import profile.Profile

import scala.util.Try

trait ProfileRepositoryOnS3 extends S3Wrapper {
  override protected lazy val bucketName = "sbt-serverless"
  override protected lazy val regionName = "us-east-1"

  private def generateKey(id: String, name: String) =
    s"${id}/${name}.json"

//  def findBy(
//      companyId: CompanyId,
//      deviceId: AssetManagementDeviceId): Either[RepositoryError, Option[InstalledApplications]] =
//    (for {
//      maybeBytes <- getObjectContent(generateKey(companyId, deviceId))
//      app <- Try(maybeBytes.map { bytes =>
//        import JsonProtocol._
//        val jsObject = JsonParser(bytes).asJsObject
//        val json     = jsObject.convertTo[UpdatedAndroidInstalledApplicationsJson]
//        InstalledApplications(
//          companyId = CompanyId(json.companyId),
//          deviceId = AssetManagementDeviceId(json.deviceId),
//          collectedAt = json.collectedAt,
//          applications = json.applications.map(
//            app =>
//              InstalledApplication(applicationIdentifier = app.applicationIdentifier,
//                                   applicationName = app.applicationName,
//                                   installType = app.installType))
//        )
//      })
//    } yield app).fold(
//      t => Left(RepositorySystemError(t)),
//      Right(_)
//    )
//
  def put(profile: Profile): Either[RepositoryError, Unit] =
    Try {
      import ProfileJsonProtocol._
      val bytes = ProfileJson(
        id = profile.id,
        name = profile.name
      ).toJson.compactPrint.getBytes(StandardCharsets.UTF_8)

      putObject(
        key = generateKey(profile.id, profile.name),
        bytes = bytes
      )
    }.fold(
      t => Left(RepositoryError(t)),
      _ => Right()
    )
}

