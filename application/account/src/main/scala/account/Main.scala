package account

import dynamodb.AccountRepositoryOnDynamoDB


object Main extends App {
  import AccountViewJsonProtocol._
  def findAccount(accountRepository: AccountRepository): Unit = {
    (for {
      t <- accountRepository.findAllBy("person_1", 1, 1)
    } yield t) match {
      case Right(r) => println(r.toPageJson.compactPrint)
      case Left(l) => println(l)
    }
  }

  val accountRepository = new AccountRepository with AccountRepositoryOnDynamoDB
  findAccount(accountRepository)
}
