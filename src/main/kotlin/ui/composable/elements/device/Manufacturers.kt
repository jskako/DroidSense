package ui.composable.elements.device

import com.jskako.droidsense.generated.resources.Res
import com.jskako.droidsense.generated.resources.agenew
import com.jskako.droidsense.generated.resources.ascom
import com.jskako.droidsense.generated.resources.atm
import com.jskako.droidsense.generated.resources.bluebird
import com.jskako.droidsense.generated.resources.casio
import com.jskako.droidsense.generated.resources.cilico
import com.jskako.droidsense.generated.resources.cipher
import com.jskako.droidsense.generated.resources.crosscall
import com.jskako.droidsense.generated.resources.datalogic
import com.jskako.droidsense.generated.resources.denso
import com.jskako.droidsense.generated.resources.elo
import com.jskako.droidsense.generated.resources.fairphone
import com.jskako.droidsense.generated.resources.fcnt
import com.jskako.droidsense.generated.resources.general
import com.jskako.droidsense.generated.resources.getac
import com.jskako.droidsense.generated.resources.google
import com.jskako.droidsense.generated.resources.hanshow
import com.jskako.droidsense.generated.resources.hmd
import com.jskako.droidsense.generated.resources.honeywell
import com.jskako.droidsense.generated.resources.honor
import com.jskako.droidsense.generated.resources.imin
import com.jskako.droidsense.generated.resources.imozen
import com.jskako.droidsense.generated.resources.isafe
import com.jskako.droidsense.generated.resources.keyence
import com.jskako.droidsense.generated.resources.kyocera
import com.jskako.droidsense.generated.resources.lenovo
import com.jskako.droidsense.generated.resources.m3
import com.jskako.droidsense.generated.resources.malata
import com.jskako.droidsense.generated.resources.meferi
import com.jskako.droidsense.generated.resources.mobiwire
import com.jskako.droidsense.generated.resources.motorola
import com.jskako.droidsense.generated.resources.newland
import com.jskako.droidsense.generated.resources.oneplus
import com.jskako.droidsense.generated.resources.oppo
import com.jskako.droidsense.generated.resources.orange
import com.jskako.droidsense.generated.resources.panasonic
import com.jskako.droidsense.generated.resources.pepperl
import com.jskako.droidsense.generated.resources.point
import com.jskako.droidsense.generated.resources.positivo
import com.jskako.droidsense.generated.resources.prodvx
import com.jskako.droidsense.generated.resources.realme
import com.jskako.droidsense.generated.resources.samsung
import com.jskako.droidsense.generated.resources.seuic
import com.jskako.droidsense.generated.resources.sharp
import com.jskako.droidsense.generated.resources.social
import com.jskako.droidsense.generated.resources.sonim
import com.jskako.droidsense.generated.resources.spectralink
import com.jskako.droidsense.generated.resources.tcl
import com.jskako.droidsense.generated.resources.transsion
import com.jskako.droidsense.generated.resources.ubiqconn
import com.jskako.droidsense.generated.resources.unitech
import com.jskako.droidsense.generated.resources.urovo
import com.jskako.droidsense.generated.resources.vivo
import com.jskako.droidsense.generated.resources.wuxi
import com.jskako.droidsense.generated.resources.xiaomi
import com.jskako.droidsense.generated.resources.zebra
import com.jskako.droidsense.generated.resources.zte
import org.jetbrains.compose.resources.DrawableResource

