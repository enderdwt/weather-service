package controllers

import javax.inject._
import play.api.libs.json.Json
import play.api.mvc._
import weather.WeatherClient
import weather.Weather._

import scala.concurrent.ExecutionContext

class WeatherController @Inject()(cc: ControllerComponents, weatherClient: WeatherClient)(implicit ec: ExecutionContext) extends AbstractController(cc) {

  def getWeather(latitude: Double, longitude: Double) = Action.async { implicit request: Request[AnyContent] =>
    weatherClient.getWeather(latitude, longitude).map { weather =>
      Ok(Json.toJson(weather))
    }.recover {
      case _ =>
        InternalServerError("Unable to get weather.")
    }
  }
}
