package profile

import domain.RepositoryError

import scala.util.Try

trait ProfileRepository {
  def put(profile: Profile): Either[RepositoryError, Unit]
}
