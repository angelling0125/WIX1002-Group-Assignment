public class WeatherExtraction {

    public static String getTodayWeather(String locationEncoded) throws Exception {
        API api = new API();

        String url =
            "https://api.data.gov.my/weather/forecast/"
            + "?contains=" + locationEncoded
            + "@location__location_name"
            + "&sort=date&limit=1";

        String response = api.get(url);

        String summmaryForecast = extractSummaryForecast(response);

        return mapWeather(summmaryForecast);
    }

    private static String extractSummaryForecast(String json) {
        String key = "\"summary_forecast\"";
        int keyIndex = json.indexOf(key);

        if (keyIndex == -1) return "Unknown";

        int colon = json.indexOf(":", keyIndex);
        int firstQuote = json.indexOf("\"", colon + 1);
        int secondQuote = json.indexOf("\"", firstQuote + 1);

        if (firstQuote == -1 || secondQuote == -1) return "Unknown";

        return json.substring(firstQuote + 1, secondQuote);
    }

    private static String mapWeather (String summary) {
        if (summary == null) {
            return "Unknown";
        }

        summary = summary.toLowerCase();   

        if (summary.contains("ribut petir")) {
            return "Stormy";
        } else if (summary.contains("tiada hujan")) {
            return "Sunny";
        } else if (summary.contains("hujan")) {
            return "Rainy";
        } else if (summary.contains("berjerebu")) {
            return "Hazy";
        } else {
            return "Unknown";
        }
    }

}
