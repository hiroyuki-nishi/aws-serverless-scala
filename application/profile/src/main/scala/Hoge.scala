import com.amazonaws.auth.profile.ProfileCredentialsProvider
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import profile.{Profile, ProfileRepository}
import s3.ProfileRepositoryOnS3

object Hoge extends App {
  val s3 = new ProfileRepository with ProfileRepositoryOnS3 {
    val s3Client = AmazonS3ClientBuilder.standard().withCredentials(new ProfileCredentialsProvider()).build()
  }
  print(s3.put(Profile("1", "hoge")))
}