enum class Manufacturers {
    PANASONIC {
        override fun drawable() = Res.drawable.panasonic
    },
    POSITIVO {
        override fun drawable() = Res.drawable.positivo
    },
    XIAOMI {
        override fun drawable() = Res.drawable.xiaomi
    },
    CROSSCALL {
        override fun drawable() = Res.drawable.crosscall
    },
    FCNT {
        override fun drawable() = Res.drawable.fcnt
    },
    GOOGLE {
        override fun drawable() = Res.drawable.google
    },
    HMD {
        override fun drawable() = Res.drawable.hmd
    },
    HONEYWELL {
        override fun drawable() = Res.drawable.honeywell
    },
    KYOCERA {
        override fun drawable() = Res.drawable.kyocera
    },
    LENOVO {
        override fun drawable() = Res.drawable.lenovo
    },
    MOTOROLA {
        override fun drawable() = Res.drawable.motorola
    },
    ONEPLUS {
        override fun drawable() = Res.drawable.oneplus
    },
    OPPO {
        override fun drawable() = Res.drawable.oppo
    },
    ORANGE {
        override fun drawable() = Res.drawable.orange
    },
    SAMSUNG {
        override fun drawable() = Res.drawable.samsung
    },
    SHARP {
        override fun drawable() = Res.drawable.sharp
    },
    SOCIAL {
        override fun drawable() = Res.drawable.social
    },
    VIVO {
        override fun drawable() = Res.drawable.vivo
    },
    ZEBRA {
        override fun drawable() = Res.drawable.zebra
    },
    AGENEW {
        override fun drawable() = Res.drawable.agenew
    },
    ASCOM {
        override fun drawable() = Res.drawable.ascom
    },
    ATM {
        override fun drawable() = Res.drawable.atm
    },
    BLUEBIRD {
        override fun drawable() = Res.drawable.bluebird
    },
    CASIO {
        override fun drawable() = Res.drawable.casio
    },
    CILICO {
        override fun drawable() = Res.drawable.cilico
    },
    CIPHER {
        override fun drawable() = Res.drawable.cipher
    },
    DATALOGIC {
        override fun drawable() = Res.drawable.datalogic
    },
    DENSO {
        override fun drawable() = Res.drawable.denso
    },
    ELO {
        override fun drawable() = Res.drawable.elo
    },
    FAIRPHONE {
        override fun drawable() = Res.drawable.fairphone
    },
    GETAC {
        override fun drawable() = Res.drawable.getac
    },
    HANSHOW {
        override fun drawable() = Res.drawable.hanshow
    },
    HONOR {
        override fun drawable() = Res.drawable.honor
    },
    SAFE {
        override fun drawable() = Res.drawable.isafe
    },
    IMIN {
        override fun drawable() = Res.drawable.imin
    },
    IMOZEN {
        override fun drawable() = Res.drawable.imozen
    },
    KEYENCE {
        override fun drawable() = Res.drawable.keyence
    },
    M3 {
        override fun drawable() = Res.drawable.m3
    },
    MEFERI {
        override fun drawable() = Res.drawable.meferi
    },
    MOBIWIRE {
        override fun drawable() = Res.drawable.mobiwire
    },
    NEWLAND {
        override fun drawable() = Res.drawable.newland
    },
    PEPPERL {
        override fun drawable() = Res.drawable.pepperl
    },
    POINT {
        override fun drawable() = Res.drawable.point
    },
    REALME {
        override fun drawable() = Res.drawable.realme
    },
    SEUIC {
        override fun drawable() = Res.drawable.seuic
    },
    MALATA {
        override fun drawable() = Res.drawable.malata
    },
    SONIM {
        override fun drawable() = Res.drawable.sonim
    },
    SPECTRALINK {
        override fun drawable() = Res.drawable.spectralink
    },
    TCL {
        override fun drawable() = Res.drawable.tcl
    },
    TRANSSION {
        override fun drawable() = Res.drawable.transsion
    },
    UNITECH {
        override fun drawable() = Res.drawable.unitech
    },
    UROVO {
        override fun drawable() = Res.drawable.urovo
    },
    WUXI {
        override fun drawable() = Res.drawable.wuxi
    },
    ZTE {
        override fun drawable() = Res.drawable.zte
    },
    PRODVX {
        override fun drawable() = Res.drawable.prodvx
    },
    UBIQCONN {
        override fun drawable() = Res.drawable.ubiqconn
    },
    GENERAL {
        override fun drawable() = Res.drawable.general
    };

    abstract fun drawable(): DrawableResource

    companion object {
        fun getManufacturer(name: String): Manufacturers {
            return entries.firstOrNull { it.name.contains(name, ignoreCase = true) } ?: GENERAL
        }
    }
}
