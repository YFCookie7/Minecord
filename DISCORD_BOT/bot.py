import discord
import os
from dotenv import load_dotenv
from flask import Flask, request
import asyncio
import threading

load_dotenv()
bot = discord.Bot()
intents = discord.Intents.all()
bot = discord.Bot(intents=intents)
app = Flask(__name__)
minecraftChannel = 1017700772466663487


async def send_message_with_timeout(channel, content, timeout):
    try:
        await asyncio.wait_for(channel.send(content), timeout=timeout)
        return True
    except asyncio.TimeoutError:
        return False


async def send_embed_with_timeout(channel, embed, timeout):
    try:
        await asyncio.wait_for(channel.send(embed=embed), timeout=timeout)
        return True
    except asyncio.TimeoutError:
        return False


# Listen POST request from minecraft server
@app.route("/discord_bot", methods=["POST"])
def receive_message():
    # Get message content
    event = request.json.get("event")
    player = request.json.get("player")
    content = request.json.get("content")

    # Send message to Discord channel
    channel = bot.get_channel(minecraftChannel)
    if event:
        if event == "playerJoin":
            msg = f"**{player}** joined the game!"
            embed = discord.Embed(description=msg, color=discord.Color.green())
            future = asyncio.run_coroutine_threadsafe(
                send_embed_with_timeout(channel, embed, 5), loop
            )
            return "Message sent to Discord channel!"
        elif event == "playerLeft":
            msg = f"**{player}** left the game!"
            embed = discord.Embed(description=msg, color=discord.Color.yellow())
            future = asyncio.run_coroutine_threadsafe(
                send_embed_with_timeout(channel, embed, 5), loop
            )
            return "Message sent to Discord channel!"
        elif event == "playerDeath":
            msg = f"**What a noob! {content}**"
            embed = discord.Embed(description=msg, color=discord.Color.red())
            future = asyncio.run_coroutine_threadsafe(
                send_embed_with_timeout(channel, embed, 5), loop
            )
            return "Message sent to Discord channel!"
        elif event == "playerChat":
            msg = f"{player}: {content}"
            future = asyncio.run_coroutine_threadsafe(
                send_message_with_timeout(channel, msg, 5), loop
            )
            return "Message sent to Discord channel!"
        elif event == "serverStart":
            msg = f"**Server has started !!**"
            embed = discord.Embed(description=msg, color=discord.Color.blue())
            future = asyncio.run_coroutine_threadsafe(
                send_embed_with_timeout(channel, embed, 5), loop
            )
            return "Message sent to Discord channel!"
        elif event == "serverStop":
            msg = f"**Server has stopped !!**"
            embed = discord.Embed(description=msg, color=discord.Color.blue())
            future = asyncio.run_coroutine_threadsafe(
                send_embed_with_timeout(channel, embed, 5), loop
            )
            return "Message sent to Discord channel!"

        # future = asyncio.run_coroutine_threadsafe(
        #     send_message_with_timeout(channel, content, 5), loop
        # )
        if future.result():
            return "Message sent to Discord channel!"
        else:
            return "Sending message timed out!"
    else:
        return "Invalid request data!"


def load_extensions(directory):
    for filename in os.listdir("./" + directory):
        if filename.endswith(".py"):
            bot.load_extension(f"{directory}.{filename[:-3]}")


async def run_bot():
    await bot.start(os.getenv("DISCORD_TOKEN"))


def run_flask():
    app.run(host="0.0.0.0", port=5000)


if __name__ == "__main__":
    load_extensions("event")
    load_extensions("command")

    loop = asyncio.get_event_loop()
    loop.create_task(run_bot())
    threading.Thread(target=run_flask).start()
    loop.run_forever()
