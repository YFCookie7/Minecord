import requests

url = "http://localhost:5000/discord_bot"


payload = {"event": "an event", "player": "a player", "content": "qwdqwujidhqwouidhw"}
response = requests.post(url, json=payload)
print(response.text)
