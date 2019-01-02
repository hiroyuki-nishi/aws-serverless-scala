package account

import domain.Page
import spray.json._

object AccountViewJsonProtocol extends DefaultJsonProtocol {
  implicit val emailGetOutputJsonFormat =
    jsonFormat(
      Email,
      "email"
    )

  implicit val accountGetOutputJsonFormat =
    jsonFormat(
      Account,
      "person_id",
      "email"
    )

  // TODO
  implicit class Page2Json[E](val page: Page[E]) extends AnyVal {
    def toPageJson(implicit writer: JsonWriter[Seq[E]]) =
      JsObject(
        Page.KeysTotalSize  -> JsNumber(page.totalSize),
        Page.KeysPageNo     -> JsNumber(page.pageNo),
        Page.KeysPageSize   -> JsNumber(page.pageSize),
        Page.KeysLastPageNo -> JsNumber(page.lastPageNo),
//        Page.KeysOrder      -> JsString(page.order),
        Page.KeysData       -> page.data.toJson(writer)
      )
  }
}
