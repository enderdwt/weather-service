package weather

import play.api.Configuration
import play.api.libs.json.Json

import javax.inject.Inject
import play.api.libs.ws._

import scala.concurrent.ExecutionContext

object ExternalAlert {
  implicit val externalAlertFormat = Json.format[ExternalAlert]
}
case class ExternalAlert(start: Long, end: Long, event: String, description: String)

object Alert {
  implicit val alertFormat = Json.format[Alert]
}
case class Alert(event: String, description: String, active: Boolean)

object Weather {
  implicit val weatherFormat = Json.format[Weather]
}
case class Weather(condition: String, temperature: String, alerts: Seq[Alert])

class WeatherClient @Inject()(wsClient: WSClient, configuration: Configuration)(implicit ec: ExecutionContext) {

  def getTemp(temp: Double): String =
    if(temp > 75) {
      "hot"
    } else if(temp > 55) {
      "moderate"
    } else {
      "cold"
    }

  def getWeather(latitude: Double, longitude: Double) = {
    wsClient.url(s"${configuration.get[String]("weather.service.api.url")}/data/2.5/onecall")
      .withQueryStringParameters(
        "lat" -> s"${latitude}",
        "lon" -> s"${longitude}",
        "units" -> "imperial",
        "exclude" -> "hourly,daily,minutely",
        "appid" -> configuration.get[String]("weather.service.api.key")
      ).get()
      .map { response =>
        val now = (response.json \ "current" \ "dt").as[Long]
        Weather(
          condition = (response.json \ "current" \ "weather"\ 0 \ "main").as[String],
          temperature = getTemp((response.json \ "current" \ "temp").as[Double]),
          alerts = (response.json \ "alerts").asOpt[Seq[ExternalAlert]].getOrElse(Seq.empty[ExternalAlert]).map { externalAlert =>
            Alert(event = externalAlert.event, description = externalAlert.description, active = now >= externalAlert.start && now <= externalAlert.end)
          }
        )
      }
  }
}
