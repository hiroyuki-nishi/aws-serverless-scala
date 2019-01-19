package profile

import domain.RepositoryError

trait ProfileListRepository {
  def findAll(): Either[RepositoryError, Seq[Profile]]
}
