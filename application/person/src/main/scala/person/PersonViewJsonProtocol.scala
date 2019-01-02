package person

import domain.Page
import spray.json._

object PersonViewJsonProtocol extends DefaultJsonProtocol {
  implicit val personGetOutputJsonFormat =
    jsonFormat(
      Person,
      "id",
      "name"
    )

  implicit class Page2Json[E](val page: Page[E]) extends AnyVal {
    def toPageJson(implicit writer: JsonWriter[Seq[E]]) =
      JsObject(
        Page.KeysTotalSize  -> JsNumber(page.totalSize),
        Page.KeysPageNo     -> JsNumber(page.pageNo),
        Page.KeysPageSize   -> JsNumber(page.pageSize),
        Page.KeysLastPageNo -> JsNumber(page.lastPageNo),
        Page.KeysData       -> page.data.toJson(writer)
      )
  }
}
