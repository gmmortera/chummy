import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import play.api.libs.json._
import java.util.UUID
import java.time.Instant
import models.domain.{ User, LoginData }

class UserDomainTest extends AnyFlatSpec with Matchers {
  val email = "test@gmail.com"
  val password = "password123"

  "User" should "be created with valid data" in {
    val id = UUID.randomUUID
    val createdAt = Instant.now

    val user = User(id, email, password, createdAt)

    user.id should be (id)
    user.email should be (email)
    user.password should be (password)
    user.createdAt should be (createdAt)
  }

  it should "be correctly serialized to JSON" in {
    val user = TestFixtures.createUser(email, password)

    val json = Json.toJson(user)

    (json \ "id").as[UUID] should be (user.id)
    (json \ "email").as[String] should be (user.email)
    json.toString should not include("password")
    json.toString should not include("createdAt")
  }

  it should "be correctly deserialized from JSON" in {
    val json = Json.obj(
      "email" -> email,
      "password" -> password
    )

    val user = json.as[User]

    user.id should not be (null)
    user.email should be (email)
    user.password should be (password)
    user.createdAt should not be (null)
  }

  "LoginData" should "be created with valid data" in {
    val login = LoginData(email, password)

    login.email should be (email)
    login.password should be (password)
  }

  it should "be correctly serialized from JSON" in {
    val json = Json.obj(
      "email" -> email,
      "password" -> password
    )

    val login = json.as[LoginData]

    login.email should be (email)
    login.password should be (password)
  }


}

object TestFixtures {
  def createUser(email: String, password: String): User = new User(
    UUID.randomUUID,
    email,
    password,
    Instant.now
  )
}