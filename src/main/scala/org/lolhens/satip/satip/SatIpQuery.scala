package org.lolhens.satip.satip

import org.lolhens.satip.satip.SatIpQuery.Parameter

/**
  * Created by pierr on 25.02.2017.
  */
case class SatIpQuery(params: Parameter*) {
  override def toString: String = params.mkString("&")
}

object SatIpQuery {

  case class Parameter(name: String, attribute: String, value: String) {
    override def toString: String = s"$attribute=$value"
  }

  object Parameter {

    class FrontendIdentifier(val feID: Int) extends Parameter("Frontend Identifier", "fe", feID.toString) {
      require(feID >= 1 && feID <= 65535)
    }

    object FrontendIdentifier {
      def apply(feID: Int) = new FrontendIdentifier(feID)
    }


    class SignalSource(val srcID: Int = 1) extends Parameter("Signal Source", "src", srcID.toString) {
      require(srcID >= 1 && srcID <= 255)
    }

    object SignalSource {
      def apply(srcID: Int = 1) = new SignalSource(srcID)
    }


    class Frequency(val frequency: Double) extends Parameter("Frequency", "freq",
      if (frequency == frequency.toInt) frequency.toInt.toString
      else frequency.toString
    )

    object Frequency {
      def apply(frequency: Double) = new Frequency(frequency)
    }


    class Polarisation(val polarisation: String) extends Parameter("Polarisation", "pol", polarisation)

    object Polarisation {

      object HorizontalLinear extends Polarisation("h")

      object VerticalLinear extends Polarisation("v")

      object CircularLeft extends Polarisation("l")

      object CircularRight extends Polarisation("r")

    }


    class RollOff(val roll_off: String) extends Parameter("Roll-Off", "ro", roll_off.toString)

    object RollOff {

      object `0.35` extends RollOff("0.35")

      object `0.25` extends RollOff("0.25")

      object `0.20` extends RollOff("0.20")

    }


    class ModulationSystem(val system: String) extends Parameter("Modulation system", "msys", system)

    object ModulationSystem {

      object dvbs extends ModulationSystem("dvbs")

      object dvbs2 extends ModulationSystem("dvbs2")

    }


    class ModulationType(val `type`: String) extends Parameter("Modulation type", "mtype", `type`)

    object ModulationType {

      object qpsk extends ModulationType("qpsk")

      object `8psk` extends ModulationType("8psk")

    }


    class PilotTones(val pilots: Boolean) extends Parameter("Pilot tones", "plts", if (pilots) "on" else "off")

    object PilotTones {
      def apply(pilots: Boolean) = new PilotTones(pilots)
    }


    class SymbolRate(val symbol_rate: Int) extends Parameter("Symbol rate", "sr", symbol_rate.toString)

    object SymbolRate {
      def apply(symbol_rate: Int) = new SymbolRate(symbol_rate)
    }


    class FECInner(val fec_inner: Int) extends Parameter("FEC inner", "fec", fec_inner.toString)

    object FECInner {
      def apply(fec_inner: Int) = new FECInner(fec_inner)
    }


    case class PID(value: String)

    object PID {

      class Num(pid: Int) extends PID(pid.toString)

      object Num {
        def apply(pid: Int) = new Num(pid)
      }

      object All extends PID("all")

      object None extends PID("none")

    }


    class ListPIDs(val pids: List[PID]) extends Parameter("List of PIDs", "pids", pids.map(_.value).mkString(","))

    object ListPIDs {
      def apply(pids: PID*) = new ListPIDs(pids.toList)
    }

    class AddPIDs(val pids: List[PID]) extends Parameter("Open PID filters", "addpids", pids.map(_.value).mkString(","))

    object AddPIDs {
      def apply(pids: PID*) = new AddPIDs(pids.toList)
    }

    class DelPIDs(val pids: List[PID]) extends Parameter("Remove PID filters", "delpids", pids.map(_.value).mkString(","))

    object DelPIDs {
      def apply(pids: PID*) = new DelPIDs(pids.toList)
    }

  }

}
