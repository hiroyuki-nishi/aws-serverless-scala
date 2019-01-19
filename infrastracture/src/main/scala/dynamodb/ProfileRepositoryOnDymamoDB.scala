package dynamodb

import com.amazonaws.services.dynamodbv2.document._
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec
import com.amazonaws.services.dynamodbv2.model.{AttributeValue, QueryRequest, Select}
import domain.RepositoryError
import profile.Profile

import scala.collection.JavaConverters._
import scala.util.Try


trait ProfileRepositoryOnDynamoDB extends DynamoDBWrapper {
  lazy override val tableName  = "profiles"
  lazy override val regionName = "ap-northeast-1"
  val AttrId         = "id"
  val AttrName       = "name"

//  private def convert2Entity(list: List[Map[String, AttributeValue]]) = {
//    list.map(x => Profile("1", "hoge"))
//  }

  def findAll(): Either[RepositoryError, Seq[Profile]] = {
//    scan[Profile](convert2Entity)
    scan[Profile].fold(
      l => Left(l),
      r => Right(r.map(x => Profile(
        x.get("company_id").getS(),
        x.get("id").getS(),
        x.get("identifier").getS(),
        x.get("updated").getS())
      ))
    )
  }
}

