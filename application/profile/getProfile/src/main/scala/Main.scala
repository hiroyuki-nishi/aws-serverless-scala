import com.amazonaws.auth.profile.ProfileCredentialsProvider
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import dynamodb.ProfileRepositoryOnDynamoDB
import profile.{Profile, ProfileCreateRepository, ProfileListRepository}
import s3.ProfileRepositoryOnS3

object Main extends App {
  val profileS3Client = new ProfileCreateRepository with ProfileRepositoryOnS3 {
    val s3Client = AmazonS3ClientBuilder.standard().withCredentials(new ProfileCredentialsProvider()).build()
  }
  val profileDynamoDBClient = new ProfileListRepository with ProfileRepositoryOnDynamoDB
  //  profileDynamoDBClient.findAll().fold(
  //    e => print(e),
  //    list => list.foreach(profileS3Client.put(_).fold(
  //      e => print(e),
  //      print(_) // TODO
  //    )
  //  ))
  profileS3Client.findBy("hoge").fold(
    l => println(l),
    r => println(r)
  )

}


