package shine.st.blog.dao

import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.{Document, FindObservable, Observable}
import shine.st.blog.protocol.document.Categories
import shine.st.blog.utils.MongoUtils.ImplicitObservable
import spray.json._

/**
  * Created by shinest on 19/01/2017.
  */
trait CategoriesCollectionDao extends CollectionDao {
  override val collectionName: String = "categories"

  type T = Categories

  implicit class CategoriesObservable(val observable: Observable[Document]) extends ImplicitObservable[Document] {
    type R = T
    override val converter: (Document) => R = (doc) => {
      val jsonSource = doc.toJson
      val jsonAst = jsonSource.parseJson
      jsonAst.convertTo[R]
    }
  }

  def findByAncestors(someAncestor: String) = {
    find(equal("ancestors", someAncestor)).getResults
  }

  override def convert(observable: FindObservable[Document]) = observable.getResults

  //  FIXME: duplicate toJson
  override def toJson(data: T): JsValue = data.toJson
}

object CategoriesCollectionDao extends CategoriesCollectionDao
