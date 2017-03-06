package org.lolhens.satip.satip

/**
  * Created by pierr on 25.02.2017.
  */
class SatIpQuery {

}

object SatIpQuery {

  case class Option(name: String, attribute: String, value: String)

  object Option {

    class FrontendIdentifier(val feID: Int) extends Option("Frontend Identifier", "fe", feID.toString) {
      require(feID >= 1 && feID <= 65535)
    }

    class SignalSource(val srcID: Int = 1) extends Option("Signal Source", "src", srcID.toString) {
      require(srcID >= 1 && srcID <= 255)
    }

    class Frequency(val frequency: Double) extends Option("Frequency", "freq",
      if (frequency == frequency.toInt) frequency.toInt.toString
      else frequency.toString
    )

    class Polarisation(val polarisation: String) extends Option("Polarisation", "pol", polarisation)

    object Polarisation {

      object HorizontalLinear extends Polarisation("h")

      object VerticalLinear extends Polarisation("v")

      object CircularLeft extends Polarisation("l")

      object CircularRight extends Polarisation("r")

    }

    class RollOff(val roll_off: String) extends Option("Roll-Off", "ro", roll_off.toString)

    object RollOff {

      object `0.35` extends RollOff("0.35")

      object `0.25` extends RollOff("0.25")

      object `0.20` extends RollOff("0.20")

    }

    class ModulationSystem(val system: String) extends Option("Modulation system", "msys", system)

    object ModulationSystem {

      object dvbs extends ModulationSystem("dvbs")

      object dvbs2 extends ModulationSystem("dvbs2")

    }

    class ModulationType(val `type`: String) extends Option("Modulation type", "mtype", `type`)

    object ModulationType {

      object qpsk extends ModulationType("qpsk")

      object `8psk` extends ModulationType("8psk")

    }

    class PilotTones(val pilots: Boolean) extends Option("Pilot tones", "plts", if (pilots) "on" else "off")

    class SymbolRate(val symbol_rate: Int) extends Option("Symbol rate", "sr", symbol_rate.toString)

    class FECInner(val fec_inner: Int) extends Option("FEC inner", "fec", fec_inner.toString)

    case class PID(value: String)

    object PID {

      class Num(pid: Int) extends PID(pid.toString)

      object All extends PID("all")

      object None extends PID("none")

    }

    class ListPIDs(val pids: List[PID]) extends Option("List of PIDs", "pids", pids.map(_.value).mkString(","))

    class AddPIDs(val pids: List[PID]) extends Option("Open PID filters", "addpids", pids.map(_.value).mkString(","))

    class DelPIDs(val pids: List[PID]) extends Option("Remove PID filters", "delpids", pids.map(_.value).mkString(","))

  }

}
