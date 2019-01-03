package s3

import java.io.{ByteArrayInputStream, InputStream}
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.util.Date

import com.amazonaws.auth.profile.ProfileCredentialsProvider
import com.amazonaws.services.s3.{AmazonS3, AmazonS3Client, AmazonS3ClientBuilder}
import com.amazonaws.services.s3.model._
import domain.RepositoryError

import scala.util.{Failure, Success, Try}

trait S3Wrapper {
  protected val s3Client: AmazonS3
  protected val bucketName: String
  protected val regionName: String
  protected val DefaultContentType = "application/json"

  protected def readAsByteArray(in: InputStream): Array[Byte] =
    Stream.continually(in.read).takeWhile(_ != -1).map(_.toByte).toArray

  private def getObjectInternal(key: String): S3Object =
    s3Client.getObject(bucketName, key)

  private def loanTry[A](f: => A): Try[A] =
    Try(f).fold(
      cause => Failure(new RuntimeException(s"$bucketName. ${cause.getMessage}", cause)),
      Success(_)
    )

  def getObject(key: String): Try[Option[S3Object]] = loanTry {
    try {
      Some(getObjectInternal(key))
    } catch {
      case e: com.amazonaws.services.s3.model.AmazonS3Exception
          if e.getMessage.startsWith("The specified key does not exist.") =>
        None
    }
  }

  def getObjectContent(key: String): Try[Option[Array[Byte]]] =
    for {
      opt <- getObject(key)
      b   <- loanTry(opt.map(o => readAsByteArray(o.getObjectContent)))
    } yield b

  def getObjectContentBytes(key: String): Try[Array[Byte]] = loanTry {
    readAsByteArray(getObjectInternal(key).getObjectContent)
  }

  def generatePreSignedUrl(key: String, fileName: String, expiration: Date): Try[String] =
    for {
      name <- loanTry(URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString))
      request <- loanTry {
        val options =
          new ResponseHeaderOverrides()
            .withContentDisposition(s"attachment;filename=$name;filename*=UTF-8''$name")
        new GeneratePresignedUrlRequest(bucketName, key)
          .withExpiration(expiration)
          .withResponseHeaders(options)
      }
      url <- loanTry(s3Client.generatePresignedUrl(request).toString)
    } yield url

  def putObject(key: String,
                bytes: Array[Byte],
                contentType: String = DefaultContentType): Try[PutObjectResult] = loanTry {
    val metadata = new ObjectMetadata()
    metadata.setContentLength(bytes.length)
    metadata.setContentType(contentType)
    s3Client.putObject(bucketName, key, new ByteArrayInputStream(bytes), metadata)
  }

  def deleteObject(key: String): Try[Unit] = loanTry {
    s3Client.deleteObject(bucketName, key)
  }
}

