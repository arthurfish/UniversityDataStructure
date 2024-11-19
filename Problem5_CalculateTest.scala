import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

class CalculateTest extends AnyFlatSpec with should.Matchers {
  "It" should "support plus" in {
    for
      a <- (9990 to 9999)
      b <- (9990 to 9999)
    do
      assert((a + b).toString ===
        (Expr4_ArbitraryPrecisionNumber.fromString(a.toString)
          + Expr4_ArbitraryPrecisionNumber.fromString(b.toString)).toString)
  }
}
