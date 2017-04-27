package com

import com.round.system.core.ProtocolBufferSupport

import scalaz.ValidationNel
import scalaz.syntax.ToNelOps
import scalaz.syntax.ToValidationOps

package object round extends ProtocolBufferSupport with ToValidationOps with ToNelOps {

  type -> [+A, +B] = (A, B)
  object -> { @inline def unapply[A, B](t: A -> B): Option[A -> B] = Tuple2.unapply(t) }

  type Validation[+A] = ValidationNel[String, A]
}
