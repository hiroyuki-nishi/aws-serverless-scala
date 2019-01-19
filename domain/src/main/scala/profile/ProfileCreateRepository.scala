package profile

import domain.RepositoryError

import scala.util.Try

trait ProfileCreateRepository {
  def findBy(companyName: String): Either[RepositoryError, Option[Profile]]
  def put(profile: Profile): Either[RepositoryError, Unit]
}
