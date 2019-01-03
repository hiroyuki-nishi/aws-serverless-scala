package object domain {
  case class RepositoryError(error: Throwable)

  trait Page[E] {
    val totalSize: Int
    val pageNo: Int
    val pageSize: Int
    val lastPageNo: Int
//    val order: String
    val data: Seq[E]

    def map[B](f: E => B): Page[B] = {
      val that = this
      new Page[B] {
        override val totalSize  = that.totalSize
        override val pageNo     = that.pageNo
        override val pageSize   = that.pageSize
        override val lastPageNo = that.lastPageNo
//        override val order      = that.order
        override val data       = that.data map f
      }
    }

    def isEmpty: Boolean  = data.isEmpty
    def nonEmpty: Boolean = data.nonEmpty
  }

  object Page {
    val KeysTotalSize  = "total_size"
    val KeysPageNo     = "page_no"
    val KeysPageSize   = "page_size"
    val KeysLastPageNo = "last_page_no"
//    val KeysOrder      = "order"
    val KeysData       = "data"
  }
}
