# Weather Service

### Dependencies
To run this application you will need have sbt and java 11 installed.

### Setup
This application needs an api key to connect to openweathermap.org. To configure this application to use your api key you will need to pass it in as the WEATHER_SERVICE_API_KEY environment variable.

### Running the application

Run the following command to start the application:

```
sbt run
```

Once you see the following the server has started:
```
[info] p.c.s.AkkaHttpServer - Listening for HTTP on /[0:0:0:0:0:0:0:0]:9000
```

To access the weather rest service you will need to hit the following url (you will need to provide the latitude and longitude in the url):
```
http://127.0.0.1:9000/weather/latitude/${latitude}/longitude/${longitude}
```

The response will look like this:

```json
{
  "condition":"Clouds",
  "temperature":"hot",
  "alerts":[
    {
      "event":"Aviso de Ciclón Tropical en el Pacífico",
      "description":"El Huracán \"Enrique\" de categoría 1 en la escala de Saffir-Simpson, ocasiona lluvias intensas a puntuales extraordinarias, vientos muy fuertes y oleaje elevado sobre el occidente y sur del territorio nacional. Los datos contenidos en el presente documento, se determinaron con base en la información de modelos numéricos de fenómenos hidrometeorológicos con los que cuenta la Comisión Nacional del Agua, por lo que al ser los mismos variables, no es posible determinar con exactitud su ocurrencia y magnitud. Las medidas para la prevención y mitigación de sus efectos son emitidas por las autoridades de protección civil.",
      "active":true
    }
  ]
}
```
