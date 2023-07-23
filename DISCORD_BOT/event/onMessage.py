from discord.ext import commands
import requests

minecraftChannel = 1017700772466663487
url = "http://localhost:5001/minecraft_server"


class onMessage(commands.Cog):
    def __init__(self, bot):
        self.bot = bot

    @commands.Cog.listener()
    async def on_message(self, message):
        if message.author == self.bot.user:
            return

        if message.channel.id == minecraftChannel:
            payload = {"sender": message.author.name, "content": message.content}
            response = requests.post(url, json=payload)
            if response.status_code != 200:
                await message.channel.send(
                    "Failed to send message. Error:", response.text
                )
            return


def setup(bot):
    bot.add_cog(onMessage(bot))
