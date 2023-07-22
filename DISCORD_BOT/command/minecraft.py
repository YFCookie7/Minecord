import discord
from discord.ext import commands


class Minecraft(commands.Cog):
    def __init__(self, bot):
        self.bot = bot

    @discord.slash_command(description="Tutorial for interacting with the bot")
    async def help(self, ctx):
        await ctx.defer()
        help_message = "This is a helpless help message"
        await ctx.respond(help_message)


def setup(bot):
    bot.add_cog(Minecraft(bot))
