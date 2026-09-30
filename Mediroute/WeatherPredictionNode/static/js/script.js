document.addEventListener('DOMContentLoaded', () => {
    const heroSection = document.getElementById('hero');
    const dashboardSection = document.getElementById('dashboard');
    const getStartedBtn = document.getElementById('get-started-btn');
    const loader = document.getElementById('loader');
    const searchBtn = document.getElementById('search-btn');
    const citySearch = document.getElementById('city-search');
    const autoDetectBtn = document.getElementById('auto-detect-btn');
    const toggleThemeBtn = document.getElementById('toggle-theme');

    let hourlyChartInstance = null;

    // Transition from Hero to Dashboard
    getStartedBtn.addEventListener('click', () => {
        heroSection.classList.add('hidden');
        dashboardSection.classList.remove('hidden');
        fetchWeatherData('New York'); // default initial load
    });

    // Search functionality
    searchBtn.addEventListener('click', () => {
        if(citySearch.value.trim() !== '') {
            fetchWeatherData(citySearch.value.trim());
        }
    });

    citySearch.addEventListener('keypress', (e) => {
        if(e.key === 'Enter' && citySearch.value.trim() !== '') {
            fetchWeatherData(citySearch.value.trim());
        }
    });

    // Auto Detect Location
    autoDetectBtn.addEventListener('click', () => {
        if (navigator.geolocation) {
            loader.classList.remove('hidden');
            // Mock geolocation for presentation
            setTimeout(() => {
                fetchWeatherData('San Francisco'); // Mock detected location
            }, 1000);
        }
    });

    // Theme Toggle
    let isDark = true;
    toggleThemeBtn.addEventListener('click', () => {
        isDark = !isDark;
        if(isDark) {
            document.documentElement.style.setProperty('--bg-gradient', 'linear-gradient(135deg, #0A192F 0%, #020C1B 100%)');
            document.documentElement.style.setProperty('--text-main', '#E6F1FF');
            document.documentElement.style.setProperty('--card-bg', 'rgba(16, 33, 65, 0.6)');
            toggleThemeBtn.innerHTML = '<i class="fa-solid fa-moon"></i>';
        } else {
            document.documentElement.style.setProperty('--bg-gradient', 'linear-gradient(135deg, #e0eafc 0%, #cfdef3 100%)');
            document.documentElement.style.setProperty('--text-main', '#0f172a');
            document.documentElement.style.setProperty('--card-bg', 'rgba(255, 255, 255, 0.6)');
            toggleThemeBtn.innerHTML = '<i class="fa-solid fa-sun"></i>';
        }
    });

    // Fetch Weather Data API
    async function fetchWeatherData(city) {
        loader.classList.remove('hidden');
        try {
            const response = await fetch(`/api/weather?city=${encodeURIComponent(city)}`);
            const data = await response.json();
            
            updateDashboard(data);
        } catch (error) {
            console.error("Error fetching weather data:", error);
            alert("Failed to fetch weather data. Please try again.");
        } finally {
            setTimeout(() => {
                loader.classList.add('hidden');
            }, 500);
        }
    }

    function updateDashboard(data) {
        // Current Info
        document.getElementById('current-location').innerText = data.location;
        document.getElementById('current-temp').innerText = `${data.current.temp}°`;
        document.getElementById('current-condition').innerText = data.current.condition;
        document.getElementById('condition-text').innerText = data.current.condition_text;
        
        document.getElementById('current-icon').className = `fa-solid fa-${data.current.icon} weather-icon-large`;
        
        document.getElementById('humidity-val').innerText = `${data.current.humidity}%`;
        document.getElementById('wind-val').innerText = `${data.current.wind_speed} km/h`;
        document.getElementById('pressure-val').innerText = `${data.current.pressure} hPa`;
        document.getElementById('uv-val').innerText = data.current.uv_index;

        // Sun
        document.getElementById('sunrise-val').innerText = data.current.sunrise;
        document.getElementById('sunset-val').innerText = data.current.sunset;

        // AI Predictions
        updateProgressBar('prob-hot', 'val-hot', data.ai_predictions.hot_weather);
        updateProgressBar('prob-rain', 'val-rain', data.ai_predictions.rainy_weather);
        updateProgressBar('prob-cloud', 'val-cloud', data.ai_predictions.cloudy_weather);
        updateProgressBar('prob-storm', 'val-storm', data.ai_predictions.storm_possibility);

        // Render Hourly Chart
        renderHourlyChart(data.hourly);

        // Render 5-Day Forecast
        renderForecast(data.forecast);
    }

    function updateProgressBar(barId, valId, value) {
        const bar = document.getElementById(barId);
        const text = document.getElementById(valId);
        
        setTimeout(() => {
            bar.style.width = `${value}%`;
            text.innerText = `${value}%`;
        }, 300); // slight delay for animation
    }

    function renderHourlyChart(hourlyData) {
        const ctx = document.getElementById('hourlyChart').getContext('2d');
        
        const labels = hourlyData.map(h => h.time);
        const dataPoints = hourlyData.map(h => h.temp);

        if(hourlyChartInstance) {
            hourlyChartInstance.destroy();
        }

        hourlyChartInstance = new Chart(ctx, {
            type: 'line',
            data: {
                labels: labels,
                datasets: [{
                    label: 'Temperature (°C)',
                    data: dataPoints,
                    borderColor: '#64FFDA',
                    backgroundColor: 'rgba(100, 255, 218, 0.1)',
                    borderWidth: 2,
                    fill: true,
                    tension: 0.4,
                    pointBackgroundColor: '#00B4D8',
                    pointBorderColor: '#fff',
                    pointBorderWidth: 2,
                    pointRadius: 4,
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: { display: false }
                },
                scales: {
                    y: {
                        grid: { color: 'rgba(255,255,255,0.05)' },
                        ticks: { color: '#8892B0' }
                    },
                    x: {
                        grid: { display: false },
                        ticks: { color: '#8892B0' }
                    }
                }
            }
        });
    }

    function renderForecast(forecastData) {
        const list = document.getElementById('forecast-list');
        list.innerHTML = '';

        forecastData.forEach(day => {
            const item = document.createElement('div');
            item.className = 'forecast-item';
            item.innerHTML = `
                <span class="forecast-day">${day.day.substring(0,3)}</span>
                <i class="fa-solid fa-${day.icon} forecast-icon"></i>
                <span class="forecast-temp">${day.temp}°</span>
                <div class="forecast-extra">
                    <span><i class="fa-solid fa-droplet"></i> ${day.rain_chance}%</span>
                    <span><i class="fa-solid fa-wind"></i> ${day.wind}km/h</span>
                </div>
            `;
            list.appendChild(item);
        });
    }
});
