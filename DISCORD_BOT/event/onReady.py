from discord.ext import commands

targetChannel = 1017700772466663487


class onReady(commands.Cog):
    def __init__(self, bot):
        self.bot = bot

    @commands.Cog.listener()
    async def on_ready(self):
        print(f"{self.bot.user} is ready and online!")
        channel = self.bot.get_channel(targetChannel)
        await channel.send("Bot is ready!")


def setup(bot):
    bot.add_cog(onReady(bot))
