package io.stellar

import org.apache.iceberg.Table
import org.apache.iceberg.catalog.{Catalog, Namespace, SupportsNamespaces, TableIdentifier}

import java.util
import scala.collection.mutable
import scala.jdk.CollectionConverters.{MapHasAsJava, MapHasAsScala, SeqHasAsJava, SetHasAsScala}

case class NamespaceContent(id: Namespace, tables: mutable.Map[String, Table], props: mutable.Map[String, String])
class MemoryCatalog(override val name: String) extends Catalog with SupportsNamespaces {
  private val namespaces: mutable.Map[Namespace, NamespaceContent] = mutable.Map.empty

  override def listTables(namespace: Namespace): util.List[TableIdentifier] =
    namespaces(namespace).tables.values.map(t => TableIdentifier.of(namespace, t.name())).toList.asJava

  override def dropTable(identifier: TableIdentifier, purge: Boolean): Boolean = {
    namespaces.get(identifier.namespace()) match {
      case Some(c) => {
        c.tables.remove(identifier.name()).isDefined
      }
      case None => {
        false
      }
    }
  }

  override def renameTable(from: TableIdentifier, to: TableIdentifier): Unit = {
    val table = namespaces(from.namespace()).tables.remove(from.name()).get
    namespaces(to.namespace()).tables += (to.name() -> table)
  }

  override def loadTable(identifier: TableIdentifier): Table = namespaces(identifier.namespace())
    .tables(identifier.name())

  override def createNamespace(namespace: Namespace, metadata: util.Map[String, String]): Unit = {
    namespaces += (namespace -> NamespaceContent(namespace, mutable.Map.empty, metadata.asScala))
  }

  override def listNamespaces(namespace: Namespace): util.List[Namespace] = {
    namespaces.keys.filter(MemoryCatalog.isChild(namespace, _)).toList.asJava
  }

  override def loadNamespaceMetadata(namespace: Namespace): util.Map[String, String] = {
    namespaces(namespace).props.asJava
  }

  override def dropNamespace(namespace: Namespace): Boolean = {
    namespaces.remove(namespace).isDefined
  }

  override def setProperties(namespace: Namespace, properties: util.Map[String, String]): Boolean = {
    namespaces(namespace).props ++= properties.asScala
    true
  }

  override def removeProperties(namespace: Namespace, properties: util.Set[String]): Boolean = {
    namespaces(namespace).props --= properties.asScala
    true
  }
}

object MemoryCatalog {
  def isChild(parent: Namespace, child: Namespace): Boolean = {
    if (parent.length() != (child.length() - 1)) {
      false
    } else {
      (0 until parent.length()).forall(i => parent.level(i) == child.level(i))
    }
  }
}
