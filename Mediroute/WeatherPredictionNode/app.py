from flask import Flask, render_template, jsonify, request
import random
import datetime

app = Flask(__name__)

# Basic mock data generator based on location
def get_mock_weather(city):
    base_temp = random.randint(10, 35)
    conditions = ["Clear", "Cloudy", "Rainy", "Stormy", "Partly Cloudy"]
    condition = random.choice(conditions)
    
    icon_map = {
        "Clear": "sun",
        "Cloudy": "cloud",
        "Partly Cloudy": "cloud-sun",
        "Rainy": "cloud-rain",
        "Stormy": "cloud-showers-heavy"
    }
    
    # Calculate mock hourly forecast
    hourly = []
    now = datetime.datetime.now()
    for i in range(12):
        hour_time = now + datetime.timedelta(hours=i)
        hourly.append({
            "time": hour_time.strftime("%H:00"),
            "temp": base_temp + random.randint(-5, 5),
            "icon": random.choice(["sun", "cloud", "cloud-rain", "cloud-showers-heavy", "cloud-sun"])
        })
        
    # Calculate 5-day forecast
    forecast = []
    for i in range(1, 6):
        day_time = now + datetime.timedelta(days=i)
        forecast.append({
            "day": day_time.strftime("%A"),
            "temp": base_temp + random.randint(-8, 8),
            "rain_chance": random.randint(0, 100),
            "wind": round(random.uniform(2.0, 20.0), 1),
            "icon": random.choice(["sun", "cloud", "cloud-rain", "cloud-showers-heavy", "cloud-sun"])
        })
        
    # AI Predictions logic (mock probabilities)
    ai_predictions = {
        "hot_weather": round(random.uniform(10, 90), 1),
        "rainy_weather": round(random.uniform(10, 90), 1),
        "cloudy_weather": round(random.uniform(10, 90), 1),
        "storm_possibility": round(random.uniform(0, 40), 1),
    }

    return {
        "location": city.title(),
        "current": {
            "temp": base_temp,
            "humidity": random.randint(30, 90),
            "wind_speed": round(random.uniform(5.0, 25.0), 1),
            "pressure": random.randint(1000, 1025),
            "uv_index": random.randint(1, 11),
            "sunrise": "06:30 AM",
            "sunset": "07:45 PM",
            "condition": condition,
            "icon": icon_map.get(condition, "sun"),
            "condition_text": f"The AI model predicts {condition.lower()} skies with a high chance of stability for the next 4 hours."
        },
        "hourly": hourly,
        "forecast": forecast,
        "ai_predictions": ai_predictions
    }

@app.route('/')
def index():
    return render_template('index.html')

@app.route('/api/weather')
def get_weather():
    city = request.args.get('city', 'New York')
    if not city:
        city = 'New York'
    data = get_mock_weather(city)
    return jsonify(data)

if __name__ == '__main__':
    app.run(debug=True, port=5000)
