package org.lolhens.satip.satip

/**
  * Created by pierr on 23.10.2016.
  */
case class Tuner(name: String)

object Tuner {

  object dvbs extends Tuner("dvbs")

  object dvbs2 extends Tuner("dvbs2")

  object dvbt extends Tuner("dvbt")

  object dvbt2 extends Tuner("dvbt2")

  object dvbc extends Tuner("dvbc")

  object dvbc2 extends Tuner("dvbc2")

  val values = List(dvbs, dvbs2, dvbt, dvbt2, dvbc, dvbc2)

  lazy val valueMap: Map[String, Tuner] = values.map(e => (e.name, e)).toMap
}