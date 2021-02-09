package models

import java.time.ZonedDateTime

import scalikejdbc._, jsr310._ // java.time.ZonedDateTimeを利用するため、jsr310は手動でインポートしてください。
import skinny.orm._

/**
 * Task
 */
case class Task(id: Option[Long], status: Option[String],body: String, createAt: ZonedDateTime, updateAt: ZonedDateTime)

object Task extends SkinnyCRUDMapper[Task] {

  override def tableName = "tasks"

  override def defaultAlias: Alias[Task] = createAlias("m")

  override def extract(rs: WrappedResultSet, n: ResultName[Task]): Task =
    autoConstruct(rs, n)

  private def toNamedValues(record: Task): Seq[(Symbol, Any)] = Seq(
    'status    -> record.status,
    'body     -> record.body,
    'createAt -> record.createAt,
    'updateAt -> record.updateAt
  )

  def create(task: Task)(implicit session: DBSession): Long =
    createWithAttributes(toNamedValues(task): _*)

  def update(task: Task)(implicit session: DBSession): Int =
    updateById(task.id.get).withAttributes(toNamedValues(task): _*)

}