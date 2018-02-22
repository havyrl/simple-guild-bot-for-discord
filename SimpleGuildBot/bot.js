'use strict';
const Discord = require("discord.js");
const logger = require('winston');
const auth = require('./auth.json');
const flag_send_text = 2048;
const flag_speak = 2097152;

// Configure logger settings
logger.remove(logger.transports.Console);
logger.add(logger.transports.Console, {
    colorize: true
});
logger.level = 'debug';

// Initialize Discord Bot
const bot = new Discord.Client();
const defaultTextChannels = new Map();
const defaultVoiceChannels = new Map();

bot.on('ready', () => {
    console.log(`Logged in as ${bot.user.tag}!`);
    
    bot.channels.forEach((channel, key) => {
        let hasPermission = false;
        switch (channel.type) {
            case "text":
                channel.permissionOverwrites.forEach((permission, key) => {
                    if (!hasPermission && permission.type === "member" && permission.id === bot.user.id && permission.deny === 0 && permission.allow & flag_send_text) {
                        hasPermission = true;
                    }
                });
                if (hasPermission) {
                    defaultTextChannels.set(channel.guild.id, channel);
                }
            case "voice":
                channel.permissionOverwrites.forEach((permission, key) => {
                    if (!hasPermission && permission.type === "member" && permission.id === bot.user.id && permission.deny === 0 && permission.allow & flag_speak) {
                        hasPermission = true;
                    }
                });
                if (hasPermission) {
                    defaultVoiceChannels.set(channel.guild.id, channel);
                }
        }
    });
});

bot.on('voiceStateUpdate', (oldStatus, newStatus) => {
    const guildId = oldStatus.guild.id || newStatus.guild.id;
    const defaultTextChannel = defaultTextChannels.get(guildId);
    if (defaultTextChannel == null) {
        return;
    }
    const username = oldStatus.user.username || newStatus.user.username;
    const oldChannelId = oldStatus.voiceChannelID;
    const oldChannel = bot.channels.get(oldChannelId);
    const newChannelId = newStatus.voiceChannelID;
    const newChannel = bot.channels.get(newChannelId);

    if (oldChannelId == null || oldChannelId == undefined) {
        defaultTextChannel.sendMessage(username + " has joind " + newChannel.name);
    } else if (newChannelId == null || newChannelId == undefined) {
        defaultTextChannel.sendMessage(username + " has left " + oldChannel.name);
    } else {
        defaultTextChannel.sendMessage(username + " has moved from " + oldChannel.name + " to " + newChannel.name);
    }
});

bot.login(auth.token);

